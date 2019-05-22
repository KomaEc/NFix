package com.github.javaparser.symbolsolver.javassistmodel;

import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import javassist.CtClass;

public class JavassistParameterDeclaration implements ResolvedParameterDeclaration {
   private ResolvedType type;
   private TypeSolver typeSolver;
   private boolean variadic;
   private String name;

   public JavassistParameterDeclaration(CtClass type, TypeSolver typeSolver, boolean variadic, String name) {
      this(JavassistFactory.typeUsageFor(type, typeSolver), typeSolver, variadic, name);
   }

   public JavassistParameterDeclaration(ResolvedType type, TypeSolver typeSolver, boolean variadic, String name) {
      this.name = name;
      this.type = type;
      this.typeSolver = typeSolver;
      this.variadic = variadic;
   }

   public String toString() {
      return "JavassistParameterDeclaration{type=" + this.type + ", typeSolver=" + this.typeSolver + ", variadic=" + this.variadic + '}';
   }

   public boolean hasName() {
      return this.name != null;
   }

   public String getName() {
      return this.name;
   }

   public boolean isField() {
      return false;
   }

   public boolean isParameter() {
      return true;
   }

   public boolean isVariadic() {
      return this.variadic;
   }

   public boolean isType() {
      return false;
   }

   public ResolvedType getType() {
      return this.type;
   }
}
