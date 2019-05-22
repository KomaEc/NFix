package soot.javaToJimple.jj.ast;

import polyglot.ast.Expr;
import polyglot.ast.Unary;
import polyglot.ext.jl.ast.Unary_c;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;

public class JjUnary_c extends Unary_c {
   public JjUnary_c(Position pos, Unary.Operator op, Expr expr) {
      super(pos, op, expr);
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      TypeSystem ts = av.typeSystem();
      if (child == this.expr) {
         if (this.op == POST_INC || this.op == POST_DEC || this.op == PRE_INC || this.op == PRE_DEC) {
            return child.type();
         }

         if (this.op == NEG || this.op == POS) {
            return child.type();
         }

         if (this.op == BIT_NOT) {
            return child.type();
         }

         if (this.op == NOT) {
            return ts.Boolean();
         }
      }

      return child.type();
   }
}
