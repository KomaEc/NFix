package com.github.javaparser.symbolsolver.javaparsermodel.declarations;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;

public class JavaParserVariableDeclaration implements ResolvedValueDeclaration {
   private VariableDeclarator variableDeclarator;
   private VariableDeclarationExpr wrappedNode;
   private TypeSolver typeSolver;

   public JavaParserVariableDeclaration(VariableDeclarator variableDeclarator, TypeSolver typeSolver) {
      if (typeSolver == null) {
         throw new IllegalArgumentException("typeSolver should not be null");
      } else {
         this.variableDeclarator = variableDeclarator;
         this.typeSolver = typeSolver;
         if (!(Navigator.requireParentNode(variableDeclarator) instanceof VariableDeclarationExpr)) {
            throw new IllegalStateException(Navigator.requireParentNode(variableDeclarator).getClass().getCanonicalName());
         } else {
            this.wrappedNode = (VariableDeclarationExpr)Navigator.requireParentNode(variableDeclarator);
         }
      }
   }

   public ResolvedType getType() {
      return JavaParserFacade.get(this.typeSolver).convert(this.variableDeclarator.getType(), (Node)this.wrappedNode);
   }

   public String getName() {
      return this.variableDeclarator.getName().getId();
   }

   public boolean isVariable() {
      return true;
   }

   public VariableDeclarationExpr getWrappedNode() {
      return this.wrappedNode;
   }

   public VariableDeclarator getVariableDeclarator() {
      return this.variableDeclarator;
   }

   public String toString() {
      return "JavaParserVariableDeclaration{" + this.getName() + "}";
   }
}
