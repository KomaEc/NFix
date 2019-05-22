package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.io.Serializable;
import javax.annotation.Nullable;

@Beta
public final class BloomFilter<T> implements Predicate<T>, Serializable {
   private final BloomFilterStrategies.BitArray bits;
   private final int numHashFunctions;
   private final Funnel<T> funnel;
   private final BloomFilter.Strategy strategy;

   private BloomFilter(BloomFilterStrategies.BitArray bits, int numHashFunctions, Funnel<T> funnel, BloomFilter.Strategy strategy) {
      Preconditions.checkArgument(numHashFunctions > 0, "numHashFunctions (%s) must be > 0", numHashFunctions);
      Preconditions.checkArgument(numHashFunctions <= 255, "numHashFunctions (%s) must be <= 255", numHashFunctions);
      this.bits = (BloomFilterStrategies.BitArray)Preconditions.checkNotNull(bits);
      this.numHashFunctions = numHashFunctions;
      this.funnel = (Funnel)Preconditions.checkNotNull(funnel);
      this.strategy = (BloomFilter.Strategy)Preconditions.checkNotNull(strategy);
   }

   public BloomFilter<T> copy() {
      return new BloomFilter(this.bits.copy(), this.numHashFunctions, this.funnel, this.strategy);
   }

   public boolean mightContain(T object) {
      return this.strategy.mightContain(object, this.funnel, this.numHashFunctions, this.bits);
   }

   /** @deprecated */
   @Deprecated
   public boolean apply(T input) {
      return this.mightContain(input);
   }

   public boolean put(T object) {
      return this.strategy.put(object, this.funnel, this.numHashFunctions, this.bits);
   }

   public double expectedFpp() {
      return Math.pow((double)this.bits.bitCount() / (double)this.bitSize(), (double)this.numHashFunctions);
   }

   @VisibleForTesting
   long bitSize() {
      return (long)this.bits.bitSize();
   }

   public boolean isCompatible(BloomFilter<T> that) {
      Preconditions.checkNotNull(that);
      return this != that && this.numHashFunctions == that.numHashFunctions && this.bitSize() == that.bitSize() && this.strategy.equals(that.strategy) && this.funnel.equals(that.funnel);
   }

   public void putAll(BloomFilter<T> that) {
      Preconditions.checkNotNull(that);
      Preconditions.checkArgument(this != that, "Cannot combine a BloomFilter with itself.");
      Preconditions.checkArgument(this.numHashFunctions == that.numHashFunctions, "BloomFilters must have the same number of hash functions (%s != %s)", this.numHashFunctions, that.numHashFunctions);
      Preconditions.checkArgument(this.bitSize() == that.bitSize(), "BloomFilters must have the same size underlying bit arrays (%s != %s)", this.bitSize(), that.bitSize());
      Preconditions.checkArgument(this.strategy.equals(that.strategy), "BloomFilters must have equal strategies (%s != %s)", this.strategy, that.strategy);
      Preconditions.checkArgument(this.funnel.equals(that.funnel), "BloomFilters must have equal funnels (%s != %s)", this.funnel, that.funnel);
      this.bits.putAll(that.bits);
   }

   public boolean equals(@Nullable Object object) {
      if (object == this) {
         return true;
      } else if (!(object instanceof BloomFilter)) {
         return false;
      } else {
         BloomFilter<?> that = (BloomFilter)object;
         return this.numHashFunctions == that.numHashFunctions && this.funnel.equals(that.funnel) && this.bits.equals(that.bits) && this.strategy.equals(that.strategy);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.numHashFunctions, this.funnel, this.strategy, this.bits);
   }

   public static <T> BloomFilter<T> create(Funnel<T> funnel, int expectedInsertions, double fpp) {
      Preconditions.checkNotNull(funnel);
      Preconditions.checkArgument(expectedInsertions >= 0, "Expected insertions (%s) must be >= 0", expectedInsertions);
      Preconditions.checkArgument(fpp > 0.0D, "False positive probability (%s) must be > 0.0", fpp);
      Preconditions.checkArgument(fpp < 1.0D, "False positive probability (%s) must be < 1.0", fpp);
      if (expectedInsertions == 0) {
         expectedInsertions = 1;
      }

      long numBits = optimalNumOfBits((long)expectedInsertions, fpp);
      int numHashFunctions = optimalNumOfHashFunctions((long)expectedInsertions, numBits);

      try {
         return new BloomFilter(new BloomFilterStrategies.BitArray(numBits), numHashFunctions, funnel, BloomFilterStrategies.MURMUR128_MITZ_32);
      } catch (IllegalArgumentException var8) {
         throw new IllegalArgumentException("Could not create BloomFilter of " + numBits + " bits", var8);
      }
   }

   public static <T> BloomFilter<T> create(Funnel<T> funnel, int expectedInsertions) {
      return create(funnel, expectedInsertions, 0.03D);
   }

   @VisibleForTesting
   static int optimalNumOfHashFunctions(long n, long m) {
      return Math.max(1, (int)Math.round((double)(m / n) * Math.log(2.0D)));
   }

   @VisibleForTesting
   static long optimalNumOfBits(long n, double p) {
      if (p == 0.0D) {
         p = Double.MIN_VALUE;
      }

      return (long)((double)(-n) * Math.log(p) / (Math.log(2.0D) * Math.log(2.0D)));
   }

   private Object writeReplace() {
      return new BloomFilter.SerialForm(this);
   }

   // $FF: synthetic method
   BloomFilter(BloomFilterStrategies.BitArray x0, int x1, Funnel x2, BloomFilter.Strategy x3, Object x4) {
      this(x0, x1, x2, x3);
   }

   private static class SerialForm<T> implements Serializable {
      final long[] data;
      final int numHashFunctions;
      final Funnel<T> funnel;
      final BloomFilter.Strategy strategy;
      private static final long serialVersionUID = 1L;

      SerialForm(BloomFilter<T> bf) {
         this.data = bf.bits.data;
         this.numHashFunctions = bf.numHashFunctions;
         this.funnel = bf.funnel;
         this.strategy = bf.strategy;
      }

      Object readResolve() {
         return new BloomFilter(new BloomFilterStrategies.BitArray(this.data), this.numHashFunctions, this.funnel, this.strategy);
      }
   }

   interface Strategy extends Serializable {
      <T> boolean put(T var1, Funnel<? super T> var2, int var3, BloomFilterStrategies.BitArray var4);

      <T> boolean mightContain(T var1, Funnel<? super T> var2, int var3, BloomFilterStrategies.BitArray var4);

      int ordinal();
   }
}
