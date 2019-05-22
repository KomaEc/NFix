package com.gzoltar.shaded.org.apache.commons.lang3;

import java.util.Random;

public class RandomUtils {
   private static final Random RANDOM = new Random();

   public static byte[] nextBytes(int count) {
      Validate.isTrue(count >= 0, "Count cannot be negative.");
      byte[] result = new byte[count];
      RANDOM.nextBytes(result);
      return result;
   }

   public static int nextInt(int startInclusive, int endExclusive) {
      Validate.isTrue(endExclusive >= startInclusive, "Start value must be smaller or equal to end value.");
      Validate.isTrue(startInclusive >= 0, "Both range values must be non-negative.");
      return startInclusive == endExclusive ? startInclusive : startInclusive + RANDOM.nextInt(endExclusive - startInclusive);
   }

   public static long nextLong(long startInclusive, long endExclusive) {
      Validate.isTrue(endExclusive >= startInclusive, "Start value must be smaller or equal to end value.");
      Validate.isTrue(startInclusive >= 0L, "Both range values must be non-negative.");
      return startInclusive == endExclusive ? startInclusive : (long)nextDouble((double)startInclusive, (double)endExclusive);
   }

   public static double nextDouble(double startInclusive, double endInclusive) {
      Validate.isTrue(endInclusive >= startInclusive, "Start value must be smaller or equal to end value.");
      Validate.isTrue(startInclusive >= 0.0D, "Both range values must be non-negative.");
      return startInclusive == endInclusive ? startInclusive : startInclusive + (endInclusive - startInclusive) * RANDOM.nextDouble();
   }

   public static float nextFloat(float startInclusive, float endInclusive) {
      Validate.isTrue(endInclusive >= startInclusive, "Start value must be smaller or equal to end value.");
      Validate.isTrue(startInclusive >= 0.0F, "Both range values must be non-negative.");
      return startInclusive == endInclusive ? startInclusive : startInclusive + (endInclusive - startInclusive) * RANDOM.nextFloat();
   }
}
