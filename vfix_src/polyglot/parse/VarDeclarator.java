package polyglot.parse;

import polyglot.ast.Expr;
import polyglot.util.Position;

public class VarDeclarator {
   public Position pos;
   public String name;
   public int dims;
   public Expr init;

   public VarDeclarator(Position pos, String name) {
      this.pos = pos;
      this.name = name;
      this.dims = 0;
      this.init = null;
   }

   public Position position() {
      return this.pos;
   }
}
