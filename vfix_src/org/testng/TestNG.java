package org.testng;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.xml.parsers.ParserConfigurationException;
import org.testng.annotations.ITestAnnotation;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.testng.collections.Sets;
import org.testng.internal.ClassHelper;
import org.testng.internal.Configuration;
import org.testng.internal.DynamicGraph;
import org.testng.internal.IConfiguration;
import org.testng.internal.IResultListener2;
import org.testng.internal.OverrideProcessor;
import org.testng.internal.SuiteRunnerMap;
import org.testng.internal.Utils;
import org.testng.internal.Version;
import org.testng.internal.annotations.DefaultAnnotationTransformer;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.internal.annotations.JDK15AnnotationFinder;
import org.testng.internal.thread.graph.GraphThreadPoolExecutor;
import org.testng.internal.thread.graph.IThreadWorkerFactory;
import org.testng.internal.thread.graph.SuiteWorkerFactory;
import org.testng.junit.JUnitTestFinder;
import org.testng.log4testng.Logger;
import org.testng.remote.SuiteDispatcher;
import org.testng.remote.SuiteSlave;
import org.testng.reporters.EmailableReporter;
import org.testng.reporters.EmailableReporter2;
import org.testng.reporters.FailedReporter;
import org.testng.reporters.JUnitReportReporter;
import org.testng.reporters.SuiteHTMLReporter;
import org.testng.reporters.VerboseReporter;
import org.testng.reporters.XMLReporter;
import org.testng.reporters.jq.Main;
import org.testng.xml.Parser;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlMethodSelector;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.xml.sax.SAXException;

public class TestNG {
   private static final Logger LOGGER = Logger.getLogger(TestNG.class);
   public static final String DEFAULT_COMMAND_LINE_SUITE_NAME = "Command line suite";
   public static final String DEFAULT_COMMAND_LINE_TEST_NAME = "Command line test";
   public static final String DEFAULT_OUTPUTDIR = "test-output";
   public static final String SHOW_TESTNG_STACK_FRAMES = "testng.show.stack.frames";
   public static final String TEST_CLASSPATH = "testng.test.classpath";
   private static TestNG m_instance;
   private static JCommander m_jCommander;
   private List<String> m_commandLineMethods;
   protected List<XmlSuite> m_suites = Lists.newArrayList();
   private List<XmlSuite> m_cmdlineSuites;
   private String m_outputDir = "test-output";
   private String[] m_includedGroups;
   private String[] m_excludedGroups;
   private Boolean m_isJUnit;
   private Boolean m_isMixed;
   protected boolean m_useDefaultListeners;
   private ITestRunnerFactory m_testRunnerFactory;
   private List<ITestListener> m_testListeners;
   private List<ISuiteListener> m_suiteListeners;
   private Set<IReporter> m_reporters;
   protected static final int HAS_FAILURE = 1;
   protected static final int HAS_SKIPPED = 2;
   protected static final int HAS_FSP = 4;
   protected static final int HAS_NO_TEST = 8;
   public static final Integer DEFAULT_VERBOSE = 1;
   private int m_status;
   private boolean m_hasTests;
   private String m_slavefileName;
   private String m_masterfileName;
   private int m_threadCount;
   private boolean m_useThreadCount;
   private String m_parallelMode;
   private boolean m_useParallelMode;
   private String m_configFailurePolicy;
   private Class[] m_commandLineTestClasses;
   private String m_defaultSuiteName;
   private String m_defaultTestName;
   private Map<String, Integer> m_methodDescriptors;
   private ITestObjectFactory m_objectFactory;
   private List<IInvokedMethodListener> m_invokedMethodListeners;
   private Integer m_dataProviderThreadCount;
   private String m_jarPath;
   private String m_xmlPathInJar;
   private List<String> m_stringSuites;
   private IHookable m_hookable;
   private IConfigurable m_configurable;
   protected long m_end;
   protected long m_start;
   private List<IExecutionListener> m_executionListeners;
   private boolean m_isInitialized;
   private Integer m_verbose;
   private final IAnnotationTransformer m_defaultAnnoProcessor;
   private IAnnotationTransformer m_annotationTransformer;
   private Boolean m_skipFailedInvocationCounts;
   private List<IMethodInterceptor> m_methodInterceptors;
   private List<String> m_testNames;
   private Integer m_suiteThreadPoolSize;
   private boolean m_randomizeSuites;
   private boolean m_preserveOrder;
   private Boolean m_groupByInstances;
   private IConfiguration m_configuration;
   private URLClassLoader m_serviceLoaderClassLoader;
   private List<ITestNGListener> m_serviceLoaderListeners;

   public TestNG() {
      this.m_isJUnit = XmlSuite.DEFAULT_JUNIT;
      this.m_isMixed = XmlSuite.DEFAULT_MIXED;
      this.m_useDefaultListeners = true;
      this.m_testListeners = Lists.newArrayList();
      this.m_suiteListeners = Lists.newArrayList();
      this.m_reporters = Sets.newHashSet();
      this.m_hasTests = false;
      this.m_slavefileName = null;
      this.m_masterfileName = null;
      this.m_defaultSuiteName = "Command line suite";
      this.m_defaultTestName = "Command line test";
      this.m_methodDescriptors = Maps.newHashMap();
      this.m_invokedMethodListeners = Lists.newArrayList();
      this.m_dataProviderThreadCount = null;
      this.m_xmlPathInJar = "testng.xml";
      this.m_stringSuites = Lists.newArrayList();
      this.m_executionListeners = Lists.newArrayList();
      this.m_isInitialized = false;
      this.m_verbose = null;
      this.m_defaultAnnoProcessor = new DefaultAnnotationTransformer();
      this.m_annotationTransformer = this.m_defaultAnnoProcessor;
      this.m_skipFailedInvocationCounts = false;
      this.m_methodInterceptors = new ArrayList();
      this.m_suiteThreadPoolSize = CommandLineArgs.SUITE_THREAD_POOL_SIZE_DEFAULT;
      this.m_randomizeSuites = Boolean.FALSE;
      this.m_preserveOrder = false;
      this.m_serviceLoaderListeners = Lists.newArrayList();
      this.init(true);
   }

