package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Lit;
import polyglot.ast.Precedence;
import polyglot.ast.Term;
import polyglot.util.Position;
import polyglot.visit.CFGBuilder;

public abstract class Lit_c extends Expr_c implements Lit {
   public Lit_c(Position pos) {
      super(pos);
   }

   public Precedence precedence() {
      return Precedence.LITERAL;
   }

   public Term entry() {
      return this;
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      return succs;
   }

   public boolean isConstant() {
      return true;
   }

   public abstract Object constantValue();
}
