package org.apache.maven.doxia.module.apt;

import org.apache.maven.doxia.parser.ParseException;

public class AptParseException extends ParseException {
   /** @deprecated */
   public AptParseException(String message, AptSource source) {
      super((Exception)null, message, source.getName(), source.getLineNumber());
   }

   /** @deprecated */
   public AptParseException(String message, AptSource source, Exception e) {
      super(e, message, source.getName(), source.getLineNumber());
   }

   public AptParseException(String message, String name, int lineNumber, Exception e) {
      super(e, message, name, lineNumber);
   }

   public AptParseException(String message, Exception e) {
      super(message, e);
   }

   public AptParseException(String message) {
      super(message);
   }

   public AptParseException(Exception e) {
      super(e);
   }
}
