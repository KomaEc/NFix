package edu.emory.mathcs.backport.java.util.concurrent.helpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FIFOWaitQueue extends WaitQueue implements Serializable {
   private static final long serialVersionUID = 2416444691925378811L;
   protected transient WaitQueue.WaitNode head_ = null;
   protected transient WaitQueue.WaitNode tail_ = null;

   public void insert(WaitQueue.WaitNode w) {
      if (this.tail_ == null) {
         this.head_ = this.tail_ = w;
      } else {
         this.tail_.next = w;
         this.tail_ = w;
      }

   }

   public WaitQueue.WaitNode extract() {
      if (this.head_ == null) {
         return null;
      } else {
         WaitQueue.WaitNode w = this.head_;
         this.head_ = w.next;
         if (this.head_ == null) {
            this.tail_ = null;
         }

         w.next = null;
         return w;
      }
   }

   public void putBack(WaitQueue.WaitNode w) {
      w.next = this.head_;
      this.head_ = w;
      if (this.tail_ == null) {
         this.tail_ = w;
      }

   }

   public boolean hasNodes() {
      return this.head_ != null;
   }

   public int getLength() {
      int count = 0;

      for(WaitQueue.WaitNode node = this.head_; node != null; node = node.next) {
         if (node.waiting) {
            ++count;
         }
      }

      return count;
   }

   public Collection getWaitingThreads() {
      List list = new ArrayList();
      int count = false;

      for(WaitQueue.WaitNode node = this.head_; node != null; node = node.next) {
         if (node.waiting) {
            list.add(node.owner);
         }
      }

      return list;
   }

   public boolean isWaiting(Thread thread) {
      if (thread == null) {
         throw new NullPointerException();
      } else {
         for(WaitQueue.WaitNode node = this.head_; node != null; node = node.next) {
            if (node.waiting && node.owner == thread) {
               return true;
            }
         }

         return false;
      }
   }
}
