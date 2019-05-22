package org.testng.internal;

import java.util.Arrays;
import org.testng.IExpectedExceptionsHolder;
import org.testng.ITestNGMethod;
import org.testng.TestException;
import org.testng.annotations.IExpectedExceptionsAnnotation;
import org.testng.annotations.ITestAnnotation;
import org.testng.internal.annotations.IAnnotationFinder;

public class ExpectedExceptionsHolder {
   protected final IAnnotationFinder finder;
   protected final ITestNGMethod method;
   private final Class<?>[] expectedClasses;
   private final IExpectedExceptionsHolder holder;

   protected ExpectedExceptionsHolder(IAnnotationFinder finder, ITestNGMethod method, IExpectedExceptionsHolder holder) {
      this.finder = finder;
      this.method = method;
      this.expectedClasses = findExpectedClasses(finder, method);
      this.holder = holder;
   }

   private static Class<?>[] findExpectedClasses(IAnnotationFinder finder, ITestNGMethod method) {
      IExpectedExceptionsAnnotation expectedExceptions = (IExpectedExceptionsAnnotation)finder.findAnnotation(method, IExpectedExceptionsAnnotation.class);
      if (expectedExceptions != null) {
         return expectedExceptions.getValue();
      } else {
         ITestAnnotation testAnnotation = (ITestAnnotation)finder.findAnnotation(method, ITestAnnotation.class);
         return testAnnotation != null ? testAnnotation.getExpectedExceptions() : new Class[0];
      }
   }

   public boolean isExpectedException(Throwable ite) {
      if (!this.hasExpectedClasses()) {
         return false;
      } else if (ite.getClass() == TestException.class) {
         return false;
      } else {
         Class<?> realExceptionClass = ite.getClass();
         Class[] arr$ = this.expectedClasses;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Class<?> exception = arr$[i$];
            if (exception.isAssignableFrom(realExceptionClass) && this.holder.isThrowableMatching(ite)) {
               return true;
            }
         }

         return false;
      }
   }

   public Throwable wrongException(Throwable ite) {
      if (!this.hasExpectedClasses()) {
         return ite;
      } else {
         return this.holder.isThrowableMatching(ite) ? new TestException("Expected exception of " + this.getExpectedExceptionsPluralize() + " but got " + ite, ite) : new TestException(this.holder.getWrongExceptionMessage(ite), ite);
      }
   }

   public TestException noException(ITestNGMethod testMethod) {
      return !this.hasExpectedClasses() ? null : new TestException("Method " + testMethod + " should have thrown an exception of " + this.getExpectedExceptionsPluralize());
   }

   private boolean hasExpectedClasses() {
      return this.expectedClasses != null && this.expectedClasses.length > 0;
   }

   private String getExpectedExceptionsPluralize() {
      StringBuilder sb = new StringBuilder();
      if (this.expectedClasses.length > 1) {
         sb.append("any of types ");
         sb.append(Arrays.toString(this.expectedClasses));
      } else {
         sb.append("type ");
         sb.append(this.expectedClasses[0]);
      }

      return sb.toString();
   }
}
