package org.jboss.util.timeout;

import java.util.concurrent.atomic.AtomicBoolean;
import org.jboss.util.JBossStringBuilder;

public class HashedTimeoutPriorityQueueImpl implements TimeoutPriorityQueue {
   private Object topLock = new Object();
   private HashedTimeoutPriorityQueueImpl.TimeoutExtImpl top;
   private HashedTimeoutPriorityQueueImpl.InternalPriorityQueue[] queues = new HashedTimeoutPriorityQueueImpl.InternalPriorityQueue[40];
   private AtomicBoolean cancelled = new AtomicBoolean(false);

   public HashedTimeoutPriorityQueueImpl() {
      for(int i = 0; i < this.queues.length; ++i) {
         this.queues[i] = new HashedTimeoutPriorityQueueImpl.InternalPriorityQueue();
      }

   }

   public TimeoutExt offer(long time, TimeoutTarget target) {
      if (this.cancelled.get()) {
         throw new IllegalStateException("TimeoutPriorityQueue has been cancelled");
      } else if (time < 0L) {
         throw new IllegalArgumentException("Negative time");
      } else if (target == null) {
         throw new IllegalArgumentException("Null timeout target");
      } else {
         HashedTimeoutPriorityQueueImpl.TimeoutExtImpl timeout = new HashedTimeoutPriorityQueueImpl.TimeoutExtImpl();
         timeout.time = time;
         timeout.target = target;
         int index = timeout.hashCode() % this.queues.length;
         return this.queues[index].offer(timeout);
      }
   }

   public TimeoutExt take() {
      return this.poll(-1L);
   }

   public TimeoutExt poll() {
      return this.poll(1L);
   }

   public TimeoutExt poll(long wait) {
      long endWait = -1L;
      if (wait > 0L) {
         endWait = System.currentTimeMillis() + wait;
      }

      synchronized(this.topLock) {
         while(!this.cancelled.get() && (wait >= 0L || endWait == -1L)) {
            if (this.top == null) {
               try {
                  if (endWait == -1L) {
                     this.topLock.wait();
                  } else {
                     this.topLock.wait(wait);
                  }
               } catch (InterruptedException var13) {
               }
            } else {
               long now = System.currentTimeMillis();
               if (this.top.time > now) {
                  long waitForFirst = this.top.time - now;
                  if (endWait != -1L && waitForFirst > wait) {
                     waitForFirst = wait;
                  }

                  try {
                     this.topLock.wait(waitForFirst);
                  } catch (InterruptedException var12) {
                  }
               }

               if (!this.cancelled.get() && this.top != null && this.top.time <= System.currentTimeMillis()) {
                  HashedTimeoutPriorityQueueImpl.TimeoutExtImpl result = this.top;
                  result.queue = null;
                  result.index = -2;
                  this.top = null;
                  this.recalculateTop(false);
                  return result;
               }
            }

            if (endWait != -1L) {
               wait = endWait - System.currentTimeMillis();
            }
         }

         return null;
      }
   }

   public TimeoutExt peek() {
      synchronized(this.topLock) {
         return this.top;
      }
   }

   public boolean remove(TimeoutExt timeout) {
      HashedTimeoutPriorityQueueImpl.TimeoutExtImpl timeoutImpl = (HashedTimeoutPriorityQueueImpl.TimeoutExtImpl)timeout;
      HashedTimeoutPriorityQueueImpl.InternalPriorityQueue queue = timeoutImpl.queue;
      if (queue != null && queue.remove(timeoutImpl)) {
         return true;
      } else {
         synchronized(this.topLock) {
            if (this.top == timeout) {
               this.top.done();
               this.top = null;
               this.recalculateTop(true);
               return true;
            } else {
               queue = timeoutImpl.queue;
               return queue != null ? queue.remove(timeoutImpl) : false;
            }
         }
      }
   }

   public void clear() {
      synchronized(this.topLock) {
         if (!this.cancelled.get()) {
            for(int i = 1; i < this.queues.length; ++i) {
               this.queues[i].clear();
            }

            this.top = this.cleanupTimeoutExtImpl(this.top);
         }
      }
   }

   public void cancel() {
      synchronized(this.topLock) {
         if (!this.cancelled.get()) {
            this.clear();
            this.topLock.notifyAll();
         }
      }
   }

   public int size() {
      int size = 0;
      if (this.top != null) {
         size = 1;
      }

      for(int i = 0; i < this.queues.length; ++i) {
         size += this.queues[i].size();
      }

      return size;
   }

   public boolean isCancelled() {
      return this.cancelled.get();
   }

