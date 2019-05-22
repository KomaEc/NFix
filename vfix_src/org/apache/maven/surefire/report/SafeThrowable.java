package org.apache.maven.surefire.report;

public class SafeThrowable {
   private final Throwable target;

   public SafeThrowable(Throwable target) {
      this.target = target;
   }

   public String getLocalizedMessage() {
      try {
         return this.target.getLocalizedMessage();
      } catch (Throwable var2) {
         return var2.getLocalizedMessage();
      }
   }

   public String getMessage() {
      try {
         return this.target.getMessage();
      } catch (Throwable var2) {
         return var2.getMessage();
      }
   }

   public Throwable getTarget() {
      return this.target;
   }
}
