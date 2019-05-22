package org.apache.maven.plugins.surefire.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TestSuiteXmlParser extends DefaultHandler {
   private ReportTestSuite defaultSuite;
   private ReportTestSuite currentSuite;
   private Map<String, ReportTestSuite> classesToSuites;
   private final NumberFormat numberFormat;
   private StringBuffer currentElement;
   private ReportTestCase testCase;
   private boolean valid;

   public TestSuiteXmlParser() {
      this.numberFormat = NumberFormat.getInstance(Locale.ENGLISH);
   }

   public Collection<ReportTestSuite> parse(String xmlPath) throws ParserConfigurationException, SAXException, IOException {
      File f = new File(xmlPath);
      FileInputStream fileInputStream = new FileInputStream(f);
      InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");

      Collection var5;
      try {
         var5 = this.parse(inputStreamReader);
      } finally {
         inputStreamReader.close();
         fileInputStream.close();
      }

      return var5;
   }

   public Collection<ReportTestSuite> parse(InputStreamReader stream) throws ParserConfigurationException, SAXException, IOException {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      this.valid = true;
      this.classesToSuites = new HashMap();
      saxParser.parse((InputSource)(new InputSource(stream)), (DefaultHandler)this);
      if (this.currentSuite != this.defaultSuite && this.defaultSuite.getNumberOfTests() == 0) {
         this.classesToSuites.remove(this.defaultSuite.getFullClassName());
      }

      return this.classesToSuites.values();
   }

   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      if (this.valid) {
         try {
            String timeAsString;
            String message;
            if ("testsuite".equals(qName)) {
               this.currentSuite = this.defaultSuite = new ReportTestSuite();

               try {
                  Number time = this.numberFormat.parse(attributes.getValue("time"));
                  this.defaultSuite.setTimeElapsed(time.floatValue());
               } catch (NullPointerException var8) {
                  System.err.println("WARNING: no time attribute found on testsuite element");
               }

               if (attributes.getValue("group") != null && !"".equals(attributes.getValue("group"))) {
                  message = attributes.getValue("group");
                  timeAsString = attributes.getValue("name");
                  this.defaultSuite.setFullClassName(message + "." + timeAsString);
               } else {
                  message = attributes.getValue("name");
                  this.defaultSuite.setFullClassName(message);
               }

               this.classesToSuites.put(this.defaultSuite.getFullClassName(), this.defaultSuite);
            } else if ("testcase".equals(qName)) {
               this.currentElement = new StringBuffer();
               this.testCase = new ReportTestCase();
               this.testCase.setName(attributes.getValue("name"));
               message = attributes.getValue("classname");
               if (message != null) {
                  this.currentSuite = (ReportTestSuite)this.classesToSuites.get(message);
                  if (this.currentSuite == null) {
                     this.currentSuite = new ReportTestSuite();
                     this.currentSuite.setFullClassName(message);
                     this.classesToSuites.put(message, this.currentSuite);
                  }
               }

               this.testCase.setFullClassName(this.currentSuite.getFullClassName());
               this.testCase.setClassName(this.currentSuite.getName());
               this.testCase.setFullName(this.currentSuite.getFullClassName() + "." + this.testCase.getName());
               timeAsString = attributes.getValue("time");
               Number time = 0;
               if (timeAsString != null) {
                  time = this.numberFormat.parse(timeAsString);
               }

               this.testCase.setTime(((Number)time).floatValue());
               if (this.currentSuite != this.defaultSuite) {
                  this.currentSuite.setTimeElapsed(((Number)time).floatValue() + this.currentSuite.getTimeElapsed());
               }
            } else if ("failure".equals(qName)) {
               this.testCase.addFailure(attributes.getValue("message"), attributes.getValue("type"));
               this.currentSuite.setNumberOfFailures(1 + this.currentSuite.getNumberOfFailures());
            } else if ("error".equals(qName)) {
               this.testCase.addFailure(attributes.getValue("message"), attributes.getValue("type"));
               this.currentSuite.setNumberOfErrors(1 + this.currentSuite.getNumberOfErrors());
            } else if ("skipped".equals(qName)) {
               message = attributes.getValue("message");
               this.testCase.addFailure(message != null ? message : "skipped", "skipped");
               this.currentSuite.setNumberOfSkipped(1 + this.currentSuite.getNumberOfSkipped());
            } else if ("failsafe-summary".equals(qName)) {
               this.valid = false;
            }

         } catch (ParseException var9) {
            throw new SAXException(var9.getMessage(), var9);
         }
      }
   }

   public void endElement(String uri, String localName, String qName) throws SAXException {
      if ("testcase".equals(qName)) {
         this.currentSuite.getTestCases().add(this.testCase);
      } else {
         Map error;
         if ("failure".equals(qName)) {
            error = this.testCase.getFailure();
            error.put("detail", this.parseCause(this.currentElement.toString()));
         } else if ("error".equals(qName)) {
            error = this.testCase.getFailure();
            error.put("detail", this.parseCause(this.currentElement.toString()));
         } else if ("time".equals(qName)) {
            try {
               Number time = this.numberFormat.parse(this.currentElement.toString());
               this.defaultSuite.setTimeElapsed(time.floatValue());
            } catch (ParseException var5) {
               throw new SAXException(var5.getMessage(), var5);
            }
         }
      }

   }

   public void characters(char[] ch, int start, int length) throws SAXException {
      if (this.valid) {
         String s = new String(ch, start, length);
         if (!"".equals(s.trim())) {
            this.currentElement.append(s);
         }

      }
   }

   private List<String> parseCause(String detail) {
      String fullName = this.testCase.getFullName();
      String name = fullName.substring(fullName.lastIndexOf(".") + 1);
      return this.parseCause(detail, name);
   }

   private List<String> parseCause(String detail, String compareTo) {
      StringTokenizer stringTokenizer = new StringTokenizer(detail, "\n");
      ArrayList parsedDetail = new ArrayList(stringTokenizer.countTokens());

      while(stringTokenizer.hasMoreTokens()) {
         String lineString = stringTokenizer.nextToken().trim();
         parsedDetail.add(lineString);
         if (lineString.contains(compareTo)) {
            break;
         }
      }

      return parsedDetail;
   }

   public boolean isValid() {
      return this.valid;
   }
}
