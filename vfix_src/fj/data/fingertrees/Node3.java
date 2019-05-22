package fj.data.fingertrees;

import fj.F;
import fj.P2;
import fj.data.vector.V3;

public final class Node3<V, A> extends Node<V, A> {
   private final V3<A> as;

   Node3(Measured<V, A> m, V3<A> as) {
      super(m, m.sum(m.measure(as._1()), m.sum(m.measure(as._2()), m.measure(as._3()))));
      this.as = as;
   }

   public <B> B foldRight(F<A, F<B, B>> aff, B z) {
      return ((F)aff.f(this.as._1())).f(((F)aff.f(this.as._2())).f(((F)aff.f(this.as._3())).f(z)));
   }

   public <B> B foldLeft(F<B, F<A, B>> bff, B z) {
      return ((F)bff.f(((F)bff.f(((F)bff.f(z)).f(this.as._1()))).f(this.as._2()))).f(this.as._3());
   }

   public <B> B match(F<Node2<V, A>, B> n2, F<Node3<V, A>, B> n3) {
      return n3.f(this);
   }

   public Digit<V, A> toDigit() {
      return new Three(this.measured(), this.as);
   }

   public P2<Integer, A> lookup(F<V, Integer> o, int i) {
      return null;
   }

   public V3<A> toVector() {
      return this.as;
   }
}
