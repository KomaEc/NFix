package com.github.javaparser.symbolsolver.javaparsermodel.declarations;

import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.resolution.declarations.ResolvedAnnotationMemberDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;

public class JavaParserAnnotationMemberDeclaration implements ResolvedAnnotationMemberDeclaration {
   private AnnotationMemberDeclaration wrappedNode;
   private TypeSolver typeSolver;

   public AnnotationMemberDeclaration getWrappedNode() {
      return this.wrappedNode;
   }

   public JavaParserAnnotationMemberDeclaration(AnnotationMemberDeclaration wrappedNode, TypeSolver typeSolver) {
      this.wrappedNode = wrappedNode;
      this.typeSolver = typeSolver;
   }

   public Expression getDefaultValue() {
      throw new UnsupportedOperationException();
   }

   public ResolvedType getType() {
      throw new UnsupportedOperationException();
   }

   public String getName() {
      return this.wrappedNode.getNameAsString();
   }
}
