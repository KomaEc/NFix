package org.junit.internal;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;

public class AssumptionViolatedException extends RuntimeException implements SelfDescribing {
   private static final long serialVersionUID = 2L;
   private final String fAssumption;
   private final boolean fValueMatcher;
   private final Object fValue;
   private final Matcher<?> fMatcher;

   /** @deprecated */
   @Deprecated
   public AssumptionViolatedException(String assumption, boolean hasValue, Object value, Matcher<?> matcher) {
      this.fAssumption = assumption;
      this.fValue = value;
      this.fMatcher = matcher;
      this.fValueMatcher = hasValue;
      if (value instanceof Throwable) {
         this.initCause((Throwable)value);
      }

   }

   /** @deprecated */
   @Deprecated
   public AssumptionViolatedException(Object value, Matcher<?> matcher) {
      this((String)null, true, value, matcher);
   }

   /** @deprecated */
   @Deprecated
   public AssumptionViolatedException(String assumption, Object value, Matcher<?> matcher) {
      this(assumption, true, value, matcher);
   }

   /** @deprecated */
   @Deprecated
   public AssumptionViolatedException(String assumption) {
      this(assumption, false, (Object)null, (Matcher)null);
   }

   /** @deprecated */
   @Deprecated
   public AssumptionViolatedException(String assumption, Throwable e) {
      this(assumption, false, (Object)null, (Matcher)null);
      this.initCause(e);
   }

   public String getMessage() {
      return StringDescription.asString(this);
   }

   public void describeTo(Description description) {
      if (this.fAssumption != null) {
         description.appendText(this.fAssumption);
      }

      if (this.fValueMatcher) {
         if (this.fAssumption != null) {
            description.appendText(": ");
         }

         description.appendText("got: ");
         description.appendValue(this.fValue);
         if (this.fMatcher != null) {
            description.appendText(", expected: ");
            description.appendDescriptionOf(this.fMatcher);
         }
      }

   }
}
