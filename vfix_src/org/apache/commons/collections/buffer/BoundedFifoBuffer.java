package org.apache.commons.collections.buffer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.commons.collections.BoundedCollection;
import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.BufferOverflowException;
import org.apache.commons.collections.BufferUnderflowException;

public class BoundedFifoBuffer extends AbstractCollection implements Buffer, BoundedCollection, Serializable {
   private static final long serialVersionUID = 5603722811189451017L;
   private transient Object[] elements;
   private transient int start;
   private transient int end;
   private transient boolean full;
   private final int maxElements;

   public BoundedFifoBuffer() {
      this(32);
   }

   public BoundedFifoBuffer(int size) {
      this.start = 0;
      this.end = 0;
      this.full = false;
      if (size <= 0) {
         throw new IllegalArgumentException("The size must be greater than 0");
      } else {
         this.elements = new Object[size];
         this.maxElements = this.elements.length;
      }
   }

   public BoundedFifoBuffer(Collection coll) {
      this(coll.size());
      this.addAll(coll);
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
      out.writeInt(this.size());
      Iterator it = this.iterator();

      while(it.hasNext()) {
         out.writeObject(it.next());
      }

   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      this.elements = new Object[this.maxElements];
      int size = in.readInt();

      for(int i = 0; i < size; ++i) {
         this.elements[i] = in.readObject();
      }

      this.start = 0;
      this.full = size == this.maxElements;
      if (this.full) {
         this.end = 0;
      } else {
         this.end = size;
      }

   }

   public int size() {
      int size = false;
      int size;
      if (this.end < this.start) {
         size = this.maxElements - this.start + this.end;
      } else if (this.end == this.start) {
         size = this.full ? this.maxElements : 0;
      } else {
         size = this.end - this.start;
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
      this.full = false;
      this.start = 0;
      this.end = 0;
      Arrays.fill(this.elements, (Object)null);
   }

   public boolean add(Object element) {
      if (null == element) {
         throw new NullPointerException("Attempted to add null object to buffer");
      } else if (this.full) {
         throw new BufferOverflowException("The buffer cannot hold more than " + this.maxElements + " objects.");
      } else {
         this.elements[this.end++] = element;
         if (this.end >= this.maxElements) {
            this.end = 0;
         }

         if (this.end == this.start) {
            this.full = true;
         }

         return true;
      }
   }

   public Object get() {
      if (this.isEmpty()) {
         throw new BufferUnderflowException("The buffer is already empty");
      } else {
         return this.elements[this.start];
      }
   }

   public Object remove() {
      if (this.isEmpty()) {
         throw new BufferUnderflowException("The buffer is already empty");
      } else {
         Object element = this.elements[this.start];
         if (null != element) {
            this.elements[this.start++] = null;
            if (this.start >= this.maxElements) {
               this.start = 0;
            }

            this.full = false;
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
            this.index = BoundedFifoBuffer.this.start;
            this.lastReturnedIndex = -1;
            this.isFirst = BoundedFifoBuffer.this.full;
         }

         public boolean hasNext() {
            return this.isFirst || this.index != BoundedFifoBuffer.this.end;
         }

         public Object next() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               this.isFirst = false;
               this.lastReturnedIndex = this.index;
               this.index = BoundedFifoBuffer.this.increment(this.index);
               return BoundedFifoBuffer.this.elements[this.lastReturnedIndex];
            }
         }

         public void remove() {
            if (this.lastReturnedIndex == -1) {
               throw new IllegalStateException();
            } else if (this.lastReturnedIndex == BoundedFifoBuffer.this.start) {
               BoundedFifoBuffer.this.remove();
               this.lastReturnedIndex = -1;
            } else {
               int pos = this.lastReturnedIndex + 1;
               if (BoundedFifoBuffer.this.start < this.lastReturnedIndex && pos < BoundedFifoBuffer.this.end) {
                  System.arraycopy(BoundedFifoBuffer.this.elements, pos, BoundedFifoBuffer.this.elements, this.lastReturnedIndex, BoundedFifoBuffer.this.end - pos);
               } else {
                  while(pos != BoundedFifoBuffer.this.end) {
                     if (pos >= BoundedFifoBuffer.this.maxElements) {
                        BoundedFifoBuffer.this.elements[pos - 1] = BoundedFifoBuffer.this.elements[0];
                        pos = 0;
                     } else {
                        BoundedFifoBuffer.this.elements[BoundedFifoBuffer.this.decrement(pos)] = BoundedFifoBuffer.this.elements[pos];
                        pos = BoundedFifoBuffer.this.increment(pos);
                     }
                  }
               }

               this.lastReturnedIndex = -1;
               BoundedFifoBuffer.this.end = BoundedFifoBuffer.this.decrement(BoundedFifoBuffer.this.end);
               BoundedFifoBuffer.this.elements[BoundedFifoBuffer.this.end] = null;
               BoundedFifoBuffer.this.full = false;
               this.index = BoundedFifoBuffer.this.decrement(this.index);
            }
         }
      };
   }
}
