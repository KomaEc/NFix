package fj.function;

import fj.F;
import fj.F2;
import fj.Function;
import fj.Monoid;
import fj.Semigroup;
import fj.data.List;
import fj.data.Option;

public final class Integers {
   public static final F<Integer, F<Integer, Integer>> add;
   public static final F<Integer, F<Integer, Integer>> multiply;
   public static final F<Integer, F<Integer, Integer>> subtract;
   public static final F<Integer, Integer> negate;
   public static final F<Integer, Integer> abs;
   public static final F<Integer, F<Integer, Integer>> remainder;
   public static final F<Integer, F<Integer, Integer>> power;
   public static final F<Integer, Boolean> even;
   public static final F<Integer, Boolean> gtZero;
   public static final F<Integer, Boolean> gteZero;
   public static final F<Integer, Boolean> ltZero;
   public static final F<Integer, Boolean> lteZero;

   private Integers() {
      throw new UnsupportedOperationException();
   }

   public static int sum(List<Integer> ints) {
      return (Integer)Monoid.intAdditionMonoid.sumLeft(ints);
   }

   public static int product(List<Integer> ints) {
      return (Integer)Monoid.intMultiplicationMonoid.sumLeft(ints);
   }

   public static F<String, Option<Integer>> fromString() {
      return new F<String, Option<Integer>>() {
         public Option<Integer> f(String s) {
            try {
               return Option.some(Integer.valueOf(s));
            } catch (NumberFormatException var3) {
               return Option.none();
            }
         }
      };
   }

   static {
      add = Semigroup.intAdditionSemigroup.sum();
      multiply = Semigroup.intMultiplicationSemigroup.sum();
      subtract = Function.curry(new F2<Integer, Integer, Integer>() {
         public Integer f(Integer x, Integer y) {
            return x - y;
         }
      });
      negate = new F<Integer, Integer>() {
         public Integer f(Integer x) {
            return x * -1;
         }
      };
      abs = new F<Integer, Integer>() {
         public Integer f(Integer x) {
            return Math.abs(x);
         }
      };
      remainder = Function.curry(new F2<Integer, Integer, Integer>() {
         public Integer f(Integer a, Integer b) {
            return a % b;
         }
      });
      power = Function.curry(new F2<Integer, Integer, Integer>() {
         public Integer f(Integer a, Integer b) {
            return (int)StrictMath.pow((double)a, (double)b);
         }
      });
      even = new F<Integer, Boolean>() {
         public Boolean f(Integer i) {
            return i % 2 == 0;
         }
      };
      gtZero = new F<Integer, Boolean>() {
         public Boolean f(Integer i) {
            return i > 0;
         }
      };
      gteZero = new F<Integer, Boolean>() {
         public Boolean f(Integer i) {
            return i >= 0;
         }
      };
      ltZero = new F<Integer, Boolean>() {
         public Boolean f(Integer i) {
            return i < 0;
         }
      };
      lteZero = new F<Integer, Boolean>() {
         public Boolean f(Integer i) {
            return i <= 0;
         }
      };
   }
}
