package polyglot.ext.jl.ast;

import polyglot.ast.Assign;
import polyglot.ast.Expr;
import polyglot.ast.Local;
import polyglot.ast.LocalAssign;
import polyglot.ast.Term;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.CFGBuilder;

public class LocalAssign_c extends Assign_c implements LocalAssign {
   public LocalAssign_c(Position pos, Local left, Assign.Operator op, Expr right) {
      super(pos, left, op, right);
   }

   public Assign left(Expr left) {
      LocalAssign_c n = (LocalAssign_c)super.left(left);
      n.assertLeftType();
      return n;
   }

   private void assertLeftType() {
      if (!(this.left() instanceof Local)) {
         throw new InternalCompilerError("left expression of an LocalAssign must be a local");
      }
   }

   public Term entry() {
      return (Term)(this.operator() != Assign.ASSIGN ? this.left() : this.right().entry());
   }

   protected void acceptCFGAssign(CFGBuilder v) {
      Local l = (Local)this.left();
      v.visitCFG(this.right(), (Term)this);
   }

   protected void acceptCFGOpAssign(CFGBuilder v) {
      Local l = (Local)this.left();
      v.visitThrow(l);
      v.edge(l, this.right().entry());
      v.visitCFG(this.right(), (Term)this);
   }
}
