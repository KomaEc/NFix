package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.CmplExpr;
import soot.jimple.ExprSwitch;
import soot.util.Switch;

public class GCmplExpr extends AbstractGrimpIntBinopExpr implements CmplExpr {
   public GCmplExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " cmpl ";
   }

   public final int getPrecedence() {
      return 600;
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseCmplExpr(this);
   }

   public Object clone() {
      return new GCmplExpr(Grimp.cloneIfNecessary(this.getOp1()), Grimp.cloneIfNecessary(this.getOp2()));
   }
}