   public TestNG(boolean useDefaultListeners) {
      this.m_isJUnit = XmlSuite.DEFAULT_JUNIT;
      this.m_isMixed = XmlSuite.DEFAULT_MIXED;
      this.m_useDefaultListeners = true;
      this.m_testListeners = Lists.newArrayList();
      this.m_suiteListeners = Lists.newArrayList();
      this.m_reporters = Sets.newHashSet();
      this.m_hasTests = false;
      this.m_slavefileName = null;
      this.m_masterfileName = null;
      this.m_defaultSuiteName = "Command line suite";
      this.m_defaultTestName = "Command line test";
      this.m_methodDescriptors = Maps.newHashMap();
      this.m_invokedMethodListeners = Lists.newArrayList();
      this.m_dataProviderThreadCount = null;
      this.m_xmlPathInJar = "testng.xml";
      this.m_stringSuites = Lists.newArrayList();
      this.m_executionListeners = Lists.newArrayList();
      this.m_isInitialized = false;
      this.m_verbose = null;
      this.m_defaultAnnoProcessor = new DefaultAnnotationTransformer();
      this.m_annotationTransformer = this.m_defaultAnnoProcessor;
      this.m_skipFailedInvocationCounts = false;
      this.m_methodInterceptors = new ArrayList();
      this.m_suiteThreadPoolSize = CommandLineArgs.SUITE_THREAD_POOL_SIZE_DEFAULT;
      this.m_randomizeSuites = Boolean.FALSE;
      this.m_preserveOrder = false;
      this.m_serviceLoaderListeners = Lists.newArrayList();
      this.init(useDefaultListeners);
   }

   private void init(boolean useDefaultListeners) {
      m_instance = this;
      this.m_useDefaultListeners = useDefaultListeners;
      this.m_configuration = new Configuration();
   }

   public int getStatus() {
      return this.m_status;
   }

   private void setStatus(int status) {
      this.m_status |= status;
   }

   public void setOutputDirectory(String outputdir) {
      if (Utils.isStringNotEmpty(outputdir)) {
         this.m_outputDir = outputdir;
      }

   }

   public void setUseDefaultListeners(boolean useDefaultListeners) {
      this.m_useDefaultListeners = useDefaultListeners;
   }

   public void setTestJar(String jarPath) {
      this.m_jarPath = jarPath;
   }

   public void setXmlPathInJar(String xmlPathInJar) {
      this.m_xmlPathInJar = xmlPathInJar;
   }

   public void initializeSuitesAndJarFile() {
      if (!this.m_isInitialized) {
         this.m_isInitialized = true;
         Iterator i$;
         if (this.m_suites.size() > 0) {
            i$ = this.m_suites.iterator();

            while(i$.hasNext()) {
               XmlSuite s = (XmlSuite)i$.next();
               Iterator i$ = s.getSuiteFiles().iterator();

               while(i$.hasNext()) {
                  String suiteFile = (String)i$.next();

                  try {
                     Collection<XmlSuite> childSuites = this.getParser(suiteFile).parse();
                     Iterator i$ = childSuites.iterator();

                     while(i$.hasNext()) {
                        XmlSuite cSuite = (XmlSuite)i$.next();
                        cSuite.setParentSuite(s);
                        s.getChildSuites().add(cSuite);
                     }
                  } catch (IOException | SAXException | ParserConfigurationException var12) {
                     var12.printStackTrace(System.out);
                  }
               }
            }

         } else {
            i$ = this.m_stringSuites.iterator();

            while(i$.hasNext()) {
               String suitePath = (String)i$.next();
               if (LOGGER.isDebugEnabled()) {
                  LOGGER.debug("suiteXmlPath: \"" + suitePath + "\"");
               }

               try {
                  Collection<XmlSuite> allSuites = this.getParser(suitePath).parse();
                  Iterator i$ = allSuites.iterator();

                  while(i$.hasNext()) {
                     XmlSuite s = (XmlSuite)i$.next();
                     if (this.m_testNames != null) {
                        this.m_suites.add(extractTestNames(s, this.m_testNames));
                     } else {
                        this.m_suites.add(s);
                     }
                  }
               } catch (ParserConfigurationException | IOException | SAXException var14) {
                  var14.printStackTrace(System.out);
               } catch (Exception var15) {
                  Object t;
                  for(t = var15; ((Throwable)t).getCause() != null; t = ((Throwable)t).getCause()) {
                  }

                  if (t instanceof TestNGException) {
                     throw (TestNGException)t;
                  }

                  throw new TestNGException((Throwable)t);
               }
            }

            if (this.m_jarPath != null && this.m_stringSuites.size() > 0) {
               StringBuilder suites = new StringBuilder();
               Iterator i$ = this.m_stringSuites.iterator();

               while(i$.hasNext()) {
                  String s = (String)i$.next();
                  suites.append(s);
               }

               Utils.log("TestNG", 2, "Ignoring the XML file inside " + this.m_jarPath + " and using " + suites + " instead");
            } else if (!Utils.isStringEmpty(this.m_jarPath)) {
               File jarFile = new File(this.m_jarPath);

               try {
                  Utils.log("TestNG", 2, "Trying to open jar file:" + jarFile);
                  JarFile jf = new JarFile(jarFile);
                  Enumeration<JarEntry> entries = jf.entries();
                  List<String> classes = Lists.newArrayList();
                  boolean foundTestngXml = false;

                  Iterator i$;
                  while(entries.hasMoreElements()) {
                     JarEntry je = (JarEntry)entries.nextElement();
                     if (je.getName().equals(this.m_xmlPathInJar)) {
                        Parser parser = this.getParser(jf.getInputStream(je));
                        Collection<XmlSuite> suites = parser.parse();
                        i$ = suites.iterator();

                        while(i$.hasNext()) {
                           XmlSuite suite = (XmlSuite)i$.next();
                           if (this.m_testNames != null) {
                              this.m_suites.add(extractTestNames(suite, this.m_testNames));
                           } else {
                              this.m_suites.add(suite);
                           }
                        }

                        foundTestngXml = true;
                        break;
                     }

                     if (je.getName().endsWith(".class")) {
                        int n = je.getName().length() - ".class".length();
                        classes.add(je.getName().replace("/", ".").substring(0, n));
                     }
                  }

                  if (!foundTestngXml) {
                     Utils.log("TestNG", 1, "Couldn't find the " + this.m_xmlPathInJar + " in the jar file, running all the classes");
                     XmlSuite xmlSuite = new XmlSuite();
                     xmlSuite.setVerbose(0);
                     xmlSuite.setName("Jar suite");
                     XmlTest xmlTest = new XmlTest(xmlSuite);
                     List<XmlClass> xmlClasses = Lists.newArrayList();
                     i$ = classes.iterator();

                     while(i$.hasNext()) {
                        String cls = (String)i$.next();
                        XmlClass xmlClass = new XmlClass(cls);
                        xmlClasses.add(xmlClass);
                     }

                     xmlTest.setXmlClasses(xmlClasses);
                     this.m_suites.add(xmlSuite);
                  }
               } catch (IOException | SAXException | ParserConfigurationException var13) {
                  var13.printStackTrace();
               }

            }
         }
      }
   }

   private Parser getParser(String path) {
      Parser result = new Parser(path);
      this.initProcessor(result);
      return result;
   }

   private Parser getParser(InputStream is) {
      Parser result = new Parser(is);
      this.initProcessor(result);
      return result;
   }

   private void initProcessor(Parser result) {
      result.setPostProcessor(new OverrideProcessor(this.m_includedGroups, this.m_excludedGroups));
   }

