package org.testng.junit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestListener;
import junit.framework.TestSuite;
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

public class JUnitTestRunner implements TestListener, IJUnitTestRunner {
   public static final String SUITE_METHODNAME = "suite";
   private ITestResultNotifier m_parentRunner;
   private Map<Test, JUnitTestRunner.TestRunInfo> m_tests = new WeakHashMap();
   private List<ITestNGMethod> m_methods = Lists.newArrayList();
   private List<IInvokedMethodListener> m_invokedMethodListeners = Lists.newArrayList();

   public JUnitTestRunner() {
   }

   public JUnitTestRunner(ITestResultNotifier tr) {
      this.m_parentRunner = tr;
   }

   public List<ITestNGMethod> getTestMethods() {
      return this.m_methods;
   }

   public void setTestResultNotifier(ITestResultNotifier notifier) {
      this.m_parentRunner = notifier;
   }

   public void startTest(Test test) {
      this.m_tests.put(test, new JUnitTestRunner.TestRunInfo(Calendar.getInstance().getTimeInMillis()));
   }

   public void addError(Test test, Throwable t) {
      this.recordFailure(test, t);
   }

   public void addFailure(Test test, AssertionFailedError t) {
      this.recordFailure(test, t);
   }

   private void recordFailure(Test test, Throwable t) {
      JUnitTestRunner.TestRunInfo tri = (JUnitTestRunner.TestRunInfo)this.m_tests.get(test);
      if (null != tri) {
         tri.setThrowable(t);
      }

   }

   public void endTest(Test test) {
      JUnitTestRunner.TestRunInfo tri = (JUnitTestRunner.TestRunInfo)this.m_tests.get(test);
      if (null != tri) {
         TestResult tr = this.recordResults(test, tri);
         runTestListeners(tr, this.m_parentRunner.getTestListeners());
      }
   }

   public void setInvokedMethodListeners(List<IInvokedMethodListener> listeners) {
      this.m_invokedMethodListeners = listeners;
   }

   private TestResult recordResults(Test test, JUnitTestRunner.TestRunInfo tri) {
      JUnitTestClass tc = new JUnit3TestClass(test);
      JUnitTestMethod tm = new JUnit3TestMethod(tc, test);
      TestResult tr = new TestResult(tc, test, tm, tri.m_failure, tri.m_start, Calendar.getInstance().getTimeInMillis(), (ITestContext)null);
      if (tri.isFailure()) {
         tr.setStatus(2);
         this.m_parentRunner.addFailedTest(tm, tr);
      } else {
         this.m_parentRunner.addPassedTest(tm, tr);
      }

      InvokedMethod im = new InvokedMethod(test, tm, new Object[0], tri.m_start, tr);
      this.m_parentRunner.addInvokedMethod(im);
      this.m_methods.add(tm);
      Iterator i$ = this.m_invokedMethodListeners.iterator();

      while(i$.hasNext()) {
         IInvokedMethodListener l = (IInvokedMethodListener)i$.next();
         l.beforeInvocation(im, tr);
      }

      return tr;
   }

   private static void runTestListeners(ITestResult tr, List<ITestListener> listeners) {
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

   protected Test getTest(Class testClass, String... methods) {
      if (methods.length > 0) {
         TestSuite ts = new TestSuite();

         try {
            Constructor c = testClass.getConstructor(String.class);
            String[] arr$ = methods;
            int len$ = methods.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               String m = arr$[i$];

               try {
                  ts.addTest((Test)c.newInstance(m));
               } catch (InstantiationException var10) {
                  this.runFailed(testClass, "abstract class " + var10);
               } catch (IllegalAccessException var11) {
                  this.runFailed(testClass, "constructor is not public " + var11);
               } catch (IllegalArgumentException var12) {
                  this.runFailed(testClass, "actual and formal parameters differ " + var12);
               } catch (InvocationTargetException var13) {
                  this.runFailed(testClass, "exception while instatiating test for method '" + m + "' " + var13);
               }
            }
         } catch (NoSuchMethodException var17) {
            this.runFailed(testClass, "no constructor accepting String argument found " + var17);
         } catch (SecurityException var18) {
            this.runFailed(testClass, "security exception " + var18);
         }

         return ts;
      } else {
         Method suiteMethod = null;

         try {
            suiteMethod = testClass.getMethod("suite");
         } catch (Exception var16) {
            return new TestSuite(testClass);
         }

         if (!Modifier.isStatic(suiteMethod.getModifiers())) {
            this.runFailed(testClass, "suite() method must be static");
            return null;
         } else {
            Test test = null;

            try {
               test = (Test)suiteMethod.invoke((Object)null, (Object[])(new Class[0]));
               return test == null ? test : test;
            } catch (InvocationTargetException var14) {
               this.runFailed(testClass, "failed to invoke method suite():" + var14.getTargetException().toString());
               return null;
            } catch (IllegalAccessException var15) {
               this.runFailed(testClass, "failed to invoke method suite():" + var15.toString());
               return null;
            }
         }
      }
   }

   public void run(Class testClass, String... methods) {
      this.start(testClass, methods);
   }

   public junit.framework.TestResult start(Class testCase, String... methods) {
      try {
         Test suite = this.getTest(testCase, methods);
         if (null != suite) {
            return this.doRun(suite);
         }

         this.runFailed(testCase, "could not create/run JUnit test suite");
      } catch (Exception var4) {
         this.runFailed(testCase, "could not create/run JUnit test suite: " + var4.getMessage());
      }

      return null;
   }

   protected void runFailed(Class clazz, String message) {
      throw new TestNGException("Failure in JUnit mode for class " + clazz.getName() + ": " + message);
   }

   protected junit.framework.TestResult createTestResult() {
      return new junit.framework.TestResult();
   }

   protected junit.framework.TestResult doRun(Test suite) {
      junit.framework.TestResult result = this.createTestResult();
      result.addListener(this);
      suite.run(result);
      return result;
   }

   private static class TestRunInfo {
      private final long m_start;
      private Throwable m_failure;

      public TestRunInfo(long start) {
         this.m_start = start;
      }

      public boolean isFailure() {
         return null != this.m_failure;
      }

      public void setThrowable(Throwable t) {
         this.m_failure = t;
      }
   }
}
