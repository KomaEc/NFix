package org.jboss.util.collection;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.jboss.util.NullArgumentException;

public class ArrayIterator implements Iterator, Serializable, Cloneable {
   private static final long serialVersionUID = -6604583440222021075L;
   protected final Object[] array;
   protected int index;

   public ArrayIterator(Object[] array) {
      if (array == null) {
         throw new NullArgumentException("array");
      } else {
         this.array = array;
      }
   }

   public boolean hasNext() {
      return this.index < this.array.length;
   }

   public Object next() {
      if (!this.hasNext()) {
         throw new NoSuchElementException();
      } else {
         return this.array[this.index++];
      }
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }
}
