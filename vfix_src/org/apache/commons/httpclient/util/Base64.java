package org.apache.commons.httpclient.util;

import org.apache.commons.httpclient.HttpConstants;

/** @deprecated */
public final class Base64 {
   private static final int BASELENGTH = 255;
   private static final int LOOKUPLENGTH = 64;
   private static final int TWENTYFOURBITGROUP = 24;
   private static final int EIGHTBIT = 8;
   private static final int SIXTEENBIT = 16;
   private static final int SIXBIT = 6;
   private static final int FOURBYTE = 4;
   private static final int SIGN = -128;
   private static final byte PAD = 61;
   private static final byte[] BASE64_ALPHABET = new byte[255];
   private static final byte[] LOOKUP_BASE64_ALPHABET = new byte[64];

   private Base64() {
   }

   public static boolean isBase64(String isValidString) {
      return isArrayByteBase64(HttpConstants.getAsciiBytes(isValidString));
   }

   static boolean isBase64(byte octect) {
      return octect == 61 || BASE64_ALPHABET[octect] != -1;
   }

   public static boolean isArrayByteBase64(byte[] arrayOctect) {
      int length = arrayOctect.length;
      if (length == 0) {
         return true;
      } else {
         for(int i = 0; i < length; ++i) {
            if (!isBase64(arrayOctect[i])) {
               return false;
            }
         }

         return true;
      }
   }

   public static byte[] encode(byte[] binaryData) {
      int lengthDataBits = binaryData.length * 8;
      int fewerThan24bits = lengthDataBits % 24;
      int numberTriplets = lengthDataBits / 24;
      byte[] encodedData = null;
      byte[] encodedData;
      if (fewerThan24bits != 0) {
         encodedData = new byte[(numberTriplets + 1) * 4];
      } else {
         encodedData = new byte[numberTriplets * 4];
      }

      byte k = false;
      byte l = false;
      byte b1 = false;
      byte b2 = false;
      byte b3 = false;
      int encodedIndex = false;
      int dataIndex = false;
      int i = false;

      byte val1;
      byte val2;
      byte k;
      byte l;
      byte b1;
      byte b2;
      int encodedIndex;
      int dataIndex;
      int i;
      for(i = 0; i < numberTriplets; ++i) {
         dataIndex = i * 3;
         b1 = binaryData[dataIndex];
         b2 = binaryData[dataIndex + 1];
         byte b3 = binaryData[dataIndex + 2];
         l = (byte)(b2 & 15);
         k = (byte)(b1 & 3);
         encodedIndex = i * 4;
         val1 = (b1 & -128) == 0 ? (byte)(b1 >> 2) : (byte)(b1 >> 2 ^ 192);
         val2 = (b2 & -128) == 0 ? (byte)(b2 >> 4) : (byte)(b2 >> 4 ^ 240);
         byte val3 = (b3 & -128) == 0 ? (byte)(b3 >> 6) : (byte)(b3 >> 6 ^ 252);
         encodedData[encodedIndex] = LOOKUP_BASE64_ALPHABET[val1];
         encodedData[encodedIndex + 1] = LOOKUP_BASE64_ALPHABET[val2 | k << 4];
         encodedData[encodedIndex + 2] = LOOKUP_BASE64_ALPHABET[l << 2 | val3];
         encodedData[encodedIndex + 3] = LOOKUP_BASE64_ALPHABET[b3 & 63];
      }

      dataIndex = i * 3;
      encodedIndex = i * 4;
      if (fewerThan24bits == 8) {
         b1 = binaryData[dataIndex];
         k = (byte)(b1 & 3);
         val1 = (b1 & -128) == 0 ? (byte)(b1 >> 2) : (byte)(b1 >> 2 ^ 192);
         encodedData[encodedIndex] = LOOKUP_BASE64_ALPHABET[val1];
         encodedData[encodedIndex + 1] = LOOKUP_BASE64_ALPHABET[k << 4];
         encodedData[encodedIndex + 2] = 61;
         encodedData[encodedIndex + 3] = 61;
      } else if (fewerThan24bits == 16) {
         b1 = binaryData[dataIndex];
         b2 = binaryData[dataIndex + 1];
         l = (byte)(b2 & 15);
         k = (byte)(b1 & 3);
         val1 = (b1 & -128) == 0 ? (byte)(b1 >> 2) : (byte)(b1 >> 2 ^ 192);
         val2 = (b2 & -128) == 0 ? (byte)(b2 >> 4) : (byte)(b2 >> 4 ^ 240);
         encodedData[encodedIndex] = LOOKUP_BASE64_ALPHABET[val1];
         encodedData[encodedIndex + 1] = LOOKUP_BASE64_ALPHABET[val2 | k << 4];
         encodedData[encodedIndex + 2] = LOOKUP_BASE64_ALPHABET[l << 2];
         encodedData[encodedIndex + 3] = 61;
      }

      return encodedData;
   }

