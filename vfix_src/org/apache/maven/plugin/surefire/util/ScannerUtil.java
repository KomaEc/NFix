package org.apache.maven.plugin.surefire.util;

import com.gzoltar.shaded.org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;

final class ScannerUtil {
   private static final String FS = System.getProperty("file.separator");
   private static final String JAVA_SOURCE_FILE_EXTENSION = ".java";
   private static final String JAVA_CLASS_FILE_EXTENSION = ".class";
   private static final boolean IS_NON_UNIX_FS;

   private ScannerUtil() {
   }

   @Nonnull
   public static String convertToJavaClassName(@Nonnull String test) {
      return StringUtils.removeEnd(test, ".class").replace(FS, ".");
   }

   @Nonnull
   public static String convertJarFileResourceToJavaClassName(@Nonnull String test) {
      return StringUtils.removeEnd(test, ".class").replace("/", ".");
   }

   @Nonnull
   public static String convertSlashToSystemFileSeparator(@Nonnull String path) {
      return IS_NON_UNIX_FS ? path.replace("/", FS) : path;
   }

   @Nonnull
   public static String stripBaseDir(String basedir, String test) {
      return StringUtils.removeStart(test, basedir);
   }

   @Nonnull
   public static String[] processIncludesExcludes(@Nonnull List<String> list) {
      List<String> newList = new ArrayList();
      Iterator i$ = list.iterator();

      String inc;
      while(i$.hasNext()) {
         Object aList = (String)i$.next();
         inc = (String)aList;
         String[] includes = inc.split(",");
         Collections.addAll(newList, includes);
      }

      String[] incs = new String[newList.size()];

      for(int i = 0; i < incs.length; ++i) {
         inc = (String)newList.get(i);
         if (inc.endsWith(".java")) {
            inc = StringUtils.removeEnd(inc, ".java") + ".class";
         }

         incs[i] = convertSlashToSystemFileSeparator(inc);
      }

      return incs;
   }

   static {
      IS_NON_UNIX_FS = !FS.equals("/");
   }
}
