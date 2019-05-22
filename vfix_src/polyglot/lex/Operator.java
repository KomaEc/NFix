package polyglot.lex;

import polyglot.util.Position;

public class Operator extends Token {
   String which;

   public Operator(Position position, String which, int sym) {
      super(position, sym);
      this.which = which;
   }

   public String toString() {
      return "operator " + this.which;
   }
}
