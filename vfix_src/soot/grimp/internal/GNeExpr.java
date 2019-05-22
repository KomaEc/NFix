package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.ExprSwitch;
import soot.jimple.NeExpr;
import soot.util.Switch;

public class GNeExpr extends AbstractGrimpIntBinopExpr implements NeExpr {
   public GNeExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " != ";
   }

   public final int getPrecedence() {
      return 550;
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseNeExpr(this);
   }

   public Object clone() {
      return new GNeExpr(Grimp.cloneIfNecessary(this.getOp1()), Grimp.cloneIfNecessary(this.getOp2()));
   }
}
