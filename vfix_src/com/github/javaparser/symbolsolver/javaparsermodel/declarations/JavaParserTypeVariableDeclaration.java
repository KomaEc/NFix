package com.github.javaparser.symbolsolver.javaparsermodel.declarations;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.logic.AbstractTypeDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class JavaParserTypeVariableDeclaration extends AbstractTypeDeclaration {
   private TypeParameter wrappedNode;
   private TypeSolver typeSolver;

   public JavaParserTypeVariableDeclaration(TypeParameter wrappedNode, TypeSolver typeSolver) {
      this.wrappedNode = wrappedNode;
      this.typeSolver = typeSolver;
   }

   public boolean isAssignableBy(ResolvedReferenceTypeDeclaration other) {
      return this.isAssignableBy((ResolvedType)(new ReferenceTypeImpl(other, this.typeSolver)));
   }

   public String getPackageName() {
      return AstResolutionUtils.getPackageName(this.wrappedNode);
   }

   public String getClassName() {
      return AstResolutionUtils.getClassName("", this.wrappedNode);
   }

   public String getQualifiedName() {
      return this.getName();
   }

   public Context getContext() {
      throw new UnsupportedOperationException();
   }

   public String toString() {
      return "JavaParserTypeVariableDeclaration{" + this.wrappedNode.getName() + '}';
   }

   public SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> parameterTypes) {
      throw new UnsupportedOperationException();
   }

   public ResolvedType getUsage(Node node) {
      throw new UnsupportedOperationException();
   }

   public boolean isAssignableBy(ResolvedType type) {
      if (type.isTypeVariable()) {
         throw new UnsupportedOperationException("Is this type variable declaration assignable by " + type.describe());
      } else {
         throw new UnsupportedOperationException("Is this type variable declaration assignable by " + type);
      }
   }

   public boolean isTypeParameter() {
      return true;
   }

   public ResolvedFieldDeclaration getField(String name) {
      throw new UnsupportedOperationException();
   }

   public boolean hasField(String name) {
      return false;
   }

   public List<ResolvedFieldDeclaration> getAllFields() {
      return new ArrayList();
   }

   public List<ResolvedReferenceType> getAncestors(boolean acceptIncompleteList) {
      throw new UnsupportedOperationException();
   }

   public Set<ResolvedMethodDeclaration> getDeclaredMethods() {
      return Collections.emptySet();
   }

   public String getName() {
      return this.wrappedNode.getName().getId();
   }

   public boolean isField() {
      throw new UnsupportedOperationException();
   }

   public boolean isParameter() {
      throw new UnsupportedOperationException();
   }

   public boolean isType() {
      return true;
   }

   public boolean hasDirectlyAnnotation(String canonicalName) {
      throw new UnsupportedOperationException();
   }

   public boolean isClass() {
      return false;
   }

   public boolean isInterface() {
      return false;
   }

   public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
      return Collections.emptyList();
   }

   public ResolvedTypeParameterDeclaration asTypeParameter() {
      return new JavaParserTypeParameter(this.wrappedNode, this.typeSolver);
   }

   public TypeParameter getWrappedNode() {
      return this.wrappedNode;
   }

   public Optional<ResolvedReferenceTypeDeclaration> containerType() {
      return this.asTypeParameter().containerType();
   }

   public List<ResolvedConstructorDeclaration> getConstructors() {
      return Collections.emptyList();
   }
}
