package com.gzoltar.shaded.org.apache.commons.io.filefilter;

import com.gzoltar.shaded.org.apache.commons.io.IOCase;
import java.io.File;
import java.io.Serializable;
import java.util.regex.Pattern;

public class RegexFileFilter extends AbstractFileFilter implements Serializable {
   private final Pattern pattern;

   public RegexFileFilter(String pattern) {
      if (pattern == null) {
         throw new IllegalArgumentException("Pattern is missing");
      } else {
         this.pattern = Pattern.compile(pattern);
      }
   }

   public RegexFileFilter(String pattern, IOCase caseSensitivity) {
      if (pattern == null) {
         throw new IllegalArgumentException("Pattern is missing");
      } else {
         int flags = 0;
         if (caseSensitivity != null && !caseSensitivity.isCaseSensitive()) {
            flags = 2;
         }

         this.pattern = Pattern.compile(pattern, flags);
      }
   }

   public RegexFileFilter(String pattern, int flags) {
      if (pattern == null) {
         throw new IllegalArgumentException("Pattern is missing");
      } else {
         this.pattern = Pattern.compile(pattern, flags);
      }
   }

   public RegexFileFilter(Pattern pattern) {
      if (pattern == null) {
         throw new IllegalArgumentException("Pattern is missing");
      } else {
         this.pattern = pattern;
      }
   }

   public boolean accept(File dir, String name) {
      return this.pattern.matcher(name).matches();
   }
}