   private static XmlSuite extractTestNames(XmlSuite s, List<String> testNames) {
      List<XmlTest> tests = Lists.newArrayList();
      Iterator i$ = s.getTests().iterator();

      while(i$.hasNext()) {
         XmlTest xt = (XmlTest)i$.next();
         Iterator i$ = testNames.iterator();

         while(i$.hasNext()) {
            String tn = (String)i$.next();
            if (xt.getName().equals(tn)) {
               tests.add(xt);
            }
         }
      }

      if (tests.size() == 0) {
         return s;
      } else {
         XmlSuite result = (XmlSuite)s.clone();
         result.getTests().clear();
         result.getTests().addAll(tests);
         return result;
      }
   }

   public void setThreadCount(int threadCount) {
      if (threadCount < 1) {
         exitWithError("Cannot use a threadCount parameter less than 1; 1 > " + threadCount);
      }

      this.m_threadCount = threadCount;
      this.m_useThreadCount = true;
   }

   public void setParallel(String parallel) {
      this.m_parallelMode = parallel;
      this.m_useParallelMode = true;
   }

   public void setCommandLineSuite(XmlSuite suite) {
      this.m_cmdlineSuites = Lists.newArrayList();
      this.m_cmdlineSuites.add(suite);
      this.m_suites.add(suite);
   }

   public void setTestClasses(Class[] classes) {
      this.m_suites.clear();
      this.m_commandLineTestClasses = classes;
   }

   private String[] splitMethod(String m) {
      int index = m.lastIndexOf(".");
      if (index < 0) {
         throw new TestNGException("Bad format for command line method:" + m + ", expected <class>.<method>");
      } else {
         return new String[]{m.substring(0, index), m.substring(index + 1).replaceAll("\\*", "\\.\\*")};
      }
   }

   private List<XmlSuite> createCommandLineSuitesForMethods(List<String> commandLineMethods) {
      Set<Class> classes = Sets.newHashSet();
      Iterator i$ = commandLineMethods.iterator();

      while(i$.hasNext()) {
         String m = (String)i$.next();
         Class c = ClassHelper.forName(this.splitMethod(m)[0]);
         if (c != null) {
            classes.add(c);
         }
      }

      List<XmlSuite> result = this.createCommandLineSuitesForClasses((Class[])classes.toArray(new Class[0]));
      List<XmlClass> xmlClasses = Lists.newArrayList();
      Iterator i$ = result.iterator();

      Iterator i$;
      while(i$.hasNext()) {
         XmlSuite s = (XmlSuite)i$.next();
         i$ = s.getTests().iterator();

         while(i$.hasNext()) {
            XmlTest t = (XmlTest)i$.next();
            xmlClasses.addAll(t.getClasses());
         }
      }

      i$ = xmlClasses.iterator();

      while(i$.hasNext()) {
         XmlClass xc = (XmlClass)i$.next();
         i$ = commandLineMethods.iterator();

         while(i$.hasNext()) {
            String m = (String)i$.next();
            String[] split = this.splitMethod(m);
            String className = split[0];
            if (xc.getName().equals(className)) {
               XmlInclude includedMethod = new XmlInclude(split[1]);
               xc.getIncludedMethods().add(includedMethod);
            }
         }
      }

      return result;
   }

   private List<XmlSuite> createCommandLineSuitesForClasses(Class[] classes) {
      XmlClass[] xmlClasses = Utils.classesToXmlClasses(classes);
      Map<String, XmlSuite> suites = Maps.newHashMap();
      IAnnotationFinder finder = this.m_configuration.getAnnotationFinder();

      for(int i = 0; i < classes.length; ++i) {
         Class c = classes[i];
         ITestAnnotation test = (ITestAnnotation)finder.findAnnotation(c, ITestAnnotation.class);
         String suiteName = this.getDefaultSuiteName();
         String testName = this.getDefaultTestName();
         boolean isJUnit = false;
         if (test != null) {
            suiteName = Utils.defaultIfStringEmpty(test.getSuiteName(), suiteName);
            testName = Utils.defaultIfStringEmpty(test.getTestName(), testName);
         } else if (this.m_isMixed && JUnitTestFinder.isJUnitTest(c)) {
            isJUnit = true;
            testName = c.getName();
         }

         XmlSuite xmlSuite = (XmlSuite)suites.get(suiteName);
         if (xmlSuite == null) {
            xmlSuite = new XmlSuite();
            xmlSuite.setName(suiteName);
            suites.put(suiteName, xmlSuite);
         }

         if (this.m_dataProviderThreadCount != null) {
            xmlSuite.setDataProviderThreadCount(this.m_dataProviderThreadCount);
         }

         XmlTest xmlTest = null;
         Iterator i$ = xmlSuite.getTests().iterator();

         while(i$.hasNext()) {
            XmlTest xt = (XmlTest)i$.next();
            if (xt.getName().equals(testName)) {
               xmlTest = xt;
               break;
            }
         }

         if (xmlTest == null) {
            xmlTest = new XmlTest(xmlSuite);
            xmlTest.setName(testName);
            xmlTest.setJUnit(isJUnit);
         }

         xmlTest.getXmlClasses().add(xmlClasses[i]);
      }

      return new ArrayList(suites.values());
   }

   public void addMethodSelector(String className, int priority) {
      this.m_methodDescriptors.put(className, priority);
   }

   public void setTestSuites(List<String> suites) {
      this.m_stringSuites = suites;
   }

   public void setXmlSuites(List<XmlSuite> suites) {
      this.m_suites = suites;
   }

   public void setExcludedGroups(String groups) {
      this.m_excludedGroups = Utils.split(groups, ",");
   }

   public void setGroups(String groups) {
      this.m_includedGroups = Utils.split(groups, ",");
   }

   private void setTestRunnerFactoryClass(Class testRunnerFactoryClass) {
      this.setTestRunnerFactory((ITestRunnerFactory)ClassHelper.newInstance(testRunnerFactoryClass));
   }

   protected void setTestRunnerFactory(ITestRunnerFactory itrf) {
      this.m_testRunnerFactory = itrf;
   }

   public void setObjectFactory(Class c) {
      this.m_objectFactory = (ITestObjectFactory)ClassHelper.newInstance(c);
   }

   public void setObjectFactory(ITestObjectFactory factory) {
      this.m_objectFactory = factory;
   }

   public void setListenerClasses(List<Class> classes) {
      Iterator i$ = classes.iterator();

      while(i$.hasNext()) {
         Class cls = (Class)i$.next();
         this.addListener(ClassHelper.newInstance(cls));
      }

   }

