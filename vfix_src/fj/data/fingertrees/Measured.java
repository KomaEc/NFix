package fj.data.fingertrees;

import fj.F;
import fj.Monoid;

public final class Measured<V, A> {
   private final Monoid<V> m;
   private final F<A, V> measure;

   private Measured(Monoid<V> m, F<A, V> measure) {
      this.m = m;
      this.measure = measure;
   }

   public static <V, A> Measured<V, A> measured(Monoid<V> m, F<A, V> measure) {
      return new Measured(m, measure);
   }

   public Monoid<V> monoid() {
      return this.m;
   }

   public F<A, V> measure() {
      return this.measure;
   }

   public V measure(A a) {
      return this.measure.f(a);
   }

   public V sum(V a, V b) {
      return this.m.sum(a, b);
   }

   public V zero() {
      return this.m.zero();
   }

   public Measured<V, Node<V, A>> nodeMeasured() {
      return new Measured(this.m, new F<Node<V, A>, V>() {
         public V f(Node<V, A> node) {
            return node.measure();
         }
      });
   }

   public Measured<V, Digit<V, A>> digitMeasured() {
      return new Measured(this.m, new F<Digit<V, A>, V>() {
         public V f(Digit<V, A> d) {
            return d.measure();
         }
      });
   }
}
