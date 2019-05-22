package com.google.common.math;

import com.google.common.primitives.UnsignedLongs;

enum LongMath$MillerRabinTester$2 {
   private long plusMod(long a, long b, long m) {
      return a >= m - b ? a + b - m : a + b;
   }

   private long times2ToThe32Mod(long a, long m) {
      int remainingPowersOf2 = 32;

      do {
         int shift = Math.min(remainingPowersOf2, Long.numberOfLeadingZeros(a));
         a = UnsignedLongs.remainder(a << shift, m);
         remainingPowersOf2 -= shift;
      } while(remainingPowersOf2 > 0);

      return a;
   }

   long mulMod(long a, long b, long m) {
      long aHi = a >>> 32;
      long bHi = b >>> 32;
      long aLo = a & 4294967295L;
      long bLo = b & 4294967295L;
      long result = this.times2ToThe32Mod(aHi * bHi, m);
      result += aHi * bLo;
      if (result < 0L) {
         result = UnsignedLongs.remainder(result, m);
      }

      result += aLo * bHi;
      result = this.times2ToThe32Mod(result, m);
      return this.plusMod(result, UnsignedLongs.remainder(aLo * bLo, m), m);
   }

   long squareMod(long a, long m) {
      long aHi = a >>> 32;
      long aLo = a & 4294967295L;
      long result = this.times2ToThe32Mod(aHi * aHi, m);
      long hiLo = aHi * aLo * 2L;
      if (hiLo < 0L) {
         hiLo = UnsignedLongs.remainder(hiLo, m);
      }

      result += hiLo;
      result = this.times2ToThe32Mod(result, m);
      return this.plusMod(result, UnsignedLongs.remainder(aLo * aLo, m), m);
   }
}
