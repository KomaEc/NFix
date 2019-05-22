package fj.data.fingertrees;

import fj.F;
import fj.P;
import fj.P2;

public final class Single<V, A> extends FingerTree<V, A> {
   private final A a;
   private final V v;

   Single(Measured<V, A> m, A a) {
      super(m);
      this.a = a;
      this.v = m.measure(a);
   }

   public <B> B foldRight(F<A, F<B, B>> aff, B z) {
      return ((F)aff.f(this.a)).f(z);
   }

   public A reduceRight(F<A, F<A, A>> aff) {
      return this.a;
   }

   public <B> B foldLeft(F<B, F<A, B>> bff, B z) {
      return ((F)bff.f(z)).f(this.a);
   }

   public A reduceLeft(F<A, F<A, A>> aff) {
      return this.a;
   }

   public <B> FingerTree<V, B> map(F<A, B> abf, Measured<V, B> m) {
      return new Single(m, abf.f(this.a));
   }

   public V measure() {
      return this.v;
   }

   public <B> B match(F<Empty<V, A>, B> empty, F<Single<V, A>, B> single, F<Deep<V, A>, B> deep) {
      return single.f(this);
   }

   public FingerTree<V, A> cons(A b) {
      MakeTree<V, A> mk = mkTree(this.measured());
      return mk.deep(mk.one(b), new Empty(this.measured().nodeMeasured()), mk.one(this.a));
   }

   public FingerTree<V, A> snoc(A b) {
      MakeTree<V, A> mk = mkTree(this.measured());
      return mk.deep(mk.one(this.a), new Empty(this.measured().nodeMeasured()), mk.one(b));
   }

   public FingerTree<V, A> append(FingerTree<V, A> t) {
      return t.cons(this.a);
   }

   public P2<Integer, A> lookup(F<V, Integer> o, int i) {
      return P.p(i, this.a);
   }

   public A value() {
      return this.a;
   }
}
