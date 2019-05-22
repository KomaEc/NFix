package org.jboss.util;

import java.io.Serializable;

public class Counter implements Serializable, Cloneable {
   private static final long serialVersionUID = 7736259185393081556L;
   private int count;

   public Counter(int count) {
      this.count = count;
   }

   public Counter() {
   }

   public int increment() {
      return ++this.count;
   }

   public int decrement() {
      return --this.count;
   }

   public int getCount() {
      return this.count;
   }

   public void reset() {
      this.count = 0;
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (obj != null && obj.getClass() == this.getClass()) {
         return ((Counter)obj).count == this.count;
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

   public static Counter makeSynchronized(Counter counter) {
      return new Counter.Wrapper(counter) {
         private static final long serialVersionUID = -6024309396861726945L;

         public synchronized int increment() {
            return this.counter.increment();
         }

         public synchronized int decrement() {
            return this.counter.decrement();
         }

         public synchronized int getCount() {
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

   public static Counter makeDirectional(Counter counter, boolean increasing) {
      Counter.Wrapper temp;
      if (increasing) {
         temp = new Counter.Wrapper(counter) {
            private static final long serialVersionUID = 2161377898611431781L;

            public int decrement() {
               throw new UnsupportedOperationException();
            }

            public void reset() {
               throw new UnsupportedOperationException();
            }
         };
      } else {
         temp = new Counter.Wrapper(counter) {
            private static final long serialVersionUID = -4683457706354663230L;

            public int increment() {
               throw new UnsupportedOperationException();
            }
         };
      }

      return temp;
   }

   private static class Wrapper extends Counter {
      private static final long serialVersionUID = -1803971437884946242L;
      protected final Counter counter;

      public Wrapper(Counter counter) {
         this.counter = counter;
      }

      public int increment() {
         return this.counter.increment();
      }

      public int decrement() {
         return this.counter.decrement();
      }

      public int getCount() {
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
