package com.gzoltar.shaded.org.jacoco.core.internal.data;

public final class CRC64 {
   private static final long POLY64REV = -2882303761517117440L;
   private static final long[] LOOKUPTABLE = new long[256];

   public static long checksum(byte[] data) {
      long sum = 0L;
      byte[] arr$ = data;
      int len$ = data.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         byte b = arr$[i$];
         int lookupidx = ((int)sum ^ b) & 255;
         sum = sum >>> 8 ^ LOOKUPTABLE[lookupidx];
      }

      return sum;
   }

   private CRC64() {
   }

   static {
      for(int i = 0; i < 256; ++i) {
         long v = (long)i;

         for(int j = 0; j < 8; ++j) {
            if ((v & 1L) == 1L) {
               v = v >>> 1 ^ -2882303761517117440L;
            } else {
               v >>>= 1;
            }
         }

         LOOKUPTABLE[i] = v;
      }

   }
}
