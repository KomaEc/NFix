package org.testng.internal;

import java.util.regex.Pattern;
import org.testng.IExpectedExceptionsHolder;
import org.testng.ITestNGMethod;
import org.testng.annotations.IExpectedExceptionsAnnotation;
import org.testng.annotations.ITestAnnotation;
import org.testng.internal.annotations.IAnnotationFinder;

public class RegexpExpectedExceptionsHolder implements IExpectedExceptionsHolder {
   public static final String DEFAULT_REGEXP = ".*";
   private final IAnnotationFinder finder;
   private final ITestNGMethod method;

   public RegexpExpectedExceptionsHolder(IAnnotationFinder finder, ITestNGMethod method) {
      this.finder = finder;
      this.method = method;
   }

   public boolean isThrowableMatching(Throwable ite) {
      String messageRegExp = this.getRegExp();
      if (".*".equals(messageRegExp)) {
         return true;
      } else {
         String message = ite.getMessage();
         return message != null && Pattern.compile(messageRegExp, 32).matcher(message).matches();
      }
   }

   public String getWrongExceptionMessage(Throwable ite) {
      return "The exception was thrown with the wrong message: expected \"" + this.getRegExp() + "\"" + " but got \"" + ite.getMessage() + "\"";
   }

   private String getRegExp() {
      IExpectedExceptionsAnnotation expectedExceptions = (IExpectedExceptionsAnnotation)this.finder.findAnnotation(this.method, IExpectedExceptionsAnnotation.class);
      if (expectedExceptions != null) {
         return ".*";
      } else {
         ITestAnnotation testAnnotation = (ITestAnnotation)this.finder.findAnnotation(this.method, ITestAnnotation.class);
         return testAnnotation != null ? testAnnotation.getExpectedExceptionsMessageRegExp() : ".*";
      }
   }
}
