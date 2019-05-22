package soot.jimple.internal;

import soot.Type;
import soot.Value;
import soot.baf.Baf;
import soot.jimple.CmpgExpr;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.util.Switch;

public class JCmpgExpr extends AbstractJimpleIntBinopExpr implements CmpgExpr {
   public JCmpgExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " cmpg ";
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseCmpgExpr(this);
   }

   Object makeBafInst(Type opType) {
      return Baf.v().newCmpgInst(this.getOp1().getType());
   }

   public Object clone() {
      return new JCmpgExpr(Jimple.cloneIfNecessary(this.getOp1()), Jimple.cloneIfNecessary(this.getOp2()));
   }
}
