package edu.emory.mathcs.backport.java.util.concurrent;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;

public abstract class TimeUnit implements Serializable {
   public static final TimeUnit NANOSECONDS = new TimeUnit(0, "NANOSECONDS") {
      private static final long serialVersionUID = 535148490883208361L;

      public long toNanos(long d) {
         return d;
      }

      public long toMicros(long d) {
         return d / 1000L;
      }

      public long toMillis(long d) {
         return d / 1000000L;
      }

      public long toSeconds(long d) {
         return d / 1000000000L;
      }

      public long toMinutes(long d) {
         return d / 60000000000L;
      }

      public long toHours(long d) {
         return d / 3600000000000L;
      }

      public long toDays(long d) {
         return d / 86400000000000L;
      }

      public long convert(long d, TimeUnit u) {
         return u.toNanos(d);
      }

      int excessNanos(long d, long m) {
         return (int)(d - m * 1000000L);
      }
   };
   public static final TimeUnit MICROSECONDS = new TimeUnit(1, "MICROSECONDS") {
      private static final long serialVersionUID = 2185906575929579108L;

      public long toNanos(long d) {
         return x(d, 1000L, 9223372036854775L);
      }

      public long toMicros(long d) {
         return d;
      }

      public long toMillis(long d) {
         return d / 1000L;
      }

      public long toSeconds(long d) {
         return d / 1000000L;
      }

      public long toMinutes(long d) {
         return d / 60000000L;
      }

      public long toHours(long d) {
         return d / 3600000000L;
      }

      public long toDays(long d) {
         return d / 86400000000L;
      }

      public long convert(long d, TimeUnit u) {
         return u.toMicros(d);
      }

      int excessNanos(long d, long m) {
         return (int)(d * 1000L - m * 1000000L);
      }
   };
   public static final TimeUnit MILLISECONDS = new TimeUnit(2, "MILLISECONDS") {
      private static final long serialVersionUID = 9032047794123325184L;

      public long toNanos(long d) {
         return x(d, 1000000L, 9223372036854L);
      }

      public long toMicros(long d) {
         return x(d, 1000L, 9223372036854775L);
      }

      public long toMillis(long d) {
         return d;
      }

      public long toSeconds(long d) {
         return d / 1000L;
      }

      public long toMinutes(long d) {
         return d / 60000L;
      }

      public long toHours(long d) {
         return d / 3600000L;
      }

      public long toDays(long d) {
         return d / 86400000L;
      }

      public long convert(long d, TimeUnit u) {
         return u.toMillis(d);
      }

      int excessNanos(long d, long m) {
         return 0;
      }
   };
   public static final TimeUnit SECONDS = new TimeUnit(3, "SECONDS") {
      private static final long serialVersionUID = 227755028449378390L;

      public long toNanos(long d) {
         return x(d, 1000000000L, 9223372036L);
      }

      public long toMicros(long d) {
         return x(d, 1000000L, 9223372036854L);
      }

      public long toMillis(long d) {
         return x(d, 1000L, 9223372036854775L);
      }

      public long toSeconds(long d) {
         return d;
      }

      public long toMinutes(long d) {
         return d / 60L;
      }

      public long toHours(long d) {
         return d / 3600L;
      }

      public long toDays(long d) {
         return d / 86400L;
      }

      public long convert(long d, TimeUnit u) {
         return u.toSeconds(d);
      }

      int excessNanos(long d, long m) {
         return 0;
      }
   };
   public static final TimeUnit MINUTES = new TimeUnit(4, "MINUTES") {
      private static final long serialVersionUID = 1827351566402609187L;

      public long toNanos(long d) {
         return x(d, 60000000000L, 153722867L);
      }

      public long toMicros(long d) {
         return x(d, 60000000L, 153722867280L);
      }

      public long toMillis(long d) {
         return x(d, 60000L, 153722867280912L);
      }

      public long toSeconds(long d) {
         return x(d, 60L, 153722867280912930L);
      }

      public long toMinutes(long d) {
         return d;
      }

      public long toHours(long d) {
         return d / 60L;
      }

      public long toDays(long d) {
         return d / 1440L;
      }

      public long convert(long d, TimeUnit u) {
         return u.toMinutes(d);
      }

      int excessNanos(long d, long m) {
         return 0;
      }
   };
   public static final TimeUnit HOURS = new TimeUnit(5, "HOURS") {
      private static final long serialVersionUID = -6438436134732089810L;

      public long toNanos(long d) {
         return x(d, 3600000000000L, 2562047L);
      }

      public long toMicros(long d) {
         return x(d, 3600000000L, 2562047788L);
      }

      public long toMillis(long d) {
         return x(d, 3600000L, 2562047788015L);
      }

      public long toSeconds(long d) {
         return x(d, 3600L, 2562047788015215L);
      }

      public long toMinutes(long d) {
         return x(d, 60L, 153722867280912930L);
      }

      public long toHours(long d) {
         return d;
      }

      public long toDays(long d) {
         return d / 24L;
      }

      public long convert(long d, TimeUnit u) {
         return u.toHours(d);
      }

      int excessNanos(long d, long m) {
         return 0;
      }
   };
   public static final TimeUnit DAYS = new TimeUnit(6, "DAYS") {
      private static final long serialVersionUID = 567463171959674600L;

      public long toNanos(long d) {
         return x(d, 86400000000000L, 106751L);
      }

      public long toMicros(long d) {
         return x(d, 86400000000L, 106751991L);
      }

      public long toMillis(long d) {
         return x(d, 86400000L, 106751991167L);
      }

      public long toSeconds(long d) {
         return x(d, 86400L, 106751991167300L);
      }

      public long toMinutes(long d) {
         return x(d, 1440L, 6405119470038038L);
      }

      public long toHours(long d) {
         return x(d, 24L, 384307168202282325L);
      }

      public long toDays(long d) {
         return d;
      }

      public long convert(long d, TimeUnit u) {
         return u.toDays(d);
      }

      int excessNanos(long d, long m) {
         return 0;
      }
   };
   private static final TimeUnit[] values;
   private final int index;
   private final String name;
   static final long C0 = 1L;
   static final long C1 = 1000L;
   static final long C2 = 1000000L;
   static final long C3 = 1000000000L;
   static final long C4 = 60000000000L;
   static final long C5 = 3600000000000L;
   static final long C6 = 86400000000000L;
   static final long MAX = Long.MAX_VALUE;

