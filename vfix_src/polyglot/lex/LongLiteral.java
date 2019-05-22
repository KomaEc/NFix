package polyglot.lex;

import polyglot.util.Position;

public class LongLiteral extends NumericLiteral {
   public LongLiteral(Position position, long l, int sym) {
      super(position, sym);
      this.val = new Long(l);
   }
}
