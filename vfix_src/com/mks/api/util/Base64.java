package com.mks.api.util;

public class Base64 {
   private static final char[] set = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};

   public static String encode(String string) {
      return string == null ? null : encode(string.getBytes());
   }

   public static String encode(byte[] data) {
      if (data == null) {
         return null;
      } else {
         StringBuffer result = new StringBuffer();
         int bytesLeft = data.length;

         for(int index = 0; index < data.length; index += 3) {
            byte b1 = data[index];
            byte b2 = bytesLeft < 2 ? 0 : data[index + 1];
            byte b3 = bytesLeft < 3 ? 0 : data[index + 2];
            result.append(set[b1 >>> 2 & 63]);
            result.append(set[(b1 << 4 & 48) + (b2 >>> 4 & 15)]);
            result.append(bytesLeft < 2 ? '=' : set[(b2 << 2 & 60) + (b3 >>> 6 & 3)]);
            result.append(bytesLeft < 3 ? '=' : set[b3 & 63]);
            bytesLeft -= 3;
         }

         return result.toString();
      }
   }

   public static String decode(String string) {
      return string == null ? null : new String(decodeToBytes(string));
   }

   public static byte[] decodeToBytes(String string) {
      if (string == null) {
         return null;
      } else {
         int length = string.length();
         int trim = 0;
         byte[] result = new byte[(length + 3) / 4 * 3];
         int resultIndex = 0;

         int index;
         byte[] sextet;
         for(index = 0; index < length; index += 4) {
            sextet = new byte[4];

            for(int subindex = 0; subindex < 4; ++subindex) {
               if (index + subindex < length && string.charAt(index + subindex) != '=') {
                  for(byte setindex = 0; setindex < 64; ++setindex) {
                     if (set[setindex] == string.charAt(index + subindex)) {
                        sextet[subindex] = setindex;
                        break;
                     }
                  }
               } else {
                  sextet[subindex] = 0;
                  ++trim;
               }
            }

            result[resultIndex++] = (byte)(sextet[0] << 2 | sextet[1] >> 4);
            result[resultIndex++] = (byte)((sextet[1] & 15) << 4 | sextet[2] >> 2);
            result[resultIndex++] = (byte)((sextet[2] & 3) << 6 | sextet[3]);
         }

         if (trim == 0) {
            return result;
         } else {
            index = result.length - trim;
            sextet = new byte[index];
            System.arraycopy(result, 0, sextet, 0, index);
            return sextet;
         }
      }
   }

   public static void main(String[] args) {
      if (args.length != 1) {
         System.err.println("usage: Base64 <string>");
      } else {
         System.out.println("Encoding: " + args[0]);
         String encoded = encode(args[0]);
         System.out.println("Got: " + encoded);
         System.out.println("Decoding: " + encoded);
         System.out.println("Got: " + decode(encoded));
      }
   }
}
