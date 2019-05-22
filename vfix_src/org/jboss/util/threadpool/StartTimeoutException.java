package org.jboss.util.threadpool;

public class StartTimeoutException extends RuntimeException {
   private static final long serialVersionUID = 2287779538710535096L;

   public StartTimeoutException() {
   }

   public StartTimeoutException(String message) {
      super(message);
   }
}
