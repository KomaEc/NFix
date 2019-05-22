package org.apache.commons.digester.xmlrules;

public class XmlLoadException extends RuntimeException {
   private Throwable cause;

   public XmlLoadException(Throwable cause) {
      this(cause.getMessage());
      this.cause = cause;
   }

   public XmlLoadException(String msg) {
      super(msg);
      this.cause = null;
   }

   public XmlLoadException(String msg, Throwable cause) {
      this(msg);
      this.cause = cause;
   }

   public Throwable getCause() {
      return this.cause;
   }
}
