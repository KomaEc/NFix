package org.apache.commons.collections;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/** @deprecated */
public class BoundedFifoBuffer extends AbstractCollection implements Buffer, BoundedCollection {
   private final Object[] m_elements;
   private int m_start;
   private int m_end;
   private boolean m_full;
   private final int maxElements;

   public BoundedFifoBuffer() {
      this(32);
   }

   public BoundedFifoBuffer(int size) {
      this.m_start = 0;
      this.m_end = 0;
      this.m_full = false;
      if (size <= 0) {
         throw new IllegalArgumentException("The size must be greater than 0");
      } else {
         this.m_elements = new Object[size];
         this.maxElements = this.m_elements.length;
      }
   }

   public BoundedFifoBuffer(Collection coll) {
      this(coll.size());
      this.addAll(coll);
   }

   public int size() {
      int size = false;
      int size;
      if (this.m_end < this.m_start) {
         size = this.maxElements - this.m_start + this.m_end;
      } else if (this.m_end == this.m_start) {
         size = this.m_full ? this.maxElements : 0;
      } else {
         size = this.m_end - this.m_start;
      }

      return size;
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public boolean isFull() {
      return this.size() == this.maxElements;
   }

   public int maxSize() {
      return this.maxElements;
   }

   public void clear() {
      this.m_full = false;
      this.m_start = 0;
      this.m_end = 0;
      Arrays.fill(this.m_elements, (Object)null);
   }

   public boolean add(Object element) {
      if (null == element) {
         throw new NullPointerException("Attempted to add null object to buffer");
      } else if (this.m_full) {
         throw new BufferOverflowException("The buffer cannot hold more than " + this.maxElements + " objects.");
      } else {
         this.m_elements[this.m_end++] = element;
         if (this.m_end >= this.maxElements) {
            this.m_end = 0;
         }

         if (this.m_end == this.m_start) {
            this.m_full = true;
         }

         return true;
      }
   }

   public Object get() {
      if (this.isEmpty()) {
         throw new BufferUnderflowException("The buffer is already empty");
      } else {
         return this.m_elements[this.m_start];
      }
   }

   public Object remove() {
      if (this.isEmpty()) {
         throw new BufferUnderflowException("The buffer is already empty");
      } else {
         Object element = this.m_elements[this.m_start];
         if (null != element) {
            this.m_elements[this.m_start++] = null;
            if (this.m_start >= this.maxElements) {
               this.m_start = 0;
            }

            this.m_full = false;
         }

         return element;
      }
   }

   private int increment(int index) {
      ++index;
      if (index >= this.maxElements) {
         index = 0;
      }

      return index;
   }

   private int decrement(int index) {
      --index;
      if (index < 0) {
         index = this.maxElements - 1;
      }

      return index;
   }

   public Iterator iterator() {
      return new Iterator() {
         private int index;
         private int lastReturnedIndex;
         private boolean isFirst;

         {
            this.index = BoundedFifoBuffer.this.m_start;
            this.lastReturnedIndex = -1;
            this.isFirst = BoundedFifoBuffer.this.m_full;
         }

         public boolean hasNext() {
            return this.isFirst || this.index != BoundedFifoBuffer.this.m_end;
         }

         public Object next() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               this.isFirst = false;
               this.lastReturnedIndex = this.index;
               this.index = BoundedFifoBuffer.this.increment(this.index);
               return BoundedFifoBuffer.this.m_elements[this.lastReturnedIndex];
            }
         }

         public void remove() {
            if (this.lastReturnedIndex == -1) {
               throw new IllegalStateException();
            } else if (this.lastReturnedIndex == BoundedFifoBuffer.this.m_start) {
               BoundedFifoBuffer.this.remove();
               this.lastReturnedIndex = -1;
            } else {
               int i = this.lastReturnedIndex + 1;

               while(i != BoundedFifoBuffer.this.m_end) {
                  if (i >= BoundedFifoBuffer.this.maxElements) {
                     BoundedFifoBuffer.this.m_elements[i - 1] = BoundedFifoBuffer.this.m_elements[0];
                     i = 0;
                  } else {
                     BoundedFifoBuffer.this.m_elements[i - 1] = BoundedFifoBuffer.this.m_elements[i];
                     ++i;
                  }
               }

               this.lastReturnedIndex = -1;
               BoundedFifoBuffer.this.m_end = BoundedFifoBuffer.this.decrement(BoundedFifoBuffer.this.m_end);
               BoundedFifoBuffer.this.m_elements[BoundedFifoBuffer.this.m_end] = null;
               BoundedFifoBuffer.this.m_full = false;
               this.index = BoundedFifoBuffer.this.decrement(this.index);
            }
         }
      };
   }
}
