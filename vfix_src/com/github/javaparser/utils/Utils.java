package com.github.javaparser.utils;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class Utils {
   public static final String EOL = System.getProperty("line.separator");
   public static final Predicate<String> STRING_NOT_EMPTY = (s) -> {
      return !s.isEmpty();
   };

   /** @deprecated */
   public static <T> List<T> ensureNotNull(List<T> list) {
      return (List)(list == null ? new ArrayList() : list);
   }

   public static <E> boolean isNullOrEmpty(Collection<E> collection) {
      return collection == null || collection.isEmpty();
   }

   public static <T> T assertNotNull(T o) {
      if (o == null) {
         throw new AssertionError("A reference was unexpectedly null.");
      } else {
         return o;
      }
   }

   public static String assertNonEmpty(String string) {
      if (string != null && !string.isEmpty()) {
         return string;
      } else {
         throw new AssertionError("A string was unexpectedly empty.");
      }
   }

   public static <T extends Number> T assertNonNegative(T number) {
      if (number.longValue() < 0L) {
         throw new AssertionError("A number was unexpectedly negative.");
      } else {
         return number;
      }
   }

   public static <T extends Number> T assertPositive(T number) {
      if (number.longValue() <= 0L) {
         throw new AssertionError("A number was unexpectedly non-positive.");
      } else {
         return number;
      }
   }

   public static String escapeEndOfLines(String string) {
      StringBuilder escapedString = new StringBuilder();
      char[] var2 = string.toCharArray();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         char c = var2[var4];
         switch(c) {
         case '\n':
            escapedString.append("\\n");
            break;
         case '\r':
            escapedString.append("\\r");
            break;
         default:
            escapedString.append(c);
         }
      }

      return escapedString.toString();
   }

   public static String readerToString(Reader reader) throws IOException {
      StringBuilder result = new StringBuilder();
      char[] buffer = new char[8192];

      int numChars;
      while((numChars = reader.read(buffer, 0, buffer.length)) > 0) {
         result.append(buffer, 0, numChars);
      }

      return result.toString();
   }

   /** @deprecated */
   @Deprecated
   public static <T> List<T> arrayToList(T[] array) {
      List<T> list = new LinkedList();
      Collections.addAll(list, array);
      return list;
   }

   /** @deprecated */
   public static String toCamelCase(String original) {
      return screamingToCamelCase(original);
   }

   public static String screamingToCamelCase(String original) {
      StringBuilder sb = new StringBuilder();
      String[] parts = original.toLowerCase().split("_");

      for(int i = 0; i < parts.length; ++i) {
         sb.append(i == 0 ? parts[i] : capitalize(parts[i]));
      }

      return sb.toString();
   }

   public static String camelCaseToScreaming(String input) {
      if (input.isEmpty()) {
         return "";
      } else {
         StringBuilder scream = new StringBuilder(input.substring(0, 1).toUpperCase());
         char[] var2 = input.substring(1).toCharArray();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            char c = var2[var4];
            if (Character.isUpperCase(c)) {
               scream.append("_");
            }

            scream.append(Character.toUpperCase(c));
         }

         return scream.toString();
      }
   }

   public static String nextWord(String string) {
      int index;
      for(index = 0; index < string.length() && !Character.isWhitespace(string.charAt(index)); ++index) {
      }

      return string.substring(0, index);
   }

   public static StringBuilder indent(StringBuilder builder, int indentLevel) {
      for(int i = 0; i < indentLevel; ++i) {
         builder.append("\t");
      }

      return builder;
   }

   public static String capitalize(String s) {
      return stringTransformer(s, "capitalize", String::toUpperCase);
   }

   public static String decapitalize(String s) {
      return stringTransformer(s, "decapitalize", String::toLowerCase);
   }

   private static String stringTransformer(String s, String operationDescription, Function<String, String> transformation) {
      if (s.isEmpty()) {
         throw new IllegalArgumentException(String.format("You cannot %s an empty string", operationDescription));
      } else {
         return (String)transformation.apply(s.substring(0, 1)) + s.substring(1);
      }
   }

   public static boolean valueIsNullOrEmpty(Object value) {
      if (value == null) {
         return true;
      } else {
         if (value instanceof Optional) {
            if (!((Optional)value).isPresent()) {
               return true;
            }

            value = ((Optional)value).get();
         }

         return value instanceof Collection && ((Collection)value).isEmpty();
      }
   }

   public static <T> Set<T> set(T... items) {
      return new HashSet(Arrays.asList(items));
   }

   public static String normalizeEolInTextBlock(String content, String endOfLineCharacter) {
      return content.replaceAll("\\R", endOfLineCharacter);
   }

   public static String removeFileExtension(String filename) {
      int extensionIndex = filename.lastIndexOf(".");
      return extensionIndex == -1 ? filename : filename.substring(0, extensionIndex);
   }

   public static String trimTrailingSpaces(String line) {
      while(line.length() > 0 && line.charAt(line.length() - 1) <= ' ') {
         line = line.substring(0, line.length() - 1);
      }

      return line;
   }
}
