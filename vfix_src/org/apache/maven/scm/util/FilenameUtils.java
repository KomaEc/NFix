package org.apache.maven.scm.util;

import java.io.File;
import org.codehaus.plexus.util.StringUtils;

public final class FilenameUtils {
   private FilenameUtils() {
   }

   public static String normalizeFilename(File file) {
      return normalizeFilename(file.getName());
   }

   public static String normalizeFilename(String filename) {
      return StringUtils.replace(StringUtils.replace(filename, "\\", "/"), "//", "/");
   }
}
