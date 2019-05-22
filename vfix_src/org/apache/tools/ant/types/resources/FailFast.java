package org.apache.tools.ant.types.resources;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.WeakHashMap;

class FailFast implements Iterator {
   private static final WeakHashMap MAP = new WeakHashMap();
   private Object parent;
   private Iterator wrapped;

   static synchronized void invalidate(Object o) {
      Set s = (Set)((Set)MAP.get(o));
      if (s != null) {
         s.clear();
      }

   }

   private static synchronized void add(FailFast f) {
      Set s = (Set)((Set)MAP.get(f.parent));
      if (s == null) {
         s = new HashSet();
         MAP.put(f.parent, s);
      }

      ((Set)s).add(f);
   }

   private static synchronized void remove(FailFast f) {
      Set s = (Set)((Set)MAP.get(f.parent));
      if (s != null) {
         s.remove(f);
      }

   }

   private static synchronized void failFast(FailFast f) {
      Set s = (Set)((Set)MAP.get(f.parent));
      if (!s.contains(f)) {
         throw new ConcurrentModificationException();
      }
   }

   FailFast(Object o, Iterator i) {
      if (o == null) {
         throw new IllegalArgumentException("parent object is null");
      } else if (i == null) {
         throw new IllegalArgumentException("cannot wrap null iterator");
      } else {
         this.parent = o;
         if (i.hasNext()) {
            this.wrapped = i;
            add(this);
         }

      }
   }

   public boolean hasNext() {
      if (this.wrapped == null) {
         return false;
      } else {
         failFast(this);
         return this.wrapped.hasNext();
      }
   }

   public Object next() {
      if (this.wrapped != null && this.wrapped.hasNext()) {
         failFast(this);

         Object var1;
         try {
            var1 = this.wrapped.next();
         } finally {
            if (!this.wrapped.hasNext()) {
               this.wrapped = null;
               remove(this);
            }

         }

         return var1;
      } else {
         throw new NoSuchElementException();
      }
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }
}
