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
import soot.UnitPrinter;
import soot.UnknownType;
import soot.Value;
import soot.ValueBox;
import soot.jimple.ExprSwitch;
import soot.jimple.NegExpr;
import soot.util.Switch;

public abstract class AbstractNegExpr extends AbstractUnopExpr implements NegExpr {
   protected AbstractNegExpr(ValueBox opBox) {
      super(opBox);
   }

   public boolean equivTo(Object o) {
      return o instanceof AbstractNegExpr ? this.opBox.getValue().equivTo(((AbstractNegExpr)o).opBox.getValue()) : false;
   }

   public int equivHashCode() {
      return this.opBox.getValue().equivHashCode();
   }

   public abstract Object clone();

   public String toString() {
      return "neg " + this.opBox.getValue().toString();
   }

   public void toString(UnitPrinter up) {
      up.literal("neg");
      up.literal(" ");
      this.opBox.toString(up);
   }

   public Type getType() {
      Value op = this.opBox.getValue();
      if (!op.getType().equals(IntType.v()) && !op.getType().equals(ByteType.v()) && !op.getType().equals(ShortType.v()) && !op.getType().equals(BooleanType.v()) && !op.getType().equals(CharType.v())) {
         if (op.getType().equals(LongType.v())) {
            return LongType.v();
         } else if (op.getType().equals(DoubleType.v())) {
            return DoubleType.v();
         } else {
            return (Type)(op.getType().equals(FloatType.v()) ? FloatType.v() : UnknownType.v());
         }
      } else {
         return IntType.v();
      }
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseNegExpr(this);
   }
}
