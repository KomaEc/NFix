package org.jboss.util.file;

import java.io.File;
import java.io.FilenameFilter;

public class FilenamePrefixFilter implements FilenameFilter {
   protected final String prefix;
   protected final boolean ignoreCase;

   public FilenamePrefixFilter(String prefix, boolean ignoreCase) {
      this.ignoreCase = ignoreCase;
      this.prefix = ignoreCase ? prefix.toLowerCase() : prefix;
   }

   public FilenamePrefixFilter(String prefix) {
      this(prefix, false);
   }

   public boolean accept(File dir, String name) {
      return this.ignoreCase ? name.toLowerCase().startsWith(this.prefix) : name.startsWith(this.prefix);
   }
}
