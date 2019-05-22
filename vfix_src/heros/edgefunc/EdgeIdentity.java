package heros.edgefunc;

import heros.EdgeFunction;

public class EdgeIdentity<V> implements EdgeFunction<V> {
   private static final EdgeIdentity instance = new EdgeIdentity();

   private EdgeIdentity() {
   }

   public V computeTarget(V source) {
      return source;
   }

   public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {
      return secondFunction;
   }

   public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {
      if (otherFunction != this && !otherFunction.equalTo(this)) {
         if (otherFunction instanceof AllBottom) {
            return otherFunction;
         } else {
            return (EdgeFunction)(otherFunction instanceof AllTop ? this : otherFunction.joinWith(this));
         }
      } else {
         return this;
      }
   }

   public boolean equalTo(EdgeFunction<V> other) {
      return other == this;
   }

   public static <A> EdgeIdentity<A> v() {
      return instance;
   }

   public String toString() {
      return "id";
   }
}
