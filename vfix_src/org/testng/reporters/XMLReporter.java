package org.testng.reporters;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.Map.Entry;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.Reporter;
import org.testng.internal.Utils;
import org.testng.xml.XmlSuite;

public class XMLReporter implements IReporter {
   public static final String FILE_NAME = "testng-results.xml";
   private final XMLReporterConfig config = new XMLReporterConfig();
   private XMLStringBuffer rootBuffer;

   public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
      if (Utils.isStringEmpty(this.config.getOutputDirectory())) {
         this.config.setOutputDirectory(outputDirectory);
      }

      int passed = 0;
      int failed = 0;
      int skipped = 0;
      Iterator i$ = suites.iterator();

      while(i$.hasNext()) {
         ISuite s = (ISuite)i$.next();

         ITestContext testContext;
         for(Iterator i$ = s.getResults().values().iterator(); i$.hasNext(); skipped += testContext.getSkippedTests().size()) {
            ISuiteResult sr = (ISuiteResult)i$.next();
            testContext = sr.getTestContext();
            passed += testContext.getPassedTests().size();
            failed += testContext.getFailedTests().size();
         }
      }

      this.rootBuffer = new XMLStringBuffer();
      Properties p = new Properties();
      p.put("passed", passed);
      p.put("failed", failed);
      p.put("skipped", skipped);
      p.put("total", passed + failed + skipped);
      this.rootBuffer.push("testng-results", p);
      this.writeReporterOutput(this.rootBuffer);
      Iterator i$ = suites.iterator();

      while(i$.hasNext()) {
         ISuite suite = (ISuite)i$.next();
         this.writeSuite(suite.getXmlSuite(), suite);
      }

