package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.AddExpr;
import soot.jimple.ExprSwitch;
import soot.util.Switch;

public class GAddExpr extends AbstractGrimpFloatBinopExpr implements AddExpr {
   public GAddExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " + ";
   }

   public final int getPrecedence() {
      return 700;
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseAddExpr(this);
   }

   public Object clone() {
      return new GAddExpr(Grimp.cloneIfNecessary(this.getOp1()), Grimp.cloneIfNecessary(this.getOp2()));
   }
}