   public void addListener(Object listener) {
      if (!(listener instanceof ITestNGListener)) {
         exitWithError("Listener " + listener + " must be one of ITestListener, ISuiteListener, IReporter, " + " IAnnotationTransformer, IMethodInterceptor or IInvokedMethodListener");
      } else {
         if (listener instanceof ISuiteListener) {
            this.addListener((ISuiteListener)listener);
         }

         if (listener instanceof ITestListener) {
            this.addListener((ITestListener)listener);
         }

         if (listener instanceof IReporter) {
            this.addListener((IReporter)listener);
         }

         if (listener instanceof IAnnotationTransformer) {
            this.setAnnotationTransformer((IAnnotationTransformer)listener);
         }

         if (listener instanceof IMethodInterceptor) {
            this.m_methodInterceptors.add((IMethodInterceptor)listener);
         }

         if (listener instanceof IInvokedMethodListener) {
            this.addInvokedMethodListener((IInvokedMethodListener)listener);
         }

         if (listener instanceof IHookable) {
            this.setHookable((IHookable)listener);
         }

         if (listener instanceof IConfigurable) {
            this.setConfigurable((IConfigurable)listener);
         }

         if (listener instanceof IExecutionListener) {
            this.addExecutionListener((IExecutionListener)listener);
         }

         if (listener instanceof IConfigurationListener) {
            this.getConfiguration().addConfigurationListener((IConfigurationListener)listener);
         }
      }

   }

   public void addListener(IInvokedMethodListener listener) {
      this.m_invokedMethodListeners.add(listener);
   }

   public void addListener(ISuiteListener listener) {
      if (null != listener) {
         this.m_suiteListeners.add(listener);
      }

   }

   public void addListener(ITestListener listener) {
      if (null != listener) {
         this.m_testListeners.add(listener);
      }

   }

   public void addListener(IReporter listener) {
      if (null != listener) {
         this.m_reporters.add(listener);
      }

   }

   public void addInvokedMethodListener(IInvokedMethodListener listener) {
      this.m_invokedMethodListeners.add(listener);
   }

   public Set<IReporter> getReporters() {
      return this.m_reporters;
   }

   public List<ITestListener> getTestListeners() {
      return this.m_testListeners;
   }

   public List<ISuiteListener> getSuiteListeners() {
      return this.m_suiteListeners;
   }

   public void setVerbose(int verbose) {
      this.m_verbose = verbose;
   }

   private void initializeCommandLineSuites() {
      if (this.m_commandLineTestClasses != null || this.m_commandLineMethods != null) {
         if (null != this.m_commandLineMethods) {
            this.m_cmdlineSuites = this.createCommandLineSuitesForMethods(this.m_commandLineMethods);
         } else {
            this.m_cmdlineSuites = this.createCommandLineSuitesForClasses(this.m_commandLineTestClasses);
         }

         Iterator i$ = this.m_cmdlineSuites.iterator();

         while(i$.hasNext()) {
            XmlSuite s = (XmlSuite)i$.next();
            Iterator i$ = s.getTests().iterator();

            while(i$.hasNext()) {
               XmlTest t = (XmlTest)i$.next();
               t.setPreserveOrder(String.valueOf(this.m_preserveOrder));
            }

            this.m_suites.add(s);
            if (this.m_groupByInstances != null) {
               s.setGroupByInstances(this.m_groupByInstances);
            }
         }
      }

   }

   private void initializeCommandLineSuitesParams() {
      if (null != this.m_cmdlineSuites) {
         Iterator i$ = this.m_cmdlineSuites.iterator();

         while(i$.hasNext()) {
            XmlSuite s = (XmlSuite)i$.next();
            if (this.m_useThreadCount) {
               s.setThreadCount(this.m_threadCount);
            }

            if (this.m_useParallelMode) {
               s.setParallel(this.m_parallelMode);
            }

            if (this.m_configFailurePolicy != null) {
               s.setConfigFailurePolicy(this.m_configFailurePolicy.toString());
            }
         }

      }
   }

   private void initializeCommandLineSuitesGroups() {
      boolean hasIncludedGroups = null != this.m_includedGroups && this.m_includedGroups.length > 0;
      boolean hasExcludedGroups = null != this.m_excludedGroups && this.m_excludedGroups.length > 0;
      List<XmlSuite> suites = this.m_cmdlineSuites != null ? this.m_cmdlineSuites : this.m_suites;
      if (hasIncludedGroups || hasExcludedGroups) {
         Iterator i$ = suites.iterator();

         while(i$.hasNext()) {
            XmlSuite s = (XmlSuite)i$.next();
            Iterator i$ = s.getTests().iterator();

            while(i$.hasNext()) {
               XmlTest t = (XmlTest)i$.next();
               if (hasIncludedGroups) {
                  t.setIncludedGroups(Arrays.asList(this.m_includedGroups));
               }

               if (hasExcludedGroups) {
                  t.setExcludedGroups(Arrays.asList(this.m_excludedGroups));
               }
            }
         }
      }

   }

   private void addReporter(Class<? extends IReporter> r) {
      if (!this.m_reporters.contains(r)) {
         this.m_reporters.add(ClassHelper.newInstance(r));
      }

   }

   private void initializeDefaultListeners() {
      this.m_testListeners.add(new TestNG.ExitCodeListener(this));
      if (this.m_useDefaultListeners) {
         this.addReporter(SuiteHTMLReporter.class);
         this.addReporter(Main.class);
         this.addReporter(FailedReporter.class);
         this.addReporter(XMLReporter.class);
         if (System.getProperty("oldTestngEmailableReporter") != null) {
            this.addReporter(EmailableReporter.class);
         } else if (System.getProperty("noEmailableReporter") == null) {
            this.addReporter(EmailableReporter2.class);
         }

         this.addReporter(JUnitReportReporter.class);
         if (this.m_verbose != null && this.m_verbose > 4) {
            this.addListener((ITestListener)(new VerboseReporter("[TestNG] ")));
         }
      }

   }

   private void initializeConfiguration() {
      ITestObjectFactory factory = this.m_objectFactory;
      this.addServiceLoaderListeners();
      Iterator i$ = this.m_suites.iterator();

      while(i$.hasNext()) {
         XmlSuite s = (XmlSuite)i$.next();
         Iterator i$ = s.getListeners().iterator();

         while(i$.hasNext()) {
            String listenerName = (String)i$.next();
            Class<?> listenerClass = ClassHelper.forName(listenerName);
            if (listenerClass == null) {
               throw new TestNGException("Listener " + listenerName + " was not found in project's classpath");
            }

            Object listener = ClassHelper.newInstance(listenerClass);
            this.addListener(listener);
         }

         i$ = s.getMethodSelectors().iterator();

         while(i$.hasNext()) {
            XmlMethodSelector methodSelector = (XmlMethodSelector)i$.next();
            this.addMethodSelector(methodSelector.getClassName(), methodSelector.getPriority());
         }

         if (s.getObjectFactory() != null) {
            if (factory != null) {
               throw new TestNGException("Found more than one object-factory tag in your suites");
            }

            factory = s.getObjectFactory();
         }
      }

      this.m_configuration.setAnnotationFinder(new JDK15AnnotationFinder(this.getAnnotationTransformer()));
      this.m_configuration.setHookable(this.m_hookable);
      this.m_configuration.setConfigurable(this.m_configurable);
      this.m_configuration.setObjectFactory(factory);
   }

