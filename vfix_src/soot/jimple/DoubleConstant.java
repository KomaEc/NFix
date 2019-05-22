package soot.jimple;

import soot.DoubleType;
import soot.Type;
import soot.util.Switch;

public class DoubleConstant extends RealConstant {
   public final double value;

   private DoubleConstant(double value) {
      this.value = value;
   }

   public static DoubleConstant v(double value) {
      return new DoubleConstant(value);
   }

   public boolean equals(Object c) {
      return c instanceof DoubleConstant && Double.compare(((DoubleConstant)c).value, this.value) == 0;
   }

   public int hashCode() {
      long v = Double.doubleToLongBits(this.value);
      return (int)(v ^ v >>> 32);
   }

   public NumericConstant add(NumericConstant c) {
      this.assertInstanceOf(c);
      return v(this.value + ((DoubleConstant)c).value);
   }

   public NumericConstant subtract(NumericConstant c) {
      this.assertInstanceOf(c);
      return v(this.value - ((DoubleConstant)c).value);
   }

   public NumericConstant multiply(NumericConstant c) {
      this.assertInstanceOf(c);
      return v(this.value * ((DoubleConstant)c).value);
   }

   public NumericConstant divide(NumericConstant c) {
      this.assertInstanceOf(c);
      return v(this.value / ((DoubleConstant)c).value);
   }

   public NumericConstant remainder(NumericConstant c) {
      this.assertInstanceOf(c);
      return v(this.value % ((DoubleConstant)c).value);
   }

   public NumericConstant equalEqual(NumericConstant c) {
      this.assertInstanceOf(c);
      return IntConstant.v(Double.compare(this.value, ((DoubleConstant)c).value) == 0 ? 1 : 0);
   }

   public NumericConstant notEqual(NumericConstant c) {
      this.assertInstanceOf(c);
      return IntConstant.v(Double.compare(this.value, ((DoubleConstant)c).value) != 0 ? 1 : 0);
   }

   public NumericConstant lessThan(NumericConstant c) {
      this.assertInstanceOf(c);
      return IntConstant.v(Double.compare(this.value, ((DoubleConstant)c).value) < 0 ? 1 : 0);
   }

   public NumericConstant lessThanOrEqual(NumericConstant c) {
      this.assertInstanceOf(c);
      return IntConstant.v(Double.compare(this.value, ((DoubleConstant)c).value) <= 0 ? 1 : 0);
   }

   public NumericConstant greaterThan(NumericConstant c) {
      this.assertInstanceOf(c);
      return IntConstant.v(Double.compare(this.value, ((DoubleConstant)c).value) > 0 ? 1 : 0);
   }

   public NumericConstant greaterThanOrEqual(NumericConstant c) {
      this.assertInstanceOf(c);
      return IntConstant.v(Double.compare(this.value, ((DoubleConstant)c).value) >= 0 ? 1 : 0);
   }

   public IntConstant cmpg(RealConstant constant) {
      this.assertInstanceOf(constant);
      double cValue = ((DoubleConstant)constant).value;
      if (this.value < cValue) {
         return IntConstant.v(-1);
      } else {
         return this.value == cValue ? IntConstant.v(0) : IntConstant.v(1);
      }
   }

   public IntConstant cmpl(RealConstant constant) {
      this.assertInstanceOf(constant);
      double cValue = ((DoubleConstant)constant).value;
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
      String doubleString = Double.toString(this.value);
      return !doubleString.equals("NaN") && !doubleString.equals("Infinity") && !doubleString.equals("-Infinity") ? doubleString : "#" + doubleString;
   }

   public Type getType() {
      return DoubleType.v();
   }

   public void apply(Switch sw) {
      ((ConstantSwitch)sw).caseDoubleConstant(this);
   }

   private void assertInstanceOf(NumericConstant constant) {
      if (!(constant instanceof DoubleConstant)) {
         throw new IllegalArgumentException("DoubleConstant expected");
      }
   }
}
