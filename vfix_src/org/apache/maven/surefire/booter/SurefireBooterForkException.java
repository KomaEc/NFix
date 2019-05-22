package org.apache.maven.surefire.booter;

public class SurefireBooterForkException extends Exception {
   public SurefireBooterForkException(String message, Throwable cause) {
      super(message, cause);
   }

   public SurefireBooterForkException(String msg) {
      super(msg);
   }
}
