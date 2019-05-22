package soot.toolkits.scalar;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ArrayPackedSet<T> extends AbstractBoundedFlowSet<T> {
   ObjectIntMapper<T> map;
   BitSet bits;

   public ArrayPackedSet(FlowUniverse<T> universe) {
      this(new ObjectIntMapper(universe));
   }

   ArrayPackedSet(ObjectIntMapper<T> map) {
      this(map, new BitSet());
   }

   ArrayPackedSet(ObjectIntMapper<T> map, BitSet bits) {
      this.map = map;
      this.bits = bits;
   }

   public ArrayPackedSet<T> clone() {
      return new ArrayPackedSet(this.map, (BitSet)this.bits.clone());
   }

   public FlowSet<T> emptySet() {
      return new ArrayPackedSet(this.map);
   }

   public int size() {
      return this.bits.cardinality();
   }

   public boolean isEmpty() {
      return this.bits.isEmpty();
   }

   public void clear() {
      this.bits.clear();
   }

   private BitSet copyBitSet(ArrayPackedSet<?> dest) {
      assert dest.map == this.map;

      if (this != dest) {
         dest.bits.clear();
         dest.bits.or(this.bits);
      }

      return dest.bits;
   }

   private boolean sameType(Object flowSet) {
      if (flowSet instanceof ArrayPackedSet) {
         return ((ArrayPackedSet)flowSet).map == this.map;
      } else {
         return false;
      }
   }

   private List<T> toList(BitSet bits, int base) {
      int len = bits.cardinality();
      switch(len) {
      case 0:
         return Collections.emptyList();
      case 1:
         return Collections.singletonList(this.map.getObject(base - 1 + bits.length()));
      default:
         List<T> elements = new ArrayList(len);
         int i = bits.nextSetBit(0);

         do {
            int endOfRun = bits.nextClearBit(i + 1);

            do {
               elements.add(this.map.getObject(base + i++));
            } while(i < endOfRun);

            i = bits.nextSetBit(i + 1);
         } while(i >= 0);

         return elements;
      }
   }

   public List<T> toList(int lowInclusive, int highInclusive) {
      if (lowInclusive > highInclusive) {
         return Collections.emptyList();
      } else {
         int highExclusive = highInclusive + 1;
         if (lowInclusive < 0) {
            throw new IllegalArgumentException();
         } else {
            return this.toList(this.bits.get(lowInclusive, highExclusive), lowInclusive);
         }
      }
   }

   public List<T> toList() {
      return this.toList(this.bits, 0);
   }

   public void add(T obj) {
      this.bits.set(this.map.getInt(obj));
   }

   public void complement(FlowSet<T> destFlow) {
      if (this.sameType(destFlow)) {
         ArrayPackedSet<T> dest = (ArrayPackedSet)destFlow;
         this.copyBitSet(dest).flip(0, dest.map.size());
      } else {
         super.complement(destFlow);
      }

   }

   public void remove(T obj) {
      this.bits.clear(this.map.getInt(obj));
   }

   public boolean isSubSet(FlowSet<T> other) {
      if (other == this) {
         return true;
      } else if (this.sameType(other)) {
         ArrayPackedSet<T> o = (ArrayPackedSet)other;
         BitSet tmp = (BitSet)o.bits.clone();
         tmp.andNot(this.bits);
         return tmp.isEmpty();
      } else {
         return super.isSubSet(other);
      }
   }

   public void union(FlowSet<T> otherFlow, FlowSet<T> destFlow) {
      if (this.sameType(otherFlow) && this.sameType(destFlow)) {
         ArrayPackedSet<T> other = (ArrayPackedSet)otherFlow;
         ArrayPackedSet<T> dest = (ArrayPackedSet)destFlow;
         this.copyBitSet(dest).or(other.bits);
      } else {
         super.union(otherFlow, destFlow);
      }

   }

   public void difference(FlowSet<T> otherFlow, FlowSet<T> destFlow) {
      if (this.sameType(otherFlow) && this.sameType(destFlow)) {
         ArrayPackedSet<T> other = (ArrayPackedSet)otherFlow;
         ArrayPackedSet<T> dest = (ArrayPackedSet)destFlow;
         this.copyBitSet(dest).andNot(other.bits);
      } else {
         super.difference(otherFlow, destFlow);
      }

   }

   public void intersection(FlowSet<T> otherFlow, FlowSet<T> destFlow) {
      if (this.sameType(otherFlow) && this.sameType(destFlow)) {
         ArrayPackedSet<T> other = (ArrayPackedSet)otherFlow;
         ArrayPackedSet<T> dest = (ArrayPackedSet)destFlow;
         this.copyBitSet(dest).and(other.bits);
      } else {
         super.intersection(otherFlow, destFlow);
      }

   }

   public boolean contains(T obj) {
      return this.map.contains(obj) && this.bits.get(this.map.getInt(obj));
   }

   public boolean equals(Object otherFlow) {
      return this.sameType(otherFlow) ? this.bits.equals(((ArrayPackedSet)otherFlow).bits) : super.equals(otherFlow);
   }

   public void copy(FlowSet<T> destFlow) {
      if (this != destFlow) {
         if (this.sameType(destFlow)) {
            ArrayPackedSet<T> dest = (ArrayPackedSet)destFlow;
            this.copyBitSet(dest);
         } else {
            super.copy(destFlow);
         }

      }
   }

   public Iterator<T> iterator() {
      return new Iterator<T>() {
         int curr = -1;
         int next;

         {
            this.next = ArrayPackedSet.this.bits.nextSetBit(0);
         }

         public boolean hasNext() {
            return this.next >= 0;
         }

         public T next() {
            if (this.next < 0) {
               throw new NoSuchElementException();
            } else {
               this.curr = this.next;
               this.next = ArrayPackedSet.this.bits.nextSetBit(this.curr + 1);
               return ArrayPackedSet.this.map.getObject(this.curr);
            }
         }

         public void remove() {
            if (this.curr < 0) {
               throw new IllegalStateException();
            } else {
               ArrayPackedSet.this.bits.clear(this.curr);
               this.curr = -1;
            }
         }
      };
   }
}
