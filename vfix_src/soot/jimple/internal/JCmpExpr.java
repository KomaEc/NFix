package soot.jimple.internal;

import soot.Type;
import soot.Value;
import soot.baf.Baf;
import soot.jimple.CmpExpr;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.util.Switch;

public class JCmpExpr extends AbstractJimpleIntBinopExpr implements CmpExpr {
   public JCmpExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " cmp ";
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseCmpExpr(this);
   }

   Object makeBafInst(Type opType) {
      return Baf.v().newCmpInst(this.getOp1().getType());
   }

   public Object clone() {
      return new JCmpExpr(Jimple.cloneIfNecessary(this.getOp1()), Jimple.cloneIfNecessary(this.getOp2()));
   }
}
