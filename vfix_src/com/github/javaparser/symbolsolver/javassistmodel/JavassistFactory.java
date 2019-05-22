package com.github.javaparser.symbolsolver.javassistmodel;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedArrayType;
import com.github.javaparser.resolution.types.ResolvedPrimitiveType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedVoidType;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import java.lang.reflect.Modifier;
import javassist.CtClass;
import javassist.NotFoundException;

public class JavassistFactory {
   public static ResolvedType typeUsageFor(CtClass ctClazz, TypeSolver typeSolver) {
      try {
         if (ctClazz.isArray()) {
            return new ResolvedArrayType(typeUsageFor(ctClazz.getComponentType(), typeSolver));
         } else if (ctClazz.isPrimitive()) {
            return ctClazz.getName().equals("void") ? ResolvedVoidType.INSTANCE : ResolvedPrimitiveType.byName(ctClazz.getName());
         } else if (ctClazz.isInterface()) {
            return new ReferenceTypeImpl(new JavassistInterfaceDeclaration(ctClazz, typeSolver), typeSolver);
         } else {
            return ctClazz.isEnum() ? new ReferenceTypeImpl(new JavassistEnumDeclaration(ctClazz, typeSolver), typeSolver) : new ReferenceTypeImpl(new JavassistClassDeclaration(ctClazz, typeSolver), typeSolver);
         }
      } catch (NotFoundException var3) {
         throw new RuntimeException(var3);
      }
   }

   public static ResolvedReferenceTypeDeclaration toTypeDeclaration(CtClass ctClazz, TypeSolver typeSolver) {
      if (ctClazz.isAnnotation()) {
         return new JavassistAnnotationDeclaration(ctClazz, typeSolver);
      } else if (ctClazz.isInterface()) {
         return new JavassistInterfaceDeclaration(ctClazz, typeSolver);
      } else if (ctClazz.isEnum()) {
         return new JavassistEnumDeclaration(ctClazz, typeSolver);
      } else if (ctClazz.isArray()) {
         throw new IllegalArgumentException("This method should not be called passing an array");
      } else {
         return new JavassistClassDeclaration(ctClazz, typeSolver);
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
