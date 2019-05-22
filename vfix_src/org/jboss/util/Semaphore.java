package org.jboss.util;

import java.util.HashMap;
import java.util.Map;

public class Semaphore implements Sync {
   private static final long DEADLOCK_TIMEOUT = 300000L;
   private static final boolean m_debug = false;
   private int m_users;
   private int m_allowed;
   private Map m_logMap;

   public Semaphore(int allowed) {
      if (allowed < 1) {
         throw new IllegalArgumentException();
      } else {
         this.m_users = 0;
         this.m_allowed = allowed;
         this.m_logMap = new HashMap();
      }
   }

   public int getUsers() {
      synchronized(this) {
         return this.m_users;
      }
   }

   public void acquire() throws InterruptedException {
      synchronized(this) {
         this.logAcquire();
         ++this.m_users;
         boolean waitSuccessful = false;

         while(this.m_allowed <= 0) {
            waitSuccessful = this.waitImpl(this);
            if (!waitSuccessful) {
               --this.m_users;
               ++this.m_allowed;
            }
         }

         --this.m_allowed;
      }
   }

   public void release() {
      synchronized(this) {
         this.logRelease();
         --this.m_users;
         ++this.m_allowed;
         this.notify();
      }
   }

   public String toString() {
      return super.toString() + " - " + this.m_users;
   }

   protected boolean waitImpl(Object lock) throws InterruptedException {
      long start = System.currentTimeMillis();
      lock.wait(300000L);
      long end = System.currentTimeMillis();
      if (end - start > 299000L) {
         this.logDeadlock();
         return false;
      } else {
         return true;
      }
   }

   protected void logAcquire() {
   }

   protected void logDeadlock() {
      System.err.println();
      System.err.println("DEADLOCK ON SEMAPHORE " + this);
      System.err.println();
   }

   protected void logRelease() {
   }

   private class Info {
      private Thread m_thread;
      private int m_counter;
      private String m_trace;

      private Info(Thread t, int i, String s) {
         this.m_thread = t;
         this.m_counter = i;
         this.m_trace = s;
      }

      public boolean equals(Object o) {
         Semaphore.Info other = (Semaphore.Info)o;
         return this.m_thread == other.m_thread;
      }

      public String toString() {
         return this.m_thread + " - " + this.m_counter + "\n" + this.m_trace;
      }
   }
}
