package com.gzoltar.shaded.org.pitest.util;

import java.util.Iterator;

public class StringUtil {
   public static String join(Iterable<String> strings, String separator) {
      StringBuilder sb = new StringBuilder();
      String sep = "";

      for(Iterator i$ = strings.iterator(); i$.hasNext(); sep = separator) {
         String s = (String)i$.next();
         sb.append(sep).append(s);
      }

      return sb.toString();
   }

   public static String newLine() {
      return System.getProperty("line.separator");
   }

   public static String separatorLine(char c) {
      return repeat(c, 80);
   }

   public static String separatorLine() {
      return repeat('-', 80);
   }

   public static String repeat(char c, int n) {
      return (new String(new char[n])).replace('\u0000', c);
   }

   public static String escapeBasicHtmlChars(String s) {
      StringBuilder sb = new StringBuilder();
      escapeBasicHtmlChars(s, sb);
      return sb.toString();
   }

   public static void escapeBasicHtmlChars(String s, StringBuilder out) {
      for(int i = 0; i < s.length(); ++i) {
         char c = s.charAt(i);
         if (c >= ' ' && c <= 127 && c != '&' && c != '\'' && c != '<' && c != '>' && c != '"') {
            out.append(c);
         } else {
            out.append('&');
            out.append('#');
            out.append(c);
            out.append(';');
         }
      }

   }
}
