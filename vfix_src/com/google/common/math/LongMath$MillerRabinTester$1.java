package com.google.common.math;

enum LongMath$MillerRabinTester$1 {
   long mulMod(long a, long b, long m) {
      return a * b % m;
   }

   long squareMod(long a, long m) {
      return a * a % m;
   }
}
