package soot.jimple.internal;

import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.IntType;
import soot.LongType;
import soot.ShortType;
import soot.Type;
import soot.UnknownType;
import soot.Value;

public abstract class AbstractIntLongBinopExpr extends AbstractBinopExpr {
   public static boolean isIntLikeType(Type t) {
      return t.equals(IntType.v()) || t.equals(ByteType.v()) || t.equals(ShortType.v()) || t.equals(CharType.v()) || t.equals(BooleanType.v());
   }

   public Type getType() {
      Value op1 = this.op1Box.getValue();
      Value op2 = this.op2Box.getValue();
      if (isIntLikeType(op1.getType()) && isIntLikeType(op2.getType())) {
         return IntType.v();
      } else {
         return (Type)(op1.getType().equals(LongType.v()) && op2.getType().equals(LongType.v()) ? LongType.v() : UnknownType.v());
      }
   }
}
