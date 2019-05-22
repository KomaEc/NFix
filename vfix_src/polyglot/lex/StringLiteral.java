package polyglot.lex;

import polyglot.util.Position;

public class StringLiteral extends Literal {
   String val;

   public StringLiteral(Position position, String s, int sym) {
      super(position, sym);
      this.val = s;
   }

   public String getValue() {
      return this.val;
   }

   public String toString() {
      return "string literal \"" + Token.escape(this.val) + "\"";
   }
}
