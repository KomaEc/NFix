package polyglot.ext.jl.ast;

import polyglot.ast.AmbAssign;
import polyglot.ast.ArrayAccess;
import polyglot.ast.Assign;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.Local;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.types.SemanticException;
import polyglot.util.Position;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.CFGBuilder;

public class AmbAssign_c extends Assign_c implements AmbAssign {
   public AmbAssign_c(Position pos, Expr left, Assign.Operator op, Expr right) {
      super(pos, left, op, right);
   }

   public Term entry() {
      return (Term)(this.operator() != Assign.ASSIGN ? this.left() : this.right().entry());
   }

   protected void acceptCFGAssign(CFGBuilder v) {
      v.visitCFG(this.right(), (Term)this);
   }

   protected void acceptCFGOpAssign(CFGBuilder v) {
      v.edge(this.left(), this.right().entry());
      v.visitCFG(this.right(), (Term)this);
   }

   public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
      Assign n = (Assign)super.disambiguate(ar);
      if (n.left() instanceof Local) {
         return ar.nodeFactory().LocalAssign(n.position(), (Local)this.left(), this.operator(), this.right());
      } else if (n.left() instanceof Field) {
         return ar.nodeFactory().FieldAssign(n.position(), (Field)this.left(), this.operator(), this.right());
      } else if (n.left() instanceof ArrayAccess) {
         return ar.nodeFactory().ArrayAccessAssign(n.position(), (ArrayAccess)this.left(), this.operator(), this.right());
      } else {
         throw new SemanticException("Could not disambiguate left side of assignment!", n.position());
      }
   }
}