   private void recalculateTop(boolean notify) {
      for(int i = 0; i < this.queues.length; ++i) {
         this.queues[i].compareAndSwapWithTop(notify);
      }

   }

   private HashedTimeoutPriorityQueueImpl.TimeoutExtImpl cleanupTimeoutExtImpl(HashedTimeoutPriorityQueueImpl.TimeoutExtImpl timeout) {
      if (timeout != null) {
         timeout.target = null;
      }

      return null;
   }

   private void assertExpr(boolean expr) {
      if (!expr) {
         throw new IllegalStateException("***** assert failed *****");
      }
   }

   public String dump() {
      JBossStringBuilder buffer = new JBossStringBuilder();
      buffer.append("TOP=");
      if (this.top == null) {
         buffer.append("null");
      } else {
         buffer.append(this.top.time);
      }

      buffer.append(" size=").append(this.size()).append('\n');

      for(int i = 0; i < this.queues.length; ++i) {
         buffer.append(i).append("=");

         for(int j = 1; j <= this.queues[i].size; ++j) {
            buffer.append(this.queues[i].queue[j].time).append(',');
         }

         buffer.append('\n');
      }

      return buffer.toString();
   }

   private class TimeoutExtImpl implements TimeoutExt {
      static final int TOP = 0;
      static final int DONE = -1;
      static final int TIMEOUT = -2;
      HashedTimeoutPriorityQueueImpl.InternalPriorityQueue queue;
      int index;
      long time;
      TimeoutTarget target;

      private TimeoutExtImpl() {
      }

      public long getTime() {
         return this.time;
      }

      public TimeoutTarget getTimeoutTarget() {
         return this.target;
      }

      public void done() {
         this.queue = null;
         this.index = -1;
      }

      public boolean cancel() {
         return HashedTimeoutPriorityQueueImpl.this.remove(this);
      }

      // $FF: synthetic method
      TimeoutExtImpl(Object x1) {
         this();
      }
   }

   private class InternalPriorityQueue {
      private Object lock = new Object();
      private int size = 0;
      private HashedTimeoutPriorityQueueImpl.TimeoutExtImpl[] queue = new HashedTimeoutPriorityQueueImpl.TimeoutExtImpl[16];

      InternalPriorityQueue() {
      }

      TimeoutExt offer(HashedTimeoutPriorityQueueImpl.TimeoutExtImpl timeout) {
         boolean checkTop = false;
         synchronized(this.lock) {
            if (++this.size == this.queue.length) {
               HashedTimeoutPriorityQueueImpl.TimeoutExtImpl[] newQ = new HashedTimeoutPriorityQueueImpl.TimeoutExtImpl[2 * this.queue.length];
               System.arraycopy(this.queue, 0, newQ, 0, this.queue.length);
               this.queue = newQ;
            }

            this.queue[this.size] = timeout;
            timeout.queue = this;
            timeout.index = this.size;
            this.normalizeUp(this.size);
            if (timeout.index == 1) {
               checkTop = true;
            }
         }

         if (checkTop) {
            synchronized(HashedTimeoutPriorityQueueImpl.this.topLock) {
               this.compareAndSwapWithTop(true);
            }
         }

         return timeout;
      }

      boolean compareAndSwapWithTop(boolean notify) {
         synchronized(this.lock) {
            if (this.size == 0) {
               return false;
            } else if (HashedTimeoutPriorityQueueImpl.this.top == null) {
               HashedTimeoutPriorityQueueImpl.this.top = this.removeNode(1);
               HashedTimeoutPriorityQueueImpl.this.top.queue = null;
               HashedTimeoutPriorityQueueImpl.this.top.index = 0;
               if (notify) {
                  HashedTimeoutPriorityQueueImpl.this.topLock.notify();
               }

               return HashedTimeoutPriorityQueueImpl.this.top != null;
            } else {
               if (HashedTimeoutPriorityQueueImpl.this.top.time > this.queue[1].time) {
                  HashedTimeoutPriorityQueueImpl.TimeoutExtImpl temp = HashedTimeoutPriorityQueueImpl.this.top;
                  HashedTimeoutPriorityQueueImpl.this.top = this.queue[1];
                  HashedTimeoutPriorityQueueImpl.this.top.queue = null;
                  HashedTimeoutPriorityQueueImpl.this.top.index = 0;
                  this.queue[1] = temp;
                  temp.queue = this;
                  temp.index = 1;
                  if (this.size > 1) {
                     this.normalizeDown(1);
                  }

                  if (notify) {
                     HashedTimeoutPriorityQueueImpl.this.topLock.notify();
                  }
               }

               return false;
            }
         }
      }

