package soot.jimple.spark.ondemand.genericutil;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArraySet<T> extends AbstractSet<T> {
   private static final ArraySet EMPTY = new ArraySet<Object>(0, true) {
      public boolean add(Object obj_) {
         throw new RuntimeException();
      }
   };
   private T[] _elems;
   private int _curIndex;
   private final boolean checkDupes;

   public static final <T> ArraySet<T> empty() {
      return EMPTY;
   }

   public ArraySet(int numElems_, boolean checkDupes) {
      this._curIndex = 0;
      this._elems = (Object[])(new Object[numElems_]);
      this.checkDupes = checkDupes;
   }

   public ArraySet() {
      this(1, true);
   }

   public ArraySet(ArraySet<T> other) {
      this._curIndex = 0;
      int size = other._curIndex;
      this._elems = (Object[])(new Object[size]);
      this.checkDupes = other.checkDupes;
      this._curIndex = size;
      System.arraycopy(other._elems, 0, this._elems, 0, size);
   }

   public ArraySet(Collection<T> other) {
      this(other.size(), true);
      this.addAll(other);
   }

   public boolean add(T obj_) {
      assert obj_ != null;

      if (this.checkDupes && this.contains(obj_)) {
         return false;
      } else {
         if (this._curIndex == this._elems.length) {
            Object[] tmp = this._elems;
            this._elems = (Object[])(new Object[tmp.length * 2]);
            System.arraycopy(tmp, 0, this._elems, 0, tmp.length);
         }

         this._elems[this._curIndex] = obj_;
         ++this._curIndex;
         return true;
      }
   }

   public boolean addAll(ArraySet<T> other) {
      boolean ret = false;

      for(int i = 0; i < other.size(); ++i) {
         boolean added = this.add(other.get(i));
         ret = ret || added;
      }

      return ret;
   }

   public boolean contains(Object obj_) {
      for(int i = 0; i < this._curIndex; ++i) {
         if (this._elems[i].equals(obj_)) {
            return true;
         }
      }

      return false;
   }

   public boolean intersects(ArraySet<T> other) {
      for(int i = 0; i < other.size(); ++i) {
         if (this.contains(other.get(i))) {
            return true;
         }
      }

      return false;
   }

   public void forall(ObjectVisitor<T> visitor_) {
      for(int i = 0; i < this._curIndex; ++i) {
         visitor_.visit(this._elems[i]);
      }

   }

   public int size() {
      return this._curIndex;
   }

   public T get(int i) {
      return this._elems[i];
   }

   public boolean remove(Object obj_) {
      int ind;
      for(ind = 0; ind < this._curIndex && !this._elems[ind].equals(obj_); ++ind) {
      }

      return ind == this._curIndex ? false : this.remove(ind);
   }

   public boolean remove(int ind) {
      System.arraycopy(this._elems, ind + 1, this._elems, ind, this._curIndex - (ind + 1));
      --this._curIndex;
      return true;
   }

   public void clear() {
      this._curIndex = 0;
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public String toString() {
      StringBuffer ret = new StringBuffer();
      ret.append('[');

      for(int i = 0; i < this.size(); ++i) {
         ret.append(this.get(i).toString());
         if (i + 1 < this.size()) {
            ret.append(", ");
         }
      }

      ret.append(']');
      return ret.toString();
   }

   public Object[] toArray() {
      throw new UnsupportedOperationException();
   }

   public boolean addAll(Collection<? extends T> c) {
      boolean ret = false;

      boolean added;
      for(Iterator var3 = c.iterator(); var3.hasNext(); ret = ret || added) {
         T element = var3.next();
         added = this.add(element);
      }

      return ret;
   }

   public Iterator<T> iterator() {
      return new ArraySet.ArraySetIterator();
   }

   public <U> U[] toArray(U[] a) {
      for(int i = 0; i < this._curIndex; ++i) {
         T t = this._elems[i];
         a[i] = t;
      }

      return a;
   }

   public class ArraySetIterator implements Iterator<T> {
      int ind = 0;
      final int setSize = ArraySet.this.size();

      public void remove() {
         throw new UnsupportedOperationException();
      }

      public boolean hasNext() {
         return this.ind < this.setSize;
      }

      public T next() {
         if (this.ind >= this.setSize) {
            throw new NoSuchElementException();
         } else {
            return ArraySet.this.get(this.ind++);
         }
      }
   }
}
