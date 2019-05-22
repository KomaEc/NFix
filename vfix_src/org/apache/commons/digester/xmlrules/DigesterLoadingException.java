package org.apache.commons.digester.xmlrules;

public class DigesterLoadingException extends Exception {
   private Throwable cause;

   public DigesterLoadingException(String msg) {
      super(msg);
      this.cause = null;
   }

   public DigesterLoadingException(Throwable cause) {
      this(cause.getMessage());
      this.cause = cause;
   }

   public DigesterLoadingException(String msg, Throwable cause) {
      this(msg);
      this.cause = cause;
   }
}
