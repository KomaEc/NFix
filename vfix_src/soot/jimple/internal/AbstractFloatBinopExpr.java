package soot.jimple.internal;

import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.ShortType;
import soot.Type;
import soot.UnknownType;
import soot.Value;

public abstract class AbstractFloatBinopExpr extends AbstractBinopExpr {
   public Type getType() {
      Value op1 = this.op1Box.getValue();
      Value op2 = this.op2Box.getValue();
      Type op1t = op1.getType();
      Type op2t = op2.getType();
      if ((op1t.equals(IntType.v()) || op1t.equals(ByteType.v()) || op1t.equals(ShortType.v()) || op1t.equals(CharType.v()) || op1t.equals(BooleanType.v())) && (op2t.equals(IntType.v()) || op2t.equals(ByteType.v()) || op2t.equals(ShortType.v()) || op2t.equals(CharType.v()) || op2t.equals(BooleanType.v()))) {
         return IntType.v();
      } else if (!op1t.equals(LongType.v()) && !op2t.equals(LongType.v())) {
         if (!op1t.equals(DoubleType.v()) && !op2t.equals(DoubleType.v())) {
            return (Type)(!op1t.equals(FloatType.v()) && !op2t.equals(FloatType.v()) ? UnknownType.v() : FloatType.v());
         } else {
            return DoubleType.v();
         }
      } else {
         return LongType.v();
      }
   }
}
