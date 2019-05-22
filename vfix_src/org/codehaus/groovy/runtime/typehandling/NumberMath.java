package org.codehaus.groovy.runtime.typehandling;

import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class NumberMath {
   public static Number abs(Number number) {
      return getMath(number).absImpl(number);
   }

   public static Number add(Number left, Number right) {
      return getMath(left, right).addImpl(left, right);
   }

   public static Number subtract(Number left, Number right) {
      return getMath(left, right).subtractImpl(left, right);
   }

   public static Number multiply(Number left, Number right) {
      return getMath(left, right).multiplyImpl(left, right);
   }

   public static Number divide(Number left, Number right) {
      return getMath(left, right).divideImpl(left, right);
   }

   public static int compareTo(Number left, Number right) {
      return getMath(left, right).compareToImpl(left, right);
   }

   public static Number or(Number left, Number right) {
      return getMath(left, right).orImpl(left, right);
   }

   public static Number and(Number left, Number right) {
      return getMath(left, right).andImpl(left, right);
   }

   public static Number xor(Number left, Number right) {
      return getMath(left, right).xorImpl(left, right);
   }

   public static Number intdiv(Number left, Number right) {
      return getMath(left, right).intdivImpl(left, right);
   }

   public static Number mod(Number left, Number right) {
      return getMath(left, right).modImpl(left, right);
   }

   public static Number leftShift(Number left, Number right) {
      if (!isFloatingPoint(right) && !isBigDecimal(right)) {
         return getMath(left).leftShiftImpl(left, right);
      } else {
         throw new UnsupportedOperationException("Shift distance must be an integral type, but " + right + " (" + right.getClass().getName() + ") was supplied");
      }
   }

   public static Number rightShift(Number left, Number right) {
      if (!isFloatingPoint(right) && !isBigDecimal(right)) {
         return getMath(left).rightShiftImpl(left, right);
      } else {
         throw new UnsupportedOperationException("Shift distance must be an integral type, but " + right + " (" + right.getClass().getName() + ") was supplied");
      }
   }

   public static Number rightShiftUnsigned(Number left, Number right) {
      if (!isFloatingPoint(right) && !isBigDecimal(right)) {
         return getMath(left).rightShiftUnsignedImpl(left, right);
      } else {
         throw new UnsupportedOperationException("Shift distance must be an integral type, but " + right + " (" + right.getClass().getName() + ") was supplied");
      }
   }

   public static Number unaryMinus(Number left) {
      return getMath(left).unaryMinusImpl(left);
   }

   public static boolean isFloatingPoint(Number number) {
      return number instanceof Double || number instanceof Float;
   }

   public static boolean isInteger(Number number) {
      return number instanceof Integer;
   }

   public static boolean isLong(Number number) {
      return number instanceof Long;
   }

   public static boolean isBigDecimal(Number number) {
      return number instanceof BigDecimal;
   }

   public static boolean isBigInteger(Number number) {
      return number instanceof BigInteger;
   }

   public static BigDecimal toBigDecimal(Number n) {
      return n instanceof BigDecimal ? (BigDecimal)n : new BigDecimal(n.toString());
   }

   public static BigInteger toBigInteger(Number n) {
      return n instanceof BigInteger ? (BigInteger)n : new BigInteger(n.toString());
   }

   public static NumberMath getMath(Number left, Number right) {
      if (!isFloatingPoint(left) && !isFloatingPoint(right)) {
         if (!isBigDecimal(left) && !isBigDecimal(right)) {
            if (!isBigInteger(left) && !isBigInteger(right)) {
               return (NumberMath)(!isLong(left) && !isLong(right) ? IntegerMath.INSTANCE : LongMath.INSTANCE);
            } else {
               return BigIntegerMath.INSTANCE;
            }
         } else {
            return BigDecimalMath.INSTANCE;
         }
      } else {
         return FloatingPointMath.INSTANCE;
      }
   }

   private static NumberMath getMath(Number number) {
      if (isLong(number)) {
         return LongMath.INSTANCE;
      } else if (isFloatingPoint(number)) {
         return FloatingPointMath.INSTANCE;
      } else if (isBigDecimal(number)) {
         return BigDecimalMath.INSTANCE;
      } else {
         return (NumberMath)(isBigInteger(number) ? BigIntegerMath.INSTANCE : IntegerMath.INSTANCE);
      }
   }

   protected abstract Number absImpl(Number var1);

   public abstract Number addImpl(Number var1, Number var2);

   public abstract Number subtractImpl(Number var1, Number var2);

   public abstract Number multiplyImpl(Number var1, Number var2);

   public abstract Number divideImpl(Number var1, Number var2);

   public abstract int compareToImpl(Number var1, Number var2);

   protected abstract Number unaryMinusImpl(Number var1);

   protected Number orImpl(Number left, Number right) {
      throw this.createUnsupportedException("or()", left);
   }

   protected Number andImpl(Number left, Number right) {
      throw this.createUnsupportedException("and()", left);
   }

   protected Number xorImpl(Number left, Number right) {
      throw this.createUnsupportedException("xor()", left);
   }

   protected Number modImpl(Number left, Number right) {
      throw this.createUnsupportedException("mod()", left);
   }

   protected Number intdivImpl(Number left, Number right) {
      throw this.createUnsupportedException("intdiv()", left);
   }

   protected Number leftShiftImpl(Number left, Number right) {
      throw this.createUnsupportedException("leftShift()", left);
   }

   protected Number rightShiftImpl(Number left, Number right) {
      throw this.createUnsupportedException("rightShift()", left);
   }

   protected Number rightShiftUnsignedImpl(Number left, Number right) {
      throw this.createUnsupportedException("rightShiftUnsigned()", left);
   }

   protected UnsupportedOperationException createUnsupportedException(String operation, Number left) {
      return new UnsupportedOperationException("Cannot use " + operation + " on this number type: " + left.getClass().getName() + " with value: " + left);
   }
}
