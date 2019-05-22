package soot.jimple.internal;

import soot.Type;
import soot.Value;
import soot.jimple.EqExpr;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.util.Switch;

public class JEqExpr extends AbstractJimpleIntBinopExpr implements EqExpr {
   public JEqExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " == ";
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseEqExpr(this);
   }

   Object makeBafInst(Type opType) {
      throw new RuntimeException("unsupported conversion: " + this);
   }

   public Object clone() {
      return new JEqExpr(Jimple.cloneIfNecessary(this.getOp1()), Jimple.cloneIfNecessary(this.getOp2()));
   }
}
