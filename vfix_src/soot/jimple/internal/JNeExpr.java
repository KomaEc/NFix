package soot.jimple.internal;

import soot.Type;
import soot.Value;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.jimple.NeExpr;
import soot.util.Switch;

public class JNeExpr extends AbstractJimpleIntBinopExpr implements NeExpr {
   public JNeExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " != ";
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseNeExpr(this);
   }

   Object makeBafInst(Type opType) {
      throw new RuntimeException("unsupported conversion: " + this);
   }

   public Object clone() {
      return new JNeExpr(Jimple.cloneIfNecessary(this.getOp1()), Jimple.cloneIfNecessary(this.getOp2()));
   }
}
