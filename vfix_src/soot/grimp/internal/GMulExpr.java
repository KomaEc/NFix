package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.ExprSwitch;
import soot.jimple.MulExpr;
import soot.util.Switch;

public class GMulExpr extends AbstractGrimpFloatBinopExpr implements MulExpr {
   public GMulExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " * ";
   }

   public final int getPrecedence() {
      return 800;
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseMulExpr(this);
   }

   public Object clone() {
      return new GMulExpr(Grimp.cloneIfNecessary(this.getOp1()), Grimp.cloneIfNecessary(this.getOp2()));
   }
}
