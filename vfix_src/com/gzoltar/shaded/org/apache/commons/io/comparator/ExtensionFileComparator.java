package com.gzoltar.shaded.org.apache.commons.io.comparator;

import com.gzoltar.shaded.org.apache.commons.io.FilenameUtils;
import com.gzoltar.shaded.org.apache.commons.io.IOCase;
import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

public class ExtensionFileComparator extends AbstractFileComparator implements Serializable {
   public static final Comparator<File> EXTENSION_COMPARATOR = new ExtensionFileComparator();
   public static final Comparator<File> EXTENSION_REVERSE;
   public static final Comparator<File> EXTENSION_INSENSITIVE_COMPARATOR;
   public static final Comparator<File> EXTENSION_INSENSITIVE_REVERSE;
   public static final Comparator<File> EXTENSION_SYSTEM_COMPARATOR;
   public static final Comparator<File> EXTENSION_SYSTEM_REVERSE;
   private final IOCase caseSensitivity;

   public ExtensionFileComparator() {
      this.caseSensitivity = IOCase.SENSITIVE;
   }

   public ExtensionFileComparator(IOCase caseSensitivity) {
      this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
   }

   public int compare(File file1, File file2) {
      String suffix1 = FilenameUtils.getExtension(file1.getName());
      String suffix2 = FilenameUtils.getExtension(file2.getName());
      return this.caseSensitivity.checkCompareTo(suffix1, suffix2);
   }

   public String toString() {
      return super.toString() + "[caseSensitivity=" + this.caseSensitivity + "]";
   }

   static {
      EXTENSION_REVERSE = new ReverseComparator(EXTENSION_COMPARATOR);
      EXTENSION_INSENSITIVE_COMPARATOR = new ExtensionFileComparator(IOCase.INSENSITIVE);
      EXTENSION_INSENSITIVE_REVERSE = new ReverseComparator(EXTENSION_INSENSITIVE_COMPARATOR);
      EXTENSION_SYSTEM_COMPARATOR = new ExtensionFileComparator(IOCase.SYSTEM);
      EXTENSION_SYSTEM_REVERSE = new ReverseComparator(EXTENSION_SYSTEM_COMPARATOR);
   }
}
