package soot.jimple;

import soot.LongType;
import soot.Type;
import soot.util.Switch;

public class LongConstant extends ArithmeticConstant {
   public final long value;

   private LongConstant(long value) {
      this.value = value;
   }

   public static LongConstant v(long value) {
      return new LongConstant(value);
   }

   public boolean equals(Object c) {
      return c instanceof LongConstant && ((LongConstant)c).value == this.value;
   }

   public int hashCode() {
      return (int)(this.value ^ this.value >>> 32);
   }

   public NumericConstant add(NumericConstant c) {
      if (!(c instanceof LongConstant)) {
         throw new IllegalArgumentException("LongConstant expected");
      } else {
         return v(this.value + ((LongConstant)c).value);
      }
   }

   public NumericConstant subtract(NumericConstant c) {
      if (!(c instanceof LongConstant)) {
         throw new IllegalArgumentException("LongConstant expected");
      } else {
         return v(this.value - ((LongConstant)c).value);
      }
   }

   public NumericConstant multiply(NumericConstant c) {
      if (!(c instanceof LongConstant)) {
         throw new IllegalArgumentException("LongConstant expected");
      } else {
         return v(this.value * ((LongConstant)c).value);
      }
   }

   public NumericConstant divide(NumericConstant c) {
      if (!(c instanceof LongConstant)) {
         throw new IllegalArgumentException("LongConstant expected");
      } else {
         return v(this.value / ((LongConstant)c).value);
      }
   }

   public NumericConstant remainder(NumericConstant c) {
      if (!(c instanceof LongConstant)) {
         throw new IllegalArgumentException("LongConstant expected");
      } else {
         return v(this.value % ((LongConstant)c).value);
      }
   }

   public NumericConstant equalEqual(NumericConstant c) {
      if (!(c instanceof LongConstant)) {
         throw new IllegalArgumentException("LongConstant expected");
      } else {
         return IntConstant.v(this.value == ((LongConstant)c).value ? 1 : 0);
      }
   }

   public NumericConstant notEqual(NumericConstant c) {
      if (!(c instanceof LongConstant)) {
         throw new IllegalArgumentException("LongConstant expected");
      } else {
         return IntConstant.v(this.value != ((LongConstant)c).value ? 1 : 0);
      }
   }

   public NumericConstant lessThan(NumericConstant c) {
      if (!(c instanceof LongConstant)) {
         throw new IllegalArgumentException("LongConstant expected");
      } else {
         return IntConstant.v(this.value < ((LongConstant)c).value ? 1 : 0);
      }
   }

   public NumericConstant lessThanOrEqual(NumericConstant c) {
      if (!(c instanceof LongConstant)) {
         throw new IllegalArgumentException("LongConstant expected");
      } else {
         return IntConstant.v(this.value <= ((LongConstant)c).value ? 1 : 0);
      }
   }

   public NumericConstant greaterThan(NumericConstant c) {
      if (!(c instanceof LongConstant)) {
         throw new IllegalArgumentException("LongConstant expected");
      } else {
         return IntConstant.v(this.value > ((LongConstant)c).value ? 1 : 0);
      }
   }

   public NumericConstant greaterThanOrEqual(NumericConstant c) {
      if (!(c instanceof LongConstant)) {
         throw new IllegalArgumentException("LongConstant expected");
      } else {
         return IntConstant.v(this.value >= ((LongConstant)c).value ? 1 : 0);
      }
   }

   public IntConstant cmp(LongConstant c) {
      if (this.value > c.value) {
         return IntConstant.v(1);
      } else {
         return this.value == c.value ? IntConstant.v(0) : IntConstant.v(-1);
      }
   }

   public NumericConstant negate() {
      return v(-this.value);
   }

   public ArithmeticConstant and(ArithmeticConstant c) {
      if (!(c instanceof LongConstant)) {
         throw new IllegalArgumentException("LongConstant expected");
      } else {
         return v(this.value & ((LongConstant)c).value);
      }
   }

   public ArithmeticConstant or(ArithmeticConstant c) {
      if (!(c instanceof LongConstant)) {
         throw new IllegalArgumentException("LongConstant expected");
      } else {
         return v(this.value | ((LongConstant)c).value);
      }
   }

   public ArithmeticConstant xor(ArithmeticConstant c) {
      if (!(c instanceof LongConstant)) {
         throw new IllegalArgumentException("LongConstant expected");
      } else {
         return v(this.value ^ ((LongConstant)c).value);
      }
   }

   public ArithmeticConstant shiftLeft(ArithmeticConstant c) {
      if (!(c instanceof IntConstant)) {
         throw new IllegalArgumentException("IntConstant expected");
      } else {
         return v(this.value << ((IntConstant)c).value);
      }
   }

   public ArithmeticConstant shiftRight(ArithmeticConstant c) {
      if (!(c instanceof IntConstant)) {
         throw new IllegalArgumentException("IntConstant expected");
      } else {
         return v(this.value >> ((IntConstant)c).value);
      }
   }

   public ArithmeticConstant unsignedShiftRight(ArithmeticConstant c) {
      if (!(c instanceof IntConstant)) {
         throw new IllegalArgumentException("IntConstant expected");
      } else {
         return v(this.value >>> ((IntConstant)c).value);
      }
   }

   public String toString() {
      return (new Long(this.value)).toString() + "L";
   }

   public Type getType() {
      return LongType.v();
   }

   public void apply(Switch sw) {
      ((ConstantSwitch)sw).caseLongConstant(this);
   }
}
