package com.github.javaparser.symbolsolver.reflectionmodel;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionFieldDeclaration implements ResolvedFieldDeclaration {
   private Field field;
   private TypeSolver typeSolver;
   private ResolvedType type;

   public ReflectionFieldDeclaration(Field field, TypeSolver typeSolver) {
      this.field = field;
      this.typeSolver = typeSolver;
      this.type = this.calcType();
   }

   private ReflectionFieldDeclaration(Field field, TypeSolver typeSolver, ResolvedType type) {
      this.field = field;
      this.typeSolver = typeSolver;
      this.type = type;
   }

   public ResolvedType getType() {
      return this.type;
   }

   private ResolvedType calcType() {
      return ReflectionFactory.typeUsageFor(this.field.getGenericType(), this.typeSolver);
   }

   public String getName() {
      return this.field.getName();
   }

   public boolean isStatic() {
      return Modifier.isStatic(this.field.getModifiers());
   }

   public boolean isField() {
      return true;
   }

   public ResolvedTypeDeclaration declaringType() {
      return ReflectionFactory.typeDeclarationFor(this.field.getDeclaringClass(), this.typeSolver);
   }

   public ResolvedFieldDeclaration replaceType(ResolvedType fieldType) {
      return new ReflectionFieldDeclaration(this.field, this.typeSolver, fieldType);
   }

   public boolean isParameter() {
      return false;
   }

   public boolean isType() {
      return false;
   }

   public AccessSpecifier accessSpecifier() {
      return ReflectionFactory.modifiersToAccessLevel(this.field.getModifiers());
   }
}
