package polyglot.lex;

import polyglot.util.Position;

public class DoubleLiteral extends NumericLiteral {
   public DoubleLiteral(Position position, double d, int sym) {
      super(position, sym);
      this.val = new Double(d);
   }
}
