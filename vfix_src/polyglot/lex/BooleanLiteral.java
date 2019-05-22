package polyglot.lex;

import polyglot.util.Position;

public class BooleanLiteral extends Literal {
   Boolean val;

   public BooleanLiteral(Position position, boolean b, int sym) {
      super(position, sym);
      this.val = b;
   }

   public Boolean getValue() {
      return this.val;
   }

   public String toString() {
      return "boolean literal " + this.val.toString();
   }
}
