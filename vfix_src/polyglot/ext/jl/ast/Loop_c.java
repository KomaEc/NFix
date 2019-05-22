package polyglot.ext.jl.ast;

import polyglot.ast.Loop;
import polyglot.util.Position;

public abstract class Loop_c extends Stmt_c implements Loop {
   public Loop_c(Position pos) {
      super(pos);
   }

   public boolean condIsConstant() {
      return this.cond().isConstant();
   }

   public boolean condIsConstantTrue() {
      return Boolean.TRUE.equals(this.cond().constantValue());
   }
}
