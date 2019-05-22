package org.testng.internal.thread;

public class ThreadTimeoutException extends Exception {
   static final long serialVersionUID = 7009400729783393548L;

   public ThreadTimeoutException(String msg) {
      super(msg);
   }

   public ThreadTimeoutException(Throwable cause) {
      super(cause);
   }

   public ThreadTimeoutException(String msg, Throwable cause) {
      super(msg, cause);
   }
}
