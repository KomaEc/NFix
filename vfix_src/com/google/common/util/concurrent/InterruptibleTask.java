package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.j2objc.annotations.ReflectionSupport;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
@ReflectionSupport(ReflectionSupport.Level.FULL)
abstract class InterruptibleTask<T> extends AtomicReference<Runnable> implements Runnable {
   private static final Runnable DONE = new InterruptibleTask.DoNothingRunnable();
   private static final Runnable INTERRUPTING = new InterruptibleTask.DoNothingRunnable();

   public final void run() {
      Thread currentThread = Thread.currentThread();
      if (this.compareAndSet((Object)null, currentThread)) {
         boolean run = !this.isDone();
         T result = null;
         Throwable error = null;

         try {
            if (run) {
               result = this.runInterruptibly();
            }
         } catch (Throwable var9) {
            error = var9;
         } finally {
            if (!this.compareAndSet(currentThread, DONE)) {
               while(this.get() == INTERRUPTING) {
                  Thread.yield();
               }
            }

            if (run) {
               this.afterRanInterruptibly(result, error);
            }

         }

      }
   }

   abstract boolean isDone();

   abstract T runInterruptibly() throws Exception;

   abstract void afterRanInterruptibly(@Nullable T var1, @Nullable Throwable var2);

   final void interruptTask() {
      Runnable currentRunner = (Runnable)this.get();
      if (currentRunner instanceof Thread && this.compareAndSet(currentRunner, INTERRUPTING)) {
         ((Thread)currentRunner).interrupt();
         this.set(DONE);
      }

   }

   public abstract String toString();

   private static final class DoNothingRunnable implements Runnable {
      private DoNothingRunnable() {
      }

      public void run() {
      }

      // $FF: synthetic method
      DoNothingRunnable(Object x0) {
         this();
      }
   }
}
