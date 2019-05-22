package org.codehaus.groovy.syntax;

import org.codehaus.groovy.GroovyException;

public class SyntaxException extends GroovyException {
   private final int line;
   private final int column;
   private String sourceLocator;

   public SyntaxException(String message, int line, int column) {
      super(message, false);
      this.line = line;
      this.column = column;
   }

   public SyntaxException(String message, Throwable cause, int line, int column) {
      super(message, cause);
      this.line = line;
      this.column = column;
   }

   public void setSourceLocator(String sourceLocator) {
      this.sourceLocator = sourceLocator;
   }

   public String getSourceLocator() {
      return this.sourceLocator;
   }

   public int getLine() {
      return this.line;
   }

   public int getStartColumn() {
      return this.column;
   }

   public int getStartLine() {
      return this.getLine();
   }

   public int getEndColumn() {
      return this.getStartColumn() + 1;
   }

   public String getOriginalMessage() {
      return super.getMessage();
   }

   public String getMessage() {
      return super.getMessage() + " @ line " + this.line + ", column " + this.column + ".";
   }
}
