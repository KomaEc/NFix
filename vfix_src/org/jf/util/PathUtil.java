package org.jf.util;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PathUtil {
   private PathUtil() {
   }

   public static File getRelativeFile(File baseFile, File fileToRelativize) throws IOException {
      if (baseFile.isFile()) {
         baseFile = baseFile.getParentFile();
      }

      return new File(getRelativeFileInternal(baseFile.getCanonicalFile(), fileToRelativize.getCanonicalFile()));
   }

   static String getRelativeFileInternal(File canonicalBaseFile, File canonicalFileToRelativize) {
      List<String> basePath = getPathComponents(canonicalBaseFile);
      List<String> pathToRelativize = getPathComponents(canonicalFileToRelativize);
      if (!((String)basePath.get(0)).equals(pathToRelativize.get(0))) {
         return canonicalFileToRelativize.getPath();
      } else {
         StringBuilder sb = new StringBuilder();

         int commonDirs;
         for(commonDirs = 1; commonDirs < basePath.size() && commonDirs < pathToRelativize.size() && ((String)basePath.get(commonDirs)).equals(pathToRelativize.get(commonDirs)); ++commonDirs) {
         }

         boolean first = true;

         int i;
         for(i = commonDirs; i < basePath.size(); ++i) {
            if (!first) {
               sb.append(File.separatorChar);
            } else {
               first = false;
            }

            sb.append("..");
         }

         first = true;

         for(i = commonDirs; i < pathToRelativize.size(); ++i) {
            if (first) {
               if (sb.length() != 0) {
                  sb.append(File.separatorChar);
               }

               first = false;
            } else {
               sb.append(File.separatorChar);
            }

            sb.append((String)pathToRelativize.get(i));
         }

         if (sb.length() == 0) {
            return ".";
         } else {
            return sb.toString();
         }
      }
   }

   private static List<String> getPathComponents(File file) {
      ArrayList path;
      File parentFile;
      for(path = new ArrayList(); file != null; file = parentFile) {
         parentFile = file.getParentFile();
         if (parentFile == null) {
            path.add(file.getPath());
         } else {
            path.add(file.getName());
         }
      }

      return Lists.reverse(path);
   }
}
