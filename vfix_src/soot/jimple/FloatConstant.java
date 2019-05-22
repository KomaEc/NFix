package soot.jimple;

import soot.FloatType;
import soot.Type;
import soot.util.Switch;

public class FloatConstant extends RealConstant {
   public final float value;

   private FloatConstant(float value) {
      this.value = value;
   }

   public static FloatConstant v(float value) {
      return new FloatConstant(value);
   }

   public boolean equals(Object c) {
      return c instanceof FloatConstant && Float.compare(((FloatConstant)c).value, this.value) == 0;
   }

   public int hashCode() {
      return Float.floatToIntBits(this.value);
   }

   public NumericConstant add(NumericConstant c) {
      this.assertInstanceOf(c);
      return v(this.value + ((FloatConstant)c).value);
   }

   public NumericConstant subtract(NumericConstant c) {
      this.assertInstanceOf(c);
      return v(this.value - ((FloatConstant)c).value);
   }

   public NumericConstant multiply(NumericConstant c) {
      this.assertInstanceOf(c);
      return v(this.value * ((FloatConstant)c).value);
   }

   public NumericConstant divide(NumericConstant c) {
      this.assertInstanceOf(c);
      return v(this.value / ((FloatConstant)c).value);
   }

   public NumericConstant remainder(NumericConstant c) {
      this.assertInstanceOf(c);
      return v(this.value % ((FloatConstant)c).value);
   }

   public NumericConstant equalEqual(NumericConstant c) {
      this.assertInstanceOf(c);
      return IntConstant.v(Float.compare(this.value, ((FloatConstant)c).value) == 0 ? 1 : 0);
   }

   public NumericConstant notEqual(NumericConstant c) {
      this.assertInstanceOf(c);
      return IntConstant.v(Float.compare(this.value, ((FloatConstant)c).value) != 0 ? 1 : 0);
   }

   public NumericConstant lessThan(NumericConstant c) {
      this.assertInstanceOf(c);
      return IntConstant.v(Float.compare(this.value, ((FloatConstant)c).value) < 0 ? 1 : 0);
   }

   public NumericConstant lessThanOrEqual(NumericConstant c) {
      this.assertInstanceOf(c);
      return IntConstant.v(Float.compare(this.value, ((FloatConstant)c).value) <= 0 ? 1 : 0);
   }

   public NumericConstant greaterThan(NumericConstant c) {
      this.assertInstanceOf(c);
      return IntConstant.v(Float.compare(this.value, ((FloatConstant)c).value) > 0 ? 1 : 0);
   }

   public NumericConstant greaterThanOrEqual(NumericConstant c) {
      this.assertInstanceOf(c);
      return IntConstant.v(Float.compare(this.value, ((FloatConstant)c).value) >= 0 ? 1 : 0);
   }

   public IntConstant cmpg(RealConstant constant) {
      this.assertInstanceOf(constant);
      float cValue = ((FloatConstant)constant).value;
      if (this.value < cValue) {
         return IntConstant.v(-1);
      } else {
         return this.value == cValue ? IntConstant.v(0) : IntConstant.v(1);
      }
   }

   public IntConstant cmpl(RealConstant constant) {
      this.assertInstanceOf(constant);
      float cValue = ((FloatConstant)constant).value;
      if (this.value > cValue) {
         return IntConstant.v(1);
      } else {
         return this.value == cValue ? IntConstant.v(0) : IntConstant.v(-1);
      }
   }

   public NumericConstant negate() {
      return v(-this.value);
   }

   public String toString() {
      String floatString = Float.toString(this.value);
      return !floatString.equals("NaN") && !floatString.equals("Infinity") && !floatString.equals("-Infinity") ? floatString + "F" : "#" + floatString + "F";
   }

   public Type getType() {
      return FloatType.v();
   }

   public void apply(Switch sw) {
      ((ConstantSwitch)sw).caseFloatConstant(this);
   }

   private void assertInstanceOf(NumericConstant constant) {
      if (!(constant instanceof FloatConstant)) {
         throw new IllegalArgumentException("FloatConstant expected");
      }
   }
}
