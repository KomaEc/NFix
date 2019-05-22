package polyglot.lex;

import polyglot.util.Position;

public class EOF extends Token {
   public EOF(Position position, int sym) {
      super(position, sym);
   }

   public String toString() {
      return "end of file";
   }
}
