package com.google.common.primitives;

import java.util.Arrays;

final class Longs$AsciiDigits {
   private static final byte[] asciiDigits;

   private Longs$AsciiDigits() {
   }

   static int digit(char c) {
      return c < 128 ? asciiDigits[c] : -1;
   }

   static {
      byte[] result = new byte[128];
      Arrays.fill(result, (byte)-1);

      int i;
      for(i = 0; i <= 9; ++i) {
         result[48 + i] = (byte)i;
      }

      for(i = 0; i <= 26; ++i) {
         result[65 + i] = (byte)(10 + i);
         result[97 + i] = (byte)(10 + i);
      }

      asciiDigits = result;
   }
}
