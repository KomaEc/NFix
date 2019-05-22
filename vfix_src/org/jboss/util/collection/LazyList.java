package org.jboss.util.collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class LazyList<T> implements List<T>, Serializable {
   private static final long serialVersionUID = 1L;
   private List<T> delegate = Collections.emptyList();

   private List<T> createImplementation() {
      return (List)(!(this.delegate instanceof ArrayList) ? new ArrayList(this.delegate) : this.delegate);
   }

   public void add(int index, T element) {
      this.delegate = this.createImplementation();
      this.delegate.add(index, element);
   }

   public boolean add(T o) {
      if (this.delegate.isEmpty()) {
         this.delegate = Collections.singletonList(o);
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

   public boolean addAll(int index, Collection<? extends T> c) {
      this.delegate = this.createImplementation();
      return this.delegate.addAll(index, c);
   }

   public void clear() {
      this.delegate = Collections.emptyList();
   }

   public boolean contains(Object o) {
      return this.delegate.contains(o);
   }

   public boolean containsAll(Collection<?> c) {
      return this.delegate.containsAll(c);
   }

   public T get(int index) {
      return this.delegate.get(index);
   }

   public int indexOf(Object o) {
      return this.delegate.indexOf(o);
   }

   public boolean isEmpty() {
      return this.delegate.isEmpty();
   }

   public Iterator<T> iterator() {
      return this.delegate.iterator();
   }

   public int lastIndexOf(Object o) {
      return this.delegate.lastIndexOf(o);
   }

   public ListIterator<T> listIterator() {
      return this.delegate.listIterator();
   }

   public ListIterator<T> listIterator(int index) {
      return this.delegate.listIterator(index);
   }

   public T remove(int index) {
      this.delegate = this.createImplementation();
      return this.delegate.remove(index);
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

   public T set(int index, T element) {
      this.delegate = this.createImplementation();
      return this.delegate.set(index, element);
   }

   public int size() {
      return this.delegate.size();
   }

   public List<T> subList(int fromIndex, int toIndex) {
      return this.delegate.subList(fromIndex, toIndex);
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
