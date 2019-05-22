package com.mks.api.util;

import java.util.NoSuchElementException;
import java.util.Vector;

public class Queue {
   private Queue.Node head = null;
   private Queue.Node tail = null;
   private int size = 0;

   public void enqueue(Object o) {
      if (this.tail == null) {
         this.head = new Queue.Node(o);
         this.tail = this.head;
      } else {
         this.tail.next = new Queue.Node(o);
         this.tail = this.tail.next;
      }

      ++this.size;
   }

   public Object dequeue() {
      if (this.head == null) {
         throw new NoSuchElementException();
      } else {
         Object o = this.head.data;
         if (this.head == this.tail) {
            this.head = null;
            this.tail = null;
         } else {
            this.head = this.head.next;
         }

         --this.size;
         return o;
      }
   }

   public boolean isEmpty() {
      return this.head == null;
   }

   public int size() {
      return this.size;
   }

   public static void main(String[] args) {
      Queue q = new Queue();
      Vector v = new Vector();
      int count = Integer.parseInt(args[0]);
      System.out.println("Start vector");
      long start = System.currentTimeMillis();

      int i;
      for(i = 0; i < count; ++i) {
         v.addElement(new Integer(i));
      }

      while(!v.isEmpty()) {
         v.removeElementAt(0);
      }

      System.out.println("Done vector; elapsed time: " + (System.currentTimeMillis() - start));
      System.out.println("Start queue");
      start = System.currentTimeMillis();

      for(i = 0; i < count; ++i) {
         q.enqueue(new Integer(i));
      }

      while(!q.isEmpty()) {
         q.dequeue();
      }

      System.out.println("Done queue; elapsed time: " + (System.currentTimeMillis() - start));
   }

   private class Node {
      public Object data;
      public Queue.Node next;

      public Node(Object data) {
         this.data = data;
         this.next = null;
      }
   }
}
