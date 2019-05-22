package com.github.javaparser.symbolsolver.utils;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.CollectionStrategy;
import com.github.javaparser.utils.Log;
import com.github.javaparser.utils.ProjectRoot;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class SymbolSolverCollectionStrategy implements CollectionStrategy {
   private final ParserConfiguration parserConfiguration;
   private final CombinedTypeSolver typeSolver;

   public SymbolSolverCollectionStrategy() {
      this(new ParserConfiguration());
   }

   public SymbolSolverCollectionStrategy(ParserConfiguration parserConfiguration) {
      this.typeSolver = new CombinedTypeSolver(new TypeSolver[]{new ReflectionTypeSolver(false)});
      this.parserConfiguration = parserConfiguration.setSymbolResolver(new JavaSymbolSolver(this.typeSolver));
   }

   public ProjectRoot collect(Path path) {
      final ProjectRoot projectRoot = new ProjectRoot(path, this.parserConfiguration);

      try {
         Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            private Path current_root;
            private PathMatcher javaMatcher = SymbolSolverCollectionStrategy.this.getPathMatcher("glob:**.java");
            private PathMatcher jarMatcher = SymbolSolverCollectionStrategy.this.getPathMatcher("glob:**.jar");

            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
               if (this.javaMatcher.matches(file)) {
                  if (this.current_root == null || !file.startsWith(this.current_root)) {
                     this.current_root = (Path)SymbolSolverCollectionStrategy.this.getRoot(file).orElse((Object)null);
                  }
               } else if (this.jarMatcher.matches(file)) {
                  SymbolSolverCollectionStrategy.this.typeSolver.add(new JarTypeSolver(file.toString()));
               }

               return FileVisitResult.CONTINUE;
            }

            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
               return Files.isHidden(dir) ? FileVisitResult.SKIP_SUBTREE : FileVisitResult.CONTINUE;
            }

            public FileVisitResult postVisitDirectory(Path dir, IOException e) {
               if (dir.equals(this.current_root)) {
                  projectRoot.addSourceRoot(dir);
                  SymbolSolverCollectionStrategy.this.typeSolver.add(new JavaParserTypeSolver(this.current_root.toFile()));
                  this.current_root = null;
               }

               return FileVisitResult.CONTINUE;
            }
         });
      } catch (IOException var4) {
         Log.error(var4, "Unable to walk %s", path);
      }

      return projectRoot;
   }
}
