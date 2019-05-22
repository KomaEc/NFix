package edu.emory.mathcs.backport.java.util.concurrent;

import edu.emory.mathcs.backport.java.util.AbstractQueue;
import edu.emory.mathcs.backport.java.util.Queue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ConcurrentLinkedQueue extends AbstractQueue implements Queue, Serializable {
   private static final long serialVersionUID = 196745693267521676L;
   private final Object headLock = new ConcurrentLinkedQueue.SerializableLock();
   private final Object tailLock = new ConcurrentLinkedQueue.SerializableLock();
   private transient volatile ConcurrentLinkedQueue.Node head = new ConcurrentLinkedQueue.Node((Object)null, (ConcurrentLinkedQueue.Node)null);
   private transient volatile ConcurrentLinkedQueue.Node tail;

   private boolean casTail(ConcurrentLinkedQueue.Node cmp, ConcurrentLinkedQueue.Node val) {
      synchronized(this.tailLock) {
         if (this.tail == cmp) {
            this.tail = val;
            return true;
         } else {
            return false;
         }
      }
   }

   private boolean casHead(ConcurrentLinkedQueue.Node cmp, ConcurrentLinkedQueue.Node val) {
      synchronized(this.headLock) {
         if (this.head == cmp) {
            this.head = val;
            return true;
         } else {
            return false;
         }
      }
   }

   public ConcurrentLinkedQueue() {
      this.tail = this.head;
   }

   public ConcurrentLinkedQueue(Collection c) {
      this.tail = this.head;
      Iterator it = c.iterator();

      while(it.hasNext()) {
         this.add(it.next());
      }

   }

   public boolean add(Object e) {
      return this.offer(e);
   }

   public boolean offer(Object e) {
      if (e == null) {
         throw new NullPointerException();
      } else {
         ConcurrentLinkedQueue.Node n = new ConcurrentLinkedQueue.Node(e, (ConcurrentLinkedQueue.Node)null);

         while(true) {
            ConcurrentLinkedQueue.Node t;
            ConcurrentLinkedQueue.Node s;
            do {
               t = this.tail;
               s = t.getNext();
            } while(t != this.tail);

            if (s == null) {
               if (t.casNext(s, n)) {
                  this.casTail(t, n);
                  return true;
               }
            } else {
               this.casTail(t, s);
            }
         }
      }
   }

   public Object poll() {
      while(true) {
         ConcurrentLinkedQueue.Node h = this.head;
         ConcurrentLinkedQueue.Node t = this.tail;
         ConcurrentLinkedQueue.Node first = h.getNext();
         if (h == this.head) {
            if (h == t) {
               if (first == null) {
                  return null;
               }

               this.casTail(t, first);
            } else if (this.casHead(h, first)) {
               Object item = first.getItem();
               if (item != null) {
                  first.setItem((Object)null);
                  return item;
               }
            }
         }
      }
   }

   public Object peek() {
      while(true) {
         ConcurrentLinkedQueue.Node h = this.head;
         ConcurrentLinkedQueue.Node t = this.tail;
         ConcurrentLinkedQueue.Node first = h.getNext();
         if (h == this.head) {
            if (h == t) {
               if (first == null) {
                  return null;
               }

               this.casTail(t, first);
            } else {
               Object item = first.getItem();
               if (item != null) {
                  return item;
               }

               this.casHead(h, first);
            }
         }
      }
   }

   ConcurrentLinkedQueue.Node first() {
      while(true) {
         ConcurrentLinkedQueue.Node h = this.head;
         ConcurrentLinkedQueue.Node t = this.tail;
         ConcurrentLinkedQueue.Node first = h.getNext();
         if (h == this.head) {
            if (h == t) {
               if (first == null) {
                  return null;
               }

               this.casTail(t, first);
            } else {
               if (first.getItem() != null) {
                  return first;
               }

               this.casHead(h, first);
            }
         }
      }
   }

   public boolean isEmpty() {
      return this.first() == null;
   }

   public int size() {
      int count = 0;

      for(ConcurrentLinkedQueue.Node p = this.first(); p != null; p = p.getNext()) {
         if (p.getItem() != null) {
            ++count;
            if (count == Integer.MAX_VALUE) {
               break;
            }
         }
      }

      return count;
   }

   public boolean contains(Object o) {
      if (o == null) {
         return false;
      } else {
         for(ConcurrentLinkedQueue.Node p = this.first(); p != null; p = p.getNext()) {
            Object item = p.getItem();
            if (item != null && o.equals(item)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean remove(Object o) {
      if (o == null) {
         return false;
      } else {
         for(ConcurrentLinkedQueue.Node p = this.first(); p != null; p = p.getNext()) {
            Object item = p.getItem();
            if (item != null && o.equals(item) && p.casItem(item, (Object)null)) {
               return true;
            }
         }

         return false;
      }
   }

   public Iterator iterator() {
      return new ConcurrentLinkedQueue.Itr();
   }

   private void writeObject(ObjectOutputStream s) throws IOException {
      s.defaultWriteObject();

      for(ConcurrentLinkedQueue.Node p = this.first(); p != null; p = p.getNext()) {
         Object item = p.getItem();
         if (item != null) {
            s.writeObject(item);
         }
      }

      s.writeObject((Object)null);
   }

   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
      s.defaultReadObject();
      this.head = new ConcurrentLinkedQueue.Node((Object)null, (ConcurrentLinkedQueue.Node)null);
      this.tail = this.head;

      while(true) {
         Object item = s.readObject();
         if (item == null) {
            return;
         }

         this.offer(item);
      }
   }

   private static class SerializableLock implements Serializable {
      private SerializableLock() {
      }

      // $FF: synthetic method
      SerializableLock(Object x0) {
         this();
      }
   }

   private class Itr implements Iterator {
      private ConcurrentLinkedQueue.Node nextNode;
      private Object nextItem;
      private ConcurrentLinkedQueue.Node lastRet;

      Itr() {
         this.advance();
      }

      private Object advance() {
         this.lastRet = this.nextNode;
         Object x = this.nextItem;

         for(ConcurrentLinkedQueue.Node p = this.nextNode == null ? ConcurrentLinkedQueue.this.first() : this.nextNode.getNext(); p != null; p = p.getNext()) {
            Object item = p.getItem();
            if (item != null) {
               this.nextNode = p;
               this.nextItem = item;
               return x;
            }
         }

         this.nextNode = null;
         this.nextItem = null;
         return x;
      }

      public boolean hasNext() {
         return this.nextNode != null;
      }

      public Object next() {
         if (this.nextNode == null) {
            throw new NoSuchElementException();
         } else {
            return this.advance();
         }
      }

      public void remove() {
         ConcurrentLinkedQueue.Node l = this.lastRet;
         if (l == null) {
            throw new IllegalStateException();
         } else {
            l.setItem((Object)null);
            this.lastRet = null;
         }
      }
   }

   private static class Node {
      private volatile Object item;
      private volatile ConcurrentLinkedQueue.Node next;

      Node(Object x) {
         this.item = x;
      }

      Node(Object x, ConcurrentLinkedQueue.Node n) {
         this.item = x;
         this.next = n;
      }

      Object getItem() {
         return this.item;
      }

      synchronized boolean casItem(Object cmp, Object val) {
         if (this.item == cmp) {
            this.item = val;
            return true;
         } else {
            return false;
         }
      }

      synchronized void setItem(Object val) {
         this.item = val;
      }

      ConcurrentLinkedQueue.Node getNext() {
         return this.next;
      }

      synchronized boolean casNext(ConcurrentLinkedQueue.Node cmp, ConcurrentLinkedQueue.Node val) {
         if (this.next == cmp) {
            this.next = val;
            return true;
         } else {
            return false;
         }
      }

      synchronized void setNext(ConcurrentLinkedQueue.Node val) {
         this.next = val;
      }
   }
}
