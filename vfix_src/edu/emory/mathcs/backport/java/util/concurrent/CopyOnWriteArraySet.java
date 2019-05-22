package edu.emory.mathcs.backport.java.util.concurrent;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class CopyOnWriteArraySet extends AbstractSet implements Serializable {
   private static final long serialVersionUID = 5457747651344034263L;
   private final CopyOnWriteArrayList al = new CopyOnWriteArrayList();

   public CopyOnWriteArraySet() {
   }

   public CopyOnWriteArraySet(Collection c) {
      this.al.addAllAbsent(c);
   }

   public int size() {
      return this.al.size();
   }

   public boolean isEmpty() {
      return this.al.isEmpty();
   }

   public boolean contains(Object o) {
      return this.al.contains(o);
   }

   public Object[] toArray() {
      return this.al.toArray();
   }

   public Object[] toArray(Object[] a) {
      return this.al.toArray(a);
   }

   public void clear() {
      this.al.clear();
   }

   public boolean remove(Object o) {
      return this.al.remove(o);
   }

   public boolean add(Object e) {
      return this.al.addIfAbsent(e);
   }

   public boolean containsAll(Collection c) {
      return this.al.containsAll(c);
   }

   public boolean addAll(Collection c) {
      return this.al.addAllAbsent(c) > 0;
   }

   public boolean removeAll(Collection c) {
      return this.al.removeAll(c);
   }

   public boolean retainAll(Collection c) {
      return this.al.retainAll(c);
   }

   public Iterator iterator() {
      return this.al.iterator();
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Set)) {
         return false;
      } else {
         Set set = (Set)o;
         Iterator it = set.iterator();
         Object[] elements = this.al.getArray();
         int len = elements.length;
         boolean[] matched = new boolean[len];

         int k;
         int i;
         label42:
         for(k = 0; it.hasNext(); matched[i] = true) {
            ++k;
            if (k > len) {
               return false;
            }

            Object x = it.next();

            for(i = 0; i < len; ++i) {
               if (!matched[i] && eq(x, elements[i])) {
                  continue label42;
               }
            }

            return false;
         }

         return k == len;
      }
   }

   private static boolean eq(Object o1, Object o2) {
      return o1 == null ? o2 == null : o1.equals(o2);
   }
}
