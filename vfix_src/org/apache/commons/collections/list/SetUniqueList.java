package org.apache.commons.collections.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import org.apache.commons.collections.iterators.AbstractIteratorDecorator;
import org.apache.commons.collections.iterators.AbstractListIteratorDecorator;
import org.apache.commons.collections.set.UnmodifiableSet;

public class SetUniqueList extends AbstractSerializableListDecorator {
   private static final long serialVersionUID = 7196982186153478694L;
   protected final Set set;

   public static SetUniqueList decorate(List list) {
      if (list == null) {
         throw new IllegalArgumentException("List must not be null");
      } else if (list.isEmpty()) {
         return new SetUniqueList(list, new HashSet());
      } else {
         List temp = new ArrayList(list);
         list.clear();
         SetUniqueList sl = new SetUniqueList(list, new HashSet());
         sl.addAll(temp);
         return sl;
      }
   }

   protected SetUniqueList(List list, Set set) {
      super(list);
      if (set == null) {
         throw new IllegalArgumentException("Set must not be null");
      } else {
         this.set = set;
      }
   }

   public Set asSet() {
      return UnmodifiableSet.decorate(this.set);
   }

   public boolean add(Object object) {
      int sizeBefore = this.size();
      this.add(this.size(), object);
      return sizeBefore != this.size();
   }

   public void add(int index, Object object) {
      if (!this.set.contains(object)) {
         super.add(index, object);
         this.set.add(object);
      }

   }

   public boolean addAll(Collection coll) {
      return this.addAll(this.size(), coll);
   }

   public boolean addAll(int index, Collection coll) {
      int sizeBefore = this.size();
      Iterator it = coll.iterator();

      while(it.hasNext()) {
         this.add(it.next());
      }

      return sizeBefore != this.size();
   }

   public Object set(int index, Object object) {
      int pos = this.indexOf(object);
      Object removed = super.set(index, object);
      if (pos != -1 && pos != index) {
         super.remove(pos);
         this.set.remove(removed);
         return removed;
      } else {
         return removed;
      }
   }

   public boolean remove(Object object) {
      boolean result = super.remove(object);
      this.set.remove(object);
      return result;
   }

   public Object remove(int index) {
      Object result = super.remove(index);
      this.set.remove(result);
      return result;
   }

   public boolean removeAll(Collection coll) {
      boolean result = super.removeAll(coll);
      this.set.removeAll(coll);
      return result;
   }

   public boolean retainAll(Collection coll) {
      boolean result = super.retainAll(coll);
      this.set.retainAll(coll);
      return result;
   }

   public void clear() {
      super.clear();
      this.set.clear();
   }

   public boolean contains(Object object) {
      return this.set.contains(object);
   }

   public boolean containsAll(Collection coll) {
      return this.set.containsAll(coll);
   }

   public Iterator iterator() {
      return new SetUniqueList.SetListIterator(super.iterator(), this.set);
   }

   public ListIterator listIterator() {
      return new SetUniqueList.SetListListIterator(super.listIterator(), this.set);
   }

   public ListIterator listIterator(int index) {
      return new SetUniqueList.SetListListIterator(super.listIterator(index), this.set);
   }

   public List subList(int fromIndex, int toIndex) {
      return new SetUniqueList(super.subList(fromIndex, toIndex), this.set);
   }

   static class SetListListIterator extends AbstractListIteratorDecorator {
      protected final Set set;
      protected Object last = null;

      protected SetListListIterator(ListIterator it, Set set) {
         super(it);
         this.set = set;
      }

      public Object next() {
         this.last = super.next();
         return this.last;
      }

      public Object previous() {
         this.last = super.previous();
         return this.last;
      }

      public void remove() {
         super.remove();
         this.set.remove(this.last);
         this.last = null;
      }

      public void add(Object object) {
         if (!this.set.contains(object)) {
            super.add(object);
            this.set.add(object);
         }

      }

      public void set(Object object) {
         throw new UnsupportedOperationException("ListIterator does not support set");
      }
   }

   static class SetListIterator extends AbstractIteratorDecorator {
      protected final Set set;
      protected Object last = null;

      protected SetListIterator(Iterator it, Set set) {
         super(it);
         this.set = set;
      }

      public Object next() {
         this.last = super.next();
         return this.last;
      }

      public void remove() {
         super.remove();
         this.set.remove(this.last);
         this.last = null;
      }
   }
}
