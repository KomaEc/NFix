package com.github.javaparser.symbolsolver.javassistmodel;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.resolution.declarations.ResolvedAnnotationMemberDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import javassist.CtMethod;

public class JavassistAnnotationMemberDeclaration implements ResolvedAnnotationMemberDeclaration {
   private CtMethod annotationMember;
   private TypeSolver typeSolver;

   public JavassistAnnotationMemberDeclaration(CtMethod annotationMember, TypeSolver typeSolver) {
      this.annotationMember = annotationMember;
      this.typeSolver = typeSolver;
   }

   public Expression getDefaultValue() {
      throw new UnsupportedOperationException("Obtaining the default value of a library annotation member is not supported yet.");
   }

   public ResolvedType getType() {
      throw new UnsupportedOperationException();
   }

   public String getName() {
      return this.annotationMember.getName();
   }
}
