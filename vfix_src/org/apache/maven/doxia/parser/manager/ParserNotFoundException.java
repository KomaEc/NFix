package org.apache.maven.doxia.parser.manager;

public class ParserNotFoundException extends Exception {
   public ParserNotFoundException(String message) {
      super(message);
   }

   public ParserNotFoundException(Throwable cause) {
      super(cause);
   }

   public ParserNotFoundException(String message, Throwable cause) {
      super(message, cause);
   }
}
