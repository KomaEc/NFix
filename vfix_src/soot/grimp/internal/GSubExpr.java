package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.ExprSwitch;
import soot.jimple.SubExpr;
import soot.util.Switch;

public class GSubExpr extends AbstractGrimpFloatBinopExpr implements SubExpr {
   public GSubExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " - ";
   }

   public final int getPrecedence() {
      return 700;
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseSubExpr(this);
   }

   public Object clone() {
      return new GSubExpr(Grimp.cloneIfNecessary(this.getOp1()), Grimp.cloneIfNecessary(this.getOp2()));
   }
}
