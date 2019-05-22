package org.testng;

import com.google.inject.Injector;
import com.google.inject.Module;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.testng.annotations.Guice;
import org.testng.annotations.ITestAnnotation;
import org.testng.collections.ListMultiMap;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.testng.collections.Sets;
import org.testng.internal.Attributes;
import org.testng.internal.ClassHelper;
import org.testng.internal.ClassImpl;
import org.testng.internal.ClassInfoMap;
import org.testng.internal.ConfigurationGroupMethods;
import org.testng.internal.Constants;
import org.testng.internal.DynamicGraph;
import org.testng.internal.IConfiguration;
import org.testng.internal.IInvoker;
import org.testng.internal.ITestResultNotifier;
import org.testng.internal.InvokedMethod;
import org.testng.internal.Invoker;
import org.testng.internal.MethodGroupsHelper;
import org.testng.internal.MethodHelper;
import org.testng.internal.MethodInstance;
import org.testng.internal.ResultMap;
import org.testng.internal.RunInfo;
import org.testng.internal.TestMethodWorker;
import org.testng.internal.TestNGClassFinder;
import org.testng.internal.TestNGMethodFinder;
import org.testng.internal.Utils;
import org.testng.internal.XmlMethodSelector;
import org.testng.internal.annotations.AnnotationHelper;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.internal.annotations.IListeners;
import org.testng.internal.thread.ThreadUtil;
import org.testng.internal.thread.graph.GraphThreadPoolExecutor;
import org.testng.internal.thread.graph.IThreadWorkerFactory;
import org.testng.internal.thread.graph.IWorker;
import org.testng.junit.IJUnitTestRunner;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlPackage;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class TestRunner implements ITestContext, ITestResultNotifier, IThreadWorkerFactory<ITestNGMethod> {
   private static final long serialVersionUID = 4247820024988306670L;
   private ISuite m_suite;
   private XmlTest m_xmlTest;
   private String m_testName;
   private transient List<XmlClass> m_testClassesFromXml = null;
   private transient List<XmlPackage> m_packageNamesFromXml = null;
   private transient IInvoker m_invoker = null;
   private transient IAnnotationFinder m_annotationFinder = null;
   private transient List<ITestListener> m_testListeners = Lists.newArrayList();
   private transient Set<IConfigurationListener> m_configurationListeners = Sets.newHashSet();
   private transient IConfigurationListener m_confListener = new TestRunner.ConfigurationListener();
   private transient boolean m_skipFailedInvocationCounts;
   private transient List<IInvokedMethodListener> m_invokedMethodListeners = Lists.newArrayList();
   private ITestNGMethod[] m_allTestMethods = new ITestNGMethod[0];
   private Date m_startDate = null;
   private Date m_endDate = null;
   private transient Map<Class<?>, ITestClass> m_classMap = Maps.newLinkedHashMap();
   private String m_outputDirectory = Constants.getDefaultValueFor("testng.outputDir");
   private XmlMethodSelector m_xmlMethodSelector = new XmlMethodSelector();
   private static int m_verbose = 1;
   private ITestNGMethod[] m_beforeSuiteMethods = new ITestNGMethod[0];
   private ITestNGMethod[] m_afterSuiteMethods = new ITestNGMethod[0];
   private ITestNGMethod[] m_beforeXmlTestMethods = new ITestNGMethod[0];
   private ITestNGMethod[] m_afterXmlTestMethods = new ITestNGMethod[0];
   private List<ITestNGMethod> m_excludedMethods = Lists.newArrayList();
   private ConfigurationGroupMethods m_groupMethods = null;
   private Map<String, List<String>> m_metaGroups = Maps.newHashMap();
   private IResultMap m_passedTests = new ResultMap();
   private IResultMap m_failedTests = new ResultMap();
   private IResultMap m_failedButWithinSuccessPercentageTests = new ResultMap();
   private IResultMap m_skippedTests = new ResultMap();
   private RunInfo m_runInfo = new RunInfo();
   private String m_host;
   private transient List<IMethodInterceptor> m_methodInterceptors;
   private transient ClassMethodMap m_classMethodMap;
   private transient TestNGClassFinder m_testClassFinder;
   private transient IConfiguration m_configuration;
   private IMethodInterceptor builtinInterceptor;
   private final List<InvokedMethod> m_invokedMethods = Lists.newArrayList();
   private IResultMap m_passedConfigurations = new ResultMap();
   private IResultMap m_skippedConfigurations = new ResultMap();
   private IResultMap m_failedConfigurations = new ResultMap();
   private IAttributes m_attributes = new Attributes();
   private ListMultiMap<Class<? extends Module>, Module> m_guiceModules = Maps.newListMultiMap();
   private Map<List<Module>, Injector> m_injectors = Maps.newHashMap();

   protected TestRunner(IConfiguration configuration, ISuite suite, XmlTest test, String outputDirectory, IAnnotationFinder finder, boolean skipFailedInvocationCounts, List<IInvokedMethodListener> invokedMethodListeners) {
      this.init(configuration, suite, test, outputDirectory, finder, skipFailedInvocationCounts, invokedMethodListeners);
   }

   public TestRunner(IConfiguration configuration, ISuite suite, XmlTest test, boolean skipFailedInvocationCounts, List<IInvokedMethodListener> listeners) {
      this.init(configuration, suite, test, suite.getOutputDirectory(), suite.getAnnotationFinder(), skipFailedInvocationCounts, listeners);
   }

   private void init(IConfiguration configuration, ISuite suite, XmlTest test, String outputDirectory, IAnnotationFinder annotationFinder, boolean skipFailedInvocationCounts, List<IInvokedMethodListener> invokedMethodListeners) {
      this.m_configuration = configuration;
      this.m_xmlTest = test;
      this.m_suite = suite;
      this.m_testName = test.getName();
      this.m_host = suite.getHost();
      this.m_testClassesFromXml = test.getXmlClasses();
      this.m_skipFailedInvocationCounts = skipFailedInvocationCounts;
      this.setVerbose(test.getVerbose());
      boolean preserveOrder = "true".equalsIgnoreCase(test.getPreserveOrder());
      this.m_methodInterceptors = new ArrayList();
      this.builtinInterceptor = (IMethodInterceptor)(preserveOrder ? new PreserveOrderMethodInterceptor() : new InstanceOrderingMethodInterceptor());
      this.m_packageNamesFromXml = test.getXmlPackages();
      if (null != this.m_packageNamesFromXml) {
         Iterator i$ = this.m_packageNamesFromXml.iterator();

         while(i$.hasNext()) {
            XmlPackage xp = (XmlPackage)i$.next();
            this.m_testClassesFromXml.addAll(xp.getXmlClasses());
         }
      }

      this.m_annotationFinder = annotationFinder;
      this.m_invokedMethodListeners = invokedMethodListeners;
      this.m_invoker = new Invoker(this.m_configuration, this, this, this.m_suite.getSuiteState(), this.m_skipFailedInvocationCounts, invokedMethodListeners);
      if (suite.getParallel() != null) {
         this.log(3, "Running the tests in '" + test.getName() + "' with parallel mode:" + suite.getParallel());
      }

      this.setOutputDirectory(outputDirectory);
      this.init();
   }

   public IInvoker getInvoker() {
      return this.m_invoker;
   }

   public ITestNGMethod[] getBeforeSuiteMethods() {
      return this.m_beforeSuiteMethods;
   }

   public ITestNGMethod[] getAfterSuiteMethods() {
      return this.m_afterSuiteMethods;
   }

   public ITestNGMethod[] getBeforeTestConfigurationMethods() {
      return this.m_beforeXmlTestMethods;
   }

   public ITestNGMethod[] getAfterTestConfigurationMethods() {
      return this.m_afterXmlTestMethods;
   }

   private void init() {
      this.initMetaGroups(this.m_xmlTest);
      this.initRunInfo(this.m_xmlTest);
      if (!this.m_xmlTest.isJUnit()) {
         this.initMethods();
      }

      this.initListeners();
      this.addConfigurationListener(this.m_confListener);
   }

   private TestRunner.ListenerHolder findAllListeners(Class<?> cls) {
      TestRunner.ListenerHolder result = new TestRunner.ListenerHolder();
      result.listenerClasses = Lists.newArrayList();

      do {
         IListeners l = (IListeners)this.m_annotationFinder.findAnnotation(cls, IListeners.class);
         if (l != null) {
            Class<? extends ITestNGListener>[] classes = l.getValue();
            Class[] arr$ = classes;
            int len$ = classes.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               Class<? extends ITestNGListener> c = arr$[i$];
               result.listenerClasses.add(c);
               if (ITestNGListenerFactory.class.isAssignableFrom(c)) {
                  if (result.listenerFactoryClass != null) {
                     throw new TestNGException("Found more than one class implementingITestNGListenerFactory:" + c + " and " + result.listenerFactoryClass);
                  }

                  result.listenerFactoryClass = c;
               }
            }
         }

         cls = cls.getSuperclass();
      } while(cls != Object.class);

      return result;
   }

   private void initListeners() {
      Set<Class<? extends ITestNGListener>> listenerClasses = Sets.newHashSet();
      Class<? extends ITestNGListenerFactory> listenerFactoryClass = null;

      IClass ic;
      Class c;
      TestRunner.ListenerHolder listenerHolder;
      for(Iterator i$ = this.getTestClasses().iterator(); i$.hasNext(); listenerClasses.addAll(listenerHolder.listenerClasses)) {
         ic = (IClass)i$.next();
         c = ic.getRealClass();
         listenerHolder = this.findAllListeners(c);
         if (listenerFactoryClass == null) {
            listenerFactoryClass = listenerHolder.listenerFactoryClass;
         }
      }

      ITestNGListenerFactory listenerFactory = null;

      try {
         if (this.m_testClassFinder != null) {
            ic = this.m_testClassFinder.getIClass(listenerFactoryClass);
            if (ic != null) {
               listenerFactory = (ITestNGListenerFactory)ic.getInstances(false)[0];
            }
         }

         if (listenerFactory == null) {
            listenerFactory = listenerFactoryClass != null ? (ITestNGListenerFactory)listenerFactoryClass.newInstance() : null;
         }
      } catch (Exception var8) {
         throw new TestNGException("Couldn't instantiate the ITestNGListenerFactory: " + var8);
      }

      Iterator i$ = listenerClasses.iterator();

      while(i$.hasNext()) {
         c = (Class)i$.next();
         Object listener = listenerFactory != null ? listenerFactory.createListener(c) : null;
         if (listener == null) {
            listener = ClassHelper.newInstance(c);
         }

         if (listener instanceof IMethodInterceptor) {
            this.m_methodInterceptors.add((IMethodInterceptor)listener);
         }

         if (listener instanceof ISuiteListener) {
            this.m_suite.addListener((ISuiteListener)listener);
         }

         if (listener instanceof IInvokedMethodListener) {
            this.m_suite.addListener((ITestNGListener)listener);
         }

         if (listener instanceof ITestListener) {
            this.addTestListener((ITestListener)listener);
         }

         if (listener instanceof IConfigurationListener) {
            this.addConfigurationListener((IConfigurationListener)listener);
         }

         if (listener instanceof IReporter) {
            this.m_suite.addListener((ITestNGListener)listener);
         }

         if (listener instanceof IConfigurable) {
            this.m_configuration.setConfigurable((IConfigurable)listener);
         }

         if (listener instanceof IHookable) {
            this.m_configuration.setHookable((IHookable)listener);
         }

         if (listener instanceof IExecutionListener) {
            IExecutionListener iel = (IExecutionListener)listener;
            iel.onExecutionStart();
            this.m_configuration.addExecutionListener(iel);
         }
      }

   }

   private void initMetaGroups(XmlTest xmlTest) {
      Map<String, List<String>> metaGroups = xmlTest.getMetaGroups();
      Iterator i$ = metaGroups.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, List<String>> entry = (Entry)i$.next();
         this.addMetaGroup((String)entry.getKey(), (List)entry.getValue());
      }

   }

   private void initRunInfo(XmlTest xmlTest) {
      this.m_xmlMethodSelector.setIncludedGroups(this.createGroups(this.m_xmlTest.getIncludedGroups()));
      this.m_xmlMethodSelector.setExcludedGroups(this.createGroups(this.m_xmlTest.getExcludedGroups()));
      this.m_xmlMethodSelector.setExpression(this.m_xmlTest.getExpression());
      this.m_xmlMethodSelector.setXmlClasses(this.m_xmlTest.getXmlClasses());
      this.m_runInfo.addMethodSelector(this.m_xmlMethodSelector, 10);
      if (null != xmlTest.getMethodSelectors()) {
         Iterator i$ = xmlTest.getMethodSelectors().iterator();

         while(i$.hasNext()) {
            org.testng.xml.XmlMethodSelector selector = (org.testng.xml.XmlMethodSelector)i$.next();
            if (selector.getClassName() != null) {
               IMethodSelector s = ClassHelper.createSelector(selector);
               this.m_runInfo.addMethodSelector(s, selector.getPriority());
            }
         }
      }

   }

   private void initMethods() {
      List<ITestNGMethod> beforeClassMethods = Lists.newArrayList();
      List<ITestNGMethod> testMethods = Lists.newArrayList();
      List<ITestNGMethod> afterClassMethods = Lists.newArrayList();
      List<ITestNGMethod> beforeSuiteMethods = Lists.newArrayList();
      List<ITestNGMethod> afterSuiteMethods = Lists.newArrayList();
      List<ITestNGMethod> beforeXmlTestMethods = Lists.newArrayList();
      List<ITestNGMethod> afterXmlTestMethods = Lists.newArrayList();
      ClassInfoMap classMap = new ClassInfoMap(this.m_testClassesFromXml);
      this.m_testClassFinder = new TestNGClassFinder(classMap, (Map)null, this.m_xmlTest, this.m_configuration, this);
      ITestMethodFinder testMethodFinder = new TestNGMethodFinder(this.m_runInfo, this.m_annotationFinder);
      this.m_runInfo.setTestMethods(testMethods);
      IClass[] classes = this.m_testClassFinder.findTestClasses();
      IClass[] arr$ = classes;
      int len$ = classes.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         IClass ic = arr$[i$];
         ITestClass tc = new TestClass(ic, testMethodFinder, this.m_annotationFinder, this.m_runInfo, this.m_xmlTest, classMap.getXmlClass(ic.getRealClass()));
         this.m_classMap.put(ic.getRealClass(), tc);
      }

      Map<String, List<ITestNGMethod>> beforeGroupMethods = MethodGroupsHelper.findGroupsMethods(this.m_classMap.values(), true);
      Map<String, List<ITestNGMethod>> afterGroupMethods = MethodGroupsHelper.findGroupsMethods(this.m_classMap.values(), false);
      Iterator i$ = this.m_classMap.values().iterator();

      while(i$.hasNext()) {
         ITestClass tc = (ITestClass)i$.next();
         this.fixMethodsWithClass(tc.getTestMethods(), tc, testMethods);
         this.fixMethodsWithClass(tc.getBeforeClassMethods(), tc, beforeClassMethods);
         this.fixMethodsWithClass(tc.getBeforeTestMethods(), tc, (List)null);
         this.fixMethodsWithClass(tc.getAfterTestMethods(), tc, (List)null);
         this.fixMethodsWithClass(tc.getAfterClassMethods(), tc, afterClassMethods);
         this.fixMethodsWithClass(tc.getBeforeSuiteMethods(), tc, beforeSuiteMethods);
         this.fixMethodsWithClass(tc.getAfterSuiteMethods(), tc, afterSuiteMethods);
         this.fixMethodsWithClass(tc.getBeforeTestConfigurationMethods(), tc, beforeXmlTestMethods);
         this.fixMethodsWithClass(tc.getAfterTestConfigurationMethods(), tc, afterXmlTestMethods);
         this.fixMethodsWithClass(tc.getBeforeGroupsMethods(), tc, MethodHelper.uniqueMethodList(beforeGroupMethods.values()));
         this.fixMethodsWithClass(tc.getAfterGroupsMethods(), tc, MethodHelper.uniqueMethodList(afterGroupMethods.values()));
      }

      this.m_beforeSuiteMethods = MethodHelper.collectAndOrderMethods(beforeSuiteMethods, false, this.m_runInfo, this.m_annotationFinder, true, this.m_excludedMethods);
      this.m_beforeXmlTestMethods = MethodHelper.collectAndOrderMethods(beforeXmlTestMethods, false, this.m_runInfo, this.m_annotationFinder, true, this.m_excludedMethods);
      this.m_allTestMethods = MethodHelper.collectAndOrderMethods(testMethods, true, this.m_runInfo, this.m_annotationFinder, false, this.m_excludedMethods);
      this.m_classMethodMap = new ClassMethodMap(testMethods, this.m_xmlMethodSelector);
      this.m_afterXmlTestMethods = MethodHelper.collectAndOrderMethods(afterXmlTestMethods, false, this.m_runInfo, this.m_annotationFinder, true, this.m_excludedMethods);
      this.m_afterSuiteMethods = MethodHelper.collectAndOrderMethods(afterSuiteMethods, false, this.m_runInfo, this.m_annotationFinder, true, this.m_excludedMethods);
      this.m_groupMethods = new ConfigurationGroupMethods(this.m_allTestMethods, beforeGroupMethods, afterGroupMethods);
   }

   private void fixMethodsWithClass(ITestNGMethod[] methods, ITestClass testCls, List<ITestNGMethod> methodList) {
      ITestNGMethod[] arr$ = methods;
      int len$ = methods.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ITestNGMethod itm = arr$[i$];
         itm.setTestClass(testCls);
         if (methodList != null) {
            methodList.add(itm);
         }
      }

   }

   public Collection<ITestClass> getTestClasses() {
      return this.m_classMap.values();
   }

   public void setTestName(String name) {
      this.m_testName = name;
   }

   public void setOutputDirectory(String od) {
      this.m_outputDirectory = od;
   }

   private void addMetaGroup(String name, List<String> groupNames) {
      this.m_metaGroups.put(name, groupNames);
   }

   private void collectGroups(String[] groups, List<String> unfinishedGroups, Map<String, String> result) {
      String[] arr$ = groups;
      int len$ = groups.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String gn = arr$[i$];
         List<String> subGroups = (List)this.m_metaGroups.get(gn);
         if (null != subGroups) {
            Iterator i$ = subGroups.iterator();

            while(i$.hasNext()) {
               String sg = (String)i$.next();
               if (null == result.get(sg)) {
                  result.put(sg, sg);
                  unfinishedGroups.add(sg);
               }
            }
         }
      }

   }

   private Map<String, String> createGroups(List<String> groups) {
      return this.createGroups((String[])groups.toArray(new String[groups.size()]));
   }

   private Map<String, String> createGroups(String[] groups) {
      Map<String, String> result = Maps.newHashMap();
      String[] arr$ = groups;
      int len$ = groups.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String group = arr$[i$];
         result.put(group, group);
      }

      List<String> unfinishedGroups = Lists.newArrayList();
      if (this.m_metaGroups.size() > 0) {
         this.collectGroups(groups, unfinishedGroups, result);

         while(unfinishedGroups.size() > 0) {
            String[] uGroups = (String[])unfinishedGroups.toArray(new String[unfinishedGroups.size()]);
            unfinishedGroups = Lists.newArrayList();
            this.collectGroups(uGroups, unfinishedGroups, result);
         }
      }

      return result;
   }

   public void run() {
      this.beforeRun();

      try {
         XmlTest test = this.getTest();
         if (test.isJUnit()) {
            this.privateRunJUnit(test);
         } else {
            this.privateRun(test);
         }
      } finally {
         this.afterRun();
      }

   }

   private void beforeRun() {
      this.m_startDate = new Date(System.currentTimeMillis());
      this.logStart();
      this.fireEvent(true);
      ITestNGMethod[] testConfigurationMethods = this.getBeforeTestConfigurationMethods();
      if (null != testConfigurationMethods && testConfigurationMethods.length > 0) {
         this.m_invoker.invokeConfigurations((IClass)null, testConfigurationMethods, this.m_xmlTest.getSuite(), this.m_xmlTest.getAllParameters(), (Object[])null, (Object)null);
      }

   }

   private void privateRunJUnit(XmlTest xmlTest) {
      final ClassInfoMap cim = new ClassInfoMap(this.m_testClassesFromXml, false);
      final Set<Class<?>> classes = cim.getClasses();
      final List<ITestNGMethod> runMethods = Lists.newArrayList();
      List<IWorker<ITestNGMethod>> workers = Lists.newArrayList();
      workers.add(new IWorker<ITestNGMethod>() {
         public long getTimeOut() {
            return 0L;
         }

         public void run() {
            Iterator i$ = classes.iterator();

            while(i$.hasNext()) {
               Class<?> tc = (Class)i$.next();
               List<XmlInclude> includedMethods = cim.getXmlClass(tc).getIncludedMethods();
               List<String> methods = Lists.newArrayList();
               Iterator i$x = includedMethods.iterator();

               while(i$x.hasNext()) {
                  XmlInclude inc = (XmlInclude)i$x.next();
                  methods.add(inc.getName());
               }

               IJUnitTestRunner tr = ClassHelper.createTestRunner(TestRunner.this);
               tr.setInvokedMethodListeners(TestRunner.this.m_invokedMethodListeners);

               try {
                  tr.run(tc, (String[])methods.toArray(new String[methods.size()]));
               } catch (Exception var10) {
                  var10.printStackTrace();
               } finally {
                  runMethods.addAll(tr.getTestMethods());
               }
            }

         }

         public List<ITestNGMethod> getTasks() {
            throw new TestNGException("JUnit not supported");
         }

         public int getPriority() {
            return TestRunner.this.m_allTestMethods.length == 1 ? TestRunner.this.m_allTestMethods[0].getPriority() : 0;
         }

         public int compareTo(IWorker<ITestNGMethod> other) {
            return this.getPriority() - other.getPriority();
         }
      });
      this.runWorkers(workers, "", (ListMultiMap)null);
      this.m_allTestMethods = (ITestNGMethod[])runMethods.toArray(new ITestNGMethod[runMethods.size()]);
   }

   private void privateRun(XmlTest xmlTest) {
      String parallelMode = xmlTest.getParallel();
      boolean parallel = "methods".equals(parallelMode) || "true".equalsIgnoreCase(parallelMode) || "classes".equals(parallelMode) || "instances".equals(parallelMode);
      int threadCount = parallel ? xmlTest.getThreadCount() : 1;
      DynamicGraph<ITestNGMethod> graph = this.createDynamicGraph(this.intercept(this.m_allTestMethods));
      if (parallel) {
         if (graph.getNodeCount() > 0) {
            GraphThreadPoolExecutor<ITestNGMethod> executor = new GraphThreadPoolExecutor(graph, this, threadCount, threadCount, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
            executor.run();

            try {
               long timeOut = this.m_xmlTest.getTimeOut((long)XmlTest.DEFAULT_TIMEOUT_MS);
               Utils.log("TestRunner", 2, "Starting executor for test " + this.m_xmlTest.getName() + " with time out:" + timeOut + " milliseconds.");
               executor.awaitTermination(timeOut, TimeUnit.MILLISECONDS);
               executor.shutdownNow();
            } catch (InterruptedException var13) {
               var13.printStackTrace();
               Thread.currentThread().interrupt();
            }
         }
      } else {
         boolean debug = false;
         List<ITestNGMethod> freeNodes = graph.getFreeNodes();
         if (debug) {
            System.out.println("Free nodes:" + freeNodes);
         }

         if (graph.getNodeCount() > 0 && freeNodes.isEmpty()) {
            throw new TestNGException("No free nodes found in:" + graph);
         }

         while(!freeNodes.isEmpty()) {
            List<IWorker<ITestNGMethod>> runnables = this.createWorkers(freeNodes);
            Iterator i$ = runnables.iterator();

            while(i$.hasNext()) {
               IWorker<ITestNGMethod> r = (IWorker)i$.next();
               r.run();
            }

            graph.setStatus((Collection)freeNodes, DynamicGraph.Status.FINISHED);
            freeNodes = graph.getFreeNodes();
            if (debug) {
               System.out.println("Free nodes:" + freeNodes);
            }
         }
      }

   }

   private ITestNGMethod[] intercept(ITestNGMethod[] methods) {
      List<IMethodInstance> methodInstances = this.methodsToMethodInstances(Arrays.asList(methods));
      this.m_methodInterceptors.add(this.builtinInterceptor);

      IMethodInterceptor m_methodInterceptor;
      for(Iterator i$ = this.m_methodInterceptors.iterator(); i$.hasNext(); methodInstances = m_methodInterceptor.intercept(methodInstances, this)) {
         m_methodInterceptor = (IMethodInterceptor)i$.next();
      }

      List<ITestNGMethod> result = Lists.newArrayList();
      Iterator i$ = methodInstances.iterator();

      while(i$.hasNext()) {
         IMethodInstance imi = (IMethodInstance)i$.next();
         result.add(imi.getMethod());
      }

      this.m_classMethodMap = new ClassMethodMap(result, (XmlMethodSelector)null);
      return (ITestNGMethod[])result.toArray(new ITestNGMethod[result.size()]);
   }

   public List<IWorker<ITestNGMethod>> createWorkers(List<ITestNGMethod> methods) {
      List result;
      if ("instances".equals(this.m_xmlTest.getParallel())) {
         result = this.createInstanceBasedParallelWorkers(methods);
      } else {
         result = this.createClassBasedParallelWorkers(methods);
      }

      return result;
   }

   private List<IWorker<ITestNGMethod>> createClassBasedParallelWorkers(List<ITestNGMethod> methods) {
      List<IWorker<ITestNGMethod>> result = Lists.newArrayList();
      Set<Class> sequentialClasses = Sets.newHashSet();
      Iterator i$ = methods.iterator();

      while(true) {
         Class cls;
         ITestAnnotation test;
         do {
            if (!i$.hasNext()) {
               List<IMethodInstance> methodInstances = Lists.newArrayList();
               Iterator i$ = methods.iterator();

               while(i$.hasNext()) {
                  ITestNGMethod tm = (ITestNGMethod)i$.next();
                  methodInstances.addAll(this.methodsToMultipleMethodInstances(tm));
               }

               IMethodInterceptor m_methodInterceptor;
               for(i$ = this.m_methodInterceptors.iterator(); i$.hasNext(); methodInstances = m_methodInterceptor.intercept(methodInstances, this)) {
                  m_methodInterceptor = (IMethodInterceptor)i$.next();
               }

               Map<String, String> params = this.m_xmlTest.getAllParameters();
               Set<Class<?>> processedClasses = Sets.newHashSet();
               Iterator i$ = methodInstances.iterator();

               while(true) {
                  while(true) {
                     Class c;
                     TestMethodWorker worker;
                     label40:
                     do {
                        while(i$.hasNext()) {
                           IMethodInstance im = (IMethodInstance)i$.next();
                           c = im.getMethod().getTestClass().getRealClass();
                           if (sequentialClasses.contains(c)) {
                              continue label40;
                           }

                           worker = this.createTestMethodWorker(Arrays.asList(im), params, c);
                           result.add(worker);
                        }

                        Collections.sort(result);
                        return result;
                     } while(processedClasses.contains(c));

                     processedClasses.add(c);
                     if (System.getProperty("experimental") != null) {
                        List<List<IMethodInstance>> instances = this.createInstances(methodInstances);
                        Iterator i$ = instances.iterator();

                        while(i$.hasNext()) {
                           List<IMethodInstance> inst = (List)i$.next();
                           TestMethodWorker worker = this.createTestMethodWorker(inst, params, c);
                           result.add(worker);
                        }
                     } else {
                        worker = this.createTestMethodWorker(methodInstances, params, c);
                        result.add(worker);
                     }
                  }
               }
            }

            ITestNGMethod m = (ITestNGMethod)i$.next();
            cls = m.getRealClass();
            test = (ITestAnnotation)this.m_annotationFinder.findAnnotation(cls, ITestAnnotation.class);
         } while((test == null || !test.getSequential() && !test.getSingleThreaded()) && !"classes".equals(this.m_xmlTest.getParallel()));

         sequentialClasses.add(cls);
      }
   }

   private List<IWorker<ITestNGMethod>> createInstanceBasedParallelWorkers(List<ITestNGMethod> methods) {
      List<IWorker<ITestNGMethod>> result = Lists.newArrayList();
      ListMultiMap<Object, ITestNGMethod> lmm = Maps.newListMultiMap();
      Iterator i$ = methods.iterator();

      while(i$.hasNext()) {
         ITestNGMethod m = (ITestNGMethod)i$.next();
         lmm.put(m.getInstance(), m);
      }

      i$ = lmm.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<Object, List<ITestNGMethod>> es = (Entry)i$.next();
         List<IMethodInstance> methodInstances = Lists.newArrayList();
         Iterator i$ = ((List)es.getValue()).iterator();

         while(i$.hasNext()) {
            ITestNGMethod m = (ITestNGMethod)i$.next();
            methodInstances.add(new MethodInstance(m));
         }

         TestMethodWorker tmw = new TestMethodWorker(this.m_invoker, (IMethodInstance[])methodInstances.toArray(new IMethodInstance[methodInstances.size()]), this.m_xmlTest.getSuite(), this.m_xmlTest.getAllParameters(), this.m_groupMethods, this.m_classMethodMap, this);
         result.add(tmw);
      }

      return result;
   }

   private List<List<IMethodInstance>> createInstances(List<IMethodInstance> methodInstances) {
      Map<Object, List<IMethodInstance>> map = Maps.newHashMap();
      Iterator i$ = methodInstances.iterator();

      while(i$.hasNext()) {
         IMethodInstance imi = (IMethodInstance)i$.next();
         Object[] arr$ = imi.getInstances();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Object o = arr$[i$];
            System.out.println(o);
            List<IMethodInstance> l = (List)map.get(o);
            if (l == null) {
               l = Lists.newArrayList();
               map.put(o, l);
            }

            l.add(imi);
         }
      }

      return new ArrayList(map.values());
   }

   private TestMethodWorker createTestMethodWorker(List<IMethodInstance> methodInstances, Map<String, String> params, Class<?> c) {
      return new TestMethodWorker(this.m_invoker, this.findClasses(methodInstances, c), this.m_xmlTest.getSuite(), params, this.m_groupMethods, this.m_classMethodMap, this);
   }

   private IMethodInstance[] findClasses(List<IMethodInstance> methodInstances, Class<?> c) {
      List<IMethodInstance> result = Lists.newArrayList();
      Iterator i$ = methodInstances.iterator();

      while(i$.hasNext()) {
         IMethodInstance mi = (IMethodInstance)i$.next();
         if (mi.getMethod().getTestClass().getRealClass() == c) {
            result.add(mi);
         }
      }

      return (IMethodInstance[])result.toArray(new IMethodInstance[result.size()]);
   }

   private List<MethodInstance> methodsToMultipleMethodInstances(ITestNGMethod... sl) {
      List<MethodInstance> vResult = Lists.newArrayList();
      ITestNGMethod[] arr$ = sl;
      int len$ = sl.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ITestNGMethod m = arr$[i$];
         vResult.add(new MethodInstance(m));
      }

      return vResult;
   }

   private List<IMethodInstance> methodsToMethodInstances(List<ITestNGMethod> sl) {
      List<IMethodInstance> result = new ArrayList();
      Iterator i$ = sl.iterator();

      while(i$.hasNext()) {
         ITestNGMethod iTestNGMethod = (ITestNGMethod)i$.next();
         result.add(new MethodInstance(iTestNGMethod));
      }

      return result;
   }

   private void runWorkers(List<? extends IWorker<ITestNGMethod>> workers, String parallelMode, ListMultiMap<Integer, TestMethodWorker> sequentialWorkers) {
      if (!"methods".equals(parallelMode) && !"true".equalsIgnoreCase(parallelMode) && !"classes".equals(parallelMode)) {
         Iterator i$ = workers.iterator();

         while(i$.hasNext()) {
            IWorker<ITestNGMethod> tmw = (IWorker)i$.next();
            tmw.run();
         }
      } else {
         long maxTimeOut = this.m_xmlTest.getTimeOut((long)XmlTest.DEFAULT_TIMEOUT_MS);
         Iterator i$ = workers.iterator();

         while(i$.hasNext()) {
            IWorker<ITestNGMethod> tmw = (IWorker)i$.next();
            long mt = tmw.getTimeOut();
            if (mt > maxTimeOut) {
               maxTimeOut = mt;
            }
         }

         ThreadUtil.execute(workers, this.m_xmlTest.getThreadCount(), maxTimeOut, false);
      }

   }

   private void afterRun() {
      ITestNGMethod[] testConfigurationMethods = this.getAfterTestConfigurationMethods();
      if (null != testConfigurationMethods && testConfigurationMethods.length > 0) {
         this.m_invoker.invokeConfigurations((IClass)null, testConfigurationMethods, this.m_xmlTest.getSuite(), this.m_xmlTest.getAllParameters(), (Object[])null, (Object)null);
      }

      this.m_endDate = new Date(System.currentTimeMillis());
      if (getVerbose() >= 3) {
         this.dumpInvokedMethods();
      }

      this.fireEvent(false);
   }

   private DynamicGraph<ITestNGMethod> createDynamicGraph(ITestNGMethod[] methods) {
      DynamicGraph<ITestNGMethod> result = new DynamicGraph();
      result.setComparator(new Comparator<ITestNGMethod>() {
         public int compare(ITestNGMethod o1, ITestNGMethod o2) {
            return o1.getPriority() - o2.getPriority();
         }
      });
      DependencyMap dependencyMap = new DependencyMap(methods);
      boolean hasDependencies = false;
      ITestNGMethod[] arr$ = methods;
      int len$ = methods.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ITestNGMethod m = arr$[i$];
         result.addNode(m);
         String[] dependentGroups = m.getMethodsDependedUpon();
         String[] arr$;
         int len$;
         int i$;
         String d;
         if (dependentGroups != null) {
            arr$ = dependentGroups;
            len$ = dependentGroups.length;

            for(i$ = 0; i$ < len$; ++i$) {
               d = arr$[i$];
               ITestNGMethod dm = dependencyMap.getMethodDependingOn(d, m);
               if (m != dm) {
                  result.addEdge(m, dm);
               }
            }
         }

         dependentGroups = m.getGroupsDependedUpon();
         arr$ = dependentGroups;
         len$ = dependentGroups.length;

         for(i$ = 0; i$ < len$; ++i$) {
            d = arr$[i$];
            hasDependencies = true;
            List<ITestNGMethod> dg = dependencyMap.getMethodsThatBelongTo(d, m);
            if (dg == null) {
               throw new TestNGException("Method \"" + m + "\" depends on nonexistent group \"" + d + "\"");
            }

            Iterator i$ = dg.iterator();

            while(i$.hasNext()) {
               ITestNGMethod ddm = (ITestNGMethod)i$.next();
               result.addEdge(m, ddm);
            }
         }
      }

      ListMultiMap instanceDependencies;
      Iterator i$;
      Entry es;
      Iterator i$;
      ITestNGMethod dm;
      if (!hasDependencies && !XmlSuite.isParallel(this.getCurrentXmlTest().getParallel()) && "true".equalsIgnoreCase(this.getCurrentXmlTest().getPreserveOrder())) {
         instanceDependencies = this.createClassDependencies(methods, this.getCurrentXmlTest());
         i$ = instanceDependencies.entrySet().iterator();

         while(i$.hasNext()) {
            es = (Entry)i$.next();
            i$ = ((List)es.getValue()).iterator();

            while(i$.hasNext()) {
               dm = (ITestNGMethod)i$.next();
               result.addEdge(dm, es.getKey());
            }
         }
      }

      if (this.getCurrentXmlTest().getGroupByInstances()) {
         instanceDependencies = this.createInstanceDependencies(methods, this.getCurrentXmlTest());
         i$ = instanceDependencies.entrySet().iterator();

         while(i$.hasNext()) {
            es = (Entry)i$.next();
            i$ = ((List)es.getValue()).iterator();

            while(i$.hasNext()) {
               dm = (ITestNGMethod)i$.next();
               result.addEdge(dm, es.getKey());
            }
         }
      }

      return result;
   }

   private ListMultiMap<ITestNGMethod, ITestNGMethod> createInstanceDependencies(ITestNGMethod[] methods, XmlTest currentXmlTest) {
      ListMultiMap<Object, ITestNGMethod> instanceMap = Maps.newListMultiMap();
      ITestNGMethod[] arr$ = methods;
      int len$ = methods.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ITestNGMethod m = arr$[i$];
         instanceMap.put(m.getInstance(), m);
      }

      ListMultiMap<ITestNGMethod, ITestNGMethod> result = Maps.newListMultiMap();
      Object previousInstance = null;
      Iterator i$ = instanceMap.entrySet().iterator();

      while(true) {
         while(i$.hasNext()) {
            Entry<Object, List<ITestNGMethod>> es = (Entry)i$.next();
            if (previousInstance == null) {
               previousInstance = es.getKey();
            } else {
               List<ITestNGMethod> previousMethods = (List)instanceMap.get(previousInstance);
               Object currentInstance = es.getKey();
               List<ITestNGMethod> currentMethods = (List)instanceMap.get(currentInstance);
               Iterator i$ = currentMethods.iterator();

               while(i$.hasNext()) {
                  ITestNGMethod cm = (ITestNGMethod)i$.next();
                  Iterator i$ = previousMethods.iterator();

                  while(i$.hasNext()) {
                     ITestNGMethod pm = (ITestNGMethod)i$.next();
                     result.put(cm, pm);
                  }
               }

               previousInstance = currentInstance;
            }
         }

         return result;
      }
   }

   private ListMultiMap<ITestNGMethod, ITestNGMethod> createClassDependencies(ITestNGMethod[] methods, XmlTest test) {
      Map<String, List<ITestNGMethod>> classes = Maps.newHashMap();
      List<XmlClass> sortedClasses = Lists.newArrayList();
      Iterator i$ = test.getXmlClasses().iterator();

      while(i$.hasNext()) {
         XmlClass c = (XmlClass)i$.next();
         classes.put(c.getName(), new ArrayList());
         if (!sortedClasses.contains(c)) {
            sortedClasses.add(c);
         }
      }

      Collections.sort(sortedClasses, new Comparator<XmlClass>() {
         public int compare(XmlClass arg0, XmlClass arg1) {
            return arg0.getIndex() - arg1.getIndex();
         }
      });
      Map<String, Integer> indexedClasses1 = Maps.newHashMap();
      Map<Integer, String> indexedClasses2 = Maps.newHashMap();
      int i = 0;

      for(Iterator i$ = sortedClasses.iterator(); i$.hasNext(); ++i) {
         XmlClass c = (XmlClass)i$.next();
         indexedClasses1.put(c.getName(), i);
         indexedClasses2.put(i, c.getName());
      }

      ListMultiMap<String, ITestNGMethod> methodsFromClass = Maps.newListMultiMap();
      ITestNGMethod[] arr$ = methods;
      int len$ = methods.length;

      int len$;
      for(len$ = 0; len$ < len$; ++len$) {
         ITestNGMethod m = arr$[len$];
         methodsFromClass.put(m.getTestClass().getName(), m);
      }

      ListMultiMap<ITestNGMethod, ITestNGMethod> result = Maps.newListMultiMap();
      ITestNGMethod[] arr$ = methods;
      len$ = methods.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ITestNGMethod m = arr$[i$];
         String name = m.getTestClass().getName();
         Integer index = (Integer)indexedClasses1.get(name);
         if (index != null && index > 0) {
            String classDependedUpon = (String)indexedClasses2.get(index - 1);
            List<ITestNGMethod> methodsDependedUpon = (List)methodsFromClass.get(classDependedUpon);
            if (methodsDependedUpon != null) {
               Iterator i$ = methodsDependedUpon.iterator();

               while(i$.hasNext()) {
                  ITestNGMethod mdu = (ITestNGMethod)i$.next();
                  result.put(mdu, m);
               }
            }
         }
      }

      return result;
   }

   private void logStart() {
      this.log(3, "Running test " + this.m_testName + " on " + this.m_classMap.size() + " " + " classes, " + " included groups:[" + this.mapToString(this.m_xmlMethodSelector.getIncludedGroups()) + "] excluded groups:[" + this.mapToString(this.m_xmlMethodSelector.getExcludedGroups()) + "]");
      if (getVerbose() >= 3) {
         Iterator i$ = this.m_classMap.values().iterator();

         while(i$.hasNext()) {
            ITestClass tc = (ITestClass)i$.next();
            ((TestClass)tc).dump();
         }
      }

   }

   private void fireEvent(boolean isStart) {
      Iterator i$ = this.m_testListeners.iterator();

      while(i$.hasNext()) {
         ITestListener itl = (ITestListener)i$.next();
         if (isStart) {
            itl.onStart(this);
         } else {
            itl.onFinish(this);
         }
      }

   }

   public String getName() {
      return this.m_testName;
   }

   public Date getStartDate() {
      return this.m_startDate;
   }

   public Date getEndDate() {
      return this.m_endDate;
   }

   public IResultMap getPassedTests() {
      return this.m_passedTests;
   }

   public IResultMap getSkippedTests() {
      return this.m_skippedTests;
   }

   public IResultMap getFailedTests() {
      return this.m_failedTests;
   }

   public IResultMap getFailedButWithinSuccessPercentageTests() {
      return this.m_failedButWithinSuccessPercentageTests;
   }

   public String[] getIncludedGroups() {
      Map<String, String> ig = this.m_xmlMethodSelector.getIncludedGroups();
      String[] result = (String[])ig.values().toArray(new String[ig.size()]);
      return result;
   }

   public String[] getExcludedGroups() {
      Map<String, String> eg = this.m_xmlMethodSelector.getExcludedGroups();
      String[] result = (String[])eg.values().toArray(new String[eg.size()]);
      return result;
   }

   public String getOutputDirectory() {
      return this.m_outputDirectory;
   }

   public ISuite getSuite() {
      return this.m_suite;
   }

   public ITestNGMethod[] getAllTestMethods() {
      return this.m_allTestMethods;
   }

   public String getHost() {
      return this.m_host;
   }

   public Collection<ITestNGMethod> getExcludedMethods() {
      Map<ITestNGMethod, ITestNGMethod> vResult = Maps.newHashMap();
      Iterator i$ = this.m_excludedMethods.iterator();

      while(i$.hasNext()) {
         ITestNGMethod m = (ITestNGMethod)i$.next();
         vResult.put(m, m);
      }

      return vResult.keySet();
   }

   public IResultMap getFailedConfigurations() {
      return this.m_failedConfigurations;
   }

   public IResultMap getPassedConfigurations() {
      return this.m_passedConfigurations;
   }

   public IResultMap getSkippedConfigurations() {
      return this.m_skippedConfigurations;
   }

   public void addPassedTest(ITestNGMethod tm, ITestResult tr) {
      this.m_passedTests.addResult(tr, tm);
   }

   public Set<ITestResult> getPassedTests(ITestNGMethod tm) {
      return this.m_passedTests.getResults(tm);
   }

   public Set<ITestResult> getFailedTests(ITestNGMethod tm) {
      return this.m_failedTests.getResults(tm);
   }

   public Set<ITestResult> getSkippedTests(ITestNGMethod tm) {
      return this.m_skippedTests.getResults(tm);
   }

   public void addSkippedTest(ITestNGMethod tm, ITestResult tr) {
      this.m_skippedTests.addResult(tr, tm);
   }

   public void addInvokedMethod(InvokedMethod im) {
      synchronized(this.m_invokedMethods) {
         this.m_invokedMethods.add(im);
      }
   }

   public void addFailedTest(ITestNGMethod testMethod, ITestResult result) {
      this.logFailedTest(testMethod, result, false);
   }

   public void addFailedButWithinSuccessPercentageTest(ITestNGMethod testMethod, ITestResult result) {
      this.logFailedTest(testMethod, result, true);
   }

   public XmlTest getTest() {
      return this.m_xmlTest;
   }

   public List<ITestListener> getTestListeners() {
      return this.m_testListeners;
   }

   public List<IConfigurationListener> getConfigurationListeners() {
      return Lists.newArrayList((Collection)this.m_configurationListeners);
   }

   private void logFailedTest(ITestNGMethod method, ITestResult tr, boolean withinSuccessPercentage) {
      if (withinSuccessPercentage) {
         this.m_failedButWithinSuccessPercentageTests.addResult(tr, method);
      } else {
         this.m_failedTests.addResult(tr, method);
      }

   }

   private String mapToString(Map<?, ?> m) {
      StringBuffer result = new StringBuffer();
      Iterator i$ = m.values().iterator();

      while(i$.hasNext()) {
         Object o = i$.next();
         result.append(o.toString()).append(" ");
      }

      return result.toString();
   }

   private void log(int level, String s) {
      Utils.log("TestRunner", level, s);
   }

   public static int getVerbose() {
      return m_verbose;
   }

   public void setVerbose(int n) {
      m_verbose = n;
   }

   private void log(String s) {
      Utils.log("TestRunner", 2, s);
   }

   public void addListener(Object listener) {
      if (listener instanceof ITestListener) {
         this.addTestListener((ITestListener)listener);
      }

      if (listener instanceof IConfigurationListener) {
         this.addConfigurationListener((IConfigurationListener)listener);
      }

   }

   public void addTestListener(ITestListener il) {
      this.m_testListeners.add(il);
   }

   void addConfigurationListener(IConfigurationListener icl) {
      this.m_configurationListeners.add(icl);
   }

   private void dumpInvokedMethods() {
      System.out.println("===== Invoked methods");
      Iterator i$ = this.m_invokedMethods.iterator();

      while(true) {
         IInvokedMethod im;
         while(true) {
            if (!i$.hasNext()) {
               System.out.println("=====");
               return;
            }

            im = (IInvokedMethod)i$.next();
            if (im.isTestMethod()) {
               System.out.print("    ");
               break;
            }

            if (im.isConfigurationMethod()) {
               System.out.print("  ");
               break;
            }
         }

         System.out.println("" + im);
      }
   }

   public List<ITestNGMethod> getInvokedMethods() {
      List<ITestNGMethod> result = Lists.newArrayList();
      synchronized(this.m_invokedMethods) {
         Iterator i$ = this.m_invokedMethods.iterator();

         while(i$.hasNext()) {
            IInvokedMethod im = (IInvokedMethod)i$.next();
            ITestNGMethod tm = im.getTestMethod();
            tm.setDate(im.getDate());
            result.add(tm);
         }

         return result;
      }
   }

   /** @deprecated */
   @Deprecated
   public void setMethodInterceptor(IMethodInterceptor methodInterceptor) {
      this.m_methodInterceptors.add(methodInterceptor);
   }

   public void addMethodInterceptor(IMethodInterceptor methodInterceptor) {
      this.m_methodInterceptors.add(methodInterceptor);
   }

   public XmlTest getCurrentXmlTest() {
      return this.m_xmlTest;
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

   public List<Module> getGuiceModules(Class<? extends Module> cls) {
      List<Module> result = (List)this.m_guiceModules.get(cls);
      return result;
   }

   private void addGuiceModule(Class<? extends Module> cls, Module module) {
      this.m_guiceModules.put(cls, module);
   }

   public Injector getInjector(List<Module> moduleInstances) {
      return (Injector)this.m_injectors.get(moduleInstances);
   }

   public Injector getInjector(IClass iClass) {
      Annotation annotation = AnnotationHelper.findAnnotationSuperClasses(Guice.class, iClass.getRealClass());
      if (annotation == null) {
         return null;
      } else {
         if (iClass instanceof TestClass) {
            iClass = ((TestClass)iClass).getIClass();
         }

         if (!(iClass instanceof ClassImpl)) {
            return null;
         } else {
            Injector parentInjector = ((ClassImpl)iClass).getParentInjector();
            Guice guice = (Guice)annotation;
            List<Module> moduleInstances = Lists.newArrayList((Object[])this.getModules(guice, parentInjector, iClass.getRealClass()));
            Injector injector = this.getInjector(moduleInstances);
            if (injector == null) {
               injector = parentInjector.createChildInjector((Iterable)moduleInstances);
               this.addInjector(moduleInstances, injector);
            }

            return injector;
         }
      }
   }

   private Module[] getModules(Guice guice, Injector parentInjector, Class<?> testClass) {
      List<Module> result = Lists.newArrayList();
      Class[] arr$ = guice.modules();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Class<? extends Module> moduleClass = arr$[i$];
         List<Module> modules = this.getGuiceModules(moduleClass);
         if (modules != null && modules.size() > 0) {
            result.addAll(modules);
         } else {
            Module instance = (Module)parentInjector.getInstance(moduleClass);
            result.add(instance);
            this.addGuiceModule(moduleClass, instance);
         }
      }

      Class<? extends IModuleFactory> factory = guice.moduleFactory();
      if (factory != IModuleFactory.class) {
         IModuleFactory factoryInstance = (IModuleFactory)parentInjector.getInstance(factory);
         Module moduleClass = factoryInstance.createModule(this, testClass);
         if (moduleClass != null) {
            result.add(moduleClass);
         }
      }

      return (Module[])result.toArray(new Module[result.size()]);
   }

   public void addInjector(List<Module> moduleInstances, Injector injector) {
      this.m_injectors.put(moduleInstances, injector);
   }

   private class ConfigurationListener implements IConfigurationListener2 {
      private ConfigurationListener() {
      }

      public void beforeConfiguration(ITestResult tr) {
      }

      public void onConfigurationFailure(ITestResult itr) {
         TestRunner.this.m_failedConfigurations.addResult(itr, itr.getMethod());
      }

      public void onConfigurationSkip(ITestResult itr) {
         TestRunner.this.m_skippedConfigurations.addResult(itr, itr.getMethod());
      }

      public void onConfigurationSuccess(ITestResult itr) {
         TestRunner.this.m_passedConfigurations.addResult(itr, itr.getMethod());
      }

      // $FF: synthetic method
      ConfigurationListener(Object x1) {
         this();
      }
   }

   private class ListenerHolder {
      private List<Class<? extends ITestNGListener>> listenerClasses;
      private Class<? extends ITestNGListenerFactory> listenerFactoryClass;

      private ListenerHolder() {
      }

      // $FF: synthetic method
      ListenerHolder(Object x1) {
         this();
      }
   }
}
