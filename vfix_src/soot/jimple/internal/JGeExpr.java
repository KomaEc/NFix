package soot.jimple.internal;

import soot.Type;
import soot.Value;
import soot.jimple.ExprSwitch;
import soot.jimple.GeExpr;
import soot.jimple.Jimple;
import soot.util.Switch;

public class JGeExpr extends AbstractJimpleIntBinopExpr implements GeExpr {
   public JGeExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " >= ";
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseGeExpr(this);
   }

   Object makeBafInst(Type opType) {
      throw new RuntimeException("unsupported conversion: " + this);
   }

   public Object clone() {
      return new JGeExpr(Jimple.cloneIfNecessary(this.getOp1()), Jimple.cloneIfNecessary(this.getOp2()));
   }
}
