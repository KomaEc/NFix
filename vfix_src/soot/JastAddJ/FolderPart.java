package soot.JastAddJ;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class FolderPart extends PathPart {
   private Map<String, Collection<String>> packageMap = new HashMap();
   private final File folder;

   public FolderPart(File folder) {
      this.folder = folder;
   }

   public boolean hasPackage(String name) {
      return !this.filesInPackage(name).isEmpty();
   }

   public boolean hasCompilationUnit(String canonicalName) {
      int index = canonicalName.lastIndexOf(46);
      String packageName = index == -1 ? "" : canonicalName.substring(0, index);
      String typeName = canonicalName.substring(index + 1, canonicalName.length());
      String fileName = typeName + this.fileSuffix();
      return this.filesInPackage(packageName).contains(fileName);
   }

   private Collection<String> filesInPackage(String packageName) {
      if (!this.packageMap.containsKey(packageName)) {
         int index = packageName.lastIndexOf(46);
         String name = packageName.substring(index == -1 ? 0 : index + 1);
         String folderName = packageName.replace('.', File.separatorChar);
         File pkgFolder = new File(this.folder, folderName);
         Object fileSet = Collections.emptyList();

         try {
            File canonical = pkgFolder.getCanonicalFile();
            if (canonical.isDirectory() && (packageName.isEmpty() || canonical.getName().equals(name))) {
               String[] files = canonical.list();
               if (files.length > 0) {
                  fileSet = new HashSet();
                  String[] var9 = files;
                  int var10 = files.length;

                  for(int var11 = 0; var11 < var10; ++var11) {
                     String file = var9[var11];
                     ((Collection)fileSet).add(file);
                  }
               }
            }
         } catch (Exception var13) {
         }

         this.packageMap.put(packageName, fileSet);
      }

      return (Collection)this.packageMap.get(packageName);
   }

   public boolean selectCompilationUnit(String canonicalName) throws IOException {
      if (this.hasCompilationUnit(canonicalName)) {
         String typeName = canonicalName.replace('.', File.separatorChar);
         String fileName = typeName + this.fileSuffix();
         File classFile = new File(this.folder, fileName);
         if (classFile.isFile()) {
            this.is = new FileInputStream(classFile);
            this.age = classFile.lastModified();
            this.pathName = classFile.getPath();
            this.relativeName = fileName + this.fileSuffix();
            this.fullName = canonicalName;
            return true;
         }
      }

      return false;
   }

   public String toString() {
      return this.folder.toString();
   }
}
