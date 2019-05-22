package org.jf.dexlib2.dexbacked.raw;

import org.jf.dexlib2.dexbacked.BaseDexBuffer;

public class OdexHeaderItem {
   public static final int ITEM_SIZE = 40;
   private static final byte[] MAGIC_VALUE = new byte[]{100, 101, 121, 10, 0, 0, 0, 0};
   private static final int[] SUPPORTED_ODEX_VERSIONS = new int[]{35, 36};
   public static final int MAGIC_OFFSET = 0;
   public static final int MAGIC_LENGTH = 8;
   public static final int DEX_OFFSET = 8;
   public static final int DEX_LENGTH_OFFSET = 12;
   public static final int DEPENDENCIES_OFFSET = 16;
   public static final int DEPENDENCIES_LENGTH_OFFSET = 20;
   public static final int AUX_OFFSET = 24;
   public static final int AUX_LENGTH_OFFSET = 28;
   public static final int FLAGS_OFFSET = 32;

   public static boolean verifyMagic(byte[] buf, int offset) {
      if (buf.length - offset < 8) {
         return false;
      } else {
         int i;
         for(i = 0; i < 4; ++i) {
            if (buf[offset + i] != MAGIC_VALUE[i]) {
               return false;
            }
         }

         for(i = 4; i < 7; ++i) {
            if (buf[offset + i] < 48 || buf[offset + i] > 57) {
               return false;
            }
         }

         if (buf[offset + 7] != MAGIC_VALUE[7]) {
            return false;
         } else {
            return true;
         }
      }
   }

   public static int getVersion(byte[] buf, int offset) {
      return !verifyMagic(buf, offset) ? -1 : getVersionUnchecked(buf, offset);
   }

   private static int getVersionUnchecked(byte[] buf, int offset) {
      int version = (buf[offset + 4] - 48) * 100;
      version += (buf[offset + 5] - 48) * 10;
      version += buf[offset + 6] - 48;
      return version;
   }

   public static boolean isSupportedOdexVersion(int version) {
      for(int i = 0; i < SUPPORTED_ODEX_VERSIONS.length; ++i) {
         if (SUPPORTED_ODEX_VERSIONS[i] == version) {
            return true;
         }
      }

      return false;
   }

   public static int getDexOffset(byte[] buf) {
      BaseDexBuffer bdb = new BaseDexBuffer(buf);
      return bdb.readSmallUint(8);
   }

   public static int getDependenciesOffset(byte[] buf) {
      BaseDexBuffer bdb = new BaseDexBuffer(buf);
      return bdb.readSmallUint(16);
   }
}
