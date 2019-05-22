package soot.jimple.internal;

import soot.Type;
import soot.Value;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.jimple.LeExpr;
import soot.util.Switch;

public class JLeExpr extends AbstractJimpleIntBinopExpr implements LeExpr {
   public JLeExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " <= ";
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseLeExpr(this);
   }

   Object makeBafInst(Type opType) {
      throw new RuntimeException("unsupported conversion: " + this);
   }

   public Object clone() {
      return new JLeExpr(Jimple.cloneIfNecessary(this.getOp1()), Jimple.cloneIfNecessary(this.getOp2()));
   }
}
