package org.jboss.util;

public class WaitSemaphore extends Semaphore implements WaitSync {
   private static final int MAX_USERS_ALLOWED = 1;
   private int m_waiters;

   public WaitSemaphore() {
      super(1);
   }

   public void doWait() throws InterruptedException {
      synchronized(this) {
         this.release();
         ++this.m_waiters;
         this.waitImpl(this);
         --this.m_waiters;
         this.acquire();
      }
   }

   public void doNotify() throws InterruptedException {
      synchronized(this) {
         if (this.getWaiters() > 0) {
            this.acquire();
            this.notify();
            this.release();
         }

      }
   }

   public int getWaiters() {
      synchronized(this) {
         return this.m_waiters;
      }
   }

   public String toString() {
      return super.toString() + " - " + this.m_waiters;
   }
}
