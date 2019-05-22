package soot.JastAddJ;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PathPart {
   protected InputStream is;
   protected String pathName;
   protected String relativeName;
   protected String fullName;
   protected long age;
   protected Program program;
   protected boolean isSource;

   protected PathPart() {
   }

   protected String fileSuffix() {
      return this.isSource ? ".java" : ".class";
   }

   public static PathPart createSourcePath(String fileName, Program program) {
      PathPart p = createPathPart(fileName);
      if (p != null) {
         p.isSource = true;
         p.program = program;
      }

      return p;
   }

   public static PathPart createClassPath(String fileName, Program program) {
      PathPart p = createPathPart(fileName);
      if (p != null) {
         p.isSource = false;
         p.program = program;
      }

      return p;
   }

   private static PathPart createPathPart(String s) {
      try {
         File f = new File(s);
         if (f.isDirectory()) {
            return new FolderPart(f);
         }

         if (f.isFile()) {
            return new ZipFilePart(f);
         }
      } catch (IOException var2) {
      }

      return null;
   }

   public InputStream getInputStream() {
      return this.is;
   }

   public long getAge() {
      return this.age;
   }

   public Program getProgram() {
      return this.program;
   }

   public void setProgram(Program program) {
      this.program = program;
   }

   public boolean hasPackage(String name) {
      return false;
   }

   public boolean selectCompilationUnit(String canonicalName) throws IOException {
      return false;
   }

   public CompilationUnit getCompilationUnit() {
      long startTime = System.currentTimeMillis();

      CompilationUnit var4;
      try {
         CompilationUnit u;
         if (!this.isSource) {
            if (this.program.options().verbose()) {
               System.out.print("Loading .class file: " + this.fullName + " ");
            }

            u = this.program.bytecodeReader.read(this.is, this.fullName, this.program);
            u.setPathName(this.pathName);
            u.setRelativeName(this.relativeName);
            u.setFromSource(false);
            if (this.program.options().verbose()) {
               System.out.println("from " + this.pathName + " in " + (System.currentTimeMillis() - startTime) + " ms");
            }

            var4 = u;
            return var4;
         }

         if (this.program.options().verbose()) {
            System.out.print("Loading .java file: " + this.fullName + " ");
         }

         u = this.program.javaParser.parse(this.is, this.fullName);
         u.setPathName(this.pathName);
         u.setRelativeName(this.relativeName);
         u.setFromSource(true);
         if (this.program.options().verbose()) {
            System.out.println("in " + (System.currentTimeMillis() - startTime) + " ms");
         }

         var4 = u;
      } catch (Exception var14) {
         throw new Error("Error: Failed to load " + this.fullName + ".", var14);
      } finally {
         try {
            if (this.is != null) {
               this.is.close();
               this.is = null;
            }
         } catch (Exception var13) {
            throw new Error("Error: Failed to close input stream for " + this.fullName + ".", var13);
         }

      }

      return var4;
   }
}
