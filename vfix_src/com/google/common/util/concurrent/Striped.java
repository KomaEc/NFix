package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import com.google.common.collect.MapMaker;
import com.google.common.math.IntMath;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Beta
public abstract class Striped<L> {
   private static final Supplier<ReadWriteLock> READ_WRITE_LOCK_SUPPLIER = new Supplier<ReadWriteLock>() {
      public ReadWriteLock get() {
         return new ReentrantReadWriteLock();
      }
   };
   private static final int ALL_SET = -1;

   private Striped() {
   }

   public abstract L get(Object var1);

   public abstract L getAt(int var1);

   abstract int indexFor(Object var1);

   public abstract int size();

   public Iterable<L> bulkGet(Iterable<?> keys) {
      Object[] array = Iterables.toArray(keys, Object.class);
      int[] stripes = new int[array.length];

      int i;
      for(i = 0; i < array.length; ++i) {
         stripes[i] = this.indexFor(array[i]);
      }

      Arrays.sort(stripes);

      for(i = 0; i < array.length; ++i) {
         array[i] = this.getAt(stripes[i]);
      }

      List<L> asList = Arrays.asList(array);
      return Collections.unmodifiableList(asList);
   }

   public static Striped<Lock> lock(int stripes) {
      return new Striped.CompactStriped(stripes, new Supplier<Lock>() {
         public Lock get() {
            return new Striped.PaddedLock();
         }
      });
   }

   public static Striped<Lock> lazyWeakLock(int stripes) {
      return new Striped.LazyStriped(stripes, new Supplier<Lock>() {
         public Lock get() {
            return new ReentrantLock(false);
         }
      });
   }

   public static Striped<Semaphore> semaphore(int stripes, final int permits) {
      return new Striped.CompactStriped(stripes, new Supplier<Semaphore>() {
         public Semaphore get() {
            return new Striped.PaddedSemaphore(permits);
         }
      });
   }

   public static Striped<Semaphore> lazyWeakSemaphore(int stripes, final int permits) {
      return new Striped.LazyStriped(stripes, new Supplier<Semaphore>() {
         public Semaphore get() {
            return new Semaphore(permits, false);
         }
      });
   }

   public static Striped<ReadWriteLock> readWriteLock(int stripes) {
      return new Striped.CompactStriped(stripes, READ_WRITE_LOCK_SUPPLIER);
   }

   public static Striped<ReadWriteLock> lazyWeakReadWriteLock(int stripes) {
      return new Striped.LazyStriped(stripes, READ_WRITE_LOCK_SUPPLIER);
   }

   private static int ceilToPowerOfTwo(int x) {
      return 1 << IntMath.log2(x, RoundingMode.CEILING);
   }

   private static int smear(int hashCode) {
      hashCode ^= hashCode >>> 20 ^ hashCode >>> 12;
      return hashCode ^ hashCode >>> 7 ^ hashCode >>> 4;
   }

   // $FF: synthetic method
   Striped(Object x0) {
      this();
   }

   private static class PaddedSemaphore extends Semaphore {
      long q1;
      long q2;
      long q3;

      PaddedSemaphore(int permits) {
         super(permits, false);
      }
   }

   private static class PaddedLock extends ReentrantLock {
      long q1;
      long q2;
      long q3;

      PaddedLock() {
         super(false);
      }
   }

   private static class LazyStriped<L> extends Striped.PowerOfTwoStriped<L> {
      final ConcurrentMap<Integer, L> locks;
      final Supplier<L> supplier;
      final int size;

      LazyStriped(int stripes, Supplier<L> supplier) {
         super(stripes);
         this.size = this.mask == -1 ? Integer.MAX_VALUE : this.mask + 1;
         this.supplier = supplier;
         this.locks = (new MapMaker()).weakValues().makeMap();
      }

      public L getAt(int index) {
         if (this.size != Integer.MAX_VALUE) {
            Preconditions.checkElementIndex(index, this.size());
         }

         L existing = this.locks.get(index);
         if (existing != null) {
            return existing;
         } else {
            L created = this.supplier.get();
            existing = this.locks.putIfAbsent(index, created);
            return Objects.firstNonNull(existing, created);
         }
      }

      public int size() {
         return this.size;
      }
   }

   private static class CompactStriped<L> extends Striped.PowerOfTwoStriped<L> {
      private final Object[] array;

      private CompactStriped(int stripes, Supplier<L> supplier) {
         super(stripes);
         Preconditions.checkArgument(stripes <= 1073741824, "Stripes must be <= 2^30)");
         this.array = new Object[this.mask + 1];

         for(int i = 0; i < this.array.length; ++i) {
            this.array[i] = supplier.get();
         }

      }

      public L getAt(int index) {
         return this.array[index];
      }

      public int size() {
         return this.array.length;
      }

      // $FF: synthetic method
      CompactStriped(int x0, Supplier x1, Object x2) {
         this(x0, x1);
      }
   }

   private abstract static class PowerOfTwoStriped<L> extends Striped<L> {
      final int mask;

      PowerOfTwoStriped(int stripes) {
         super(null);
         Preconditions.checkArgument(stripes > 0, "Stripes must be positive");
         this.mask = stripes > 1073741824 ? -1 : Striped.ceilToPowerOfTwo(stripes) - 1;
      }

      final int indexFor(Object key) {
         int hash = Striped.smear(key.hashCode());
         return hash & this.mask;
      }

      public final L get(Object key) {
         return this.getAt(this.indexFor(key));
      }
   }
}
