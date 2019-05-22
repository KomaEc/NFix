package org.junit.internal.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

public class ThrowableMessageMatcher<T extends Throwable> extends org.hamcrest.TypeSafeMatcher<T> {
   private final Matcher<String> matcher;

   public ThrowableMessageMatcher(Matcher<String> matcher) {
      this.matcher = matcher;
   }

   public void describeTo(Description description) {
      description.appendText("exception with message ");
      description.appendDescriptionOf(this.matcher);
   }

   protected boolean matchesSafely(T item) {
      return this.matcher.matches(item.getMessage());
   }

   protected void describeMismatchSafely(T item, Description description) {
      description.appendText("message ");
      this.matcher.describeMismatch(item.getMessage(), description);
   }

   @Factory
   public static <T extends Throwable> Matcher<T> hasMessage(Matcher<String> matcher) {
      return new ThrowableMessageMatcher(matcher);
   }
}
