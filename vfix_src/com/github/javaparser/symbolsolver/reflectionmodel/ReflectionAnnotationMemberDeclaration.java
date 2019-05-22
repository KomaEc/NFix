package com.github.javaparser.symbolsolver.reflectionmodel;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.resolution.declarations.ResolvedAnnotationMemberDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.lang.reflect.Method;

public class ReflectionAnnotationMemberDeclaration implements ResolvedAnnotationMemberDeclaration {
   private Method annotationMember;
   private TypeSolver typeSolver;

   public ReflectionAnnotationMemberDeclaration(Method annotationMember, TypeSolver typeSolver) {
      this.annotationMember = annotationMember;
      this.typeSolver = typeSolver;
   }

   public Expression getDefaultValue() {
      throw new UnsupportedOperationException("Obtaining the default value of a reflection annotation member is not supported yet.");
   }

   public ResolvedType getType() {
      throw new UnsupportedOperationException();
   }

   public String getName() {
      return this.annotationMember.getName();
   }
}
