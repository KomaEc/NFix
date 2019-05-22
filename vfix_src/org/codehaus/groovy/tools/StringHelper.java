package org.codehaus.groovy.tools;

import java.util.LinkedList;
import java.util.List;

public class StringHelper {
   private static final char SPACE = ' ';
   private static final char SINGLE_QUOTE = '\'';
   private static final char DOUBLE_QUOTE = '"';

   public static String[] tokenizeUnquoted(String s) {
      List tokens = new LinkedList();

      int last;
      for(int first = 0; first < s.length(); first = last) {
         first = skipWhitespace(s, first);
         last = scanToken(s, first);
         if (first < last) {
            tokens.add(s.substring(first, last));
         }
      }

      return (String[])((String[])tokens.toArray(new String[0]));
   }

   private static int scanToken(String s, int pos0) {
      int pos = pos0;

      while(pos < s.length()) {
         char c = s.charAt(pos);
         if (' ' == c) {
            break;
         }

         ++pos;
         if ('\'' == c) {
            pos = scanQuoted(s, pos, '\'');
         } else if ('"' == c) {
            pos = scanQuoted(s, pos, '"');
         }
      }

      return pos;
   }

   private static int scanQuoted(String s, int pos0, char quote) {
      int pos = pos0;

      while(pos < s.length()) {
         char c = s.charAt(pos++);
         if (quote == c) {
            break;
         }
      }

      return pos;
   }

   private static int skipWhitespace(String s, int pos0) {
      int pos;
      for(pos = pos0; pos < s.length(); ++pos) {
         char c = s.charAt(pos);
         if (' ' != c) {
            break;
         }
      }

      return pos;
   }
}
