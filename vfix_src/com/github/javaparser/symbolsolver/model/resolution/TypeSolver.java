package com.github.javaparser.symbolsolver.model.resolution;

import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

public interface TypeSolver {
   default TypeSolver getRoot() {
      return this.getParent() == null ? this : this.getParent().getRoot();
   }

   TypeSolver getParent();

   void setParent(TypeSolver var1);

   SymbolReference<ResolvedReferenceTypeDeclaration> tryToSolveType(String var1);

   default ResolvedReferenceTypeDeclaration solveType(String name) throws UnsolvedSymbolException {
      SymbolReference<ResolvedReferenceTypeDeclaration> ref = this.tryToSolveType(name);
      if (ref.isSolved()) {
         return (ResolvedReferenceTypeDeclaration)ref.getCorrespondingDeclaration();
      } else {
         throw new UnsolvedSymbolException(name, this.toString());
      }
   }

   default boolean hasType(String name) {
      return this.tryToSolveType(name).isSolved();
   }
}
