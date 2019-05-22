package org.jboss.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

public final class Strings {
   public static final String EMPTY = "";
   private static final long MSEC = 1L;
   private static final long SECS = 1000L;
   private static final long MINS = 60000L;
   private static final long HOUR = 3600000L;
   private static final String[] keywords = new String[]{"abstract", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "extends", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while", "true", "false", "null"};
   private static final String[] ejbQlIdentifiers = new String[]{"AND", "AS", "BETWEEN", "DISTINCT", "EMPTY", "FALSE", "FROM", "IN", "IS", "LIKE", "MEMBER", "NOT", "NULL", "OBJECT", "OF", "OR", "SELECT", "UNKNOWN", "TRUE", "WHERE"};

   public static String subst(StringBuffer buff, String from, String to, String string) {
      int begin = 0;

      for(int end = 0; (end = string.indexOf(from, end)) != -1; end = begin) {
         buff.append(string.substring(begin, end));
         buff.append(to);
         begin = end + from.length();
      }

      buff.append(string.substring(begin, string.length()));
      return buff.toString();
   }

   public static String subst(String from, String to, String string) {
      return subst(new StringBuffer(), from, to, string);
   }

   public static String subst(StringBuffer buff, String string, Map map, String beginToken, String endToken) {
      int begin = 0;

      Strings.Range range;
      for(int rangeEnd = 0; (range = rangeOf(beginToken, endToken, string, rangeEnd)) != null; rangeEnd = begin) {
         buff.append(string.substring(begin, range.begin));
         String key = string.substring(range.begin + beginToken.length(), range.end);
         Object value = map.get(key);
         if (value == null) {
            value = "";
         }

         buff.append(value);
         begin = range.end + endToken.length();
      }

      buff.append(string.substring(begin, string.length()));
      return buff.toString();
   }

   public static String subst(String string, Map map, String beginToken, String endToken) {
      return subst(new StringBuffer(), string, map, beginToken, endToken);
   }

   public static String subst(StringBuffer buff, String string, String[] replace, char token) {
      int i = string.length();

      for(int j = 0; j >= 0 && j < i; ++j) {
         char c = string.charAt(j);
         if (c == token) {
            if (j != i) {
               int k = Character.digit(string.charAt(j + 1), 10);
               if (k == -1) {
                  buff.append(string.charAt(j + 1));
               } else if (k < replace.length) {
                  buff.append(replace[k]);
               }

               ++j;
            }
         } else {
            buff.append(c);
         }
      }

      return buff.toString();
   }

   public static String subst(String string, String[] replace, char token) {
      return subst(new StringBuffer(), string, replace, token);
   }

   public static String subst(String string, String[] replace) {
      return subst(new StringBuffer(), string, replace, '%');
   }

   public static Strings.Range rangeOf(String beginToken, String endToken, String string, int fromIndex) {
      int begin = string.indexOf(beginToken, fromIndex);
      if (begin != -1) {
         int end = string.indexOf(endToken, begin + 1);
         if (end != -1) {
            return new Strings.Range(begin, end);
         }
      }

      return null;
   }

   public static Strings.Range rangeOf(String beginToken, String endToken, String string) {
      return rangeOf(beginToken, endToken, string, 0);
   }

   public static String[] split(String string, String delim, int limit) {
      int count = count(string, delim) + 1;
      if (limit > 0 && count > limit) {
         count = limit;
      }

      String[] strings = new String[count];
      int begin = 0;

      for(int i = 0; i < count; ++i) {
         int end = string.indexOf(delim, begin);
         if (end == -1 || i + 1 == count) {
            end = string.length();
         }

         if (end == 0) {
            strings[i] = "";
         } else {
            strings[i] = string.substring(begin, end);
         }

         begin = end + 1;
      }

      return strings;
   }

   public static String[] split(String string, String delim) {
      return split(string, delim, -1);
   }

   public static String join(StringBuffer buff, Object[] array, String delim) {
      boolean haveDelim = delim != null;

      for(int i = 0; i < array.length; ++i) {
         buff.append(array[i]);
         if (haveDelim && i + 1 < array.length) {
            buff.append(delim);
         }
      }

      return buff.toString();
   }

   public static String join(Object[] array, String delim) {
      return join(new StringBuffer(), array, delim);
   }

   public static String join(Object[] array) {
      return join(array, (String)null);
   }

   public static String join(byte[] array) {
      Byte[] bytes = new Byte[array.length];

      for(int i = 0; i < bytes.length; ++i) {
         bytes[i] = new Byte(array[i]);
      }

      return join(bytes, (String)null);
   }

   public static String join(StringBuffer buff, Object[] array, String prefix, String separator, String suffix) {
      buff.append(prefix);
      join(buff, array, separator);
      buff.append(suffix);
      return buff.toString();
   }

   public static String join(Object[] array, String prefix, String separator, String suffix) {
      return join(new StringBuffer(), array, prefix, separator, suffix);
   }

   public static int count(String string, String substring) {
      int count = 0;

      for(int idx = 0; (idx = string.indexOf(substring, idx)) != -1; ++count) {
         ++idx;
      }

      return count;
   }

   public static int count(String string, char c) {
      return count(string, String.valueOf(c));
   }

   public static String pad(StringBuffer buff, String string, int count) {
      for(int i = 0; i < count; ++i) {
         buff.append(string);
      }

      return buff.toString();
   }

   public static String pad(String string, int count) {
      return pad(new StringBuffer(), string, count);
   }

   public static String pad(Object obj, int count) {
      return pad(new StringBuffer(), String.valueOf(obj), count);
   }

   public static boolean compare(String me, String you) {
      if (me == you) {
         return true;
      } else {
         return me == null && you != null ? false : me.equals(you);
      }
   }

   public static boolean isEmpty(String string) {
      return string.equals("");
   }

   public static int nthIndexOf(String string, String token, int index) {
      int j = 0;

      for(int i = 0; i < index; ++i) {
         j = string.indexOf(token, j + 1);
         if (j == -1) {
            break;
         }
      }

      return j;
   }

   public static String capitalize(String string) {
      if (string == null) {
         throw new NullArgumentException("string");
      } else if (string.equals("")) {
         throw new EmptyStringException("string");
      } else {
         return Character.toUpperCase(string.charAt(0)) + string.substring(1);
      }
   }

   public static String[] trim(String[] strings) {
      for(int i = 0; i < strings.length; ++i) {
         strings[i] = strings[i].trim();
      }

      return strings;
   }

   public static URL toURL(String urlspec, String relativePrefix) throws MalformedURLException {
      urlspec = urlspec.trim();

      URL url;
      try {
         url = new URL(urlspec);
         if (url.getProtocol().equals("file")) {
            url = makeURLFromFilespec(url.getFile(), relativePrefix);
         }
      } catch (Exception var6) {
         try {
            url = makeURLFromFilespec(urlspec, relativePrefix);
         } catch (IOException var5) {
            throw new MalformedURLException(var5.toString());
         }
      }

      return url;
   }

   public static URI toURI(String urispec, String relativePrefix) throws URISyntaxException {
      urispec = urispec.trim();
      URI uri;
      if (urispec.startsWith("file:")) {
         uri = makeURIFromFilespec(urispec.substring(5), relativePrefix);
      } else {
         uri = new URI(urispec);
      }

      return uri;
   }

   private static URL makeURLFromFilespec(String filespec, String relativePrefix) throws IOException {
      File file = new File(decode(filespec));
      if (relativePrefix != null && !file.isAbsolute()) {
         file = new File(relativePrefix, filespec);
      }

      file = file.getCanonicalFile();
      return file.toURI().toURL();
   }

   private static String decode(String filespec) {
      try {
         return URLDecoder.decode(filespec, "UTF-8");
      } catch (UnsupportedEncodingException var2) {
         throw new RuntimeException("Error decoding filespec: " + filespec, var2);
      }
   }

   private static URI makeURIFromFilespec(String filespec, String relativePrefix) {
      File file = new File(decode(filespec));
      if (relativePrefix != null && !file.isAbsolute()) {
         file = new File(relativePrefix, filespec);
      }

      return file.toURI();
   }

   public static URL toURL(String urlspec) throws MalformedURLException {
      return toURL(urlspec, (String)null);
   }

   public static URI toURI(String urispec) throws URISyntaxException {
      return toURI(urispec, (String)null);
   }

   public static final boolean isJavaKeyword(String s) {
      if (s != null && s.length() != 0) {
         for(int i = 0; i < keywords.length; ++i) {
            if (keywords[i].equals(s)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static final boolean isEjbQlIdentifier(String s) {
      if (s != null && s.length() != 0) {
         for(int i = 0; i < ejbQlIdentifiers.length; ++i) {
            if (ejbQlIdentifiers[i].equalsIgnoreCase(s)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static final boolean isValidJavaIdentifier(String s) {
      if (s != null && s.length() != 0) {
         char[] c = s.toCharArray();
         if (!Character.isJavaIdentifierStart(c[0])) {
            return false;
         } else {
            for(int i = 1; i < c.length; ++i) {
               if (!Character.isJavaIdentifierPart(c[i])) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public static String removeWhiteSpace(String s) {
      String retn = null;
      if (s != null) {
         int len = s.length();
         StringBuffer sbuf = new StringBuffer(len);

         for(int i = 0; i < len; ++i) {
            char c = s.charAt(i);
            if (!Character.isWhitespace(c)) {
               sbuf.append(c);
            }
         }

         retn = sbuf.toString();
      }

      return retn;
   }

   public static final String defaultToString(Object object) {
      return object == null ? "null" : object.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(object));
   }

   public static final void defaultToString(JBossStringBuilder buffer, Object object) {
      if (object == null) {
         buffer.append("null");
      } else {
         buffer.append(object.getClass().getName());
         buffer.append('@');
         buffer.append(Integer.toHexString(System.identityHashCode(object)));
      }

   }

   public static final void defaultToString(StringBuffer buffer, Object object) {
      if (object == null) {
         buffer.append("null");
      } else {
         buffer.append(object.getClass().getName());
         buffer.append('@');
         buffer.append(Integer.toHexString(System.identityHashCode(object)));
      }

   }

   public static long parseTimePeriod(String period) {
      try {
         String s = period.toLowerCase();
         long factor;
         if (s.endsWith("msec")) {
            s = s.substring(0, s.lastIndexOf("msec"));
            factor = 1L;
         } else if (s.endsWith("sec")) {
            s = s.substring(0, s.lastIndexOf("sec"));
            factor = 1000L;
         } else if (s.endsWith("min")) {
            s = s.substring(0, s.lastIndexOf("min"));
            factor = 60000L;
         } else if (s.endsWith("h")) {
            s = s.substring(0, s.lastIndexOf("h"));
            factor = 3600000L;
         } else {
            factor = 1L;
         }

         return Long.parseLong(s) * factor;
      } catch (RuntimeException var4) {
         throw new NumberFormatException("For input time period: '" + period + "'");
      }
   }

   public static long parsePositiveTimePeriod(String period) {
      long retval = parseTimePeriod(period);
      if (retval < 0L) {
         throw new NumberFormatException("Negative input time period: '" + period + "'");
      } else {
         return retval;
      }
   }

   public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
      if (str == null) {
         return null;
      } else {
         StringTokenizer st = new StringTokenizer(str, delimiters);
         ArrayList tokens = new ArrayList();

         while(true) {
            String token;
            do {
               if (!st.hasMoreTokens()) {
                  return (String[])tokens.toArray(new String[tokens.size()]);
               }

               token = st.nextToken();
               if (trimTokens) {
                  token = token.trim();
               }
            } while(ignoreEmptyTokens && token.length() <= 0);

            tokens.add(token);
         }
      }
   }

   public static String trimLeadingWhitespace(String str) {
      return trimLeadingCharacter(str, CharacterChecker.WHITESPACE);
   }

   public static String trimLeadingCharacter(String str, final char leadingCharacter) {
      return trimLeadingCharacter(str, new CharacterChecker() {
         public boolean isCharacterLegal(char character) {
            return character == leadingCharacter;
         }
      });
   }

   public static String trimLeadingCharacter(String str, CharacterChecker checker) {
      if (!hasLength(str)) {
         return str;
      } else if (checker == null) {
         throw new IllegalArgumentException("Null character checker");
      } else {
         StringBuffer buf = new StringBuffer(str);

         while(buf.length() > 0 && checker.isCharacterLegal(buf.charAt(0))) {
            buf.deleteCharAt(0);
         }

         return buf.toString();
      }
   }

   public static boolean hasLength(String string) {
      return string != null && string.length() > 0;
   }

   public static Locale parseLocaleString(String localeString) {
      String[] parts = tokenizeToStringArray(localeString, "_ ", false, false);
      String language = parts.length > 0 ? parts[0] : "";
      String country = parts.length > 1 ? parts[1] : "";
      String variant = "";
      if (parts.length >= 2) {
         int endIndexOfCountryCode = localeString.indexOf(country) + country.length();
         variant = trimLeadingWhitespace(localeString.substring(endIndexOfCountryCode));
         if (variant.startsWith("_")) {
            variant = trimLeadingCharacter(variant, '_');
         }
      }

      return language.length() > 0 ? new Locale(language, country, variant) : null;
   }

   public static class Range {
      public int begin;
      public int end;

      public Range(int begin, int end) {
         this.begin = begin;
         this.end = end;
      }

      public Range() {
      }
   }
}
