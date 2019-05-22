package polyglot.lex;

import polyglot.util.Position;

public abstract class Literal extends Token {
   public Literal(Position position, int sym) {
      super(position, sym);
   }
}
