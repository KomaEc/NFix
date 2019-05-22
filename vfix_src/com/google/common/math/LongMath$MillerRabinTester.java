package com.google.common.math;

enum LongMath$MillerRabinTester {
   SMALL,
   LARGE;

   private LongMath$MillerRabinTester() {
   }

   static boolean test(long base, long n) {
      return (n <= 3037000499L ? SMALL : LARGE).testWitness(base, n);
   }

   abstract long mulMod(long var1, long var3, long var5);

   abstract long squareMod(long var1, long var3);

   private long powMod(long a, long p, long m) {
      long res;
      for(res = 1L; p != 0L; p >>= 1) {
         if ((p & 1L) != 0L) {
            res = this.mulMod(res, a, m);
         }

         a = this.squareMod(a, m);
      }

      return res;
   }

   private boolean testWitness(long base, long n) {
      int r = Long.numberOfTrailingZeros(n - 1L);
      long d = n - 1L >> r;
      base %= n;
      if (base == 0L) {
         return true;
      } else {
         long a = this.powMod(base, d, n);
         if (a == 1L) {
            return true;
         } else {
            for(int j = 0; a != n - 1L; a = this.squareMod(a, n)) {
               ++j;
               if (j == r) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   // $FF: synthetic method
   LongMath$MillerRabinTester(Object x2) {
      this();
   }
}
