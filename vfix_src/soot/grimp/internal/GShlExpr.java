package soot.grimp.internal;

import soot.IntType;
import soot.LongType;
import soot.Type;
import soot.UnknownType;
import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.ExprSwitch;
import soot.jimple.ShlExpr;
import soot.util.Switch;

public class GShlExpr extends AbstractGrimpIntLongBinopExpr implements ShlExpr {
   public GShlExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public String getSymbol() {
      return " << ";
   }

   public int getPrecedence() {
      return 650;
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseShlExpr(this);
   }

   public Type getType() {
      Value op1 = this.op1Box.getValue();
      Value op2 = this.op2Box.getValue();
      if (!isIntLikeType(op2.getType())) {
         return UnknownType.v();
      } else if (isIntLikeType(op1.getType())) {
         return IntType.v();
      } else {
         return (Type)(op1.getType().equals(LongType.v()) ? LongType.v() : UnknownType.v());
      }
   }

   public Object clone() {
      return new GShlExpr(Grimp.cloneIfNecessary(this.getOp1()), Grimp.cloneIfNecessary(this.getOp2()));
   }
}
