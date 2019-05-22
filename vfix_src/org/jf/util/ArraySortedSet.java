package org.jf.util;

import com.google.common.collect.Iterators;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nonnull;

public class ArraySortedSet<T> implements SortedSet<T> {
   @Nonnull
   private final Comparator<? super T> comparator;
   @Nonnull
   private final Object[] arr;

   private ArraySortedSet(@Nonnull Comparator<? super T> comparator, @Nonnull T[] arr) {
      this.comparator = comparator;
      this.arr = arr;
   }

   public static <T> ArraySortedSet<T> of(@Nonnull Comparator<? super T> comparator, @Nonnull T[] arr) {
      return new ArraySortedSet(comparator, arr);
   }

   public int size() {
      return this.arr.length;
   }

   public boolean isEmpty() {
      return this.arr.length > 0;
   }

   public boolean contains(Object o) {
      return Arrays.binarySearch((Object[])this.arr, o, this.comparator) >= 0;
   }

   public Iterator<T> iterator() {
      return Iterators.forArray((Object[])this.arr);
   }

   public Object[] toArray() {
      return (Object[])this.arr.clone();
   }

   public <T> T[] toArray(T[] a) {
      if (a.length <= this.arr.length) {
         System.arraycopy(this.arr, 0, (Object[])a, 0, this.arr.length);
         return a;
      } else {
         return Arrays.copyOf((Object[])this.arr, this.arr.length);
      }
   }

   public boolean add(T t) {
      throw new UnsupportedOperationException();
   }

   public boolean remove(Object o) {
      throw new UnsupportedOperationException();
   }

   public boolean containsAll(Collection<?> c) {
      Iterator var2 = c.iterator();

      Object o;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         o = var2.next();
      } while(this.contains(o));

      return false;
   }

   public boolean addAll(Collection<? extends T> c) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(Collection<?> c) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(Collection<?> c) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }

   public Comparator<? super T> comparator() {
      return this.comparator;
   }

   public SortedSet<T> subSet(T fromElement, T toElement) {
      throw new UnsupportedOperationException();
   }

   public SortedSet<T> headSet(T toElement) {
      throw new UnsupportedOperationException();
   }

   public SortedSet<T> tailSet(T fromElement) {
      throw new UnsupportedOperationException();
   }

   public T first() {
      if (this.arr.length == 0) {
         throw new NoSuchElementException();
      } else {
         return this.arr[0];
      }
   }

   public T last() {
      if (this.arr.length == 0) {
         throw new NoSuchElementException();
      } else {
         return this.arr[this.arr.length - 1];
      }
   }

   public int hashCode() {
      int result = 0;
      Object[] var2 = this.arr;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object o = var2[var4];
         result += o.hashCode();
      }

      return result;
   }

   public boolean equals(Object o) {
      if (o == null) {
         return false;
      } else if (o instanceof SortedSet) {
         SortedSet other = (SortedSet)o;
         return this.arr.length != other.size() ? false : Iterators.elementsEqual(this.iterator(), other.iterator());
      } else if (o instanceof Set) {
         Set other = (Set)o;
         return this.arr.length != other.size() ? false : this.containsAll(other);
      } else {
         return false;
      }
   }
}
