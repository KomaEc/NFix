package org.codehaus.plexus.interpolation.util;

public class StringUtils {
   public static String replace(String text, String repl, String with) {
      return replace(text, repl, with, -1);
   }

   public static String replace(String text, String repl, String with, int max) {
      if (text != null && repl != null && with != null && repl.length() != 0) {
         StringBuffer buf = new StringBuffer(text.length());
         int start = 0;
         boolean var6 = false;

         int end;
         while((end = text.indexOf(repl, start)) != -1) {
            buf.append(text.substring(start, end)).append(with);
            start = end + repl.length();
            --max;
            if (max == 0) {
               break;
            }
         }

         buf.append(text.substring(start));
         return buf.toString();
      } else {
         return text;
      }
   }

   public static String capitalizeFirstLetter(String data) {
      char firstLetter = Character.toTitleCase(data.substring(0, 1).charAt(0));
      String restLetters = data.substring(1);
      return firstLetter + restLetters;
   }
}
