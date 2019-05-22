package org.testng.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.testng.IClass;
import org.testng.IConfigurable;
import org.testng.IConfigurationListener;
import org.testng.IConfigurationListener2;
import org.testng.IHookable;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IRetryAnalyzer;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.SuiteRunState;
import org.testng.TestException;
import org.testng.TestNGException;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.NoInjection;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.testng.collections.Sets;
import org.testng.internal.annotations.AnnotationHelper;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.internal.invokers.InvokedMethodListenerInvoker;
import org.testng.internal.invokers.InvokedMethodListenerMethod;
import org.testng.internal.thread.ThreadExecutionException;
import org.testng.internal.thread.ThreadUtil;
import org.testng.internal.thread.graph.IWorker;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class Invoker implements IInvoker {
   private final ITestContext m_testContext;
   private final ITestResultNotifier m_notifier;
   private final IAnnotationFinder m_annotationFinder;
   private final SuiteRunState m_suiteState;
   private final boolean m_skipFailedInvocationCounts;
   private final List<IInvokedMethodListener> m_invokedMethodListeners;
   private final boolean m_continueOnFailedConfiguration;
   private Map<String, Boolean> m_beforegroupsFailures = Maps.newHashtable();
   private Map<Class<?>, Set<Object>> m_classInvocationResults = Maps.newHashtable();
   private Map<ITestNGMethod, Set<Object>> m_methodInvocationResults = Maps.newHashtable();
   private IConfiguration m_configuration;
   private static Invoker.Predicate<ITestNGMethod, IClass> CAN_RUN_FROM_CLASS = new Invoker.CanRunFromClassPredicate();
   private static final Invoker.Predicate<ITestNGMethod, IClass> SAME_CLASS = new Invoker.SameClassNamePredicate();

   private void setClassInvocationFailure(Class<?> clazz, Object instance) {
      Set<Object> instances = (Set)this.m_classInvocationResults.get(clazz);
      if (instances == null) {
         instances = Sets.newHashSet();
         this.m_classInvocationResults.put(clazz, instances);
      }

      instances.add(instance);
   }

   private void setMethodInvocationFailure(ITestNGMethod method, Object instance) {
      Set<Object> instances = (Set)this.m_methodInvocationResults.get(method);
      if (instances == null) {
         instances = Sets.newHashSet();
         this.m_methodInvocationResults.put(method, instances);
      }

      instances.add(this.getMethodInvocationToken(method, instance));
   }

   public Invoker(IConfiguration configuration, ITestContext testContext, ITestResultNotifier notifier, SuiteRunState state, boolean skipFailedInvocationCounts, List<IInvokedMethodListener> invokedMethodListeners) {
      this.m_configuration = configuration;
      this.m_testContext = testContext;
      this.m_suiteState = state;
      this.m_notifier = notifier;
      this.m_annotationFinder = configuration.getAnnotationFinder();
      this.m_skipFailedInvocationCounts = skipFailedInvocationCounts;
      this.m_invokedMethodListeners = invokedMethodListeners;
      this.m_continueOnFailedConfiguration = "continue".equals(testContext.getSuite().getXmlSuite().getConfigFailurePolicy());
   }

   public void invokeConfigurations(IClass testClass, ITestNGMethod[] allMethods, XmlSuite suite, Map<String, String> params, Object[] parameterValues, Object instance) {
      this.invokeConfigurations(testClass, (ITestNGMethod)null, allMethods, suite, params, parameterValues, instance, (ITestResult)null);
   }

   private void invokeConfigurations(IClass testClass, ITestNGMethod currentTestMethod, ITestNGMethod[] allMethods, XmlSuite suite, Map<String, String> params, Object[] parameterValues, Object instance, ITestResult testMethodResult) {
      if (null == allMethods) {
         this.log(5, "No configuration methods found");
      } else {
         ITestNGMethod[] methods = this.filterMethods((IClass)testClass, allMethods, SAME_CLASS);
         ITestNGMethod[] arr$ = methods;
         int len$ = methods.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            ITestNGMethod tm = arr$[i$];
            if (null == testClass) {
               testClass = tm.getTestClass();
            }

            ITestResult testResult = new TestResult((IClass)testClass, instance, tm, (Throwable)null, System.currentTimeMillis(), System.currentTimeMillis(), this.m_testContext);
            IConfigurationAnnotation configurationAnnotation = null;

            try {
               Object inst = tm.getInstance();
               if (inst == null) {
                  inst = instance;
               }

               Class<?> objectClass = inst.getClass();
               Method method = tm.getMethod();
               configurationAnnotation = AnnotationHelper.findConfiguration(this.m_annotationFinder, method);
               boolean alwaysRun = this.isAlwaysRun(configurationAnnotation);
               if (!MethodHelper.isEnabled(objectClass, this.m_annotationFinder) && !alwaysRun) {
                  this.log(3, "Skipping " + Utils.detailedMethodName(tm, true) + " because " + objectClass.getName() + " is not enabled");
               } else if (MethodHelper.isEnabled(configurationAnnotation)) {
                  if (!this.confInvocationPassed(tm, currentTestMethod, (IClass)testClass, instance) && !alwaysRun) {
                     this.handleConfigurationSkip(tm, testResult, configurationAnnotation, currentTestMethod, instance, suite);
                  } else {
                     this.log(3, "Invoking " + Utils.detailedMethodName(tm, true));
                     Object[] parameters = Parameters.createConfigurationParameters(tm.getMethod(), params, parameterValues, currentTestMethod, this.m_annotationFinder, suite, this.m_testContext, testMethodResult);
                     testResult.setParameters(parameters);
                     Object newInstance = null != instance ? instance : inst;
                     this.runConfigurationListeners(testResult, true);
                     this.invokeConfigurationMethod(newInstance, tm, parameters, testResult);
                     testResult.setEndMillis(System.currentTimeMillis());
                     this.runConfigurationListeners(testResult, false);
                  }
               } else {
                  this.log(3, "Skipping " + Utils.detailedMethodName(tm, true) + " because it is not enabled");
               }
            } catch (InvocationTargetException var22) {
               this.handleConfigurationFailure(var22, tm, testResult, configurationAnnotation, currentTestMethod, instance, suite);
            } catch (Throwable var23) {
               this.handleConfigurationFailure(var23, tm, testResult, configurationAnnotation, currentTestMethod, instance, suite);
            }
         }

      }
   }

   private void handleConfigurationSkip(ITestNGMethod tm, ITestResult testResult, IConfigurationAnnotation annotation, ITestNGMethod currentTestMethod, Object instance, XmlSuite suite) {
      this.recordConfigurationInvocationFailed(tm, testResult.getTestClass(), annotation, currentTestMethod, instance, suite);
      testResult.setStatus(3);
      this.runConfigurationListeners(testResult, false);
   }

   private boolean isAlwaysRun(IConfigurationAnnotation configurationAnnotation) {
      if (null == configurationAnnotation) {
         return false;
      } else {
         boolean alwaysRun = false;
         if ((configurationAnnotation.getAfterSuite() || configurationAnnotation.getAfterTest() || configurationAnnotation.getAfterTestClass() || configurationAnnotation.getAfterTestMethod() || configurationAnnotation.getBeforeTestMethod() || configurationAnnotation.getBeforeTestClass() || configurationAnnotation.getBeforeTest() || configurationAnnotation.getBeforeSuite()) && configurationAnnotation.getAlwaysRun()) {
            alwaysRun = true;
         }

         return alwaysRun;
      }
   }

   private void handleConfigurationFailure(Throwable ite, ITestNGMethod tm, ITestResult testResult, IConfigurationAnnotation annotation, ITestNGMethod currentTestMethod, Object instance, XmlSuite suite) {
      Throwable cause = ite.getCause() != null ? ite.getCause() : ite;
      if (this.isSkipExceptionAndSkip(cause)) {
         testResult.setThrowable(cause);
         this.handleConfigurationSkip(tm, testResult, annotation, currentTestMethod, instance, suite);
      } else {
         Utils.log("", 3, "Failed to invoke configuration method " + tm.getRealClass().getName() + "." + tm.getMethodName() + ":" + cause.getMessage());
         this.handleException(cause, tm, testResult, 1);
         this.runConfigurationListeners(testResult, false);
         if (null != annotation) {
            this.recordConfigurationInvocationFailed(tm, testResult.getTestClass(), annotation, currentTestMethod, instance, suite);
         }

      }
   }

   private XmlClass[] findClassesInSameTest(Class<?> cls, XmlSuite suite) {
      Map<String, XmlClass> vResult = Maps.newHashMap();
      String className = cls.getName();
      Iterator i$ = suite.getTests().iterator();

      label31:
      while(i$.hasNext()) {
         XmlTest test = (XmlTest)i$.next();
         Iterator i$ = test.getXmlClasses().iterator();

         while(true) {
            XmlClass testClass;
            do {
               if (!i$.hasNext()) {
                  continue label31;
               }

               testClass = (XmlClass)i$.next();
            } while(!testClass.getName().equals(className));

            Iterator i$ = test.getXmlClasses().iterator();

            while(i$.hasNext()) {
               XmlClass thisClass = (XmlClass)i$.next();
               vResult.put(thisClass.getName(), thisClass);
            }
         }
      }

      XmlClass[] result = (XmlClass[])vResult.values().toArray(new XmlClass[vResult.size()]);
      return result;
   }

   private void recordConfigurationInvocationFailed(ITestNGMethod tm, IClass testClass, IConfigurationAnnotation annotation, ITestNGMethod currentTestMethod, Object instance, XmlSuite suite) {
      int len$;
      int i$;
      if (!annotation.getBeforeTestClass() && !annotation.getAfterTestClass()) {
         if (!annotation.getBeforeTestMethod() && !annotation.getAfterTestMethod()) {
            if (!annotation.getBeforeSuite() && !annotation.getAfterSuite()) {
               if (annotation.getBeforeTest() || annotation.getAfterTest()) {
                  this.setClassInvocationFailure(tm.getRealClass(), instance);
                  XmlClass[] classes = this.findClassesInSameTest(tm.getRealClass(), suite);
                  XmlClass[] arr$ = classes;
                  len$ = classes.length;

                  for(i$ = 0; i$ < len$; ++i$) {
                     XmlClass xmlClass = arr$[i$];
                     this.setClassInvocationFailure(xmlClass.getSupportClass(), instance);
                  }
               }
            } else {
               this.m_suiteState.failed();
            }
         } else if (this.m_continueOnFailedConfiguration) {
            this.setMethodInvocationFailure(currentTestMethod, instance);
         } else {
            this.setClassInvocationFailure(tm.getRealClass(), instance);
         }
      } else if (this.m_continueOnFailedConfiguration) {
         this.setClassInvocationFailure(testClass.getRealClass(), instance);
      } else {
         this.setClassInvocationFailure(tm.getRealClass(), instance);
      }

      String[] beforeGroups = annotation.getBeforeGroups();
      if (null != beforeGroups && beforeGroups.length > 0) {
         String[] arr$ = beforeGroups;
         len$ = beforeGroups.length;

         for(i$ = 0; i$ < len$; ++i$) {
            String group = arr$[i$];
            this.m_beforegroupsFailures.put(group, Boolean.FALSE);
         }
      }

   }

   private boolean classConfigurationFailed(Class<?> cls) {
      Iterator i$ = this.m_classInvocationResults.keySet().iterator();

      Class c;
      do {
         if (!i$.hasNext()) {
            return false;
         }

         c = (Class)i$.next();
      } while(c != cls && !c.isAssignableFrom(cls));

      return true;
   }

   private boolean confInvocationPassed(ITestNGMethod method, ITestNGMethod currentTestMethod, IClass testClass, Object instance) {
      boolean result = true;
      Class<?> cls = testClass.getRealClass();
      if (this.m_suiteState.isFailed()) {
         result = false;
      } else if (this.classConfigurationFailed(cls)) {
         if (!this.m_continueOnFailedConfiguration) {
            result = !this.classConfigurationFailed(cls);
         } else {
            result = !((Set)this.m_classInvocationResults.get(cls)).contains(instance);
         }
      } else if (this.m_continueOnFailedConfiguration && currentTestMethod != null && this.m_methodInvocationResults.containsKey(currentTestMethod)) {
         result = !((Set)this.m_methodInvocationResults.get(currentTestMethod)).contains(this.getMethodInvocationToken(currentTestMethod, instance));
      } else if (!this.m_continueOnFailedConfiguration) {
         Iterator i$ = this.m_classInvocationResults.keySet().iterator();

         while(i$.hasNext()) {
            Class<?> clazz = (Class)i$.next();
            if (clazz.isAssignableFrom(cls)) {
               result = false;
               break;
            }
         }
      }

      String[] groups = method.getGroups();
      if (null != groups && groups.length > 0) {
         String[] arr$ = groups;
         int len$ = groups.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String group = arr$[i$];
            if (this.m_beforegroupsFailures.containsKey(group)) {
               result = false;
               break;
            }
         }
      }

      return result;
   }

   private Object getMethodInvocationToken(ITestNGMethod method, Object instance) {
      return String.format("%s+%d", instance.toString(), method.getCurrentInvocationCount());
   }

   private void invokeConfigurationMethod(Object targetInstance, ITestNGMethod tm, Object[] params, ITestResult testResult) throws InvocationTargetException, IllegalAccessException {
      tm.setId(ThreadUtil.currentThreadInfo());
      InvokedMethod invokedMethod = new InvokedMethod(targetInstance, tm, params, System.currentTimeMillis(), testResult);
      this.runInvokedMethodListeners(InvokedMethodListenerMethod.BEFORE_INVOCATION, invokedMethod, testResult);
      this.m_notifier.addInvokedMethod(invokedMethod);

      try {
         Reporter.setCurrentTestResult(testResult);
         Method method = tm.getMethod();
         IConfigurable configurableInstance = IConfigurable.class.isAssignableFrom(tm.getMethod().getDeclaringClass()) ? (IConfigurable)targetInstance : this.m_configuration.getConfigurable();
         if (configurableInstance != null) {
            MethodInvocationHelper.invokeConfigurable(targetInstance, params, configurableInstance, method, testResult);
         } else if (MethodHelper.calculateTimeOut(tm) <= 0L) {
            MethodInvocationHelper.invokeMethod(method, targetInstance, params);
         } else {
            MethodInvocationHelper.invokeWithTimeout(tm, targetInstance, params, testResult);
            if (!testResult.isSuccess()) {
               this.throwConfigurationFailure(testResult, testResult.getThrowable());
               throw testResult.getThrowable();
            }
         }
      } catch (IllegalAccessException | InvocationTargetException var12) {
         this.throwConfigurationFailure(testResult, var12);
         throw var12;
      } catch (Throwable var13) {
         this.throwConfigurationFailure(testResult, var13);
         throw new TestNGException(var13);
      } finally {
         Reporter.setCurrentTestResult(testResult);
         this.runInvokedMethodListeners(InvokedMethodListenerMethod.AFTER_INVOCATION, invokedMethod, testResult);
         Reporter.setCurrentTestResult((ITestResult)null);
      }

   }

   private void throwConfigurationFailure(ITestResult testResult, Throwable ex) {
      testResult.setStatus(2);
      testResult.setThrowable(ex.getCause() == null ? ex : ex.getCause());
   }

   private void runInvokedMethodListeners(InvokedMethodListenerMethod listenerMethod, IInvokedMethod invokedMethod, ITestResult testResult) {
      if (!this.noListenersPresent()) {
         InvokedMethodListenerInvoker invoker = new InvokedMethodListenerInvoker(listenerMethod, testResult, this.m_testContext);
         Iterator i$ = this.m_invokedMethodListeners.iterator();

         while(i$.hasNext()) {
            IInvokedMethodListener currentListener = (IInvokedMethodListener)i$.next();
            invoker.invokeListener(currentListener, invokedMethod);
         }

      }
   }

   private boolean noListenersPresent() {
      return this.m_invokedMethodListeners == null || this.m_invokedMethodListeners.size() == 0;
   }

   private ITestResult invokeMethod(Object instance, ITestNGMethod tm, Object[] parameterValues, int parametersIndex, XmlSuite suite, Map<String, String> params, ITestClass testClass, ITestNGMethod[] beforeMethods, ITestNGMethod[] afterMethods, ConfigurationGroupMethods groupMethods, Invoker.FailureContext failureContext) {
      TestResult testResult = new TestResult();
      this.invokeBeforeGroupsConfigurations(testClass, tm, groupMethods, suite, params, instance);
      this.invokeConfigurations(testClass, tm, this.filterConfigurationMethods(tm, beforeMethods, true), suite, params, parameterValues, instance, testResult);
      InvokedMethod invokedMethod = null;
      boolean var23 = false;

      ExpectedExceptionsHolder expectedExceptionClasses;
      List results;
      label238: {
         label239: {
            label240: {
               try {
                  var23 = true;
                  testResult.init(testClass, instance, tm, (Throwable)null, System.currentTimeMillis(), 0L, this.m_testContext);
                  testResult.setParameters(parameterValues);
                  testResult.setHost(this.m_testContext.getHost());
                  testResult.setStatus(16);
                  invokedMethod = new InvokedMethod(instance, tm, parameterValues, System.currentTimeMillis(), testResult);
                  this.runTestListeners(testResult);
                  this.runInvokedMethodListeners(InvokedMethodListenerMethod.BEFORE_INVOCATION, invokedMethod, testResult);
                  this.m_notifier.addInvokedMethod(invokedMethod);
                  Method thisMethod = tm.getConstructorOrMethod().getMethod();
                  if (this.confInvocationPassed(tm, tm, testClass, instance)) {
                     this.log(3, "Invoking " + tm.getRealClass().getName() + "." + tm.getMethodName());
                     Reporter.setCurrentTestResult(testResult);
                     IHookable hookableInstance = IHookable.class.isAssignableFrom(tm.getRealClass()) ? (IHookable)instance : this.m_configuration.getHookable();
                     if (MethodHelper.calculateTimeOut(tm) <= 0L) {
                        if (hookableInstance != null) {
                           MethodInvocationHelper.invokeHookable(instance, parameterValues, hookableInstance, thisMethod, testResult);
                        } else {
                           MethodInvocationHelper.invokeMethod(thisMethod, instance, parameterValues);
                        }

                        testResult.setStatus(1);
                        var23 = false;
                     } else {
                        MethodInvocationHelper.invokeWithTimeout(tm, instance, parameterValues, testResult, hookableInstance);
                        var23 = false;
                     }
                  } else {
                     testResult.setStatus(3);
                     var23 = false;
                  }
                  break label238;
               } catch (InvocationTargetException var24) {
                  testResult.setThrowable(var24.getCause());
                  testResult.setStatus(2);
                  var23 = false;
               } catch (ThreadExecutionException var25) {
                  Throwable cause = var25.getCause();
                  if (InvokeMethodRunnable.TestNGRuntimeException.class.equals(cause.getClass())) {
                     testResult.setThrowable(cause.getCause());
                  } else {
                     testResult.setThrowable(cause);
                  }

                  testResult.setStatus(2);
                  var23 = false;
                  break label240;
               } catch (Throwable var26) {
                  testResult.setThrowable(var26);
                  testResult.setStatus(2);
                  var23 = false;
                  break label239;
               } finally {
                  if (var23) {
                     testResult.setEndMillis(System.currentTimeMillis());
                     ExpectedExceptionsHolder expectedExceptionClasses = new ExpectedExceptionsHolder(this.m_annotationFinder, tm, new RegexpExpectedExceptionsHolder(this.m_annotationFinder, tm));
                     List results = Lists.newArrayList((Object[])(testResult));
                     this.handleInvocationResults(tm, results, expectedExceptionClasses, false, false, failureContext);
                     if (testResult.getThrowable() != null && parameterValues.length > 0) {
                        tm.addFailedInvocationNumber(parametersIndex);
                     }

                     tm.incrementCurrentInvocationCount();
                     this.runInvokedMethodListeners(InvokedMethodListenerMethod.AFTER_INVOCATION, invokedMethod, testResult);
                     this.runTestListeners(testResult);
                     if (!results.isEmpty()) {
                        this.collectResults(tm, Collections.singleton(testResult));
                     }

                     this.invokeConfigurations(testClass, tm, this.filterConfigurationMethods(tm, afterMethods, false), suite, params, parameterValues, instance, testResult);
                     this.invokeAfterGroupsConfigurations(testClass, tm, groupMethods, suite, params, instance);
                     Reporter.setCurrentTestResult((ITestResult)null);
                  }
               }

               testResult.setEndMillis(System.currentTimeMillis());
               expectedExceptionClasses = new ExpectedExceptionsHolder(this.m_annotationFinder, tm, new RegexpExpectedExceptionsHolder(this.m_annotationFinder, tm));
               results = Lists.newArrayList((Object[])(testResult));
               this.handleInvocationResults(tm, results, expectedExceptionClasses, false, false, failureContext);
               if (testResult.getThrowable() != null && parameterValues.length > 0) {
                  tm.addFailedInvocationNumber(parametersIndex);
               }

               tm.incrementCurrentInvocationCount();
               this.runInvokedMethodListeners(InvokedMethodListenerMethod.AFTER_INVOCATION, invokedMethod, testResult);
               this.runTestListeners(testResult);
               if (!results.isEmpty()) {
                  this.collectResults(tm, Collections.singleton(testResult));
               }

               this.invokeConfigurations(testClass, tm, this.filterConfigurationMethods(tm, afterMethods, false), suite, params, parameterValues, instance, testResult);
               this.invokeAfterGroupsConfigurations(testClass, tm, groupMethods, suite, params, instance);
               Reporter.setCurrentTestResult((ITestResult)null);
               return testResult;
            }

            testResult.setEndMillis(System.currentTimeMillis());
            expectedExceptionClasses = new ExpectedExceptionsHolder(this.m_annotationFinder, tm, new RegexpExpectedExceptionsHolder(this.m_annotationFinder, tm));
            results = Lists.newArrayList((Object[])(testResult));
            this.handleInvocationResults(tm, results, expectedExceptionClasses, false, false, failureContext);
            if (testResult.getThrowable() != null && parameterValues.length > 0) {
               tm.addFailedInvocationNumber(parametersIndex);
            }

            tm.incrementCurrentInvocationCount();
            this.runInvokedMethodListeners(InvokedMethodListenerMethod.AFTER_INVOCATION, invokedMethod, testResult);
            this.runTestListeners(testResult);
            if (!results.isEmpty()) {
               this.collectResults(tm, Collections.singleton(testResult));
            }

            this.invokeConfigurations(testClass, tm, this.filterConfigurationMethods(tm, afterMethods, false), suite, params, parameterValues, instance, testResult);
            this.invokeAfterGroupsConfigurations(testClass, tm, groupMethods, suite, params, instance);
            Reporter.setCurrentTestResult((ITestResult)null);
            return testResult;
         }

         testResult.setEndMillis(System.currentTimeMillis());
         expectedExceptionClasses = new ExpectedExceptionsHolder(this.m_annotationFinder, tm, new RegexpExpectedExceptionsHolder(this.m_annotationFinder, tm));
         results = Lists.newArrayList((Object[])(testResult));
         this.handleInvocationResults(tm, results, expectedExceptionClasses, false, false, failureContext);
         if (testResult.getThrowable() != null && parameterValues.length > 0) {
            tm.addFailedInvocationNumber(parametersIndex);
         }

         tm.incrementCurrentInvocationCount();
         this.runInvokedMethodListeners(InvokedMethodListenerMethod.AFTER_INVOCATION, invokedMethod, testResult);
         this.runTestListeners(testResult);
         if (!results.isEmpty()) {
            this.collectResults(tm, Collections.singleton(testResult));
         }

         this.invokeConfigurations(testClass, tm, this.filterConfigurationMethods(tm, afterMethods, false), suite, params, parameterValues, instance, testResult);
         this.invokeAfterGroupsConfigurations(testClass, tm, groupMethods, suite, params, instance);
         Reporter.setCurrentTestResult((ITestResult)null);
         return testResult;
      }

      testResult.setEndMillis(System.currentTimeMillis());
      expectedExceptionClasses = new ExpectedExceptionsHolder(this.m_annotationFinder, tm, new RegexpExpectedExceptionsHolder(this.m_annotationFinder, tm));
      results = Lists.newArrayList((Object[])(testResult));
      this.handleInvocationResults(tm, results, expectedExceptionClasses, false, false, failureContext);
      if (testResult.getThrowable() != null && parameterValues.length > 0) {
         tm.addFailedInvocationNumber(parametersIndex);
      }

      tm.incrementCurrentInvocationCount();
      this.runInvokedMethodListeners(InvokedMethodListenerMethod.AFTER_INVOCATION, invokedMethod, testResult);
      this.runTestListeners(testResult);
      if (!results.isEmpty()) {
         this.collectResults(tm, Collections.singleton(testResult));
      }

      this.invokeConfigurations(testClass, tm, this.filterConfigurationMethods(tm, afterMethods, false), suite, params, parameterValues, instance, testResult);
      this.invokeAfterGroupsConfigurations(testClass, tm, groupMethods, suite, params, instance);
      Reporter.setCurrentTestResult((ITestResult)null);
      return testResult;
   }

   void collectResults(ITestNGMethod testMethod, Collection<ITestResult> results) {
      Iterator i$ = results.iterator();

      while(i$.hasNext()) {
         ITestResult result = (ITestResult)i$.next();
         int status = result.getStatus();
         if (1 == status) {
            this.m_notifier.addPassedTest(testMethod, result);
         } else if (3 == status) {
            this.m_notifier.addSkippedTest(testMethod, result);
         } else if (2 == status) {
            this.m_notifier.addFailedTest(testMethod, result);
         } else if (4 == status) {
            this.m_notifier.addFailedButWithinSuccessPercentageTest(testMethod, result);
         } else {
            assert false : "UNKNOWN STATUS:" + status;
         }
      }

   }

   private ITestNGMethod[] filterConfigurationMethods(ITestNGMethod tm, ITestNGMethod[] methods, boolean isBefore) {
      List<ITestNGMethod> result = Lists.newArrayList();
      ITestNGMethod[] arr$ = methods;
      int len$ = methods.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ITestNGMethod m = arr$[i$];
         ConfigurationMethod cm = (ConfigurationMethod)m;
         if (isBefore) {
            if (!cm.isFirstTimeOnly() || cm.isFirstTimeOnly() && tm.getCurrentInvocationCount() == 0) {
               result.add(m);
            }
         } else {
            int current = tm.getCurrentInvocationCount();
            boolean isLast = false;
            if (tm.getParameterInvocationCount() > 0) {
               isLast = current == tm.getParameterInvocationCount() * tm.getTotalInvocationCount();
            } else if (tm.getTotalInvocationCount() > 1) {
               isLast = current == tm.getTotalInvocationCount();
            }

            if (!cm.isLastTimeOnly() || cm.isLastTimeOnly() && isLast) {
               result.add(m);
            }
         }
      }

      return (ITestNGMethod[])result.toArray(new ITestNGMethod[result.size()]);
   }

   protected ITestResult invokeTestMethod(Object instance, ITestNGMethod tm, Object[] parameterValues, int parametersIndex, XmlSuite suite, Map<String, String> params, ITestClass testClass, ITestNGMethod[] beforeMethods, ITestNGMethod[] afterMethods, ConfigurationGroupMethods groupMethods, Invoker.FailureContext failureContext) {
      tm.setId(ThreadUtil.currentThreadInfo());
      ITestResult result = this.invokeMethod(instance, tm, parameterValues, parametersIndex, suite, params, testClass, beforeMethods, afterMethods, groupMethods, failureContext);
      return result;
   }

   private void invokeBeforeGroupsConfigurations(ITestClass testClass, ITestNGMethod currentTestMethod, ConfigurationGroupMethods groupMethods, XmlSuite suite, Map<String, String> params, Object instance) {
      synchronized(groupMethods) {
         List<ITestNGMethod> filteredMethods = Lists.newArrayList();
         String[] groups = currentTestMethod.getGroups();
         Map<String, List<ITestNGMethod>> beforeGroupMap = groupMethods.getBeforeGroupsMap();
         String[] arr$ = groups;
         int len$ = groups.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String group = arr$[i$];
            List<ITestNGMethod> methods = (List)beforeGroupMap.get(group);
            if (methods != null) {
               filteredMethods.addAll(methods);
            }
         }

         ITestNGMethod[] beforeMethodsArray = (ITestNGMethod[])filteredMethods.toArray(new ITestNGMethod[filteredMethods.size()]);
         if (beforeMethodsArray.length > 0) {
            this.invokeConfigurations((IClass)null, beforeMethodsArray, suite, params, (Object[])null, (Object)null);
         }

         groupMethods.removeBeforeGroups(groups);
      }
   }

   private void invokeAfterGroupsConfigurations(ITestClass testClass, ITestNGMethod currentTestMethod, ConfigurationGroupMethods groupMethods, XmlSuite suite, Map<String, String> params, Object instance) {
      if (currentTestMethod.getGroups().length != 0) {
         Map<String, String> filteredGroups = Maps.newHashMap();
         String[] groups = currentTestMethod.getGroups();
         synchronized(groupMethods) {
            String[] arr$ = groups;
            int len$ = groups.length;

            String g;
            for(int i$ = 0; i$ < len$; ++i$) {
               g = arr$[i$];
               if (groupMethods.isLastMethodForGroup(g, currentTestMethod)) {
                  filteredGroups.put(g, g);
               }
            }

            if (!filteredGroups.isEmpty()) {
               Map<ITestNGMethod, ITestNGMethod> afterMethods = Maps.newHashMap();
               Map<String, List<ITestNGMethod>> map = groupMethods.getAfterGroupsMap();
               Iterator i$ = filteredGroups.values().iterator();

               while(true) {
                  List methods;
                  do {
                     if (!i$.hasNext()) {
                        ITestNGMethod[] afterMethodsArray = (ITestNGMethod[])afterMethods.keySet().toArray(new ITestNGMethod[afterMethods.size()]);
                        this.invokeConfigurations((IClass)null, afterMethodsArray, suite, params, (Object[])null, (Object)null);
                        groupMethods.removeAfterGroups(filteredGroups.keySet());
                        return;
                     }

                     g = (String)i$.next();
                     methods = (List)map.get(g);
                  } while(methods == null);

                  Iterator i$ = methods.iterator();

                  while(i$.hasNext()) {
                     ITestNGMethod m = (ITestNGMethod)i$.next();
                     afterMethods.put(m, m);
                  }
               }
            }
         }
      }
   }

   private Object[] getParametersFromIndex(Iterator<Object[]> parametersValues, int index) {
      while(parametersValues.hasNext()) {
         Object[] parameters = (Object[])parametersValues.next();
         if (index == 0) {
            return parameters;
         }

         --index;
      }

      return null;
   }

   int retryFailed(Object instance, ITestNGMethod tm, XmlSuite suite, ITestClass testClass, ITestNGMethod[] beforeMethods, ITestNGMethod[] afterMethods, ConfigurationGroupMethods groupMethods, List<ITestResult> result, int failureCount, ExpectedExceptionsHolder expectedExceptionHolder, ITestContext testContext, Map<String, String> parameters, int parametersIndex) {
      Invoker.FailureContext failure = new Invoker.FailureContext();
      failure.count = failureCount;

      do {
         failure.instances = Lists.newArrayList();
         Map<String, String> allParameters = Maps.newHashMap();
         Invoker.ParameterBag bag = this.createParameters(tm, parameters, allParameters, suite, testContext, (Object)null);
         Object[] parameterValues = this.getParametersFromIndex(bag.parameterHolder.parameters, parametersIndex);
         result.add(this.invokeMethod(instance, tm, parameterValues, parametersIndex, suite, allParameters, testClass, beforeMethods, afterMethods, groupMethods, failure));
         this.handleInvocationResults(tm, result, expectedExceptionHolder, true, true, failure);
      } while(!failure.instances.isEmpty());

      return failure.count;
   }

   private Invoker.ParameterBag createParameters(ITestNGMethod testMethod, Map<String, String> parameters, Map<String, String> allParameterNames, XmlSuite suite, ITestContext testContext, Object fedInstance) {
      Object instance;
      if (fedInstance != null) {
         instance = fedInstance;
      } else {
         instance = testMethod.getInstance();
      }

      Invoker.ParameterBag bag = this.handleParameters(testMethod, instance, allParameterNames, parameters, (Object[])null, suite, testContext, fedInstance, (ITestResult)null);
      return bag;
   }

   public List<ITestResult> invokeTestMethods(ITestNGMethod testMethod, XmlSuite suite, Map<String, String> testParameters, ConfigurationGroupMethods groupMethods, Object instance, ITestContext testContext) {
      assert null != testMethod.getTestClass() : "COULDN'T FIND TESTCLASS FOR " + testMethod.getRealClass();

      if (!MethodHelper.isEnabled(testMethod.getMethod(), this.m_annotationFinder)) {
         return Collections.emptyList();
      } else {
         String okToProceed = this.checkDependencies(testMethod, testContext.getAllTestMethods());
         if (okToProceed != null) {
            ITestResult result = this.registerSkippedTestResult(testMethod, (Object)null, System.currentTimeMillis(), new Throwable(okToProceed));
            this.m_notifier.addSkippedTest(testMethod, result);
            return Collections.singletonList(result);
         } else {
            Map<String, String> parameters = testMethod.findMethodParameters(testContext.getCurrentXmlTest());
            if (testMethod.getInvocationCount() > 1 && testMethod.getThreadPoolSize() > 1) {
               return this.invokePooledTestMethods(testMethod, suite, parameters, groupMethods, testContext);
            } else {
               long timeOutInvocationCount = testMethod.getInvocationTimeOut();
               boolean onlyOne = testMethod.getThreadPoolSize() > 1 || timeOutInvocationCount > 0L;
               int invocationCount = onlyOne ? 1 : testMethod.getInvocationCount();
               ExpectedExceptionsHolder expectedExceptionHolder = new ExpectedExceptionsHolder(this.m_annotationFinder, testMethod, new RegexpExpectedExceptionsHolder(this.m_annotationFinder, testMethod));
               ITestClass testClass = testMethod.getTestClass();
               List<ITestResult> result = Lists.newArrayList();
               Invoker.FailureContext failure = new Invoker.FailureContext();
               ITestNGMethod[] beforeMethods = this.filterMethods(testClass, testClass.getBeforeTestMethods(), CAN_RUN_FROM_CLASS);
               ITestNGMethod[] afterMethods = this.filterMethods(testClass, testClass.getAfterTestMethods(), CAN_RUN_FROM_CLASS);

               while(true) {
                  label288:
                  while(invocationCount-- > 0) {
                     long start = System.currentTimeMillis();
                     Map<String, String> allParameterNames = Maps.newHashMap();
                     Invoker.ParameterBag bag = this.createParameters(testMethod, parameters, allParameterNames, suite, testContext, instance);
                     if (bag.hasErrors()) {
                        ITestResult tr = bag.errorResult;
                        tr.setStatus(3);
                        this.runTestListeners(tr);
                        this.m_notifier.addSkippedTest(testMethod, tr);
                        result.add(tr);
                     } else {
                        Iterator<Object[]> allParameterValues = bag.parameterHolder.parameters;
                        int parametersIndex = 0;

                        try {
                           List<TestMethodWithDataProviderMethodWorker> workers = Lists.newArrayList();
                           List tmpResults;
                           Iterator i$;
                           Object[] parameterValues;
                           if (bag.parameterHolder.origin == ParameterHolder.ParameterOrigin.ORIGIN_DATA_PROVIDER && bag.parameterHolder.dataProviderHolder.annotation.isParallel()) {
                              while(allParameterValues.hasNext()) {
                                 parameterValues = this.injectParameters((Object[])allParameterValues.next(), testMethod.getMethod(), testContext, (ITestResult)null);
                                 TestMethodWithDataProviderMethodWorker w = new TestMethodWithDataProviderMethodWorker(this, testMethod, parametersIndex, parameterValues, instance, suite, parameters, testClass, beforeMethods, afterMethods, groupMethods, expectedExceptionHolder, testContext, this.m_skipFailedInvocationCounts, invocationCount, failure.count, this.m_notifier);
                                 workers.add(w);
                                 ++parametersIndex;
                              }

                              PoolService<List<ITestResult>> ps = new PoolService(suite.getDataProviderThreadCount());
                              tmpResults = ps.submitTasksAndWait(workers);
                              i$ = tmpResults.iterator();

                              while(i$.hasNext()) {
                                 List<ITestResult> l2 = (List)i$.next();
                                 result.addAll(l2);
                              }
                           } else {
                              while(allParameterValues.hasNext()) {
                                 parameterValues = this.injectParameters((Object[])allParameterValues.next(), testMethod.getMethod(), testContext, (ITestResult)null);
                                 tmpResults = Lists.newArrayList();
                                 boolean var37 = false;

                                 try {
                                    var37 = true;
                                    tmpResults.add(this.invokeTestMethod(instance, testMethod, parameterValues, parametersIndex, suite, parameters, testClass, beforeMethods, afterMethods, groupMethods, failure));
                                    var37 = false;
                                 } finally {
                                    if (var37) {
                                       if (failure.instances.isEmpty()) {
                                          result.addAll(tmpResults);
                                       } else {
                                          Iterator i$ = failure.instances.iterator();

                                          while(i$.hasNext()) {
                                             Object failedInstance = i$.next();
                                             List<ITestResult> retryResults = Lists.newArrayList();
                                             failure.count = this.retryFailed(failedInstance, testMethod, suite, testClass, beforeMethods, afterMethods, groupMethods, retryResults, failure.count, expectedExceptionHolder, testContext, parameters, parametersIndex);
                                             result.addAll(retryResults);
                                          }
                                       }

                                       if (failure.count > 0 && (this.m_skipFailedInvocationCounts || testMethod.skipFailedInvocations())) {
                                          while(true) {
                                             if (invocationCount-- <= 0) {
                                                continue label288;
                                             }

                                             result.add(this.registerSkippedTestResult(testMethod, instance, System.currentTimeMillis(), (Throwable)null));
                                          }
                                       }

                                    }
                                 }

                                 if (failure.instances.isEmpty()) {
                                    result.addAll(tmpResults);
                                 } else {
                                    i$ = failure.instances.iterator();

                                    while(i$.hasNext()) {
                                       Object failedInstance = i$.next();
                                       List<ITestResult> retryResults = Lists.newArrayList();
                                       failure.count = this.retryFailed(failedInstance, testMethod, suite, testClass, beforeMethods, afterMethods, groupMethods, retryResults, failure.count, expectedExceptionHolder, testContext, parameters, parametersIndex);
                                       result.addAll(retryResults);
                                    }
                                 }

                                 if (failure.count > 0 && (this.m_skipFailedInvocationCounts || testMethod.skipFailedInvocations())) {
                                    while(invocationCount-- > 0) {
                                       result.add(this.registerSkippedTestResult(testMethod, instance, System.currentTimeMillis(), (Throwable)null));
                                    }
                                    break;
                                 }

                                 ++parametersIndex;
                              }
                           }
                        } catch (Throwable var39) {
                           ITestResult r = new TestResult(testMethod.getTestClass(), instance, testMethod, var39, start, System.currentTimeMillis(), this.m_testContext);
                           r.setStatus(2);
                           result.add(r);
                           this.runTestListeners(r);
                           this.m_notifier.addFailedTest(testMethod, r);
                        }
                     }
                  }

                  return result;
               }
            }
         }
      }
   }

   private ITestResult registerSkippedTestResult(ITestNGMethod testMethod, Object instance, long start, Throwable throwable) {
      ITestResult result = new TestResult(testMethod.getTestClass(), instance, testMethod, throwable, start, System.currentTimeMillis(), this.m_testContext);
      result.setStatus(3);
      this.runTestListeners(result);
      return result;
   }

   private Object[] injectParameters(Object[] parameterValues, Method method, ITestContext context, ITestResult testResult) throws TestNGException {
      List<Object> vResult = Lists.newArrayList();
      int i = 0;
      int numValues = parameterValues.length;
      int numParams = method.getParameterTypes().length;
      if (numValues > numParams && !method.isVarArgs()) {
         throw new TestNGException("The data provider is trying to pass " + numValues + " parameters but the method " + method.getDeclaringClass().getName() + "#" + method.getName() + " takes " + numParams);
      } else {
         Class[] arr$ = method.getParameterTypes();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Class<?> cls = arr$[i$];
            Annotation[] annotations = method.getParameterAnnotations()[i];
            boolean noInjection = false;
            Annotation[] arr$ = annotations;
            int len$ = annotations.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               Annotation a = arr$[i$];
               if (a instanceof NoInjection) {
                  noInjection = true;
                  break;
               }
            }

            Object injected = Parameters.getInjectedParameter(cls, method, context, testResult);
            if (injected != null && !noInjection) {
               vResult.add(injected);
            } else {
               try {
                  if (method.isVarArgs()) {
                     vResult.add(parameterValues);
                  } else {
                     vResult.add(parameterValues[i++]);
                  }
               } catch (ArrayIndexOutOfBoundsException var19) {
                  throw new TestNGException("The data provider is trying to pass " + numValues + " parameters but the method " + method.getDeclaringClass().getName() + "#" + method.getName() + " takes " + numParams + " and TestNG is unable in inject a suitable object", var19);
               }
            }
         }

         return vResult.toArray(new Object[vResult.size()]);
      }
   }

   private Invoker.ParameterBag handleParameters(ITestNGMethod testMethod, Object instance, Map<String, String> allParameterNames, Map<String, String> parameters, Object[] parameterValues, XmlSuite suite, ITestContext testContext, Object fedInstance, ITestResult testResult) {
      try {
         return new Invoker.ParameterBag(Parameters.handleParameters(testMethod, allParameterNames, instance, new Parameters.MethodParameters(parameters, testMethod.findMethodParameters(testContext.getCurrentXmlTest()), parameterValues, testMethod.getMethod(), testContext, testResult), suite, this.m_annotationFinder, fedInstance));
      } catch (Throwable var11) {
         return new Invoker.ParameterBag(new TestResult(testMethod.getTestClass(), instance, testMethod, var11, System.currentTimeMillis(), System.currentTimeMillis(), this.m_testContext));
      }
   }

   private List<ITestResult> invokePooledTestMethods(ITestNGMethod testMethod, XmlSuite suite, Map<String, String> parameters, ConfigurationGroupMethods groupMethods, ITestContext testContext) {
      List<IWorker<ITestNGMethod>> workers = Lists.newArrayList();

      for(int i = 0; i < testMethod.getInvocationCount(); ++i) {
         ITestNGMethod clonedMethod = testMethod.clone();
         clonedMethod.setInvocationCount(1);
         clonedMethod.setThreadPoolSize(1);
         MethodInstance mi = new MethodInstance(clonedMethod);
         workers.add(new SingleTestMethodWorker(this, mi, suite, parameters, testContext));
      }

      return this.runWorkers(testMethod, workers, testMethod.getThreadPoolSize(), groupMethods, suite, parameters);
   }

   void handleInvocationResults(ITestNGMethod testMethod, List<ITestResult> result, ExpectedExceptionsHolder expectedExceptionsHolder, boolean triggerListeners, boolean collectResults, Invoker.FailureContext failure) {
      List<ITestResult> resultsToRetry = Lists.newArrayList();
      Iterator i$ = result.iterator();

      while(i$.hasNext()) {
         ITestResult testResult = (ITestResult)i$.next();
         Throwable ite = testResult.getThrowable();
         int status = testResult.getStatus();
         boolean handled = false;
         if (ite != null) {
            if (this.isSkipExceptionAndSkip(ite)) {
               status = 3;
            } else if (expectedExceptionsHolder != null) {
               if (expectedExceptionsHolder.isExpectedException(ite)) {
                  testResult.setStatus(1);
                  status = 1;
               } else {
                  testResult.setThrowable(expectedExceptionsHolder.wrongException(ite));
                  status = 2;
               }
            } else {
               this.handleException(ite, testMethod, testResult, failure.count++);
               handled = true;
               status = testResult.getStatus();
            }
         } else if (status != 3 && expectedExceptionsHolder != null) {
            TestException exception = expectedExceptionsHolder.noException(testMethod);
            if (exception != null) {
               testResult.setThrowable(exception);
               status = 2;
            }
         }

         IRetryAnalyzer retryAnalyzer = testMethod.getRetryAnalyzer();
         boolean willRetry = retryAnalyzer != null && status == 2 && failure.instances != null && retryAnalyzer.retry(testResult);
         if (willRetry) {
            resultsToRetry.add(testResult);
            failure.instances.add(testResult.getInstance());
            testResult.setStatus(3);
         } else {
            testResult.setStatus(status);
            if (status == 2 && !handled) {
               this.handleException(ite, testMethod, testResult, failure.count++);
            }
         }

         if (collectResults) {
            this.collectResults(testMethod, Collections.singleton(testResult));
         }
      }

      this.removeResultsToRetryFromResult(resultsToRetry, result, failure);
   }

   private boolean isSkipExceptionAndSkip(Throwable ite) {
      return SkipException.class.isAssignableFrom(ite.getClass()) && ((SkipException)ite).isSkip();
   }

   private void removeResultsToRetryFromResult(List<ITestResult> resultsToRetry, List<ITestResult> result, Invoker.FailureContext failure) {
      if (resultsToRetry != null) {
         for(Iterator i$ = resultsToRetry.iterator(); i$.hasNext(); --failure.count) {
            ITestResult res = (ITestResult)i$.next();
            result.remove(res);
         }
      }

   }

   private List<ITestResult> runWorkers(ITestNGMethod testMethod, List<IWorker<ITestNGMethod>> workers, int threadPoolSize, ConfigurationGroupMethods groupMethods, XmlSuite suite, Map<String, String> parameters) {
      ITestClass testClass = testMethod.getTestClass();
      Object[] instances = testClass.getInstances(true);
      Object[] arr$ = instances;
      int len$ = instances.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Object instance = arr$[i$];
         this.invokeBeforeGroupsConfigurations(testClass, testMethod, groupMethods, suite, parameters, instance);
      }

      long maxTimeOut = -1L;
      Iterator i$ = workers.iterator();

      while(i$.hasNext()) {
         IWorker<ITestNGMethod> tmw = (IWorker)i$.next();
         long mt = tmw.getTimeOut();
         if (mt > maxTimeOut) {
            maxTimeOut = mt;
         }
      }

      ThreadUtil.execute(workers, threadPoolSize, maxTimeOut, true);
      List<ITestResult> result = Lists.newArrayList();
      Iterator i$ = workers.iterator();

      while(i$.hasNext()) {
         IWorker<ITestNGMethod> tmw = (IWorker)i$.next();
         if (tmw instanceof TestMethodWorker) {
            result.addAll(((TestMethodWorker)tmw).getTestResults());
         }
      }

      Object[] arr$ = instances;
      int len$ = instances.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Object instance = arr$[i$];
         this.invokeAfterGroupsConfigurations(testClass, testMethod, groupMethods, suite, parameters, instance);
      }

      return result;
   }

   private String checkDependencies(ITestNGMethod testMethod, ITestNGMethod[] allTestMethods) {
      if (testMethod.isAlwaysRun()) {
         return null;
      } else if (testMethod.getMissingGroup() != null && !testMethod.ignoreMissingDependencies()) {
         return "Method " + testMethod + " depends on nonexistent group \"" + testMethod.getMissingGroup() + "\"";
      } else {
         String[] groups = testMethod.getGroupsDependedUpon();
         if (null != groups && groups.length > 0) {
            String[] arr$ = groups;
            int len$ = groups.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               String element = arr$[i$];
               ITestNGMethod[] methods = MethodGroupsHelper.findMethodsThatBelongToGroup(testMethod, this.m_testContext.getAllTestMethods(), element);
               if (methods.length == 0 && !testMethod.ignoreMissingDependencies()) {
                  return "Method " + testMethod + " depends on nonexistent group \"" + element + "\"";
               }

               if (!this.haveBeenRunSuccessfully(testMethod, methods)) {
                  return "Method " + testMethod + " depends on not successfully finished methods in group \"" + element + "\"";
               }
            }
         }

         if (this.dependsOnMethods(testMethod)) {
            ITestNGMethod[] methods = MethodHelper.findDependedUponMethods(testMethod, allTestMethods);
            if (!this.haveBeenRunSuccessfully(testMethod, methods)) {
               return "Method " + testMethod + " depends on not successfully finished methods";
            }
         }

         return null;
      }
   }

   private Set<ITestResult> keepSameInstances(ITestNGMethod method, Set<ITestResult> results) {
      Set<ITestResult> result = Sets.newHashSet();
      Iterator i$ = results.iterator();

      while(true) {
         ITestResult r;
         Object o;
         Object instance;
         do {
            if (!i$.hasNext()) {
               return result;
            }

            r = (ITestResult)i$.next();
            o = method.getInstance();
            instance = r.getInstance() != null ? r.getInstance() : r.getMethod().getInstance();
         } while(r.getTestClass() == method.getTestClass() && instance != o);

         result.add(r);
      }
   }

   private boolean haveBeenRunSuccessfully(ITestNGMethod testMethod, ITestNGMethod[] methods) {
      ITestNGMethod[] arr$ = methods;
      int len$ = methods.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ITestNGMethod method = arr$[i$];
         Set<ITestResult> results = this.keepSameInstances(testMethod, this.m_notifier.getPassedTests(method));
         Set<ITestResult> failedAndSkippedMethods = Sets.newHashSet();
         failedAndSkippedMethods.addAll(this.m_notifier.getFailedTests(method));
         failedAndSkippedMethods.addAll(this.m_notifier.getSkippedTests(method));
         Set<ITestResult> failedresults = this.keepSameInstances(testMethod, failedAndSkippedMethods);
         if (failedresults != null && failedresults.size() > 0) {
            return false;
         }

         Iterator i$ = results.iterator();

         while(i$.hasNext()) {
            ITestResult result = (ITestResult)i$.next();
            if (!result.isSuccess()) {
               return false;
            }
         }
      }

      return true;
   }

   private void handleException(Throwable throwable, ITestNGMethod testMethod, ITestResult testResult, int failureCount) {
      if (throwable != null) {
         testResult.setThrowable(throwable);
      }

      int successPercentage = testMethod.getSuccessPercentage();
      int invocationCount = testMethod.getInvocationCount();
      float numberOfTestsThatCanFail = (float)((100 - successPercentage) * invocationCount) / 100.0F;
      if ((float)failureCount < numberOfTestsThatCanFail) {
         testResult.setStatus(4);
      } else {
         testResult.setStatus(2);
      }

   }

   private ITestNGMethod[] filterMethods(IClass testClass, ITestNGMethod[] methods, Invoker.Predicate<ITestNGMethod, IClass> predicate) {
      List<ITestNGMethod> vResult = Lists.newArrayList();
      ITestNGMethod[] result = methods;
      int len$ = methods.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ITestNGMethod tm = result[i$];
         if (predicate.isTrue(tm, testClass)) {
            this.log(10, "Keeping method " + tm + " for class " + testClass);
            vResult.add(tm);
         } else {
            this.log(10, "Filtering out method " + tm + " for class " + testClass);
         }
      }

      result = (ITestNGMethod[])vResult.toArray(new ITestNGMethod[vResult.size()]);
      return result;
   }

   private boolean dependsOnMethods(ITestNGMethod tm) {
      String[] methods = tm.getMethodsDependedUpon();
      return null != methods && methods.length > 0;
   }

   private void runConfigurationListeners(ITestResult tr, boolean before) {
      Iterator i$;
      IConfigurationListener icl;
      if (before) {
         i$ = this.m_notifier.getConfigurationListeners().iterator();

         while(i$.hasNext()) {
            icl = (IConfigurationListener)i$.next();
            if (icl instanceof IConfigurationListener2) {
               ((IConfigurationListener2)icl).beforeConfiguration(tr);
            }
         }
      } else {
         i$ = this.m_notifier.getConfigurationListeners().iterator();

         while(i$.hasNext()) {
            icl = (IConfigurationListener)i$.next();
            switch(tr.getStatus()) {
            case 1:
               icl.onConfigurationSuccess(tr);
               break;
            case 2:
               icl.onConfigurationFailure(tr);
               break;
            case 3:
               icl.onConfigurationSkip(tr);
            }
         }
      }

   }

   void runTestListeners(ITestResult tr) {
      runTestListeners(tr, this.m_notifier.getTestListeners());
   }

   public static void runTestListeners(ITestResult tr, List<ITestListener> listeners) {
      Iterator i$ = listeners.iterator();

      while(i$.hasNext()) {
         ITestListener itl = (ITestListener)i$.next();
         switch(tr.getStatus()) {
         case 1:
            itl.onTestSuccess(tr);
            break;
         case 2:
            itl.onTestFailure(tr);
            break;
         case 3:
            itl.onTestSkipped(tr);
            break;
         case 4:
            itl.onTestFailedButWithinSuccessPercentage(tr);
            break;
         case 16:
            itl.onTestStart(tr);
            break;
         default:
            assert false : "UNKNOWN STATUS:" + tr;
         }
      }

   }

   private void log(int level, String s) {
      Utils.log("Invoker " + Thread.currentThread().hashCode(), level, s);
   }

   private static class ParameterBag {
      final ParameterHolder parameterHolder;
      final ITestResult errorResult;

      public ParameterBag(ParameterHolder parameterHolder) {
         this.parameterHolder = parameterHolder;
         this.errorResult = null;
      }

      public ParameterBag(ITestResult errorResult) {
         this.parameterHolder = null;
         this.errorResult = errorResult;
      }

      public boolean hasErrors() {
         return this.errorResult != null;
      }
   }

   static class SameClassNamePredicate implements Invoker.Predicate<ITestNGMethod, IClass> {
      public boolean isTrue(ITestNGMethod m, IClass c) {
         return c == null || m.getTestClass().getName().equals(c.getName());
      }
   }

   static class CanRunFromClassPredicate implements Invoker.Predicate<ITestNGMethod, IClass> {
      public boolean isTrue(ITestNGMethod m, IClass v) {
         return m.canRunFromClass(v);
      }
   }

   interface Predicate<K, T> {
      boolean isTrue(K var1, T var2);
   }

   static class FailureContext {
      int count = 0;
      List<Object> instances = Lists.newArrayList();
   }
}
