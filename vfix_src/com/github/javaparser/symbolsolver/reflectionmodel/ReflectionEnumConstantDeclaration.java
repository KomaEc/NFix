package com.github.javaparser.symbolsolver.reflectionmodel;

import com.github.javaparser.resolution.declarations.ResolvedEnumConstantDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.lang.reflect.Field;

public class ReflectionEnumConstantDeclaration implements ResolvedEnumConstantDeclaration {
   private Field enumConstant;
   private TypeSolver typeSolver;

   public ReflectionEnumConstantDeclaration(Field enumConstant, TypeSolver typeSolver) {
      if (!enumConstant.isEnumConstant()) {
         throw new IllegalArgumentException("The given field does not represent an enum constant");
      } else {
         this.enumConstant = enumConstant;
         this.typeSolver = typeSolver;
      }
   }

   public String getName() {
      return this.enumConstant.getName();
   }

   public ResolvedType getType() {
      throw new UnsupportedOperationException();
   }
}
