package soot.grimp.internal;

import soot.SootFieldRef;
import soot.Value;
import soot.grimp.Grimp;
import soot.grimp.Precedence;
import soot.jimple.internal.AbstractInstanceFieldRef;

public class GInstanceFieldRef extends AbstractInstanceFieldRef implements Precedence {
   public GInstanceFieldRef(Value base, SootFieldRef fieldRef) {
      super(Grimp.v().newObjExprBox(base), fieldRef);
   }

   private String toString(Value op, String opString, String rightString) {
      String leftOp = opString;
      if (op instanceof Precedence && ((Precedence)op).getPrecedence() < this.getPrecedence()) {
         leftOp = "(" + opString + ")";
      }

      return leftOp + rightString;
   }

   public String toString() {
      return this.toString(this.getBase(), this.getBase().toString(), "." + this.fieldRef.getSignature());
   }

   public int getPrecedence() {
      return 950;
   }

   public Object clone() {
      return new GInstanceFieldRef(Grimp.cloneIfNecessary(this.getBase()), this.fieldRef);
   }
}
