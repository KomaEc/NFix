package org.apache.commons.collections.buffer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.BufferUnderflowException;

public class UnboundedFifoBuffer extends AbstractCollection implements Buffer, Serializable {
   private static final long serialVersionUID = -3482960336579541419L;
   protected transient Object[] buffer;
   protected transient int head;
   protected transient int tail;

   public UnboundedFifoBuffer() {
      this(32);
   }

   public UnboundedFifoBuffer(int initialSize) {
      if (initialSize <= 0) {
         throw new IllegalArgumentException("The size must be greater than 0");
      } else {
         this.buffer = new Object[initialSize + 1];
         this.head = 0;
         this.tail = 0;
      }
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
      int size = in.readInt();
      this.buffer = new Object[size + 1];

      for(int i = 0; i < size; ++i) {
         this.buffer[i] = in.readObject();
      }

      this.head = 0;
      this.tail = size;
   }

   public int size() {
      int size = false;
      int size;
      if (this.tail < this.head) {
         size = this.buffer.length - this.head + this.tail;
      } else {
         size = this.tail - this.head;
      }

      return size;
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public boolean add(Object obj) {
      if (obj == null) {
         throw new NullPointerException("Attempted to add null object to buffer");
      } else {
         if (this.size() + 1 >= this.buffer.length) {
            Object[] tmp = new Object[(this.buffer.length - 1) * 2 + 1];
            int j = 0;

            for(int i = this.head; i != this.tail; i = this.increment(i)) {
               tmp[j] = this.buffer[i];
               this.buffer[i] = null;
               ++j;
            }

            this.buffer = tmp;
            this.head = 0;
            this.tail = j;
         }

         this.buffer[this.tail] = obj;
         this.tail = this.increment(this.tail);
         return true;
      }
   }

   public Object get() {
      if (this.isEmpty()) {
         throw new BufferUnderflowException("The buffer is already empty");
      } else {
         return this.buffer[this.head];
      }
   }

   public Object remove() {
      if (this.isEmpty()) {
         throw new BufferUnderflowException("The buffer is already empty");
      } else {
         Object element = this.buffer[this.head];
         if (element != null) {
            this.buffer[this.head] = null;
            this.head = this.increment(this.head);
         }

         return element;
      }
   }

   private int increment(int index) {
      ++index;
      if (index >= this.buffer.length) {
         index = 0;
      }

      return index;
   }

   private int decrement(int index) {
      --index;
      if (index < 0) {
         index = this.buffer.length - 1;
      }

      return index;
   }

   public Iterator iterator() {
      return new Iterator() {
         private int index;
         private int lastReturnedIndex;

         {
            this.index = UnboundedFifoBuffer.this.head;
            this.lastReturnedIndex = -1;
         }

         public boolean hasNext() {
            return this.index != UnboundedFifoBuffer.this.tail;
         }

         public Object next() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               this.lastReturnedIndex = this.index;
               this.index = UnboundedFifoBuffer.this.increment(this.index);
               return UnboundedFifoBuffer.this.buffer[this.lastReturnedIndex];
            }
         }

         public void remove() {
            if (this.lastReturnedIndex == -1) {
               throw new IllegalStateException();
            } else if (this.lastReturnedIndex == UnboundedFifoBuffer.this.head) {
               UnboundedFifoBuffer.this.remove();
               this.lastReturnedIndex = -1;
            } else {
               for(int i = UnboundedFifoBuffer.this.increment(this.lastReturnedIndex); i != UnboundedFifoBuffer.this.tail; i = UnboundedFifoBuffer.this.increment(i)) {
                  UnboundedFifoBuffer.this.buffer[UnboundedFifoBuffer.this.decrement(i)] = UnboundedFifoBuffer.this.buffer[i];
               }

               this.lastReturnedIndex = -1;
               UnboundedFifoBuffer.this.tail = UnboundedFifoBuffer.this.decrement(UnboundedFifoBuffer.this.tail);
               UnboundedFifoBuffer.this.buffer[UnboundedFifoBuffer.this.tail] = null;
               this.index = UnboundedFifoBuffer.this.decrement(this.index);
            }
         }
      };
   }
}
