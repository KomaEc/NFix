package edu.emory.mathcs.backport.java.util.concurrent.helpers;

import edu.emory.mathcs.backport.java.util.Arrays;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import edu.emory.mathcs.backport.java.util.concurrent.locks.Condition;
import java.lang.reflect.Array;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.Iterator;
import sun.misc.Perf;

public final class Utils {
   private static final NanoTimer nanoTimer;
   private static final String providerProp = "edu.emory.mathcs.backport.java.util.concurrent.NanoTimerProvider";

   private Utils() {
   }

   public static long nanoTime() {
      return nanoTimer.nanoTime();
   }

   public static long awaitNanos(Condition cond, long nanosTimeout) throws InterruptedException {
      if (nanosTimeout <= 0L) {
         return nanosTimeout;
      } else {
         long now = nanoTime();
         cond.await(nanosTimeout, TimeUnit.NANOSECONDS);
         return nanosTimeout - (nanoTime() - now);
      }
   }

   private static long gcd(long a, long b) {
      while(b > 0L) {
         long r = a % b;
         a = b;
         b = r;
      }

      return a;
   }

   public static Object[] collectionToArray(Collection c) {
      int len = c.size();
      Object[] arr = new Object[len];
      Iterator itr = c.iterator();
      int idx = 0;

      while(true) {
         while(idx >= len || !itr.hasNext()) {
            if (!itr.hasNext()) {
               if (idx == len) {
                  return arr;
               }

               return Arrays.copyOf(arr, idx, Object[].class);
            }

            int newcap = (arr.length / 2 + 1) * 3;
            if (newcap < arr.length) {
               if (arr.length >= Integer.MAX_VALUE) {
                  throw new OutOfMemoryError("required array size too large");
               }

               newcap = Integer.MAX_VALUE;
            }

            arr = Arrays.copyOf(arr, newcap, Object[].class);
            len = newcap;
         }

         arr[idx++] = itr.next();
      }
   }

   public static Object[] collectionToArray(Collection c, Object[] a) {
      Class aType = a.getClass();
      int len = c.size();
      Object[] arr = a.length >= len ? a : (Object[])Array.newInstance(aType.getComponentType(), len);
      Iterator itr = c.iterator();
      int idx = 0;

      while(true) {
         while(idx >= len || !itr.hasNext()) {
            if (!itr.hasNext()) {
               if (idx == len) {
                  return arr;
               }

               if (arr == a) {
                  a[idx] = null;
                  return a;
               }

               return Arrays.copyOf(arr, idx, aType);
            }

            int newcap = (arr.length / 2 + 1) * 3;
            if (newcap < arr.length) {
               if (arr.length >= Integer.MAX_VALUE) {
                  throw new OutOfMemoryError("required array size too large");
               }

               newcap = Integer.MAX_VALUE;
            }

            arr = Arrays.copyOf(arr, newcap, aType);
            len = newcap;
         }

         arr[idx++] = itr.next();
      }
   }

   static {
      Object timer = null;

      try {
         String nanoTimerClassName = (String)AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
               return System.getProperty("edu.emory.mathcs.backport.java.util.concurrent.NanoTimerProvider");
            }
         });
         if (nanoTimerClassName != null) {
            Class cls = Class.forName(nanoTimerClassName);
            timer = (NanoTimer)cls.newInstance();
         }
      } catch (Exception var4) {
         System.err.println("WARNING: unable to load the system-property-defined nanotime provider; switching to the default");
         var4.printStackTrace();
      }

      if (timer == null) {
         try {
            timer = new Utils.SunPerfProvider();
         } catch (Throwable var3) {
         }
      }

      if (timer == null) {
         timer = new Utils.MillisProvider();
      }

      nanoTimer = (NanoTimer)timer;
   }

   private static final class MillisProvider implements NanoTimer {
      MillisProvider() {
      }

      public long nanoTime() {
         return System.currentTimeMillis() * 1000000L;
      }
   }

   private static final class SunPerfProvider implements NanoTimer {
      final Perf perf = (Perf)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            return Perf.getPerf();
         }
      });
      final long multiplier;
      final long divisor;

      SunPerfProvider() {
         long numerator = 1000000000L;
         long denominator = this.perf.highResFrequency();
         long gcd = Utils.gcd(numerator, denominator);
         this.multiplier = numerator / gcd;
         this.divisor = denominator / gcd;
      }

      public long nanoTime() {
         long ctr = this.perf.highResCounter();
         return ctr / this.divisor * this.multiplier + ctr % this.divisor * this.multiplier / this.divisor;
      }
   }
}
