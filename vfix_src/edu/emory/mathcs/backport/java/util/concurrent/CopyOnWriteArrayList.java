package edu.emory.mathcs.backport.java.util.concurrent;

import edu.emory.mathcs.backport.java.util.Arrays;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

public class CopyOnWriteArrayList implements List, RandomAccess, Cloneable, Serializable {
   private static final long serialVersionUID = 8673264195747942595L;
   private transient volatile Object[] array;

   public CopyOnWriteArrayList() {
      this.setArray(new Object[0]);
   }

   public CopyOnWriteArrayList(Collection c) {
      Object[] array = c.toArray();
      if (array.getClass() != Object[].class) {
         array = Arrays.copyOf(array, array.length, Object[].class);
      }

      this.setArray(array);
   }

   public CopyOnWriteArrayList(Object[] array) {
      this.setArray(Arrays.copyOf(array, array.length, Object[].class));
   }

   final Object[] getArray() {
      return this.array;
   }

   final void setArray(Object[] array) {
      this.array = array;
   }

   public int size() {
      return this.getArray().length;
   }

   public boolean isEmpty() {
      return this.getArray().length == 0;
   }

   private static int search(Object[] array, Object subject, int pos, int end) {
      if (subject == null) {
         while(pos < end) {
            if (array[pos] == null) {
               return pos;
            }

            ++pos;
         }
      } else {
         while(pos < end) {
            if (subject.equals(array[pos])) {
               return pos;
            }

            ++pos;
         }
      }

      return -1;
   }

   private static int reverseSearch(Object[] array, Object subject, int start, int pos) {
      if (subject == null) {
         --pos;

         while(pos >= start) {
            if (array[pos] == null) {
               return pos;
            }

            --pos;
         }
      } else {
         --pos;

         while(pos >= start) {
            if (subject.equals(array[pos])) {
               return pos;
            }

            --pos;
         }
      }

      return -1;
   }

   public boolean contains(Object o) {
      Object[] array = this.getArray();
      return search(array, o, 0, array.length) >= 0;
   }

   public Iterator iterator() {
      return new CopyOnWriteArrayList.COWIterator(this.getArray(), 0);
   }

   public Object[] toArray() {
      Object[] array = this.getArray();
      return Arrays.copyOf(array, array.length, Object[].class);
   }

   public Object[] toArray(Object[] a) {
      Object[] array = this.getArray();
      int length = array.length;
      if (a.length < length) {
         return Arrays.copyOf(array, length, a.getClass());
      } else {
         System.arraycopy(array, 0, a, 0, length);
         if (a.length > length) {
            a[length] = null;
         }

         return a;
      }
   }

   public boolean add(Object o) {
      synchronized(this) {
         Object[] oldarr = this.getArray();
         int length = oldarr.length;
         Object[] newarr = new Object[length + 1];
         System.arraycopy(oldarr, 0, newarr, 0, length);
         newarr[length] = o;
         this.setArray(newarr);
         return true;
      }
   }

   public boolean addIfAbsent(Object o) {
      synchronized(this) {
         Object[] oldarr = this.getArray();
         int length = oldarr.length;
         if (search(this.array, o, 0, length) >= 0) {
            return false;
         } else {
            Object[] newarr = new Object[length + 1];
            System.arraycopy(oldarr, 0, newarr, 0, length);
            newarr[length] = o;
            this.setArray(newarr);
            return true;
         }
      }
   }

   public int addAllAbsent(Collection c) {
      Object[] arr = c.toArray();
      if (arr.length == 0) {
         return 0;
      } else {
         synchronized(this) {
            Object[] oldarr = this.getArray();
            int oldlength = oldarr.length;
            Object[] tmp = new Object[arr.length];
            int added = 0;

            for(int i = 0; i < arr.length; ++i) {
               Object o = arr[i];
               if (search(oldarr, o, 0, oldlength) < 0 && search(tmp, o, 0, added) < 0) {
                  tmp[added++] = o;
               }
            }

            if (added == 0) {
               return 0;
            } else {
               Object[] newarr = new Object[oldlength + added];
               System.arraycopy(oldarr, 0, newarr, 0, oldlength);
               System.arraycopy(tmp, 0, newarr, oldlength, added);
               this.setArray(newarr);
               return added;
            }
         }
      }
   }

