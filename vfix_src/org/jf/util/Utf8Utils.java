package org.jf.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Utf8Utils {
   private static final ThreadLocal<char[]> localBuffer = new ThreadLocal<char[]>() {
      protected char[] initialValue() {
         return new char[256];
      }
   };

   public static byte[] stringToUtf8Bytes(String string) {
      int len = string.length();
      byte[] bytes = new byte[len * 3];
      int outAt = 0;

      for(int i = 0; i < len; ++i) {
         char c = string.charAt(i);
         if (c != 0 && c < 128) {
            bytes[outAt] = (byte)c;
            ++outAt;
         } else if (c < 2048) {
            bytes[outAt] = (byte)(c >> 6 & 31 | 192);
            bytes[outAt + 1] = (byte)(c & 63 | 128);
            outAt += 2;
         } else {
            bytes[outAt] = (byte)(c >> 12 & 15 | 224);
            bytes[outAt + 1] = (byte)(c >> 6 & 63 | 128);
            bytes[outAt + 2] = (byte)(c & 63 | 128);
            outAt += 3;
         }
      }

      byte[] result = new byte[outAt];
      System.arraycopy(bytes, 0, result, 0, outAt);
      return result;
   }

   public static String utf8BytesToString(byte[] bytes, int start, int length) {
      char[] chars = (char[])localBuffer.get();
      if (chars == null || chars.length < length) {
         chars = new char[length];
         localBuffer.set(chars);
      }

      int outAt = 0;

      for(int at = start; length > 0; ++outAt) {
         int v0 = bytes[at] & 255;
         char out;
         int v1;
         int v2;
         switch(v0 >> 4) {
         case 0:
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
            --length;
            if (v0 == 0) {
               return throwBadUtf8(v0, at);
            }

            out = (char)v0;
            ++at;
            break;
         case 8:
         case 9:
         case 10:
         case 11:
         default:
            return throwBadUtf8(v0, at);
         case 12:
         case 13:
            length -= 2;
            if (length < 0) {
               return throwBadUtf8(v0, at);
            }

            v1 = bytes[at + 1] & 255;
            if ((v1 & 192) != 128) {
               return throwBadUtf8(v1, at + 1);
            }

            v2 = (v0 & 31) << 6 | v1 & 63;
            if (v2 != 0 && v2 < 128) {
               return throwBadUtf8(v1, at + 1);
            }

            out = (char)v2;
            at += 2;
            break;
         case 14:
            length -= 3;
            if (length < 0) {
               return throwBadUtf8(v0, at);
            }

            v1 = bytes[at + 1] & 255;
            if ((v1 & 192) != 128) {
               return throwBadUtf8(v1, at + 1);
            }

            v2 = bytes[at + 2] & 255;
            if ((v2 & 192) != 128) {
               return throwBadUtf8(v2, at + 2);
            }

            int value = (v0 & 15) << 12 | (v1 & 63) << 6 | v2 & 63;
            if (value < 2048) {
               return throwBadUtf8(v2, at + 2);
            }

            out = (char)value;
            at += 3;
         }

         chars[outAt] = out;
      }

      return new String(chars, 0, outAt);
   }

   public static String utf8BytesWithUtf16LengthToString(@Nonnull byte[] bytes, int start, int utf16Length) {
      return utf8BytesWithUtf16LengthToString(bytes, start, utf16Length, (int[])null);
   }

   public static String utf8BytesWithUtf16LengthToString(@Nonnull byte[] bytes, int start, int utf16Length, @Nullable int[] readLength) {
      char[] chars = (char[])localBuffer.get();
      if (chars == null || chars.length < utf16Length) {
         chars = new char[utf16Length];
         localBuffer.set(chars);
      }

      int outAt = 0;
      int at = false;

      int at;
      for(at = start; utf16Length > 0; --utf16Length) {
         int v0 = bytes[at] & 255;
         char out;
         int v1;
         int v2;
         switch(v0 >> 4) {
         case 0:
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
            if (v0 == 0) {
               return throwBadUtf8(v0, at);
            }

            out = (char)v0;
            ++at;
            break;
         case 8:
         case 9:
         case 10:
         case 11:
         default:
            return throwBadUtf8(v0, at);
         case 12:
         case 13:
            v1 = bytes[at + 1] & 255;
            if ((v1 & 192) != 128) {
               return throwBadUtf8(v1, at + 1);
            }

            v2 = (v0 & 31) << 6 | v1 & 63;
            if (v2 != 0 && v2 < 128) {
               return throwBadUtf8(v1, at + 1);
            }

            out = (char)v2;
            at += 2;
            break;
         case 14:
            v1 = bytes[at + 1] & 255;
            if ((v1 & 192) != 128) {
               return throwBadUtf8(v1, at + 1);
            }

            v2 = bytes[at + 2] & 255;
            if ((v2 & 192) != 128) {
               return throwBadUtf8(v2, at + 2);
            }

            int value = (v0 & 15) << 12 | (v1 & 63) << 6 | v2 & 63;
            if (value < 2048) {
               return throwBadUtf8(v2, at + 2);
            }

            out = (char)value;
            at += 3;
         }

         chars[outAt] = out;
         ++outAt;
      }

      if (readLength != null && readLength.length > 0) {
         readLength[0] = at - start;
         readLength[0] = at - start;
      }

      return new String(chars, 0, outAt);
   }

   private static String throwBadUtf8(int value, int offset) {
      throw new IllegalArgumentException("bad utf-8 byte " + Hex.u1(value) + " at offset " + Hex.u4(offset));
   }
}
