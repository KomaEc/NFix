package fj.test;

import fj.F;
import fj.data.Option;
import java.util.Random;

public final class Rand {
   private final F<Option<Long>, F<Integer, F<Integer, Integer>>> f;
   private final F<Option<Long>, F<Double, F<Double, Double>>> g;
   private static final F<Long, Random> fr = new F<Long, Random>() {
      public Random f(Long x) {
         return new Random(x);
      }
   };
   public static final Rand standard = new Rand(new F<Option<Long>, F<Integer, F<Integer, Integer>>>() {
      public F<Integer, F<Integer, Integer>> f(final Option<Long> seed) {
         return new F<Integer, F<Integer, Integer>>() {
            public F<Integer, Integer> f(final Integer from) {
               return new F<Integer, Integer>() {
                  public Integer f(Integer to) {
                     int f = Math.min(from, to);
                     int t = Math.max(from, to);
                     return f + ((Random)seed.map(Rand.fr).orSome((Object)(new Random()))).nextInt(t - f + 1);
                  }
               };
            }
         };
      }
   }, new F<Option<Long>, F<Double, F<Double, Double>>>() {
      public F<Double, F<Double, Double>> f(final Option<Long> seed) {
         return new F<Double, F<Double, Double>>() {
            public F<Double, Double> f(final Double from) {
               return new F<Double, Double>() {
                  public Double f(Double to) {
                     double f = Math.min(from, to);
                     double t = Math.max(from, to);
                     return ((Random)seed.map(Rand.fr).orSome((Object)(new Random()))).nextDouble() * (t - f) + f;
                  }
               };
            }
         };
      }
   });

   private Rand(F<Option<Long>, F<Integer, F<Integer, Integer>>> f, F<Option<Long>, F<Double, F<Double, Double>>> g) {
      this.f = f;
      this.g = g;
   }

   public int choose(long seed, int from, int to) {
      return (Integer)((F)((F)this.f.f(Option.some(seed))).f(from)).f(to);
   }

   public int choose(int from, int to) {
      return (Integer)((F)((F)this.f.f(Option.none())).f(from)).f(to);
   }

   public double choose(long seed, double from, double to) {
      return (Double)((F)((F)this.g.f(Option.some(seed))).f(from)).f(to);
   }

   public double choose(double from, double to) {
      return (Double)((F)((F)this.g.f(Option.none())).f(from)).f(to);
   }

   public Rand reseed(final long seed) {
      return new Rand(new F<Option<Long>, F<Integer, F<Integer, Integer>>>() {
         public F<Integer, F<Integer, Integer>> f(Option<Long> old) {
            return new F<Integer, F<Integer, Integer>>() {
               public F<Integer, Integer> f(final Integer from) {
                  return new F<Integer, Integer>() {
                     public Integer f(Integer to) {
                        return (Integer)((F)((F)Rand.this.f.f(Option.some(seed))).f(from)).f(to);
                     }
                  };
               }
            };
         }
      }, new F<Option<Long>, F<Double, F<Double, Double>>>() {
         public F<Double, F<Double, Double>> f(Option<Long> old) {
            return new F<Double, F<Double, Double>>() {
               public F<Double, Double> f(final Double from) {
                  return new F<Double, Double>() {
                     public Double f(Double to) {
                        return (Double)((F)((F)Rand.this.g.f(Option.some(seed))).f(from)).f(to);
                     }
                  };
               }
            };
         }
      });
   }

   public static Rand rand(F<Option<Long>, F<Integer, F<Integer, Integer>>> f, F<Option<Long>, F<Double, F<Double, Double>>> g) {
      return new Rand(f, g);
   }
}
