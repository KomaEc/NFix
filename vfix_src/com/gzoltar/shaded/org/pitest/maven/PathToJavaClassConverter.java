package com.gzoltar.shaded.org.pitest.maven;

import com.gzoltar.shaded.org.pitest.functional.F;
import java.io.File;
import java.util.Collections;

class PathToJavaClassConverter implements F<String, Iterable<String>> {
   private final String sourceRoot;

   PathToJavaClassConverter(String sourceRoot) {
      this.sourceRoot = sourceRoot;
   }

   public Iterable<String> apply(String a) {
      File f = new File(a);
      String modifiedFilePath = f.getAbsolutePath();
      String fileName = f.getName();
      return (Iterable)(modifiedFilePath.startsWith(this.sourceRoot) && fileName.indexOf(46) != -1 ? this.createClassGlobFromFilePath(this.sourceRoot, modifiedFilePath) : Collections.emptyList());
   }

   private Iterable<String> createClassGlobFromFilePath(String sourceRoot, String modifiedFilePath) {
      String rootedPath = modifiedFilePath.substring(sourceRoot.length() + 1, modifiedFilePath.length());
      return Collections.singleton(this.stripFileExtension(rootedPath).replace('/', '.').replace('\\', '.') + "*");
   }

   private String stripFileExtension(String rootedPath) {
      return rootedPath.substring(0, rootedPath.lastIndexOf("."));
   }
}
