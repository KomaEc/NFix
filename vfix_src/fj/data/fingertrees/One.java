package fj.data.fingertrees;

import fj.F;

public final class One<V, A> extends Digit<V, A> {
   private final A a;

   One(Measured<V, A> m, A a) {
      super(m);
      this.a = a;
   }

   public <B> B foldRight(F<A, F<B, B>> aff, B z) {
      return ((F)aff.f(this.a)).f(z);
   }

   public <B> B foldLeft(F<B, F<A, B>> bff, B z) {
      return ((F)bff.f(z)).f(this.a);
   }

   public <B> B match(F<One<V, A>, B> one, F<Two<V, A>, B> two, F<Three<V, A>, B> three, F<Four<V, A>, B> four) {
      return one.f(this);
   }

   public A value() {
      return this.a;
   }
}
