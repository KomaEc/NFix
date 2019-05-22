package org.apache.maven.doxia.util;

import java.io.UnsupportedEncodingException;
import org.apache.maven.doxia.sink.StructureSink;

public class HtmlTools {
   public static String escapeHTML(String text) {
      if (text == null) {
         return "";
      } else {
         int length = text.length();
         StringBuffer buffer = new StringBuffer(length);

         for(int i = 0; i < length; ++i) {
            char c = text.charAt(i);
            switch(c) {
            case '"':
               buffer.append("&quot;");
               break;
            case '&':
               buffer.append("&amp;");
               break;
            case '<':
               buffer.append("&lt;");
               break;
            case '>':
               buffer.append("&gt;");
               break;
            default:
               buffer.append(c);
            }
         }

         return buffer.toString();
      }
   }

   public static String encodeURL(String url) {
      if (url == null) {
         return null;
      } else {
         StringBuffer encoded = new StringBuffer();
         int length = url.length();
         char[] unicode = new char[1];

         for(int i = 0; i < length; ++i) {
            char c = url.charAt(i);
            switch(c) {
            case '!':
            case '#':
            case '$':
            case '&':
            case '\'':
            case '(':
            case ')':
            case '*':
            case '+':
            case ',':
            case '-':
            case '.':
            case '/':
            case ':':
            case ';':
            case '=':
            case '?':
            case '@':
            case '[':
            case ']':
            case '_':
            case '~':
               encoded.append(c);
               continue;
            case '"':
            case '%':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '<':
            case '>':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case '\\':
            case '^':
            case '`':
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'k':
            case 'l':
            case 'm':
            case 'n':
            case 'o':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 't':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
            case '{':
            case '|':
            case '}':
            }

            if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9') {
               encoded.append(c);
            } else {
               byte[] bytes;
               try {
                  unicode[0] = c;
                  bytes = (new String(unicode, 0, 1)).getBytes("UTF8");
               } catch (UnsupportedEncodingException var9) {
                  bytes = new byte[0];
               }

               for(int j = 0; j < bytes.length; ++j) {
                  String hex = Integer.toHexString(bytes[j] & 255);
                  encoded.append('%');
                  if (hex.length() == 1) {
                     encoded.append('0');
                  }

                  encoded.append(hex);
               }
            }
         }

         return encoded.toString();
      }
   }

   public static String encodeFragment(String text) {
      return text == null ? null : encodeURL(StructureSink.linkToKey(text));
   }

   public static String encodeId(String id) {
      if (id == null) {
         return null;
      } else {
         id = id.trim();
         int length = id.length();
         StringBuffer buffer = new StringBuffer(length);

         for(int i = 0; i < length; ++i) {
            char c = id.charAt(i);
            if (i == 0 && !Character.isLetter(c)) {
               buffer.append("a");
            }

            if (c == ' ') {
               buffer.append("_");
            } else if (Character.isLetterOrDigit(c) || c == '-' || c == '_' || c == ':' || c == '.') {
               buffer.append(c);
            }
         }

         return buffer.toString();
      }
   }

   public static boolean isId(String text) {
      if (text != null && text.length() != 0) {
         for(int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            if (i == 0 && !Character.isLetter(c)) {
               return false;
            }

            if (c == ' ') {
               return false;
            }

            if (!Character.isLetterOrDigit(c) && c != '-' && c != '_' && c != ':' && c != '.') {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }
}
