package soot.jimple.internal;

import soot.Type;
import soot.Value;
import soot.baf.Baf;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.jimple.OrExpr;
import soot.util.Switch;

public class JOrExpr extends AbstractJimpleIntLongBinopExpr implements OrExpr {
   public JOrExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public String getSymbol() {
      return " | ";
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseOrExpr(this);
   }

   Object makeBafInst(Type opType) {
      return Baf.v().newOrInst(this.getOp1().getType());
   }

   public Object clone() {
      return new JOrExpr(Jimple.cloneIfNecessary(this.getOp1()), Jimple.cloneIfNecessary(this.getOp2()));
   }
}
