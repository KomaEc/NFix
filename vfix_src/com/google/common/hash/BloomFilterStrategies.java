package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;
import java.math.RoundingMode;
import java.util.Arrays;

enum BloomFilterStrategies implements BloomFilter.Strategy {
   MURMUR128_MITZ_32 {
      public <T> boolean put(T object, Funnel<? super T> funnel, int numHashFunctions, BloomFilterStrategies.BitArray bits) {
         long hash64 = Hashing.murmur3_128().hashObject(object, funnel).asLong();
         int hash1 = (int)hash64;
         int hash2 = (int)(hash64 >>> 32);
         boolean bitsChanged = false;

         for(int i = 1; i <= numHashFunctions; ++i) {
            int nextHash = hash1 + i * hash2;
            if (nextHash < 0) {
               nextHash = ~nextHash;
            }

            bitsChanged |= bits.set(nextHash % bits.bitSize());
         }

         return bitsChanged;
      }

      public <T> boolean mightContain(T object, Funnel<? super T> funnel, int numHashFunctions, BloomFilterStrategies.BitArray bits) {
         long hash64 = Hashing.murmur3_128().hashObject(object, funnel).asLong();
         int hash1 = (int)hash64;
         int hash2 = (int)(hash64 >>> 32);

         for(int i = 1; i <= numHashFunctions; ++i) {
            int nextHash = hash1 + i * hash2;
            if (nextHash < 0) {
               nextHash = ~nextHash;
            }

            if (!bits.get(nextHash % bits.bitSize())) {
               return false;
            }
         }

         return true;
      }
   };

   private BloomFilterStrategies() {
   }

   // $FF: synthetic method
   BloomFilterStrategies(Object x2) {
      this();
   }

   static class BitArray {
      final long[] data;
      int bitCount;

      BitArray(long bits) {
         this(new long[Ints.checkedCast(LongMath.divide(bits, 64L, RoundingMode.CEILING))]);
      }

      BitArray(long[] data) {
         Preconditions.checkArgument(data.length > 0, "data length is zero!");
         this.data = data;
         int bitCount = 0;
         long[] arr$ = data;
         int len$ = data.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            long value = arr$[i$];
            bitCount += Long.bitCount(value);
         }

         this.bitCount = bitCount;
      }

      boolean set(int index) {
         if (!this.get(index)) {
            long[] var10000 = this.data;
            var10000[index >> 6] |= 1L << index;
            ++this.bitCount;
            return true;
         } else {
            return false;
         }
      }

      boolean get(int index) {
         return (this.data[index >> 6] & 1L << index) != 0L;
      }

      int bitSize() {
         return this.data.length * 64;
      }

      int bitCount() {
         return this.bitCount;
      }

      BloomFilterStrategies.BitArray copy() {
         return new BloomFilterStrategies.BitArray((long[])this.data.clone());
      }

      void putAll(BloomFilterStrategies.BitArray array) {
         Preconditions.checkArgument(this.data.length == array.data.length, "BitArrays must be of equal length (%s != %s)", this.data.length, array.data.length);
         this.bitCount = 0;

         for(int i = 0; i < this.data.length; ++i) {
            long[] var10000 = this.data;
            var10000[i] |= array.data[i];
            this.bitCount += Long.bitCount(this.data[i]);
         }

      }

      public boolean equals(Object o) {
         if (o instanceof BloomFilterStrategies.BitArray) {
            BloomFilterStrategies.BitArray bitArray = (BloomFilterStrategies.BitArray)o;
            return Arrays.equals(this.data, bitArray.data);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Arrays.hashCode(this.data);
      }
   }
}
