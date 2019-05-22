package heros.solver;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class CountLatch {
   private final CountLatch.Sync sync;

   public CountLatch(int count) {
      this.sync = new CountLatch.Sync(count);
   }

   public void awaitZero() throws InterruptedException {
      this.sync.acquireShared(1);
   }

   public boolean awaitZero(long timeout, TimeUnit unit) throws InterruptedException {
      return this.sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
   }

   public void increment() {
      this.sync.acquireNonBlocking(1);
   }

   public void decrement() {
      this.sync.releaseShared(1);
   }

   public void resetAndInterrupt() {
      this.sync.reset();

      for(int i = 0; i < 3; ++i) {
         Iterator var2 = this.sync.getQueuedThreads().iterator();

         while(var2.hasNext()) {
            Thread t = (Thread)var2.next();
            t.interrupt();
         }
      }

      this.sync.reset();
   }

   public String toString() {
      return super.toString() + "[Count = " + this.sync.getCount() + "]";
   }

   public boolean isAtZero() {
      return this.sync.getCount() == 0;
   }

   private static final class Sync extends AbstractQueuedSynchronizer {
      Sync(int count) {
         this.setState(count);
      }

      int getCount() {
         return this.getState();
      }

      void reset() {
         this.setState(0);
      }

      protected int tryAcquireShared(int acquires) {
         return this.getState() == 0 ? 1 : -1;
      }

      protected int acquireNonBlocking(int acquires) {
         int c;
         int nextc;
         do {
            c = this.getState();
            nextc = c + 1;
         } while(!this.compareAndSetState(c, nextc));

         return 1;
      }

      protected boolean tryReleaseShared(int releases) {
         int c;
         int nextc;
         do {
            c = this.getState();
            if (c == 0) {
               return false;
            }

            nextc = c - 1;
         } while(!this.compareAndSetState(c, nextc));

         return nextc == 0;
      }
   }
}
