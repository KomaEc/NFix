package org.testng;

import com.google.inject.Injector;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.testng.internal.Attributes;
import org.testng.internal.IConfiguration;
import org.testng.internal.IInvoker;
import org.testng.internal.Utils;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.internal.thread.ThreadUtil;
import org.testng.reporters.JUnitXMLReporter;
import org.testng.reporters.TestHTMLReporter;
import org.testng.reporters.TextReporter;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class SuiteRunner implements ISuite, Serializable, IInvokedMethodListener {
   private static final long serialVersionUID = 5284208932089503131L;
   private static final String DEFAULT_OUTPUT_DIR = "test-output";
   private Map<String, ISuiteResult> m_suiteResults;
   private transient List<TestRunner> m_testRunners;
   private transient List<ISuiteListener> m_listeners;
   private transient TestListenerAdapter m_textReporter;
   private String m_outputDir;
   private XmlSuite m_suite;
   private Injector m_parentInjector;
   private transient List<ITestListener> m_testListeners;
   private transient ITestRunnerFactory m_tmpRunnerFactory;
   private transient ITestRunnerFactory m_runnerFactory;
   private transient boolean m_useDefaultListeners;
   private String m_host;
   private transient IConfiguration m_configuration;
   private transient ITestObjectFactory m_objectFactory;
   private transient Boolean m_skipFailedInvocationCounts;
   private transient List<IMethodInterceptor> m_methodInterceptors;
   private List<IInvokedMethodListener> m_invokedMethodListeners;
   private List<IInvokedMethod> m_invokedMethods;
   private List<ITestNGMethod> m_allTestMethods;
   private List<IReporter> m_reporters;
   private SuiteRunState m_suiteState;
   private IAttributes m_attributes;

   public SuiteRunner(IConfiguration configuration, XmlSuite suite, String outputDir) {
      this(configuration, suite, outputDir, (ITestRunnerFactory)null);
   }

   public SuiteRunner(IConfiguration configuration, XmlSuite suite, String outputDir, ITestRunnerFactory runnerFactory) {
      this(configuration, suite, outputDir, runnerFactory, false);
   }

   public SuiteRunner(IConfiguration configuration, XmlSuite suite, String outputDir, ITestRunnerFactory runnerFactory, boolean useDefaultListeners) {
      this(configuration, suite, outputDir, runnerFactory, useDefaultListeners, new ArrayList(), (List)null, (List)null);
   }

   protected SuiteRunner(IConfiguration configuration, XmlSuite suite, String outputDir, ITestRunnerFactory runnerFactory, boolean useDefaultListeners, List<IMethodInterceptor> methodInterceptors, List<IInvokedMethodListener> invokedMethodListeners, List<ITestListener> testListeners) {
      this.m_suiteResults = Collections.synchronizedMap(Maps.newLinkedHashMap());
      this.m_testRunners = Lists.newArrayList();
      this.m_listeners = Lists.newArrayList();
      this.m_textReporter = new TestListenerAdapter();
      this.m_testListeners = Lists.newArrayList();
      this.m_useDefaultListeners = true;
      this.m_skipFailedInvocationCounts = Boolean.FALSE;
      this.m_invokedMethods = Collections.synchronizedList(Lists.newArrayList());
      this.m_allTestMethods = Lists.newArrayList();
      this.m_reporters = Lists.newArrayList();
      this.m_suiteState = new SuiteRunState();
      this.m_attributes = new Attributes();
      this.init(configuration, suite, outputDir, runnerFactory, useDefaultListeners, methodInterceptors, invokedMethodListeners, testListeners);
   }

   private void init(IConfiguration configuration, XmlSuite suite, String outputDir, ITestRunnerFactory runnerFactory, boolean useDefaultListeners, List<IMethodInterceptor> methodInterceptors, List<IInvokedMethodListener> invokedMethodListener, List<ITestListener> testListeners) {
      this.m_configuration = configuration;
      this.m_suite = suite;
      this.m_useDefaultListeners = useDefaultListeners;
      this.m_tmpRunnerFactory = runnerFactory;
      this.m_methodInterceptors = (List)(methodInterceptors != null ? methodInterceptors : new ArrayList());
      this.setOutputDir(outputDir);
      this.m_objectFactory = this.m_configuration.getObjectFactory();
      if (this.m_objectFactory == null) {
         this.m_objectFactory = suite.getObjectFactory();
      }

      this.m_invokedMethodListeners = invokedMethodListener;
      if (this.m_invokedMethodListeners == null) {
         this.m_invokedMethodListeners = Lists.newArrayList();
      }

      this.m_invokedMethodListeners.add(this);
      this.m_skipFailedInvocationCounts = suite.skipFailedInvocationCounts();
      if (null != testListeners) {
         this.m_testListeners.addAll(testListeners);
      }

      this.m_runnerFactory = this.buildRunnerFactory();
      List<XmlTest> xmlTests = this.m_suite.getTests();
      Collections.sort(xmlTests, new Comparator<XmlTest>() {
         public int compare(XmlTest arg0, XmlTest arg1) {
            return arg0.getIndex() - arg1.getIndex();
         }
      });
      Iterator i$ = xmlTests.iterator();

      while(i$.hasNext()) {
         XmlTest test = (XmlTest)i$.next();
         TestRunner tr = this.m_runnerFactory.newTestRunner(this, test, this.m_invokedMethodListeners);
         Iterator i$ = this.m_methodInterceptors.iterator();

         while(i$.hasNext()) {
            IMethodInterceptor methodInterceptor = (IMethodInterceptor)i$.next();
            tr.addMethodInterceptor(methodInterceptor);
         }

         tr.addListener(this.m_textReporter);
         this.m_testRunners.add(tr);
         this.m_allTestMethods.addAll(Arrays.asList(tr.getAllTestMethods()));
      }

   }

   public XmlSuite getXmlSuite() {
      return this.m_suite;
   }

   public String getName() {
      return this.m_suite.getName();
   }

   public void setObjectFactory(ITestObjectFactory objectFactory) {
      this.m_objectFactory = objectFactory;
   }

   public void setReportResults(boolean reportResults) {
      this.m_useDefaultListeners = reportResults;
   }

   private void invokeListeners(boolean start) {
      Iterator i$ = this.m_listeners.iterator();

      while(i$.hasNext()) {
         ISuiteListener sl = (ISuiteListener)i$.next();
         if (start) {
            sl.onStart(this);
         } else {
            sl.onFinish(this);
         }
      }

   }

   private void setOutputDir(String outputdir) {
      if (Utils.isStringBlank(outputdir) && this.m_useDefaultListeners) {
         outputdir = "test-output";
      }

      this.m_outputDir = null != outputdir ? (new File(outputdir)).getAbsolutePath() : null;
   }

   private ITestRunnerFactory buildRunnerFactory() {
      ITestRunnerFactory factory = null;
      if (null == this.m_tmpRunnerFactory) {
         factory = new SuiteRunner.DefaultTestRunnerFactory(this.m_configuration, (ITestListener[])this.m_testListeners.toArray(new ITestListener[this.m_testListeners.size()]), this.m_useDefaultListeners, this.m_skipFailedInvocationCounts);
      } else {
         factory = new SuiteRunner.ProxyTestRunnerFactory((ITestListener[])this.m_testListeners.toArray(new ITestListener[this.m_testListeners.size()]), this.m_tmpRunnerFactory);
      }

      return (ITestRunnerFactory)factory;
   }

   public String getParallel() {
      return this.m_suite.getParallel();
   }

   public String getParentModule() {
      return this.m_suite.getParentModule();
   }

   public String getGuiceStage() {
      return this.m_suite.getGuiceStage();
   }

   public Injector getParentInjector() {
      return this.m_parentInjector;
   }

   public void setParentInjector(Injector injector) {
      this.m_parentInjector = injector;
   }

   public void run() {
      this.invokeListeners(true);

      try {
         this.privateRun();
      } finally {
         this.invokeListeners(false);
      }

   }

   private void privateRun() {
      Map<Method, ITestNGMethod> beforeSuiteMethods = new LinkedHashMap();
      Map<Method, ITestNGMethod> afterSuiteMethods = new LinkedHashMap();
      IInvoker invoker = null;
      Iterator i$ = this.m_testRunners.iterator();

      while(i$.hasNext()) {
         TestRunner tr = (TestRunner)i$.next();
         invoker = tr.getInvoker();
         ITestNGMethod[] arr$ = tr.getBeforeSuiteMethods();
         int len$ = arr$.length;

         int i$;
         ITestNGMethod m;
         for(i$ = 0; i$ < len$; ++i$) {
            m = arr$[i$];
            beforeSuiteMethods.put(m.getMethod(), m);
         }

         arr$ = tr.getAfterSuiteMethods();
         len$ = arr$.length;

         for(i$ = 0; i$ < len$; ++i$) {
            m = arr$[i$];
            afterSuiteMethods.put(m.getMethod(), m);
         }
      }

      if (invoker != null) {
         if (beforeSuiteMethods.values().size() > 0) {
            invoker.invokeConfigurations((IClass)null, (ITestNGMethod[])beforeSuiteMethods.values().toArray(new ITestNGMethod[beforeSuiteMethods.size()]), this.m_suite, this.m_suite.getParameters(), (Object[])null, (Object)null);
         }

         Utils.log("SuiteRunner", 3, "Created " + this.m_testRunners.size() + " TestRunners");
         boolean testsInParallel = "tests".equals(this.m_suite.getParallel());
         if (!testsInParallel) {
            this.runSequentially();
         } else {
            this.runInParallelTestMode();
         }

         if (afterSuiteMethods.values().size() > 0) {
            invoker.invokeConfigurations((IClass)null, (ITestNGMethod[])afterSuiteMethods.values().toArray(new ITestNGMethod[afterSuiteMethods.size()]), this.m_suite, this.m_suite.getAllParameters(), (Object[])null, (Object)null);
         }
      }

   }

   private void addReporter(IReporter listener) {
      this.m_reporters.add(listener);
   }

   void addConfigurationListener(IConfigurationListener listener) {
      this.m_configuration.addConfigurationListener(listener);
   }

   public List<IReporter> getReporters() {
      return this.m_reporters;
   }

   private void runSequentially() {
      Iterator i$ = this.m_testRunners.iterator();

      while(i$.hasNext()) {
         TestRunner tr = (TestRunner)i$.next();
         this.runTest(tr);
      }

   }

   private void runTest(TestRunner tr) {
      tr.run();
      ISuiteResult sr = new SuiteResult(this.m_suite, tr);
      this.m_suiteResults.put(tr.getName(), sr);
   }

   private void runInParallelTestMode() {
      List<Runnable> tasks = Lists.newArrayList(this.m_testRunners.size());
      Iterator i$ = this.m_testRunners.iterator();

      while(i$.hasNext()) {
         TestRunner tr = (TestRunner)i$.next();
         tasks.add(new SuiteRunner.SuiteWorker(tr));
      }

      ThreadUtil.execute(tasks, this.m_suite.getThreadCount(), this.m_suite.getTimeOut((long)XmlTest.DEFAULT_TIMEOUT_MS), false);
   }

   protected void addListener(ISuiteListener reporter) {
      this.m_listeners.add(reporter);
   }

   public void addListener(ITestNGListener listener) {
      if (listener instanceof IInvokedMethodListener) {
         this.m_invokedMethodListeners.add((IInvokedMethodListener)listener);
      }

      if (listener instanceof ISuiteListener) {
         this.addListener((ISuiteListener)listener);
      }

      if (listener instanceof IReporter) {
         this.addReporter((IReporter)listener);
      }

      if (listener instanceof IConfigurationListener) {
         this.addConfigurationListener((IConfigurationListener)listener);
      }

   }

   public String getOutputDirectory() {
      return this.m_outputDir + File.separatorChar + this.getName();
   }

   public Map<String, ISuiteResult> getResults() {
      return this.m_suiteResults;
   }

   public String getParameter(String parameterName) {
      return this.m_suite.getParameter(parameterName);
   }

   public Map<String, Collection<ITestNGMethod>> getMethodsByGroups() {
      Map<String, Collection<ITestNGMethod>> result = Maps.newHashMap();
      Iterator i$ = this.m_testRunners.iterator();

      while(i$.hasNext()) {
         TestRunner tr = (TestRunner)i$.next();
         ITestNGMethod[] methods = tr.getAllTestMethods();
         ITestNGMethod[] arr$ = methods;
         int len$ = methods.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            ITestNGMethod m = arr$[i$];
            String[] groups = m.getGroups();
            String[] arr$ = groups;
            int len$ = groups.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               String groupName = arr$[i$];
               Collection<ITestNGMethod> testMethods = (Collection)result.get(groupName);
               if (null == testMethods) {
                  testMethods = Lists.newArrayList();
                  result.put(groupName, testMethods);
               }

               ((Collection)testMethods).add(m);
            }
         }
      }

      return result;
   }

   public Collection<ITestNGMethod> getInvokedMethods() {
      return this.getIncludedOrExcludedMethods(true);
   }

   public Collection<ITestNGMethod> getExcludedMethods() {
      return this.getIncludedOrExcludedMethods(false);
   }

   private Collection<ITestNGMethod> getIncludedOrExcludedMethods(boolean included) {
      List<ITestNGMethod> result = Lists.newArrayList();
      Iterator i$ = this.m_testRunners.iterator();

      while(i$.hasNext()) {
         TestRunner tr = (TestRunner)i$.next();
         Collection<ITestNGMethod> methods = included ? tr.getInvokedMethods() : tr.getExcludedMethods();
         Iterator i$ = ((Collection)methods).iterator();

         while(i$.hasNext()) {
            ITestNGMethod m = (ITestNGMethod)i$.next();
            result.add(m);
         }
      }

      return result;
   }

   public IObjectFactory getObjectFactory() {
      return this.m_objectFactory instanceof IObjectFactory ? (IObjectFactory)this.m_objectFactory : null;
   }

   public IObjectFactory2 getObjectFactory2() {
      return this.m_objectFactory instanceof IObjectFactory2 ? (IObjectFactory2)this.m_objectFactory : null;
   }

   public IAnnotationFinder getAnnotationFinder() {
      return this.m_configuration.getAnnotationFinder();
   }

   public static void ppp(String s) {
      System.out.println("[SuiteRunner] " + s);
   }

   public void setHost(String host) {
      this.m_host = host;
   }

   public String getHost() {
      return this.m_host;
   }

   public SuiteRunState getSuiteState() {
      return this.m_suiteState;
   }

   public void setSkipFailedInvocationCounts(Boolean skipFailedInvocationCounts) {
      if (skipFailedInvocationCounts != null) {
         this.m_skipFailedInvocationCounts = skipFailedInvocationCounts;
      }

   }

   public Object getAttribute(String name) {
      return this.m_attributes.getAttribute(name);
   }

   public void setAttribute(String name, Object value) {
      this.m_attributes.setAttribute(name, value);
   }

   public Set<String> getAttributeNames() {
      return this.m_attributes.getAttributeNames();
   }

   public Object removeAttribute(String name) {
      return this.m_attributes.removeAttribute(name);
   }

   public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
   }

   public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
      if (method == null) {
         throw new NullPointerException("Method should not be null");
      } else {
         this.m_invokedMethods.add(method);
      }
   }

   public List<IInvokedMethod> getAllInvokedMethods() {
      return this.m_invokedMethods;
   }

   public List<ITestNGMethod> getAllMethods() {
      return this.m_allTestMethods;
   }

   private static class ProxyTestRunnerFactory implements ITestRunnerFactory {
      private ITestListener[] m_failureGenerators;
      private ITestRunnerFactory m_target;

      public ProxyTestRunnerFactory(ITestListener[] failureListeners, ITestRunnerFactory target) {
         this.m_failureGenerators = failureListeners;
         this.m_target = target;
      }

      public TestRunner newTestRunner(ISuite suite, XmlTest test, List<IInvokedMethodListener> listeners) {
         TestRunner testRunner = this.m_target.newTestRunner(suite, test, listeners);
         testRunner.addListener(new TextReporter(testRunner.getName(), TestRunner.getVerbose()));
         ITestListener[] arr$ = this.m_failureGenerators;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            ITestListener itl = arr$[i$];
            testRunner.addListener(itl);
         }

         return testRunner;
      }
   }

   private static class DefaultTestRunnerFactory implements ITestRunnerFactory {
      private ITestListener[] m_failureGenerators;
      private boolean m_useDefaultListeners;
      private boolean m_skipFailedInvocationCounts;
      private IConfiguration m_configuration;

      public DefaultTestRunnerFactory(IConfiguration configuration, ITestListener[] failureListeners, boolean useDefaultListeners, boolean skipFailedInvocationCounts) {
         this.m_configuration = configuration;
         this.m_failureGenerators = failureListeners;
         this.m_useDefaultListeners = useDefaultListeners;
         this.m_skipFailedInvocationCounts = skipFailedInvocationCounts;
      }

      public TestRunner newTestRunner(ISuite suite, XmlTest test, List<IInvokedMethodListener> listeners) {
         boolean skip = this.m_skipFailedInvocationCounts;
         if (!skip) {
            skip = test.skipFailedInvocationCounts();
         }

         TestRunner testRunner = new TestRunner(this.m_configuration, suite, test, suite.getOutputDirectory(), suite.getAnnotationFinder(), skip, listeners);
         if (this.m_useDefaultListeners) {
            testRunner.addListener(new TestHTMLReporter());
            testRunner.addListener(new JUnitXMLReporter());
            testRunner.addListener(new TextReporter(testRunner.getName(), TestRunner.getVerbose()));
         }

         ITestListener[] arr$ = this.m_failureGenerators;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            ITestListener itl = arr$[i$];
            testRunner.addListener(itl);
         }

         Iterator i$ = this.m_configuration.getConfigurationListeners().iterator();

         while(i$.hasNext()) {
            IConfigurationListener cl = (IConfigurationListener)i$.next();
            testRunner.addConfigurationListener(cl);
         }

         return testRunner;
      }
   }

   private class SuiteWorker implements Runnable {
      private TestRunner m_testRunner;

      public SuiteWorker(TestRunner tr) {
         this.m_testRunner = tr;
      }

      public void run() {
         Utils.log("[SuiteWorker]", 4, "Running XML Test '" + this.m_testRunner.getTest().getName() + "' in Parallel");
         SuiteRunner.this.runTest(this.m_testRunner);
      }
   }
}
