package org.jboss.util.timeout;

public class TimeoutPriorityQueueImpl implements TimeoutPriorityQueue {
   private Object lock = new Object();
   private int size = 0;
   private TimeoutPriorityQueueImpl.TimeoutExtImpl[] queue = new TimeoutPriorityQueueImpl.TimeoutExtImpl[16];

   public TimeoutExt offer(long time, TimeoutTarget target) {
      if (this.queue == null) {
         throw new IllegalStateException("TimeoutPriorityQueue has been cancelled");
      } else if (time < 0L) {
         throw new IllegalArgumentException("Negative time");
      } else if (target == null) {
         throw new IllegalArgumentException("Null timeout target");
      } else {
         synchronized(this.lock) {
            if (++this.size == this.queue.length) {
               TimeoutPriorityQueueImpl.TimeoutExtImpl[] newQ = new TimeoutPriorityQueueImpl.TimeoutExtImpl[2 * this.queue.length];
               System.arraycopy(this.queue, 0, newQ, 0, this.queue.length);
               this.queue = newQ;
            }

            TimeoutPriorityQueueImpl.TimeoutExtImpl timeout = this.queue[this.size] = new TimeoutPriorityQueueImpl.TimeoutExtImpl();
            timeout.index = this.size;
            timeout.time = time;
            timeout.target = target;
            this.normalizeUp(this.size);
            if (timeout.index == 1) {
               this.lock.notify();
            }

            return timeout;
         }
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

      synchronized(this.lock) {
         while(this.queue != null && (wait >= 0L || endWait == -1L)) {
            if (this.size == 0) {
               try {
                  if (endWait == -1L) {
                     this.lock.wait();
                  } else {
                     this.lock.wait(wait);
                  }
               } catch (InterruptedException var13) {
               }
            } else {
               long now = System.currentTimeMillis();
               if (this.queue[1].time > now) {
                  long waitForFirst = this.queue[1].time - now;
                  if (endWait != -1L && waitForFirst > wait) {
                     waitForFirst = wait;
                  }

                  try {
                     this.lock.wait(waitForFirst);
                  } catch (InterruptedException var12) {
                  }
               }

               if (this.size > 0 && this.queue != null && this.queue[1].time <= System.currentTimeMillis()) {
                  TimeoutPriorityQueueImpl.TimeoutExtImpl result = this.removeNode(1);
                  result.index = -2;
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
      synchronized(this.lock) {
         return this.size > 0 ? this.queue[1] : null;
      }
   }

   public boolean remove(TimeoutExt timeout) {
      TimeoutPriorityQueueImpl.TimeoutExtImpl timeoutImpl = (TimeoutPriorityQueueImpl.TimeoutExtImpl)timeout;
      synchronized(this.lock) {
         if (timeoutImpl.index > 0) {
            this.removeNode(timeoutImpl.index);
            timeoutImpl.index = -1;
            return true;
         } else {
            return false;
         }
      }
   }

   public void clear() {
      synchronized(this.lock) {
         if (this.queue != null) {
            for(int i = 1; i <= this.size; ++i) {
               this.queue[i] = this.cleanupTimeoutExtImpl(this.queue[i]);
            }

         }
      }
   }

   public void cancel() {
      synchronized(this.lock) {
         if (this.queue != null) {
            this.clear();
            this.queue = null;
            this.size = 0;
            this.lock.notifyAll();
         }
      }
   }

   public int size() {
      return this.size;
   }

   public boolean isCancelled() {
      return this.queue == null;
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

   private void swap(int a, int b) {
      TimeoutPriorityQueueImpl.TimeoutExtImpl temp = this.queue[a];
      this.queue[a] = this.queue[b];
      this.queue[a].index = a;
      this.queue[b] = temp;
      this.queue[b].index = b;
   }

   private TimeoutPriorityQueueImpl.TimeoutExtImpl removeNode(int index) {
      TimeoutPriorityQueueImpl.TimeoutExtImpl res = this.queue[index];
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
            long t = this.queue[index].time;

            for(int c = index << 1; c <= this.size; c = index << 1) {
               TimeoutPriorityQueueImpl.TimeoutExtImpl l = this.queue[c];
               if (c + 1 <= this.size) {
                  TimeoutPriorityQueueImpl.TimeoutExtImpl r = this.queue[c + 1];
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

            return res;
         }
      }
   }

   private TimeoutPriorityQueueImpl.TimeoutExtImpl cleanupTimeoutExtImpl(TimeoutPriorityQueueImpl.TimeoutExtImpl timeout) {
      if (timeout != null) {
         timeout.target = null;
      }

      return null;
   }

   void checkTree() {
      this.assertExpr(this.size >= 0);
      this.assertExpr(this.size < this.queue.length);
      this.assertExpr(this.queue[0] == null);
      if (this.size > 0) {
         this.assertExpr(this.queue[1] != null);
         this.assertExpr(this.queue[1].index == 1);

         int i;
         for(i = 2; i <= this.size; ++i) {
            this.assertExpr(this.queue[i] != null);
            this.assertExpr(this.queue[i].index == i);
            this.assertExpr(this.queue[i >> 1].time <= this.queue[i].time);
         }

         for(i = this.size + 1; i < this.queue.length; ++i) {
            this.assertExpr(this.queue[i] == null);
         }
      }

   }

   private void assertExpr(boolean expr) {
      if (!expr) {
         throw new IllegalStateException("***** assert failed *****");
      }
   }

   private class TimeoutExtImpl implements TimeoutExt {
      static final int DONE = -1;
      static final int TIMEOUT = -2;
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
         this.index = -1;
      }

      public boolean cancel() {
         return TimeoutPriorityQueueImpl.this.remove(this);
      }

      // $FF: synthetic method
      TimeoutExtImpl(Object x1) {
         this();
      }
   }
}
