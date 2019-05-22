package org.junit.internal.runners;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import junit.extensions.TestDecorator;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestListener;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import org.junit.runner.Describable;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

public class JUnit38ClassRunner extends Runner implements Filterable, Sortable {
   private volatile Test test;

   public JUnit38ClassRunner(Class<?> klass) {
      this((Test)(new TestSuite(klass.asSubclass(TestCase.class))));
   }

   public JUnit38ClassRunner(Test test) {
      this.setTest(test);
   }

   public void run(RunNotifier notifier) {
      TestResult result = new TestResult();
      result.addListener(this.createAdaptingListener(notifier));
      this.getTest().run(result);
   }

   public TestListener createAdaptingListener(RunNotifier notifier) {
      return new JUnit38ClassRunner.OldTestClassAdaptingListener(notifier);
   }

   public Description getDescription() {
      return makeDescription(this.getTest());
   }

   private static Description makeDescription(Test test) {
      if (test instanceof TestCase) {
         TestCase tc = (TestCase)test;
         return Description.createTestDescription(tc.getClass(), tc.getName(), getAnnotations(tc));
      } else if (!(test instanceof TestSuite)) {
         if (test instanceof Describable) {
            Describable adapter = (Describable)test;
            return adapter.getDescription();
         } else if (test instanceof TestDecorator) {
            TestDecorator decorator = (TestDecorator)test;
            return makeDescription(decorator.getTest());
         } else {
            return Description.createSuiteDescription(test.getClass());
         }
      } else {
         TestSuite ts = (TestSuite)test;
         String name = ts.getName() == null ? createSuiteDescription(ts) : ts.getName();
         Description description = Description.createSuiteDescription(name);
         int n = ts.testCount();

         for(int i = 0; i < n; ++i) {
            Description made = makeDescription(ts.testAt(i));
            description.addChild(made);
         }

         return description;
      }
   }

   private static Annotation[] getAnnotations(TestCase test) {
      try {
         Method m = test.getClass().getMethod(test.getName());
         return m.getDeclaredAnnotations();
      } catch (SecurityException var2) {
      } catch (NoSuchMethodException var3) {
      }

      return new Annotation[0];
   }

   private static String createSuiteDescription(TestSuite ts) {
      int count = ts.countTestCases();
      String example = count == 0 ? "" : String.format(" [example: %s]", ts.testAt(0));
      return String.format("TestSuite with %s tests%s", count, example);
   }

   public void filter(Filter filter) throws NoTestsRemainException {
      if (this.getTest() instanceof Filterable) {
         Filterable adapter = (Filterable)this.getTest();
         adapter.filter(filter);
      } else if (this.getTest() instanceof TestSuite) {
         TestSuite suite = (TestSuite)this.getTest();
         TestSuite filtered = new TestSuite(suite.getName());
         int n = suite.testCount();

         for(int i = 0; i < n; ++i) {
            Test test = suite.testAt(i);
            if (filter.shouldRun(makeDescription(test))) {
               filtered.addTest(test);
            }
         }

         this.setTest(filtered);
         if (filtered.testCount() == 0) {
            throw new NoTestsRemainException();
         }
      }

   }

   public void sort(Sorter sorter) {
      if (this.getTest() instanceof Sortable) {
         Sortable adapter = (Sortable)this.getTest();
         adapter.sort(sorter);
      }

   }

   private void setTest(Test test) {
      this.test = test;
   }

   private Test getTest() {
      return this.test;
   }

   private static final class OldTestClassAdaptingListener implements TestListener {
      private final RunNotifier notifier;

      private OldTestClassAdaptingListener(RunNotifier notifier) {
         this.notifier = notifier;
      }

      public void endTest(Test test) {
         this.notifier.fireTestFinished(this.asDescription(test));
      }

      public void startTest(Test test) {
         this.notifier.fireTestStarted(this.asDescription(test));
      }

      public void addError(Test test, Throwable e) {
         Failure failure = new Failure(this.asDescription(test), e);
         this.notifier.fireTestFailure(failure);
      }

      private Description asDescription(Test test) {
         if (test instanceof Describable) {
            Describable facade = (Describable)test;
            return facade.getDescription();
         } else {
            return Description.createTestDescription(this.getEffectiveClass(test), this.getName(test));
         }
      }

      private Class<? extends Test> getEffectiveClass(Test test) {
         return test.getClass();
      }

      private String getName(Test test) {
         return test instanceof TestCase ? ((TestCase)test).getName() : test.toString();
      }

      public void addFailure(Test test, AssertionFailedError t) {
         this.addError(test, t);
      }

      // $FF: synthetic method
      OldTestClassAdaptingListener(RunNotifier x0, Object x1) {
         this(x0);
      }
   }
}
