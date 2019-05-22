package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.AndExpr;
import soot.jimple.ExprSwitch;
import soot.util.Switch;

public class GAndExpr extends AbstractGrimpIntLongBinopExpr implements AndExpr {
   public GAndExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " & ";
   }

   public final int getPrecedence() {
      return 500;
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseAndExpr(this);
   }

   public Object clone() {
      return new GAndExpr(Grimp.cloneIfNecessary(this.getOp1()), Grimp.cloneIfNecessary(this.getOp2()));
   }
}
