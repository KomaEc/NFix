package fj.function;

import fj.F;
import fj.F2;
import fj.Function;
import fj.Semigroup;

public final class Longs {
   public static final F<Long, F<Long, Long>> add;
   public static final F<Long, F<Long, Long>> multiply;
   public static final F<Long, F<Long, Long>> subtract;
   public static final F<Long, Long> negate;
   public static final F<Long, Long> abs;
   public static final F<Long, F<Long, Long>> remainder;

   private Longs() {
      throw new UnsupportedOperationException();
   }

   static {
      add = Semigroup.longAdditionSemigroup.sum();
      multiply = Semigroup.longMultiplicationSemigroup.sum();
      subtract = Function.curry(new F2<Long, Long, Long>() {
         public Long f(Long x, Long y) {
            return x - y;
         }
      });
      negate = new F<Long, Long>() {
         public Long f(Long x) {
            return x * -1L;
         }
      };
      abs = new F<Long, Long>() {
         public Long f(Long x) {
            return Math.abs(x);
         }
      };
      remainder = Function.curry(new F2<Long, Long, Long>() {
         public Long f(Long a, Long b) {
            return a % b;
         }
      });
   }
}
