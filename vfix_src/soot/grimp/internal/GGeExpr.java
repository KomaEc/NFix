package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.ExprSwitch;
import soot.jimple.GeExpr;
import soot.util.Switch;

public class GGeExpr extends AbstractGrimpIntBinopExpr implements GeExpr {
   public GGeExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " >= ";
   }

   public final int getPrecedence() {
      return 600;
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseGeExpr(this);
   }

   public Object clone() {
      return new GGeExpr(Grimp.cloneIfNecessary(this.getOp1()), Grimp.cloneIfNecessary(this.getOp2()));
   }
}
