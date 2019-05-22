package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.DivExpr;
import soot.jimple.ExprSwitch;
import soot.util.Switch;

public class GDivExpr extends AbstractGrimpFloatBinopExpr implements DivExpr {
   public GDivExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " / ";
   }

   public final int getPrecedence() {
      return 800;
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseDivExpr(this);
   }

   public Object clone() {
      return new GDivExpr(Grimp.cloneIfNecessary(this.getOp1()), Grimp.cloneIfNecessary(this.getOp2()));
   }
}
