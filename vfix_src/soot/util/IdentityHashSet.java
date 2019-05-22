package soot.util;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;

/** @deprecated */
@Deprecated
public class IdentityHashSet<E> extends AbstractSet<E> implements Set<E> {
   protected IdentityHashMap<E, E> delegate = new IdentityHashMap();

   public IdentityHashSet() {
   }

   public IdentityHashSet(Collection<E> original) {
      this.addAll(original);
   }

   public int size() {
      return this.delegate.size();
   }

   public boolean contains(Object o) {
      return this.delegate.containsKey(o);
   }

   public Iterator<E> iterator() {
      return this.delegate.keySet().iterator();
   }

   public boolean add(E o) {
      return this.delegate.put(o, o) == null;
   }

   public boolean remove(Object o) {
      return this.delegate.remove(o) != null;
   }

   public void clear() {
      this.delegate.entrySet().clear();
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      int result = 31 * result + (this.delegate == null ? 0 : this.delegate.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         IdentityHashSet<?> other = (IdentityHashSet)obj;
         if (this.delegate == null) {
            if (other.delegate != null) {
               return false;
            }
         } else if (!this.delegate.equals(other.delegate)) {
            return false;
         }

         return true;
      }
   }

   public String toString() {
      return this.delegate.keySet().toString();
   }
}
