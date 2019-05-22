package polyglot.ext.jl.ast;

import polyglot.ast.Stmt;
import polyglot.util.Position;

public abstract class Stmt_c extends Term_c implements Stmt {
   public Stmt_c(Position pos) {
      super(pos);
   }
}
