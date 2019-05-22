package soot.jimple.internal;

import soot.Type;
import soot.Value;
import soot.baf.Baf;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.jimple.SubExpr;
import soot.util.Switch;

public class JSubExpr extends AbstractJimpleFloatBinopExpr implements SubExpr {
   public JSubExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " - ";
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseSubExpr(this);
   }

   Object makeBafInst(Type opType) {
      return Baf.v().newSubInst(this.getOp1().getType());
   }

   public Object clone() {
      return new JSubExpr(Jimple.cloneIfNecessary(this.getOp1()), Jimple.cloneIfNecessary(this.getOp2()));
   }
}
