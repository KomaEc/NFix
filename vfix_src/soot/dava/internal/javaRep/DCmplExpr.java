package soot.dava.internal.javaRep;

import soot.IntType;
import soot.Type;
import soot.Value;
import soot.grimp.Grimp;
import soot.grimp.internal.AbstractGrimpIntBinopExpr;
import soot.jimple.CmplExpr;
import soot.jimple.ExprSwitch;
import soot.util.Switch;

public class DCmplExpr extends AbstractGrimpIntBinopExpr implements CmplExpr {
   public DCmplExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public final String getSymbol() {
      return " - ";
   }

   public final int getPrecedence() {
      return 700;
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseCmplExpr(this);
   }

   public Object clone() {
      return new DCmplExpr(Grimp.cloneIfNecessary(this.getOp1()), Grimp.cloneIfNecessary(this.getOp2()));
   }

   public Type getType() {
      return (Type)(this.getOp1().getType().equals(this.getOp2().getType()) ? this.getOp1().getType() : IntType.v());
   }
}
