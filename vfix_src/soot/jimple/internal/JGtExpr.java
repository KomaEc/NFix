package soot.jimple.internal;

import soot.Type;
import soot.Value;
import soot.jimple.ExprSwitch;
import soot.jimple.GtExpr;
import soot.jimple.Jimple;
import soot.util.Switch;

public class JGtExpr extends AbstractJimpleIntBinopExpr implements GtExpr {
   public JGtExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " > ";
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseGtExpr(this);
   }

   Object makeBafInst(Type opType) {
      throw new RuntimeException("unsupported conversion: " + this);
   }

   public Object clone() {
      return new JGtExpr(Jimple.cloneIfNecessary(this.getOp1()), Jimple.cloneIfNecessary(this.getOp2()));
   }
}
