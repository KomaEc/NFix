package com.github.javaparser.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserSymbolDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.Collections;
import java.util.List;

public class ForechStatementContext extends AbstractJavaParserContext<ForeachStmt> {
   public ForechStatementContext(ForeachStmt wrappedNode, TypeSolver typeSolver) {
      super(wrappedNode, typeSolver);
   }

   public SymbolReference<? extends ResolvedValueDeclaration> solveSymbol(String name, TypeSolver typeSolver) {
      if (((ForeachStmt)this.wrappedNode).getVariable().getVariables().size() != 1) {
         throw new IllegalStateException();
      } else {
         VariableDeclarator variableDeclarator = (VariableDeclarator)((ForeachStmt)this.wrappedNode).getVariable().getVariables().get(0);
         if (variableDeclarator.getName().getId().equals(name)) {
            return SymbolReference.solved(JavaParserSymbolDeclaration.localVar(variableDeclarator, typeSolver));
         } else {
            return Navigator.requireParentNode(this.wrappedNode) instanceof BlockStmt ? StatementContext.solveInBlock(name, typeSolver, (Statement)this.wrappedNode) : this.getParent().solveSymbol(name, typeSolver);
         }
      }
   }

   public SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> argumentsTypes, boolean staticOnly, TypeSolver typeSolver) {
      return this.getParent().solveMethod(name, argumentsTypes, false, typeSolver);
   }

   public List<VariableDeclarator> localVariablesExposedToChild(Node child) {
      return (List)(child == ((ForeachStmt)this.wrappedNode).getBody() ? ((ForeachStmt)this.wrappedNode).getVariable().getVariables() : Collections.emptyList());
   }
}
