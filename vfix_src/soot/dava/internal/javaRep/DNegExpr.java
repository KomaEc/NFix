package soot.dava.internal.javaRep;

import soot.UnitPrinter;
import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.internal.AbstractNegExpr;

public class DNegExpr extends AbstractNegExpr {
   public DNegExpr(Value op) {
      super(Grimp.v().newExprBox(op));
   }

   public Object clone() {
      return new DNegExpr(Grimp.cloneIfNecessary(this.getOp()));
   }

   public void toString(UnitPrinter up) {
      up.literal("(");
      up.literal("-");
      up.literal(" ");
      up.literal("(");
      this.getOpBox().toString(up);
      up.literal(")");
      up.literal(")");
   }

   public String toString() {
      return "(- (" + this.getOpBox().getValue().toString() + "))";
   }
}
