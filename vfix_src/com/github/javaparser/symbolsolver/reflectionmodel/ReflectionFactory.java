package com.github.javaparser.symbolsolver.reflectionmodel;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedArrayType;
import com.github.javaparser.resolution.types.ResolvedPrimitiveType;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedTypeVariable;
import com.github.javaparser.resolution.types.ResolvedVoidType;
import com.github.javaparser.resolution.types.ResolvedWildcard;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionFactory {
   public static ResolvedReferenceTypeDeclaration typeDeclarationFor(Class<?> clazz, TypeSolver typeSolver) {
      if (clazz.isArray()) {
         throw new IllegalArgumentException("No type declaration available for an Array");
      } else if (clazz.isPrimitive()) {
         throw new IllegalArgumentException();
      } else if (clazz.isAnnotation()) {
         return new ReflectionAnnotationDeclaration(clazz, typeSolver);
      } else if (clazz.isInterface()) {
         return new ReflectionInterfaceDeclaration(clazz, typeSolver);
      } else {
         return (ResolvedReferenceTypeDeclaration)(clazz.isEnum() ? new ReflectionEnumDeclaration(clazz, typeSolver) : new ReflectionClassDeclaration(clazz, typeSolver));
      }
   }

   public static ResolvedType typeUsageFor(Type type, TypeSolver typeSolver) {
      if (type instanceof TypeVariable) {
         TypeVariable<?> tv = (TypeVariable)type;
         boolean declaredOnClass = tv.getGenericDeclaration() instanceof Type;
         ResolvedTypeParameterDeclaration typeParameter = new ReflectionTypeParameter(tv, declaredOnClass, typeSolver);
         return new ResolvedTypeVariable(typeParameter);
      } else if (type instanceof ParameterizedType) {
         ParameterizedType pt = (ParameterizedType)type;
         ResolvedReferenceType rawType = typeUsageFor(pt.getRawType(), typeSolver).asReferenceType();
         List<Type> actualTypes = new ArrayList();
         actualTypes.addAll(Arrays.asList(pt.getActualTypeArguments()));
         rawType = rawType.transformTypeParameters((tp) -> {
            return typeUsageFor((Type)actualTypes.remove(0), typeSolver);
         }).asReferenceType();
         return rawType;
      } else if (type instanceof Class) {
         Class<?> c = (Class)type;
         if (c.isPrimitive()) {
            return c.getName().equals(Void.TYPE.getName()) ? ResolvedVoidType.INSTANCE : ResolvedPrimitiveType.byName(c.getName());
         } else {
            return (ResolvedType)(c.isArray() ? new ResolvedArrayType(typeUsageFor(c.getComponentType(), typeSolver)) : new ReferenceTypeImpl(typeDeclarationFor(c, typeSolver), typeSolver));
         }
      } else if (type instanceof GenericArrayType) {
         GenericArrayType genericArrayType = (GenericArrayType)type;
         return new ResolvedArrayType(typeUsageFor(genericArrayType.getGenericComponentType(), typeSolver));
      } else if (type instanceof WildcardType) {
         WildcardType wildcardType = (WildcardType)type;
         if (wildcardType.getLowerBounds().length > 0 && wildcardType.getUpperBounds().length > 0 && wildcardType.getUpperBounds().length == 1 && wildcardType.getUpperBounds()[0].getTypeName().equals("java.lang.Object")) {
         }

         if (wildcardType.getLowerBounds().length > 0) {
            if (wildcardType.getLowerBounds().length > 1) {
               throw new UnsupportedOperationException();
            } else {
               return ResolvedWildcard.superBound(typeUsageFor(wildcardType.getLowerBounds()[0], typeSolver));
            }
         } else if (wildcardType.getUpperBounds().length > 0) {
            if (wildcardType.getUpperBounds().length > 1) {
               throw new UnsupportedOperationException();
            } else {
               return ResolvedWildcard.extendsBound(typeUsageFor(wildcardType.getUpperBounds()[0], typeSolver));
            }
         } else {
            return ResolvedWildcard.UNBOUNDED;
         }
      } else {
         throw new UnsupportedOperationException(type.getClass().getCanonicalName() + " " + type);
      }
   }

   static AccessSpecifier modifiersToAccessLevel(int modifiers) {
      if (Modifier.isPublic(modifiers)) {
         return AccessSpecifier.PUBLIC;
      } else if (Modifier.isProtected(modifiers)) {
         return AccessSpecifier.PROTECTED;
      } else {
         return Modifier.isPrivate(modifiers) ? AccessSpecifier.PRIVATE : AccessSpecifier.DEFAULT;
      }
   }
}
