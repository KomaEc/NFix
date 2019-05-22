package com.github.javaparser.symbolsolver.reflectionmodel;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedEnumConstantDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedEnumDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.logic.AbstractTypeDeclaration;
import com.github.javaparser.symbolsolver.logic.ConfilictingGenericTypesException;
import com.github.javaparser.symbolsolver.logic.InferenceContext;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ReflectionEnumDeclaration extends AbstractTypeDeclaration implements ResolvedEnumDeclaration {
   private Class<?> clazz;
   private TypeSolver typeSolver;
   private ReflectionClassAdapter reflectionClassAdapter;

   public ReflectionEnumDeclaration(Class<?> clazz, TypeSolver typeSolver) {
      if (clazz == null) {
         throw new IllegalArgumentException("Class should not be null");
      } else if (clazz.isInterface()) {
         throw new IllegalArgumentException("Class should not be an interface");
      } else if (clazz.isPrimitive()) {
         throw new IllegalArgumentException("Class should not represent a primitive class");
      } else if (clazz.isArray()) {
         throw new IllegalArgumentException("Class should not be an array");
      } else if (!clazz.isEnum()) {
         throw new IllegalArgumentException("Class should be an enum");
      } else {
         this.clazz = clazz;
         this.typeSolver = typeSolver;
         this.reflectionClassAdapter = new ReflectionClassAdapter(clazz, typeSolver, this);
      }
   }

   public AccessSpecifier accessSpecifier() {
      return ReflectionFactory.modifiersToAccessLevel(this.clazz.getModifiers());
   }

   public Optional<ResolvedReferenceTypeDeclaration> containerType() {
      return this.reflectionClassAdapter.containerType();
   }

   public String getPackageName() {
      return this.clazz.getPackage() != null ? this.clazz.getPackage().getName() : null;
   }

   public String getClassName() {
      String canonicalName = this.clazz.getCanonicalName();
      return canonicalName != null && this.getPackageName() != null ? canonicalName.substring(this.getPackageName().length() + 1, canonicalName.length()) : null;
   }

   public String getQualifiedName() {
      return this.clazz.getCanonicalName();
   }

   public List<ResolvedReferenceType> getAncestors(boolean acceptIncompleteList) {
      return this.reflectionClassAdapter.getAncestors();
   }

   public ResolvedFieldDeclaration getField(String name) {
      return this.reflectionClassAdapter.getField(name);
   }

   public boolean hasField(String name) {
      return this.reflectionClassAdapter.hasField(name);
   }

   public List<ResolvedFieldDeclaration> getAllFields() {
      return this.reflectionClassAdapter.getAllFields();
   }

   public Set<ResolvedMethodDeclaration> getDeclaredMethods() {
      return this.reflectionClassAdapter.getDeclaredMethods();
   }

   public boolean isAssignableBy(ResolvedType type) {
      return this.reflectionClassAdapter.isAssignableBy(type);
   }

   public boolean isAssignableBy(ResolvedReferenceTypeDeclaration other) {
      return this.isAssignableBy((ResolvedType)(new ReferenceTypeImpl(other, this.typeSolver)));
   }

   public boolean hasDirectlyAnnotation(String qualifiedName) {
      return this.reflectionClassAdapter.hasDirectlyAnnotation(qualifiedName);
   }

   public String getName() {
      return this.clazz.getSimpleName();
   }

   public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
      return this.reflectionClassAdapter.getTypeParameters();
   }

   public SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> parameterTypes, boolean staticOnly) {
      return ReflectionMethodResolutionLogic.solveMethod(name, parameterTypes, staticOnly, this.typeSolver, this, this.clazz);
   }

   public Optional<MethodUsage> solveMethodAsUsage(String name, List<ResolvedType> parameterTypes, TypeSolver typeSolver, Context invokationContext, List<ResolvedType> typeParameterValues) {
      Optional<MethodUsage> res = ReflectionMethodResolutionLogic.solveMethodAsUsage(name, parameterTypes, typeSolver, invokationContext, typeParameterValues, this, this.clazz);
      if (!res.isPresent()) {
         return res;
      } else {
         InferenceContext inferenceContext = new InferenceContext(MyObjectProvider.INSTANCE);
         MethodUsage methodUsage = (MethodUsage)res.get();
         int i = 0;
         List<ResolvedType> parameters = new LinkedList();

         for(Iterator var11 = parameterTypes.iterator(); var11.hasNext(); ++i) {
            ResolvedType actualType = (ResolvedType)var11.next();
            ResolvedType formalType = methodUsage.getParamType(i);
            parameters.add(inferenceContext.addPair(formalType, actualType));
         }

         try {
            ResolvedType returnType = inferenceContext.addSingle(methodUsage.returnType());

            for(int j = 0; j < parameters.size(); ++j) {
               methodUsage = methodUsage.replaceParamType(j, inferenceContext.resolve((ResolvedType)parameters.get(j)));
            }

            methodUsage = methodUsage.replaceReturnType(inferenceContext.resolve(returnType));
            return Optional.of(methodUsage);
         } catch (ConfilictingGenericTypesException var14) {
            return Optional.empty();
         }
      }
   }

   public List<ResolvedEnumConstantDeclaration> getEnumConstants() {
      return (List)Arrays.stream(this.clazz.getFields()).filter(Field::isEnumConstant).map((c) -> {
         return new ReflectionEnumConstantDeclaration(c, this.typeSolver);
      }).collect(Collectors.toList());
   }

   public Set<ResolvedReferenceTypeDeclaration> internalTypes() {
      return (Set)Arrays.stream(this.clazz.getDeclaredClasses()).map((ic) -> {
         return ReflectionFactory.typeDeclarationFor(ic, this.typeSolver);
      }).collect(Collectors.toSet());
   }

   public List<ResolvedConstructorDeclaration> getConstructors() {
      return this.reflectionClassAdapter.getConstructors();
   }
}
