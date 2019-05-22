package com.github.javaparser.symbolsolver.javaparsermodel.declarations;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedAnnotationDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedAnnotationMemberDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.logic.AbstractTypeDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JavaParserAnnotationDeclaration extends AbstractTypeDeclaration implements ResolvedAnnotationDeclaration {
   private AnnotationDeclaration wrappedNode;
   private TypeSolver typeSolver;

   public JavaParserAnnotationDeclaration(AnnotationDeclaration wrappedNode, TypeSolver typeSolver) {
      this.wrappedNode = wrappedNode;
      this.typeSolver = typeSolver;
   }

   public List<ResolvedReferenceType> getAncestors(boolean acceptIncompleteList) {
      throw new UnsupportedOperationException();
   }

   public List<ResolvedFieldDeclaration> getAllFields() {
      throw new UnsupportedOperationException();
   }

   public Set<ResolvedMethodDeclaration> getDeclaredMethods() {
      throw new UnsupportedOperationException();
   }

   public boolean isAssignableBy(ResolvedType type) {
      throw new UnsupportedOperationException();
   }

   public boolean isAssignableBy(ResolvedReferenceTypeDeclaration other) {
      throw new UnsupportedOperationException();
   }

   public boolean hasDirectlyAnnotation(String canonicalName) {
      return AstResolutionUtils.hasDirectlyAnnotation(this.wrappedNode, this.typeSolver, canonicalName);
   }

   public String getPackageName() {
      return AstResolutionUtils.getPackageName(this.wrappedNode);
   }

   public String getClassName() {
      return AstResolutionUtils.getClassName("", this.wrappedNode);
   }

   public String getQualifiedName() {
      String containerName = AstResolutionUtils.containerName((Node)this.wrappedNode.getParentNode().orElse((Object)null));
      return containerName.isEmpty() ? this.wrappedNode.getName().getId() : containerName + "." + this.wrappedNode.getName();
   }

   public String getName() {
      return this.wrappedNode.getName().getId();
   }

   public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
      throw new UnsupportedOperationException();
   }

   public Optional<ResolvedReferenceTypeDeclaration> containerType() {
      throw new UnsupportedOperationException("containerType is not supported for " + this.getClass().getCanonicalName());
   }

   public List<ResolvedAnnotationMemberDeclaration> getAnnotationMembers() {
      return (List)this.wrappedNode.getMembers().stream().filter((m) -> {
         return m instanceof AnnotationMemberDeclaration;
      }).map((m) -> {
         return new JavaParserAnnotationMemberDeclaration((AnnotationMemberDeclaration)m, this.typeSolver);
      }).collect(Collectors.toList());
   }

   public List<ResolvedConstructorDeclaration> getConstructors() {
      return Collections.emptyList();
   }

   public Optional<AnnotationDeclaration> toAst() {
      return Optional.of(this.wrappedNode);
   }
}
