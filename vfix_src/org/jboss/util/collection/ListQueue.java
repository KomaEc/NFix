package org.jboss.util.collection;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jboss.util.NullArgumentException;

public class ListQueue<E> extends AbstractQueue<E> {
   protected final List<E> list;

   public ListQueue(List<E> list, int maxSize) {
      super(maxSize);
      if (list == null) {
         throw new NullArgumentException("list");
      } else {
         this.list = list;
      }
   }

   public ListQueue(int maxSize) {
      super(maxSize);
      this.list = new LinkedList();
   }

   public ListQueue(List<E> list) {
      this(list, -1);
   }

   public ListQueue() {
      this(new LinkedList(), -1);
   }

   protected boolean addLast(E obj) {
      return this.list.add(obj);
   }

   protected E removeFirst() {
      return this.list.remove(0);
   }

   public int size() {
      return this.list.size();
   }

   public Iterator<E> iterator() {
      return this.list.iterator();
   }

   public E getFront() throws EmptyCollectionException {
      if (this.isEmpty()) {
         throw new EmptyCollectionException();
      } else {
         return this.list.get(0);
      }
   }

   public E getBack() throws EmptyCollectionException {
      if (this.isEmpty()) {
         throw new EmptyCollectionException();
      } else {
         return this.list.get(this.list.size() - 1);
      }
   }

   public Iterator<E> reverseIterator() {
      return new ReverseListIterator(this.list);
   }

   public String toString() {
      return this.list.toString();
   }
}
