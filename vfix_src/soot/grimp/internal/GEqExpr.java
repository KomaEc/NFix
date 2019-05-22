package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.EqExpr;
import soot.jimple.ExprSwitch;
import soot.util.Switch;

public class GEqExpr extends AbstractGrimpIntBinopExpr implements EqExpr {
   public GEqExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " == ";
   }

   public final int getPrecedence() {
      return 550;
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseEqExpr(this);
   }

   public Object clone() {
      return new GEqExpr(Grimp.cloneIfNecessary(this.getOp1()), Grimp.cloneIfNecessary(this.getOp2()));
   }
}
