package org.apache.maven.doxia.tools;

public class SiteToolException extends Exception {
   static final long serialVersionUID = 2331441332996055959L;

   public SiteToolException(String message, Exception cause) {
      super(message, cause);
   }

   public SiteToolException(String message, Throwable cause) {
      super(message, cause);
   }

   public SiteToolException(String message) {
      super(message);
   }
}
