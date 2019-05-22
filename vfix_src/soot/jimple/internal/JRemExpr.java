package soot.jimple.internal;

import soot.Type;
import soot.Value;
import soot.baf.Baf;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.jimple.RemExpr;
import soot.util.Switch;

public class JRemExpr extends AbstractJimpleFloatBinopExpr implements RemExpr {
   public JRemExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public String getSymbol() {
      return " % ";
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseRemExpr(this);
   }

   Object makeBafInst(Type opType) {
      return Baf.v().newRemInst(this.getOp1().getType());
   }

   public Object clone() {
      return new JRemExpr(Jimple.cloneIfNecessary(this.getOp1()), Jimple.cloneIfNecessary(this.getOp2()));
   }
}
