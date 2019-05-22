package org.junit.rules;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Assert;
import org.junit.internal.matchers.ThrowableCauseMatcher;
import org.junit.internal.matchers.ThrowableMessageMatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class ExpectedException implements TestRule {
   private final ExpectedExceptionMatcherBuilder matcherBuilder = new ExpectedExceptionMatcherBuilder();
   private String missingExceptionMessage = "Expected test to throw %s";

   public static ExpectedException none() {
      return new ExpectedException();
   }

   private ExpectedException() {
   }

   /** @deprecated */
   @Deprecated
   public ExpectedException handleAssertionErrors() {
      return this;
   }

   /** @deprecated */
   @Deprecated
   public ExpectedException handleAssumptionViolatedExceptions() {
      return this;
   }

   public ExpectedException reportMissingExceptionWithMessage(String message) {
      this.missingExceptionMessage = message;
      return this;
   }

   public Statement apply(Statement base, Description description) {
      return new ExpectedException.ExpectedExceptionStatement(base);
   }

   public void expect(Matcher<?> matcher) {
      this.matcherBuilder.add(matcher);
   }

   public void expect(Class<? extends Throwable> type) {
      this.expect(CoreMatchers.instanceOf(type));
   }

   public void expectMessage(String substring) {
      this.expectMessage(CoreMatchers.containsString(substring));
   }

   public void expectMessage(Matcher<String> matcher) {
      this.expect(ThrowableMessageMatcher.hasMessage(matcher));
   }

   public void expectCause(Matcher<? extends Throwable> expectedCause) {
      this.expect(ThrowableCauseMatcher.hasCause(expectedCause));
   }

   private void handleException(Throwable e) throws Throwable {
      if (this.isAnyExceptionExpected()) {
         Assert.assertThat(e, this.matcherBuilder.build());
      } else {
         throw e;
      }
   }

   private boolean isAnyExceptionExpected() {
      return this.matcherBuilder.expectsThrowable();
   }

   private void failDueToMissingException() throws AssertionError {
      Assert.fail(this.missingExceptionMessage());
   }

   private String missingExceptionMessage() {
      String expectation = StringDescription.toString(this.matcherBuilder.build());
      return String.format(this.missingExceptionMessage, expectation);
   }

   private class ExpectedExceptionStatement extends Statement {
      private final Statement next;

      public ExpectedExceptionStatement(Statement base) {
         this.next = base;
      }

      public void evaluate() throws Throwable {
         try {
            this.next.evaluate();
         } catch (Throwable var2) {
            ExpectedException.this.handleException(var2);
            return;
         }

         if (ExpectedException.this.isAnyExceptionExpected()) {
            ExpectedException.this.failDueToMissingException();
         }

      }
   }
}
