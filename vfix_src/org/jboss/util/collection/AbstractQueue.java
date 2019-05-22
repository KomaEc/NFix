package org.jboss.util.collection;

import java.util.AbstractCollection;

public abstract class AbstractQueue<E> extends AbstractCollection<E> implements Queue<E> {
   public static int DEFAULT_MAXIMUM_SIZE = -1;
   protected int maximumSize;

   protected AbstractQueue() {
      this.maximumSize = DEFAULT_MAXIMUM_SIZE;
   }

   protected AbstractQueue(int maxSize) {
      this.maximumSize = DEFAULT_MAXIMUM_SIZE;
      this.setMaximumSize(maxSize);
   }

   public int getMaximumSize() {
      return this.maximumSize;
   }

   public void setMaximumSize(int size) {
      if (size < 0 && size != -1) {
         throw new IllegalArgumentException("illegal size: " + size);
      } else {
         this.maximumSize = size;
      }
   }

   public boolean isFull() {
      return this.maximumSize != -1 && this.size() >= this.maximumSize;
   }

   public boolean isEmpty() {
      return this.size() <= 0;
   }

   public boolean add(E obj) throws FullCollectionException {
      if (this.isFull()) {
         throw new FullCollectionException();
      } else {
         return this.addLast(obj);
      }
   }

   public E remove() throws EmptyCollectionException {
      if (this.isEmpty()) {
         throw new EmptyCollectionException();
      } else {
         return this.removeFirst();
      }
   }

   public void clear() {
      while(!this.isEmpty()) {
         this.remove();
      }

   }

   protected abstract boolean addLast(E var1);

   protected abstract E removeFirst();
}
