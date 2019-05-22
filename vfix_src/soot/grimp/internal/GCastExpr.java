package soot.grimp.internal;

import soot.Type;
import soot.Value;
import soot.grimp.Grimp;
import soot.grimp.Precedence;
import soot.jimple.internal.AbstractCastExpr;

public class GCastExpr extends AbstractCastExpr implements Precedence {
   public GCastExpr(Value op, Type type) {
      super(Grimp.v().newExprBox(op), type);
   }

   public int getPrecedence() {
      return 850;
   }

   private String toString(String leftString, Value op, String opString) {
      String rightOp = opString;
      if (op instanceof Precedence && ((Precedence)op).getPrecedence() < this.getPrecedence()) {
         rightOp = "(" + opString + ")";
      }

      return leftString + rightOp;
   }

   public String toString() {
      return this.toString("(" + this.getCastType().toString() + ") ", this.getOp(), this.getOp().toString());
   }

   public Object clone() {
      return new GCastExpr(Grimp.cloneIfNecessary(this.getOp()), this.getCastType());
   }
}
