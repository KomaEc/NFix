package org.apache.commons.collections;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/** @deprecated */
class CursorableSubList extends CursorableLinkedList implements List {
   protected CursorableLinkedList _list = null;
   protected CursorableLinkedList.Listable _pre = null;
   protected CursorableLinkedList.Listable _post = null;

   CursorableSubList(CursorableLinkedList list, int from, int to) {
      if (0 <= from && list.size() >= to) {
         if (from > to) {
            throw new IllegalArgumentException();
         } else {
            this._list = list;
            if (from < list.size()) {
               super._head.setNext(this._list.getListableAt(from));
               this._pre = null == super._head.next() ? null : super._head.next().prev();
            } else {
               this._pre = this._list.getListableAt(from - 1);
            }

            if (from == to) {
               super._head.setNext((CursorableLinkedList.Listable)null);
               super._head.setPrev((CursorableLinkedList.Listable)null);
               if (to < list.size()) {
                  this._post = this._list.getListableAt(to);
               } else {
                  this._post = null;
               }
            } else {
               super._head.setPrev(this._list.getListableAt(to - 1));
               this._post = super._head.prev().next();
            }

            super._size = to - from;
            super._modCount = this._list._modCount;
         }
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public void clear() {
      this.checkForComod();
      Iterator it = this.iterator();

      while(it.hasNext()) {
         it.next();
         it.remove();
      }

   }

   public Iterator iterator() {
      this.checkForComod();
      return super.iterator();
   }

   public int size() {
      this.checkForComod();
      return super.size();
   }

   public boolean isEmpty() {
      this.checkForComod();
      return super.isEmpty();
   }

   public Object[] toArray() {
      this.checkForComod();
      return super.toArray();
   }

   public Object[] toArray(Object[] a) {
      this.checkForComod();
      return super.toArray(a);
   }

   public boolean contains(Object o) {
      this.checkForComod();
      return super.contains(o);
   }

   public boolean remove(Object o) {
      this.checkForComod();
      return super.remove(o);
   }

   public Object removeFirst() {
      this.checkForComod();
      return super.removeFirst();
   }

   public Object removeLast() {
      this.checkForComod();
      return super.removeLast();
   }

   public boolean addAll(Collection c) {
      this.checkForComod();
      return super.addAll(c);
   }

   public boolean add(Object o) {
      this.checkForComod();
      return super.add(o);
   }

   public boolean addFirst(Object o) {
      this.checkForComod();
      return super.addFirst(o);
   }

   public boolean addLast(Object o) {
      this.checkForComod();
      return super.addLast(o);
   }

   public boolean removeAll(Collection c) {
      this.checkForComod();
      return super.removeAll(c);
   }

   public boolean containsAll(Collection c) {
      this.checkForComod();
      return super.containsAll(c);
   }

   public boolean addAll(int index, Collection c) {
      this.checkForComod();
      return super.addAll(index, c);
   }

   public int hashCode() {
      this.checkForComod();
      return super.hashCode();
   }

   public boolean retainAll(Collection c) {
      this.checkForComod();
      return super.retainAll(c);
   }

   public Object set(int index, Object element) {
      this.checkForComod();
      return super.set(index, element);
   }

   public boolean equals(Object o) {
      this.checkForComod();
      return super.equals(o);
   }

   public Object get(int index) {
      this.checkForComod();
      return super.get(index);
   }

   public Object getFirst() {
      this.checkForComod();
      return super.getFirst();
   }

   public Object getLast() {
      this.checkForComod();
      return super.getLast();
   }

   public void add(int index, Object element) {
      this.checkForComod();
      super.add(index, element);
   }

   public ListIterator listIterator(int index) {
      this.checkForComod();
      return super.listIterator(index);
   }

   public Object remove(int index) {
      this.checkForComod();
      return super.remove(index);
   }

   public int indexOf(Object o) {
      this.checkForComod();
      return super.indexOf(o);
   }

   public int lastIndexOf(Object o) {
      this.checkForComod();
      return super.lastIndexOf(o);
   }

   public ListIterator listIterator() {
      this.checkForComod();
      return super.listIterator();
   }

   public List subList(int fromIndex, int toIndex) {
      this.checkForComod();
      return super.subList(fromIndex, toIndex);
   }

   protected CursorableLinkedList.Listable insertListable(CursorableLinkedList.Listable before, CursorableLinkedList.Listable after, Object value) {
      ++super._modCount;
      ++super._size;
      CursorableLinkedList.Listable elt = this._list.insertListable(null == before ? this._pre : before, null == after ? this._post : after, value);
      if (null == super._head.next()) {
         super._head.setNext(elt);
         super._head.setPrev(elt);
      }

      if (before == super._head.prev()) {
         super._head.setPrev(elt);
      }

      if (after == super._head.next()) {
         super._head.setNext(elt);
      }

      this.broadcastListableInserted(elt);
      return elt;
   }

   protected void removeListable(CursorableLinkedList.Listable elt) {
      ++super._modCount;
      --super._size;
      if (super._head.next() == elt && super._head.prev() == elt) {
         super._head.setNext((CursorableLinkedList.Listable)null);
         super._head.setPrev((CursorableLinkedList.Listable)null);
      }

      if (super._head.next() == elt) {
         super._head.setNext(elt.next());
      }

      if (super._head.prev() == elt) {
         super._head.setPrev(elt.prev());
      }

      this._list.removeListable(elt);
      this.broadcastListableRemoved(elt);
   }

   protected void checkForComod() throws ConcurrentModificationException {
      if (super._modCount != this._list._modCount) {
         throw new ConcurrentModificationException();
      }
   }
}
