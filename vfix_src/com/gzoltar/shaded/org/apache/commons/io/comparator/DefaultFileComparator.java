package com.gzoltar.shaded.org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

public class DefaultFileComparator extends AbstractFileComparator implements Serializable {
   public static final Comparator<File> DEFAULT_COMPARATOR = new DefaultFileComparator();
   public static final Comparator<File> DEFAULT_REVERSE;

   public int compare(File file1, File file2) {
      return file1.compareTo(file2);
   }

   static {
      DEFAULT_REVERSE = new ReverseComparator(DEFAULT_COMPARATOR);
   }
}
