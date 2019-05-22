package com.github.javaparser.symbolsolver.javaparsermodel.declarations;

import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedEnumConstantDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;

public class JavaParserEnumConstantDeclaration implements ResolvedEnumConstantDeclaration {
   private TypeSolver typeSolver;
   private EnumConstantDeclaration wrappedNode;

   public JavaParserEnumConstantDeclaration(EnumConstantDeclaration wrappedNode, TypeSolver typeSolver) {
      this.wrappedNode = wrappedNode;
      this.typeSolver = typeSolver;
   }

   public ResolvedType getType() {
      return new ReferenceTypeImpl(new JavaParserEnumDeclaration((EnumDeclaration)Navigator.requireParentNode(this.wrappedNode), this.typeSolver), this.typeSolver);
   }

   public String getName() {
      return this.wrappedNode.getName().getId();
   }

   public EnumConstantDeclaration getWrappedNode() {
      return this.wrappedNode;
   }
}