   private void addServiceLoaderListeners() {
      Iterable<ITestNGListener> loader = this.m_serviceLoaderClassLoader != null ? ServiceLoader.load(ITestNGListener.class, this.m_serviceLoaderClassLoader) : ServiceLoader.load(ITestNGListener.class);
      Iterator i$ = loader.iterator();

      while(i$.hasNext()) {
         ITestNGListener l = (ITestNGListener)i$.next();
         Utils.log("[TestNG]", 2, "Adding ServiceLoader listener:" + l);
         this.addListener((Object)l);
         this.addServiceLoaderListener(l);
      }

   }

   private void sanityCheck() {
      this.checkTestNames(this.m_suites);
      this.checkSuiteNames(this.m_suites);
   }

   private void checkTestNames(List<XmlSuite> suites) {
      Iterator i$ = suites.iterator();

      while(i$.hasNext()) {
         XmlSuite suite = (XmlSuite)i$.next();
         Set<String> testNames = Sets.newHashSet();
         Iterator i$ = suite.getTests().iterator();

         while(i$.hasNext()) {
            XmlTest test = (XmlTest)i$.next();
            if (testNames.contains(test.getName())) {
               throw new TestNGException("Two tests in the same suite cannot have the same name: " + test.getName());
            }

            testNames.add(test.getName());
         }

         this.checkTestNames(suite.getChildSuites());
      }

   }

   private void checkSuiteNames(List<XmlSuite> suites) {
      this.checkSuiteNamesInternal(suites, Sets.newHashSet());
   }

   private void checkSuiteNamesInternal(List<XmlSuite> suites, Set<String> names) {
      Iterator i$ = suites.iterator();

      while(i$.hasNext()) {
         XmlSuite suite = (XmlSuite)i$.next();
         String name = suite.getName();
         if (names.contains(name)) {
            throw new TestNGException("Two suites cannot have the same name: " + name);
         }

         names.add(name);
         this.checkSuiteNamesInternal(suite.getChildSuites(), names);
      }

   }

   public void run() {
      this.initializeSuitesAndJarFile();
      this.initializeConfiguration();
      this.initializeDefaultListeners();
      this.initializeCommandLineSuites();
      this.initializeCommandLineSuitesParams();
      this.initializeCommandLineSuitesGroups();
      this.sanityCheck();
      List<ISuite> suiteRunners = null;
      this.runExecutionListeners(true);
      this.m_start = System.currentTimeMillis();
      if (this.m_slavefileName != null) {
         SuiteSlave slave = new SuiteSlave(this.m_slavefileName, this);
         slave.waitForSuites();
      } else if (this.m_masterfileName == null) {
         suiteRunners = this.runSuitesLocally();
      } else {
         SuiteDispatcher dispatcher = new SuiteDispatcher(this.m_masterfileName);
         suiteRunners = dispatcher.dispatch(this.getConfiguration(), this.m_suites, this.getOutputDirectory(), this.getTestListeners());
      }

      this.m_end = System.currentTimeMillis();
      this.runExecutionListeners(false);
      if (null != suiteRunners) {
         this.generateReports(suiteRunners);
      }

      if (!this.m_hasTests) {
         this.setStatus(8);
         if (TestRunner.getVerbose() > 1) {
            System.err.println("[TestNG] No tests found. Nothing was run");
            usage();
         }
      }

   }

   private void p(String string) {
      System.out.println("[TestNG] " + string);
   }

   private void runExecutionListeners(boolean start) {
      Iterator i$ = Arrays.asList(this.m_executionListeners, this.m_configuration.getExecutionListeners()).iterator();

      while(i$.hasNext()) {
         List<IExecutionListener> listeners = (List)i$.next();
         Iterator i$ = listeners.iterator();

         while(i$.hasNext()) {
            IExecutionListener l = (IExecutionListener)i$.next();
            if (start) {
               l.onExecutionStart();
            } else {
               l.onExecutionFinish();
            }
         }
      }

   }

   public void addExecutionListener(IExecutionListener l) {
      this.m_executionListeners.add(l);
   }

   private static void usage() {
      if (m_jCommander == null) {
         m_jCommander = new JCommander(new CommandLineArgs());
      }

      m_jCommander.usage();
   }

   private void generateReports(List<ISuite> suiteRunners) {
      Iterator i$ = this.m_reporters.iterator();

      while(i$.hasNext()) {
         IReporter reporter = (IReporter)i$.next();

         try {
            long start = System.currentTimeMillis();
            reporter.generateReport(this.m_suites, suiteRunners, this.m_outputDir);
            Utils.log("TestNG", 2, "Time taken by " + reporter + ": " + (System.currentTimeMillis() - start) + " ms");
         } catch (Exception var7) {
            System.err.println("[TestNG] Reporter " + reporter + " failed");
            var7.printStackTrace(System.err);
         }
      }

   }

