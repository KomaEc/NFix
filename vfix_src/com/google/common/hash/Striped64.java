package com.google.common.hash;

import com.google.common.annotations.GwtIncompatible;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Random;
import javax.annotation.Nullable;
import sun.misc.Unsafe;

@GwtIncompatible
abstract class Striped64 extends Number {
   static final ThreadLocal<int[]> threadHashCode = new ThreadLocal();
   static final Random rng = new Random();
   static final int NCPU = Runtime.getRuntime().availableProcessors();
   transient volatile Striped64.Cell[] cells;
   transient volatile long base;
   transient volatile int busy;
   private static final Unsafe UNSAFE;
   private static final long baseOffset;
   private static final long busyOffset;

   final boolean casBase(long cmp, long val) {
      return UNSAFE.compareAndSwapLong(this, baseOffset, cmp, val);
   }

   final boolean casBusy() {
      return UNSAFE.compareAndSwapInt(this, busyOffset, 0, 1);
   }

   abstract long fn(long var1, long var3);

   final void retryUpdate(long x, @Nullable int[] hc, boolean wasUncontended) {
      int h;
      if (hc == null) {
         threadHashCode.set(hc = new int[1]);
         int r = rng.nextInt();
         h = hc[0] = r == 0 ? 1 : r;
      } else {
         h = hc[0];
      }

      boolean collide = false;

      while(true) {
         Striped64.Cell[] as;
         int n;
         long v;
         if ((as = this.cells) != null && (n = as.length) > 0) {
            Striped64.Cell a;
            if ((a = as[n - 1 & h]) == null) {
               if (this.busy == 0) {
                  Striped64.Cell r = new Striped64.Cell(x);
                  if (this.busy == 0 && this.casBusy()) {
                     boolean created = false;

                     try {
                        Striped64.Cell[] rs;
                        int m;
                        int j;
                        if ((rs = this.cells) != null && (m = rs.length) > 0 && rs[j = m - 1 & h] == null) {
                           rs[j] = r;
                           created = true;
                        }
                     } finally {
                        this.busy = 0;
                     }

                     if (created) {
                        break;
                     }
                     continue;
                  }
               }

               collide = false;
            } else if (!wasUncontended) {
               wasUncontended = true;
            } else {
               if (a.cas(v = a.value, this.fn(v, x))) {
                  break;
               }

               if (n < NCPU && this.cells == as) {
                  if (!collide) {
                     collide = true;
                  } else if (this.busy == 0 && this.casBusy()) {
                     try {
                        if (this.cells == as) {
                           Striped64.Cell[] rs = new Striped64.Cell[n << 1];

                           for(int i = 0; i < n; ++i) {
                              rs[i] = as[i];
                           }

                           this.cells = rs;
                        }
                     } finally {
                        this.busy = 0;
                     }

                     collide = false;
                     continue;
                  }
               } else {
                  collide = false;
               }
            }

            h ^= h << 13;
            h ^= h >>> 17;
            h ^= h << 5;
            hc[0] = h;
         } else if (this.busy == 0 && this.cells == as && this.casBusy()) {
            boolean init = false;

            try {
               if (this.cells == as) {
                  Striped64.Cell[] rs = new Striped64.Cell[2];
                  rs[h & 1] = new Striped64.Cell(x);
                  this.cells = rs;
                  init = true;
               }
            } finally {
               this.busy = 0;
            }

            if (init) {
               break;
            }
         } else if (this.casBase(v = this.base, this.fn(v, x))) {
            break;
         }
      }

   }

   final void internalReset(long initialValue) {
      Striped64.Cell[] as = this.cells;
      this.base = initialValue;
      if (as != null) {
         int n = as.length;

         for(int i = 0; i < n; ++i) {
            Striped64.Cell a = as[i];
            if (a != null) {
               a.value = initialValue;
            }
         }
      }

   }

   private static Unsafe getUnsafe() {
      try {
         return Unsafe.getUnsafe();
      } catch (SecurityException var2) {
         try {
            return (Unsafe)AccessController.doPrivileged(new PrivilegedExceptionAction<Unsafe>() {
               public Unsafe run() throws Exception {
                  Class<Unsafe> k = Unsafe.class;
                  Field[] var2 = k.getDeclaredFields();
                  int var3 = var2.length;

                  for(int var4 = 0; var4 < var3; ++var4) {
                     Field f = var2[var4];
                     f.setAccessible(true);
                     Object x = f.get((Object)null);
                     if (k.isInstance(x)) {
                        return (Unsafe)k.cast(x);
                     }
                  }

                  throw new NoSuchFieldError("the Unsafe");
               }
            });
         } catch (PrivilegedActionException var1) {
            throw new RuntimeException("Could not initialize intrinsics", var1.getCause());
         }
      }
   }

   static {
      try {
         UNSAFE = getUnsafe();
         Class<?> sk = Striped64.class;
         baseOffset = UNSAFE.objectFieldOffset(sk.getDeclaredField("base"));
         busyOffset = UNSAFE.objectFieldOffset(sk.getDeclaredField("busy"));
      } catch (Exception var1) {
         throw new Error(var1);
      }
   }

   static final class Cell {
      volatile long p0;
      volatile long p1;
      volatile long p2;
      volatile long p3;
      volatile long p4;
      volatile long p5;
      volatile long p6;
      volatile long value;
      volatile long q0;
      volatile long q1;
      volatile long q2;
      volatile long q3;
      volatile long q4;
      volatile long q5;
      volatile long q6;
      private static final Unsafe UNSAFE;
      private static final long valueOffset;

      Cell(long x) {
         this.value = x;
      }

      final boolean cas(long cmp, long val) {
         return UNSAFE.compareAndSwapLong(this, valueOffset, cmp, val);
      }

      static {
         try {
            UNSAFE = Striped64.getUnsafe();
            Class<?> ak = Striped64.Cell.class;
            valueOffset = UNSAFE.objectFieldOffset(ak.getDeclaredField("value"));
         } catch (Exception var1) {
            throw new Error(var1);
         }
      }
   }
}
