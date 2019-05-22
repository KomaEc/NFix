package heros.edgefunc;

import heros.EdgeFunction;

public class AllBottom<V> implements EdgeFunction<V> {
   private final V bottomElement;

   public AllBottom(V bottomElement) {
      this.bottomElement = bottomElement;
   }

   public V computeTarget(V source) {
      return this.bottomElement;
   }

   public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {
      return (EdgeFunction)(secondFunction instanceof EdgeIdentity ? this : secondFunction);
   }

   public EdgeFunction<V> joinWith(EdgeFunction<V> otherFunction) {
      if (otherFunction != this && !otherFunction.equalTo(this)) {
         if (otherFunction instanceof AllTop) {
            return this;
         } else if (otherFunction instanceof EdgeIdentity) {
            return this;
         } else {
            throw new IllegalStateException("unexpected edge function: " + otherFunction);
         }
      } else {
         return this;
      }
   }

   public boolean equalTo(EdgeFunction<V> other) {
      if (other instanceof AllBottom) {
         AllBottom allBottom = (AllBottom)other;
         return allBottom.bottomElement.equals(this.bottomElement);
      } else {
         return false;
      }
   }

   public String toString() {
      return "allbottom";
   }
}
