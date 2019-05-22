package org.apache.tools.ant.util;

public class Base64Converter {
   private static final char[] ALPHABET = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
   public static final char[] alphabet;

   public String encode(String s) {
      return this.encode(s.getBytes());
   }

   public String encode(byte[] octetString) {
      char[] out = new char[((octetString.length - 1) / 3 + 1) * 4];
      int outIndex = 0;

      int i;
      int bits24;
      int bits6;
      for(i = 0; i + 3 <= octetString.length; out[outIndex++] = ALPHABET[bits6]) {
         bits24 = (octetString[i++] & 255) << 16;
         bits24 |= (octetString[i++] & 255) << 8;
         bits24 |= octetString[i++];
         bits6 = (bits24 & 16515072) >> 18;
         out[outIndex++] = ALPHABET[bits6];
         bits6 = (bits24 & 258048) >> 12;
         out[outIndex++] = ALPHABET[bits6];
         bits6 = (bits24 & 4032) >> 6;
         out[outIndex++] = ALPHABET[bits6];
         bits6 = bits24 & 63;
      }

      if (octetString.length - i == 2) {
         bits24 = (octetString[i] & 255) << 16;
         bits24 |= (octetString[i + 1] & 255) << 8;
         bits6 = (bits24 & 16515072) >> 18;
         out[outIndex++] = ALPHABET[bits6];
         bits6 = (bits24 & 258048) >> 12;
         out[outIndex++] = ALPHABET[bits6];
         bits6 = (bits24 & 4032) >> 6;
         out[outIndex++] = ALPHABET[bits6];
         out[outIndex++] = '=';
      } else if (octetString.length - i == 1) {
         bits24 = (octetString[i] & 255) << 16;
         bits6 = (bits24 & 16515072) >> 18;
         out[outIndex++] = ALPHABET[bits6];
         bits6 = (bits24 & 258048) >> 12;
         out[outIndex++] = ALPHABET[bits6];
         out[outIndex++] = '=';
         out[outIndex++] = '=';
      }

      return new String(out);
   }

   static {
      alphabet = ALPHABET;
   }
}
