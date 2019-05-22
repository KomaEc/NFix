package org.codehaus.groovy.syntax;

public class TokenException extends SyntaxException {
   public TokenException(String message, Token token) {
      super(token == null ? message + ". No token" : message, getLine(token), getColumn(token));
   }

   public TokenException(String message, Throwable cause, int line, int column) {
      super(message, cause, line, column);
   }

   public int getEndColumn() {
      int length = 1;
      return this.getStartColumn() + length;
   }

   private static int getColumn(Token token) {
      return token != null ? token.getStartColumn() : -1;
   }

   private static int getLine(Token token) {
      return token != null ? token.getStartLine() : -1;
   }
}
