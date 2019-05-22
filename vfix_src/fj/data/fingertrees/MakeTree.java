package fj.data.fingertrees;

import fj.data.vector.V;
import fj.data.vector.V2;
import fj.data.vector.V3;

public final class MakeTree<V, A> {
   private final Measured<V, A> m;

   MakeTree(Measured<V, A> m) {
      this.m = m;
   }

   public FingerTree<V, A> empty() {
      return new Empty(this.m);
   }

   public FingerTree<V, A> single(A a) {
      return new Single(this.m, a);
   }

   public FingerTree<V, A> deep(Digit<V, A> prefix, FingerTree<V, Node<V, A>> middle, Digit<V, A> suffix) {
      return this.deep(this.m.sum(prefix.measure(), this.m.sum(middle.measure(), suffix.measure())), prefix, middle, suffix);
   }

   public FingerTree<V, A> deep(V v, Digit<V, A> prefix, FingerTree<V, Node<V, A>> middle, Digit<V, A> suffix) {
      return new Deep(this.m, v, prefix, middle, suffix);
   }

   public One<V, A> one(A a) {
      return new One(this.m, a);
   }

   public Two<V, A> two(A a, A b) {
      return new Two(this.m, V.v(a, b));
   }

   public Three<V, A> three(A a, A b, A c) {
      return new Three(this.m, V.v(a, b, c));
   }

   public Four<V, A> four(A a, A b, A c, A d) {
      return new Four(this.m, V.v(a, b, c, d));
   }

   public Node2<V, A> node2(A a, A b) {
      return new Node2(this.m, V.v(a, b));
   }

   public Node3<V, A> node3(A a, A b, A c) {
      return new Node3(this.m, V.v(a, b, c));
   }

   public Node2<V, A> node2(V2<A> v) {
      return new Node2(this.m, v);
   }

   public Node3<V, A> node3(V3<A> v) {
      return new Node3(this.m, v);
   }
}
