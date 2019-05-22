package soot.jimple.spark.sets;

import java.util.Iterator;
import java.util.List;
import soot.Type;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.util.BitSetIterator;
import soot.util.BitVector;

public class SharedHybridSet extends PointsToSetInternal {
   public static final int OVERFLOW_SIZE = 16;
   public static final int OVERFLOW_THRESHOLD = 5;
   private PointsToBitVector bitVector = null;
   private SharedHybridSet.OverflowList overflow = new SharedHybridSet.OverflowList();
   private PAG pag;
   private int numElements = 0;

   public SharedHybridSet(Type type, PAG pag) {
      super(type);
      this.pag = pag;
   }

   public boolean contains(Node n) {
      if (this.bitVector != null && this.bitVector.contains(n)) {
         return true;
      } else {
         return this.overflow.contains(n);
      }
   }

   public boolean isEmpty() {
      return this.numElements == 0;
   }

   private SharedHybridSet.OverflowList remainder(PointsToBitVector a, PointsToBitVector b) {
      PointsToBitVector xorResult = new PointsToBitVector(a);
      xorResult.xor(b);
      return new SharedHybridSet.OverflowList(xorResult);
   }

   private void findAppropriateBitVector(PointsToBitVector newBitVector, PointsToBitVector otherBitVector, int otherSize, int szBitvector) {
      if (otherBitVector != null && otherSize <= this.numElements && otherSize + 5 >= this.numElements && otherBitVector.isSubsetOf(newBitVector)) {
         this.setNewBitVector(szBitvector, otherBitVector);
         this.overflow = this.remainder(newBitVector, otherBitVector);
      } else if (this.bitVector != null && szBitvector <= this.numElements && szBitvector + 5 >= this.numElements && this.bitVector.isSubsetOf(newBitVector)) {
         this.overflow = this.remainder(newBitVector, this.bitVector);
      } else {
         for(int overFlowSize = 0; overFlowSize < 5; ++overFlowSize) {
            int bitVectorCardinality = this.numElements - overFlowSize;
            if (bitVectorCardinality < 0) {
               break;
            }

            if (bitVectorCardinality < AllSharedHybridNodes.v().lookupMap.map.length && AllSharedHybridNodes.v().lookupMap.map[bitVectorCardinality] != null) {
               List<PointsToBitVector> lst = AllSharedHybridNodes.v().lookupMap.map[bitVectorCardinality];
               Iterator var8 = lst.iterator();

               while(var8.hasNext()) {
                  PointsToBitVector candidate = (PointsToBitVector)var8.next();
                  if (candidate.isSubsetOf(newBitVector)) {
                     this.setNewBitVector(szBitvector, candidate);
                     this.overflow = this.remainder(newBitVector, candidate);
                     return;
                  }
               }
            }
         }

         this.setNewBitVector(szBitvector, newBitVector);
         this.overflow.removeAll();
         AllSharedHybridNodes.v().lookupMap.add(this.numElements, newBitVector);
      }

   }

   private void setNewBitVector(int size, PointsToBitVector newBitVector) {
      newBitVector.incRefCount();
      if (this.bitVector != null) {
         this.bitVector.decRefCount();
         if (this.bitVector.unused()) {
            AllSharedHybridNodes.v().lookupMap.remove(size, this.bitVector);
         }
      }

      this.bitVector = newBitVector;
   }

   public boolean add(Node n) {
      if (this.contains(n)) {
         return false;
      } else {
         ++this.numElements;
         if (!this.overflow.full()) {
            this.overflow.add(n);
         } else {
            PointsToBitVector newBitVector;
            if (this.bitVector == null) {
               newBitVector = new PointsToBitVector(this.pag.getAllocNodeNumberer().size());
            } else {
               newBitVector = new PointsToBitVector(this.bitVector);
            }

            newBitVector.add(n);
            this.add(newBitVector, this.overflow);
            this.findAppropriateBitVector(newBitVector, (PointsToBitVector)null, 0, this.numElements - this.overflow.size() - 1);
         }

         return true;
      }
   }

