package edu.emory.mathcs.backport.java.util.concurrent;

import edu.emory.mathcs.backport.java.util.concurrent.helpers.Utils;

public class CyclicBarrier {
   private final Object lock;
   private final int parties;
   private final Runnable barrierCommand;
   private CyclicBarrier.Generation generation;
   private int count;

   private void nextGeneration() {
      this.lock.notifyAll();
      this.count = this.parties;
      this.generation = new CyclicBarrier.Generation();
   }

   private void breakBarrier() {
      this.generation.broken = true;
      this.count = this.parties;
      this.lock.notifyAll();
   }

   private int dowait(boolean timed, long nanos) throws InterruptedException, BrokenBarrierException, TimeoutException {
      synchronized(this.lock) {
         CyclicBarrier.Generation g = this.generation;
         if (g.broken) {
            throw new BrokenBarrierException();
         } else if (Thread.interrupted()) {
            this.breakBarrier();
            throw new InterruptedException();
         } else {
            int index = --this.count;
            if (index == 0) {
               boolean ranAction = false;

               byte var9;
               try {
                  Runnable command = this.barrierCommand;
                  if (command != null) {
                     command.run();
                  }

                  ranAction = true;
                  this.nextGeneration();
                  var9 = 0;
               } finally {
                  if (!ranAction) {
                     this.breakBarrier();
                  }

               }

               return var9;
            } else {
               long deadline = timed ? Utils.nanoTime() + nanos : 0L;

               while(true) {
                  try {
                     if (!timed) {
                        this.lock.wait();
                     } else if (nanos > 0L) {
                        TimeUnit.NANOSECONDS.timedWait(this.lock, nanos);
                     }
                  } catch (InterruptedException var18) {
                     if (g == this.generation && !g.broken) {
                        this.breakBarrier();
                        throw var18;
                     }

                     Thread.currentThread().interrupt();
                  }

                  if (g.broken) {
                     throw new BrokenBarrierException();
                  }

                  if (g != this.generation) {
                     return index;
                  }

                  if (timed && nanos <= 0L) {
                     this.breakBarrier();
                     throw new TimeoutException();
                  }

                  nanos = deadline - Utils.nanoTime();
               }
            }
         }
      }
   }

   public CyclicBarrier(int parties, Runnable barrierAction) {
      this.lock = new Object();
      this.generation = new CyclicBarrier.Generation();
      if (parties <= 0) {
         throw new IllegalArgumentException();
      } else {
         this.parties = parties;
         this.count = parties;
         this.barrierCommand = barrierAction;
      }
   }

   public CyclicBarrier(int parties) {
      this(parties, (Runnable)null);
   }

   public int getParties() {
      return this.parties;
   }

   public int await() throws InterruptedException, BrokenBarrierException {
      try {
         return this.dowait(false, 0L);
      } catch (TimeoutException var2) {
         throw new Error(var2);
      }
   }

   public int await(long timeout, TimeUnit unit) throws InterruptedException, BrokenBarrierException, TimeoutException {
      return this.dowait(true, unit.toNanos(timeout));
   }

   public boolean isBroken() {
      synchronized(this.lock) {
         return this.generation.broken;
      }
   }

   public void reset() {
      synchronized(this.lock) {
         this.breakBarrier();
         this.nextGeneration();
      }
   }

   public int getNumberWaiting() {
      synchronized(this.lock) {
         return this.parties - this.count;
      }
   }

   private static class Generation {
      boolean broken;

      private Generation() {
         this.broken = false;
      }

      // $FF: synthetic method
      Generation(Object x0) {
         this();
      }
   }
}
