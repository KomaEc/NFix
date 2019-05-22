package soot.toolkits.scalar;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ArraySparseSet<T> extends AbstractFlowSet<T> {
   protected static final int DEFAULT_SIZE = 8;
   protected int numElements;
   protected int maxElements;
   protected T[] elements;

   public ArraySparseSet() {
      this.maxElements = 8;
      this.elements = (Object[])(new Object[8]);
      this.numElements = 0;
   }

   private ArraySparseSet(ArraySparseSet<T> other) {
      this.numElements = other.numElements;
      this.maxElements = other.maxElements;
      this.elements = (Object[])other.elements.clone();
   }

   private boolean sameType(Object flowSet) {
      return flowSet instanceof ArraySparseSet;
   }

   public ArraySparseSet<T> clone() {
      return new ArraySparseSet(this);
   }

   public FlowSet<T> emptySet() {
      return new ArraySparseSet();
   }

   public void clear() {
      this.numElements = 0;
      Arrays.fill(this.elements, (Object)null);
   }

   public int size() {
      return this.numElements;
   }

   public boolean isEmpty() {
      return this.numElements == 0;
   }

   public List<T> toList() {
      return Arrays.asList(Arrays.copyOf(this.elements, this.numElements));
   }

   public void add(T e) {
      if (!this.contains(e)) {
         if (this.numElements == this.maxElements) {
            this.doubleCapacity();
         }

         this.elements[this.numElements++] = e;
      }

   }

   private void doubleCapacity() {
      int newSize = this.maxElements * 2;
      T[] newElements = (Object[])(new Object[newSize]);
      System.arraycopy(this.elements, 0, newElements, 0, this.numElements);
      this.elements = newElements;
      this.maxElements = newSize;
   }

   public void remove(Object obj) {
      for(int i = 0; i < this.numElements; ++i) {
         if (this.elements[i].equals(obj)) {
            this.remove(i);
            break;
         }
      }

   }

   public void remove(int idx) {
      --this.numElements;
      this.elements[idx] = this.elements[this.numElements];
      this.elements[this.numElements] = null;
   }

   public void union(FlowSet<T> otherFlow, FlowSet<T> destFlow) {
      if (this.sameType(otherFlow) && this.sameType(destFlow)) {
         ArraySparseSet<T> other = (ArraySparseSet)otherFlow;
         ArraySparseSet<T> dest = (ArraySparseSet)destFlow;
         int i;
         if (dest == other) {
            for(i = 0; i < this.numElements; ++i) {
               dest.add(this.elements[i]);
            }
         } else {
            if (this != dest) {
               this.copy(dest);
            }

            for(i = 0; i < other.numElements; ++i) {
               dest.add(other.elements[i]);
            }
         }
      } else {
         super.union(otherFlow, destFlow);
      }

   }

   public void intersection(FlowSet<T> otherFlow, FlowSet<T> destFlow) {
      if (this.sameType(otherFlow) && this.sameType(destFlow)) {
         ArraySparseSet<T> other = (ArraySparseSet)otherFlow;
         ArraySparseSet<T> dest = (ArraySparseSet)destFlow;
         ArraySparseSet workingSet;
         if (dest != other && dest != this) {
            workingSet = dest;
            dest.clear();
         } else {
            workingSet = new ArraySparseSet();
         }

         for(int i = 0; i < this.numElements; ++i) {
            if (other.contains(this.elements[i])) {
               workingSet.add(this.elements[i]);
            }
         }

         if (workingSet != dest) {
            workingSet.copy(dest);
         }
      } else {
         super.intersection(otherFlow, destFlow);
      }

   }

   public void difference(FlowSet<T> otherFlow, FlowSet<T> destFlow) {
      if (this.sameType(otherFlow) && this.sameType(destFlow)) {
         ArraySparseSet<T> other = (ArraySparseSet)otherFlow;
         ArraySparseSet<T> dest = (ArraySparseSet)destFlow;
         ArraySparseSet workingSet;
         if (dest != other && dest != this) {
            workingSet = dest;
            dest.clear();
         } else {
            workingSet = new ArraySparseSet();
         }

         for(int i = 0; i < this.numElements; ++i) {
            if (!other.contains(this.elements[i])) {
               workingSet.add(this.elements[i]);
            }
         }

         if (workingSet != dest) {
            workingSet.copy(dest);
         }
      } else {
         super.difference(otherFlow, destFlow);
      }

   }

   /** @deprecated */
   @Deprecated
   public boolean contains(Object obj) {
      for(int i = 0; i < this.numElements; ++i) {
         if (this.elements[i].equals(obj)) {
            return true;
         }
      }

      return false;
   }

   public boolean equals(Object otherFlow) {
      if (this.sameType(otherFlow)) {
         ArraySparseSet<T> other = (ArraySparseSet)otherFlow;
         if (other.numElements != this.numElements) {
            return false;
         } else {
            int size = this.numElements;

            for(int i = 0; i < size; ++i) {
               if (!other.contains(this.elements[i])) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return super.equals(otherFlow);
      }
   }

   public void copy(FlowSet<T> destFlow) {
      if (this.sameType(destFlow)) {
         ArraySparseSet dest = (ArraySparseSet)destFlow;

         while(dest.maxElements < this.maxElements) {
            dest.doubleCapacity();
         }

         dest.numElements = this.numElements;
         System.arraycopy(this.elements, 0, dest.elements, 0, this.numElements);
      } else {
         super.copy(destFlow);
      }

   }

   public Iterator<T> iterator() {
      return new Iterator<T>() {
         int lastIdx = 0;

         public boolean hasNext() {
            return this.lastIdx < ArraySparseSet.this.numElements;
         }

         public T next() {
            return ArraySparseSet.this.elements[this.lastIdx++];
         }

         public void remove() {
            ArraySparseSet.this.remove(this.lastIdx);
            --this.lastIdx;
         }
      };
   }
}
