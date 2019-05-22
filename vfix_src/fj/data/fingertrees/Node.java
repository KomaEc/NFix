package fj.data.fingertrees;

import fj.F;
import fj.F2;
import fj.Function;
import fj.P2;

public abstract class Node<V, A> {
   private final Measured<V, A> m;
   private final V measure;

   public abstract <B> B foldRight(F<A, F<B, B>> var1, B var2);

   public abstract <B> B foldLeft(F<B, F<A, B>> var1, B var2);

   public static <V, A, B> F<B, F<Node<V, A>, B>> foldLeft_(final F<B, F<A, B>> bff) {
      return Function.curry(new F2<B, Node<V, A>, B>() {
         public B f(B b, Node<V, A> node) {
            return node.foldLeft(bff, b);
         }
      });
   }

   public static <V, A, B> F<B, F<Node<V, A>, B>> foldRight_(final F<A, F<B, B>> aff) {
      return Function.curry(new F2<B, Node<V, A>, B>() {
         public B f(B b, Node<V, A> node) {
            return node.foldRight(aff, b);
         }
      });
   }

   public final <B> Node<V, B> map(final F<A, B> f, final Measured<V, B> m) {
      return (Node)this.match(new F<Node2<V, A>, Node<V, B>>() {
         public Node<V, B> f(Node2<V, A> node2) {
            return new Node2(m, node2.toVector().map(f));
         }
      }, new F<Node3<V, A>, Node<V, B>>() {
         public Node<V, B> f(Node3<V, A> node3) {
            return new Node3(m, node3.toVector().map(f));
         }
      });
   }

   public static <V, A, B> F<Node<V, A>, Node<V, B>> liftM(final F<A, B> f, final Measured<V, B> m) {
      return new F<Node<V, A>, Node<V, B>>() {
         public Node<V, B> f(Node<V, A> node) {
            return node.map(f, m);
         }
      };
   }

   public abstract Digit<V, A> toDigit();

   Node(Measured<V, A> m, V measure) {
      this.m = m;
      this.measure = measure;
   }

   public final V measure() {
      return this.measure;
   }

   Measured<V, A> measured() {
      return this.m;
   }

   public abstract P2<Integer, A> lookup(F<V, Integer> var1, int var2);

   public abstract <B> B match(F<Node2<V, A>, B> var1, F<Node3<V, A>, B> var2);
}
