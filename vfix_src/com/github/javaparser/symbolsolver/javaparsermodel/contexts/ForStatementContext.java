package com.github.javaparser.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithStatements;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserSymbolDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ForStatementContext extends AbstractJavaParserContext<ForStmt> {
   public ForStatementContext(ForStmt wrappedNode, TypeSolver typeSolver) {
      super(wrappedNode, typeSolver);
   }

   public SymbolReference<? extends ResolvedValueDeclaration> solveSymbol(String name, TypeSolver typeSolver) {
      Iterator var3 = ((ForStmt)this.wrappedNode).getInitialization().iterator();

      while(true) {
         while(var3.hasNext()) {
            Expression expression = (Expression)var3.next();
            if (expression instanceof VariableDeclarationExpr) {
               VariableDeclarationExpr variableDeclarationExpr = (VariableDeclarationExpr)expression;
               Iterator var6 = variableDeclarationExpr.getVariables().iterator();

               while(var6.hasNext()) {
                  VariableDeclarator variableDeclarator = (VariableDeclarator)var6.next();
                  if (variableDeclarator.getName().getId().equals(name)) {
                     return SymbolReference.solved(JavaParserSymbolDeclaration.localVar(variableDeclarator, typeSolver));
                  }
               }
            } else if (!(expression instanceof AssignExpr) && !(expression instanceof MethodCallExpr)) {
               throw new UnsupportedOperationException(expression.getClass().getCanonicalName());
            }
         }

         if (Navigator.requireParentNode(this.wrappedNode) instanceof NodeWithStatements) {
            return StatementContext.solveInBlock(name, typeSolver, (Statement)this.wrappedNode);
         }

         return this.getParent().solveSymbol(name, typeSolver);
      }
   }

   public SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> argumentsTypes, boolean staticOnly, TypeSolver typeSolver) {
      return this.getParent().solveMethod(name, argumentsTypes, false, typeSolver);
   }

   public List<VariableDeclarator> localVariablesExposedToChild(Node child) {
      List<VariableDeclarator> res = new LinkedList();
      Iterator var3 = ((ForStmt)this.wrappedNode).getInitialization().iterator();

      while(var3.hasNext()) {
         Expression expression = (Expression)var3.next();
         if (expression instanceof VariableDeclarationExpr) {
            VariableDeclarationExpr variableDeclarationExpr = (VariableDeclarationExpr)expression;
            res.addAll(variableDeclarationExpr.getVariables());
         }
      }

      return res;
   }
}
