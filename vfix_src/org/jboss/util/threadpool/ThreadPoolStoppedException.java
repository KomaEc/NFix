package org.jboss.util.threadpool;

public class ThreadPoolStoppedException extends RuntimeException {
   private static final long serialVersionUID = 8732248855455909385L;

   public ThreadPoolStoppedException() {
   }

   public ThreadPoolStoppedException(String message) {
      super(message);
   }
}
