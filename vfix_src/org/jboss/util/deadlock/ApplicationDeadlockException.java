package org.jboss.util.deadlock;

public class ApplicationDeadlockException extends RuntimeException {
   private static final long serialVersionUID = -908428091244600395L;
   protected boolean retry = false;

   public ApplicationDeadlockException() {
   }

   public ApplicationDeadlockException(String msg, boolean retry) {
      super(msg);
      this.retry = retry;
   }

   public boolean retryable() {
      return this.retry;
   }

   public static ApplicationDeadlockException isADE(Throwable t) {
      while(t != null) {
         if (t instanceof ApplicationDeadlockException) {
            return (ApplicationDeadlockException)t;
         }

         t = t.getCause();
      }

      return null;
   }
}