   public List<ISuite> runSuitesLocally() {
      SuiteRunnerMap suiteRunnerMap = new SuiteRunnerMap();
      if (this.m_suites.size() > 0) {
         if (((XmlSuite)this.m_suites.get(0)).getVerbose() >= 2) {
            Version.displayBanner();
         }

         Iterator i$ = this.m_suites.iterator();

         XmlSuite xmlSuite;
         while(i$.hasNext()) {
            xmlSuite = (XmlSuite)i$.next();
            this.createSuiteRunners(suiteRunnerMap, xmlSuite);
         }

         if (this.m_suiteThreadPoolSize == 1 && !this.m_randomizeSuites) {
            i$ = this.m_suites.iterator();

            while(i$.hasNext()) {
               xmlSuite = (XmlSuite)i$.next();
               this.runSuitesSequentially(xmlSuite, suiteRunnerMap, this.getVerbose(xmlSuite), this.getDefaultSuiteName());
            }
         } else {
            DynamicGraph<ISuite> suiteGraph = new DynamicGraph();
            Iterator i$ = this.m_suites.iterator();

            while(i$.hasNext()) {
               XmlSuite xmlSuite = (XmlSuite)i$.next();
               this.populateSuiteGraph(suiteGraph, suiteRunnerMap, xmlSuite);
            }

            IThreadWorkerFactory<ISuite> factory = new SuiteWorkerFactory(suiteRunnerMap, 0, this.getDefaultSuiteName());
            GraphThreadPoolExecutor<ISuite> pooledExecutor = new GraphThreadPoolExecutor(suiteGraph, factory, this.m_suiteThreadPoolSize, this.m_suiteThreadPoolSize, 2147483647L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
            Utils.log("TestNG", 2, "Starting executor for all suites");
            pooledExecutor.run();

            try {
               pooledExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
               pooledExecutor.shutdownNow();
            } catch (InterruptedException var6) {
               Thread.currentThread().interrupt();
               error("Error waiting for concurrent executors to finish " + var6.getMessage());
            }
         }
      } else {
         this.setStatus(8);
         error("No test suite found. Nothing to run");
         usage();
      }

      return Lists.newArrayList(suiteRunnerMap.values());
   }

   private static void error(String s) {
      LOGGER.error(s);
   }

   private int getVerbose(XmlSuite xmlSuite) {
      int result = xmlSuite.getVerbose() != null ? xmlSuite.getVerbose() : (this.m_verbose != null ? this.m_verbose : DEFAULT_VERBOSE);
      return result;
   }

   private void runSuitesSequentially(XmlSuite xmlSuite, SuiteRunnerMap suiteRunnerMap, int verbose, String defaultSuiteName) {
      Iterator i$ = xmlSuite.getChildSuites().iterator();

      while(i$.hasNext()) {
         XmlSuite childSuite = (XmlSuite)i$.next();
         this.runSuitesSequentially(childSuite, suiteRunnerMap, verbose, defaultSuiteName);
      }

      SuiteRunnerWorker srw = new SuiteRunnerWorker(suiteRunnerMap.get(xmlSuite), suiteRunnerMap, verbose, defaultSuiteName);
      srw.run();
   }

   private void populateSuiteGraph(DynamicGraph<ISuite> suiteGraph, SuiteRunnerMap suiteRunnerMap, XmlSuite xmlSuite) {
      ISuite parentSuiteRunner = suiteRunnerMap.get(xmlSuite);
      if (xmlSuite.getChildSuites().isEmpty()) {
         suiteGraph.addNode(parentSuiteRunner);
      } else {
         Iterator i$ = xmlSuite.getChildSuites().iterator();

         while(i$.hasNext()) {
            XmlSuite childSuite = (XmlSuite)i$.next();
            suiteGraph.addEdge(parentSuiteRunner, suiteRunnerMap.get(childSuite));
            this.populateSuiteGraph(suiteGraph, suiteRunnerMap, childSuite);
         }
      }

   }

   private void createSuiteRunners(SuiteRunnerMap suiteRunnerMap, XmlSuite xmlSuite) {
      if (null != this.m_isJUnit && !this.m_isJUnit.equals(XmlSuite.DEFAULT_JUNIT)) {
         xmlSuite.setJUnit(this.m_isJUnit);
      }

      if (null != this.m_skipFailedInvocationCounts) {
         xmlSuite.setSkipFailedInvocationCounts(this.m_skipFailedInvocationCounts);
      }

      if (this.m_verbose != null) {
         xmlSuite.setVerbose(this.m_verbose);
      }

      if (null != this.m_configFailurePolicy) {
         xmlSuite.setConfigFailurePolicy(this.m_configFailurePolicy);
      }

      Iterator i$ = xmlSuite.getTests().iterator();

      while(i$.hasNext()) {
         XmlTest t = (XmlTest)i$.next();
         Iterator i$ = this.m_methodDescriptors.entrySet().iterator();

         while(i$.hasNext()) {
            Entry<String, Integer> ms = (Entry)i$.next();
            XmlMethodSelector xms = new XmlMethodSelector();
            xms.setName((String)ms.getKey());
            xms.setPriority((Integer)ms.getValue());
            t.getMethodSelectors().add(xms);
         }
      }

      suiteRunnerMap.put(xmlSuite, this.createSuiteRunner(xmlSuite));
      i$ = xmlSuite.getChildSuites().iterator();

      while(i$.hasNext()) {
         XmlSuite childSuite = (XmlSuite)i$.next();
         this.createSuiteRunners(suiteRunnerMap, childSuite);
      }

   }

   private SuiteRunner createSuiteRunner(XmlSuite xmlSuite) {
      SuiteRunner result = new SuiteRunner(this.getConfiguration(), xmlSuite, this.m_outputDir, this.m_testRunnerFactory, this.m_useDefaultListeners, this.m_methodInterceptors, this.m_invokedMethodListeners, this.m_testListeners);
      Iterator i$ = this.m_suiteListeners.iterator();

      while(i$.hasNext()) {
         ISuiteListener isl = (ISuiteListener)i$.next();
         result.addListener(isl);
      }

      i$ = result.getReporters().iterator();

      while(i$.hasNext()) {
         IReporter r = (IReporter)i$.next();
         this.addListener(r);
      }

      i$ = this.m_configuration.getConfigurationListeners().iterator();

      while(i$.hasNext()) {
         IConfigurationListener cl = (IConfigurationListener)i$.next();
         result.addConfigurationListener(cl);
      }

      return result;
   }

   protected IConfiguration getConfiguration() {
      return this.m_configuration;
   }

   public static void main(String[] argv) {
      TestNG testng = privateMain(argv, (ITestListener)null);
      System.exit(testng.getStatus());
   }

   public static TestNG privateMain(String[] argv, ITestListener listener) {
      TestNG result = new TestNG();
      if (null != listener) {
         result.addListener(listener);
      }

      try {
         CommandLineArgs cla = new CommandLineArgs();
         m_jCommander = new JCommander(cla, argv);
         validateCommandLineParameters(cla);
         result.configure(cla);
      } catch (ParameterException var4) {
         exitWithError(var4.getMessage());
      }

      try {
         result.run();
      } catch (TestNGException var5) {
         if (TestRunner.getVerbose() > 1) {
            var5.printStackTrace(System.out);
         } else {
            error(var5.getMessage());
         }

         result.setStatus(1);
      }

      return result;
   }

   protected void configure(CommandLineArgs cla) {
      if (cla.verbose != null) {
         this.setVerbose(cla.verbose);
      }

      this.setOutputDirectory(cla.outputDirectory);
      String testClasses = cla.testClass;
      String[] strs;
      int i$;
      int len$;
      if (null != testClasses) {
         strs = testClasses.split(",");
         List<Class> classes = Lists.newArrayList();
         String[] arr$ = strs;
         i$ = strs.length;

         for(len$ = 0; len$ < i$; ++len$) {
            String c = arr$[len$];
            classes.add(ClassHelper.fileToClass(c));
         }

         this.setTestClasses((Class[])classes.toArray(new Class[classes.size()]));
      }

      this.setOutputDirectory(cla.outputDirectory);
      if (cla.testNames != null) {
         this.setTestNames(Arrays.asList(cla.testNames.split(",")));
      }

      if (cla.useDefaultListeners != null) {
         this.setUseDefaultListeners("true".equalsIgnoreCase(cla.useDefaultListeners));
      }

      this.setGroups(cla.groups);
      this.setExcludedGroups(cla.excludedGroups);
      this.setTestJar(cla.testJar);
      this.setXmlPathInJar(cla.xmlPathInJar);
      this.setJUnit(cla.junit);
      this.setMixed(cla.mixed);
      this.setMaster(cla.master);
      this.setSlave(cla.slave);
      this.setSkipFailedInvocationCounts(cla.skipFailedInvocationCounts);
      if (cla.parallelMode != null) {
         this.setParallel(cla.parallelMode);
      }

      if (cla.configFailurePolicy != null) {
         this.setConfigFailurePolicy(cla.configFailurePolicy);
      }

      if (cla.threadCount != null) {
         this.setThreadCount(cla.threadCount);
      }

      if (cla.dataProviderThreadCount != null) {
         this.setDataProviderThreadCount(cla.dataProviderThreadCount);
      }

      if (cla.suiteName != null) {
         this.setDefaultSuiteName(cla.suiteName);
      }

      if (cla.testName != null) {
         this.setDefaultTestName(cla.testName);
      }

      String[] arr$;
      if (cla.listener != null) {
         String sep = ";";
         if (cla.listener.contains(",")) {
            sep = ",";
         }

         arr$ = Utils.split(cla.listener, sep);
         List<Class> classes = Lists.newArrayList();
         String[] arr$ = arr$;
         len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String cls = arr$[i$];
            classes.add(ClassHelper.fileToClass(cls));
         }

         this.setListenerClasses(classes);
      }

      if (null != cla.methodSelectors) {
         strs = Utils.split(cla.methodSelectors, ",");
         arr$ = strs;
         int len$ = strs.length;

         for(i$ = 0; i$ < len$; ++i$) {
            String cls = arr$[i$];
            String[] sel = Utils.split(cls, ":");

            try {
               if (sel.length == 2) {
                  this.addMethodSelector(sel[0], Integer.valueOf(sel[1]));
               } else {
                  error("Method selector value was not in the format org.example.Selector:4");
               }
            } catch (NumberFormatException var10) {
               error("Method selector value was not in the format org.example.Selector:4");
            }
         }
      }

      if (cla.objectFactory != null) {
         this.setObjectFactory(ClassHelper.fileToClass(cla.objectFactory));
      }

      if (cla.testRunnerFactory != null) {
         this.setTestRunnerFactoryClass(ClassHelper.fileToClass(cla.testRunnerFactory));
      }

      if (cla.reporter != null) {
         ReporterConfig reporterConfig = ReporterConfig.deserialize(cla.reporter);
         this.addReporter(reporterConfig);
      }

      if (cla.commandLineMethods.size() > 0) {
         this.m_commandLineMethods = cla.commandLineMethods;
      }

      if (cla.suiteFiles != null) {
         this.setTestSuites(cla.suiteFiles);
      }

      this.setSuiteThreadPoolSize(cla.suiteThreadPoolSize);
      this.setRandomizeSuites(cla.randomizeSuites);
   }