      boolean remove(TimeoutExt timeout) {
         synchronized(this.lock) {
            HashedTimeoutPriorityQueueImpl.TimeoutExtImpl timeoutImpl = (HashedTimeoutPriorityQueueImpl.TimeoutExtImpl)timeout;
            if (timeoutImpl.queue == this && timeoutImpl.index > 0) {
               this.removeNode(timeoutImpl.index);
               timeoutImpl.queue = null;
               timeoutImpl.index = -1;
               return true;
            } else {
               return false;
            }
         }
      }

      public void clear() {
         synchronized(this.lock) {
            if (!HashedTimeoutPriorityQueueImpl.this.cancelled.get()) {
               for(int i = 1; i <= this.size; ++i) {
                  this.queue[i] = HashedTimeoutPriorityQueueImpl.this.cleanupTimeoutExtImpl(this.queue[i]);
               }

            }
         }
      }

      public void cancel() {
         synchronized(this.lock) {
            if (!HashedTimeoutPriorityQueueImpl.this.cancelled.get()) {
               this.clear();
            }
         }
      }

      public int size() {
         return this.size;
      }

      private boolean normalizeUp(int index) {
         if (index == 1) {
            return false;
         } else {
            boolean ret = false;
            long t = this.queue[index].time;

            for(int p = index >> 1; this.queue[p].time > t; p >>= 1) {
               this.swap(p, index);
               ret = true;
               if (p == 1) {
                  break;
               }

               index = p;
            }

            return ret;
         }
      }

      void normalizeDown(int index) {
         long t = this.queue[index].time;

         for(int c = index << 1; c <= this.size; c = index << 1) {
            HashedTimeoutPriorityQueueImpl.TimeoutExtImpl l = this.queue[c];
            if (c + 1 <= this.size) {
               HashedTimeoutPriorityQueueImpl.TimeoutExtImpl r = this.queue[c + 1];
               if (l.time <= r.time) {
                  if (t <= l.time) {
                     break;
                  }

                  this.swap(index, c);
                  index = c;
               } else {
                  if (t <= r.time) {
                     break;
                  }

                  this.swap(index, c + 1);
                  index = c + 1;
               }
            } else {
               if (t <= l.time) {
                  break;
               }

               this.swap(index, c);
               index = c;
            }
         }

      }

      private void swap(int a, int b) {
         HashedTimeoutPriorityQueueImpl.TimeoutExtImpl temp = this.queue[a];
         this.queue[a] = this.queue[b];
         this.queue[a].index = a;
         this.queue[b] = temp;
         this.queue[b].index = b;
      }

      private HashedTimeoutPriorityQueueImpl.TimeoutExtImpl removeNode(int index) {
         HashedTimeoutPriorityQueueImpl.TimeoutExtImpl res = this.queue[index];
         if (index == this.size) {
            --this.size;
            this.queue[index] = null;
            return res;
         } else {
            this.swap(index, this.size);
            --this.size;
            this.queue[res.index] = null;
            if (this.normalizeUp(index)) {
               return res;
            } else {
               this.normalizeDown(index);
               return res;
            }
         }
      }

      void checkTree() {
         HashedTimeoutPriorityQueueImpl.this.assertExpr(this.size >= 0);
         HashedTimeoutPriorityQueueImpl.this.assertExpr(this.size < this.queue.length);
         HashedTimeoutPriorityQueueImpl.this.assertExpr(this.queue[0] == null);
         if (this.size > 0) {
            HashedTimeoutPriorityQueueImpl.this.assertExpr(this.queue[1] != null);
            HashedTimeoutPriorityQueueImpl.this.assertExpr(this.queue[1].index == 1);
            HashedTimeoutPriorityQueueImpl.this.assertExpr(this.queue[1].queue == this);

            int i;
            for(i = 2; i <= this.size; ++i) {
               HashedTimeoutPriorityQueueImpl.this.assertExpr(this.queue[i] != null);
               HashedTimeoutPriorityQueueImpl.this.assertExpr(this.queue[i].index == i);
               HashedTimeoutPriorityQueueImpl.this.assertExpr(this.queue[i].queue == this);
               HashedTimeoutPriorityQueueImpl.this.assertExpr(this.queue[i >> 1].time <= this.queue[i].time);
            }

            for(i = this.size + 1; i < this.queue.length; ++i) {
               HashedTimeoutPriorityQueueImpl.this.assertExpr(this.queue[i] == null);
            }
         }

      }
   }
}
