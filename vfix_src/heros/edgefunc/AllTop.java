package heros.edgefunc;

import heros.EdgeFunction;

public class AllTop<V> implements EdgeFunction<V> {
   private final V topElement;

   public AllTop(V topElement) {
      this.topElement = topElement;
   }

   public V computeTarget(V source) {
      return this.topElement;
   }

   public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {
      return this;
   }

   public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {
      return otherFunction;
   }

   public boolean equalTo(EdgeFunction<V> other) {
      if (other instanceof AllTop) {
         AllTop allTop = (AllTop)other;
         return allTop.topElement.equals(this.topElement);
      } else {
         return false;
      }
   }

   public String toString() {
      return "alltop";
   }
}
