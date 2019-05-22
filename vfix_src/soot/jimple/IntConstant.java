package soot.jimple;

import soot.IntType;
import soot.Type;
import soot.util.Switch;

public class IntConstant extends ArithmeticConstant {
   public final int value;

   protected IntConstant(int value) {
      this.value = value;
   }

   public static IntConstant v(int value) {
      return new IntConstant(value);
   }

   public boolean equals(Object c) {
      return c instanceof IntConstant && ((IntConstant)c).value == this.value;
   }

   public int hashCode() {
      return this.value;
   }

   public NumericConstant add(NumericConstant c) {
      if (!(c instanceof IntConstant)) {
         throw new IllegalArgumentException("IntConstant expected");
      } else {
         return v(this.value + ((IntConstant)c).value);
      }
   }

   public NumericConstant subtract(NumericConstant c) {
      if (!(c instanceof IntConstant)) {
         throw new IllegalArgumentException("IntConstant expected");
      } else {
         return v(this.value - ((IntConstant)c).value);
      }
   }

   public NumericConstant multiply(NumericConstant c) {
      if (!(c instanceof IntConstant)) {
         throw new IllegalArgumentException("IntConstant expected");
      } else {
         return v(this.value * ((IntConstant)c).value);
      }
   }

   public NumericConstant divide(NumericConstant c) {
      if (!(c instanceof IntConstant)) {
         throw new IllegalArgumentException("IntConstant expected");
      } else {
         return v(this.value / ((IntConstant)c).value);
      }
   }

   public NumericConstant remainder(NumericConstant c) {
      if (!(c instanceof IntConstant)) {
         throw new IllegalArgumentException("IntConstant expected");
      } else {
         return v(this.value % ((IntConstant)c).value);
      }
   }

   public NumericConstant equalEqual(NumericConstant c) {
      if (!(c instanceof IntConstant)) {
         throw new IllegalArgumentException("IntConstant expected");
      } else {
         return v(this.value == ((IntConstant)c).value ? 1 : 0);
      }
   }

   public NumericConstant notEqual(NumericConstant c) {
      if (!(c instanceof IntConstant)) {
         throw new IllegalArgumentException("IntConstant expected");
      } else {
         return v(this.value != ((IntConstant)c).value ? 1 : 0);
      }
   }

   public NumericConstant lessThan(NumericConstant c) {
      if (!(c instanceof IntConstant)) {
         throw new IllegalArgumentException("IntConstant expected");
      } else {
         return v(this.value < ((IntConstant)c).value ? 1 : 0);
      }
   }

   public NumericConstant lessThanOrEqual(NumericConstant c) {
      if (!(c instanceof IntConstant)) {
         throw new IllegalArgumentException("IntConstant expected");
      } else {
         return v(this.value <= ((IntConstant)c).value ? 1 : 0);
      }
   }

   public NumericConstant greaterThan(NumericConstant c) {
      if (!(c instanceof IntConstant)) {
         throw new IllegalArgumentException("IntConstant expected");
      } else {
         return v(this.value > ((IntConstant)c).value ? 1 : 0);
      }
   }

   public NumericConstant greaterThanOrEqual(NumericConstant c) {
      if (!(c instanceof IntConstant)) {
         throw new IllegalArgumentException("IntConstant expected");
      } else {
         return v(this.value >= ((IntConstant)c).value ? 1 : 0);
      }
   }

   public NumericConstant negate() {
      return v(-this.value);
   }

   public ArithmeticConstant and(ArithmeticConstant c) {
      if (!(c instanceof IntConstant)) {
         throw new IllegalArgumentException("IntConstant expected");
      } else {
         return v(this.value & ((IntConstant)c).value);
      }
   }

   public ArithmeticConstant or(ArithmeticConstant c) {
      if (!(c instanceof IntConstant)) {
         throw new IllegalArgumentException("IntConstant expected");
      } else {
         return v(this.value | ((IntConstant)c).value);
      }
   }

   public ArithmeticConstant xor(ArithmeticConstant c) {
      if (!(c instanceof IntConstant)) {
         throw new IllegalArgumentException("IntConstant expected");
      } else {
         return v(this.value ^ ((IntConstant)c).value);
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
      return (new Integer(this.value)).toString();
   }

   public Type getType() {
      return IntType.v();
   }

   public void apply(Switch sw) {
      ((ConstantSwitch)sw).caseIntConstant(this);
   }
}
