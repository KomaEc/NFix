package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.ExprSwitch;
import soot.jimple.RemExpr;
import soot.util.Switch;

public class GRemExpr extends AbstractGrimpFloatBinopExpr implements RemExpr {
   public GRemExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " % ";
   }

   public final int getPrecedence() {
      return 800;
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseRemExpr(this);
   }

   public Object clone() {
      return new GRemExpr(Grimp.cloneIfNecessary(this.getOp1()), Grimp.cloneIfNecessary(this.getOp2()));
   }
}
