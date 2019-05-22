package org.jboss.util.collection;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class SoftSet implements Set {
   private HashMap map = new HashMap();
   private ReferenceQueue gcqueue = new ReferenceQueue();

   public int size() {
      this.processQueue();
      return this.map.size();
   }

   public boolean isEmpty() {
      this.processQueue();
      return this.map.isEmpty();
   }

   public boolean contains(Object o) {
      this.processQueue();
      Integer key = new Integer(o.hashCode());
      boolean contains = this.map.containsKey(key);
      return contains;
   }

   public Iterator iterator() {
      this.processQueue();
      Iterator theIter = this.map.values().iterator();
      return new SoftSet.ComparableSoftReferenceIterator(theIter);
   }

   public Object[] toArray() {
      this.processQueue();
      return this.toArray(new Object[0]);
   }

   public Object[] toArray(Object[] a) {
      this.processQueue();
      int size = this.map.size();
      Object[] array = new Object[0];
      if (a.length >= size) {
         array = a;
      }

      Iterator iter = this.map.values().iterator();
      int index = 0;

      while(true) {
         while(iter.hasNext()) {
            SoftSet.ComparableSoftReference csr = (SoftSet.ComparableSoftReference)iter.next();
            Object value = csr.get();
            if (array.length == 0) {
               if (value == null) {
                  ++index;
                  continue;
               }

               Array.newInstance(value.getClass(), size);
            }

            array[index] = value;
            ++index;
         }

         return array;
      }
   }

   public boolean add(Object o) {
      this.processQueue();
      Integer key = new Integer(o.hashCode());
      SoftSet.ComparableSoftReference sr = new SoftSet.ComparableSoftReference(key, o, this.gcqueue);
      return this.map.put(key, sr) == null;
   }

   public boolean remove(Object o) {
      this.processQueue();
      Integer key = new Integer(o.hashCode());
      return this.map.remove(key) != null;
   }

   public boolean containsAll(Collection c) {
      this.processQueue();
      Iterator iter = c.iterator();

      boolean contains;
      Integer key;
      for(contains = true; iter.hasNext(); contains &= this.map.containsKey(key)) {
         Object value = iter.next();
         key = new Integer(value.hashCode());
      }

      return contains;
   }

   public boolean addAll(Collection c) {
      this.processQueue();
      Iterator iter = c.iterator();

      boolean added;
      Integer key;
      SoftSet.ComparableSoftReference sr;
      for(added = false; iter.hasNext(); added |= this.map.put(key, sr) == null) {
         Object value = iter.next();
         key = new Integer(value.hashCode());
         sr = new SoftSet.ComparableSoftReference(key, value, this.gcqueue);
      }

      return added;
   }

   public boolean retainAll(Collection c) {
      Iterator iter = this.iterator();
      boolean removed = false;

      while(iter.hasNext()) {
         Object value = iter.next();
         if (!c.contains(value)) {
            iter.remove();
            removed = true;
         }
      }

      return removed;
   }

   public boolean removeAll(Collection c) {
      this.processQueue();
      Iterator iter = c.iterator();

      boolean removed;
      Object value;
      for(removed = false; iter.hasNext(); removed |= this.remove(value)) {
         value = iter.next();
      }

      return removed;
   }

   public void clear() {
      while(this.gcqueue.poll() != null) {
      }

      this.map.clear();
   }

   public boolean equals(Object o) {
      return this.map.equals(o);
   }

   public int hashCode() {
      return this.map.hashCode();
   }

   private void processQueue() {
      SoftSet.ComparableSoftReference cr;
      while((cr = (SoftSet.ComparableSoftReference)this.gcqueue.poll()) != null) {
         this.map.remove(cr.getKey());
      }

   }

   static class ComparableSoftReferenceIterator implements Iterator {
      Iterator theIter;

      ComparableSoftReferenceIterator(Iterator theIter) {
         this.theIter = theIter;
      }

      public boolean hasNext() {
         return this.theIter.hasNext();
      }

      public Object next() {
         SoftSet.ComparableSoftReference csr = (SoftSet.ComparableSoftReference)this.theIter.next();
         return csr.get();
      }

      public void remove() {
         this.theIter.remove();
      }
   }

   static class ComparableSoftReference extends SoftReference {
      private Integer key;

      ComparableSoftReference(Integer key, Object o, ReferenceQueue q) {
         super(o, q);
         this.key = key;
      }

      Integer getKey() {
         return this.key;
      }
   }
}
