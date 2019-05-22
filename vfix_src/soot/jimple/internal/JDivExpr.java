package soot.jimple.internal;

import soot.Type;
import soot.Value;
import soot.baf.Baf;
import soot.jimple.DivExpr;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.util.Switch;

public class JDivExpr extends AbstractJimpleFloatBinopExpr implements DivExpr {
   public JDivExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " / ";
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseDivExpr(this);
   }

   Object makeBafInst(Type opType) {
      return Baf.v().newDivInst(this.getOp1().getType());
   }

   public Object clone() {
      return new JDivExpr(Jimple.cloneIfNecessary(this.getOp1()), Jimple.cloneIfNecessary(this.getOp2()));
   }
}
