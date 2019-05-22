package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.CmpgExpr;
import soot.jimple.ExprSwitch;
import soot.util.Switch;

public class GCmpgExpr extends AbstractGrimpIntBinopExpr implements CmpgExpr {
   public GCmpgExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " cmpg ";
   }

   public final int getPrecedence() {
      return 600;
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseCmpgExpr(this);
   }

   public Object clone() {
      return new GCmpgExpr(Grimp.cloneIfNecessary(this.getOp1()), Grimp.cloneIfNecessary(this.getOp2()));
   }
}
