package soot.dava.internal.javaRep;

import soot.UnitPrinter;
import soot.Value;
import soot.grimp.Grimp;
import soot.grimp.Precedence;
import soot.grimp.PrecedenceTest;
import soot.jimple.internal.AbstractLengthExpr;

public class DLengthExpr extends AbstractLengthExpr implements Precedence {
   public DLengthExpr(Value op) {
      super(Grimp.v().newObjExprBox(op));
   }

   public int getPrecedence() {
      return 950;
   }

   public Object clone() {
      return new DLengthExpr(Grimp.cloneIfNecessary(this.getOp()));
   }

   public void toString(UnitPrinter up) {
      if (PrecedenceTest.needsBrackets(this.getOpBox(), this)) {
         up.literal("(");
      }

      this.getOpBox().toString(up);
      if (PrecedenceTest.needsBrackets(this.getOpBox(), this)) {
         up.literal(")");
      }

      up.literal(".");
      up.literal("length");
   }

   public String toString() {
      StringBuffer b = new StringBuffer();
      if (PrecedenceTest.needsBrackets(this.getOpBox(), this)) {
         b.append("(");
      }

      b.append(this.getOpBox().getValue().toString());
      if (PrecedenceTest.needsBrackets(this.getOpBox(), this)) {
         b.append(")");
      }

      b.append(".length");
      return b.toString();
   }
}
