package com.github.javaparser.utils;

import com.github.javaparser.ParserConfiguration;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class ParserCollectionStrategy implements CollectionStrategy {
   private final ParserConfiguration parserConfiguration;

   public ParserCollectionStrategy() {
      this(new ParserConfiguration());
   }

   public ParserCollectionStrategy(ParserConfiguration parserConfiguration) {
      this.parserConfiguration = parserConfiguration;
   }

   public ProjectRoot collect(Path path) {
      final ProjectRoot projectRoot = new ProjectRoot(path, this.parserConfiguration);

      try {
         Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            Path current_root;
            PathMatcher javaMatcher = ParserCollectionStrategy.this.getPathMatcher("glob:**.java");

            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
               if (this.javaMatcher.matches(file)) {
                  this.current_root = (Path)ParserCollectionStrategy.this.getRoot(file).orElse((Object)null);
                  if (this.current_root != null) {
                     return FileVisitResult.SKIP_SIBLINGS;
                  }
               }

               return FileVisitResult.CONTINUE;
            }

            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
               return !Files.isHidden(dir) && (this.current_root == null || !dir.startsWith(this.current_root)) ? FileVisitResult.CONTINUE : FileVisitResult.SKIP_SUBTREE;
            }

            public FileVisitResult postVisitDirectory(Path dir, IOException e) {
               if (dir.equals(this.current_root)) {
                  projectRoot.addSourceRoot(dir);
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
