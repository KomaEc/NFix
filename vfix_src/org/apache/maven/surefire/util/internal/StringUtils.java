package org.apache.maven.surefire.util.internal;

import java.util.StringTokenizer;

public class StringUtils {
   private static final byte[] HEX_CHARS = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70};

   public static String[] split(String text, String separator) {
      int max = -1;
      StringTokenizer tok;
      if (separator == null) {
         tok = new StringTokenizer(text);
      } else {
         tok = new StringTokenizer(text, separator);
      }

      int listSize = tok.countTokens();
      if (max > 0 && listSize > max) {
         listSize = max;
      }

      String[] list = new String[listSize];
      int i = 0;

      for(int lastTokenEnd = 0; tok.hasMoreTokens(); ++i) {
         int lastTokenBegin;
         if (max > 0 && i == listSize - 1) {
            String endToken = tok.nextToken();
            lastTokenBegin = text.indexOf(endToken, lastTokenEnd);
            list[i] = text.substring(lastTokenBegin);
            break;
         }

         list[i] = tok.nextToken();
         lastTokenBegin = text.indexOf(list[i], lastTokenEnd);
         lastTokenEnd = lastTokenBegin + list[i].length();
      }

      return list;
   }

   public static boolean isBlank(String str) {
      return str == null || str.trim().length() == 0;
   }

   public static void escapeToPrintable(StringBuilder target, CharSequence str) {
      if (target == null) {
         throw new IllegalArgumentException("The target buffer must not be null");
      } else if (str != null) {
         for(int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            if (c >= ' ' && c <= '~' && c != '\\' && c != ',') {
               target.append(c);
            } else {
               target.append('\\');
               target.append((char)HEX_CHARS[('\uf000' & c) >> 12]);
               target.append((char)HEX_CHARS[(3840 & c) >> 8]);
               target.append((char)HEX_CHARS[(240 & c) >> 4]);
               target.append((char)HEX_CHARS[15 & c]);
            }
         }

      }
   }

   public static void unescapeString(StringBuilder target, CharSequence str) {
      if (target == null) {
         throw new IllegalArgumentException("The target buffer must not be null");
      } else if (str != null) {
         for(int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            if (ch == '\\') {
               ++i;
               int var10001 = digit(str.charAt(i)) << 12;
               ++i;
               var10001 |= digit(str.charAt(i)) << 8;
               ++i;
               var10001 |= digit(str.charAt(i)) << 4;
               ++i;
               target.append((char)(var10001 | digit(str.charAt(i))));
            } else {
               target.append(ch);
            }
         }

      }
   }

   private static int digit(char ch) {
      if (ch >= 'a') {
         return 10 + ch - 97;
      } else {
         return ch >= 'A' ? 10 + ch - 65 : ch - 48;
      }
   }

   public static int escapeBytesToPrintable(byte[] out, int outoff, byte[] input, int off, int len) {
      if (out == null) {
         throw new IllegalArgumentException("The output array must not be null");
      } else if (input != null && input.length != 0) {
         int outputPos = outoff;
         int end = off + len;

         for(int i = off; i < end; ++i) {
            byte b = input[i];
            if (b >= 32 && b <= 126 && b != 92 && b != 44) {
               out[outputPos++] = b;
            } else {
               int upper = (240 & b) >> 4;
               int lower = 15 & b;
               out[outputPos++] = 92;
               out[outputPos++] = HEX_CHARS[upper];
               out[outputPos++] = HEX_CHARS[lower];
            }
         }

         return outputPos - outoff;
      } else {
         return 0;
      }
   }

   public static int unescapeBytes(byte[] out, String str) {
      int outPos = 0;
      if (out == null) {
         throw new IllegalArgumentException("The output array must not be null");
      } else if (str == null) {
         return 0;
      } else {
         for(int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            if (ch == '\\') {
               ++i;
               int upper = digit(str.charAt(i));
               ++i;
               int lower = digit(str.charAt(i));
               out[outPos++] = (byte)(upper << 4 | lower);
            } else {
               out[outPos++] = (byte)ch;
            }
         }

         return outPos;
      }
   }
}