   public boolean remove(Object o) {
      synchronized(this) {
         Object[] array = this.getArray();
         int length = array.length;
         int pos = search(array, o, 0, length);
         if (pos < 0) {
            return false;
         } else {
            Object[] newarr = new Object[length - 1];
            int moved = length - pos - 1;
            if (pos > 0) {
               System.arraycopy(array, 0, newarr, 0, pos);
            }

            if (moved > 0) {
               System.arraycopy(array, pos + 1, newarr, pos, moved);
            }

            this.setArray(newarr);
            return true;
         }
      }
   }

   public boolean containsAll(Collection c) {
      Object[] array = this.getArray();
      Iterator itr = c.iterator();

      do {
         if (!itr.hasNext()) {
            return true;
         }
      } while(search(array, itr.next(), 0, array.length) >= 0);

      return false;
   }

   public boolean addAll(Collection c) {
      Object[] ca = c.toArray();
      if (ca.length == 0) {
         return false;
      } else {
         synchronized(this) {
            Object[] oldarr = this.getArray();
            int length = oldarr.length;
            Object[] newarr = new Object[length + ca.length];
            System.arraycopy(oldarr, 0, newarr, 0, length);
            System.arraycopy(ca, 0, newarr, length, ca.length);
            this.setArray(newarr);
            return true;
         }
      }
   }

