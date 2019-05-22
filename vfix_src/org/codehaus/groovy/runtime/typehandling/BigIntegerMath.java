package org.codehaus.groovy.runtime.typehandling;

public final class BigIntegerMath extends NumberMath {
   public static final BigIntegerMath INSTANCE = new BigIntegerMath();

   private BigIntegerMath() {
   }

   protected Number absImpl(Number number) {
      return toBigInteger(number).abs();
   }

   public Number addImpl(Number left, Number right) {
      return toBigInteger(left).add(toBigInteger(right));
   }

   public Number subtractImpl(Number left, Number right) {
      return toBigInteger(left).subtract(toBigInteger(right));
   }

   public Number multiplyImpl(Number left, Number right) {
      return toBigInteger(left).multiply(toBigInteger(right));
   }

   public Number divideImpl(Number left, Number right) {
      return BigDecimalMath.INSTANCE.divideImpl(left, right);
   }

   public int compareToImpl(Number left, Number right) {
      return toBigInteger(left).compareTo(toBigInteger(right));
   }

   protected Number intdivImpl(Number left, Number right) {
      return toBigInteger(left).divide(toBigInteger(right));
   }

   protected Number modImpl(Number left, Number right) {
      return toBigInteger(left).mod(toBigInteger(right));
   }

   protected Number unaryMinusImpl(Number left) {
      return toBigInteger(left).negate();
   }

   protected Number bitwiseNegateImpl(Number left) {
      return toBigInteger(left).not();
   }

   protected Number orImpl(Number left, Number right) {
      return toBigInteger(left).or(toBigInteger(right));
   }

   protected Number andImpl(Number left, Number right) {
      return toBigInteger(left).and(toBigInteger(right));
   }

   protected Number xorImpl(Number left, Number right) {
      return toBigInteger(left).xor(toBigInteger(right));
   }
}
