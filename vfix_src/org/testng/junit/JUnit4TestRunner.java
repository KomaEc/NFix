package org.testng.junit;

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Pattern;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestNGException;
import org.testng.collections.Lists;
import org.testng.internal.ITestResultNotifier;
import org.testng.internal.InvokedMethod;
import org.testng.internal.TestResult;

public class JUnit4TestRunner implements IJUnitTestRunner {
   private ITestResultNotifier m_parentRunner;
   private List<ITestNGMethod> m_methods = Lists.newArrayList();
   private List<ITestListener> m_listeners = Lists.newArrayList();
   private List<IInvokedMethodListener> m_invokeListeners = Lists.newArrayList();

   public JUnit4TestRunner() {
   }

   public JUnit4TestRunner(ITestResultNotifier tr) {
      this.m_parentRunner = tr;
      this.m_listeners = this.m_parentRunner.getTestListeners();
   }

   public List<ITestNGMethod> getTestMethods() {
      return this.m_methods;
   }

   public void setTestResultNotifier(ITestResultNotifier notifier) {
      this.m_parentRunner = notifier;
      this.m_listeners = this.m_parentRunner.getTestListeners();
   }

   public void setInvokedMethodListeners(List<IInvokedMethodListener> listeners) {
      this.m_invokeListeners = listeners;
   }

   public void run(Class testClass, String... methods) {
      this.start(testClass, methods);
   }

   public Result start(Class testCase, final String... methods) {
      try {
         JUnitCore core = new JUnitCore();
         core.addListener(new JUnit4TestRunner.RL());
         Request r = Request.aClass(testCase);
         return core.run(r.filterWith(new Filter() {
            public boolean shouldRun(Description description) {
               if (description == null) {
                  return false;
               } else if (methods.length == 0) {
                  return true;
               } else {
                  String[] arr$ = methods;
                  int len$ = arr$.length;

                  for(int i$ = 0; i$ < len$; ++i$) {
                     String m = arr$[i$];
                     Pattern p = Pattern.compile(m);
                     if (p.matcher(description.getMethodName()).matches()) {
                        return true;
                     }
                  }

                  return false;
               }
            }

            public String describe() {
               return "TestNG method filter";
            }
         }));
      } catch (Throwable var5) {
         throw new TestNGException("Failure in JUnit mode for class " + testCase.getName(), var5);
      }
   }

   private static boolean isAssumptionFailed(Failure failure) {
      if (failure == null) {
         return false;
      } else {
         Throwable exception = failure.getException();
         return exception == null ? false : "org.junit.internal.AssumptionViolatedException".equals(exception.getClass().getCanonicalName());
      }
   }

   private class RL extends RunListener {
      private Map<Description, ITestResult> runs;
      private List<Description> notified;

      private RL() {
         this.runs = new WeakHashMap();
         this.notified = new LinkedList();
      }

      public void testAssumptionFailure(Failure failure) {
         this.notified.add(failure.getDescription());
         ITestResult tr = (ITestResult)this.runs.get(failure.getDescription());
         tr.setStatus(3);
         tr.setEndMillis(Calendar.getInstance().getTimeInMillis());
         tr.setThrowable(failure.getException());
         JUnit4TestRunner.this.m_parentRunner.addSkippedTest(tr.getMethod(), tr);
         Iterator i$ = JUnit4TestRunner.this.m_listeners.iterator();

         while(i$.hasNext()) {
            ITestListener l = (ITestListener)i$.next();
            l.onTestSkipped(tr);
         }

      }

      public void testFailure(Failure failure) throws Exception {
         if (JUnit4TestRunner.isAssumptionFailed(failure)) {
            this.testAssumptionFailure(failure);
         } else {
            this.notified.add(failure.getDescription());
            ITestResult tr = (ITestResult)this.runs.get(failure.getDescription());
            tr.setStatus(2);
            tr.setEndMillis(Calendar.getInstance().getTimeInMillis());
            tr.setThrowable(failure.getException());
            JUnit4TestRunner.this.m_parentRunner.addFailedTest(tr.getMethod(), tr);
            Iterator i$ = JUnit4TestRunner.this.m_listeners.iterator();

            while(i$.hasNext()) {
               ITestListener l = (ITestListener)i$.next();
               l.onTestFailure(tr);
            }

         }
      }

      public void testFinished(Description description) throws Exception {
         ITestResult tr = (ITestResult)this.runs.get(description);
         if (!this.notified.contains(description)) {
            tr.setStatus(1);
            tr.setEndMillis(Calendar.getInstance().getTimeInMillis());
            JUnit4TestRunner.this.m_parentRunner.addPassedTest(tr.getMethod(), tr);
            Iterator i$ = JUnit4TestRunner.this.m_listeners.iterator();

            while(i$.hasNext()) {
               ITestListener l = (ITestListener)i$.next();
               l.onTestSuccess(tr);
            }
         }

         JUnit4TestRunner.this.m_methods.add(tr.getMethod());
      }

      public void testIgnored(Description description) throws Exception {
         ITestResult tr = this.createTestResult(description);
         tr.setStatus(3);
         tr.setEndMillis(tr.getStartMillis());
         JUnit4TestRunner.this.m_parentRunner.addSkippedTest(tr.getMethod(), tr);
         JUnit4TestRunner.this.m_methods.add(tr.getMethod());
         Iterator i$ = JUnit4TestRunner.this.m_listeners.iterator();

         while(i$.hasNext()) {
            ITestListener l = (ITestListener)i$.next();
            l.onTestSkipped(tr);
         }

      }

      public void testRunFinished(Result result) throws Exception {
      }

      public void testRunStarted(Description description) throws Exception {
      }

      public void testStarted(Description description) throws Exception {
         ITestResult tr = this.createTestResult(description);
         this.runs.put(description, tr);
         Iterator i$ = JUnit4TestRunner.this.m_listeners.iterator();

         while(i$.hasNext()) {
            ITestListener l = (ITestListener)i$.next();
            l.onTestStart(tr);
         }

      }

      private ITestResult createTestResult(Description test) {
         JUnit4TestClass tc = new JUnit4TestClass(test);
         JUnitTestMethod tm = new JUnit4TestMethod(tc, test);
         TestResult tr = new TestResult(tc, test, tm, (Throwable)null, Calendar.getInstance().getTimeInMillis(), 0L, (ITestContext)null);
         InvokedMethod im = new InvokedMethod(tr.getTestClass(), tr.getMethod(), new Object[0], tr.getStartMillis(), tr);
         JUnit4TestRunner.this.m_parentRunner.addInvokedMethod(im);
         Iterator i$ = JUnit4TestRunner.this.m_invokeListeners.iterator();

         while(i$.hasNext()) {
            IInvokedMethodListener l = (IInvokedMethodListener)i$.next();
            l.beforeInvocation(im, tr);
         }

         return tr;
      }

      // $FF: synthetic method
      RL(Object x1) {
         this();
      }
   }
}
