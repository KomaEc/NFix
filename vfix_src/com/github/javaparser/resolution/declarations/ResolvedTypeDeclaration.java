package com.github.javaparser.resolution.declarations;

import com.github.javaparser.resolution.UnsolvedSymbolException;
import java.util.Optional;
import java.util.Set;

public interface ResolvedTypeDeclaration extends ResolvedDeclaration {
   default Set<ResolvedReferenceTypeDeclaration> internalTypes() {
      throw new UnsupportedOperationException("InternalTypes not available for " + this.getClass().getCanonicalName());
   }

   default ResolvedReferenceTypeDeclaration getInternalType(String name) {
      Optional<ResolvedReferenceTypeDeclaration> type = this.internalTypes().stream().filter((f) -> {
         return f.getName().equals(name);
      }).findFirst();
      return (ResolvedReferenceTypeDeclaration)type.orElseThrow(() -> {
         return new UnsolvedSymbolException("Internal type not found: " + name);
      });
   }

   default boolean hasInternalType(String name) {
      return this.internalTypes().stream().anyMatch((f) -> {
         return f.getName().equals(name);
      });
   }

   Optional<ResolvedReferenceTypeDeclaration> containerType();

   default boolean isClass() {
      return false;
   }

   default boolean isInterface() {
      return false;
   }

   default boolean isEnum() {
      return false;
   }

   default boolean isTypeParameter() {
      return false;
   }

   default boolean isType() {
      return true;
   }

   default boolean isAnonymousClass() {
      return false;
   }

   default ResolvedTypeDeclaration asType() {
      return this;
   }

   default ResolvedClassDeclaration asClass() {
      throw new UnsupportedOperationException(String.format("%s is not a class", this));
   }

   default ResolvedInterfaceDeclaration asInterface() {
      throw new UnsupportedOperationException(String.format("%s is not an interface", this));
   }

   default ResolvedEnumDeclaration asEnum() {
      throw new UnsupportedOperationException(String.format("%s is not an enum", this));
   }

   default ResolvedTypeParameterDeclaration asTypeParameter() {
      throw new UnsupportedOperationException(String.format("%s is not a type parameter", this));
   }

   default ResolvedReferenceTypeDeclaration asReferenceType() {
      throw new UnsupportedOperationException(String.format("%s is not a reference type", this));
   }

   String getPackageName();

   String getClassName();

   String getQualifiedName();

   default String getId() {
      String qname = this.getQualifiedName();
      return qname == null ? String.format("<localClass>:%s", this.getName()) : qname;
   }
}
