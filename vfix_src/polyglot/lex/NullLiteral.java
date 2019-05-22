package polyglot.lex;

import polyglot.util.Position;

public class NullLiteral extends Literal {
   public NullLiteral(Position position, int sym) {
      super(position, sym);
   }

   public String toString() {
      return "literal null";
   }
}
