package soot.tagkit;

public class Base64 {
   private static final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
   private static final byte[] codes = new byte[256];

   public static char[] encode(byte[] data) {
      char[] out = new char[(data.length + 2) / 3 * 4];
      int i = 0;

      for(int index = 0; i < data.length; index += 4) {
         boolean quad = false;
         boolean trip = false;
         int val = 255 & data[i];
         val <<= 8;
         if (i + 1 < data.length) {
            val |= 255 & data[i + 1];
            trip = true;
         }

         val <<= 8;
         if (i + 2 < data.length) {
            val |= 255 & data[i + 2];
            quad = true;
         }

         out[index + 3] = alphabet[quad ? val & 63 : 64];
         val >>= 6;
         out[index + 2] = alphabet[trip ? val & 63 : 64];
         val >>= 6;
         out[index + 1] = alphabet[val & 63];
         val >>= 6;
         out[index + 0] = alphabet[val & 63];
         i += 3;
      }

      return out;
   }

   public static byte[] decode(char[] data) {
      int tempLen = data.length;
      char[] var2 = data;
      int var3 = data.length;

      int shift;
      for(shift = 0; shift < var3; ++shift) {
         char element = var2[shift];
         if (element > 255 || codes[element] < 0) {
            --tempLen;
         }
      }

      int len = tempLen / 4 * 3;
      if (tempLen % 4 == 3) {
         len += 2;
      }

      if (tempLen % 4 == 2) {
         ++len;
      }

      byte[] out = new byte[len];
      shift = 0;
      int accum = 0;
      int index = 0;
      char[] var7 = data;
      int var8 = data.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         char element = var7[var9];
         int value = element > 255 ? -1 : codes[element];
         if (value >= 0) {
            accum <<= 6;
            shift += 6;
            accum |= value;
            if (shift >= 8) {
               shift -= 8;
               out[index++] = (byte)(accum >> shift & 255);
            }
         }
      }

      if (index != out.length) {
         throw new Error("Miscalculated data length (wrote " + index + " instead of " + out.length + ")");
      } else {
         return out;
      }
   }

   static {
      int i;
      for(i = 0; i < 256; ++i) {
         codes[i] = -1;
      }

      for(i = 65; i <= 90; ++i) {
         codes[i] = (byte)(i - 65);
      }

      for(i = 97; i <= 122; ++i) {
         codes[i] = (byte)(26 + i - 97);
      }

      for(i = 48; i <= 57; ++i) {
         codes[i] = (byte)(52 + i - 48);
      }

      codes[43] = 62;
      codes[47] = 63;
   }
}
