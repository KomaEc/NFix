package groovyjarjarcommonscli;

class Util {
   static String stripLeadingHyphens(String str) {
      if (str == null) {
         return null;
      } else if (str.startsWith("--")) {
         return str.substring(2, str.length());
      } else {
         return str.startsWith("-") ? str.substring(1, str.length()) : str;
      }
   }

   static String stripLeadingAndTrailingQuotes(String str) {
      if (str.startsWith("\"")) {
         str = str.substring(1, str.length());
      }

      if (str.endsWith("\"")) {
         str = str.substring(0, str.length() - 1);
      }

      return str;
   }
}
