package org.apache.velocity.exception;

public interface ExtendedParseException {
   String getTemplateName();

   int getLineNumber();

   int getColumnNumber();
}
