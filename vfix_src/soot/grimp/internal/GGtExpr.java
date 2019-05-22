package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.ExprSwitch;
import soot.jimple.GtExpr;
import soot.util.Switch;

public class GGtExpr extends AbstractGrimpIntBinopExpr implements GtExpr {
   public GGtExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " > ";
   }

   public final int getPrecedence() {
      return 600;
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseGtExpr(this);
   }

   public Object clone() {
      return new GGtExpr(Grimp.cloneIfNecessary(this.getOp1()), Grimp.cloneIfNecessary(this.getOp2()));
   }
}
