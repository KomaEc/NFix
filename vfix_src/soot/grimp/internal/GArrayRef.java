package soot.grimp.internal;

import soot.UnitPrinter;
import soot.Value;
import soot.grimp.Grimp;
import soot.grimp.Precedence;
import soot.grimp.PrecedenceTest;
import soot.jimple.internal.JArrayRef;

public class GArrayRef extends JArrayRef implements Precedence {
   public GArrayRef(Value base, Value index) {
      super(Grimp.v().newObjExprBox(base), Grimp.v().newExprBox(index));
   }

   public int getPrecedence() {
      return 950;
   }

   private String toString(Value op1, String leftOp, String rightOp) {
      if (op1 instanceof Precedence && ((Precedence)op1).getPrecedence() < this.getPrecedence()) {
         leftOp = "(" + leftOp + ")";
      }

      return leftOp + "[" + rightOp + "]";
   }

   public void toString(UnitPrinter up) {
      if (PrecedenceTest.needsBrackets(this.baseBox, this)) {
         up.literal("(");
      }

      this.baseBox.toString(up);
      if (PrecedenceTest.needsBrackets(this.baseBox, this)) {
         up.literal(")");
      }

      up.literal("[");
      this.indexBox.toString(up);
      up.literal("]");
   }

   public String toString() {
      Value op1 = this.getBase();
      Value op2 = this.getIndex();
      String leftOp = op1.toString();
      String rightOp = op2.toString();
      return this.toString(op1, leftOp, rightOp);
   }

   public Object clone() {
      return new GArrayRef(Grimp.cloneIfNecessary(this.getBase()), Grimp.cloneIfNecessary(this.getIndex()));
   }
}
