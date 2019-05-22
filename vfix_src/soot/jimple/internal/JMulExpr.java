package soot.jimple.internal;

import soot.Type;
import soot.Value;
import soot.baf.Baf;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.jimple.MulExpr;
import soot.util.Switch;

public class JMulExpr extends AbstractJimpleFloatBinopExpr implements MulExpr {
   public JMulExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " * ";
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseMulExpr(this);
   }

   Object makeBafInst(Type opType) {
      return Baf.v().newMulInst(this.getOp1().getType());
   }

   public Object clone() {
      return new JMulExpr(Jimple.cloneIfNecessary(this.getOp1()), Jimple.cloneIfNecessary(this.getOp2()));
   }
}
