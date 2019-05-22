package com.github.javaparser.symbolsolver.javassistmodel;

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
import javassist.CtClass;

public class JavassistAnnotationDeclaration extends AbstractTypeDeclaration implements ResolvedAnnotationDeclaration {
   private CtClass ctClass;
   private TypeSolver typeSolver;
   private JavassistTypeDeclarationAdapter javassistTypeDeclarationAdapter;

   public String toString() {
      return this.getClass().getSimpleName() + "{ctClass=" + this.ctClass.getName() + ", typeSolver=" + this.typeSolver + '}';
   }

   public JavassistAnnotationDeclaration(CtClass ctClass, TypeSolver typeSolver) {
      if (!ctClass.isAnnotation()) {
         throw new IllegalArgumentException("Not an annotation: " + ctClass.getName());
      } else {
         this.ctClass = ctClass;
         this.typeSolver = typeSolver;
         this.javassistTypeDeclarationAdapter = new JavassistTypeDeclarationAdapter(ctClass, typeSolver);
      }
   }

   public String getPackageName() {
      return this.ctClass.getPackageName();
   }

   public String getClassName() {
      String qualifiedName = this.getQualifiedName();
      return qualifiedName.contains(".") ? qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1, qualifiedName.length()) : qualifiedName;
   }

   public String getQualifiedName() {
      return this.ctClass.getName().replace('$', '.');
   }

   public boolean isAssignableBy(ResolvedType type) {
      throw new UnsupportedOperationException();
   }

   public List<ResolvedFieldDeclaration> getAllFields() {
      throw new UnsupportedOperationException();
   }

   public boolean isAssignableBy(ResolvedReferenceTypeDeclaration other) {
      throw new UnsupportedOperationException();
   }

   public List<ResolvedReferenceType> getAncestors(boolean acceptIncompleteList) {
      throw new UnsupportedOperationException();
   }

   public Set<ResolvedMethodDeclaration> getDeclaredMethods() {
      throw new UnsupportedOperationException();
   }

   public boolean hasDirectlyAnnotation(String canonicalName) {
      return this.ctClass.hasAnnotation(canonicalName);
   }

   public String getName() {
      return this.getClassName();
   }

   public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
      throw new UnsupportedOperationException();
   }

   public Optional<ResolvedReferenceTypeDeclaration> containerType() {
      throw new UnsupportedOperationException("containerType() is not supported for " + this.getClass().getCanonicalName());
   }

   public List<ResolvedConstructorDeclaration> getConstructors() {
      return Collections.emptyList();
   }

   public List<ResolvedAnnotationMemberDeclaration> getAnnotationMembers() {
      return (List)Stream.of(this.ctClass.getDeclaredMethods()).map((m) -> {
         return new JavassistAnnotationMemberDeclaration(m, this.typeSolver);
      }).collect(Collectors.toList());
   }

   public Optional<AnnotationDeclaration> toAst() {
      return Optional.empty();
   }
}
