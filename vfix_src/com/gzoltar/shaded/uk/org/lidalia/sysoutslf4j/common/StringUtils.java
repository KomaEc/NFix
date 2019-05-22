package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common;

public final class StringUtils {
   public static String stripEnd(String str, String stripChars) {
      return stripEnd(str, str.length() - 1, stripChars);
   }

   private static String stripEnd(String string, int index, String stripChars) {
      String result;
      if (index == -1) {
         result = "";
      } else {
         char candidateToBeStripped = string.charAt(index);
         boolean candidateShouldNotBeStripped = stripChars.indexOf(candidateToBeStripped) == -1;
         if (candidateShouldNotBeStripped) {
            result = string.substring(0, index + 1);
         } else {
            result = stripEnd(string, index - 1, stripChars);
         }
      }

      return result;
   }

   public static String substringBefore(String toBeSubstringed, String separator) {
      int indexOfSeparator = toBeSubstringed.indexOf(separator);
      String substring;
      if (indexOfSeparator == -1) {
         substring = toBeSubstringed;
      } else {
         substring = toBeSubstringed.substring(0, indexOfSeparator);
      }

      return substring;
   }

   private StringUtils() {
      throw new UnsupportedOperationException("Not instantiable");
   }
}
