package org.apache.tools.tar;

public class TarUtils {
   public static long parseOctal(byte[] header, int offset, int length) {
      long result = 0L;
      boolean stillPadding = true;
      int end = offset + length;

      for(int i = offset; i < end && header[i] != 0; ++i) {
         if (header[i] == 32 || header[i] == 48) {
            if (stillPadding) {
               continue;
            }

            if (header[i] == 32) {
               break;
            }
         }

         stillPadding = false;
         result = (result << 3) + (long)(header[i] - 48);
      }

      return result;
   }

   public static StringBuffer parseName(byte[] header, int offset, int length) {
      StringBuffer result = new StringBuffer(length);
      int end = offset + length;

      for(int i = offset; i < end && header[i] != 0; ++i) {
         result.append((char)header[i]);
      }

      return result;
   }

   public static int getNameBytes(StringBuffer name, byte[] buf, int offset, int length) {
      int i;
      for(i = 0; i < length && i < name.length(); ++i) {
         buf[offset + i] = (byte)name.charAt(i);
      }

      while(i < length) {
         buf[offset + i] = 0;
         ++i;
      }

      return offset + length;
   }

   public static int getOctalBytes(long value, byte[] buf, int offset, int length) {
      int idx = length - 1;
      buf[offset + idx] = 0;
      --idx;
      buf[offset + idx] = 32;
      --idx;
      if (value == 0L) {
         buf[offset + idx] = 48;
         --idx;
      } else {
         for(long val = value; idx >= 0 && val > 0L; --idx) {
            buf[offset + idx] = (byte)(48 + (byte)((int)(val & 7L)));
            val >>= 3;
         }
      }

      while(idx >= 0) {
         buf[offset + idx] = 32;
         --idx;
      }

      return offset + length;
   }

   public static int getLongOctalBytes(long value, byte[] buf, int offset, int length) {
      byte[] temp = new byte[length + 1];
      getOctalBytes(value, temp, 0, length + 1);
      System.arraycopy(temp, 0, buf, offset, length);
      return offset + length;
   }

   public static int getCheckSumOctalBytes(long value, byte[] buf, int offset, int length) {
      getOctalBytes(value, buf, offset, length);
      buf[offset + length - 1] = 32;
      buf[offset + length - 2] = 0;
      return offset + length;
   }

   public static long computeCheckSum(byte[] buf) {
      long sum = 0L;

      for(int i = 0; i < buf.length; ++i) {
         sum += (long)(255 & buf[i]);
      }

      return sum;
   }
}
