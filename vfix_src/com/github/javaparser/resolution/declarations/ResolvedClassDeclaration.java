package com.github.javaparser.resolution.declarations;

import com.github.javaparser.ast.Node;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import java.util.List;

public interface ResolvedClassDeclaration extends ResolvedReferenceTypeDeclaration, ResolvedTypeParametrizable, HasAccessSpecifier, AssociableToAST<Node> {
   default boolean isClass() {
      return true;
   }

   ResolvedReferenceType getSuperClass();

   List<ResolvedReferenceType> getInterfaces();

   List<ResolvedReferenceType> getAllSuperClasses();

   List<ResolvedReferenceType> getAllInterfaces();

   List<ResolvedConstructorDeclaration> getConstructors();
}
