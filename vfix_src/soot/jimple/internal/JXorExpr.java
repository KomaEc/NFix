package soot.jimple.internal;

import soot.Type;
import soot.Value;
import soot.baf.Baf;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.jimple.XorExpr;
import soot.util.Switch;

public class JXorExpr extends AbstractJimpleIntLongBinopExpr implements XorExpr {
   public JXorExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " ^ ";
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseXorExpr(this);
   }

   Object makeBafInst(Type opType) {
      return Baf.v().newXorInst(this.getOp1().getType());
   }

   public Object clone() {
      return new JXorExpr(Jimple.cloneIfNecessary(this.getOp1()), Jimple.cloneIfNecessary(this.getOp2()));
   }
}
