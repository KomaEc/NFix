package polyglot.lex;

import polyglot.util.Position;

public class Identifier extends Token {
   String identifier;

   public Identifier(Position position, String identifier, int sym) {
      super(position, sym);
      this.identifier = identifier;
   }

   public String getIdentifier() {
      return this.identifier;
   }

   public String toString() {
      return "identifier \"" + this.identifier + "\"";
   }
}
