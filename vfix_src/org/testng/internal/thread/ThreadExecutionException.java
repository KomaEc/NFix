package org.testng.internal.thread;

public class ThreadExecutionException extends Exception {
   static final long serialVersionUID = -7766644143333236263L;

   public ThreadExecutionException(Throwable t) {
      super(t);
   }
}
