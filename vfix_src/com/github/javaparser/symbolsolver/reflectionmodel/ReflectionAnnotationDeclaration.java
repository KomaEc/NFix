package com.github.javaparser.symbolsolver.reflectionmodel;

import com.github.javaparser.ast.body.AnnotationDeclaration;
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
import java.util.stream.Stream;

public class ReflectionAnnotationDeclaration extends AbstractTypeDeclaration implements ResolvedAnnotationDeclaration {
   private Class<?> clazz;
   private TypeSolver typeSolver;
   private ReflectionClassAdapter reflectionClassAdapter;

   public ReflectionAnnotationDeclaration(Class<?> clazz, TypeSolver typeSolver) {
      if (!clazz.isAnnotation()) {
         throw new IllegalArgumentException("The given type is not an annotation.");
      } else {
         this.clazz = clazz;
         this.typeSolver = typeSolver;
         this.reflectionClassAdapter = new ReflectionClassAdapter(clazz, typeSolver, this);
      }
   }

   public String getPackageName() {
      return this.clazz.getPackage() != null ? this.clazz.getPackage().getName() : "";
   }

   public String getClassName() {
      String qualifiedName = this.getQualifiedName();
      return qualifiedName.contains(".") ? qualifiedName.substring(qualifiedName.lastIndexOf("."), qualifiedName.length()) : qualifiedName;
   }

   public String getQualifiedName() {
      return this.clazz.getCanonicalName();
   }

   public String toString() {
      return this.getClass().getSimpleName() + "{clazz=" + this.clazz.getCanonicalName() + '}';
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof ReflectionAnnotationDeclaration)) {
         return false;
      } else {
         ReflectionAnnotationDeclaration that = (ReflectionAnnotationDeclaration)o;
         return this.clazz.getCanonicalName().equals(that.clazz.getCanonicalName());
      }
   }

   public int hashCode() {
      return this.clazz.getCanonicalName().hashCode();
   }

   public boolean isAssignableBy(ResolvedType type) {
      throw new UnsupportedOperationException();
   }

   public boolean isAssignableBy(ResolvedReferenceTypeDeclaration other) {
      throw new UnsupportedOperationException();
   }

   public boolean hasDirectlyAnnotation(String canonicalName) {
      return this.reflectionClassAdapter.hasDirectlyAnnotation(canonicalName);
   }

   public List<ResolvedFieldDeclaration> getAllFields() {
      throw new UnsupportedOperationException();
   }

   public List<ResolvedReferenceType> getAncestors(boolean acceptIncompleteList) {
      throw new UnsupportedOperationException();
   }

   public Set<ResolvedMethodDeclaration> getDeclaredMethods() {
      throw new UnsupportedOperationException();
   }

   public String getName() {
      return this.clazz.getSimpleName();
   }

   public Optional<ResolvedReferenceTypeDeclaration> containerType() {
      throw new UnsupportedOperationException("containerType() is not supported for " + this.getClass().getCanonicalName());
   }

   public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
      throw new UnsupportedOperationException();
   }

   public List<ResolvedConstructorDeclaration> getConstructors() {
      return Collections.emptyList();
   }

   public List<ResolvedAnnotationMemberDeclaration> getAnnotationMembers() {
      return (List)Stream.of(this.clazz.getDeclaredMethods()).map((m) -> {
         return new ReflectionAnnotationMemberDeclaration(m, this.typeSolver);
      }).collect(Collectors.toList());
   }

   public Optional<AnnotationDeclaration> toAst() {
      return Optional.empty();
   }
}
