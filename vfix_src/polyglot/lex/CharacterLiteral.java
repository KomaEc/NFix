package polyglot.lex;

import polyglot.util.Position;

public class CharacterLiteral extends Literal {
   Character val;

   public CharacterLiteral(Position position, char c, int sym) {
      super(position, sym);
      this.val = new Character(c);
   }

   public Character getValue() {
      return this.val;
   }

   public String getEscapedValue() {
      return Token.escape(String.valueOf(this.val));
   }

   public String toString() {
      return "char literal '" + this.getEscapedValue() + "'";
   }
}