   public static TimeUnit[] values() {
      return (TimeUnit[])values.clone();
   }

   public static TimeUnit valueOf(String name) {
      for(int i = 0; i < values.length; ++i) {
         if (values[i].name.equals(name)) {
            return values[i];
         }
      }

      throw new IllegalArgumentException("No enum const TimeUnit." + name);
   }

   TimeUnit(int index, String name) {
      this.index = index;
      this.name = name;
   }

   static long x(long d, long m, long over) {
      if (d > over) {
         return Long.MAX_VALUE;
      } else {
         return d < -over ? Long.MIN_VALUE : d * m;
      }
   }

   public abstract long convert(long var1, TimeUnit var3);

   public abstract long toNanos(long var1);

   public abstract long toMicros(long var1);

   public abstract long toMillis(long var1);

   public abstract long toSeconds(long var1);

   public abstract long toMinutes(long var1);

   public abstract long toHours(long var1);

   public abstract long toDays(long var1);

   abstract int excessNanos(long var1, long var3);

   public String name() {
      return this.name;
   }

   public int ordinal() {
      return this.index;
   }

   protected Object readResolve() throws ObjectStreamException {
      try {
         return valueOf(this.name);
      } catch (IllegalArgumentException var2) {
         throw new InvalidObjectException(this.name + " is not a valid enum for TimeUnit");
      }
   }

   public void timedWait(Object obj, long timeout) throws InterruptedException {
      if (timeout > 0L) {
         long ms = this.toMillis(timeout);
         int ns = this.excessNanos(timeout, ms);
         obj.wait(ms, ns);
      }

   }

   public void timedJoin(Thread thread, long timeout) throws InterruptedException {
      if (timeout > 0L) {
         long ms = this.toMillis(timeout);
         int ns = this.excessNanos(timeout, ms);
         thread.join(ms, ns);
      }

   }

   public void sleep(long timeout) throws InterruptedException {
      if (timeout > 0L) {
         long ms = this.toMillis(timeout);
         int ns = this.excessNanos(timeout, ms);
         Thread.sleep(ms, ns);
      }

   }

   public String toString() {
      return this.name;
   }

   static {
      values = new TimeUnit[]{NANOSECONDS, MICROSECONDS, MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS};
   }
}
