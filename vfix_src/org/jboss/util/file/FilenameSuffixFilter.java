package org.jboss.util.file;

import java.io.File;
import java.io.FilenameFilter;

public class FilenameSuffixFilter implements FilenameFilter {
   protected final String suffix;
   protected final boolean ignoreCase;

   public FilenameSuffixFilter(String suffix, boolean ignoreCase) {
      this.ignoreCase = ignoreCase;
      this.suffix = ignoreCase ? suffix.toLowerCase() : suffix;
   }

   public FilenameSuffixFilter(String suffix) {
      this(suffix, false);
   }

   public boolean accept(File dir, String name) {
      return this.ignoreCase ? name.toLowerCase().endsWith(this.suffix) : name.endsWith(this.suffix);
   }
}
