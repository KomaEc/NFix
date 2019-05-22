package com.github.javaparser.symbolsolver.javaparsermodel.declarations;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParametrizable;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.logic.AbstractTypeDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JavaParserTypeParameter extends AbstractTypeDeclaration implements ResolvedTypeParameterDeclaration {
   private TypeParameter wrappedNode;
   private TypeSolver typeSolver;

   public JavaParserTypeParameter(TypeParameter wrappedNode, TypeSolver typeSolver) {
      this.wrappedNode = wrappedNode;
      this.typeSolver = typeSolver;
   }

   public Set<ResolvedMethodDeclaration> getDeclaredMethods() {
      return Collections.emptySet();
   }

   public SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> parameterTypes) {
      return this.getContext().solveMethod(name, parameterTypes, false, this.typeSolver);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof JavaParserTypeParameter)) {
         return false;
      } else {
         JavaParserTypeParameter that = (JavaParserTypeParameter)o;
         if (this.wrappedNode != null) {
            if (!this.wrappedNode.equals(that.wrappedNode)) {
               return false;
            }
         } else if (that.wrappedNode != null) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int result = this.wrappedNode != null ? this.wrappedNode.hashCode() : 0;
      result = 31 * result + (this.typeSolver != null ? this.typeSolver.hashCode() : 0);
      return result;
   }

   public String getName() {
      return this.wrappedNode.getName().getId();
   }

   public boolean isAssignableBy(ResolvedReferenceTypeDeclaration other) {
      return this.isAssignableBy((ResolvedType)(new ReferenceTypeImpl(other, this.typeSolver)));
   }

   public String getContainerQualifiedName() {
      ResolvedTypeParametrizable container = this.getContainer();
      if (container instanceof ResolvedReferenceTypeDeclaration) {
         return ((ResolvedReferenceTypeDeclaration)container).getQualifiedName();
      } else {
         return container instanceof JavaParserConstructorDeclaration ? ((JavaParserConstructorDeclaration)container).getQualifiedSignature() : ((JavaParserMethodDeclaration)container).getQualifiedSignature();
      }
   }

   public String getContainerId() {
      ResolvedTypeParametrizable container = this.getContainer();
      if (container instanceof ResolvedReferenceTypeDeclaration) {
         return ((ResolvedReferenceTypeDeclaration)container).getId();
      } else {
         return container instanceof JavaParserConstructorDeclaration ? ((JavaParserConstructorDeclaration)container).getQualifiedSignature() : ((JavaParserMethodDeclaration)container).getQualifiedSignature();
      }
   }

   public ResolvedTypeParametrizable getContainer() {
      Node parentNode = Navigator.requireParentNode(this.wrappedNode);
      if (parentNode instanceof ClassOrInterfaceDeclaration) {
         ClassOrInterfaceDeclaration jpTypeDeclaration = (ClassOrInterfaceDeclaration)parentNode;
         return JavaParserFacade.get(this.typeSolver).getTypeDeclaration(jpTypeDeclaration);
      } else if (parentNode instanceof ConstructorDeclaration) {
         ConstructorDeclaration jpConstructorDeclaration = (ConstructorDeclaration)parentNode;
         Optional<ClassOrInterfaceDeclaration> jpTypeDeclaration = jpConstructorDeclaration.findAncestor(ClassOrInterfaceDeclaration.class);
         if (jpTypeDeclaration.isPresent()) {
            ResolvedReferenceTypeDeclaration typeDeclaration = JavaParserFacade.get(this.typeSolver).getTypeDeclaration((ClassOrInterfaceDeclaration)jpTypeDeclaration.get());
            if (typeDeclaration.isClass()) {
               return new JavaParserConstructorDeclaration(typeDeclaration.asClass(), jpConstructorDeclaration, this.typeSolver);
            }
         }

         throw new UnsupportedOperationException();
      } else {
         MethodDeclaration jpMethodDeclaration = (MethodDeclaration)parentNode;
         return new JavaParserMethodDeclaration(jpMethodDeclaration, this.typeSolver);
      }
   }

   public String getQualifiedName() {
      return String.format("%s.%s", this.getContainerQualifiedName(), this.getName());
   }

   public List<ResolvedTypeParameterDeclaration.Bound> getBounds() {
      return (List)this.wrappedNode.getTypeBound().stream().map((astB) -> {
         return this.toBound(astB, this.typeSolver);
      }).collect(Collectors.toList());
   }

   private ResolvedTypeParameterDeclaration.Bound toBound(ClassOrInterfaceType classOrInterfaceType, TypeSolver typeSolver) {
      ResolvedType type = JavaParserFacade.get(typeSolver).convertToUsage(classOrInterfaceType, (Node)classOrInterfaceType);
      return ResolvedTypeParameterDeclaration.Bound.extendsBound(type);
   }

   public Context getContext() {
      throw new UnsupportedOperationException();
   }

   public ResolvedType getUsage(Node node) {
      throw new UnsupportedOperationException();
   }

   public boolean isAssignableBy(ResolvedType type) {
      throw new UnsupportedOperationException();
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

   public boolean isTypeParameter() {
      return true;
   }

   public boolean hasDirectlyAnnotation(String canonicalName) {
      throw new UnsupportedOperationException();
   }

   public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
      return Collections.emptyList();
   }

   public TypeParameter getWrappedNode() {
      return this.wrappedNode;
   }

   public String toString() {
      return "JPTypeParameter(" + this.wrappedNode.getName() + ", bounds=" + this.wrappedNode.getTypeBound() + ")";
   }

   public Optional<ResolvedReferenceTypeDeclaration> containerType() {
      ResolvedTypeParametrizable container = this.getContainer();
      return container instanceof ResolvedReferenceTypeDeclaration ? Optional.of((ResolvedReferenceTypeDeclaration)container) : Optional.empty();
   }

   public List<ResolvedConstructorDeclaration> getConstructors() {
      return Collections.emptyList();
   }
}
