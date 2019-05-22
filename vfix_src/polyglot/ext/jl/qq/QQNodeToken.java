package polyglot.ext.jl.qq;

import polyglot.ast.Node;
import polyglot.lex.Token;
import polyglot.util.Position;

public class QQNodeToken extends Token {
   Node node;

   public QQNodeToken(Position position, Node node, int sym) {
      super(position, sym);
      this.node = node;
   }

   public Node node() {
      return this.node;
   }

   public String toString() {
      return "qq" + this.symbol() + "(" + this.node + ")";
   }
}
