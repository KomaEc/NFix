package org.codehaus.groovy.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ManagedLinkedList<T> {
   private ManagedLinkedList<T>.Element<T> tail;
   private ManagedLinkedList<T>.Element<T> head;
   private ReferenceBundle bundle;

   public ManagedLinkedList(ReferenceBundle bundle) {
      this.bundle = bundle;
   }

   public void add(T value) {
      ManagedLinkedList<T>.Element<T> element = new ManagedLinkedList.Element(this.bundle, value);
      element.previous = this.tail;
      if (this.tail != null) {
         this.tail.next = element;
      }

      this.tail = element;
      if (this.head == null) {
         this.head = element;
      }

   }

   public Iterator<T> iterator() {
      return new ManagedLinkedList.Iter();
   }

   public T[] toArray(T[] tArray) {
      List<T> array = new ArrayList(100);
      Iterator it = this.iterator();

      while(it.hasNext()) {
         T val = it.next();
         if (val != null) {
            array.add(val);
         }
      }

      return array.toArray(tArray);
   }

   public boolean isEmpty() {
      return this.head == null;
   }

   private final class Iter implements Iterator<T> {
      private ManagedLinkedList<T>.Element<T> current;
      private boolean currentHandled = false;

      Iter() {
         this.current = ManagedLinkedList.this.head;
      }

      public boolean hasNext() {
         if (this.current == null) {
            return false;
         } else if (this.currentHandled) {
            return this.current.next != null;
         } else {
            return this.current != null;
         }
      }

      public T next() {
         if (this.currentHandled) {
            this.current = this.current.next;
         }

         this.currentHandled = true;
         return this.current == null ? null : this.current.get();
      }

      public void remove() {
         if (this.current != null) {
            this.current.finalizeReference();
         }

      }
   }

   private final class Element<V> extends ManagedReference<V> {
      ManagedLinkedList.Element next;
      ManagedLinkedList.Element previous;

      public Element(ReferenceBundle bundle, V value) {
         super(bundle, value);
      }

      public void finalizeReference() {
         if (this.previous != null && this.previous.next != null) {
            this.previous.next = this.next;
         }

         if (this.next != null && this.next.previous != null) {
            this.next.previous = this.previous;
         }

         if (this == ManagedLinkedList.this.head) {
            ManagedLinkedList.this.head = this.next;
         }

         this.next = null;
         if (this == ManagedLinkedList.this.tail) {
            ManagedLinkedList.this.tail = this.previous;
         }

         this.previous = null;
         super.finalizeReference();
      }
   }
}
