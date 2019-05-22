package fj.test;

import fj.F;
import java.util.HashMap;

public final class Variant {
   private static final HashMap<Variant.LongGen, Gen<?>> variantMemo = new HashMap();

   private Variant() {
      throw new UnsupportedOperationException();
   }

   public static <A> Gen<A> variant(final long n, final Gen<A> g) {
      Variant.LongGen p = new Variant.LongGen(n, g);
      final Gen<?> gx = (Gen)variantMemo.get(p);
      if (gx == null) {
         Gen<A> t = Gen.gen(new F<Integer, F<Rand, A>>() {
            public F<Rand, A> f(final Integer i) {
               return new F<Rand, A>() {
                  public A f(Rand r) {
                     return g.gen(i, r.reseed(n));
                  }
               };
            }
         });
         variantMemo.put(p, t);
         return t;
      } else {
         return Gen.gen(new F<Integer, F<Rand, A>>() {
            public F<Rand, A> f(final Integer i) {
               return new F<Rand, A>() {
                  public A f(Rand r) {
                     return gx.gen(i, r);
                  }
               };
            }
         });
      }
   }

   public static <A> F<Gen<A>, Gen<A>> variant(final long n) {
      return new F<Gen<A>, Gen<A>>() {
         public Gen<A> f(Gen<A> g) {
            return Variant.variant(n, g);
         }
      };
   }

   private static final class LongGen {
      private final long n;
      private final Gen<?> gen;

      LongGen(long n, Gen<?> gen) {
         this.n = n;
         this.gen = gen;
      }

      public boolean equals(Object o) {
         return o != null && o.getClass() == Variant.LongGen.class && this.n == ((Variant.LongGen)o).n && this.gen == ((Variant.LongGen)o).gen;
      }

      public int hashCode() {
         int p = true;
         int result = 239;
         int result = 419 * result + (int)(this.n ^ this.n >>> 32);
         result = 419 * result + this.gen.hashCode();
         return result;
      }
   }
}
