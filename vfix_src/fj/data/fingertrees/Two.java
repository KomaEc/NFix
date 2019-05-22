package fj.data.fingertrees;

import fj.F;
import fj.data.vector.V2;

public final class Two<V, A> extends Digit<V, A> {
   private final V2<A> as;

   Two(Measured<V, A> m, V2<A> as) {
      super(m);
      this.as = as;
   }

   public <B> B foldRight(F<A, F<B, B>> aff, B z) {
      return ((F)aff.f(this.as._1())).f(((F)aff.f(this.as._2())).f(z));
   }

   public <B> B foldLeft(F<B, F<A, B>> bff, B z) {
      return this.as.toStream().foldLeft(bff, z);
   }

   public <B> B match(F<One<V, A>, B> one, F<Two<V, A>, B> two, F<Three<V, A>, B> three, F<Four<V, A>, B> four) {
      return two.f(this);
   }

   public V2<A> values() {
      return this.as;
   }
}
