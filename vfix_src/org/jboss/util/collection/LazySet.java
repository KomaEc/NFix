package org.jboss.util.collection;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class LazySet<T> implements Set<T>, Serializable {
   private static final long serialVersionUID = 1L;
   private Set<T> delegate = Collections.emptySet();

   private Set<T> createImplementation() {
      return (Set)(!(this.delegate instanceof HashSet) ? new HashSet(this.delegate) : this.delegate);
   }

   public boolean add(T o) {
      if (this.delegate.isEmpty()) {
         this.delegate = Collections.singleton(o);
         return true;
      } else {
         this.delegate = this.createImplementation();
         return this.delegate.add(o);
      }
   }

   public boolean addAll(Collection<? extends T> c) {
      this.delegate = this.createImplementation();
      return this.delegate.addAll(c);
   }

   public void clear() {
      this.delegate = Collections.emptySet();
   }

   public boolean contains(Object o) {
      return this.delegate.contains(o);
   }

   public boolean containsAll(Collection<?> c) {
      return this.delegate.containsAll(c);
   }

   public boolean isEmpty() {
      return this.delegate.isEmpty();
   }

   public Iterator<T> iterator() {
      return this.delegate.iterator();
   }

   public boolean remove(Object o) {
      this.delegate = this.createImplementation();
      return this.delegate.remove(o);
   }

   public boolean removeAll(Collection<?> c) {
      this.delegate = this.createImplementation();
      return this.delegate.removeAll(c);
   }

   public boolean retainAll(Collection<?> c) {
      this.delegate = this.createImplementation();
      return this.delegate.retainAll(c);
   }

   public int size() {
      return this.delegate.size();
   }

   public Object[] toArray() {
      return this.delegate.toArray();
   }

   public <U> U[] toArray(U[] a) {
      return this.delegate.toArray(a);
   }

   public String toString() {
      return this.delegate.toString();
   }
}
