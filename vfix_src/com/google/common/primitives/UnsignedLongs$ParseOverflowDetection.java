package com.google.common.primitives;

import java.math.BigInteger;

final class UnsignedLongs$ParseOverflowDetection {
   static final long[] maxValueDivs = new long[37];
   static final int[] maxValueMods = new int[37];
   static final int[] maxSafeDigits = new int[37];

   private UnsignedLongs$ParseOverflowDetection() {
   }

   static boolean overflowInParse(long current, int digit, int radix) {
      if (current >= 0L) {
         if (current < maxValueDivs[radix]) {
            return false;
         } else if (current > maxValueDivs[radix]) {
            return true;
         } else {
            return digit > maxValueMods[radix];
         }
      } else {
         return true;
      }
   }

   static {
      BigInteger overflow = new BigInteger("10000000000000000", 16);

      for(int i = 2; i <= 36; ++i) {
         maxValueDivs[i] = UnsignedLongs.divide(-1L, (long)i);
         maxValueMods[i] = (int)UnsignedLongs.remainder(-1L, (long)i);
         maxSafeDigits[i] = overflow.toString(i).length() - 1;
      }

   }
}
