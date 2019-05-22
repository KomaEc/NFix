package polyglot.ext.jl.qq;

import java.util.List;
import polyglot.lex.Token;
import polyglot.util.Position;

public class QQListToken extends Token {
   List list;

   public QQListToken(Position position, List list, int sym) {
      super(position, sym);
      this.list = list;
   }

   public List list() {
      return this.list;
   }

   public String toString() {
      return "qq(" + this.list + ")";
   }
}
