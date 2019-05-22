package org.jf.util;

public abstract class NibbleUtils {
   public static int extractHighSignedNibble(int value) {
      return value << 24 >> 28;
   }

   public static int extractLowSignedNibble(int value) {
      return value << 28 >> 28;
   }

   public static int extractHighUnsignedNibble(int value) {
      return (value & 240) >>> 4;
   }

   public static int extractLowUnsignedNibble(int value) {
      return value & 15;
   }
}
