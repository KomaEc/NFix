package com.gzoltar.shaded.org.pitest.functional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public final class MutableList<A> implements FunctionalList<A> {
   private final List<A> impl;

   public MutableList(A... as) {
      this(Arrays.asList(as));
   }

   public MutableList(List<A> impl) {
      this.impl = impl;
   }

   public MutableList() {
      this((List)(new ArrayList()));
   }

   public boolean add(A o) {
      return this.impl.add(o);
   }

   public boolean addAll(Collection<? extends A> c) {
      return this.impl.addAll(c);
   }

   public void clear() {
      this.impl.clear();
   }

   public boolean contains(Object o) {
      return this.impl.contains(o);
   }

   public boolean containsAll(Collection<?> c) {
      return this.impl.containsAll(c);
   }

   public boolean isEmpty() {
      return this.impl.isEmpty();
   }

   public Iterator<A> iterator() {
      return this.impl.iterator();
   }

   public boolean remove(Object o) {
      return this.impl.remove(o);
   }

   public boolean removeAll(Collection<?> c) {
      return this.impl.removeAll(c);
   }

   public boolean retainAll(Collection<?> c) {
      return this.impl.retainAll(c);
   }

   public int size() {
      return this.impl.size();
   }

   public Object[] toArray() {
      return this.impl.toArray();
   }

   public <T> T[] toArray(T[] a) {
      return this.impl.toArray(a);
   }

   public boolean contains(F<A, Boolean> predicate) {
      return FCollection.contains(this, predicate);
   }

   public FunctionalList<A> filter(F<A, Boolean> predicate) {
      return FCollection.filter(this, predicate);
   }

   public <B> FunctionalList<B> flatMap(F<A, ? extends Iterable<B>> f) {
      return FCollection.flatMap(this, f);
   }

   public void forEach(SideEffect1<A> e) {
      FCollection.forEach(this, e);
   }

   public <B> FunctionalList<B> map(F<A, B> f) {
      return FCollection.map(this, f);
   }

   public <B> void mapTo(F<A, B> f, Collection<? super B> bs) {
      FCollection.mapTo(this, f, bs);
   }

   public void add(int arg0, A arg1) {
      this.impl.add(arg0, arg1);
   }

   public boolean addAll(int arg0, Collection<? extends A> arg1) {
      return this.impl.addAll(arg0, arg1);
   }

   public A get(int index) {
      return this.impl.get(index);
   }

   public int indexOf(Object arg0) {
      return this.impl.indexOf(arg0);
   }

   public int lastIndexOf(Object arg0) {
      return this.impl.lastIndexOf(arg0);
   }

   public ListIterator<A> listIterator() {
      return this.impl.listIterator();
   }

   public ListIterator<A> listIterator(int index) {
      return this.impl.listIterator(index);
   }

   public A remove(int index) {
      return this.impl.remove(index);
   }

   public A set(int index, A element) {
      return this.impl.set(index, element);
   }

   public List<A> subList(int fromIndex, int toIndex) {
      return this.impl.subList(fromIndex, toIndex);
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.impl == null ? 0 : this.impl.hashCode());
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
         MutableList other = (MutableList)obj;
         if (this.impl == null) {
            if (other.impl != null) {
               return false;
            }
         } else if (!this.impl.equals(other.impl)) {
            return false;
         }

         return true;
      }
   }

   public String toString() {
      return this.impl.toString();
   }
}
