package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLongArray;
import javax.annotation.Nullable;

final class BloomFilterStrategies$LockFreeBitArray {
   private static final int LONG_ADDRESSABLE_BITS = 6;
   final AtomicLongArray data;
   private final LongAddable bitCount;

   BloomFilterStrategies$LockFreeBitArray(long bits) {
      this(new long[Ints.checkedCast(LongMath.divide(bits, 64L, RoundingMode.CEILING))]);
   }

   BloomFilterStrategies$LockFreeBitArray(long[] data) {
      Preconditions.checkArgument(data.length > 0, "data length is zero!");
      this.data = new AtomicLongArray(data);
      this.bitCount = LongAddables.create();
      long bitCount = 0L;
      long[] var4 = data;
      int var5 = data.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         long value = var4[var6];
         bitCount += (long)Long.bitCount(value);
      }

      this.bitCount.add(bitCount);
   }

   boolean set(long bitIndex) {
      if (this.get(bitIndex)) {
         return false;
      } else {
         int longIndex = (int)(bitIndex >>> 6);
         long mask = 1L << (int)bitIndex;

         long oldValue;
         long newValue;
         do {
            oldValue = this.data.get(longIndex);
            newValue = oldValue | mask;
            if (oldValue == newValue) {
               return false;
            }
         } while(!this.data.compareAndSet(longIndex, oldValue, newValue));

         this.bitCount.increment();
         return true;
      }
   }

   boolean get(long bitIndex) {
      return (this.data.get((int)(bitIndex >>> 6)) & 1L << (int)bitIndex) != 0L;
   }

   public static long[] toPlainArray(AtomicLongArray atomicLongArray) {
      long[] array = new long[atomicLongArray.length()];

      for(int i = 0; i < array.length; ++i) {
         array[i] = atomicLongArray.get(i);
      }

      return array;
   }

   long bitSize() {
      return (long)this.data.length() * 64L;
   }

   long bitCount() {
      return this.bitCount.sum();
   }

   BloomFilterStrategies$LockFreeBitArray copy() {
      return new BloomFilterStrategies$LockFreeBitArray(toPlainArray(this.data));
   }

   void putAll(BloomFilterStrategies$LockFreeBitArray other) {
      Preconditions.checkArgument(this.data.length() == other.data.length(), "BitArrays must be of equal length (%s != %s)", this.data.length(), other.data.length());

      for(int i = 0; i < this.data.length(); ++i) {
         long otherLong = other.data.get(i);
         boolean changedAnyBits = true;

         long ourLongOld;
         long ourLongNew;
         do {
            ourLongOld = this.data.get(i);
            ourLongNew = ourLongOld | otherLong;
            if (ourLongOld == ourLongNew) {
               changedAnyBits = false;
               break;
            }
         } while(!this.data.compareAndSet(i, ourLongOld, ourLongNew));

         if (changedAnyBits) {
            int bitsAdded = Long.bitCount(ourLongNew) - Long.bitCount(ourLongOld);
            this.bitCount.add((long)bitsAdded);
         }
      }

   }

   public boolean equals(@Nullable Object o) {
      if (o instanceof BloomFilterStrategies$LockFreeBitArray) {
         BloomFilterStrategies$LockFreeBitArray lockFreeBitArray = (BloomFilterStrategies$LockFreeBitArray)o;
         return Arrays.equals(toPlainArray(this.data), toPlainArray(lockFreeBitArray.data));
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Arrays.hashCode(toPlainArray(this.data));
   }
}
