package com.github.javaparser.symbolsolver.resolution.typesolvers;

import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.javassistmodel.JavassistFactory;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class JarTypeSolver implements TypeSolver {
   private static JarTypeSolver instance;
   private TypeSolver parent;
   private Map<String, JarTypeSolver.ClasspathElement> classpathElements;
   private ClassPool classPool;

   public JarTypeSolver(Path pathToJar) throws IOException {
      this(pathToJar.toFile());
   }

   public JarTypeSolver(File pathToJar) throws IOException {
      this(pathToJar.getCanonicalPath());
   }

   public JarTypeSolver(String pathToJar) throws IOException {
      this.classpathElements = new HashMap();
      this.classPool = new ClassPool(false);
      this.addPathToJar(pathToJar);
   }

   public JarTypeSolver(InputStream jarInputStream) throws IOException {
      this.classpathElements = new HashMap();
      this.classPool = new ClassPool(false);
      this.addPathToJar(jarInputStream);
   }

   public static JarTypeSolver getJarTypeSolver(String pathToJar) throws IOException {
      if (instance == null) {
         instance = new JarTypeSolver(pathToJar);
      } else {
         instance.addPathToJar(pathToJar);
      }

      return instance;
   }

   private File dumpToTempFile(InputStream inputStream) throws IOException {
      File tempFile = File.createTempFile("jar_file_from_input_stream", ".jar");
      tempFile.deleteOnExit();
      byte[] buffer = new byte[8192];

      try {
         OutputStream output = new FileOutputStream(tempFile);
         Throwable var5 = null;

         try {
            int bytesRead;
            try {
               while((bytesRead = inputStream.read(buffer)) != -1) {
                  output.write(buffer, 0, bytesRead);
               }
            } catch (Throwable var21) {
               var5 = var21;
               throw var21;
            }
         } finally {
            if (output != null) {
               if (var5 != null) {
                  try {
                     output.close();
                  } catch (Throwable var20) {
                     var5.addSuppressed(var20);
                  }
               } else {
                  output.close();
               }
            }

         }
      } finally {
         inputStream.close();
      }

      return tempFile;
   }

   private void addPathToJar(InputStream jarInputStream) throws IOException {
      this.addPathToJar(this.dumpToTempFile(jarInputStream).getAbsolutePath());
   }

   private void addPathToJar(String pathToJar) throws IOException {
      try {
         this.classPool.appendClassPath(pathToJar);
         this.classPool.appendSystemPath();
      } catch (NotFoundException var6) {
         throw new RuntimeException(var6);
      }

      JarFile jarFile = new JarFile(pathToJar);
      Enumeration e = jarFile.entries();

      while(e.hasMoreElements()) {
         JarEntry entry = (JarEntry)e.nextElement();
         if (entry != null && !entry.isDirectory() && entry.getName().endsWith(".class")) {
            String name = this.entryPathToClassName(entry.getName());
            this.classpathElements.put(name, new JarTypeSolver.ClasspathElement(jarFile, entry));
         }
      }

   }

   public TypeSolver getParent() {
      return this.parent;
   }

   public void setParent(TypeSolver parent) {
      this.parent = parent;
   }

   private String entryPathToClassName(String entryPath) {
      if (!entryPath.endsWith(".class")) {
         throw new IllegalStateException();
      } else {
         String className = entryPath.substring(0, entryPath.length() - ".class".length());
         className = className.replace('/', '.');
         className = className.replace('$', '.');
         return className;
      }
   }

   public SymbolReference<ResolvedReferenceTypeDeclaration> tryToSolveType(String name) {
      try {
         return this.classpathElements.containsKey(name) ? SymbolReference.solved(JavassistFactory.toTypeDeclaration(((JarTypeSolver.ClasspathElement)this.classpathElements.get(name)).toCtClass(), this.getRoot())) : SymbolReference.unsolved(ResolvedReferenceTypeDeclaration.class);
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public ResolvedReferenceTypeDeclaration solveType(String name) throws UnsolvedSymbolException {
      SymbolReference<ResolvedReferenceTypeDeclaration> ref = this.tryToSolveType(name);
      if (ref.isSolved()) {
         return (ResolvedReferenceTypeDeclaration)ref.getCorrespondingDeclaration();
      } else {
         throw new UnsolvedSymbolException(name);
      }
   }

   private class ClasspathElement {
      private JarFile jarFile;
      private JarEntry entry;

      ClasspathElement(JarFile jarFile, JarEntry entry) {
         this.jarFile = jarFile;
         this.entry = entry;
      }

      CtClass toCtClass() throws IOException {
         InputStream is = this.jarFile.getInputStream(this.entry);
         Throwable var2 = null;

         CtClass var3;
         try {
            var3 = JarTypeSolver.this.classPool.makeClass(is);
         } catch (Throwable var12) {
            var2 = var12;
            throw var12;
         } finally {
            if (is != null) {
               if (var2 != null) {
                  try {
                     is.close();
                  } catch (Throwable var11) {
                     var2.addSuppressed(var11);
                  }
               } else {
                  is.close();
               }
            }

         }

         return var3;
      }
   }
}
