package org.jboss.util.collection;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import org.jboss.util.Null;

public final class Iterators {
   public static final Iterator EMPTY_ITERATOR = new Iterators.EmptyIterator();

   public static Iterator forEnumeration(Enumeration enumeration) {
      return new Iterators.Enum2Iterator(enumeration);
   }

   public static Enumeration toEnumeration(Iterator iter) {
      return new Iterators.Iter2Enumeration(iter);
   }

   public static Iterator makeImmutable(Iterator iter) {
      return new Iterators.ImmutableIterator(iter);
   }

   public static Iterator makeSynchronized(Iterator iter) {
      return new Iterators.SyncIterator(iter);
   }

   public static Enumeration makeSynchronized(Enumeration enumeration) {
      return new Iterators.SyncEnumeration(enumeration);
   }

   public static Iterator union(Iterator[] iters) {
      Map map = new HashMap();

      for(int i = 0; i < iters.length; ++i) {
         if (iters[i] != null) {
            while(iters[i].hasNext()) {
               Object obj = iters[i].next();
               if (!map.containsKey(obj)) {
                  map.put(obj, Null.VALUE);
               }
            }
         }
      }

      return map.keySet().iterator();
   }

   public static String toString(Iterator iter, String delim) {
      StringBuffer buff = new StringBuffer();

      while(iter.hasNext()) {
         buff.append(iter.next());
         if (iter.hasNext()) {
            buff.append(delim);
         }
      }

      return buff.toString();
   }

   public static String toString(Iterator iter) {
      return toString(iter, ",");
   }

   private static final class EmptyIterator implements Iterator {
      private EmptyIterator() {
      }

      public boolean hasNext() {
         return false;
      }

      public Object next() {
         throw new NoSuchElementException("no more elements");
      }

      public void remove() {
         throw new IllegalStateException("no more elements");
      }

      // $FF: synthetic method
      EmptyIterator(Object x0) {
         this();
      }
   }

   private static final class SyncEnumeration implements Enumeration {
      private final Enumeration enumeration;

      public SyncEnumeration(Enumeration enumeration) {
         this.enumeration = enumeration;
      }

      public synchronized boolean hasMoreElements() {
         return this.enumeration.hasMoreElements();
      }

      public synchronized Object nextElement() {
         return this.enumeration.nextElement();
      }
   }

   private static final class SyncIterator implements Iterator {
      private final Iterator iter;

      public SyncIterator(Iterator iter) {
         this.iter = iter;
      }

      public synchronized boolean hasNext() {
         return this.iter.hasNext();
      }

      public synchronized Object next() {
         return this.iter.next();
      }

      public synchronized void remove() {
         this.iter.remove();
      }
   }

   private static final class ImmutableIterator implements Iterator {
      private final Iterator iter;

      public ImmutableIterator(Iterator iter) {
         this.iter = iter;
      }

      public boolean hasNext() {
         return this.iter.hasNext();
      }

      public Object next() {
         return this.iter.next();
      }

      public void remove() {
         throw new UnsupportedOperationException("iterator is immutable");
      }
   }

   private static final class Iter2Enumeration implements Enumeration {
      private final Iterator iter;

      public Iter2Enumeration(Iterator iter) {
         this.iter = iter;
      }

      public boolean hasMoreElements() {
         return this.iter.hasNext();
      }

      public Object nextElement() {
         return this.iter.next();
      }
   }

   private static final class Enum2Iterator implements Iterator {
      private final Enumeration enumeration;

      public Enum2Iterator(Enumeration enumeration) {
         this.enumeration = enumeration;
      }

      public boolean hasNext() {
         return this.enumeration.hasMoreElements();
      }

      public Object next() {
         return this.enumeration.nextElement();
      }

      public void remove() {
         throw new UnsupportedOperationException("Enumerations are immutable");
      }
   }
}
