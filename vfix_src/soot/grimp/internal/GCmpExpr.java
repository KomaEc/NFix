package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.CmpExpr;
import soot.jimple.ExprSwitch;
import soot.util.Switch;

public class GCmpExpr extends AbstractGrimpIntBinopExpr implements CmpExpr {
   public GCmpExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " cmp ";
   }

   public final int getPrecedence() {
      return 550;
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseCmpExpr(this);
   }

   public Object clone() {
      return new GCmpExpr(Grimp.cloneIfNecessary(this.getOp1()), Grimp.cloneIfNecessary(this.getOp2()));
   }
}
