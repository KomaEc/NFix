package soot.javaToJimple.jj.ast;

import polyglot.ast.ArrayAccess;
import polyglot.ast.Assign;
import polyglot.ast.Expr;
import polyglot.ext.jl.ast.ArrayAccessAssign_c;
import polyglot.types.Type;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;

public class JjArrayAccessAssign_c extends ArrayAccessAssign_c {
   public JjArrayAccessAssign_c(Position pos, ArrayAccess left, Assign.Operator op, Expr right) {
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
