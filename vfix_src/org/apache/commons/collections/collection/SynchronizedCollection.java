package org.apache.commons.collections.collection;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

public class SynchronizedCollection implements Collection, Serializable {
   private static final long serialVersionUID = 2412805092710877986L;
   protected final Collection collection;
   protected final Object lock;

   public static Collection decorate(Collection coll) {
      return new SynchronizedCollection(coll);
   }

   protected SynchronizedCollection(Collection collection) {
      if (collection == null) {
         throw new IllegalArgumentException("Collection must not be null");
      } else {
         this.collection = collection;
         this.lock = this;
      }
   }

   protected SynchronizedCollection(Collection collection, Object lock) {
      if (collection == null) {
         throw new IllegalArgumentException("Collection must not be null");
      } else {
         this.collection = collection;
         this.lock = lock;
      }
   }

   public boolean add(Object object) {
      Object var2 = this.lock;
      synchronized(var2) {
         boolean var3 = this.collection.add(object);
         return var3;
      }
   }

   public boolean addAll(Collection coll) {
      Object var2 = this.lock;
      synchronized(var2) {
         boolean var3 = this.collection.addAll(coll);
         return var3;
      }
   }

   public void clear() {
      Object var1 = this.lock;
      synchronized(var1) {
         this.collection.clear();
      }
   }

   public boolean contains(Object object) {
      Object var2 = this.lock;
      synchronized(var2) {
         boolean var3 = this.collection.contains(object);
         return var3;
      }
   }

   public boolean containsAll(Collection coll) {
      Object var2 = this.lock;
      synchronized(var2) {
         boolean var3 = this.collection.containsAll(coll);
         return var3;
      }
   }

   public boolean isEmpty() {
      Object var1 = this.lock;
      synchronized(var1) {
         boolean var2 = this.collection.isEmpty();
         return var2;
      }
   }

   public Iterator iterator() {
      return this.collection.iterator();
   }

   public Object[] toArray() {
      Object var1 = this.lock;
      synchronized(var1) {
         Object[] var2 = this.collection.toArray();
         return var2;
      }
   }

   public Object[] toArray(Object[] object) {
      Object var2 = this.lock;
      synchronized(var2) {
         Object[] var3 = this.collection.toArray(object);
         return var3;
      }
   }

   public boolean remove(Object object) {
      Object var2 = this.lock;
      synchronized(var2) {
         boolean var3 = this.collection.remove(object);
         return var3;
      }
   }

   public boolean removeAll(Collection coll) {
      Object var2 = this.lock;
      synchronized(var2) {
         boolean var3 = this.collection.removeAll(coll);
         return var3;
      }
   }

   public boolean retainAll(Collection coll) {
      Object var2 = this.lock;
      synchronized(var2) {
         boolean var3 = this.collection.retainAll(coll);
         return var3;
      }
   }

   public int size() {
      Object var1 = this.lock;
      synchronized(var1) {
         int var2 = this.collection.size();
         return var2;
      }
   }

   public boolean equals(Object object) {
      Object var2 = this.lock;
      synchronized(var2) {
         boolean var3;
         if (object == this) {
            var3 = true;
            return var3;
         } else {
            var3 = this.collection.equals(object);
            return var3;
         }
      }
   }

   public int hashCode() {
      Object var1 = this.lock;
      synchronized(var1) {
         int var2 = this.collection.hashCode();
         return var2;
      }
   }

   public String toString() {
      Object var1 = this.lock;
      synchronized(var1) {
         String var2 = this.collection.toString();
         return var2;
      }
   }
}