   public static byte[] decode(byte[] base64Data) {
      if (base64Data.length == 0) {
         return new byte[0];
      } else {
         int numberQuadruple = base64Data.length / 4;
         byte[] decodedData = null;
         byte b1 = false;
         byte b2 = false;
         byte b3 = false;
         byte b4 = false;
         byte marker0 = false;
         byte marker1 = false;
         int encodedIndex = 0;
         int dataIndex = false;
         int i = base64Data.length;

         while(base64Data[i - 1] == 61) {
            --i;
            if (i == 0) {
               return new byte[0];
            }
         }

         byte[] decodedData = new byte[i - numberQuadruple];

         for(i = 0; i < numberQuadruple; ++i) {
            int dataIndex = i * 4;
            byte marker0 = base64Data[dataIndex + 2];
            byte marker1 = base64Data[dataIndex + 3];
            byte b1 = BASE64_ALPHABET[base64Data[dataIndex]];
            byte b2 = BASE64_ALPHABET[base64Data[dataIndex + 1]];
            byte b3;
            if (marker0 != 61 && marker1 != 61) {
               b3 = BASE64_ALPHABET[marker0];
               byte b4 = BASE64_ALPHABET[marker1];
               decodedData[encodedIndex] = (byte)(b1 << 2 | b2 >> 4);
               decodedData[encodedIndex + 1] = (byte)((b2 & 15) << 4 | b3 >> 2 & 15);
               decodedData[encodedIndex + 2] = (byte)(b3 << 6 | b4);
            } else if (marker0 == 61) {
               decodedData[encodedIndex] = (byte)(b1 << 2 | b2 >> 4);
            } else if (marker1 == 61) {
               b3 = BASE64_ALPHABET[marker0];
               decodedData[encodedIndex] = (byte)(b1 << 2 | b2 >> 4);
               decodedData[encodedIndex + 1] = (byte)((b2 & 15) << 4 | b3 >> 2 & 15);
            }

            encodedIndex += 3;
         }

         return decodedData;
      }
   }

   static {
      for(int i = 0; i < 255; ++i) {
         BASE64_ALPHABET[i] = -1;
      }

      for(int i = 90; i >= 65; --i) {
         BASE64_ALPHABET[i] = (byte)(i - 65);
      }

      for(int i = 122; i >= 97; --i) {
         BASE64_ALPHABET[i] = (byte)(i - 97 + 26);
      }

      for(int i = 57; i >= 48; --i) {
         BASE64_ALPHABET[i] = (byte)(i - 48 + 52);
      }

      BASE64_ALPHABET[43] = 62;
      BASE64_ALPHABET[47] = 63;

      for(int i = 0; i <= 25; ++i) {
         LOOKUP_BASE64_ALPHABET[i] = (byte)(65 + i);
      }

      int i = 26;

      for(int j = 0; i <= 51; ++j) {
         LOOKUP_BASE64_ALPHABET[i] = (byte)(97 + j);
         ++i;
      }

      int i = 52;

      for(int j = 0; i <= 61; ++j) {
         LOOKUP_BASE64_ALPHABET[i] = (byte)(48 + j);
         ++i;
      }

      LOOKUP_BASE64_ALPHABET[62] = 43;
      LOOKUP_BASE64_ALPHABET[63] = 47;
   }
}