   public void setSuiteThreadPoolSize(Integer suiteThreadPoolSize) {
      this.m_suiteThreadPoolSize = suiteThreadPoolSize;
   }

   public Integer getSuiteThreadPoolSize() {
      return this.m_suiteThreadPoolSize;
   }

   public void setRandomizeSuites(boolean randomizeSuites) {
      this.m_randomizeSuites = randomizeSuites;
   }

   public void setSourcePath(String path) {
   }

   /** @deprecated */
   @Deprecated
   public void configure(Map cmdLineArgs) {
      CommandLineArgs result = new CommandLineArgs();
      Integer verbose = (Integer)cmdLineArgs.get("-log");
      if (null != verbose) {
         result.verbose = verbose;
      }

      result.outputDirectory = (String)cmdLineArgs.get("-d");
      String testClasses = (String)cmdLineArgs.get("-testclass");
      if (null != testClasses) {
         result.testClass = testClasses;
      }

      String testNames = (String)cmdLineArgs.get("-testnames");
      if (testNames != null) {
         result.testNames = testNames;
      }

      String useDefaultListeners = (String)cmdLineArgs.get("-usedefaultlisteners");
      if (null != useDefaultListeners) {
         result.useDefaultListeners = useDefaultListeners;
      }

      result.groups = (String)cmdLineArgs.get("-groups");
      result.excludedGroups = (String)cmdLineArgs.get("-excludegroups");
      result.testJar = (String)cmdLineArgs.get("-testjar");
      result.xmlPathInJar = (String)cmdLineArgs.get("-xmlpathinjar");
      result.junit = (Boolean)cmdLineArgs.get("-junit");
      result.mixed = (Boolean)cmdLineArgs.get("-mixed");
      result.master = (String)cmdLineArgs.get("-master");
      result.slave = (String)cmdLineArgs.get("-slave");
      result.skipFailedInvocationCounts = (Boolean)cmdLineArgs.get("-skipfailedinvocationcounts");
      String parallelMode = (String)cmdLineArgs.get("-parallel");
      if (parallelMode != null) {
         result.parallelMode = parallelMode;
      }

      String threadCount = (String)cmdLineArgs.get("-threadcount");
      if (threadCount != null) {
         result.threadCount = Integer.parseInt(threadCount);
      }

      Integer dptc = (Integer)cmdLineArgs.get("-dataproviderthreadcount");
      if (dptc != null) {
         result.dataProviderThreadCount = dptc;
      }

      String defaultSuiteName = (String)cmdLineArgs.get("-suitename");
      if (defaultSuiteName != null) {
         result.suiteName = defaultSuiteName;
      }

      String defaultTestName = (String)cmdLineArgs.get("-testname");
      if (defaultTestName != null) {
         result.testName = defaultTestName;
      }

      Object listeners = cmdLineArgs.get("-listener");
      if (listeners instanceof List) {
         result.listener = Utils.join((List)listeners, ",");
      } else {
         result.listener = (String)listeners;
      }

      String ms = (String)cmdLineArgs.get("-methodselectors");
      if (null != ms) {
         result.methodSelectors = ms;
      }

      String objectFactory = (String)cmdLineArgs.get("-objectfactory");
      if (null != objectFactory) {
         result.objectFactory = objectFactory;
      }

      String runnerFactory = (String)cmdLineArgs.get("-testrunfactory");
      if (null != runnerFactory) {
         result.testRunnerFactory = runnerFactory;
      }

      String reporterConfigs = (String)cmdLineArgs.get("-reporter");
      if (reporterConfigs != null) {
         result.reporter = reporterConfigs;
      }

      String failurePolicy = (String)cmdLineArgs.get("-configfailurepolicy");
      if (failurePolicy != null) {
         result.configFailurePolicy = failurePolicy;
      }

      this.configure(result);
   }

   public void setTestNames(List<String> testNames) {
      this.m_testNames = testNames;
   }

   public void setSkipFailedInvocationCounts(Boolean skip) {
      this.m_skipFailedInvocationCounts = skip;
   }

   private void addReporter(ReporterConfig reporterConfig) {
      Object instance = reporterConfig.newReporterInstance();
      if (instance != null) {
         this.addListener(instance);
      } else {
         LOGGER.warn("Could not find reporte class : " + reporterConfig.getClassName());
      }

   }

