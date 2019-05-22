package edu.emory.mathcs.backport.java.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class AbstractQueue extends AbstractCollection implements Queue {
   protected AbstractQueue() {
   }

   public boolean add(Object e) {
      if (this.offer(e)) {
         return true;
      } else {
         throw new IllegalStateException("Queue full");
      }
   }

   public Object remove() {
      Object x = this.poll();
      if (x != null) {
         return x;
      } else {
         throw new NoSuchElementException();
      }
   }

   public Object element() {
      Object x = this.peek();
      if (x != null) {
         return x;
      } else {
         throw new NoSuchElementException();
      }
   }

   public void clear() {
      while(this.poll() != null) {
      }

   }

   public boolean addAll(Collection c) {
      if (c == null) {
         throw new NullPointerException();
      } else if (c == this) {
         throw new IllegalArgumentException();
      } else {
         boolean modified = false;
         Iterator e = c.iterator();

         while(e.hasNext()) {
            if (this.add(e.next())) {
               modified = true;
            }
         }

         return modified;
      }
   }
}
