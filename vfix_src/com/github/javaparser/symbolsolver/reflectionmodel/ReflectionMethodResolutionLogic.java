package com.github.javaparser.symbolsolver.reflectionmodel;

import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedTypeVariable;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.symbolsolver.resolution.MethodResolutionLogic;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class ReflectionMethodResolutionLogic {
   static SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> parameterTypes, boolean staticOnly, TypeSolver typeSolver, ResolvedReferenceTypeDeclaration scopeType, Class clazz) {
      List<ResolvedMethodDeclaration> methods = new ArrayList();
      Predicate<Method> staticOnlyCheck = (m) -> {
         return !staticOnly || staticOnly && Modifier.isStatic(m.getModifiers());
      };
      Method[] var8 = clazz.getMethods();
      int var9 = var8.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         Method method = var8[var10];
         if (!method.isBridge() && !method.isSynthetic() && method.getName().equals(name) && staticOnlyCheck.test(method)) {
            ResolvedMethodDeclaration methodDeclaration = new ReflectionMethodDeclaration(method, typeSolver);
            methods.add(methodDeclaration);
         }
      }

      Iterator var13 = scopeType.getAncestors().iterator();

      while(var13.hasNext()) {
         ResolvedReferenceType ancestor = (ResolvedReferenceType)var13.next();
         SymbolReference<ResolvedMethodDeclaration> ref = MethodResolutionLogic.solveMethodInType(ancestor.getTypeDeclaration(), name, parameterTypes, staticOnly, typeSolver);
         if (ref.isSolved()) {
            methods.add(ref.getCorrespondingDeclaration());
         }
      }

      if (scopeType.getAncestors().isEmpty()) {
         ReferenceTypeImpl objectClass = new ReferenceTypeImpl(new ReflectionClassDeclaration(Object.class, typeSolver), typeSolver);
         SymbolReference<ResolvedMethodDeclaration> ref = MethodResolutionLogic.solveMethodInType(objectClass.getTypeDeclaration(), name, parameterTypes, staticOnly, typeSolver);
         if (ref.isSolved()) {
            methods.add(ref.getCorrespondingDeclaration());
         }
      }

      return MethodResolutionLogic.findMostApplicable(methods, name, parameterTypes, typeSolver);
   }

   static Optional<MethodUsage> solveMethodAsUsage(String name, List<ResolvedType> argumentsTypes, TypeSolver typeSolver, Context invokationContext, List<ResolvedType> typeParameterValues, ResolvedReferenceTypeDeclaration scopeType, Class clazz) {
      if (((List)typeParameterValues).size() != scopeType.getTypeParameters().size() && !scopeType.getTypeParameters().isEmpty()) {
         typeParameterValues = new ArrayList();

         for(int i = 0; i < scopeType.getTypeParameters().size(); ++i) {
            ((List)typeParameterValues).add(new ReferenceTypeImpl(new ReflectionClassDeclaration(Object.class, typeSolver), typeSolver));
         }
      }

      List<MethodUsage> methods = new ArrayList();
      Method[] var8 = clazz.getMethods();
      int var9 = var8.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         Method method = var8[var10];
         if (method.getName().equals(name) && !method.isBridge() && !method.isSynthetic()) {
            ResolvedMethodDeclaration methodDeclaration = new ReflectionMethodDeclaration(method, typeSolver);
            MethodUsage methodUsage = replaceParams((List)typeParameterValues, scopeType, methodDeclaration);
            methods.add(methodUsage);
         }
      }

      Iterator var15 = scopeType.getAncestors().iterator();

      while(var15.hasNext()) {
         ResolvedReferenceType ancestor = (ResolvedReferenceType)var15.next();
         SymbolReference<ResolvedMethodDeclaration> ref = MethodResolutionLogic.solveMethodInType(ancestor.getTypeDeclaration(), name, argumentsTypes, typeSolver);
         if (ref.isSolved()) {
            ResolvedMethodDeclaration correspondingDeclaration = (ResolvedMethodDeclaration)ref.getCorrespondingDeclaration();
            MethodUsage methodUsage = replaceParams((List)typeParameterValues, ancestor.getTypeDeclaration(), correspondingDeclaration);
            methods.add(methodUsage);
         }
      }

      if (scopeType.getAncestors().isEmpty()) {
         ReferenceTypeImpl objectClass = new ReferenceTypeImpl(new ReflectionClassDeclaration(Object.class, typeSolver), typeSolver);
         SymbolReference<ResolvedMethodDeclaration> ref = MethodResolutionLogic.solveMethodInType(objectClass.getTypeDeclaration(), name, argumentsTypes, typeSolver);
         if (ref.isSolved()) {
            MethodUsage usage = replaceParams((List)typeParameterValues, objectClass.getTypeDeclaration(), (ResolvedMethodDeclaration)ref.getCorrespondingDeclaration());
            methods.add(usage);
         }
      }

      argumentsTypes = (List)argumentsTypes.stream().map((pt) -> {
         int i = 0;

         for(Iterator var4 = scopeType.getTypeParameters().iterator(); var4.hasNext(); ++i) {
            ResolvedTypeParameterDeclaration tp = (ResolvedTypeParameterDeclaration)var4.next();
            pt = pt.replaceTypeVariables(tp, (ResolvedType)typeParameterValues.get(i));
         }

         return pt;
      }).collect(Collectors.toList());
      return MethodResolutionLogic.findMostApplicableUsage(methods, name, argumentsTypes, typeSolver);
   }

   private static MethodUsage replaceParams(List<ResolvedType> typeParameterValues, ResolvedReferenceTypeDeclaration typeParametrizable, ResolvedMethodDeclaration methodDeclaration) {
      MethodUsage methodUsage = new MethodUsage(methodDeclaration);
      int i = 0;
      Iterator var5;
      ResolvedTypeParameterDeclaration methodTypeParameter;
      if (typeParameterValues.size() == typeParametrizable.getTypeParameters().size()) {
         for(var5 = typeParametrizable.getTypeParameters().iterator(); var5.hasNext(); ++i) {
            methodTypeParameter = (ResolvedTypeParameterDeclaration)var5.next();
            methodUsage = methodUsage.replaceTypeParameter(methodTypeParameter, (ResolvedType)typeParameterValues.get(i));
         }
      }

      for(var5 = methodDeclaration.getTypeParameters().iterator(); var5.hasNext(); methodUsage = methodUsage.replaceTypeParameter(methodTypeParameter, new ResolvedTypeVariable(methodTypeParameter))) {
         methodTypeParameter = (ResolvedTypeParameterDeclaration)var5.next();
      }

      return methodUsage;
   }
}
