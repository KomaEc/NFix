package soot.JastAddJ;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class FileNamesPart extends PathPart {
   private HashMap sourceFiles = new HashMap();
   private HashSet packages = new HashSet();

   public FileNamesPart(Program p) {
      this.isSource = true;
      this.program = p;
   }

   public boolean hasPackage(String name) {
      return this.packages.contains(name);
   }

   public boolean isEmpty() {
      return this.sourceFiles.isEmpty();
   }

   public Collection keySet() {
      return this.sourceFiles.keySet();
   }

   public boolean selectCompilationUnit(String canonicalName) throws IOException {
      if (this.sourceFiles.containsKey(canonicalName)) {
         String f = (String)this.sourceFiles.get(canonicalName);
         File classFile = new File(f);
         if (classFile.isFile()) {
            this.is = new FileInputStream(classFile);
            this.pathName = classFile.getPath();
            this.relativeName = f;
            this.fullName = canonicalName;
            this.sourceFiles.remove(canonicalName);
            return true;
         }
      }

      return false;
   }

   public CompilationUnit addSourceFile(String name) {
      try {
         File classFile = new File(name);
         if (classFile.isFile()) {
            this.is = new FileInputStream(classFile);
            this.pathName = classFile.getPath();
            this.relativeName = name;
            this.fullName = name;
            CompilationUnit u = this.getCompilationUnit();
            if (u != null) {
               this.program.addCompilationUnit(u);
               String packageName = u.getPackageDecl();
               if (packageName != null && !this.packages.contains(packageName)) {
                  this.packages.add(packageName);
                  int pos = 0;

                  while(packageName != null && -1 != (pos = packageName.indexOf(46, pos + 1))) {
                     String n = packageName.substring(0, pos);
                     if (!this.packages.contains(n)) {
                        this.packages.add(n);
                     }
                  }
               }
            }

            return u;
         }
      } catch (IOException var7) {
      }

      return null;
   }
}