   public boolean addAll(int index, Collection c) {
      Object[] ca = c.toArray();
      synchronized(this) {
         Object[] oldarr = this.getArray();
         int length = oldarr.length;
         if (index >= 0 && index <= length) {
            if (ca.length == 0) {
               return false;
            } else {
               Object[] newarr = new Object[length + ca.length];
               int moved = length - index;
               System.arraycopy(oldarr, 0, newarr, 0, index);
               System.arraycopy(ca, 0, newarr, index, ca.length);
               if (moved > 0) {
                  System.arraycopy(oldarr, index, newarr, index + ca.length, moved);
               }

               this.setArray(newarr);
               return true;
            }
         } else {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + length);
         }
      }
   }

   public boolean removeAll(Collection c) {
      if (c.isEmpty()) {
         return false;
      } else {
         synchronized(this) {
            Object[] array = this.getArray();
            int length = array.length;
            Object[] tmp = new Object[length];
            int newlen = 0;

            for(int i = 0; i < length; ++i) {
               Object o = array[i];
               if (!c.contains(o)) {
                  tmp[newlen++] = o;
               }
            }

            if (newlen == length) {
               return false;
            } else {
               Object[] newarr = new Object[newlen];
               System.arraycopy(tmp, 0, newarr, 0, newlen);
               this.setArray(newarr);
               return true;
            }
         }
      }
   }

   public boolean retainAll(Collection c) {
      synchronized(this) {
         Object[] array = this.getArray();
         int length = array.length;
         Object[] tmp = new Object[length];
         int newlen = 0;

         for(int i = 0; i < length; ++i) {
            Object o = array[i];
            if (c.contains(o)) {
               tmp[newlen++] = o;
            }
         }

         if (newlen == length) {
            return false;
         } else {
            Object[] newarr = new Object[newlen];
            System.arraycopy(tmp, 0, newarr, 0, newlen);
            this.setArray(newarr);
            return true;
         }
      }
   }

   public void clear() {
      this.setArray(new Object[0]);
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof List)) {
         return false;
      } else {
         ListIterator itr = ((List)o).listIterator();
         Object[] array = this.getArray();
         int length = array.length;
         int idx = 0;

         while(idx < length && itr.hasNext()) {
            Object o1 = array[idx++];
            Object o2 = itr.next();
            if (!eq(o1, o2)) {
               return false;
            }
         }

         return idx == length && !itr.hasNext();
      }
   }

   public int hashCode() {
      int hashCode = 1;
      Object[] array = this.getArray();
      int length = array.length;

      for(int i = 0; i < length; ++i) {
         Object o = array[i];
         hashCode = 31 * hashCode + (o == null ? 0 : o.hashCode());
      }

      return hashCode;
   }

   public Object get(int index) {
      return this.getArray()[index];
   }

   public Object set(int index, Object element) {
      synchronized(this) {
         Object[] oldarr = this.getArray();
         int length = oldarr.length;
         Object oldVal = oldarr[index];
         if (oldVal == element) {
            this.setArray(oldarr);
         } else {
            Object[] newarr = new Object[length];
            System.arraycopy(oldarr, 0, newarr, 0, length);
            newarr[index] = element;
            this.setArray(newarr);
         }

         return oldVal;
      }
   }

   public void add(int index, Object element) {
      synchronized(this) {
         Object[] oldarr = this.getArray();
         int length = oldarr.length;
         if (index >= 0 && index <= length) {
            Object[] newarr = new Object[length + 1];
            int moved = length - index;
            System.arraycopy(oldarr, 0, newarr, 0, index);
            newarr[index] = element;
            if (moved > 0) {
               System.arraycopy(oldarr, index, newarr, index + 1, moved);
            }

            this.setArray(newarr);
         } else {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + length);
         }
      }
   }

   public Object remove(int index) {
      synchronized(this) {
         Object[] array = this.getArray();
         int length = array.length;
         if (index >= 0 && index < length) {
            Object result = array[index];
            Object[] newarr = new Object[length - 1];
            int moved = length - index - 1;
            if (index > 0) {
               System.arraycopy(array, 0, newarr, 0, index);
            }

            if (moved > 0) {
               System.arraycopy(array, index + 1, newarr, index, moved);
            }

            this.setArray(newarr);
            return result;
         } else {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + length);
         }
      }
   }

   public int indexOf(Object o) {
      Object[] array = this.getArray();
      return search(array, o, 0, array.length);
   }

   public int indexOf(Object o, int index) {
      Object[] array = this.getArray();
      return search(array, o, index, array.length);
   }

   public int lastIndexOf(Object o) {
      Object[] array = this.getArray();
      return reverseSearch(array, o, 0, array.length);
   }

   public int lastIndexOf(Object o, int index) {
      Object[] array = this.getArray();
      return reverseSearch(array, o, 0, index);
   }

   public ListIterator listIterator() {
      return new CopyOnWriteArrayList.COWIterator(this.getArray(), 0);
   }

   public ListIterator listIterator(int index) {
      Object[] array = this.getArray();
      if (index >= 0 && index <= array.length) {
         return new CopyOnWriteArrayList.COWIterator(array, index);
      } else {
         throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + array.length);
      }
   }

   public List subList(int fromIndex, int toIndex) {
      Object[] array = this.getArray();
      if (fromIndex >= 0 && toIndex <= array.length && fromIndex <= toIndex) {
         return new CopyOnWriteArrayList.COWSubList(fromIndex, toIndex - fromIndex);
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
      Object[] array = this.getArray();
      int length = array.length;
      out.writeInt(length);

      for(int i = 0; i < length; ++i) {
         out.writeObject(array[i]);
      }

   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      int length = in.readInt();
      Object[] array = new Object[length];

      for(int i = 0; i < length; ++i) {
         array[i] = in.readObject();
      }

      this.setArray(array);
   }

   public String toString() {
      Object[] array = this.getArray();
      int length = array.length;
      StringBuffer buf = new StringBuffer();
      buf.append('[');

      for(int i = 0; i < length; ++i) {
         if (i > 0) {
            buf.append(", ");
         }

         buf.append(array[i]);
      }

      buf.append(']');
      return buf.toString();
   }

   private static boolean eq(Object o1, Object o2) {
      return o1 == null ? o2 == null : o1.equals(o2);
   }

   static class COWSubIterator implements ListIterator {
      final Object[] array;
      int cursor;
      int first;
      int last;

      COWSubIterator(Object[] array, int first, int last, int cursor) {
         this.array = array;
         this.first = first;
         this.last = last;
         this.cursor = cursor;
      }

      public boolean hasNext() {
         return this.cursor < this.last;
      }

      public boolean hasPrevious() {
         return this.cursor > this.first;
      }

      public int nextIndex() {
         return this.cursor - this.first;
      }

      public Object next() {
         if (this.cursor == this.last) {
            throw new NoSuchElementException();
         } else {
            return this.array[this.cursor++];
         }
      }

      public int previousIndex() {
         return this.cursor - this.first - 1;
      }

      public Object previous() {
         if (this.cursor == this.first) {
            throw new NoSuchElementException();
         } else {
            return this.array[--this.cursor];
         }
      }

      public void add(Object val) {
         throw new UnsupportedOperationException();
      }

      public void set(Object val) {
         throw new UnsupportedOperationException();
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }
   }

   class COWSubList implements Serializable, List {
      private static final long serialVersionUID = -8660955369431018984L;
      final int offset;
      int length;
      transient Object[] expectedArray;

      COWSubList(int offset, int length) {
         this.offset = offset;
         this.length = length;
         this.expectedArray = CopyOnWriteArrayList.this.getArray();
      }

      public int size() {
         return this.length;
      }

      public boolean isEmpty() {
         return this.length == 0;
      }

      public boolean contains(Object o) {
         return CopyOnWriteArrayList.search(CopyOnWriteArrayList.this.getArray(), o, this.offset, this.offset + this.length) >= 0;
      }

      public Iterator iterator() {
         return this.listIterator();
      }

      public Object[] toArray() {
         Object[] array = CopyOnWriteArrayList.this.getArray();
         Object[] newarr = new Object[this.length];
         System.arraycopy(array, this.offset, newarr, 0, this.length);
         return newarr;
      }

      public Object[] toArray(Object[] a) {
         Object[] array = CopyOnWriteArrayList.this.getArray();
         if (a.length < this.length) {
            a = (Object[])Array.newInstance(a.getClass().getComponentType(), this.length);
            System.arraycopy(array, this.offset, a, 0, this.length);
         } else {
            System.arraycopy(array, this.offset, a, 0, this.length);
            if (a.length > this.length) {
               a[this.length] = null;
            }
         }

         return a;
      }

      public boolean add(Object o) {
         this.add(this.length, o);
         return true;
      }

      public boolean remove(Object o) {
         synchronized(CopyOnWriteArrayList.this) {
            Object[] array = CopyOnWriteArrayList.this.getArray();
            if (array != this.expectedArray) {
               throw new ConcurrentModificationException();
            } else {
               int fullLength = array.length;
               int pos = CopyOnWriteArrayList.search(array, o, this.offset, this.length);
               if (pos < 0) {
                  return false;
               } else {
                  Object[] newarr = new Object[fullLength - 1];
                  int moved = this.length - pos - 1;
                  if (pos > 0) {
                     System.arraycopy(array, 0, newarr, 0, pos);
                  }

                  if (moved > 0) {
                     System.arraycopy(array, pos + 1, newarr, pos, moved);
                  }

                  CopyOnWriteArrayList.this.setArray(newarr);
                  this.expectedArray = newarr;
                  --this.length;
                  return true;
               }
            }
         }
      }

      public boolean containsAll(Collection c) {
         Object[] array = CopyOnWriteArrayList.this.getArray();
         Iterator itr = c.iterator();

         do {
            if (!itr.hasNext()) {
               return true;
            }
         } while(CopyOnWriteArrayList.search(array, itr.next(), this.offset, this.length) >= 0);

         return false;
      }

      public boolean addAll(Collection c) {
         return this.addAll(this.length, c);
      }

      public boolean addAll(int index, Collection c) {
         int added = c.size();
         synchronized(CopyOnWriteArrayList.this) {
            if (index >= 0 && index < this.length) {
               Object[] oldarr = CopyOnWriteArrayList.this.getArray();
               if (oldarr != this.expectedArray) {
                  throw new ConcurrentModificationException();
               } else if (added == 0) {
                  return false;
               } else {
                  int fullLength = oldarr.length;
                  Object[] newarr = new Object[fullLength + added];
                  int pos = this.offset + index;
                  int newpos = pos;
                  System.arraycopy(oldarr, 0, newarr, 0, pos);
                  int rem = fullLength - pos;

                  for(Iterator itr = c.iterator(); itr.hasNext(); newarr[newpos++] = itr.next()) {
                  }

                  if (rem > 0) {
                     System.arraycopy(oldarr, pos, newarr, newpos, rem);
                  }

                  CopyOnWriteArrayList.this.setArray(newarr);
                  this.expectedArray = newarr;
                  this.length += added;
                  return true;
               }
            } else {
               throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.length);
            }
         }
      }

      public boolean removeAll(Collection c) {
         if (c.isEmpty()) {
            return false;
         } else {
            synchronized(CopyOnWriteArrayList.this) {
               Object[] array = CopyOnWriteArrayList.this.getArray();
               if (array != this.expectedArray) {
                  throw new ConcurrentModificationException();
               } else {
                  int fullLength = array.length;
                  Object[] tmp = new Object[this.length];
                  int retained = 0;

                  for(int i = this.offset; i < this.offset + this.length; ++i) {
                     Object o = array[i];
                     if (!c.contains(o)) {
                        tmp[retained++] = o;
                     }
                  }

                  if (retained == this.length) {
                     return false;
                  } else {
                     Object[] newarr = new Object[fullLength + retained - this.length];
                     int moved = fullLength - this.offset - this.length;
                     if (this.offset > 0) {
                        System.arraycopy(array, 0, newarr, 0, this.offset);
                     }

                     if (retained > 0) {
                        System.arraycopy(tmp, 0, newarr, this.offset, retained);
                     }

                     if (moved > 0) {
                        System.arraycopy(array, this.offset + this.length, newarr, this.offset + retained, moved);
                     }

                     CopyOnWriteArrayList.this.setArray(newarr);
                     this.expectedArray = newarr;
                     this.length = retained;
                     return true;
                  }
               }
            }
         }
      }

      public boolean retainAll(Collection c) {
         synchronized(CopyOnWriteArrayList.this) {
            Object[] array = CopyOnWriteArrayList.this.getArray();
            if (array != this.expectedArray) {
               throw new ConcurrentModificationException();
            } else {
               int fullLength = array.length;
               Object[] tmp = new Object[this.length];
               int retained = 0;

               for(int i = this.offset; i < this.offset + this.length; ++i) {
                  Object o = array[i];
                  if (c.contains(o)) {
                     tmp[retained++] = o;
                  }
               }

               if (retained == this.length) {
                  return false;
               } else {
                  Object[] newarr = new Object[fullLength + retained - this.length];
                  int moved = fullLength - this.offset - this.length;
                  if (this.offset > 0) {
                     System.arraycopy(array, 0, newarr, 0, this.offset);
                  }

                  if (retained > 0) {
                     System.arraycopy(tmp, 0, newarr, this.offset, retained);
                  }

                  if (moved > 0) {
                     System.arraycopy(array, this.offset + this.length, newarr, this.offset + retained, moved);
                  }

                  CopyOnWriteArrayList.this.setArray(newarr);
                  this.expectedArray = newarr;
                  this.length = retained;
                  return true;
               }
            }
         }
      }

      public void clear() {
         synchronized(CopyOnWriteArrayList.this) {
            Object[] array = CopyOnWriteArrayList.this.getArray();
            if (array != this.expectedArray) {
               throw new ConcurrentModificationException();
            } else {
               int fullLength = array.length;
               Object[] newarr = new Object[fullLength - this.length];
               int moved = fullLength - this.offset - this.length;
               if (this.offset > 0) {
                  System.arraycopy(array, 0, newarr, 0, this.offset);
               }

               if (moved > 0) {
                  System.arraycopy(array, this.offset + this.length, newarr, this.offset, moved);
               }

               CopyOnWriteArrayList.this.setArray(newarr);
               this.expectedArray = newarr;
               this.length = 0;
            }
         }
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof List)) {
            return false;
         } else {
            Object[] array;
            int last;
            synchronized(CopyOnWriteArrayList.this) {
               array = CopyOnWriteArrayList.this.getArray();
               if (array != this.expectedArray) {
                  throw new ConcurrentModificationException();
               }

               last = this.offset + this.length;
            }

            ListIterator itr = ((List)o).listIterator();
            int idx = this.offset;

            while(idx < last && itr.hasNext()) {
               Object o1 = array[idx];
               Object o2 = itr.next();
               if (!CopyOnWriteArrayList.eq(o1, o2)) {
                  return false;
               }
            }

            return idx == last && !itr.hasNext();
         }
      }

      public int hashCode() {
         int hashCode = 1;
         Object[] array;
         int last;
         synchronized(CopyOnWriteArrayList.this) {
            array = CopyOnWriteArrayList.this.getArray();
            if (array != this.expectedArray) {
               throw new ConcurrentModificationException();
            }

            last = this.offset + this.length;
         }

         for(int i = this.offset; i < last; ++i) {
            Object o = array[i];
            hashCode = 31 * hashCode + (o == null ? 0 : o.hashCode());
         }

         return hashCode;
      }

      public Object get(int index) {
         return CopyOnWriteArrayList.this.getArray()[this.offset + index];
      }

      public Object set(int index, Object element) {
         synchronized(CopyOnWriteArrayList.this) {
            if (index >= 0 && index < this.length) {
               Object[] oldarr = CopyOnWriteArrayList.this.getArray();
               if (oldarr != this.expectedArray) {
                  throw new ConcurrentModificationException();
               } else {
                  int fullLength = oldarr.length;
                  Object oldVal = oldarr[this.offset + index];
                  if (oldVal == element) {
                     CopyOnWriteArrayList.this.setArray(oldarr);
                  } else {
                     Object[] newarr = new Object[fullLength];
                     System.arraycopy(oldarr, 0, newarr, 0, fullLength);
                     newarr[this.offset + index] = element;
                     CopyOnWriteArrayList.this.setArray(newarr);
                     this.expectedArray = newarr;
                  }

                  return oldVal;
               }
            } else {
               throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.length);
            }
         }
      }

      public void add(int index, Object element) {
         synchronized(CopyOnWriteArrayList.this) {
            if (index >= 0 && index <= this.length) {
               Object[] oldarr = CopyOnWriteArrayList.this.getArray();
               if (oldarr != this.expectedArray) {
                  throw new ConcurrentModificationException();
               } else {
                  int fullLength = oldarr.length;
                  Object[] newarr = new Object[fullLength + 1];
                  int pos = this.offset + index;
                  int moved = fullLength - pos;
                  System.arraycopy(oldarr, 0, newarr, 0, pos);
                  newarr[pos] = element;
                  if (moved > 0) {
                     System.arraycopy(oldarr, pos, newarr, pos + 1, moved);
                  }

                  CopyOnWriteArrayList.this.setArray(newarr);
                  this.expectedArray = newarr;
                  ++this.length;
               }
            } else {
               throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.length);
            }
         }
      }

      public Object remove(int index) {
         synchronized(CopyOnWriteArrayList.this) {
            if (index >= 0 && index < this.length) {
               Object[] array = CopyOnWriteArrayList.this.getArray();
               if (array != this.expectedArray) {
                  throw new ConcurrentModificationException();
               } else {
                  int fullLength = array.length;
                  int pos = this.offset + index;
                  Object result = array[pos];
                  Object[] newarr = new Object[fullLength - 1];
                  int moved = fullLength - pos - 1;
                  if (index > 0) {
                     System.arraycopy(array, 0, newarr, 0, pos);
                  }

                  if (moved > 0) {
                     System.arraycopy(array, pos + 1, newarr, pos, moved);
                  }

                  CopyOnWriteArrayList.this.setArray(newarr);
                  this.expectedArray = newarr;
                  --this.length;
                  return result;
               }
            } else {
               throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.length);
            }
         }
      }

      public int indexOf(Object o) {
         int pos = CopyOnWriteArrayList.search(CopyOnWriteArrayList.this.getArray(), o, this.offset, this.offset + this.length);
         return pos >= 0 ? pos - this.offset : -1;
      }

      public int indexOf(Object o, int index) {
         int pos = CopyOnWriteArrayList.search(CopyOnWriteArrayList.this.getArray(), o, this.offset + index, this.offset + this.length) - this.offset;
         return pos >= 0 ? pos - this.offset : -1;
      }

      public int lastIndexOf(Object o) {
         int pos = CopyOnWriteArrayList.reverseSearch(CopyOnWriteArrayList.this.getArray(), o, this.offset, this.offset + this.length) - this.offset;
         return pos >= 0 ? pos - this.offset : -1;
      }

      public int lastIndexOf(Object o, int index) {
         int pos = CopyOnWriteArrayList.reverseSearch(CopyOnWriteArrayList.this.getArray(), o, this.offset, this.offset + index) - this.offset;
         return pos >= 0 ? pos - this.offset : -1;
      }

      public ListIterator listIterator() {
         synchronized(CopyOnWriteArrayList.this) {
            Object[] array = CopyOnWriteArrayList.this.getArray();
            if (array != this.expectedArray) {
               throw new ConcurrentModificationException();
            } else {
               return new CopyOnWriteArrayList.COWSubIterator(array, this.offset, this.offset + this.length, this.offset);
            }
         }
      }

      public ListIterator listIterator(int index) {
         synchronized(CopyOnWriteArrayList.this) {
            if (index >= 0 && index < this.length) {
               Object[] array = CopyOnWriteArrayList.this.getArray();
               if (array != this.expectedArray) {
                  throw new ConcurrentModificationException();
               } else {
                  return new CopyOnWriteArrayList.COWSubIterator(array, this.offset, this.offset + this.length, this.offset + index);
               }
            } else {
               throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.length);
            }
         }
      }

      public List subList(int fromIndex, int toIndex) {
         if (fromIndex >= 0 && toIndex <= this.length && fromIndex <= toIndex) {
            return CopyOnWriteArrayList.this.new COWSubList(this.offset + fromIndex, toIndex - fromIndex);
         } else {
            throw new IndexOutOfBoundsException();
         }
      }

      public String toString() {
         Object[] array;
         int last;
         synchronized(CopyOnWriteArrayList.this) {
            array = CopyOnWriteArrayList.this.getArray();
            if (array != this.expectedArray) {
               throw new ConcurrentModificationException();
            }

            last = this.offset + this.length;
         }

         StringBuffer buf = new StringBuffer();
         buf.append('[');

         for(int i = this.offset; i < last; ++i) {
            if (i > this.offset) {
               buf.append(", ");
            }

            buf.append(array[i]);
         }

         buf.append(']');
         return buf.toString();
      }

      private void writeObject(ObjectOutputStream out) throws IOException {
         synchronized(CopyOnWriteArrayList.this) {
            if (CopyOnWriteArrayList.this.getArray() != this.expectedArray) {
               throw new ConcurrentModificationException();
            }
         }

         out.defaultWriteObject();
         synchronized(CopyOnWriteArrayList.this) {
            if (CopyOnWriteArrayList.this.getArray() != this.expectedArray) {
               throw new ConcurrentModificationException();
            }
         }
      }

      private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
         in.defaultReadObject();
         synchronized(CopyOnWriteArrayList.this) {
            this.expectedArray = CopyOnWriteArrayList.this.getArray();
         }
      }
   }

   static class COWIterator implements ListIterator {
      final Object[] array;
      int cursor;

      COWIterator(Object[] array, int cursor) {
         this.array = array;
         this.cursor = cursor;
      }

      public boolean hasNext() {
         return this.cursor < this.array.length;
      }

      public boolean hasPrevious() {
         return this.cursor > 0;
      }

      public int nextIndex() {
         return this.cursor;
      }

      public Object next() {
         try {
            return this.array[this.cursor++];
         } catch (IndexOutOfBoundsException var2) {
            --this.cursor;
            throw new NoSuchElementException();
         }
      }

      public int previousIndex() {
         return this.cursor - 1;
      }

      public Object previous() {
         try {
            return this.array[--this.cursor];
         } catch (IndexOutOfBoundsException var2) {
            ++this.cursor;
            throw new NoSuchElementException();
         }
      }

      public void add(Object val) {
         throw new UnsupportedOperationException();
      }

      public void set(Object val) {
         throw new UnsupportedOperationException();
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }
   }
}
