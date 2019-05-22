package com.github.javaparser.symbolsolver.javaparsermodel.declarations;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DefaultConstructorDeclaration<N extends ResolvedReferenceTypeDeclaration> implements ResolvedConstructorDeclaration {
   private N declaringType;

   DefaultConstructorDeclaration(N declaringType) {
      this.declaringType = declaringType;
   }

   public N declaringType() {
      return this.declaringType;
   }

   public int getNumberOfParams() {
      return 0;
   }

   public ResolvedParameterDeclaration getParam(int i) {
      throw new UnsupportedOperationException("The default constructor has not parameters");
   }

   public String getName() {
      return this.declaringType.getName();
   }

   public AccessSpecifier accessSpecifier() {
      return AccessSpecifier.PUBLIC;
   }

   public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
      return Collections.emptyList();
   }

   public int getNumberOfSpecifiedExceptions() {
      return 0;
   }

   public ResolvedType getSpecifiedException(int index) {
      throw new UnsupportedOperationException("The default constructor does not throw exceptions");
   }

   public Optional<ConstructorDeclaration> toAst() {
      return Optional.empty();
   }
}
