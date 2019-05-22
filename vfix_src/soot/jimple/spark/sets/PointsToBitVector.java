package soot.jimple.spark.sets;

import soot.jimple.spark.pag.Node;
import soot.util.BitVector;

public class PointsToBitVector extends BitVector {
   private int refCount = 0;

   public PointsToBitVector(int size) {
      super(size);
   }

   public boolean add(Node n) {
      int num = n.getNumber();
      if (!this.get(num)) {
         this.set(num);
         return true;
      } else {
         return false;
      }
   }

   public boolean contains(Node n) {
      return this.get(n.getNumber());
   }

   public boolean isSubsetOf(PointsToBitVector other) {
      BitVector andResult = BitVector.and(this, other);
      return andResult.equals(this);
   }

   public PointsToBitVector(PointsToBitVector other) {
      super(other);
   }

   public void incRefCount() {
      ++this.refCount;
   }

   public void decRefCount() {
      --this.refCount;
   }

   public boolean unused() {
      return this.refCount == 0;
   }
}
