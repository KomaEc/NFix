package soot.util;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class StringTools {
   public static final String lineSeparator = System.getProperty("line.separator");

   public static String getEscapedStringOf(String fromString) {
      StringBuffer whole = new StringBuffer();
      StringBuffer mini = new StringBuffer();
      char[] fromStringArray = fromString.toCharArray();
      int cr = lineSeparator.charAt(0);
      int lf = -1;
      if (lineSeparator.length() == 2) {
         lf = lineSeparator.charAt(1);
      }

      char[] var7 = fromStringArray;
      int var8 = fromStringArray.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         char element = var7[var9];
         if ((element >= ' ' && element <= '~' || element == cr || element == lf) && element != '\\') {
            whole.append((char)element);
         } else {
            mini.setLength(0);
            mini.append(Integer.toHexString(element));

            while(mini.length() < 4) {
               mini.insert(0, "0");
            }

            mini.insert(0, "\\u");
            whole.append(mini.toString());
         }
      }

      return whole.toString();
   }

   public static String getQuotedStringOf(String fromString) {
      StringBuffer toStringBuffer = new StringBuffer(fromString.length() + 20);
      toStringBuffer.append("\"");

      for(int i = 0; i < fromString.length(); ++i) {
         char ch = fromString.charAt(i);
         if (ch == '\\') {
            toStringBuffer.append("\\\\");
         } else if (ch == '\'') {
            toStringBuffer.append("\\'");
         } else if (ch == '"') {
            toStringBuffer.append("\\\"");
         } else if (ch == '\n') {
            toStringBuffer.append("\\n");
         } else if (ch == '\t') {
            toStringBuffer.append("\\t");
         } else if (ch == '\r') {
            toStringBuffer.append("\\r");
         } else if (ch == '\f') {
            toStringBuffer.append("\\f");
         } else if (ch >= ' ' && ch <= '~') {
            toStringBuffer.append(ch);
         } else {
            toStringBuffer.append(getUnicodeStringFromChar(ch));
         }
      }

      toStringBuffer.append("\"");
      return toStringBuffer.toString();
   }

   public static String getUnicodeStringFromChar(char ch) {
      String s = Integer.toHexString(ch);
      String padding = null;
      switch(s.length()) {
      case 1:
         padding = "000";
         break;
      case 2:
         padding = "00";
         break;
      case 3:
         padding = "0";
         break;
      case 4:
         padding = "";
      }

      return "\\u" + padding + s;
   }

   public static String getUnEscapedStringOf(String str) {
      StringBuffer buf = new StringBuffer();
      CharacterIterator iter = new StringCharacterIterator(str);

      for(char ch = iter.first(); ch != '\uffff'; ch = iter.next()) {
         if (ch != '\\') {
            buf.append(ch);
         } else {
            ch = iter.next();
            if (ch == '\\') {
               buf.append(ch);
            } else {
               char format;
               if ((format = getCFormatChar(ch)) != 0) {
                  buf.append(format);
               } else {
                  if (ch != 'u') {
                     throw new RuntimeException("Unexpected char: " + ch);
                  }

                  StringBuffer mini = new StringBuffer(4);

                  for(int i = 0; i < 4; ++i) {
                     mini.append(iter.next());
                  }

                  ch = (char)Integer.parseInt(mini.toString(), 16);
                  buf.append(ch);
               }
            }
         }
      }

      return buf.toString();
   }

   public static char getCFormatChar(char c) {
      char res;
      switch(c) {
      case '"':
         res = '"';
         break;
      case '\'':
         res = '\'';
         break;
      case 'b':
         res = '\b';
         break;
      case 'f':
         res = '\f';
         break;
      case 'n':
         res = '\n';
         break;
      case 'r':
         res = '\r';
         break;
      case 't':
         res = '\t';
         break;
      default:
         res = 0;
      }

      return res;
   }
}
