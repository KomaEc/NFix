package com.github.javaparser.symbolsolver.resolution.typesolvers;

import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class AarTypeSolver implements TypeSolver {
   private TypeSolver parent;
   private JarTypeSolver delegate;

   public AarTypeSolver(String aarFile) throws IOException {
      this(new File(aarFile));
   }

   public AarTypeSolver(Path aarFile) throws IOException {
      this(aarFile.toFile());
   }

   public AarTypeSolver(File aarFile) throws IOException {
      JarFile jarFile = new JarFile(aarFile);
      ZipEntry classesJarEntry = jarFile.getEntry("classes.jar");
      if (classesJarEntry == null) {
         throw new IllegalArgumentException(String.format("The given file (%s) is malformed: entry classes.jar was not found", aarFile.getAbsolutePath()));
      } else {
         this.delegate = new JarTypeSolver(jarFile.getInputStream(classesJarEntry));
      }
   }

   public TypeSolver getParent() {
      return this.parent;
   }

   public void setParent(TypeSolver parent) {
      this.parent = parent;
   }

   public SymbolReference<ResolvedReferenceTypeDeclaration> tryToSolveType(String name) {
      return this.delegate.tryToSolveType(name);
   }
}
