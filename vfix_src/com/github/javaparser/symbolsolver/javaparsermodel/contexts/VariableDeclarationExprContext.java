package com.github.javaparser.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.Collections;
import java.util.List;

public class VariableDeclarationExprContext extends AbstractJavaParserContext<VariableDeclarationExpr> {
   public VariableDeclarationExprContext(VariableDeclarationExpr wrappedNode, TypeSolver typeSolver) {
      super(wrappedNode, typeSolver);
   }

   public List<VariableDeclarator> localVariablesExposedToChild(Node child) {
      for(int i = 0; i < ((VariableDeclarationExpr)this.wrappedNode).getVariables().size(); ++i) {
         if (child == ((VariableDeclarationExpr)this.wrappedNode).getVariable(i)) {
            return ((VariableDeclarationExpr)this.wrappedNode).getVariables().subList(0, i);
         }
      }

      return Collections.emptyList();
   }
}
