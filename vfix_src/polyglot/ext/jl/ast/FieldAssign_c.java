package polyglot.ext.jl.ast;

import java.util.ArrayList;
import java.util.List;
import polyglot.ast.Assign;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.FieldAssign;
import polyglot.ast.Term;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.CFGBuilder;

public class FieldAssign_c extends Assign_c implements FieldAssign {
   public FieldAssign_c(Position pos, Field left, Assign.Operator op, Expr right) {
      super(pos, left, op, right);
   }

   public Assign left(Expr left) {
      FieldAssign_c n = (FieldAssign_c)super.left(left);
      n.assertLeftType();
      return n;
   }

   private void assertLeftType() {
      if (!(this.left() instanceof Field)) {
         throw new InternalCompilerError("left expression of an FieldAssign must be a field");
      }
   }

   public Term entry() {
      Field f = (Field)this.left();
      if (f.target() instanceof Expr) {
         return ((Expr)f.target()).entry();
      } else {
         return (Term)(this.operator() != Assign.ASSIGN ? f : this.right().entry());
      }
   }

   protected void acceptCFGAssign(CFGBuilder v) {
      Field f = (Field)this.left();
      if (f.target() instanceof Expr) {
         Expr o = (Expr)f.target();
         v.visitCFG(o, (Term)this.right().entry());
         v.visitCFG(this.right(), (Term)this);
      } else {
         v.visitCFG(this.right(), (Term)this);
      }

   }

   protected void acceptCFGOpAssign(CFGBuilder v) {
      Field f = (Field)this.left();
      if (f.target() instanceof Expr) {
         Expr o = (Expr)f.target();
         v.visitCFG(o, (Term)f);
         v.visitThrow(f);
         v.edge(f, this.right().entry());
         v.visitCFG(this.right(), (Term)this);
      } else {
         v.visitThrow(f);
         v.edge(f, this.right().entry());
         v.visitCFG(this.right(), (Term)this);
      }

   }

   public List throwTypes(TypeSystem ts) {
      List l = new ArrayList(super.throwTypes(ts));
      Field f = (Field)this.left();
      if (f.target() instanceof Expr) {
         l.add(ts.NullPointerException());
      }

      return l;
   }
}
