package com.github.javaparser.symbolsolver.reflectionmodel;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedInterfaceDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.javaparsermodel.LambdaArgumentTypePlaceholder;
import com.github.javaparser.symbolsolver.logic.AbstractTypeDeclaration;
import com.github.javaparser.symbolsolver.logic.ConfilictingGenericTypesException;
import com.github.javaparser.symbolsolver.logic.InferenceContext;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.NullType;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ReflectionInterfaceDeclaration extends AbstractTypeDeclaration implements ResolvedInterfaceDeclaration {
   private Class<?> clazz;
   private TypeSolver typeSolver;
   private ReflectionClassAdapter reflectionClassAdapter;

   public ReflectionInterfaceDeclaration(Class<?> clazz, TypeSolver typeSolver) {
      if (!clazz.isInterface()) {
         throw new IllegalArgumentException();
      } else {
         this.clazz = clazz;
         this.typeSolver = typeSolver;
         this.reflectionClassAdapter = new ReflectionClassAdapter(clazz, typeSolver, this);
      }
   }

   public boolean isAssignableBy(ResolvedReferenceTypeDeclaration other) {
      return this.isAssignableBy((ResolvedType)(new ReferenceTypeImpl(other, this.typeSolver)));
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

   /** @deprecated */
   @Deprecated
   public SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> parameterTypes, boolean staticOnly) {
      return ReflectionMethodResolutionLogic.solveMethod(name, parameterTypes, staticOnly, this.typeSolver, this, this.clazz);
   }

   public String toString() {
      return "ReflectionInterfaceDeclaration{clazz=" + this.clazz.getCanonicalName() + '}';
   }

   public ResolvedType getUsage(Node node) {
      return new ReferenceTypeImpl(this, this.typeSolver);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof ReflectionInterfaceDeclaration)) {
         return false;
      } else {
         ReflectionInterfaceDeclaration that = (ReflectionInterfaceDeclaration)o;
         if (!this.clazz.getCanonicalName().equals(that.clazz.getCanonicalName())) {
            return false;
         } else {
            return this.getTypeParameters().equals(that.getTypeParameters());
         }
      }
   }

   public int hashCode() {
      return this.clazz.hashCode();
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

   public boolean canBeAssignedTo(ResolvedReferenceTypeDeclaration other) {
      if (other instanceof LambdaArgumentTypePlaceholder) {
         return this.isFunctionalInterface();
      } else if (other.getQualifiedName().equals(this.getQualifiedName())) {
         return true;
      } else if (this.clazz.getSuperclass() != null && (new ReflectionInterfaceDeclaration(this.clazz.getSuperclass(), this.typeSolver)).canBeAssignedTo(other)) {
         return true;
      } else {
         Class[] var2 = this.clazz.getInterfaces();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Class interfaze = var2[var4];
            if ((new ReflectionInterfaceDeclaration(interfaze, this.typeSolver)).canBeAssignedTo(other)) {
               return true;
            }
         }

         if (other.getQualifiedName().equals(Object.class.getCanonicalName())) {
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean isAssignableBy(ResolvedType type) {
      if (type instanceof NullType) {
         return true;
      } else if (type instanceof LambdaArgumentTypePlaceholder) {
         return this.isFunctionalInterface();
      } else if (type.isArray()) {
         return false;
      } else if (type.isPrimitive()) {
         return false;
      } else if (type.describe().equals(this.getQualifiedName())) {
         return true;
      } else if (type instanceof ReferenceTypeImpl) {
         ReferenceTypeImpl otherTypeDeclaration = (ReferenceTypeImpl)type;
         return otherTypeDeclaration.getTypeDeclaration().canBeAssignedTo(this);
      } else {
         return false;
      }
   }

   public boolean isTypeParameter() {
      return false;
   }

   public ResolvedFieldDeclaration getField(String name) {
      return this.reflectionClassAdapter.getField(name);
   }

   public List<ResolvedFieldDeclaration> getAllFields() {
      return this.reflectionClassAdapter.getAllFields();
   }

   /** @deprecated */
   @Deprecated
   public SymbolReference<? extends ResolvedValueDeclaration> solveSymbol(String name, TypeSolver typeSolver) {
      Field[] var3 = this.clazz.getFields();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field field = var3[var5];
         if (field.getName().equals(name)) {
            return SymbolReference.solved(new ReflectionFieldDeclaration(field, typeSolver));
         }
      }

      return SymbolReference.unsolved(ResolvedValueDeclaration.class);
   }

   public List<ResolvedReferenceType> getAncestors(boolean acceptIncompleteList) {
      return this.reflectionClassAdapter.getAncestors();
   }

   public Set<ResolvedMethodDeclaration> getDeclaredMethods() {
      return this.reflectionClassAdapter.getDeclaredMethods();
   }

   public boolean hasField(String name) {
      return this.reflectionClassAdapter.hasField(name);
   }

   public String getName() {
      return this.clazz.getSimpleName();
   }

   public boolean isInterface() {
      return true;
   }

   public List<ResolvedReferenceType> getInterfacesExtended() {
      List<ResolvedReferenceType> res = new ArrayList();
      Class[] var2 = this.clazz.getInterfaces();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Class i = var2[var4];
         res.add(new ReferenceTypeImpl(new ReflectionInterfaceDeclaration(i, this.typeSolver), this.typeSolver));
      }

      return res;
   }

   public Optional<ResolvedReferenceTypeDeclaration> containerType() {
      return this.reflectionClassAdapter.containerType();
   }

   public Set<ResolvedReferenceTypeDeclaration> internalTypes() {
      return (Set)Arrays.stream(this.clazz.getDeclaredClasses()).map((ic) -> {
         return ReflectionFactory.typeDeclarationFor(ic, this.typeSolver);
      }).collect(Collectors.toSet());
   }

   public ResolvedInterfaceDeclaration asInterface() {
      return this;
   }

   public boolean hasDirectlyAnnotation(String canonicalName) {
      return this.reflectionClassAdapter.hasDirectlyAnnotation(canonicalName);
   }

   public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
      return this.reflectionClassAdapter.getTypeParameters();
   }

   public AccessSpecifier accessSpecifier() {
      return ReflectionFactory.modifiersToAccessLevel(this.clazz.getModifiers());
   }

   public List<ResolvedConstructorDeclaration> getConstructors() {
      return Collections.emptyList();
   }

   public Optional<ClassOrInterfaceDeclaration> toAst() {
      return Optional.empty();
   }
}
