package com.github.javaparser.symbolsolver.model.resolution;

import com.github.javaparser.resolution.declarations.ResolvedDeclaration;
import java.util.Optional;

public class SymbolReference<S extends ResolvedDeclaration> {
   private Optional<? extends S> correspondingDeclaration;

   private SymbolReference(Optional<? extends S> correspondingDeclaration) {
      this.correspondingDeclaration = correspondingDeclaration;
   }

   public static <S extends ResolvedDeclaration, S2 extends S> SymbolReference<S> solved(S2 symbolDeclaration) {
      return new SymbolReference(Optional.of(symbolDeclaration));
   }

   public static <S extends ResolvedDeclaration, S2 extends S> SymbolReference<S> unsolved(Class<S2> clazz) {
      return new SymbolReference(Optional.empty());
   }

   public String toString() {
      return "SymbolReference{" + this.correspondingDeclaration + "}";
   }

   public S getCorrespondingDeclaration() {
      if (!this.isSolved()) {
         throw new UnsupportedOperationException("CorrespondingDeclaration not available for unsolved symbol.");
      } else {
         return (ResolvedDeclaration)this.correspondingDeclaration.get();
      }
   }

   public boolean isSolved() {
      return this.correspondingDeclaration.isPresent();
   }

   public static <O extends ResolvedDeclaration> SymbolReference<O> adapt(SymbolReference<? extends O> ref, Class<O> clazz) {
      return ref.isSolved() ? solved(ref.getCorrespondingDeclaration()) : unsolved(clazz);
   }
}
