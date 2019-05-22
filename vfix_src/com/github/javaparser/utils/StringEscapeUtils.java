package com.github.javaparser.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;

public class StringEscapeUtils {
   private static final String[][] JAVA_CTRL_CHARS_UNESCAPE = new String[][]{{"\\b", "\b"}, {"\\n", "\n"}, {"\\t", "\t"}, {"\\f", "\f"}, {"\\r", "\r"}};
   private static final String[][] JAVA_CTRL_CHARS_ESCAPE = new String[][]{{"\b", "\\b"}, {"\n", "\\n"}, {"\t", "\\t"}, {"\f", "\\f"}, {"\r", "\\r"}};
   private static final StringEscapeUtils.CharSequenceTranslator ESCAPE_JAVA;
   private static final StringEscapeUtils.CharSequenceTranslator UNESCAPE_JAVA;

   private StringEscapeUtils() {
   }

   public static String escapeJava(final String input) {
      return ESCAPE_JAVA.translate(input);
   }

   public static String unescapeJava(final String input) {
      return UNESCAPE_JAVA.translate(input);
   }

   static {
      ESCAPE_JAVA = new StringEscapeUtils.AggregateTranslator(new StringEscapeUtils.CharSequenceTranslator[]{new StringEscapeUtils.LookupTranslator(new String[][]{{"\"", "\\\""}, {"\\", "\\\\"}}), new StringEscapeUtils.LookupTranslator((CharSequence[][])JAVA_CTRL_CHARS_ESCAPE.clone())});
      UNESCAPE_JAVA = new StringEscapeUtils.AggregateTranslator(new StringEscapeUtils.CharSequenceTranslator[]{new StringEscapeUtils.OctalUnescaper(), new StringEscapeUtils.UnicodeUnescaper(), new StringEscapeUtils.LookupTranslator((CharSequence[][])JAVA_CTRL_CHARS_UNESCAPE.clone()), new StringEscapeUtils.LookupTranslator(new String[][]{{"\\\\", "\\"}, {"\\\"", "\""}, {"\\'", "'"}, {"\\", ""}})});
   }

   private static class UnicodeUnescaper extends StringEscapeUtils.CharSequenceTranslator {
      private UnicodeUnescaper() {
         super(null);
      }

      public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
         if (input.charAt(index) == '\\' && index + 1 < input.length() && input.charAt(index + 1) == 'u') {
            int i;
            for(i = 2; index + i < input.length() && input.charAt(index + i) == 'u'; ++i) {
            }

            if (index + i < input.length() && input.charAt(index + i) == '+') {
               ++i;
            }

            if (index + i + 4 <= input.length()) {
               CharSequence unicode = input.subSequence(index + i, index + i + 4);

               try {
                  int value = Integer.parseInt(unicode.toString(), 16);
                  out.write((char)value);
               } catch (NumberFormatException var7) {
                  throw new IllegalArgumentException("Unable to parse unicode value: " + unicode, var7);
               }

               return i + 4;
            } else {
               throw new IllegalArgumentException("Less than 4 hex digits in unicode value: '" + input.subSequence(index, input.length()) + "' due to end of CharSequence");
            }
         } else {
            return 0;
         }
      }

