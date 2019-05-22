package com.github.javaparser.symbolsolver.reflectionmodel;

import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.LambdaArgumentTypePlaceholder;
import com.github.javaparser.symbolsolver.logic.FunctionalInterfaceLogic;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.NullType;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class ReflectionClassAdapter {
   private Class<?> clazz;
   private TypeSolver typeSolver;
   private ResolvedReferenceTypeDeclaration typeDeclaration;

   public ReflectionClassAdapter(Class<?> clazz, TypeSolver typeSolver, ResolvedReferenceTypeDeclaration typeDeclaration) {
      this.clazz = clazz;
      this.typeSolver = typeSolver;
      this.typeDeclaration = typeDeclaration;
   }

   public ReferenceTypeImpl getSuperClass() {
      if (this.clazz.getGenericSuperclass() == null) {
         return null;
      } else {
         Type superType = this.clazz.getGenericSuperclass();
         if (superType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)superType;
            List<ResolvedType> typeParameters = (List)Arrays.stream(parameterizedType.getActualTypeArguments()).map((t) -> {
               return ReflectionFactory.typeUsageFor(t, this.typeSolver);
            }).collect(Collectors.toList());
            return new ReferenceTypeImpl(new ReflectionClassDeclaration(this.clazz.getSuperclass(), this.typeSolver), typeParameters, this.typeSolver);
         } else {
            return new ReferenceTypeImpl(new ReflectionClassDeclaration(this.clazz.getSuperclass(), this.typeSolver), this.typeSolver);
         }
      }
   }

   public List<ResolvedReferenceType> getInterfaces() {
      List<ResolvedReferenceType> interfaces = new ArrayList();
      Type[] var2 = this.clazz.getGenericInterfaces();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Type superInterface = var2[var4];
         if (superInterface instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)superInterface;
            List<ResolvedType> typeParameters = (List)Arrays.stream(parameterizedType.getActualTypeArguments()).map((t) -> {
               return ReflectionFactory.typeUsageFor(t, this.typeSolver);
            }).collect(Collectors.toList());
            interfaces.add(new ReferenceTypeImpl(new ReflectionInterfaceDeclaration((Class)((ParameterizedType)superInterface).getRawType(), this.typeSolver), typeParameters, this.typeSolver));
         } else {
            interfaces.add(new ReferenceTypeImpl(new ReflectionInterfaceDeclaration((Class)superInterface, this.typeSolver), this.typeSolver));
         }
      }

      return interfaces;
   }

   public List<ResolvedReferenceType> getAncestors() {
      List<ResolvedReferenceType> ancestors = new LinkedList();
      ReferenceTypeImpl superClass;
      if (this.getSuperClass() != null) {
         superClass = this.getSuperClass();
         ancestors.add(superClass);
      } else {
         superClass = new ReferenceTypeImpl(new ReflectionClassDeclaration(Object.class, this.typeSolver), this.typeSolver);
         ancestors.add(superClass);
      }

      ancestors.addAll(this.getInterfaces());

      for(int i = 0; i < ancestors.size(); ++i) {
         ResolvedReferenceType ancestor = (ResolvedReferenceType)ancestors.get(i);
         if (ancestor.hasName() && ancestor.getQualifiedName().equals(Object.class.getCanonicalName())) {
            ancestors.remove(i);
            --i;
         }
      }

      return ancestors;
   }

   public ResolvedFieldDeclaration getField(String name) {
      Field[] var2 = this.clazz.getDeclaredFields();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field field = var2[var4];
         if (field.getName().equals(name)) {
            return new ReflectionFieldDeclaration(field, this.typeSolver);
         }
      }

      Iterator var6 = this.typeDeclaration.getAllAncestors().iterator();

      ResolvedReferenceType ancestor;
      do {
         if (!var6.hasNext()) {
            throw new UnsolvedSymbolException(name, "Field in " + this);
         }

         ancestor = (ResolvedReferenceType)var6.next();
      } while(!ancestor.getTypeDeclaration().hasField(name));

      ReflectionFieldDeclaration reflectionFieldDeclaration = (ReflectionFieldDeclaration)ancestor.getTypeDeclaration().getField(name);
      return reflectionFieldDeclaration.replaceType((ResolvedType)ancestor.getFieldType(name).get());
   }

   public boolean hasField(String name) {
      Field[] var2 = this.clazz.getDeclaredFields();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field field = var2[var4];
         if (field.getName().equals(name)) {
            return true;
         }
      }

      ReferenceTypeImpl superclass = this.getSuperClass();
      return superclass != null && superclass.getTypeDeclaration().hasField(name);
   }

   public List<ResolvedFieldDeclaration> getAllFields() {
      ArrayList<ResolvedFieldDeclaration> fields = new ArrayList();
      Field[] var2 = this.clazz.getDeclaredFields();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field field = var2[var4];
         fields.add(new ReflectionFieldDeclaration(field, this.typeSolver));
      }

      Iterator var6 = this.typeDeclaration.getAllAncestors().iterator();

      while(var6.hasNext()) {
         ResolvedReferenceType ancestor = (ResolvedReferenceType)var6.next();
         fields.addAll(ancestor.getTypeDeclaration().getAllFields());
      }

      return fields;
   }

   public Set<ResolvedMethodDeclaration> getDeclaredMethods() {
      return (Set)Arrays.stream(this.clazz.getDeclaredMethods()).filter((m) -> {
         return !m.isSynthetic() && !m.isBridge();
      }).map((m) -> {
         return new ReflectionMethodDeclaration(m, this.typeSolver);
      }).collect(Collectors.toSet());
   }

   public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
      List<ResolvedTypeParameterDeclaration> params = new ArrayList();
      TypeVariable[] var2 = this.clazz.getTypeParameters();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         TypeVariable<?> tv = var2[var4];
         params.add(new ReflectionTypeParameter(tv, true, this.typeSolver));
      }

      return params;
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
      } else if (type.describe().equals(this.typeDeclaration.getQualifiedName())) {
         return true;
      } else if (type instanceof ReferenceTypeImpl) {
         ReferenceTypeImpl otherTypeDeclaration = (ReferenceTypeImpl)type;
         return otherTypeDeclaration.getTypeDeclaration().canBeAssignedTo(this.typeDeclaration);
      } else {
         return false;
      }
   }

   public boolean hasDirectlyAnnotation(String canonicalName) {
      Annotation[] var2 = this.clazz.getDeclaredAnnotations();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Annotation a = var2[var4];
         if (a.annotationType().getCanonicalName().equals(canonicalName)) {
            return true;
         }
      }

      return false;
   }

   private final boolean isFunctionalInterface() {
      return FunctionalInterfaceLogic.getFunctionalMethod(this.typeDeclaration).isPresent();
   }

   public List<ResolvedConstructorDeclaration> getConstructors() {
      return (List)Arrays.stream(this.clazz.getDeclaredConstructors()).map((m) -> {
         return new ReflectionConstructorDeclaration(m, this.typeSolver);
      }).collect(Collectors.toList());
   }

   public Optional<ResolvedReferenceTypeDeclaration> containerType() {
      Class<?> declaringClass = this.clazz.getDeclaringClass();
      return declaringClass == null ? Optional.empty() : Optional.of(ReflectionFactory.typeDeclarationFor(declaringClass, this.typeSolver));
   }
}
