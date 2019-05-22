package fj.function;

import fj.F;
import fj.F2;
import fj.Function;
import fj.Monoid;
import fj.Semigroup;
import fj.data.List;
import fj.data.Option;

public final class Doubles {
   public static final F<Double, F<Double, Double>> add;
   public static final F<Double, F<Double, Double>> multiply;
   public static final F<Double, F<Double, Double>> subtract;
   public static final F<Double, Double> negate;
   public static final F<Double, Double> abs;
   public static final F<Double, F<Double, Double>> remainder;
   public static final F<Double, F<Double, Double>> power;
   public static final F<Double, Boolean> even;
   public static final F<Double, Boolean> gtZero;
   public static final F<Double, Boolean> gteZero;
   public static final F<Double, Boolean> ltZero;
   public static final F<Double, Boolean> lteZero;

   private Doubles() {
      throw new UnsupportedOperationException();
   }

   public static double sum(List<Double> doubles) {
      return (Double)Monoid.doubleAdditionMonoid.sumLeft(doubles);
   }

   public static double product(List<Double> doubles) {
      return (Double)Monoid.doubleMultiplicationMonoid.sumLeft(doubles);
   }

   public static F<String, Option<Double>> fromString() {
      return new F<String, Option<Double>>() {
         public Option<Double> f(String s) {
            try {
               return Option.some(Double.valueOf(s));
            } catch (NumberFormatException var3) {
               return Option.none();
            }
         }
      };
   }

   static {
      add = Semigroup.doubleAdditionSemigroup.sum();
      multiply = Semigroup.doubleMultiplicationSemigroup.sum();
      subtract = Function.curry(new F2<Double, Double, Double>() {
         public Double f(Double x, Double y) {
            return x - y;
         }
      });
      negate = new F<Double, Double>() {
         public Double f(Double x) {
            return x * -1.0D;
         }
      };
      abs = new F<Double, Double>() {
         public Double f(Double x) {
            return Math.abs(x);
         }
      };
      remainder = Function.curry(new F2<Double, Double, Double>() {
         public Double f(Double a, Double b) {
            return a % b;
         }
      });
      power = Function.curry(new F2<Double, Double, Double>() {
         public Double f(Double a, Double b) {
            return StrictMath.pow(a, b);
         }
      });
      even = new F<Double, Boolean>() {
         public Boolean f(Double i) {
            return i % 2.0D == 0.0D;
         }
      };
      gtZero = new F<Double, Boolean>() {
         public Boolean f(Double i) {
            return Double.compare(i, 0.0D) > 0;
         }
      };
      gteZero = new F<Double, Boolean>() {
         public Boolean f(Double i) {
            return Double.compare(i, 0.0D) >= 0;
         }
      };
      ltZero = new F<Double, Boolean>() {
         public Boolean f(Double i) {
            return Double.compare(i, 0.0D) < 0;
         }
      };
      lteZero = new F<Double, Boolean>() {
         public Boolean f(Double i) {
            return Double.compare(i, 0.0D) <= 0;
         }
      };
   }
}
