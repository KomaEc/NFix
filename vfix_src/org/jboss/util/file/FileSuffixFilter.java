package org.jboss.util.file;

import java.io.File;
import java.io.FileFilter;

public class FileSuffixFilter implements FileFilter {
   protected final String[] suffixes;
   protected final boolean ignoreCase;

   public FileSuffixFilter(String[] suffixes, boolean ignoreCase) {
      this.ignoreCase = ignoreCase;
      if (ignoreCase) {
         this.suffixes = new String[suffixes.length];

         for(int i = 0; i < suffixes.length; ++i) {
            this.suffixes[i] = suffixes[i].toLowerCase();
         }
      } else {
         this.suffixes = suffixes;
      }

   }

   public FileSuffixFilter(String[] suffixes) {
      this(suffixes, false);
   }

   public FileSuffixFilter(String suffix, boolean ignoreCase) {
      this(new String[]{suffix}, ignoreCase);
   }

   public FileSuffixFilter(String suffix) {
      this(suffix, false);
   }

   public boolean accept(File file) {
      boolean success = false;

      for(int i = 0; i < this.suffixes.length && !success; ++i) {
         if (this.ignoreCase) {
            success = file.getName().toLowerCase().endsWith(this.suffixes[i]);
         } else {
            success = file.getName().endsWith(this.suffixes[i]);
         }
      }

      return success;
   }
}
