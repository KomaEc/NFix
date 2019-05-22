package com.github.javaparser.symbolsolver.javaparsermodel.declarations;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFactory;
import com.github.javaparser.symbolsolver.logic.AbstractClassDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaParserAnonymousClassDeclaration extends AbstractClassDeclaration {
   private final TypeSolver typeSolver;
   private final ObjectCreationExpr wrappedNode;
   private final ResolvedTypeDeclaration superTypeDeclaration;
   private final String name = "Anonymous-" + UUID.randomUUID();

   public JavaParserAnonymousClassDeclaration(ObjectCreationExpr wrappedNode, TypeSolver typeSolver) {
      this.typeSolver = typeSolver;
      this.wrappedNode = wrappedNode;
      this.superTypeDeclaration = (ResolvedTypeDeclaration)JavaParserFactory.getContext((Node)wrappedNode.getParentNode().get(), typeSolver).solveType(wrappedNode.getType().getName().getId(), typeSolver).getCorrespondingDeclaration();
   }

   public ResolvedTypeDeclaration getSuperTypeDeclaration() {
      return this.superTypeDeclaration;
   }

   public <T extends Node> List<T> findMembersOfKind(Class<T> memberClass) {
      if (this.wrappedNode.getAnonymousClassBody().isPresent()) {
         Stream var10000 = ((NodeList)this.wrappedNode.getAnonymousClassBody().get()).stream().filter((node) -> {
            return memberClass.isAssignableFrom(node.getClass());
         });
         memberClass.getClass();
         return (List)var10000.map(memberClass::cast).collect(Collectors.toList());
      } else {
         return Collections.emptyList();
      }
   }

   public Context getContext() {
      return JavaParserFactory.getContext(this.wrappedNode, this.typeSolver);
   }

   protected ResolvedReferenceType object() {
      return new ReferenceTypeImpl(this.typeSolver.solveType(Object.class.getCanonicalName()), this.typeSolver);
   }

   public ResolvedReferenceType getSuperClass() {
      ResolvedReferenceTypeDeclaration superRRTD = this.superTypeDeclaration.asReferenceType();
      if (superRRTD == null) {
         throw new RuntimeException("The super ResolvedReferenceTypeDeclaration is not expected to be null");
      } else {
         return new ReferenceTypeImpl(superRRTD, this.typeSolver);
      }
   }

   public List<ResolvedReferenceType> getInterfaces() {
      return (List)this.superTypeDeclaration.asReferenceType().getAncestors().stream().filter((type) -> {
         return type.getTypeDeclaration().isInterface();
      }).collect(Collectors.toList());
   }

   public List<ResolvedConstructorDeclaration> getConstructors() {
      return (List)this.findMembersOfKind(ConstructorDeclaration.class).stream().map((ctor) -> {
         return new JavaParserConstructorDeclaration(this, ctor, this.typeSolver);
      }).collect(Collectors.toList());
   }

   public AccessSpecifier accessSpecifier() {
      return AccessSpecifier.PRIVATE;
   }

   public List<ResolvedReferenceType> getAncestors(boolean acceptIncompleteList) {
      return ImmutableList.builder().add((Object)this.getSuperClass()).addAll((Iterable)this.superTypeDeclaration.asReferenceType().getAncestors(acceptIncompleteList)).build();
   }

   public List<ResolvedFieldDeclaration> getAllFields() {
      List<JavaParserFieldDeclaration> myFields = (List)this.findMembersOfKind(FieldDeclaration.class).stream().flatMap((field) -> {
         return field.getVariables().stream().map((variable) -> {
            return new JavaParserFieldDeclaration(variable, this.typeSolver);
         });
      }).collect(Collectors.toList());
      List<ResolvedFieldDeclaration> superClassFields = this.getSuperClass().getTypeDeclaration().getAllFields();
      List<ResolvedFieldDeclaration> interfaceFields = (List)this.getInterfaces().stream().flatMap((inteface) -> {
         return inteface.getTypeDeclaration().getAllFields().stream();
      }).collect(Collectors.toList());
      return ImmutableList.builder().addAll((Iterable)myFields).addAll((Iterable)superClassFields).addAll((Iterable)interfaceFields).build();
   }

   public Set<ResolvedMethodDeclaration> getDeclaredMethods() {
      return (Set)this.findMembersOfKind(MethodDeclaration.class).stream().map((method) -> {
         return new JavaParserMethodDeclaration(method, this.typeSolver);
      }).collect(Collectors.toSet());
   }

   public boolean isAssignableBy(ResolvedType type) {
      return false;
   }

   public boolean isAssignableBy(ResolvedReferenceTypeDeclaration other) {
      return false;
   }

   public boolean hasDirectlyAnnotation(String qualifiedName) {
      return false;
   }

   public String getPackageName() {
      return AstResolutionUtils.getPackageName(this.wrappedNode);
   }

   public String getClassName() {
      return AstResolutionUtils.getClassName("", this.wrappedNode);
   }

   public String getQualifiedName() {
      String containerName = AstResolutionUtils.containerName((Node)this.wrappedNode.getParentNode().orElse((Object)null));
      return containerName.isEmpty() ? this.getName() : containerName + "." + this.getName();
   }

   public Set<ResolvedReferenceTypeDeclaration> internalTypes() {
      return (Set)this.findMembersOfKind(TypeDeclaration.class).stream().map((typeMember) -> {
         return JavaParserFacade.get(this.typeSolver).getTypeDeclaration(typeMember);
      }).collect(Collectors.toSet());
   }

   public String getName() {
      return this.name;
   }

   public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
      return Lists.newArrayList();
   }

   public Optional<ResolvedReferenceTypeDeclaration> containerType() {
      throw new UnsupportedOperationException("containerType is not supported for " + this.getClass().getCanonicalName());
   }

   public Optional<Node> toAst() {
      return Optional.of(this.wrappedNode);
   }
}
