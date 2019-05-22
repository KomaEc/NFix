package org.jboss.util.threadpool;

public class ThreadPoolFullException extends RuntimeException {
   private static final long serialVersionUID = -1044683480627340299L;

   public ThreadPoolFullException() {
   }

   public ThreadPoolFullException(String message) {
      super(message);
   }

   public ThreadPoolFullException(String message, Throwable t) {
      super(message, t);
   }
}
