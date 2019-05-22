package edu.emory.mathcs.backport.java.util.concurrent;

import edu.emory.mathcs.backport.java.util.AbstractQueue;
import edu.emory.mathcs.backport.java.util.Arrays;
import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicLong;
import edu.emory.mathcs.backport.java.util.concurrent.helpers.Utils;
import edu.emory.mathcs.backport.java.util.concurrent.locks.Condition;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ScheduledThreadPoolExecutor extends ThreadPoolExecutor implements ScheduledExecutorService {
   private volatile boolean continueExistingPeriodicTasksAfterShutdown;
   private volatile boolean executeExistingDelayedTasksAfterShutdown = true;
   private volatile boolean removeOnCancel = false;
   private static final AtomicLong sequencer = new AtomicLong(0L);

   final long now() {
      return Utils.nanoTime();
   }

   boolean canRunInCurrentRunState(boolean periodic) {
      return this.isRunningOrShutdown(periodic ? this.continueExistingPeriodicTasksAfterShutdown : this.executeExistingDelayedTasksAfterShutdown);
   }

   private void delayedExecute(RunnableScheduledFuture task) {
      if (this.isShutdown()) {
         this.reject(task);
      } else {
         super.getQueue().add(task);
         if (this.isShutdown() && !this.canRunInCurrentRunState(task.isPeriodic()) && this.remove(task)) {
            task.cancel(false);
         } else {
            this.prestartCoreThread();
         }
      }

   }

   void reExecutePeriodic(RunnableScheduledFuture task) {
      if (this.canRunInCurrentRunState(true)) {
         super.getQueue().add(task);
         if (!this.canRunInCurrentRunState(true) && this.remove(task)) {
            task.cancel(false);
         } else {
            this.prestartCoreThread();
         }
      }

   }

   void onShutdown() {
      BlockingQueue q = super.getQueue();
      boolean keepDelayed = this.getExecuteExistingDelayedTasksAfterShutdownPolicy();
      boolean keepPeriodic = this.getContinueExistingPeriodicTasksAfterShutdownPolicy();
      if (!keepDelayed && !keepPeriodic) {
         q.clear();
      } else {
         Object[] entries = q.toArray();

         for(int i = 0; i < entries.length; ++i) {
            Object e = entries[i];
            if (e instanceof RunnableScheduledFuture) {
               RunnableScheduledFuture t;
               label30: {
                  t = (RunnableScheduledFuture)e;
                  if (t.isPeriodic()) {
                     if (!keepPeriodic) {
                        break label30;
                     }
                  } else if (!keepDelayed) {
                     break label30;
                  }

                  if (!t.isCancelled()) {
                     continue;
                  }
               }

               if (q.remove(t)) {
                  t.cancel(false);
               }
            }
         }
      }

      this.tryTerminate();
   }

   protected RunnableScheduledFuture decorateTask(Runnable runnable, RunnableScheduledFuture task) {
      return task;
   }

   protected RunnableScheduledFuture decorateTask(Callable callable, RunnableScheduledFuture task) {
      return task;
   }

   public ScheduledThreadPoolExecutor(int corePoolSize) {
      super(corePoolSize, Integer.MAX_VALUE, 0L, TimeUnit.NANOSECONDS, new ScheduledThreadPoolExecutor.DelayedWorkQueue());
   }

   public ScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory) {
      super(corePoolSize, Integer.MAX_VALUE, 0L, TimeUnit.NANOSECONDS, new ScheduledThreadPoolExecutor.DelayedWorkQueue(), (ThreadFactory)threadFactory);
   }

   public ScheduledThreadPoolExecutor(int corePoolSize, RejectedExecutionHandler handler) {
      super(corePoolSize, Integer.MAX_VALUE, 0L, TimeUnit.NANOSECONDS, new ScheduledThreadPoolExecutor.DelayedWorkQueue(), (RejectedExecutionHandler)handler);
   }

   public ScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
      super(corePoolSize, Integer.MAX_VALUE, 0L, TimeUnit.NANOSECONDS, new ScheduledThreadPoolExecutor.DelayedWorkQueue(), threadFactory, handler);
   }

   public ScheduledFuture schedule(Runnable command, long delay, TimeUnit unit) {
      if (command != null && unit != null) {
         if (delay < 0L) {
            delay = 0L;
         }

         long triggerTime = this.now() + unit.toNanos(delay);
         RunnableScheduledFuture t = this.decorateTask((Runnable)command, new ScheduledThreadPoolExecutor.ScheduledFutureTask(command, (Object)null, triggerTime));
         this.delayedExecute(t);
         return t;
      } else {
         throw new NullPointerException();
      }
   }

   public ScheduledFuture schedule(Callable callable, long delay, TimeUnit unit) {
      if (callable != null && unit != null) {
         if (delay < 0L) {
            delay = 0L;
         }

         long triggerTime = this.now() + unit.toNanos(delay);
         RunnableScheduledFuture t = this.decorateTask((Callable)callable, new ScheduledThreadPoolExecutor.ScheduledFutureTask(callable, triggerTime));
         this.delayedExecute(t);
         return t;
      } else {
         throw new NullPointerException();
      }
   }

   public ScheduledFuture scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
      if (command != null && unit != null) {
         if (period <= 0L) {
            throw new IllegalArgumentException();
         } else {
            if (initialDelay < 0L) {
               initialDelay = 0L;
            }

            long triggerTime = this.now() + unit.toNanos(initialDelay);
            RunnableScheduledFuture t = this.decorateTask((Runnable)command, new ScheduledThreadPoolExecutor.ScheduledFutureTask(command, (Object)null, triggerTime, unit.toNanos(period)));
            this.delayedExecute(t);
            return t;
         }
      } else {
         throw new NullPointerException();
      }
   }

   public ScheduledFuture scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
      if (command != null && unit != null) {
         if (delay <= 0L) {
            throw new IllegalArgumentException();
         } else {
            if (initialDelay < 0L) {
               initialDelay = 0L;
            }

            long triggerTime = this.now() + unit.toNanos(initialDelay);
            RunnableScheduledFuture t = this.decorateTask((Runnable)command, new ScheduledThreadPoolExecutor.ScheduledFutureTask(command, (Object)null, triggerTime, unit.toNanos(-delay)));
            this.delayedExecute(t);
            return t;
         }
      } else {
         throw new NullPointerException();
      }
   }

   public void execute(Runnable command) {
      this.schedule(command, 0L, TimeUnit.NANOSECONDS);
   }

   public Future submit(Runnable task) {
      return this.schedule(task, 0L, TimeUnit.NANOSECONDS);
   }

   public Future submit(Runnable task, Object result) {
      return this.schedule(Executors.callable(task, result), 0L, TimeUnit.NANOSECONDS);
   }

   public Future submit(Callable task) {
      return this.schedule(task, 0L, TimeUnit.NANOSECONDS);
   }

   public void setContinueExistingPeriodicTasksAfterShutdownPolicy(boolean value) {
      this.continueExistingPeriodicTasksAfterShutdown = value;
      if (!value && this.isShutdown()) {
         this.onShutdown();
      }

   }

   public boolean getContinueExistingPeriodicTasksAfterShutdownPolicy() {
      return this.continueExistingPeriodicTasksAfterShutdown;
   }

   public void setExecuteExistingDelayedTasksAfterShutdownPolicy(boolean value) {
      this.executeExistingDelayedTasksAfterShutdown = value;
      if (!value && this.isShutdown()) {
         this.onShutdown();
      }

   }

   public boolean getExecuteExistingDelayedTasksAfterShutdownPolicy() {
      return this.executeExistingDelayedTasksAfterShutdown;
   }

   public void setRemoveOnCancelPolicy(boolean value) {
      this.removeOnCancel = value;
   }

   public boolean getRemoveOnCancelPolicy() {
      return this.removeOnCancel;
   }

   public void shutdown() {
      super.shutdown();
   }

   public List shutdownNow() {
      return super.shutdownNow();
   }

   public BlockingQueue getQueue() {
      return super.getQueue();
   }

   static class DelayedWorkQueue extends AbstractQueue implements BlockingQueue {
      private static final int INITIAL_CAPACITY = 64;
      private transient RunnableScheduledFuture[] queue = new RunnableScheduledFuture[64];
      private final transient ReentrantLock lock = new ReentrantLock();
      private final transient Condition available;
      private int size;

      DelayedWorkQueue() {
         this.available = this.lock.newCondition();
         this.size = 0;
      }

      private void setIndex(Object f, int idx) {
         if (f instanceof ScheduledThreadPoolExecutor.ScheduledFutureTask) {
            ((ScheduledThreadPoolExecutor.ScheduledFutureTask)f).heapIndex = idx;
         }

      }

      private void siftUp(int k, RunnableScheduledFuture key) {
         while(true) {
            if (k > 0) {
               int parent = k - 1 >>> 1;
               RunnableScheduledFuture e = this.queue[parent];
               if (key.compareTo(e) < 0) {
                  this.queue[k] = e;
                  this.setIndex(e, k);
                  k = parent;
                  continue;
               }
            }

            this.queue[k] = key;
            this.setIndex(key, k);
            return;
         }
      }

      private void siftDown(int k, RunnableScheduledFuture key) {
         int child;
         for(int half = this.size >>> 1; k < half; k = child) {
            child = (k << 1) + 1;
            RunnableScheduledFuture c = this.queue[child];
            int right = child + 1;
            if (right < this.size && c.compareTo(this.queue[right]) > 0) {
               child = right;
               c = this.queue[right];
            }

            if (key.compareTo(c) <= 0) {
               break;
            }

            this.queue[k] = c;
            this.setIndex(c, k);
         }

         this.queue[k] = key;
         this.setIndex(key, k);
      }

      private RunnableScheduledFuture finishPoll(RunnableScheduledFuture f) {
         int s = --this.size;
         RunnableScheduledFuture x = this.queue[s];
         this.queue[s] = null;
         if (s != 0) {
            this.siftDown(0, x);
            this.available.signalAll();
         }

         this.setIndex(f, -1);
         return f;
      }

      private void grow() {
         int oldCapacity = this.queue.length;
         int newCapacity = oldCapacity + (oldCapacity >> 1);
         if (newCapacity < 0) {
            newCapacity = Integer.MAX_VALUE;
         }

         RunnableScheduledFuture[] newqueue = new RunnableScheduledFuture[newCapacity];
         System.arraycopy(this.queue, 0, newqueue, 0, this.queue.length);
         this.queue = newqueue;
      }

      private int indexOf(Object x) {
         if (x != null) {
            for(int i = 0; i < this.size; ++i) {
               if (x.equals(this.queue[i])) {
                  return i;
               }
            }
         }

         return -1;
      }

      public boolean remove(Object x) {
         ReentrantLock lock = this.lock;
         lock.lock();

         boolean removed;
         try {
            int i;
            if (x instanceof ScheduledThreadPoolExecutor.ScheduledFutureTask) {
               i = ((ScheduledThreadPoolExecutor.ScheduledFutureTask)x).heapIndex;
            } else {
               i = this.indexOf(x);
            }

            if (removed = i >= 0 && i < this.size && this.queue[i] == x) {
               this.setIndex(x, -1);
               int s = --this.size;
               RunnableScheduledFuture replacement = this.queue[s];
               this.queue[s] = null;
               if (s != i) {
                  this.siftDown(i, replacement);
                  if (this.queue[i] == replacement) {
                     this.siftUp(i, replacement);
                  }
               }
            }
         } finally {
            lock.unlock();
         }

         return removed;
      }

      public int size() {
         ReentrantLock lock = this.lock;
         lock.lock();

         int s;
         try {
            s = this.size;
         } finally {
            lock.unlock();
         }

         return s;
      }

      public boolean isEmpty() {
         return this.size() == 0;
      }

      public int remainingCapacity() {
         return Integer.MAX_VALUE;
      }

      public Object peek() {
         ReentrantLock lock = this.lock;
         lock.lock();

         RunnableScheduledFuture var2;
         try {
            var2 = this.queue[0];
         } finally {
            lock.unlock();
         }

         return var2;
      }

      public boolean offer(Object x) {
         if (x == null) {
            throw new NullPointerException();
         } else {
            RunnableScheduledFuture e = (RunnableScheduledFuture)x;
            ReentrantLock lock = this.lock;
            lock.lock();

            try {
               int i = this.size;
               if (i >= this.queue.length) {
                  this.grow();
               }

               this.size = i + 1;
               boolean notify;
               if (i == 0) {
                  notify = true;
                  this.queue[0] = e;
                  this.setIndex(e, 0);
               } else {
                  notify = e.compareTo(this.queue[0]) < 0;
                  this.siftUp(i, e);
               }

               if (notify) {
                  this.available.signalAll();
               }
            } finally {
               lock.unlock();
            }

            return true;
         }
      }

      public void put(Object e) {
         this.offer(e);
      }

      public boolean add(Runnable e) {
         return this.offer(e);
      }

      public boolean offer(Object e, long timeout, TimeUnit unit) {
         return this.offer(e);
      }

      public Object poll() {
         ReentrantLock lock = this.lock;
         lock.lock();

         RunnableScheduledFuture var3;
         try {
            RunnableScheduledFuture first = this.queue[0];
            if (first != null && first.getDelay(TimeUnit.NANOSECONDS) <= 0L) {
               var3 = this.finishPoll(first);
               return var3;
            }

            var3 = null;
         } finally {
            lock.unlock();
         }

         return var3;
      }

      public Object take() throws InterruptedException {
         ReentrantLock lock = this.lock;
         lock.lockInterruptibly();

         try {
            while(true) {
               while(true) {
                  RunnableScheduledFuture first = this.queue[0];
                  if (first != null) {
                     long delay = first.getDelay(TimeUnit.NANOSECONDS);
                     if (delay <= 0L) {
                        RunnableScheduledFuture var5 = this.finishPoll(first);
                        return var5;
                     }

                     this.available.await(delay, TimeUnit.NANOSECONDS);
                  } else {
                     this.available.await();
                  }
               }
            }
         } finally {
            lock.unlock();
         }
      }

      public Object poll(long timeout, TimeUnit unit) throws InterruptedException {
         long nanos = unit.toNanos(timeout);
         long deadline = Utils.nanoTime() + nanos;
         ReentrantLock lock = this.lock;
         lock.lockInterruptibly();

         try {
            while(true) {
               RunnableScheduledFuture first = this.queue[0];
               if (first == null) {
                  if (nanos <= 0L) {
                     Object var10 = null;
                     return var10;
                  }

                  this.available.await(nanos, TimeUnit.NANOSECONDS);
                  nanos = deadline - Utils.nanoTime();
               } else {
                  long delay = first.getDelay(TimeUnit.NANOSECONDS);
                  RunnableScheduledFuture var13;
                  if (delay > 0L) {
                     if (nanos > 0L) {
                        if (delay > nanos) {
                           delay = nanos;
                        }

                        this.available.await(delay, TimeUnit.NANOSECONDS);
                        nanos = deadline - Utils.nanoTime();
                        continue;
                     }

                     var13 = null;
                     return var13;
                  }

                  var13 = this.finishPoll(first);
                  return var13;
               }
            }
         } finally {
            lock.unlock();
         }
      }

      public void clear() {
         ReentrantLock lock = this.lock;
         lock.lock();

         try {
            for(int i = 0; i < this.size; ++i) {
               RunnableScheduledFuture t = this.queue[i];
               if (t != null) {
                  this.queue[i] = null;
                  this.setIndex(t, -1);
               }
            }

            this.size = 0;
         } finally {
            lock.unlock();
         }

      }

      private RunnableScheduledFuture pollExpired() {
         RunnableScheduledFuture first = this.queue[0];
         if (first != null && first.getDelay(TimeUnit.NANOSECONDS) <= 0L) {
            this.setIndex(first, -1);
            int s = --this.size;
            RunnableScheduledFuture x = this.queue[s];
            this.queue[s] = null;
            if (s != 0) {
               this.siftDown(0, x);
            }

            return first;
         } else {
            return null;
         }
      }

      public int drainTo(Collection c) {
         if (c == null) {
            throw new NullPointerException();
         } else if (c == this) {
            throw new IllegalArgumentException();
         } else {
            ReentrantLock lock = this.lock;
            lock.lock();

            try {
               int n = 0;

               while(true) {
                  RunnableScheduledFuture first = this.pollExpired();
                  if (first == null) {
                     if (n > 0) {
                        this.available.signalAll();
                     }

                     int var8 = n;
                     return var8;
                  }

                  c.add(first);
                  ++n;
               }
            } finally {
               lock.unlock();
            }
         }
      }

      public int drainTo(Collection c, int maxElements) {
         if (c == null) {
            throw new NullPointerException();
         } else if (c == this) {
            throw new IllegalArgumentException();
         } else if (maxElements <= 0) {
            return 0;
         } else {
            ReentrantLock lock = this.lock;
            lock.lock();

            int var9;
            try {
               int n;
               for(n = 0; n < maxElements; ++n) {
                  RunnableScheduledFuture first = this.pollExpired();
                  if (first == null) {
                     break;
                  }

                  c.add(first);
               }

               if (n > 0) {
                  this.available.signalAll();
               }

               var9 = n;
            } finally {
               lock.unlock();
            }

            return var9;
         }
      }

      public Object[] toArray() {
         ReentrantLock lock = this.lock;
         lock.lock();

         Object[] var2;
         try {
            var2 = Arrays.copyOf((Object[])this.queue, this.size);
         } finally {
            lock.unlock();
         }

         return var2;
      }

      public Object[] toArray(Object[] a) {
         ReentrantLock lock = this.lock;
         lock.lock();

         Object[] var3;
         try {
            if (a.length >= this.size) {
               System.arraycopy(this.queue, 0, a, 0, this.size);
               if (a.length > this.size) {
                  a[this.size] = null;
               }

               var3 = a;
               return var3;
            }

            var3 = (Object[])Arrays.copyOf(this.queue, this.size, a.getClass());
         } finally {
            lock.unlock();
         }

         return var3;
      }

      public Iterator iterator() {
         return new ScheduledThreadPoolExecutor.DelayedWorkQueue.Itr(this.toArray());
      }

      private class Itr implements Iterator {
         final Object[] array;
         int cursor;
         int lastRet = -1;

         Itr(Object[] array) {
            this.array = array;
         }

         public boolean hasNext() {
            return this.cursor < this.array.length;
         }

         public Object next() {
            if (this.cursor >= this.array.length) {
               throw new NoSuchElementException();
            } else {
               this.lastRet = this.cursor;
               return (Runnable)this.array[this.cursor++];
            }
         }

         public void remove() {
            if (this.lastRet < 0) {
               throw new IllegalStateException();
            } else {
               DelayedWorkQueue.this.remove(this.array[this.lastRet]);
               this.lastRet = -1;
            }
         }
      }
   }

   private class ScheduledFutureTask extends FutureTask implements RunnableScheduledFuture {
      private final long sequenceNumber;
      private long time;
      private final long period;
      int heapIndex;

      ScheduledFutureTask(Runnable r, Object result, long ns) {
         super(r, result);
         this.time = ns;
         this.period = 0L;
         this.sequenceNumber = ScheduledThreadPoolExecutor.sequencer.getAndIncrement();
      }

      ScheduledFutureTask(Runnable r, Object result, long ns, long period) {
         super(r, result);
         this.time = ns;
         this.period = period;
         this.sequenceNumber = ScheduledThreadPoolExecutor.sequencer.getAndIncrement();
      }

      ScheduledFutureTask(Callable callable, long ns) {
         super(callable);
         this.time = ns;
         this.period = 0L;
         this.sequenceNumber = ScheduledThreadPoolExecutor.sequencer.getAndIncrement();
      }

      public long getDelay(TimeUnit unit) {
         long d = unit.convert(this.time - ScheduledThreadPoolExecutor.this.now(), TimeUnit.NANOSECONDS);
         return d;
      }

      public int compareTo(Object other) {
         Delayed otherd = (Delayed)other;
         if (otherd == this) {
            return 0;
         } else if (otherd instanceof ScheduledThreadPoolExecutor.ScheduledFutureTask) {
            ScheduledThreadPoolExecutor.ScheduledFutureTask x = (ScheduledThreadPoolExecutor.ScheduledFutureTask)other;
            long diff = this.time - x.time;
            if (diff < 0L) {
               return -1;
            } else if (diff > 0L) {
               return 1;
            } else {
               return this.sequenceNumber < x.sequenceNumber ? -1 : 1;
            }
         } else {
            long d = this.getDelay(TimeUnit.NANOSECONDS) - otherd.getDelay(TimeUnit.NANOSECONDS);
            return d == 0L ? 0 : (d < 0L ? -1 : 1);
         }
      }

      public boolean isPeriodic() {
         return this.period != 0L;
      }

      private void setNextRunTime() {
         long p = this.period;
         if (p > 0L) {
            this.time += p;
         } else {
            this.time = ScheduledThreadPoolExecutor.this.now() - p;
         }

      }

      public boolean cancel(boolean mayInterruptIfRunning) {
         boolean cancelled = super.cancel(mayInterruptIfRunning);
         if (cancelled && ScheduledThreadPoolExecutor.this.removeOnCancel && this.heapIndex >= 0) {
            ScheduledThreadPoolExecutor.this.remove(this);
         }

         return cancelled;
      }

      public void run() {
         boolean periodic = this.isPeriodic();
         if (!ScheduledThreadPoolExecutor.this.canRunInCurrentRunState(periodic)) {
            this.cancel(false);
         } else if (!periodic) {
            access$201(this);
         } else if (access$301(this)) {
            this.setNextRunTime();
            ScheduledThreadPoolExecutor.this.reExecutePeriodic(this);
         }

      }

      // $FF: synthetic method
      static void access$201(ScheduledThreadPoolExecutor.ScheduledFutureTask x0) {
         x0.run();
      }

      // $FF: synthetic method
      static boolean access$301(ScheduledThreadPoolExecutor.ScheduledFutureTask x0) {
         return x0.runAndReset();
      }
   }
}
