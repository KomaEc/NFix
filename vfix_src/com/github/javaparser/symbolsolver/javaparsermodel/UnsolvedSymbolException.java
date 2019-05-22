package com.github.javaparser.symbolsolver.javaparsermodel;

import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;

/** @deprecated */
@Deprecated
public class UnsolvedSymbolException extends RuntimeException {
   private String context;
   private String name;
   private TypeSolver typeSolver;

   /** @deprecated */
   @Deprecated
   public UnsolvedSymbolException(String name, TypeSolver typeSolver) {
      super("Unsolved symbol : " + name + " using typesolver " + typeSolver);
      this.typeSolver = typeSolver;
      this.name = name;
   }

   /** @deprecated */
   @Deprecated
   public UnsolvedSymbolException(Context context, String name) {
      super("Unsolved symbol in " + context + " : " + name);
      this.context = context.toString();
      this.name = name;
   }

   /** @deprecated */
   @Deprecated
   public UnsolvedSymbolException(String context, String name) {
      super("Unsolved symbol in " + context + " : " + name);
      this.context = context;
      this.name = name;
   }

   /** @deprecated */
   @Deprecated
   public UnsolvedSymbolException(String name) {
      super("Unsolved symbol : " + name);
      this.context = "unknown";
      this.name = name;
   }

   public String toString() {
      return "UnsolvedSymbolException{context='" + this.context + '\'' + ", name='" + this.name + '\'' + ", typeSolver=" + this.typeSolver + '}';
   }
}
