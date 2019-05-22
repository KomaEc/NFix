package org.jf.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;

public class TextUtils {
   private static String newline = System.getProperty("line.separator");

   @Nonnull
   public static String normalizeNewlines(@Nonnull String source) {
      return normalizeNewlines(source, newline);
   }

   @Nonnull
   public static String normalizeNewlines(@Nonnull String source, String newlineValue) {
      return source.replace("\r", "").replace("\n", newlineValue);
   }

   @Nonnull
   public static String normalizeWhitespace(@Nonnull String source) {
      source = normalizeNewlines(source);
      Pattern pattern = Pattern.compile("((^[ \t]+)|([ \t]+$))", 8);
      Matcher matcher = pattern.matcher(source);
      source = matcher.replaceAll("");
      Pattern pattern2 = Pattern.compile("^\r?\n?", 8);
      Matcher matcher2 = pattern2.matcher(source);
      source = matcher2.replaceAll("");
      Pattern pattern3 = Pattern.compile("\r?\n?$");
      Matcher matcher3 = pattern3.matcher(source);
      source = matcher3.replaceAll("");
      source = normalizeNewlines(source, "\n");
      return source;
   }

   @Nonnull
   public static String stripComments(@Nonnull String source) {
      Pattern pattern = Pattern.compile("#(.*)");
      Matcher matcher = pattern.matcher(source);
      return matcher.replaceAll("");
   }
}
