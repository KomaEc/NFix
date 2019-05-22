package edu.emory.mathcs.backport.java.util.concurrent;

import edu.emory.mathcs.backport.java.util.AbstractQueue;
import edu.emory.mathcs.backport.java.util.PriorityQueue;
import edu.emory.mathcs.backport.java.util.concurrent.helpers.Utils;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DelayQueue extends AbstractQueue implements BlockingQueue {
   private final transient Object lock = new Object();
   private final PriorityQueue q = new PriorityQueue();
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public DelayQueue() {
   }

   public DelayQueue(Collection c) {
      this.addAll(c);
   }

   public boolean add(Object e) {
      return this.offer(e);
   }

   public boolean offer(Object e) {
      synchronized(this.lock) {
         Object first = this.q.peek();
         this.q.offer(e);
         if (first == null || ((Delayed)e).compareTo(first) < 0) {
            this.lock.notifyAll();
         }

         return true;
      }
   }

   public void put(Object e) {
      this.offer(e);
   }

   public boolean offer(Object e, long timeout, TimeUnit unit) {
      return this.offer(e);
   }

   public Object poll() {
      synchronized(this.lock) {
         Object first = this.q.peek();
         if (first != null && ((Delayed)first).getDelay(TimeUnit.NANOSECONDS) <= 0L) {
            Object x = this.q.poll();
            if (!$assertionsDisabled && x == null) {
               throw new AssertionError();
            } else {
               if (this.q.size() != 0) {
                  this.lock.notifyAll();
               }

               return x;
            }
         } else {
            return null;
         }
      }
   }

   public Object take() throws InterruptedException {
      synchronized(this.lock) {
         while(true) {
            while(true) {
               Object first = this.q.peek();
               if (first == null) {
                  this.lock.wait();
               } else {
                  long delay = ((Delayed)first).getDelay(TimeUnit.NANOSECONDS);
                  if (delay <= 0L) {
                     Object x = this.q.poll();
                     if (!$assertionsDisabled && x == null) {
                        throw new AssertionError();
                     }

                     if (this.q.size() != 0) {
                        this.lock.notifyAll();
                     }

                     return x;
                  }

                  TimeUnit.NANOSECONDS.timedWait(this.lock, delay);
               }
            }
         }
      }
   }

   public Object poll(long timeout, TimeUnit unit) throws InterruptedException {
      long nanos = unit.toNanos(timeout);
      long deadline = Utils.nanoTime() + nanos;
      synchronized(this.lock) {
         while(true) {
            while(true) {
               Object first = this.q.peek();
               if (first == null) {
                  if (nanos <= 0L) {
                     return null;
                  }

                  TimeUnit.NANOSECONDS.timedWait(this.lock, nanos);
                  nanos = deadline - Utils.nanoTime();
               } else {
                  long delay = ((Delayed)first).getDelay(TimeUnit.NANOSECONDS);
                  if (delay <= 0L) {
                     Object x = this.q.poll();
                     if (!$assertionsDisabled && x == null) {
                        throw new AssertionError();
                     }

                     if (this.q.size() != 0) {
                        this.lock.notifyAll();
                     }

                     return x;
                  }

                  if (nanos <= 0L) {
                     return null;
                  }

                  if (delay > nanos) {
                     delay = nanos;
                  }

                  TimeUnit.NANOSECONDS.timedWait(this.lock, delay);
                  nanos = deadline - Utils.nanoTime();
               }
            }
         }
      }
   }

   public Object peek() {
      synchronized(this.lock) {
         return this.q.peek();
      }
   }

   public int size() {
      synchronized(this.lock) {
         return this.q.size();
      }
   }

   public int drainTo(Collection c) {
      if (c == null) {
         throw new NullPointerException();
      } else if (c == this) {
         throw new IllegalArgumentException();
      } else {
         synchronized(this.lock) {
            int n = 0;

            while(true) {
               Object first = this.q.peek();
               if (first == null || ((Delayed)first).getDelay(TimeUnit.NANOSECONDS) > 0L) {
                  if (n > 0) {
                     this.lock.notifyAll();
                  }

                  return n;
               }

               c.add(this.q.poll());
               ++n;
            }
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
         synchronized(this.lock) {
            int n;
            for(n = 0; n < maxElements; ++n) {
               Object first = this.q.peek();
               if (first == null || ((Delayed)first).getDelay(TimeUnit.NANOSECONDS) > 0L) {
                  break;
               }

               c.add(this.q.poll());
            }

            if (n > 0) {
               this.lock.notifyAll();
            }

            return n;
         }
      }
   }

   public void clear() {
      synchronized(this.lock) {
         this.q.clear();
      }
   }

   public int remainingCapacity() {
      return Integer.MAX_VALUE;
   }

   public Object[] toArray() {
      synchronized(this.lock) {
         return this.q.toArray();
      }
   }

   public Object[] toArray(Object[] a) {
      synchronized(this.lock) {
         return this.q.toArray(a);
      }
   }

   public boolean remove(Object o) {
      synchronized(this.lock) {
         return this.q.remove(o);
      }
   }

   public Iterator iterator() {
      return new DelayQueue.Itr(this.toArray());
   }

   static {
      $assertionsDisabled = !DelayQueue.class.desiredAssertionStatus();
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
            return this.array[this.cursor++];
         }
      }

      public void remove() {
         if (this.lastRet < 0) {
            throw new IllegalStateException();
         } else {
            Object x = this.array[this.lastRet];
            this.lastRet = -1;
            synchronized(DelayQueue.this.lock) {
               Iterator it = DelayQueue.this.q.iterator();

               do {
                  if (!it.hasNext()) {
                     return;
                  }
               } while(it.next() != x);

               it.remove();
            }
         }
      }
   }
}
