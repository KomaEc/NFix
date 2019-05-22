package polyglot.util;

public class StringUtil {
   public static String getPackageComponent(String fullName) {
      int lastDot = fullName.lastIndexOf(46);
      return lastDot >= 0 ? fullName.substring(0, lastDot) : "";
   }

   public static String getShortNameComponent(String fullName) {
      int lastDot = fullName.lastIndexOf(46);
      return lastDot >= 0 ? fullName.substring(lastDot + 1) : fullName;
   }

   public static boolean isNameShort(String name) {
      return name.indexOf(46) < 0;
   }

   public static String getFirstComponent(String fullName) {
      int firstDot = fullName.indexOf(46);
      return firstDot >= 0 ? fullName.substring(0, firstDot) : fullName;
   }

   public static String removeFirstComponent(String fullName) {
      int firstDot = fullName.indexOf(46);
      return firstDot >= 0 ? fullName.substring(firstDot + 1) : "";
   }

   public static String escape(String s) {
      return escape(s, false);
   }

   public static String escape(char c) {
      return escape(String.valueOf(c), false);
   }

   public static String unicodeEscape(String s) {
      return escape(s, true);
   }

   public static String unicodeEscape(char c) {
      return escape(String.valueOf(c), true);
   }

   public static String escape(String s, boolean unicode) {
      StringBuffer sb = new StringBuffer(s.length());

      for(int i = 0; i < s.length(); ++i) {
         char c = s.charAt(i);
         escape(sb, c, unicode);
      }

      return sb.toString();
   }

   private static void escape(StringBuffer sb, char c, boolean unicode) {
      if (c <= 255) {
         switch(c) {
         case '\b':
            sb.append("\\b");
            return;
         case '\t':
            sb.append("\\t");
            return;
         case '\n':
            sb.append("\\n");
            return;
         case '\f':
            sb.append("\\f");
            return;
         case '\r':
            sb.append("\\r");
            return;
         case '"':
            sb.append("\\" + c);
            return;
         case '\'':
            sb.append("\\" + c);
            return;
         case '\\':
            sb.append("\\" + c);
            return;
         default:
            if (c >= ' ' && c < 127) {
               sb.append(c);
            } else {
               sb.append("\\" + (char)(48 + c / 64) + (char)(48 + (c & 63) / 8) + (char)(48 + (c & 7)));
            }
         }
      } else {
         if (unicode) {
            String s;
            for(s = Integer.toHexString(c); s.length() < 4; s = "0" + s) {
            }

            sb.append("\\u" + s);
         } else {
            sb.append(c);
         }

      }
   }

   public static String nth(int n) {
      StringBuffer s = new StringBuffer(String.valueOf(n));
      if (s.length() > 1 && s.charAt(s.length() - 2) == '1') {
         s.append("th");
         return s.toString();
      } else {
         char last = s.charAt(s.length() - 1);
         switch(last) {
         case '1':
            s.append("st");
            break;
         case '2':
            s.append("nd");
            break;
         case '3':
            s.append("rd");
            break;
         default:
            s.append("th");
         }

         return s.toString();
      }
   }
}
