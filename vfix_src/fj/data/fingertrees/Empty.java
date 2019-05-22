package fj.data.fingertrees;

import fj.Bottom;
import fj.F;
import fj.P2;

public final class Empty<V, A> extends FingerTree<V, A> {
   Empty(Measured<V, A> m) {
      super(m);
   }

   public FingerTree<V, A> cons(A a) {
      return new Single(this.measured(), a);
   }

   public FingerTree<V, A> snoc(A a) {
      return this.cons(a);
   }

   public FingerTree<V, A> append(FingerTree<V, A> t) {
      return t;
   }

   public P2<Integer, A> lookup(F<V, Integer> o, int i) {
      throw Bottom.error("Lookup of empty tree.");
   }

   public <B> B foldRight(F<A, F<B, B>> aff, B z) {
      return z;
   }

   public A reduceRight(F<A, F<A, A>> aff) {
      throw Bottom.error("Reduction of empty tree");
   }

   public <B> B foldLeft(F<B, F<A, B>> bff, B z) {
      return z;
   }

   public A reduceLeft(F<A, F<A, A>> aff) {
      throw Bottom.error("Reduction of empty tree");
   }

   public <B> FingerTree<V, B> map(F<A, B> abf, Measured<V, B> m) {
      return new Empty(m);
   }

   public V measure() {
      return this.measured().zero();
   }

   public <B> B match(F<Empty<V, A>, B> empty, F<Single<V, A>, B> single, F<Deep<V, A>, B> deep) {
      return empty.f(this);
   }
}
