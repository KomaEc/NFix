package org.jboss.util;

import java.io.Serializable;

public class LongCounter implements Serializable, Cloneable {
   private static final long serialVersionUID = 1615297462859270139L;
   private long count;

   public LongCounter(long count) {
      this.count = count;
   }

   public LongCounter() {
   }

   public long increment() {
      return ++this.count;
   }

   public long decrement() {
      return --this.count;
   }

   public long getCount() {
      return this.count;
   }

   public void reset() {
      this.count = 0L;
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (obj != null && obj.getClass() == this.getClass()) {
         return ((LongCounter)obj).count == this.count;
      } else {
         return false;
      }
   }

   public String toString() {
      return String.valueOf(this.count);
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }

   public static LongCounter makeSynchronized(LongCounter counter) {
      return new LongCounter.Wrapper(counter) {
         private static final long serialVersionUID = 8903330696503363758L;

         public synchronized long increment() {
            return this.counter.increment();
         }

         public synchronized long decrement() {
            return this.counter.decrement();
         }

         public synchronized long getCount() {
            return this.counter.getCount();
         }

         public synchronized void reset() {
            this.counter.reset();
         }

         public synchronized int hashCode() {
            return this.counter.hashCode();
         }

         public synchronized boolean equals(Object obj) {
            return this.counter.equals(obj);
         }

         public synchronized String toString() {
            return this.counter.toString();
         }

         public synchronized Object clone() {
            return this.counter.clone();
         }
      };
   }

   public static LongCounter makeDirectional(LongCounter counter, boolean increasing) {
      LongCounter.Wrapper temp;
      if (increasing) {
         temp = new LongCounter.Wrapper(counter) {
            private static final long serialVersionUID = -8902748795144754375L;

            public long decrement() {
               throw new UnsupportedOperationException();
            }

            public void reset() {
               throw new UnsupportedOperationException();
            }
         };
      } else {
         temp = new LongCounter.Wrapper(counter) {
            private static final long serialVersionUID = 2584758778978644599L;

            public long increment() {
               throw new UnsupportedOperationException();
            }
         };
      }

      return temp;
   }

   private static class Wrapper extends LongCounter {
      private static final long serialVersionUID = 6720507617603163762L;
      protected final LongCounter counter;

      public Wrapper(LongCounter counter) {
         this.counter = counter;
      }

      public long increment() {
         return this.counter.increment();
      }

      public long decrement() {
         return this.counter.decrement();
      }

      public long getCount() {
         return this.counter.getCount();
      }

      public void reset() {
         this.counter.reset();
      }

      public boolean equals(Object obj) {
         return this.counter.equals(obj);
      }

      public String toString() {
         return this.counter.toString();
      }

      public Object clone() {
         return this.counter.clone();
      }
   }
}
