package org.testng.internal;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.collections.Lists;
import org.testng.xml.XmlSuite;

public class TestMethodWithDataProviderMethodWorker implements Callable<List<ITestResult>> {
   private ITestNGMethod m_testMethod;
   private Object[] m_parameterValues;
   private Object m_instance;
   private XmlSuite m_xmlSuite;
   private Map<String, String> m_parameters;
   private ITestClass m_testClass;
   private ITestNGMethod[] m_beforeMethods;
   private ITestNGMethod[] m_afterMethods;
   private ConfigurationGroupMethods m_groupMethods;
   private Invoker m_invoker;
   private ExpectedExceptionsHolder m_expectedExceptionHolder;
   private ITestContext m_testContext;
   private int m_parameterIndex;
   private boolean m_skipFailedInvocationCounts;
   private int m_invocationCount;
   private ITestResultNotifier m_notifier;
   private List<ITestResult> m_testResults = Lists.newArrayList();
   private int m_failureCount;

   public TestMethodWithDataProviderMethodWorker(Invoker invoker, ITestNGMethod testMethod, int parameterIndex, Object[] parameterValues, Object instance, XmlSuite suite, Map<String, String> parameters, ITestClass testClass, ITestNGMethod[] beforeMethods, ITestNGMethod[] afterMethods, ConfigurationGroupMethods groupMethods, ExpectedExceptionsHolder expectedExceptionHolder, ITestContext testContext, boolean skipFailedInvocationCounts, int invocationCount, int failureCount, ITestResultNotifier notifier) {
      this.m_invoker = invoker;
      this.m_testMethod = testMethod;
      this.m_parameterIndex = parameterIndex;
      this.m_parameterValues = parameterValues;
      this.m_instance = instance;
      this.m_xmlSuite = suite;
      this.m_parameters = parameters;
      this.m_testClass = testClass;
      this.m_beforeMethods = beforeMethods;
      this.m_afterMethods = afterMethods;
      this.m_groupMethods = groupMethods;
      this.m_expectedExceptionHolder = expectedExceptionHolder;
      this.m_skipFailedInvocationCounts = skipFailedInvocationCounts;
      this.m_testContext = testContext;
      this.m_invocationCount = invocationCount;
      this.m_failureCount = failureCount;
      this.m_notifier = notifier;
   }

   public long getMaxTimeOut() {
      return 500L;
   }

   public List<ITestResult> call() {
      List<ITestResult> tmpResults = Lists.newArrayList();
      long start = System.currentTimeMillis();
      Invoker.FailureContext failure = new Invoker.FailureContext();
      failure.count = this.m_failureCount;
      boolean var13 = false;

      try {
         var13 = true;
         tmpResults.add(this.m_invoker.invokeTestMethod(this.m_instance, this.m_testMethod, this.m_parameterValues, this.m_parameterIndex, this.m_xmlSuite, this.m_parameters, this.m_testClass, this.m_beforeMethods, this.m_afterMethods, this.m_groupMethods, failure));
         var13 = false;
      } finally {
         if (var13) {
            this.m_failureCount = failure.count;
            if (failure.instances.isEmpty()) {
               this.m_testResults.addAll(tmpResults);
            } else {
               Iterator i$ = failure.instances.iterator();

               while(i$.hasNext()) {
                  Object instance = i$.next();
                  List<ITestResult> retryResults = Lists.newArrayList();
                  this.m_failureCount = this.m_invoker.retryFailed(instance, this.m_testMethod, this.m_xmlSuite, this.m_testClass, this.m_beforeMethods, this.m_afterMethods, this.m_groupMethods, retryResults, this.m_failureCount, this.m_expectedExceptionHolder, this.m_testContext, this.m_parameters, this.m_parameterIndex);
                  this.m_testResults.addAll(retryResults);
               }
            }

            if (!this.m_skipFailedInvocationCounts) {
               this.m_skipFailedInvocationCounts = this.m_testMethod.skipFailedInvocations();
            }

            if (this.m_failureCount > 0 && this.m_skipFailedInvocationCounts) {
               while(this.m_invocationCount-- > 0) {
                  ITestResult r = new TestResult(this.m_testMethod.getTestClass(), this.m_instance, this.m_testMethod, (Throwable)null, start, System.currentTimeMillis(), this.m_testContext);
                  r.setStatus(3);
                  this.m_testResults.add(r);
                  this.m_invoker.runTestListeners(r);
                  this.m_notifier.addSkippedTest(this.m_testMethod, r);
               }
            }

         }
      }

      this.m_failureCount = failure.count;
      if (failure.instances.isEmpty()) {
         this.m_testResults.addAll(tmpResults);
      } else {
         Iterator i$ = failure.instances.iterator();

         while(i$.hasNext()) {
            Object instance = i$.next();
            List<ITestResult> retryResults = Lists.newArrayList();
            this.m_failureCount = this.m_invoker.retryFailed(instance, this.m_testMethod, this.m_xmlSuite, this.m_testClass, this.m_beforeMethods, this.m_afterMethods, this.m_groupMethods, retryResults, this.m_failureCount, this.m_expectedExceptionHolder, this.m_testContext, this.m_parameters, this.m_parameterIndex);
            this.m_testResults.addAll(retryResults);
         }
      }

      if (!this.m_skipFailedInvocationCounts) {
         this.m_skipFailedInvocationCounts = this.m_testMethod.skipFailedInvocations();
      }

      if (this.m_failureCount > 0 && this.m_skipFailedInvocationCounts) {
         while(this.m_invocationCount-- > 0) {
            ITestResult r = new TestResult(this.m_testMethod.getTestClass(), this.m_instance, this.m_testMethod, (Throwable)null, start, System.currentTimeMillis(), this.m_testContext);
            r.setStatus(3);
            this.m_testResults.add(r);
            this.m_invoker.runTestListeners(r);
            this.m_notifier.addSkippedTest(this.m_testMethod, r);
         }
      }

      ++this.m_parameterIndex;
      return this.m_testResults;
   }

   public List<ITestResult> getTestResults() {
      return this.m_testResults;
   }

   public int getInvocationCount() {
      return this.m_invocationCount;
   }

   public int getFailureCount() {
      return this.m_failureCount;
   }
}