   public void setMaster(String fileName) {
      this.m_masterfileName = fileName;
   }

   public void setSlave(String fileName) {
      this.m_slavefileName = fileName;
   }

   public void setJUnit(Boolean isJUnit) {
      this.m_isJUnit = isJUnit;
   }

   public void setMixed(Boolean isMixed) {
      if (isMixed != null) {
         this.m_isMixed = isMixed;
      }
   }

   /** @deprecated */
   @Deprecated
   public static void setTestNGVersion() {
      LOGGER.info("setTestNGVersion has been deprecated.");
   }

   /** @deprecated */
   @Deprecated
   public static boolean isJdk14() {
      return false;
   }

   protected static void validateCommandLineParameters(CommandLineArgs args) {
      String testClasses = args.testClass;
      List<String> testNgXml = args.suiteFiles;
      String testJar = args.testJar;
      String slave = args.slave;
      List<String> methods = args.commandLineMethods;
      if (testClasses != null || slave != null || testJar != null || testNgXml != null && !testNgXml.isEmpty() || methods != null && !methods.isEmpty()) {
         String groups = args.groups;
         String excludedGroups = args.excludedGroups;
         if (testJar != null || null == groups && null == excludedGroups || testClasses != null || testNgXml != null && !testNgXml.isEmpty()) {
            if (args.slave != null && args.master != null) {
               throw new ParameterException("-slave can't be combined with -master");
            } else {
               Boolean junit = args.junit;
               Boolean mixed = args.mixed;
               if (junit && mixed) {
                  throw new ParameterException("-mixed can't be combined with -junit");
               }
            }
         } else {
            throw new ParameterException("Groups option should be used with testclass option");
         }
      } else {
         throw new ParameterException("You need to specify at least one testng.xml, one class or one method");
      }
   }

   public boolean hasFailure() {
      return (this.getStatus() & 1) == 1;
   }

   public boolean hasFailureWithinSuccessPercentage() {
      return (this.getStatus() & 4) == 4;
   }

   public boolean hasSkip() {
      return (this.getStatus() & 2) == 2;
   }

   static void exitWithError(String msg) {
      System.err.println(msg);
      usage();
      System.exit(1);
   }

   public String getOutputDirectory() {
      return this.m_outputDir;
   }

   public IAnnotationTransformer getAnnotationTransformer() {
      return this.m_annotationTransformer;
   }

   public void setAnnotationTransformer(IAnnotationTransformer t) {
      if (this.m_annotationTransformer != this.m_defaultAnnoProcessor && this.m_annotationTransformer != t) {
         LOGGER.warn("AnnotationTransformer already set");
      }

      this.m_annotationTransformer = t;
   }

   public String getDefaultSuiteName() {
      return this.m_defaultSuiteName;
   }

   public void setDefaultSuiteName(String defaultSuiteName) {
      this.m_defaultSuiteName = defaultSuiteName;
   }

   public String getDefaultTestName() {
      return this.m_defaultTestName;
   }

   public void setDefaultTestName(String defaultTestName) {
      this.m_defaultTestName = defaultTestName;
   }

   public void setConfigFailurePolicy(String failurePolicy) {
      this.m_configFailurePolicy = failurePolicy;
   }

   public String getConfigFailurePolicy() {
      return this.m_configFailurePolicy;
   }

   /** @deprecated */
   @Deprecated
   public static TestNG getDefault() {
      return m_instance;
   }

   /** @deprecated */
   @Deprecated
   public void setHasFailure(boolean hasFailure) {
      this.m_status |= 1;
   }

   /** @deprecated */
   @Deprecated
   public void setHasFailureWithinSuccessPercentage(boolean hasFailureWithinSuccessPercentage) {
      this.m_status |= 4;
   }

   /** @deprecated */
   @Deprecated
   public void setHasSkip(boolean hasSkip) {
      this.m_status |= 2;
   }

   private void setConfigurable(IConfigurable c) {
      if (this.m_configurable != null && this.m_configurable != c) {
         LOGGER.warn("Configurable already set");
      }

      this.m_configurable = c;
   }

   private void setHookable(IHookable h) {
      if (this.m_hookable != null && this.m_hookable != h) {
         LOGGER.warn("Hookable already set");
      }

      this.m_hookable = h;
   }

   public void setMethodInterceptor(IMethodInterceptor methodInterceptor) {
      this.m_methodInterceptors.add(methodInterceptor);
   }

   public void setDataProviderThreadCount(int count) {
      this.m_dataProviderThreadCount = count;
   }

   public void addClassLoader(ClassLoader loader) {
      if (loader != null) {
         ClassHelper.addClassLoader(loader);
      }

   }

   public void setPreserveOrder(boolean b) {
      this.m_preserveOrder = b;
   }

   protected long getStart() {
      return this.m_start;
   }

   protected long getEnd() {
      return this.m_end;
   }

   public void setGroupByInstances(boolean b) {
      this.m_groupByInstances = b;
   }

   public void setServiceLoaderClassLoader(URLClassLoader ucl) {
      this.m_serviceLoaderClassLoader = ucl;
   }

   private void addServiceLoaderListener(ITestNGListener l) {
      this.m_serviceLoaderListeners.add(l);
   }

   public List<ITestNGListener> getServiceLoaderListeners() {
      return this.m_serviceLoaderListeners;
   }

   public static class ExitCodeListener implements IResultListener2 {
      private TestNG m_mainRunner;

      public ExitCodeListener() {
         this.m_mainRunner = TestNG.m_instance;
      }

      public ExitCodeListener(TestNG runner) {
         this.m_mainRunner = runner;
      }

      public void beforeConfiguration(ITestResult tr) {
      }

      public void onTestFailure(ITestResult result) {
         this.setHasRunTests();
         this.m_mainRunner.setStatus(1);
      }

      public void onTestSkipped(ITestResult result) {
         this.setHasRunTests();
         if ((this.m_mainRunner.getStatus() & 1) != 0) {
            this.m_mainRunner.setStatus(2);
         }

      }

      public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
         this.setHasRunTests();
         this.m_mainRunner.setStatus(4);
      }

      public void onTestSuccess(ITestResult result) {
         this.setHasRunTests();
      }

      public void onStart(ITestContext context) {
         this.setHasRunTests();
      }

      public void onFinish(ITestContext context) {
      }

      public void onTestStart(ITestResult result) {
         this.setHasRunTests();
      }

      private void setHasRunTests() {
         this.m_mainRunner.m_hasTests = true;
      }

      public void onConfigurationFailure(ITestResult itr) {
         this.m_mainRunner.setStatus(1);
      }

      public void onConfigurationSkip(ITestResult itr) {
         this.m_mainRunner.setStatus(2);
      }

      public void onConfigurationSuccess(ITestResult itr) {
      }
   }
}
