package org.apache.maven.doxia.sink;

public class StructureSink {
   public static boolean isExternalLink(String link) {
      String text = link.toLowerCase();
      return text.indexOf("http:/") == 0 || text.indexOf("https:/") == 0 || text.indexOf("ftp:/") == 0 || text.indexOf("mailto:") == 0 || text.indexOf("file:/") == 0 || text.indexOf("../") == 0 || text.indexOf("./") == 0;
   }

   public static String linkToKey(String text) {
      int length = text.length();
      StringBuffer buffer = new StringBuffer(length);

      for(int i = 0; i < length; ++i) {
         char c = text.charAt(i);
         if (Character.isLetterOrDigit(c)) {
            buffer.append(Character.toLowerCase(c));
         }
      }

      return buffer.toString();
   }
}