      this.rootBuffer.pop();
      Utils.writeUtf8File(this.config.getOutputDirectory(), "testng-results.xml", this.rootBuffer, (String)null);
   }

   private void writeReporterOutput(XMLStringBuffer xmlBuffer) {
      xmlBuffer.push("reporter-output");
      List<String> output = Reporter.getOutput();
      Iterator i$ = output.iterator();

      while(i$.hasNext()) {
         String line = (String)i$.next();
         if (line != null) {
            xmlBuffer.push("line");
            xmlBuffer.addCDATA(line);
            xmlBuffer.pop();
         }
      }

      xmlBuffer.pop();
   }

   private void writeSuite(XmlSuite xmlSuite, ISuite suite) {
      switch(this.config.getFileFragmentationLevel()) {
      case 1:
         this.writeSuiteToBuffer(this.rootBuffer, suite);
         break;
      case 2:
      case 3:
         File suiteFile = this.referenceSuite(this.rootBuffer, suite);
         this.writeSuiteToFile(suiteFile, suite);
      }

   }

   private void writeSuiteToFile(File suiteFile, ISuite suite) {
      XMLStringBuffer xmlBuffer = new XMLStringBuffer();
      this.writeSuiteToBuffer(xmlBuffer, suite);
      File parentDir = suiteFile.getParentFile();
      if (parentDir.exists() || suiteFile.getParentFile().mkdirs()) {
         Utils.writeFile(parentDir.getAbsolutePath(), "testng-results.xml", xmlBuffer.toXML());
      }

   }

   private File referenceSuite(XMLStringBuffer xmlBuffer, ISuite suite) {
      String relativePath = suite.getName() + File.separatorChar + "testng-results.xml";
      File suiteFile = new File(this.config.getOutputDirectory(), relativePath);
      Properties attrs = new Properties();
      attrs.setProperty("url", relativePath);
      xmlBuffer.addEmptyElement("suite", attrs);
      return suiteFile;
   }

   private void writeSuiteToBuffer(XMLStringBuffer xmlBuffer, ISuite suite) {
      xmlBuffer.push("suite", this.getSuiteAttributes(suite));
      this.writeSuiteGroups(xmlBuffer, suite);
      Map<String, ISuiteResult> results = suite.getResults();
      XMLSuiteResultWriter suiteResultWriter = new XMLSuiteResultWriter(this.config);
      Iterator i$ = results.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, ISuiteResult> result = (Entry)i$.next();
         suiteResultWriter.writeSuiteResult(xmlBuffer, (ISuiteResult)result.getValue());
      }

      xmlBuffer.pop();
   }

   private void writeSuiteGroups(XMLStringBuffer xmlBuffer, ISuite suite) {
      xmlBuffer.push("groups");
      Map<String, Collection<ITestNGMethod>> methodsByGroups = suite.getMethodsByGroups();
      Iterator i$ = methodsByGroups.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, Collection<ITestNGMethod>> entry = (Entry)i$.next();
         Properties groupAttrs = new Properties();
         groupAttrs.setProperty("name", (String)entry.getKey());
         xmlBuffer.push("group", groupAttrs);
         Set<ITestNGMethod> groupMethods = this.getUniqueMethodSet((Collection)entry.getValue());
         Iterator i$ = groupMethods.iterator();

         while(i$.hasNext()) {
            ITestNGMethod groupMethod = (ITestNGMethod)i$.next();
            Properties methodAttrs = new Properties();
            methodAttrs.setProperty("name", groupMethod.getMethodName());
            methodAttrs.setProperty("signature", groupMethod.toString());
            methodAttrs.setProperty("class", groupMethod.getRealClass().getName());
            xmlBuffer.addEmptyElement("method", methodAttrs);
         }

         xmlBuffer.pop();
      }

      xmlBuffer.pop();
   }

   private Properties getSuiteAttributes(ISuite suite) {
      Properties props = new Properties();
      props.setProperty("name", suite.getName());
      Map<String, ISuiteResult> results = suite.getResults();
      Date minStartDate = new Date();
      Date maxEndDate = null;
      Iterator i$ = results.entrySet().iterator();

      while(true) {
         Date startDate;
         Date endDate;
         do {
            if (!i$.hasNext()) {
               if (maxEndDate == null) {
                  maxEndDate = minStartDate;
               }

               addDurationAttributes(this.config, props, minStartDate, maxEndDate);
               return props;
            }

            Entry<String, ISuiteResult> result = (Entry)i$.next();
            ITestContext testContext = ((ISuiteResult)result.getValue()).getTestContext();
            startDate = testContext.getStartDate();
            endDate = testContext.getEndDate();
            if (minStartDate.after(startDate)) {
               minStartDate = startDate;
            }
         } while(maxEndDate != null && !maxEndDate.before(endDate));

         maxEndDate = endDate != null ? endDate : startDate;
      }
   }

   public static void addDurationAttributes(XMLReporterConfig config, Properties attributes, Date minStartDate, Date maxEndDate) {
      SimpleDateFormat format = new SimpleDateFormat(XMLReporterConfig.getTimestampFormat());
      TimeZone utc = TimeZone.getTimeZone("UTC");
      format.setTimeZone(utc);
      String startTime = format.format(minStartDate);
      String endTime = format.format(maxEndDate);
      long duration = maxEndDate.getTime() - minStartDate.getTime();
      attributes.setProperty("started-at", startTime);
      attributes.setProperty("finished-at", endTime);
      attributes.setProperty("duration-ms", Long.toString(duration));
   }

   private Set<ITestNGMethod> getUniqueMethodSet(Collection<ITestNGMethod> methods) {
      Set<ITestNGMethod> result = new LinkedHashSet();
      Iterator i$ = methods.iterator();

      while(i$.hasNext()) {
         ITestNGMethod method = (ITestNGMethod)i$.next();
         result.add(method);
      }

      return result;
   }

   public int getFileFragmentationLevel() {
      return this.config.getFileFragmentationLevel();
   }

   public void setFileFragmentationLevel(int fileFragmentationLevel) {
      this.config.setFileFragmentationLevel(fileFragmentationLevel);
   }

   public int getStackTraceOutputMethod() {
      return this.config.getStackTraceOutputMethod();
   }

   public void setStackTraceOutputMethod(int stackTraceOutputMethod) {
      this.config.setStackTraceOutputMethod(stackTraceOutputMethod);
   }

   public String getOutputDirectory() {
      return this.config.getOutputDirectory();
   }

   public void setOutputDirectory(String outputDirectory) {
      this.config.setOutputDirectory(outputDirectory);
   }

   public boolean isGenerateGroupsAttribute() {
      return this.config.isGenerateGroupsAttribute();
   }

   public void setGenerateGroupsAttribute(boolean generateGroupsAttribute) {
      this.config.setGenerateGroupsAttribute(generateGroupsAttribute);
   }

   public boolean isSplitClassAndPackageNames() {
      return this.config.isSplitClassAndPackageNames();
   }

   public void setSplitClassAndPackageNames(boolean splitClassAndPackageNames) {
      this.config.setSplitClassAndPackageNames(splitClassAndPackageNames);
   }

   public String getTimestampFormat() {
      XMLReporterConfig var10000 = this.config;
      return XMLReporterConfig.getTimestampFormat();
   }

   public void setTimestampFormat(String timestampFormat) {
      this.config.setTimestampFormat(timestampFormat);
   }

   public boolean isGenerateDependsOnMethods() {
      return this.config.isGenerateDependsOnMethods();
   }

   public void setGenerateDependsOnMethods(boolean generateDependsOnMethods) {
      this.config.setGenerateDependsOnMethods(generateDependsOnMethods);
   }

   public void setGenerateDependsOnGroups(boolean generateDependsOnGroups) {
      this.config.setGenerateDependsOnGroups(generateDependsOnGroups);
   }

   public boolean isGenerateDependsOnGroups() {
      return this.config.isGenerateDependsOnGroups();
   }

   public void setGenerateTestResultAttributes(boolean generateTestResultAttributes) {
      this.config.setGenerateTestResultAttributes(generateTestResultAttributes);
   }

   public boolean isGenerateTestResultAttributes() {
      return this.config.isGenerateTestResultAttributes();
   }
}
