package polyglot.util;

public final class Assert {
   public static void check(boolean ok) {
      if (!ok) {
         throw new Assert.AssertionFailedError("Assertion failed");
      }
   }

   public static void check(String condition, boolean ok) {
      if (!ok) {
         throw new Assert.AssertionFailedError("Assertion \"" + condition + "\" failed.");
      }
   }

   private Assert() {
   }

   private static class AssertionFailedError extends Error {
      public AssertionFailedError() {
      }

      public AssertionFailedError(String s) {
         super(s);
      }
   }
}
