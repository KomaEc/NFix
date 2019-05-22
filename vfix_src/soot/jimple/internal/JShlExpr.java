package soot.jimple.internal;

import soot.IntType;
import soot.LongType;
import soot.Type;
import soot.UnknownType;
import soot.Value;
import soot.baf.Baf;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.jimple.ShlExpr;
import soot.util.Switch;

public class JShlExpr extends AbstractJimpleIntLongBinopExpr implements ShlExpr {
   public JShlExpr(Value op1, Value op2) {
      super(op1, op2);
   }

   public String getSymbol() {
      return " << ";
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseShlExpr(this);
   }

   Object makeBafInst(Type opType) {
      return Baf.v().newShlInst(this.getOp1().getType());
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
      return new JShlExpr(Jimple.cloneIfNecessary(this.getOp1()), Jimple.cloneIfNecessary(this.getOp2()));
   }
}
