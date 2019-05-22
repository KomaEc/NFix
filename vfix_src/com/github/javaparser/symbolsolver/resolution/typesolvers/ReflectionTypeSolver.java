package com.github.javaparser.symbolsolver.resolution.typesolvers;

public class ReflectionTypeSolver extends ClassLoaderTypeSolver {
   private final boolean jreOnly;

   public ReflectionTypeSolver(boolean jreOnly) {
      super(ReflectionTypeSolver.class.getClassLoader());
      this.jreOnly = jreOnly;
   }

   public ReflectionTypeSolver() {
      this(true);
   }

   protected boolean filterName(String name) {
      return !this.jreOnly || name.startsWith("java.") || name.startsWith("javax.");
   }
}
