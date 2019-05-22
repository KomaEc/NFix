package soot.dava.internal.javaRep;

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
import soot.grimp.Grimp;
import soot.jimple.internal.AbstractUnopExpr;
import soot.util.Switch;

public class DNotExpr extends AbstractUnopExpr {
   public DNotExpr(Value op) {
      super(Grimp.v().newExprBox(op));
   }

   public Object clone() {
      return new DNotExpr(Grimp.cloneIfNecessary(this.getOpBox().getValue()));
   }

   public void toString(UnitPrinter up) {
      up.literal(" ! (");
      this.getOpBox().toString(up);
      up.literal(")");
   }

   public String toString() {
      return " ! (" + this.getOpBox().getValue().toString() + ")";
   }

   public Type getType() {
      Value op = this.getOpBox().getValue();
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
   }

   public boolean equivTo(Object o) {
      return o instanceof DNotExpr ? this.getOpBox().getValue().equivTo(((DNotExpr)o).getOpBox().getValue()) : false;
   }

   public int equivHashCode() {
      return this.getOpBox().getValue().equivHashCode();
   }
}
