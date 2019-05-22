package com.github.javaparser.symbolsolver.reflectionmodel;

import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.lang.reflect.Type;
import java.util.Objects;

public class ReflectionParameterDeclaration implements ResolvedParameterDeclaration {
   private Class<?> type;
   private Type genericType;
   private TypeSolver typeSolver;
   private boolean variadic;
   private String name;

   public ReflectionParameterDeclaration(Class<?> type, Type genericType, TypeSolver typeSolver, boolean variadic, String name) {
      this.type = type;
      this.genericType = genericType;
      this.typeSolver = typeSolver;
      this.variadic = variadic;
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public boolean hasName() {
      return this.name != null;
   }

   public String toString() {
      return "ReflectionParameterDeclaration{type=" + this.type + ", name=" + this.name + '}';
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
      return ReflectionFactory.typeUsageFor(this.genericType, this.typeSolver);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ReflectionParameterDeclaration that = (ReflectionParameterDeclaration)o;
         return this.variadic == that.variadic && Objects.equals(this.type, that.type) && Objects.equals(this.genericType, that.genericType) && Objects.equals(this.typeSolver, that.typeSolver) && Objects.equals(this.name, that.name);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.type, this.genericType, this.typeSolver, this.variadic, this.name});
   }
}
