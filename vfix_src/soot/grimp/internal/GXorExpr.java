package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.ExprSwitch;
import soot.jimple.XorExpr;
import soot.util.Switch;

public class GXorExpr extends AbstractGrimpIntLongBinopExpr implements XorExpr {
   public GXorExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " ^ ";
   }

   public final int getPrecedence() {
      return 450;
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseXorExpr(this);
   }

   public Object clone() {
      return new GXorExpr(Grimp.cloneIfNecessary(this.getOp1()), Grimp.cloneIfNecessary(this.getOp2()));
   }
}
