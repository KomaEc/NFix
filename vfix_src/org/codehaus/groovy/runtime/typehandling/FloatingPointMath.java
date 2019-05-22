package org.codehaus.groovy.runtime.typehandling;

public final class FloatingPointMath extends NumberMath {
   public static final FloatingPointMath INSTANCE = new FloatingPointMath();

   private FloatingPointMath() {
   }

   protected Number absImpl(Number number) {
      return new Double(Math.abs(number.doubleValue()));
   }

   public Number addImpl(Number left, Number right) {
      return new Double(left.doubleValue() + right.doubleValue());
   }

   public Number subtractImpl(Number left, Number right) {
      return new Double(left.doubleValue() - right.doubleValue());
   }

   public Number multiplyImpl(Number left, Number right) {
      return new Double(left.doubleValue() * right.doubleValue());
   }

   public Number divideImpl(Number left, Number right) {
      return new Double(left.doubleValue() / right.doubleValue());
   }

   public int compareToImpl(Number left, Number right) {
      return Double.compare(left.doubleValue(), right.doubleValue());
   }

   protected Number modImpl(Number left, Number right) {
      return new Double(left.doubleValue() % right.doubleValue());
   }

   protected Number unaryMinusImpl(Number left) {
      return new Double(-left.doubleValue());
   }
}
