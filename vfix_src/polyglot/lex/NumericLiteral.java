package polyglot.lex;

import polyglot.util.Position;

public abstract class NumericLiteral extends Literal {
   Number val;

   public NumericLiteral(Position position, int sym) {
      super(position, sym);
   }

   public Number getValue() {
      return this.val;
   }

   public String toString() {
      return "numeric literal " + this.val.toString();
   }
}
