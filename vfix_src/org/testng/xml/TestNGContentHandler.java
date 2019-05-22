package org.testng.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;
import org.testng.ITestObjectFactory;
import org.testng.TestNGException;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.testng.internal.Utils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class TestNGContentHandler extends DefaultHandler {
   private XmlSuite m_currentSuite = null;
   private XmlTest m_currentTest = null;
   private List<String> m_currentDefines = null;
   private List<String> m_currentRuns = null;
   private List<XmlClass> m_currentClasses = null;
   private int m_currentTestIndex = 0;
   private int m_currentClassIndex = 0;
   private int m_currentIncludeIndex = 0;
   private List<XmlPackage> m_currentPackages = null;
   private XmlPackage m_currentPackage = null;
   private List<XmlSuite> m_suites = Lists.newArrayList();
   private List<String> m_currentIncludedGroups = null;
   private List<String> m_currentExcludedGroups = null;
   private Map<String, String> m_currentTestParameters = null;
   private Map<String, String> m_currentSuiteParameters = null;
   private Map<String, String> m_currentClassParameters = null;
   private TestNGContentHandler.Include m_currentInclude;
   private List<String> m_currentMetaGroup = null;
   private String m_currentMetaGroupName;
   private Stack<TestNGContentHandler.Location> m_locations = new Stack();
   private XmlClass m_currentClass = null;
   private ArrayList<XmlInclude> m_currentIncludedMethods = null;
   private List<String> m_currentExcludedMethods = null;
   private ArrayList<XmlMethodSelector> m_currentSelectors = null;
   private XmlMethodSelector m_currentSelector = null;
   private String m_currentLanguage = null;
   private String m_currentExpression = null;
   private List<String> m_suiteFiles = Lists.newArrayList();
   private boolean m_enabledTest;
   private List<String> m_listeners;
   private String m_fileName;
   private boolean m_loadClasses;

   public TestNGContentHandler(String fileName, boolean loadClasses) {
      this.m_fileName = fileName;
      this.m_loadClasses = loadClasses;
   }

   private static void ppp(String s) {
      System.out.println("[TestNGContentHandler] " + s);
   }

   public InputSource resolveEntity(String systemId, String publicId) throws IOException, SAXException {
      InputSource result = null;
      if (!"http://beust.com/testng/testng-1.0.dtd".equals(publicId) && !"http://testng.org/testng-1.0.dtd".equals(publicId)) {
         result = super.resolveEntity(systemId, publicId);
      } else {
         InputStream is = this.getClass().getClassLoader().getResourceAsStream("testng-1.0.dtd");
         if (null == is) {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream("testng-1.0.dtd");
            if (null == is) {
               System.out.println("WARNING: couldn't find in classpath " + publicId + "\n" + "Fetching it from the Web site.");
               result = super.resolveEntity(systemId, publicId);
            } else {
               result = new InputSource(is);
            }
         } else {
            result = new InputSource(is);
         }
      }

      return result;
   }

   private void xmlSuiteFile(boolean start, Attributes attributes) {
      if (start) {
         String path = attributes.getValue("path");
         this.pushLocation(TestNGContentHandler.Location.SUITE);
         this.m_suiteFiles.add(path);
      } else {
         this.m_currentSuite.setSuiteFiles(this.m_suiteFiles);
         this.popLocation(TestNGContentHandler.Location.SUITE);
      }

   }

   private void xmlSuite(boolean start, Attributes attributes) {
      if (start) {
         this.pushLocation(TestNGContentHandler.Location.SUITE);
         String name = attributes.getValue("name");
         if (Utils.isStringBlank(name)) {
            throw new TestNGException("The <suite> tag must define the name attribute");
         }

         this.m_currentSuite = new XmlSuite();
         this.m_currentSuite.setFileName(this.m_fileName);
         this.m_currentSuite.setName(name);
         this.m_currentSuiteParameters = Maps.newHashMap();
         String verbose = attributes.getValue("verbose");
         if (null != verbose) {
            this.m_currentSuite.setVerbose(new Integer(verbose));
         }

         String jUnit = attributes.getValue("junit");
         if (null != jUnit) {
            this.m_currentSuite.setJUnit(Boolean.valueOf(jUnit));
         }

         String parallel = attributes.getValue("parallel");
         if (parallel != null) {
            if (this.isValidParallel(parallel)) {
               this.m_currentSuite.setParallel(parallel);
            } else {
               Utils.log("Parser", 1, "[WARN] Unknown value of attribute 'parallel' at suite level: '" + parallel + "'.");
            }
         }

         String parentModule = attributes.getValue("parent-module");
         if (parentModule != null) {
            this.m_currentSuite.setParentModule(parentModule);
         }

         String guiceStage = attributes.getValue("guice-stage");
         if (guiceStage != null) {
            this.m_currentSuite.setGuiceStage(guiceStage);
         }

         String configFailurePolicy = attributes.getValue("configfailurepolicy");
         if (null != configFailurePolicy && ("skip".equals(configFailurePolicy) || "continue".equals(configFailurePolicy))) {
            this.m_currentSuite.setConfigFailurePolicy(configFailurePolicy);
         }

         String groupByInstances = attributes.getValue("group-by-instances");
         if (groupByInstances != null) {
            this.m_currentSuite.setGroupByInstances(Boolean.valueOf(groupByInstances));
         }

         String skip = attributes.getValue("skipfailedinvocationcounts");
         if (skip != null) {
            this.m_currentSuite.setSkipFailedInvocationCounts(Boolean.valueOf(skip));
         }

         String threadCount = attributes.getValue("thread-count");
         if (null != threadCount) {
            this.m_currentSuite.setThreadCount(Integer.parseInt(threadCount));
         }

         String dataProviderThreadCount = attributes.getValue("data-provider-thread-count");
         if (null != dataProviderThreadCount) {
            this.m_currentSuite.setDataProviderThreadCount(Integer.parseInt(dataProviderThreadCount));
         }

         String timeOut = attributes.getValue("time-out");
         if (null != timeOut) {
            this.m_currentSuite.setTimeOut(timeOut);
         }

         String objectFactory = attributes.getValue("object-factory");
         if (null != objectFactory && this.m_loadClasses) {
            try {
               this.m_currentSuite.setObjectFactory((ITestObjectFactory)Class.forName(objectFactory).newInstance());
            } catch (Exception var18) {
               Utils.log("Parser", 1, "[ERROR] Unable to create custom object factory '" + objectFactory + "' :" + var18);
            }
         }

         String preserveOrder = attributes.getValue("preserve-order");
         if (preserveOrder != null) {
            this.m_currentSuite.setPreserveOrder(preserveOrder);
         }

         String allowReturnValues = attributes.getValue("allow-return-values");
         if (allowReturnValues != null) {
            this.m_currentSuite.setAllowReturnValues(Boolean.valueOf(allowReturnValues));
         }
      } else {
         this.m_currentSuite.setParameters(this.m_currentSuiteParameters);
         this.m_suites.add(this.m_currentSuite);
         this.m_currentSuiteParameters = null;
         this.popLocation(TestNGContentHandler.Location.SUITE);
      }

   }

   private void xmlDefine(boolean start, Attributes attributes) {
      if (start) {
         String name = attributes.getValue("name");
         this.m_currentDefines = Lists.newArrayList();
         this.m_currentMetaGroup = Lists.newArrayList();
         this.m_currentMetaGroupName = name;
      } else {
         this.m_currentTest.addMetaGroup(this.m_currentMetaGroupName, this.m_currentMetaGroup);
         this.m_currentDefines = null;
      }

   }

   private void xmlScript(boolean start, Attributes attributes) {
      if (start) {
         this.m_currentLanguage = attributes.getValue("language");
         this.m_currentExpression = "";
      } else {
         this.m_currentSelector.setExpression(this.m_currentExpression);
         this.m_currentSelector.setLanguage(this.m_currentLanguage);
         if (this.m_locations.peek() == TestNGContentHandler.Location.TEST) {
            this.m_currentTest.setBeanShellExpression(this.m_currentExpression);
         }

         this.m_currentLanguage = null;
         this.m_currentExpression = null;
      }

   }

   private void xmlTest(boolean start, Attributes attributes) {
      if (start) {
         this.m_currentTest = new XmlTest(this.m_currentSuite, this.m_currentTestIndex++);
         this.pushLocation(TestNGContentHandler.Location.TEST);
         this.m_currentTestParameters = Maps.newHashMap();
         String testName = attributes.getValue("name");
         if (Utils.isStringBlank(testName)) {
            throw new TestNGException("The <test> tag must define the name attribute");
         }

         this.m_currentTest.setName(attributes.getValue("name"));
         String verbose = attributes.getValue("verbose");
         if (null != verbose) {
            this.m_currentTest.setVerbose(Integer.parseInt(verbose));
         }

         String jUnit = attributes.getValue("junit");
         if (null != jUnit) {
            this.m_currentTest.setJUnit(Boolean.valueOf(jUnit));
         }

         String skip = attributes.getValue("skipfailedinvocationcounts");
         if (skip != null) {
            this.m_currentTest.setSkipFailedInvocationCounts(Boolean.valueOf(skip));
         }

         String groupByInstances = attributes.getValue("group-by-instances");
         if (groupByInstances != null) {
            this.m_currentTest.setGroupByInstances(Boolean.valueOf(groupByInstances));
         }

         String preserveOrder = attributes.getValue("preserve-order");
         if (preserveOrder != null) {
            this.m_currentTest.setPreserveOrder(preserveOrder);
         }

         String parallel = attributes.getValue("parallel");
         if (parallel != null) {
            if (this.isValidParallel(parallel)) {
               this.m_currentTest.setParallel(parallel);
            } else {
               Utils.log("Parser", 1, "[WARN] Unknown value of attribute 'parallel' for test '" + this.m_currentTest.getName() + "': '" + parallel + "'");
            }
         }

         String threadCount = attributes.getValue("thread-count");
         if (null != threadCount) {
            this.m_currentTest.setThreadCount(Integer.parseInt(threadCount));
         }

         String timeOut = attributes.getValue("time-out");
         if (null != timeOut) {
            this.m_currentTest.setTimeOut(Long.parseLong(timeOut));
         }

         this.m_enabledTest = true;
         String enabledTestString = attributes.getValue("enabled");
         if (null != enabledTestString) {
            this.m_enabledTest = Boolean.valueOf(enabledTestString);
         }
      } else {
         if (null != this.m_currentTestParameters && this.m_currentTestParameters.size() > 0) {
            this.m_currentTest.setParameters(this.m_currentTestParameters);
         }

         if (null != this.m_currentClasses) {
            this.m_currentTest.setXmlClasses(this.m_currentClasses);
         }

         this.m_currentClasses = null;
         this.m_currentTest = null;
         this.m_currentTestParameters = null;
         this.popLocation(TestNGContentHandler.Location.TEST);
         if (!this.m_enabledTest) {
            List<XmlTest> tests = this.m_currentSuite.getTests();
            tests.remove(tests.size() - 1);
         }
      }

   }

   private boolean isValidParallel(String parallel) {
      return XmlSuite.PARALLEL_MODES.contains(parallel);
   }

   public void xmlClasses(boolean start, Attributes attributes) {
      if (start) {
         this.m_currentClasses = Lists.newArrayList();
         this.m_currentClassIndex = 0;
      } else {
         this.m_currentTest.setXmlClasses(this.m_currentClasses);
         this.m_currentClasses = null;
      }

   }

   public void xmlListeners(boolean start, Attributes attributes) {
      if (start) {
         this.m_listeners = Lists.newArrayList();
      } else if (null != this.m_listeners) {
         this.m_currentSuite.setListeners(this.m_listeners);
         this.m_listeners = null;
      }

   }

   public void xmlListener(boolean start, Attributes attributes) {
      if (start) {
         String listener = attributes.getValue("class-name");
         this.m_listeners.add(listener);
      }

   }

   public void xmlPackages(boolean start, Attributes attributes) {
      if (start) {
         this.m_currentPackages = Lists.newArrayList();
      } else {
         if (null != this.m_currentPackages) {
            switch((TestNGContentHandler.Location)this.m_locations.peek()) {
            case TEST:
               this.m_currentTest.setXmlPackages(this.m_currentPackages);
               break;
            case SUITE:
               this.m_currentSuite.setXmlPackages(this.m_currentPackages);
               break;
            case CLASS:
               throw new UnsupportedOperationException("CLASS");
            }
         }

         this.m_currentPackages = null;
         this.m_currentPackage = null;
      }

   }

   public void xmlMethodSelectors(boolean start, Attributes attributes) {
      if (start) {
         this.m_currentSelectors = new ArrayList();
      } else {
         switch((TestNGContentHandler.Location)this.m_locations.peek()) {
         case TEST:
            this.m_currentTest.setMethodSelectors(this.m_currentSelectors);
            break;
         default:
            this.m_currentSuite.setMethodSelectors((List)this.m_currentSelectors);
         }

         this.m_currentSelectors = null;
      }

   }

   public void xmlSelectorClass(boolean start, Attributes attributes) {
      if (start) {
         this.m_currentSelector.setName(attributes.getValue("name"));
         String priority = attributes.getValue("priority");
         if (priority == null) {
            priority = "0";
         }

         this.m_currentSelector.setPriority(new Integer(priority));
      }

   }

   public void xmlMethodSelector(boolean start, Attributes attributes) {
      if (start) {
         this.m_currentSelector = new XmlMethodSelector();
      } else {
         this.m_currentSelectors.add(this.m_currentSelector);
         this.m_currentSelector = null;
      }

   }

   private void xmlMethod(boolean start, Attributes attributes) {
      if (start) {
         this.m_currentIncludedMethods = new ArrayList();
         this.m_currentExcludedMethods = Lists.newArrayList();
         this.m_currentIncludeIndex = 0;
      } else {
         this.m_currentClass.setIncludedMethods(this.m_currentIncludedMethods);
         this.m_currentClass.setExcludedMethods(this.m_currentExcludedMethods);
         this.m_currentIncludedMethods = null;
         this.m_currentExcludedMethods = null;
      }

   }

   public void xmlRun(boolean start, Attributes attributes) throws SAXException {
      if (start) {
         this.m_currentRuns = Lists.newArrayList();
      } else {
         if (this.m_currentTest != null) {
            this.m_currentTest.setIncludedGroups(this.m_currentIncludedGroups);
            this.m_currentTest.setExcludedGroups(this.m_currentExcludedGroups);
         } else {
            this.m_currentSuite.setIncludedGroups(this.m_currentIncludedGroups);
            this.m_currentSuite.setExcludedGroups(this.m_currentExcludedGroups);
         }

         this.m_currentRuns = null;
      }

   }

   public void xmlGroup(boolean start, Attributes attributes) throws SAXException {
      if (start) {
         this.m_currentTest.addXmlDependencyGroup(attributes.getValue("name"), attributes.getValue("depends-on"));
      }

   }

   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      String name = attributes.getValue("name");
      if ("suite".equals(qName)) {
         this.xmlSuite(true, attributes);
      } else if ("suite-file".equals(qName)) {
         this.xmlSuiteFile(true, attributes);
      } else if ("test".equals(qName)) {
         this.xmlTest(true, attributes);
      } else if ("script".equals(qName)) {
         this.xmlScript(true, attributes);
      } else if ("method-selector".equals(qName)) {
         this.xmlMethodSelector(true, attributes);
      } else if ("method-selectors".equals(qName)) {
         this.xmlMethodSelectors(true, attributes);
      } else if ("selector-class".equals(qName)) {
         this.xmlSelectorClass(true, attributes);
      } else if ("classes".equals(qName)) {
         this.xmlClasses(true, attributes);
      } else if ("packages".equals(qName)) {
         this.xmlPackages(true, attributes);
      } else if ("listeners".equals(qName)) {
         this.xmlListeners(true, attributes);
      } else if ("listener".equals(qName)) {
         this.xmlListener(true, attributes);
      } else if ("class".equals(qName)) {
         if (null != this.m_currentClasses) {
            this.m_currentClass = new XmlClass(name, this.m_currentClassIndex++, this.m_loadClasses);
            this.m_currentClass.setXmlTest(this.m_currentTest);
            this.m_currentClassParameters = Maps.newHashMap();
            this.m_currentClasses.add(this.m_currentClass);
            this.pushLocation(TestNGContentHandler.Location.CLASS);
         }
      } else if ("package".equals(qName)) {
         if (null != this.m_currentPackages) {
            this.m_currentPackage = new XmlPackage();
            this.m_currentPackage.setName(name);
            this.m_currentPackages.add(this.m_currentPackage);
         }
      } else if ("define".equals(qName)) {
         this.xmlDefine(true, attributes);
      } else if ("run".equals(qName)) {
         this.xmlRun(true, attributes);
      } else if ("group".equals(qName)) {
         this.xmlGroup(true, attributes);
      } else if ("groups".equals(qName)) {
         this.m_currentIncludedGroups = Lists.newArrayList();
         this.m_currentExcludedGroups = Lists.newArrayList();
      } else if ("methods".equals(qName)) {
         this.xmlMethod(true, attributes);
      } else if ("include".equals(qName)) {
         this.xmlInclude(true, attributes);
      } else if ("exclude".equals(qName)) {
         this.xmlExclude(true, attributes);
      } else if ("parameter".equals(qName)) {
         String value = expandValue(attributes.getValue("value"));
         switch((TestNGContentHandler.Location)this.m_locations.peek()) {
         case TEST:
            this.m_currentTestParameters.put(name, value);
            break;
         case SUITE:
            this.m_currentSuiteParameters.put(name, value);
            break;
         case CLASS:
            this.m_currentClassParameters.put(name, value);
            break;
         case INCLUDE:
            this.m_currentInclude.parameters.put(name, value);
         }
      }

   }

   private void xmlInclude(boolean start, Attributes attributes) {
      if (start) {
         this.m_locations.push(TestNGContentHandler.Location.INCLUDE);
         this.m_currentInclude = new TestNGContentHandler.Include(attributes.getValue("name"), attributes.getValue("invocation-numbers"));
      } else {
         String name = this.m_currentInclude.name;
         if (null == this.m_currentIncludedMethods) {
            if (null != this.m_currentDefines) {
               this.m_currentMetaGroup.add(name);
            } else if (null != this.m_currentRuns) {
               this.m_currentIncludedGroups.add(name);
            } else if (null != this.m_currentPackage) {
               this.m_currentPackage.getInclude().add(name);
            }
         } else {
            String in = this.m_currentInclude.invocationNumbers;
            XmlInclude include;
            if (!Utils.isStringEmpty(in)) {
               include = new XmlInclude(name, this.stringToList(in), this.m_currentIncludeIndex++);
            } else {
               include = new XmlInclude(name, this.m_currentIncludeIndex++);
            }

            Iterator i$ = this.m_currentInclude.parameters.entrySet().iterator();

            while(i$.hasNext()) {
               Entry<String, String> entry = (Entry)i$.next();
               include.addParameter((String)entry.getKey(), (String)entry.getValue());
            }

            include.setDescription(this.m_currentInclude.description);
            this.m_currentIncludedMethods.add(include);
         }

         this.popLocation(TestNGContentHandler.Location.INCLUDE);
         this.m_currentInclude = null;
      }

   }

   private void xmlExclude(boolean start, Attributes attributes) {
      if (start) {
         this.m_locations.push(TestNGContentHandler.Location.EXCLUDE);
         String name = attributes.getValue("name");
         if (null != this.m_currentExcludedMethods) {
            this.m_currentExcludedMethods.add(name);
         } else if (null != this.m_currentRuns) {
            this.m_currentExcludedGroups.add(name);
         } else if (null != this.m_currentPackage) {
            this.m_currentPackage.getExclude().add(name);
         }
      } else {
         this.popLocation(TestNGContentHandler.Location.EXCLUDE);
      }

   }

   private void pushLocation(TestNGContentHandler.Location l) {
      this.m_locations.push(l);
   }

   private TestNGContentHandler.Location popLocation(TestNGContentHandler.Location location) {
      return (TestNGContentHandler.Location)this.m_locations.pop();
   }

   private List<Integer> stringToList(String in) {
      String[] numbers = in.split(" ");
      List<Integer> result = Lists.newArrayList();
      String[] arr$ = numbers;
      int len$ = numbers.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String n = arr$[i$];
         result.add(Integer.parseInt(n));
      }

      return result;
   }

   public void endElement(String uri, String localName, String qName) throws SAXException {
      if ("suite".equals(qName)) {
         this.xmlSuite(false, (Attributes)null);
      } else if ("suite-file".equals(qName)) {
         this.xmlSuiteFile(false, (Attributes)null);
      } else if ("test".equals(qName)) {
         this.xmlTest(false, (Attributes)null);
      } else if ("define".equals(qName)) {
         this.xmlDefine(false, (Attributes)null);
      } else if ("run".equals(qName)) {
         this.xmlRun(false, (Attributes)null);
      } else if ("methods".equals(qName)) {
         this.xmlMethod(false, (Attributes)null);
      } else if ("classes".equals(qName)) {
         this.xmlClasses(false, (Attributes)null);
      } else if ("packages".equals(qName)) {
         this.xmlPackages(false, (Attributes)null);
      } else if ("class".equals(qName)) {
         this.m_currentClass.setParameters(this.m_currentClassParameters);
         this.m_currentClassParameters = null;
         this.popLocation(TestNGContentHandler.Location.CLASS);
      } else if ("listeners".equals(qName)) {
         this.xmlListeners(false, (Attributes)null);
      } else if ("method-selector".equals(qName)) {
         this.xmlMethodSelector(false, (Attributes)null);
      } else if ("method-selectors".equals(qName)) {
         this.xmlMethodSelectors(false, (Attributes)null);
      } else if ("selector-class".equals(qName)) {
         this.xmlSelectorClass(false, (Attributes)null);
      } else if ("script".equals(qName)) {
         this.xmlScript(false, (Attributes)null);
      } else if ("packages".equals(qName)) {
         this.xmlPackages(false, (Attributes)null);
      } else if ("include".equals(qName)) {
         this.xmlInclude(false, (Attributes)null);
      } else if ("exclude".equals(qName)) {
         this.xmlExclude(false, (Attributes)null);
      }

   }

   public void error(SAXParseException e) throws SAXException {
      throw e;
   }

   private boolean areWhiteSpaces(char[] ch, int start, int length) {
      for(int i = start; i < start + length; ++i) {
         char c = ch[i];
         if (c != '\n' && c != '\t' && c != ' ') {
            return false;
         }
      }

      return true;
   }

   public void characters(char[] ch, int start, int length) {
      if (null != this.m_currentLanguage && !this.areWhiteSpaces(ch, start, length)) {
         this.m_currentExpression = this.m_currentExpression + new String(ch, start, length);
      }

   }

   public XmlSuite getSuite() {
      return this.m_currentSuite;
   }

   private static String expandValue(String value) {
      StringBuffer result = null;
      int startIndex = false;
      int endIndex = false;
      int startPosition = 0;

      int startIndex;
      int endIndex;
      for(String property = null; (startIndex = value.indexOf("${", startPosition)) > -1 && (endIndex = value.indexOf("}", startIndex + 3)) > -1; startPosition = startIndex + 3 + property.length()) {
         property = value.substring(startIndex + 2, endIndex);
         if (result == null) {
            result = new StringBuffer(value.substring(startPosition, startIndex));
         } else {
            result.append(value.substring(startPosition, startIndex));
         }

         String propertyValue = System.getProperty(property);
         if (propertyValue == null) {
            propertyValue = System.getenv(property);
         }

         if (propertyValue != null) {
            result.append(propertyValue);
         } else {
            result.append("${");
            result.append(property);
            result.append("}");
         }
      }

      if (result != null) {
         result.append(value.substring(startPosition));
         return result.toString();
      } else {
         return value;
      }
   }

   private class Include {
      String name;
      String invocationNumbers;
      String description;
      Map<String, String> parameters = Maps.newHashMap();

      public Include(String name, String numbers) {
         this.name = name;
         this.invocationNumbers = numbers;
      }
   }

   static enum Location {
      SUITE,
      TEST,
      CLASS,
      INCLUDE,
      EXCLUDE;
   }
}
