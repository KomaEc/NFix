package org.jf.util;

public abstract class AlignmentUtils {
   public static int alignOffset(int offset, int alignment) {
      int mask = alignment - 1;

      assert alignment >= 0 && (mask & alignment) == 0;

      return offset + mask & ~mask;
   }

   public static boolean isAligned(int offset, int alignment) {
      return offset % alignment == 0;
   }
}
