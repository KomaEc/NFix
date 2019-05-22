package soot.jimple.internal;

import soot.Type;
import soot.Value;
import soot.baf.Baf;
import soot.jimple.AndExpr;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.util.Switch;

public class JAndExpr extends AbstractJimpleIntLongBinopExpr implements AndExpr {
   public JAndExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " & ";
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseAndExpr(this);
   }

   Object makeBafInst(Type opType) {
      return Baf.v().newAndInst(this.getOp1().getType());
   }

   public Object clone() {
      return new JAndExpr(Jimple.cloneIfNecessary(this.getOp1()), Jimple.cloneIfNecessary(this.getOp2()));
   }
}
