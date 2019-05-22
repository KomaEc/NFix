package soot.javaToJimple.jj.ast;

import polyglot.ast.Assign;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ext.jl.ast.FieldAssign_c;
import polyglot.types.Type;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;

public class JjFieldAssign_c extends FieldAssign_c {
   public JjFieldAssign_c(Position pos, Field left, Assign.Operator op, Expr right) {
      super(pos, left, op, right);
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      if (this.op != SHL_ASSIGN && this.op != SHR_ASSIGN && this.op != USHR_ASSIGN) {
         return child == this.right ? this.left.type() : child.type();
      } else {
         return child.type();
      }
   }
}
