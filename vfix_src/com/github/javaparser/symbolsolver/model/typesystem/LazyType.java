package com.github.javaparser.symbolsolver.model.typesystem;

import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedArrayType;
import com.github.javaparser.resolution.types.ResolvedPrimitiveType;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedTypeVariable;
import com.github.javaparser.resolution.types.ResolvedWildcard;
import java.util.Map;
import java.util.function.Function;

public class LazyType implements ResolvedType {
   private ResolvedType concrete;
   private Function<Void, ResolvedType> provider;

   public LazyType(Function<Void, ResolvedType> provider) {
      this.provider = provider;
   }

   private ResolvedType getType() {
      if (this.concrete == null) {
         this.concrete = (ResolvedType)this.provider.apply((Object)null);
      }

      return this.concrete;
   }

   public boolean isArray() {
      return this.getType().isArray();
   }

   public int arrayLevel() {
      return this.getType().arrayLevel();
   }

   public boolean isPrimitive() {
      return this.getType().isPrimitive();
   }

   public boolean isNull() {
      return this.getType().isNull();
   }

   public boolean isReference() {
      return this.getType().isReference();
   }

   public boolean isReferenceType() {
      return this.getType().isReferenceType();
   }

   public boolean isVoid() {
      return this.getType().isVoid();
   }

   public boolean isTypeVariable() {
      return this.getType().isTypeVariable();
   }

   public boolean isWildcard() {
      return this.getType().isWildcard();
   }

   public ResolvedArrayType asArrayType() {
      return this.getType().asArrayType();
   }

   public ResolvedReferenceType asReferenceType() {
      return this.getType().asReferenceType();
   }

   public ResolvedTypeParameterDeclaration asTypeParameter() {
      return this.getType().asTypeParameter();
   }

   public ResolvedTypeVariable asTypeVariable() {
      return this.getType().asTypeVariable();
   }

   public ResolvedPrimitiveType asPrimitive() {
      return this.getType().asPrimitive();
   }

   public ResolvedWildcard asWildcard() {
      return this.getType().asWildcard();
   }

   public String describe() {
      return this.getType().describe();
   }

   public ResolvedType replaceTypeVariables(ResolvedTypeParameterDeclaration tp, ResolvedType replaced, Map<ResolvedTypeParameterDeclaration, ResolvedType> inferredTypes) {
      return this.getType().replaceTypeVariables(tp, replaced, inferredTypes);
   }

   public ResolvedType replaceTypeVariables(ResolvedTypeParameterDeclaration tp, ResolvedType replaced) {
      return this.getType().replaceTypeVariables(tp, replaced);
   }

   public boolean isAssignableBy(ResolvedType other) {
      return this.getType().isAssignableBy(other);
   }
}
