package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.annotation.Nonnull;

public class MatchPattern {
   private final String source;
   private final String regexPattern;
   private final String separator;
   private final String[] tokenized;

   private MatchPattern(@Nonnull String source, @Nonnull String separator) {
      this.regexPattern = SelectorUtils.isRegexPrefixedPattern(source) ? source.substring("%regex[".length(), source.length() - "]".length()) : null;
      this.source = SelectorUtils.isAntPrefixedPattern(source) ? source.substring("%ant[".length(), source.length() - "]".length()) : source;
      this.separator = separator;
      this.tokenized = tokenizePathToString(this.source, separator);
   }

   public boolean matchPath(String str, boolean isCaseSensitive) {
      return this.regexPattern != null ? str.matches(this.regexPattern) : SelectorUtils.matchAntPathPattern(this, str, this.separator, isCaseSensitive);
   }

   boolean matchPath(String str, String[] strDirs, boolean isCaseSensitive) {
      return this.regexPattern != null ? str.matches(this.regexPattern) : SelectorUtils.matchAntPathPattern(this.getTokenizedPathString(), strDirs, isCaseSensitive);
   }

   public boolean matchPatternStart(@Nonnull String str, boolean isCaseSensitive) {
      if (this.regexPattern != null) {
         return true;
      } else {
         String altStr = this.source.replace('\\', '/');
         return SelectorUtils.matchAntPathPatternStart(this, str, File.separator, isCaseSensitive) || SelectorUtils.matchAntPathPatternStart(this, altStr, "/", isCaseSensitive);
      }
   }

   public String[] getTokenizedPathString() {
      return this.tokenized;
   }

   public boolean startsWith(String string) {
      return this.source.startsWith(string);
   }

   static String[] tokenizePathToString(@Nonnull String path, @Nonnull String separator) {
      List<String> ret = new ArrayList();
      StringTokenizer st = new StringTokenizer(path, separator);

      while(st.hasMoreTokens()) {
         ret.add(st.nextToken());
      }

      return (String[])ret.toArray(new String[ret.size()]);
   }

   public static MatchPattern fromString(String source) {
      return new MatchPattern(source, File.separator);
   }
}