      // $FF: synthetic method
      UnicodeUnescaper(Object x0) {
         this();
      }
   }

   private static class OctalUnescaper extends StringEscapeUtils.CharSequenceTranslator {
      private OctalUnescaper() {
         super(null);
      }

      public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
         int remaining = input.length() - index - 1;
         StringBuilder builder = new StringBuilder();
         if (input.charAt(index) == '\\' && remaining > 0 && this.isOctalDigit(input.charAt(index + 1))) {
            int next = index + 1;
            int next2 = index + 2;
            int next3 = index + 3;
            builder.append(input.charAt(next));
            if (remaining > 1 && this.isOctalDigit(input.charAt(next2))) {
               builder.append(input.charAt(next2));
               if (remaining > 2 && this.isZeroToThree(input.charAt(next)) && this.isOctalDigit(input.charAt(next3))) {
                  builder.append(input.charAt(next3));
               }
            }

            out.write(Integer.parseInt(builder.toString(), 8));
            return 1 + builder.length();
         } else {
            return 0;
         }
      }

      private boolean isOctalDigit(final char ch) {
         return ch >= '0' && ch <= '7';
      }

      private boolean isZeroToThree(final char ch) {
         return ch >= '0' && ch <= '3';
      }

      // $FF: synthetic method
      OctalUnescaper(Object x0) {
         this();
      }
   }

   private static class AggregateTranslator extends StringEscapeUtils.CharSequenceTranslator {
      private final StringEscapeUtils.CharSequenceTranslator[] translators;

      public AggregateTranslator(final StringEscapeUtils.CharSequenceTranslator... translators) {
         super(null);
         this.translators = translators == null ? null : (StringEscapeUtils.CharSequenceTranslator[])translators.clone();
      }

      public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
         StringEscapeUtils.CharSequenceTranslator[] var4 = this.translators;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            StringEscapeUtils.CharSequenceTranslator translator = var4[var6];
            int consumed = translator.translate(input, index, out);
            if (consumed != 0) {
               return consumed;
            }
         }

         return 0;
      }
   }

   private static class LookupTranslator extends StringEscapeUtils.CharSequenceTranslator {
      private final HashMap<String, String> lookupMap = new HashMap();
      private final HashSet<Character> prefixSet = new HashSet();
      private final int shortest;
      private final int longest;

      public LookupTranslator(final CharSequence[]... lookup) {
         super(null);
         int _shortest = Integer.MAX_VALUE;
         int _longest = 0;
         if (lookup != null) {
            CharSequence[][] var4 = lookup;
            int var5 = lookup.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               CharSequence[] seq = var4[var6];
               this.lookupMap.put(seq[0].toString(), seq[1].toString());
               this.prefixSet.add(seq[0].charAt(0));
               int sz = seq[0].length();
               if (sz < _shortest) {
                  _shortest = sz;
               }

               if (sz > _longest) {
                  _longest = sz;
               }
            }
         }

         this.shortest = _shortest;
         this.longest = _longest;
      }

      public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
         if (this.prefixSet.contains(input.charAt(index))) {
            int max = this.longest;
            if (index + this.longest > input.length()) {
               max = input.length() - index;
            }

            for(int i = max; i >= this.shortest; --i) {
               CharSequence subSeq = input.subSequence(index, index + i);
               String result = (String)this.lookupMap.get(subSeq.toString());
               if (result != null) {
                  out.write(result);
                  return i;
               }
            }
         }

         return 0;
      }
   }

   private abstract static class CharSequenceTranslator {
      private CharSequenceTranslator() {
      }

      public abstract int translate(CharSequence input, int index, Writer out) throws IOException;

      public final String translate(final CharSequence input) {
         if (input == null) {
            return null;
         } else {
            try {
               StringWriter writer = new StringWriter(input.length() * 2);
               this.translate(input, writer);
               return writer.toString();
            } catch (IOException var3) {
               throw new RuntimeException(var3);
            }
         }
      }

      public final void translate(final CharSequence input, final Writer out) throws IOException {
         if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
         } else if (input != null) {
            int pos = 0;
            int len = input.length();

            while(true) {
               while(pos < len) {
                  int consumed = this.translate(input, pos, out);
                  if (consumed == 0) {
                     char c1 = input.charAt(pos);
                     out.write(c1);
                     ++pos;
                     if (Character.isHighSurrogate(c1) && pos < len) {
                        char c2 = input.charAt(pos);
                        if (Character.isLowSurrogate(c2)) {
                           out.write(c2);
                           ++pos;
                        }
                     }
                  } else {
                     for(int pt = 0; pt < consumed; ++pt) {
                        pos += Character.charCount(Character.codePointAt(input, pos));
                     }
                  }
               }

               return;
            }
         }
      }

      public final StringEscapeUtils.CharSequenceTranslator with(final StringEscapeUtils.CharSequenceTranslator... translators) {
         StringEscapeUtils.CharSequenceTranslator[] newArray = new StringEscapeUtils.CharSequenceTranslator[translators.length + 1];
         newArray[0] = this;
         System.arraycopy(translators, 0, newArray, 1, translators.length);
         return new StringEscapeUtils.AggregateTranslator(newArray);
      }

      // $FF: synthetic method
      CharSequenceTranslator(Object x0) {
         this();
      }
   }
}
