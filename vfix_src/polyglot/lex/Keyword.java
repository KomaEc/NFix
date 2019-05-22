package polyglot.lex;

import polyglot.util.Position;

public class Keyword extends Token {
   String keyword;

   public Keyword(Position position, String s, int sym) {
      super(position, sym);
      this.keyword = s;
   }

   public String toString() {
      return this.keyword;
   }
}
