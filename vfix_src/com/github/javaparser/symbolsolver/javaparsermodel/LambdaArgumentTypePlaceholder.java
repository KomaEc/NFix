package com.github.javaparser.symbolsolver.javaparsermodel;

import com.github.javaparser.resolution.declarations.ResolvedMethodLikeDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;

public class LambdaArgumentTypePlaceholder implements ResolvedType {
   private int pos;
   private SymbolReference<? extends ResolvedMethodLikeDeclaration> method;

   public LambdaArgumentTypePlaceholder(int pos) {
      this.pos = pos;
   }

   public boolean isArray() {
      return false;
   }

   public boolean isPrimitive() {
      return false;
   }

   public boolean isReferenceType() {
      return false;
   }

   public String describe() {
      throw new UnsupportedOperationException();
   }

   public boolean isTypeVariable() {
      return false;
   }

   public void setMethod(SymbolReference<? extends ResolvedMethodLikeDeclaration> method) {
      this.method = method;
   }

   public boolean isAssignableBy(ResolvedType other) {
      throw new UnsupportedOperationException();
   }
}
