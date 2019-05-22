package org.apache.commons.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class FastArrayList extends ArrayList {
   protected ArrayList list = null;
   protected boolean fast = false;

   public FastArrayList() {
      this.list = new ArrayList();
   }

   public FastArrayList(int capacity) {
      this.list = new ArrayList(capacity);
   }

   public FastArrayList(Collection collection) {
      this.list = new ArrayList(collection);
   }

   public boolean getFast() {
      return this.fast;
   }

   public void setFast(boolean fast) {
      this.fast = fast;
   }

   public boolean add(Object element) {
      if (this.fast) {
         synchronized(this) {
            ArrayList temp = (ArrayList)this.list.clone();
            boolean result = temp.add(element);
            this.list = temp;
            return result;
         }
      } else {
         ArrayList var2 = this.list;
         synchronized(var2) {
            boolean var3 = this.list.add(element);
            return var3;
         }
      }
   }

   public void add(int index, Object element) {
      if (this.fast) {
         synchronized(this) {
            ArrayList temp = (ArrayList)this.list.clone();
            temp.add(index, element);
            this.list = temp;
         }
      } else {
         ArrayList var3 = this.list;
         synchronized(var3) {
            this.list.add(index, element);
         }
      }

   }

   public boolean addAll(Collection collection) {
      if (this.fast) {
         synchronized(this) {
            ArrayList temp = (ArrayList)this.list.clone();
            boolean result = temp.addAll(collection);
            this.list = temp;
            return result;
         }
      } else {
         ArrayList var2 = this.list;
         synchronized(var2) {
            boolean var3 = this.list.addAll(collection);
            return var3;
         }
      }
   }

   public boolean addAll(int index, Collection collection) {
      if (this.fast) {
         synchronized(this) {
            ArrayList temp = (ArrayList)this.list.clone();
            boolean result = temp.addAll(index, collection);
            this.list = temp;
            return result;
         }
      } else {
         ArrayList var3 = this.list;
         synchronized(var3) {
            boolean var4 = this.list.addAll(index, collection);
            return var4;
         }
      }
   }

   public void clear() {
      if (this.fast) {
         synchronized(this) {
            ArrayList temp = (ArrayList)this.list.clone();
            temp.clear();
            this.list = temp;
         }
      } else {
         ArrayList var1 = this.list;
         synchronized(var1) {
            this.list.clear();
         }
      }

   }

   public Object clone() {
      FastArrayList results = null;
      if (this.fast) {
         results = new FastArrayList(this.list);
      } else {
         ArrayList var2 = this.list;
         synchronized(var2) {
            results = new FastArrayList(this.list);
         }
      }

      results.setFast(this.getFast());
      return results;
   }

   public boolean contains(Object element) {
      if (this.fast) {
         return this.list.contains(element);
      } else {
         ArrayList var2 = this.list;
         synchronized(var2) {
            boolean var3 = this.list.contains(element);
            return var3;
         }
      }
   }

   public boolean containsAll(Collection collection) {
      if (this.fast) {
         return this.list.containsAll(collection);
      } else {
         ArrayList var2 = this.list;
         synchronized(var2) {
            boolean var3 = this.list.containsAll(collection);
            return var3;
         }
      }
   }

   public void ensureCapacity(int capacity) {
      if (this.fast) {
         synchronized(this) {
            ArrayList temp = (ArrayList)this.list.clone();
            temp.ensureCapacity(capacity);
            this.list = temp;
         }
      } else {
         ArrayList var2 = this.list;
         synchronized(var2) {
            this.list.ensureCapacity(capacity);
         }
      }

   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof List)) {
         return false;
      } else {
         List lo = (List)o;
         ListIterator li1;
         Object o1;
         if (this.fast) {
            ListIterator li1 = this.list.listIterator();
            li1 = lo.listIterator();

            while(li1.hasNext() && li1.hasNext()) {
               Object o1 = li1.next();
               o1 = li1.next();
               if (!(o1 == null ? o1 == null : o1.equals(o1))) {
                  return false;
               }
            }

            return !li1.hasNext() && !li1.hasNext();
         } else {
            ArrayList var3 = this.list;
            synchronized(var3) {
               li1 = this.list.listIterator();
               ListIterator li2 = lo.listIterator();

               while(li1.hasNext() && li2.hasNext()) {
                  o1 = li1.next();
                  Object o2 = li2.next();
                  if (!(o1 == null ? o2 == null : o1.equals(o2))) {
                     boolean var8 = false;
                     return var8;
                  }
               }

               boolean var13 = !li1.hasNext() && !li2.hasNext();
               return var13;
            }
         }
      }
   }

   public Object get(int index) {
      if (this.fast) {
         return this.list.get(index);
      } else {
         ArrayList var2 = this.list;
         synchronized(var2) {
            Object var3 = this.list.get(index);
            return var3;
         }
      }
   }

   public int hashCode() {
      if (this.fast) {
         int hashCode = 1;

         Object o;
         for(Iterator i = this.list.iterator(); i.hasNext(); hashCode = 31 * hashCode + (o == null ? 0 : o.hashCode())) {
            o = i.next();
         }

         return hashCode;
      } else {
         ArrayList var1 = this.list;
         synchronized(var1) {
            int hashCode = 1;

            Object o;
            for(Iterator i = this.list.iterator(); i.hasNext(); hashCode = 31 * hashCode + (o == null ? 0 : o.hashCode())) {
               o = i.next();
            }

            return hashCode;
         }
      }
   }

   public int indexOf(Object element) {
      if (this.fast) {
         return this.list.indexOf(element);
      } else {
         ArrayList var2 = this.list;
         synchronized(var2) {
            int var3 = this.list.indexOf(element);
            return var3;
         }
      }
   }

   public boolean isEmpty() {
      if (this.fast) {
         return this.list.isEmpty();
      } else {
         ArrayList var1 = this.list;
         synchronized(var1) {
            boolean var2 = this.list.isEmpty();
            return var2;
         }
      }
   }

   public Iterator iterator() {
      return (Iterator)(this.fast ? new FastArrayList.ListIter(0) : this.list.iterator());
   }

   public int lastIndexOf(Object element) {
      if (this.fast) {
         return this.list.lastIndexOf(element);
      } else {
         ArrayList var2 = this.list;
         synchronized(var2) {
            int var3 = this.list.lastIndexOf(element);
            return var3;
         }
      }
   }

   public ListIterator listIterator() {
      return (ListIterator)(this.fast ? new FastArrayList.ListIter(0) : this.list.listIterator());
   }

   public ListIterator listIterator(int index) {
      return (ListIterator)(this.fast ? new FastArrayList.ListIter(index) : this.list.listIterator(index));
   }

   public Object remove(int index) {
      if (this.fast) {
         synchronized(this) {
            ArrayList temp = (ArrayList)this.list.clone();
            Object result = temp.remove(index);
            this.list = temp;
            return result;
         }
      } else {
         ArrayList var2 = this.list;
         synchronized(var2) {
            Object var3 = this.list.remove(index);
            return var3;
         }
      }
   }

   public boolean remove(Object element) {
      if (this.fast) {
         synchronized(this) {
            ArrayList temp = (ArrayList)this.list.clone();
            boolean result = temp.remove(element);
            this.list = temp;
            return result;
         }
      } else {
         ArrayList var2 = this.list;
         synchronized(var2) {
            boolean var3 = this.list.remove(element);
            return var3;
         }
      }
   }

   public boolean removeAll(Collection collection) {
      if (this.fast) {
         synchronized(this) {
            ArrayList temp = (ArrayList)this.list.clone();
            boolean result = temp.removeAll(collection);
            this.list = temp;
            return result;
         }
      } else {
         ArrayList var2 = this.list;
         synchronized(var2) {
            boolean var3 = this.list.removeAll(collection);
            return var3;
         }
      }
   }

   public boolean retainAll(Collection collection) {
      if (this.fast) {
         synchronized(this) {
            ArrayList temp = (ArrayList)this.list.clone();
            boolean result = temp.retainAll(collection);
            this.list = temp;
            return result;
         }
      } else {
         ArrayList var2 = this.list;
         synchronized(var2) {
            boolean var3 = this.list.retainAll(collection);
            return var3;
         }
      }
   }

   public Object set(int index, Object element) {
      if (this.fast) {
         return this.list.set(index, element);
      } else {
         ArrayList var3 = this.list;
         synchronized(var3) {
            Object var4 = this.list.set(index, element);
            return var4;
         }
      }
   }

   public int size() {
      if (this.fast) {
         return this.list.size();
      } else {
         ArrayList var1 = this.list;
         synchronized(var1) {
            int var2 = this.list.size();
            return var2;
         }
      }
   }

   public List subList(int fromIndex, int toIndex) {
      return (List)(this.fast ? new FastArrayList.SubList(fromIndex, toIndex) : this.list.subList(fromIndex, toIndex));
   }

   public Object[] toArray() {
      if (this.fast) {
         return this.list.toArray();
      } else {
         ArrayList var1 = this.list;
         synchronized(var1) {
            Object[] var2 = this.list.toArray();
            return var2;
         }
      }
   }

   public Object[] toArray(Object[] array) {
      if (this.fast) {
         return this.list.toArray(array);
      } else {
         ArrayList var2 = this.list;
         synchronized(var2) {
            Object[] var3 = this.list.toArray(array);
            return var3;
         }
      }
   }

   public String toString() {
      StringBuffer sb = new StringBuffer("FastArrayList[");
      sb.append(this.list.toString());
      sb.append("]");
      return sb.toString();
   }

   public void trimToSize() {
      if (this.fast) {
         synchronized(this) {
            ArrayList temp = (ArrayList)this.list.clone();
            temp.trimToSize();
            this.list = temp;
         }
      } else {
         ArrayList var1 = this.list;
         synchronized(var1) {
            this.list.trimToSize();
         }
      }

   }

   private class ListIter implements ListIterator {
      private List expected;
      private ListIterator iter;
      private int lastReturnedIndex = -1;

      public ListIter(int i) {
         this.expected = FastArrayList.this.list;
         this.iter = this.get().listIterator(i);
      }

      private void checkMod() {
         if (FastArrayList.this.list != this.expected) {
            throw new ConcurrentModificationException();
         }
      }

      List get() {
         return this.expected;
      }

      public boolean hasNext() {
         return this.iter.hasNext();
      }

      public Object next() {
         this.lastReturnedIndex = this.iter.nextIndex();
         return this.iter.next();
      }

      public boolean hasPrevious() {
         return this.iter.hasPrevious();
      }

      public Object previous() {
         this.lastReturnedIndex = this.iter.previousIndex();
         return this.iter.previous();
      }

      public int previousIndex() {
         return this.iter.previousIndex();
      }

      public int nextIndex() {
         return this.iter.nextIndex();
      }

      public void remove() {
         this.checkMod();
         if (this.lastReturnedIndex < 0) {
            throw new IllegalStateException();
         } else {
            this.get().remove(this.lastReturnedIndex);
            this.expected = FastArrayList.this.list;
            this.iter = this.get().listIterator(this.lastReturnedIndex);
            this.lastReturnedIndex = -1;
         }
      }

      public void set(Object o) {
         this.checkMod();
         if (this.lastReturnedIndex < 0) {
            throw new IllegalStateException();
         } else {
            this.get().set(this.lastReturnedIndex, o);
            this.expected = FastArrayList.this.list;
            this.iter = this.get().listIterator(this.previousIndex() + 1);
         }
      }

      public void add(Object o) {
         this.checkMod();
         int i = this.nextIndex();
         this.get().add(i, o);
         this.expected = FastArrayList.this.list;
         this.iter = this.get().listIterator(i + 1);
         this.lastReturnedIndex = -1;
      }
   }

   private class SubList implements List {
      private int first;
      private int last;
      private List expected;

      public SubList(int first, int last) {
         this.first = first;
         this.last = last;
         this.expected = FastArrayList.this.list;
      }

      private List get(List l) {
         if (FastArrayList.this.list != this.expected) {
            throw new ConcurrentModificationException();
         } else {
            return l.subList(this.first, this.last);
         }
      }

      public void clear() {
         if (FastArrayList.this.fast) {
            FastArrayList var1 = FastArrayList.this;
            synchronized(var1) {
               ArrayList temp = (ArrayList)FastArrayList.this.list.clone();
               this.get(temp).clear();
               this.last = this.first;
               FastArrayList.this.list = temp;
               this.expected = temp;
            }
         } else {
            ArrayList var7 = FastArrayList.this.list;
            synchronized(var7) {
               this.get(this.expected).clear();
            }
         }

      }

      public boolean remove(Object o) {
         if (FastArrayList.this.fast) {
            FastArrayList var10 = FastArrayList.this;
            synchronized(var10) {
               ArrayList temp = (ArrayList)FastArrayList.this.list.clone();
               boolean r = this.get(temp).remove(o);
               if (r) {
                  --this.last;
               }

               FastArrayList.this.list = temp;
               this.expected = temp;
               return r;
            }
         } else {
            ArrayList var2 = FastArrayList.this.list;
            synchronized(var2) {
               boolean var3 = this.get(this.expected).remove(o);
               return var3;
            }
         }
      }

      public boolean removeAll(Collection o) {
         if (FastArrayList.this.fast) {
            FastArrayList var11 = FastArrayList.this;
            synchronized(var11) {
               ArrayList temp = (ArrayList)FastArrayList.this.list.clone();
               List sub = this.get(temp);
               boolean r = sub.removeAll(o);
               if (r) {
                  this.last = this.first + sub.size();
               }

               FastArrayList.this.list = temp;
               this.expected = temp;
               return r;
            }
         } else {
            ArrayList var2 = FastArrayList.this.list;
            synchronized(var2) {
               boolean var3 = this.get(this.expected).removeAll(o);
               return var3;
            }
         }
      }

      public boolean retainAll(Collection o) {
         if (FastArrayList.this.fast) {
            FastArrayList var11 = FastArrayList.this;
            synchronized(var11) {
               ArrayList temp = (ArrayList)FastArrayList.this.list.clone();
               List sub = this.get(temp);
               boolean r = sub.retainAll(o);
               if (r) {
                  this.last = this.first + sub.size();
               }

               FastArrayList.this.list = temp;
               this.expected = temp;
               return r;
            }
         } else {
            ArrayList var2 = FastArrayList.this.list;
            synchronized(var2) {
               boolean var3 = this.get(this.expected).retainAll(o);
               return var3;
            }
         }
      }

      public int size() {
         if (FastArrayList.this.fast) {
            return this.get(this.expected).size();
         } else {
            ArrayList var1 = FastArrayList.this.list;
            synchronized(var1) {
               int var2 = this.get(this.expected).size();
               return var2;
            }
         }
      }

      public boolean isEmpty() {
         if (FastArrayList.this.fast) {
            return this.get(this.expected).isEmpty();
         } else {
            ArrayList var1 = FastArrayList.this.list;
            synchronized(var1) {
               boolean var2 = this.get(this.expected).isEmpty();
               return var2;
            }
         }
      }

      public boolean contains(Object o) {
         if (FastArrayList.this.fast) {
            return this.get(this.expected).contains(o);
         } else {
            ArrayList var2 = FastArrayList.this.list;
            synchronized(var2) {
               boolean var3 = this.get(this.expected).contains(o);
               return var3;
            }
         }
      }

      public boolean containsAll(Collection o) {
         if (FastArrayList.this.fast) {
            return this.get(this.expected).containsAll(o);
         } else {
            ArrayList var2 = FastArrayList.this.list;
            synchronized(var2) {
               boolean var3 = this.get(this.expected).containsAll(o);
               return var3;
            }
         }
      }

      public Object[] toArray(Object[] o) {
         if (FastArrayList.this.fast) {
            return this.get(this.expected).toArray(o);
         } else {
            ArrayList var2 = FastArrayList.this.list;
            synchronized(var2) {
               Object[] var3 = this.get(this.expected).toArray(o);
               return var3;
            }
         }
      }

      public Object[] toArray() {
         if (FastArrayList.this.fast) {
            return this.get(this.expected).toArray();
         } else {
            ArrayList var1 = FastArrayList.this.list;
            synchronized(var1) {
               Object[] var2 = this.get(this.expected).toArray();
               return var2;
            }
         }
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (FastArrayList.this.fast) {
            return this.get(this.expected).equals(o);
         } else {
            ArrayList var2 = FastArrayList.this.list;
            synchronized(var2) {
               boolean var3 = this.get(this.expected).equals(o);
               return var3;
            }
         }
      }

      public int hashCode() {
         if (FastArrayList.this.fast) {
            return this.get(this.expected).hashCode();
         } else {
            ArrayList var1 = FastArrayList.this.list;
            synchronized(var1) {
               int var2 = this.get(this.expected).hashCode();
               return var2;
            }
         }
      }

      public boolean add(Object o) {
         if (FastArrayList.this.fast) {
            FastArrayList var10 = FastArrayList.this;
            synchronized(var10) {
               ArrayList temp = (ArrayList)FastArrayList.this.list.clone();
               boolean r = this.get(temp).add(o);
               if (r) {
                  ++this.last;
               }

               FastArrayList.this.list = temp;
               this.expected = temp;
               return r;
            }
         } else {
            ArrayList var2 = FastArrayList.this.list;
            synchronized(var2) {
               boolean var3 = this.get(this.expected).add(o);
               return var3;
            }
         }
      }

      public boolean addAll(Collection o) {
         if (FastArrayList.this.fast) {
            FastArrayList var10 = FastArrayList.this;
            synchronized(var10) {
               ArrayList temp = (ArrayList)FastArrayList.this.list.clone();
               boolean r = this.get(temp).addAll(o);
               if (r) {
                  this.last += o.size();
               }

               FastArrayList.this.list = temp;
               this.expected = temp;
               return r;
            }
         } else {
            ArrayList var2 = FastArrayList.this.list;
            synchronized(var2) {
               boolean var3 = this.get(this.expected).addAll(o);
               return var3;
            }
         }
      }

      public void add(int i, Object o) {
         if (FastArrayList.this.fast) {
            FastArrayList var3 = FastArrayList.this;
            synchronized(var3) {
               ArrayList temp = (ArrayList)FastArrayList.this.list.clone();
               this.get(temp).add(i, o);
               ++this.last;
               FastArrayList.this.list = temp;
               this.expected = temp;
            }
         } else {
            ArrayList var9 = FastArrayList.this.list;
            synchronized(var9) {
               this.get(this.expected).add(i, o);
            }
         }

      }

      public boolean addAll(int i, Collection o) {
         if (FastArrayList.this.fast) {
            FastArrayList var11 = FastArrayList.this;
            synchronized(var11) {
               ArrayList temp = (ArrayList)FastArrayList.this.list.clone();
               boolean r = this.get(temp).addAll(i, o);
               FastArrayList.this.list = temp;
               if (r) {
                  this.last += o.size();
               }

               this.expected = temp;
               return r;
            }
         } else {
            ArrayList var3 = FastArrayList.this.list;
            synchronized(var3) {
               boolean var4 = this.get(this.expected).addAll(i, o);
               return var4;
            }
         }
      }

      public Object remove(int i) {
         if (FastArrayList.this.fast) {
            FastArrayList var10 = FastArrayList.this;
            synchronized(var10) {
               ArrayList temp = (ArrayList)FastArrayList.this.list.clone();
               Object o = this.get(temp).remove(i);
               --this.last;
               FastArrayList.this.list = temp;
               this.expected = temp;
               return o;
            }
         } else {
            ArrayList var2 = FastArrayList.this.list;
            synchronized(var2) {
               Object var3 = this.get(this.expected).remove(i);
               return var3;
            }
         }
      }

      public Object set(int i, Object a) {
         if (FastArrayList.this.fast) {
            FastArrayList var11 = FastArrayList.this;
            synchronized(var11) {
               ArrayList temp = (ArrayList)FastArrayList.this.list.clone();
               Object o = this.get(temp).set(i, a);
               FastArrayList.this.list = temp;
               this.expected = temp;
               return o;
            }
         } else {
            ArrayList var3 = FastArrayList.this.list;
            synchronized(var3) {
               Object var4 = this.get(this.expected).set(i, a);
               return var4;
            }
         }
      }

      public Iterator iterator() {
         return new FastArrayList.SubList.SubListIter(0);
      }

      public ListIterator listIterator() {
         return new FastArrayList.SubList.SubListIter(0);
      }

      public ListIterator listIterator(int i) {
         return new FastArrayList.SubList.SubListIter(i);
      }

      public Object get(int i) {
         if (FastArrayList.this.fast) {
            return this.get(this.expected).get(i);
         } else {
            ArrayList var2 = FastArrayList.this.list;
            synchronized(var2) {
               Object var3 = this.get(this.expected).get(i);
               return var3;
            }
         }
      }

      public int indexOf(Object o) {
         if (FastArrayList.this.fast) {
            return this.get(this.expected).indexOf(o);
         } else {
            ArrayList var2 = FastArrayList.this.list;
            synchronized(var2) {
               int var3 = this.get(this.expected).indexOf(o);
               return var3;
            }
         }
      }

      public int lastIndexOf(Object o) {
         if (FastArrayList.this.fast) {
            return this.get(this.expected).lastIndexOf(o);
         } else {
            ArrayList var2 = FastArrayList.this.list;
            synchronized(var2) {
               int var3 = this.get(this.expected).lastIndexOf(o);
               return var3;
            }
         }
      }

      public List subList(int f, int l) {
         if (FastArrayList.this.list != this.expected) {
            throw new ConcurrentModificationException();
         } else {
            return FastArrayList.this.new SubList(this.first + f, f + l);
         }
      }

      private class SubListIter implements ListIterator {
         private List expected;
         private ListIterator iter;
         private int lastReturnedIndex = -1;

         public SubListIter(int i) {
            this.expected = FastArrayList.this.list;
            this.iter = SubList.this.get(this.expected).listIterator(i);
         }

         private void checkMod() {
            if (FastArrayList.this.list != this.expected) {
               throw new ConcurrentModificationException();
            }
         }

         List get() {
            return SubList.this.get(this.expected);
         }

         public boolean hasNext() {
            this.checkMod();
            return this.iter.hasNext();
         }

         public Object next() {
            this.checkMod();
            this.lastReturnedIndex = this.iter.nextIndex();
            return this.iter.next();
         }

         public boolean hasPrevious() {
            this.checkMod();
            return this.iter.hasPrevious();
         }

         public Object previous() {
            this.checkMod();
            this.lastReturnedIndex = this.iter.previousIndex();
            return this.iter.previous();
         }

         public int previousIndex() {
            this.checkMod();
            return this.iter.previousIndex();
         }

         public int nextIndex() {
            this.checkMod();
            return this.iter.nextIndex();
         }

         public void remove() {
            this.checkMod();
            if (this.lastReturnedIndex < 0) {
               throw new IllegalStateException();
            } else {
               this.get().remove(this.lastReturnedIndex);
               SubList.this.last--;
               this.expected = FastArrayList.this.list;
               this.iter = this.get().listIterator(this.lastReturnedIndex);
               this.lastReturnedIndex = -1;
            }
         }

         public void set(Object o) {
            this.checkMod();
            if (this.lastReturnedIndex < 0) {
               throw new IllegalStateException();
            } else {
               this.get().set(this.lastReturnedIndex, o);
               this.expected = FastArrayList.this.list;
               this.iter = this.get().listIterator(this.previousIndex() + 1);
            }
         }

         public void add(Object o) {
            this.checkMod();
            int i = this.nextIndex();
            this.get().add(i, o);
            SubList.this.last++;
            this.expected = FastArrayList.this.list;
            this.iter = this.get().listIterator(i + 1);
            this.lastReturnedIndex = -1;
         }
      }
   }
}
