package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.ExprSwitch;
import soot.jimple.LtExpr;
import soot.util.Switch;

public class GLtExpr extends AbstractGrimpIntBinopExpr implements LtExpr {
   public GLtExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " < ";
   }

   public final int getPrecedence() {
      return 600;
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseLtExpr(this);
   }

   public Object clone() {
      return new GLtExpr(Grimp.cloneIfNecessary(this.getOp1()), Grimp.cloneIfNecessary(this.getOp2()));
   }
}
