package org.jf.util;

import java.io.IOException;
import java.io.Writer;

public class StringUtils {
   public static void writeEscapedChar(Writer writer, char c) throws IOException {
      if (c >= ' ' && c < 127) {
         if (c == '\'' || c == '"' || c == '\\') {
            writer.write(92);
         }

         writer.write(c);
      } else {
         if (c <= 127) {
            switch(c) {
            case '\t':
               writer.write("\\t");
               return;
            case '\n':
               writer.write("\\n");
               return;
            case '\u000b':
            case '\f':
            default:
               break;
            case '\r':
               writer.write("\\r");
               return;
            }
         }

         writer.write("\\u");
         writer.write(Character.forDigit(c >> 12, 16));
         writer.write(Character.forDigit(c >> 8 & 15, 16));
         writer.write(Character.forDigit(c >> 4 & 15, 16));
         writer.write(Character.forDigit(c & 15, 16));
      }
   }

   public static void writeEscapedString(Writer writer, String value) throws IOException {
      for(int i = 0; i < value.length(); ++i) {
         char c = value.charAt(i);
         if (c >= ' ' && c < 127) {
            if (c == '\'' || c == '"' || c == '\\') {
               writer.write(92);
            }

            writer.write(c);
         } else {
            if (c <= 127) {
               switch(c) {
               case '\t':
                  writer.write("\\t");
                  continue;
               case '\n':
                  writer.write("\\n");
                  continue;
               case '\u000b':
               case '\f':
               default:
                  break;
               case '\r':
                  writer.write("\\r");
                  continue;
               }
            }

            writer.write("\\u");
            writer.write(Character.forDigit(c >> 12, 16));
            writer.write(Character.forDigit(c >> 8 & 15, 16));
            writer.write(Character.forDigit(c >> 4 & 15, 16));
            writer.write(Character.forDigit(c & 15, 16));
         }
      }

   }

   public static String escapeString(String value) {
      int len = value.length();
      StringBuilder sb = new StringBuilder(len * 3 / 2);

      for(int i = 0; i < len; ++i) {
         char c = value.charAt(i);
         if (c >= ' ' && c < 127) {
            if (c == '\'' || c == '"' || c == '\\') {
               sb.append('\\');
            }

            sb.append(c);
         } else {
            if (c <= 127) {
               switch(c) {
               case '\t':
                  sb.append("\\t");
                  continue;
               case '\n':
                  sb.append("\\n");
                  continue;
               case '\u000b':
               case '\f':
               default:
                  break;
               case '\r':
                  sb.append("\\r");
                  continue;
               }
            }

            sb.append("\\u");
            sb.append(Character.forDigit(c >> 12, 16));
            sb.append(Character.forDigit(c >> 8 & 15, 16));
            sb.append(Character.forDigit(c >> 4 & 15, 16));
            sb.append(Character.forDigit(c & 15, 16));
         }
      }

      return sb.toString();
   }
}
