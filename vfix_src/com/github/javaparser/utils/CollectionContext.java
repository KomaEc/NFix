package com.github.javaparser.utils;

import java.nio.file.Path;

/** @deprecated */
@Deprecated
public class CollectionContext {
   private final CollectionStrategy strategy;

   public CollectionContext(CollectionStrategy strategy) {
      this.strategy = strategy;
   }

   public ProjectRoot collect(Path path) {
      return this.strategy.collect(path);
   }
}
