package soot.jimple.parser.parser;

import soot.jimple.parser.node.Token;

public class ParserException extends Exception {
   Token token;

   public ParserException(Token token, String message) {
      super(message);
      this.token = token;
   }

   public Token getToken() {
      return this.token;
   }
}
