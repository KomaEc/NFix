package com.github.javaparser.resolution.declarations;

import java.util.List;
import java.util.Optional;

public interface ResolvedTypeParametrizable {
   List<ResolvedTypeParameterDeclaration> getTypeParameters();

   Optional<ResolvedTypeParameterDeclaration> findTypeParameter(String name);

   default boolean isGeneric() {
      return !this.getTypeParameters().isEmpty();
   }
}
