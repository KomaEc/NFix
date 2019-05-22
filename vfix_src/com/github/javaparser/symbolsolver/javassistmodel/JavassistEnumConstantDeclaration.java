package com.github.javaparser.symbolsolver.javassistmodel;

import com.github.javaparser.resolution.declarations.ResolvedEnumConstantDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import javassist.CtField;

public class JavassistEnumConstantDeclaration implements ResolvedEnumConstantDeclaration {
   private CtField ctField;
   private TypeSolver typeSolver;
   private ResolvedType type;

   public JavassistEnumConstantDeclaration(CtField ctField, TypeSolver typeSolver) {
      if (ctField == null) {
         throw new IllegalArgumentException();
      } else if ((ctField.getFieldInfo2().getAccessFlags() & 16384) == 0) {
         throw new IllegalArgumentException("Trying to instantiate a JavassistEnumConstantDeclaration with something which is not an enum field: " + ctField.toString());
      } else {
         this.ctField = ctField;
         this.typeSolver = typeSolver;
      }
   }

   public String getName() {
      return this.ctField.getName();
   }

   public ResolvedType getType() {
      if (this.type == null) {
         this.type = new ReferenceTypeImpl(new JavassistEnumDeclaration(this.ctField.getDeclaringClass(), this.typeSolver), this.typeSolver);
      }

      return this.type;
   }
}
