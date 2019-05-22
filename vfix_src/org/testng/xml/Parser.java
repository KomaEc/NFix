package org.testng.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import javax.xml.parsers.ParserConfigurationException;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.xml.sax.SAXException;

public class Parser {
   public static final String TESTNG_DTD = "testng-1.0.dtd";
   public static final String DEPRECATED_TESTNG_DTD_URL = "http://beust.com/testng/testng-1.0.dtd";
   public static final String TESTNG_DTD_URL = "http://testng.org/testng-1.0.dtd";
   public static final String DEFAULT_FILENAME = "testng.xml";
   private static final ISuiteParser DEFAULT_FILE_PARSER = new SuiteXmlParser();
   private static final List<ISuiteParser> PARSERS;
   private String m_fileName;
   private InputStream m_inputStream;
   private IPostProcessor m_postProcessor;
   private boolean m_loadClasses = true;

   public Parser(String fileName) {
      this.init(fileName, (InputStream)null, (IFileParser)null);
   }

   public Parser() throws FileNotFoundException {
      this.init((String)null, (InputStream)null, (IFileParser)null);
   }

   public Parser(InputStream is) {
      this.init((String)null, is, (IFileParser)null);
   }

   private void init(String fileName, InputStream is, IFileParser fp) {
      this.m_fileName = fileName != null ? fileName : "testng.xml";
      this.m_inputStream = is;
   }

   public void setPostProcessor(IPostProcessor processor) {
      this.m_postProcessor = processor;
   }

   public void setLoadClasses(boolean loadClasses) {
      this.m_loadClasses = loadClasses;
   }

   private static IFileParser getParser(String fileName) {
      Iterator i$ = PARSERS.iterator();

      ISuiteParser parser;
      do {
         if (!i$.hasNext()) {
            return DEFAULT_FILE_PARSER;
         }

         parser = (ISuiteParser)i$.next();
      } while(!parser.accept(fileName));

      return parser;
   }

   public Collection<XmlSuite> parse() throws ParserConfigurationException, SAXException, IOException {
      List<String> processedSuites = Lists.newArrayList();
      XmlSuite resultSuite = null;
      List<String> toBeParsed = Lists.newArrayList();
      List<String> toBeAdded = Lists.newArrayList();
      List<String> toBeRemoved = Lists.newArrayList();
      if (this.m_fileName != null) {
         File mainFile = new File(this.m_fileName);
         toBeParsed.add(mainFile.getCanonicalPath());
      }

      label85:
      for(Map childToParentMap = Maps.newHashMap(); toBeParsed.size() > 0; toBeAdded = Lists.newArrayList()) {
         Iterator i$ = toBeParsed.iterator();

         while(true) {
            File parentFile;
            XmlSuite currentXmlSuite;
            List suiteFiles;
            do {
               String s;
               if (!i$.hasNext()) {
                  i$ = toBeRemoved.iterator();

                  while(i$.hasNext()) {
                     s = (String)i$.next();
                     toBeParsed.remove(s);
                  }

                  toBeRemoved = Lists.newArrayList();
                  i$ = toBeAdded.iterator();

                  while(i$.hasNext()) {
                     s = (String)i$.next();
                     toBeParsed.add(s);
                  }
                  continue label85;
               }

               s = (String)i$.next();
               File currFile = new File(s);
               parentFile = currFile.getParentFile();
               InputStream inputStream = this.m_inputStream != null ? this.m_inputStream : new FileInputStream(s);
               IFileParser<XmlSuite> fileParser = getParser(s);
               currentXmlSuite = (XmlSuite)fileParser.parse(s, (InputStream)inputStream, this.m_loadClasses);
               processedSuites.add(s);
               toBeRemoved.add(s);
               if (childToParentMap.containsKey(s)) {
                  XmlSuite parentSuite = (XmlSuite)childToParentMap.get(s);
                  currentXmlSuite.setParentSuite(parentSuite);
                  parentSuite.getChildSuites().add(currentXmlSuite);
               }

               if (null == resultSuite) {
                  resultSuite = currentXmlSuite;
               }

               suiteFiles = currentXmlSuite.getSuiteFiles();
            } while(suiteFiles.size() <= 0);

            Iterator i$ = suiteFiles.iterator();

            while(i$.hasNext()) {
               String path = (String)i$.next();
               String canonicalPath;
               if (parentFile != null && (new File(parentFile, path)).exists()) {
                  canonicalPath = (new File(parentFile, path)).getCanonicalPath();
               } else {
                  canonicalPath = (new File(path)).getCanonicalPath();
               }

               if (!processedSuites.contains(canonicalPath)) {
                  toBeAdded.add(canonicalPath);
                  childToParentMap.put(canonicalPath, currentXmlSuite);
               }
            }
         }
      }

      List<XmlSuite> resultList = Lists.newArrayList();
      resultList.add(resultSuite);
      boolean postProcess = true;
      if (postProcess && this.m_postProcessor != null) {
         return this.m_postProcessor.process(resultList);
      } else {
         return resultList;
      }
   }

   public List<XmlSuite> parseToList() throws ParserConfigurationException, SAXException, IOException {
      List<XmlSuite> result = Lists.newArrayList();
      Collection<XmlSuite> suites = this.parse();
      Iterator i$ = suites.iterator();

      while(i$.hasNext()) {
         XmlSuite suite = (XmlSuite)i$.next();
         result.add(suite);
      }

      return result;
   }

   static {
      PARSERS = Lists.newArrayList((Object[])(DEFAULT_FILE_PARSER));
      ServiceLoader<ISuiteParser> suiteParserLoader = ServiceLoader.load(ISuiteParser.class);
      Iterator i$ = suiteParserLoader.iterator();

      while(i$.hasNext()) {
         ISuiteParser parser = (ISuiteParser)i$.next();
         PARSERS.add(parser);
      }

   }
}
