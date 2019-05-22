package fj.function;

import fj.F;
import fj.F2;
import fj.Function;
import fj.Monoid;
import fj.data.List;
import java.math.BigInteger;

public final class BigIntegers {
   public static final F<BigInteger, F<BigInteger, BigInteger>> add = Function.curry(new F2<BigInteger, BigInteger, BigInteger>() {
      public BigInteger f(BigInteger a1, BigInteger a2) {
         return a1.add(a2);
      }
   });
   public static final F<BigInteger, F<BigInteger, BigInteger>> multiply = Function.curry(new F2<BigInteger, BigInteger, BigInteger>() {
      public BigInteger f(BigInteger a1, BigInteger a2) {
         return a1.multiply(a2);
      }
   });
   public static final F<BigInteger, F<BigInteger, BigInteger>> subtract = Function.curry(new F2<BigInteger, BigInteger, BigInteger>() {
      public BigInteger f(BigInteger a1, BigInteger a2) {
         return a1.subtract(a2);
      }
   });
   public static final F<BigInteger, BigInteger> negate = new F<BigInteger, BigInteger>() {
      public BigInteger f(BigInteger i) {
         return i.negate();
      }
   };
   public static final F<BigInteger, BigInteger> abs = new F<BigInteger, BigInteger>() {
      public BigInteger f(BigInteger i) {
         return i.abs();
      }
   };
   public static final F<BigInteger, F<BigInteger, BigInteger>> remainder = Function.curry(new F2<BigInteger, BigInteger, BigInteger>() {
      public BigInteger f(BigInteger a1, BigInteger a2) {
         return a1.remainder(a2);
      }
   });
   public static final F<BigInteger, F<Integer, BigInteger>> power = Function.curry(new F2<BigInteger, Integer, BigInteger>() {
      public BigInteger f(BigInteger a1, Integer a2) {
         return a1.pow(a2);
      }
   });

   private BigIntegers() {
      throw new UnsupportedOperationException();
   }

   public static BigInteger sum(List<BigInteger> ints) {
      return (BigInteger)Monoid.bigintAdditionMonoid.sumLeft(ints);
   }

   public static BigInteger product(List<BigInteger> ints) {
      return (BigInteger)Monoid.bigintMultiplicationMonoid.sumLeft(ints);
   }
}
