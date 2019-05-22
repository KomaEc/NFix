package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.ExprSwitch;
import soot.jimple.OrExpr;
import soot.util.Switch;

public class GOrExpr extends AbstractGrimpIntLongBinopExpr implements OrExpr {
   public GOrExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public String getSymbol() {
      return " | ";
   }

   public int getPrecedence() {
      return 350;
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseOrExpr(this);
   }

   public Object clone() {
      return new GOrExpr(Grimp.cloneIfNecessary(this.getOp1()), Grimp.cloneIfNecessary(this.getOp2()));
   }
}
