package org.jboss.util.file;

import java.io.File;
import java.io.FileFilter;

public class FilePrefixFilter implements FileFilter {
   protected final String prefix;
   protected final boolean ignoreCase;

   public FilePrefixFilter(String prefix, boolean ignoreCase) {
      this.ignoreCase = ignoreCase;
      this.prefix = ignoreCase ? prefix.toLowerCase() : prefix;
   }

   public FilePrefixFilter(String prefix) {
      this(prefix, false);
   }

   public boolean accept(File file) {
      return this.ignoreCase ? file.getName().toLowerCase().startsWith(this.prefix) : file.getName().startsWith(this.prefix);
   }
}
