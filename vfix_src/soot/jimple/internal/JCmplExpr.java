package soot.jimple.internal;

import soot.Type;
import soot.Value;
import soot.baf.Baf;
import soot.jimple.CmplExpr;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.util.Switch;

public class JCmplExpr extends AbstractJimpleIntBinopExpr implements CmplExpr {
   public JCmplExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " cmpl ";
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseCmplExpr(this);
   }

   Object makeBafInst(Type opType) {
      return Baf.v().newCmplInst(this.getOp1().getType());
   }

   public Object clone() {
      return new JCmplExpr(Jimple.cloneIfNecessary(this.getOp1()), Jimple.cloneIfNecessary(this.getOp2()));
   }
}
