package org.apache.commons.collections.buffer;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.BufferUnderflowException;

public class PriorityBuffer extends AbstractCollection implements Buffer, Serializable {
   private static final long serialVersionUID = 6891186490470027896L;
   private static final int DEFAULT_CAPACITY = 13;
   protected Object[] elements;
   protected int size;
   protected boolean ascendingOrder;
   protected Comparator comparator;

   public PriorityBuffer() {
      this(13, true, (Comparator)null);
   }

   public PriorityBuffer(Comparator comparator) {
      this(13, true, comparator);
   }

   public PriorityBuffer(boolean ascendingOrder) {
      this(13, ascendingOrder, (Comparator)null);
   }

   public PriorityBuffer(boolean ascendingOrder, Comparator comparator) {
      this(13, ascendingOrder, comparator);
   }

   public PriorityBuffer(int capacity) {
      this(capacity, true, (Comparator)null);
   }

   public PriorityBuffer(int capacity, Comparator comparator) {
      this(capacity, true, comparator);
   }

   public PriorityBuffer(int capacity, boolean ascendingOrder) {
      this(capacity, ascendingOrder, (Comparator)null);
   }

   public PriorityBuffer(int capacity, boolean ascendingOrder, Comparator comparator) {
      if (capacity <= 0) {
         throw new IllegalArgumentException("invalid capacity");
      } else {
         this.ascendingOrder = ascendingOrder;
         this.elements = new Object[capacity + 1];
         this.comparator = comparator;
      }
   }

   public boolean isAscendingOrder() {
      return this.ascendingOrder;
   }

   public Comparator comparator() {
      return this.comparator;
   }

   public int size() {
      return this.size;
   }

   public void clear() {
      this.elements = new Object[this.elements.length];
      this.size = 0;
   }

   public boolean add(Object element) {
      if (this.isAtCapacity()) {
         this.grow();
      }

      if (this.ascendingOrder) {
         this.percolateUpMinHeap(element);
      } else {
         this.percolateUpMaxHeap(element);
      }

      return true;
   }

   public Object get() {
      if (this.isEmpty()) {
         throw new BufferUnderflowException();
      } else {
         return this.elements[1];
      }
   }

   public Object remove() {
      Object result = this.get();
      this.elements[1] = this.elements[this.size--];
      this.elements[this.size + 1] = null;
      if (this.size != 0) {
         if (this.ascendingOrder) {
            this.percolateDownMinHeap(1);
         } else {
            this.percolateDownMaxHeap(1);
         }
      }

      return result;
   }

   protected boolean isAtCapacity() {
      return this.elements.length == this.size + 1;
   }

   protected void percolateDownMinHeap(int index) {
      Object element = this.elements[index];

      int hole;
      int child;
      for(hole = index; hole * 2 <= this.size; hole = child) {
         child = hole * 2;
         if (child != this.size && this.compare(this.elements[child + 1], this.elements[child]) < 0) {
            ++child;
         }

         if (this.compare(this.elements[child], element) >= 0) {
            break;
         }

         this.elements[hole] = this.elements[child];
      }

      this.elements[hole] = element;
   }

   protected void percolateDownMaxHeap(int index) {
      Object element = this.elements[index];

      int hole;
      int child;
      for(hole = index; hole * 2 <= this.size; hole = child) {
         child = hole * 2;
         if (child != this.size && this.compare(this.elements[child + 1], this.elements[child]) > 0) {
            ++child;
         }

         if (this.compare(this.elements[child], element) <= 0) {
            break;
         }

         this.elements[hole] = this.elements[child];
      }

      this.elements[hole] = element;
   }

   protected void percolateUpMinHeap(int index) {
      int hole = index;

      Object element;
      int next;
      for(element = this.elements[index]; hole > 1 && this.compare(element, this.elements[hole / 2]) < 0; hole = next) {
         next = hole / 2;
         this.elements[hole] = this.elements[next];
      }

      this.elements[hole] = element;
   }

   protected void percolateUpMinHeap(Object element) {
      this.elements[++this.size] = element;
      this.percolateUpMinHeap(this.size);
   }

   protected void percolateUpMaxHeap(int index) {
      int hole = index;

      Object element;
      int next;
      for(element = this.elements[index]; hole > 1 && this.compare(element, this.elements[hole / 2]) > 0; hole = next) {
         next = hole / 2;
         this.elements[hole] = this.elements[next];
      }

      this.elements[hole] = element;
   }

   protected void percolateUpMaxHeap(Object element) {
      this.elements[++this.size] = element;
      this.percolateUpMaxHeap(this.size);
   }

   protected int compare(Object a, Object b) {
      return this.comparator != null ? this.comparator.compare(a, b) : ((Comparable)a).compareTo(b);
   }

   protected void grow() {
      Object[] array = new Object[this.elements.length * 2];
      System.arraycopy(this.elements, 0, array, 0, this.elements.length);
      this.elements = array;
   }

   public Iterator iterator() {
      return new Iterator() {
         private int index = 1;
         private int lastReturnedIndex = -1;

         public boolean hasNext() {
            return this.index <= PriorityBuffer.this.size;
         }

         public Object next() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               this.lastReturnedIndex = this.index++;
               return PriorityBuffer.this.elements[this.lastReturnedIndex];
            }
         }

         public void remove() {
            if (this.lastReturnedIndex == -1) {
               throw new IllegalStateException();
            } else {
               PriorityBuffer.this.elements[this.lastReturnedIndex] = PriorityBuffer.this.elements[PriorityBuffer.this.size];
               PriorityBuffer.this.elements[PriorityBuffer.this.size] = null;
               --PriorityBuffer.this.size;
               if (PriorityBuffer.this.size != 0 && this.lastReturnedIndex <= PriorityBuffer.this.size) {
                  int compareToParent = 0;
                  if (this.lastReturnedIndex > 1) {
                     compareToParent = PriorityBuffer.this.compare(PriorityBuffer.this.elements[this.lastReturnedIndex], PriorityBuffer.this.elements[this.lastReturnedIndex / 2]);
                  }

                  if (PriorityBuffer.this.ascendingOrder) {
                     if (this.lastReturnedIndex > 1 && compareToParent < 0) {
                        PriorityBuffer.this.percolateUpMinHeap(this.lastReturnedIndex);
                     } else {
                        PriorityBuffer.this.percolateDownMinHeap(this.lastReturnedIndex);
                     }
                  } else if (this.lastReturnedIndex > 1 && compareToParent > 0) {
                     PriorityBuffer.this.percolateUpMaxHeap(this.lastReturnedIndex);
                  } else {
                     PriorityBuffer.this.percolateDownMaxHeap(this.lastReturnedIndex);
                  }
               }

               --this.index;
               this.lastReturnedIndex = -1;
            }
         }
      };
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("[ ");

      for(int i = 1; i < this.size + 1; ++i) {
         if (i != 1) {
            sb.append(", ");
         }

         sb.append(this.elements[i]);
      }

      sb.append(" ]");
      return sb.toString();
   }
}
