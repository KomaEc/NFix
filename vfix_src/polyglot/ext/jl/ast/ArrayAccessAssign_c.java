package polyglot.ext.jl.ast;

import java.util.ArrayList;
import java.util.List;
import polyglot.ast.ArrayAccess;
import polyglot.ast.ArrayAccessAssign;
import polyglot.ast.Assign;
import polyglot.ast.Expr;
import polyglot.ast.Term;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.CFGBuilder;

public class ArrayAccessAssign_c extends Assign_c implements ArrayAccessAssign {
   public ArrayAccessAssign_c(Position pos, ArrayAccess left, Assign.Operator op, Expr right) {
      super(pos, left, op, right);
   }

   public Assign left(Expr left) {
      ArrayAccessAssign_c n = (ArrayAccessAssign_c)super.left(left);
      n.assertLeftType();
      return n;
   }

   private void assertLeftType() {
      if (!(this.left() instanceof ArrayAccess)) {
         throw new InternalCompilerError("left expression of an ArrayAccessAssign must be an array access");
      }
   }

   public Term entry() {
      return this.left().entry();
   }

   protected void acceptCFGAssign(CFGBuilder v) {
      ArrayAccess a = (ArrayAccess)this.left();
      v.visitCFG(a.array(), (Term)a.index().entry());
      v.visitCFG(a.index(), (Term)this.right().entry());
      v.visitCFG(this.right(), (Term)this);
   }

   protected void acceptCFGOpAssign(CFGBuilder v) {
      ArrayAccess a = (ArrayAccess)this.left();
      v.visitCFG(a.array(), (Term)a.index().entry());
      v.visitCFG(a.index(), (Term)a);
      v.visitThrow(a);
      v.edge(a, this.right().entry());
      v.visitCFG(this.right(), (Term)this);
   }

   public List throwTypes(TypeSystem ts) {
      List l = new ArrayList(super.throwTypes(ts));
      if (this.throwsArrayStoreException()) {
         l.add(ts.ArrayStoreException());
      }

      l.add(ts.NullPointerException());
      l.add(ts.OutOfBoundsException());
      return l;
   }

   public boolean throwsArrayStoreException() {
      return this.op == Assign.ASSIGN && this.left.type().isReference();
   }
}
