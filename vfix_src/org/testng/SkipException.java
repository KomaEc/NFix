package org.testng;

public class SkipException extends RuntimeException {
   private static final long serialVersionUID = 4052142657885527260L;
   private StackTraceElement[] m_stackTrace;
   private volatile boolean m_stackReduced;

   public SkipException(String skipMessage) {
      super(skipMessage);
   }

   public SkipException(String skipMessage, Throwable cause) {
      super(skipMessage, cause);
   }

   public boolean isSkip() {
      return true;
   }

   protected void reduceStackTrace() {
      if (!this.m_stackReduced) {
         synchronized(this) {
            StackTraceElement[] newStack = new StackTraceElement[1];
            StackTraceElement[] originalStack = this.getStackTrace();
            if (originalStack.length > 0) {
               this.m_stackTrace = originalStack;
               newStack[0] = this.getStackTrace()[0];
               this.setStackTrace(newStack);
            }

            this.m_stackReduced = true;
         }
      }

   }

   protected void restoreStackTrace() {
      if (this.m_stackReduced && null != this.m_stackTrace) {
         synchronized(this) {
            this.setStackTrace(this.m_stackTrace);
            this.m_stackReduced = false;
         }
      }

   }
}