   private boolean nativeAddAll(SharedHybridSet other, SharedHybridSet exclude) {
      BitVector mask = this.getBitMask(other, this.pag);
      if (exclude != null) {
         if (exclude.overflow.size() > 0) {
            PointsToBitVector newBitVector;
            if (exclude.bitVector == null) {
               newBitVector = new PointsToBitVector(this.pag.getAllocNodeNumberer().size());
            } else {
               newBitVector = new PointsToBitVector(exclude.bitVector);
            }

            this.add(newBitVector, exclude.overflow);
            exclude = new SharedHybridSet(this.type, this.pag);
            exclude.bitVector = newBitVector;
         } else if (exclude.bitVector == null) {
            exclude = null;
         }
      }

      int originalSize = this.size();
      int originalOnes = originalSize - this.overflow.size();
      int otherBitVectorSize = other.size() - other.overflow.size();
      SharedHybridSet.OverflowList toReAdd;
      if (this.bitVector == null) {
         this.bitVector = other.bitVector;
         if (this.bitVector != null) {
            this.bitVector.incRefCount();
            toReAdd = this.overflow;
            this.overflow = new SharedHybridSet.OverflowList();
            boolean newBitVectorCreated = false;
            this.numElements = otherBitVectorSize;
            if (exclude != null || mask != null) {
               PointsToBitVector result = new PointsToBitVector(this.bitVector);
               if (exclude != null) {
                  result.andNot(exclude.bitVector);
               }

               if (mask != null) {
                  result.and(mask);
               }

               if (!result.equals(this.bitVector)) {
                  this.add(result, toReAdd);
                  int newBitVectorSize = result.cardinality();
                  this.numElements = newBitVectorSize;
                  this.findAppropriateBitVector(result, other.bitVector, otherBitVectorSize, otherBitVectorSize);
                  newBitVectorCreated = true;
               }
            }

            if (!newBitVectorCreated) {
               for(SharedHybridSet.OverflowList.ListNode i = toReAdd.overflow; i != null; i = i.next) {
                  this.add(i.elem);
               }
            }
         }
      } else if (other.bitVector != null) {
         PointsToBitVector newBitVector = new PointsToBitVector(other.bitVector);
         if (exclude != null) {
            newBitVector.andNot(exclude.bitVector);
         }

         if (mask != null) {
            newBitVector.and(mask);
         }

         newBitVector.or(this.bitVector);
         if (!newBitVector.equals(this.bitVector)) {
            if (other.overflow.size() != 0) {
               PointsToBitVector toAdd = new PointsToBitVector(newBitVector.size());
               this.add(toAdd, other.overflow);
               if (mask != null) {
                  toAdd.and(mask);
               }

               if (exclude != null) {
                  toAdd.andNot(exclude.bitVector);
               }

               newBitVector.or(toAdd);
            }

            int numOnes = newBitVector.cardinality();
            int numAdded = this.add(newBitVector, this.overflow);
            this.numElements += numOnes - originalOnes + numAdded - this.overflow.size();
            if (this.size() > originalSize) {
               this.findAppropriateBitVector(newBitVector, other.bitVector, otherBitVectorSize, originalOnes);
               return true;
            }

            return false;
         }
      }

      toReAdd = other.overflow;

      for(SharedHybridSet.OverflowList.ListNode i = toReAdd.overflow; i != null; i = i.next) {
         Node nodeToMaybeAdd = i.elem;
         if ((exclude == null || !exclude.contains(nodeToMaybeAdd)) && (mask == null || mask.get(nodeToMaybeAdd.getNumber()))) {
            this.add(nodeToMaybeAdd);
         }
      }

      return this.size() > originalSize;
   }

   private int add(PointsToBitVector p, SharedHybridSet.OverflowList arr) {
      int retVal = 0;

      for(SharedHybridSet.OverflowList.ListNode i = arr.overflow; i != null; i = i.next) {
         if (p.add(i.elem)) {
            ++retVal;
         }
      }

      return retVal;
   }

   public boolean addAll(PointsToSetInternal other, PointsToSetInternal exclude) {
      if (other == null) {
         return false;
      } else {
         return other instanceof SharedHybridSet && (exclude == null || exclude instanceof SharedHybridSet) ? this.nativeAddAll((SharedHybridSet)other, (SharedHybridSet)exclude) : super.addAll(other, exclude);
      }
   }

   public boolean forall(P2SetVisitor v) {
      if (this.bitVector != null) {
         BitSetIterator it = this.bitVector.iterator();

         while(it.hasNext()) {
            v.visit((Node)this.pag.getAllocNodeNumberer().get((long)it.next()));
         }
      }

      for(SharedHybridSet.OverflowList.ListNode i = this.overflow.overflow; i != null; i = i.next) {
         v.visit(i.elem);
      }

      return v.getReturnValue();
   }

   public static final P2SetFactory getFactory() {
      return new P2SetFactory() {
         public final PointsToSetInternal newSet(Type type, PAG pag) {
            return new SharedHybridSet(type, pag);
         }
      };
   }

   public int size() {
      return this.numElements;
   }

   private class OverflowList {
      public SharedHybridSet.OverflowList.ListNode overflow = null;
      private int overflowElements = 0;

      public OverflowList() {
      }

      public OverflowList(PointsToBitVector bv) {
         BitSetIterator it = bv.iterator();

         while(it.hasNext()) {
            Node n = (Node)SharedHybridSet.this.pag.getAllocNodeNumberer().get((long)it.next());
            this.add(n);
         }

      }

      public void add(Node n) {
         if (this.full()) {
            throw new RuntimeException("Can't add an element to a full overflow list.");
         } else {
            this.overflow = new SharedHybridSet.OverflowList.ListNode(n, this.overflow);
            ++this.overflowElements;
         }
      }

      public int size() {
         return this.overflowElements;
      }

      public boolean full() {
         return this.overflowElements == 16;
      }

      public boolean contains(Node n) {
         for(SharedHybridSet.OverflowList.ListNode l = this.overflow; l != null; l = l.next) {
            if (n == l.elem) {
               return true;
            }
         }

         return false;
      }

      public void removeAll() {
         this.overflow = null;
         this.overflowElements = 0;
      }

      public class ListNode {
         public Node elem;
         public SharedHybridSet.OverflowList.ListNode next;

         public ListNode(Node elem, SharedHybridSet.OverflowList.ListNode next) {
            this.elem = elem;
            this.next = next;
         }
      }
   }
}
