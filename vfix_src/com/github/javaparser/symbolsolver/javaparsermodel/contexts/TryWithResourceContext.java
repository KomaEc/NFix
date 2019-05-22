package com.github.javaparser.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserSymbolDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.resolution.Value;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TryWithResourceContext extends AbstractJavaParserContext<TryStmt> {
   public TryWithResourceContext(TryStmt wrappedNode, TypeSolver typeSolver) {
      super(wrappedNode, typeSolver);
   }

   public Optional<Value> solveSymbolAsValue(String name, TypeSolver typeSolver) {
      Iterator var3 = ((TryStmt)this.wrappedNode).getResources().iterator();

      while(true) {
         Expression expr;
         do {
            if (!var3.hasNext()) {
               if (Navigator.requireParentNode(this.wrappedNode) instanceof BlockStmt) {
                  return StatementContext.solveInBlockAsValue(name, typeSolver, (Statement)this.wrappedNode);
               }

               return this.getParent().solveSymbolAsValue(name, typeSolver);
            }

            expr = (Expression)var3.next();
         } while(!(expr instanceof VariableDeclarationExpr));

         Iterator var5 = ((VariableDeclarationExpr)expr).getVariables().iterator();

         while(var5.hasNext()) {
            VariableDeclarator v = (VariableDeclarator)var5.next();
            if (v.getName().getIdentifier().equals(name)) {
               JavaParserSymbolDeclaration decl = JavaParserSymbolDeclaration.localVar(v, typeSolver);
               return Optional.of(Value.from(decl));
            }
         }
      }
   }

   public SymbolReference<? extends ResolvedValueDeclaration> solveSymbol(String name, TypeSolver typeSolver) {
      Iterator var3 = ((TryStmt)this.wrappedNode).getResources().iterator();

      while(true) {
         Expression expr;
         do {
            if (!var3.hasNext()) {
               if (Navigator.requireParentNode(this.wrappedNode) instanceof BlockStmt) {
                  return StatementContext.solveInBlock(name, typeSolver, (Statement)this.wrappedNode);
               }

               return this.getParent().solveSymbol(name, typeSolver);
            }

            expr = (Expression)var3.next();
         } while(!(expr instanceof VariableDeclarationExpr));

         Iterator var5 = ((VariableDeclarationExpr)expr).getVariables().iterator();

         while(var5.hasNext()) {
            VariableDeclarator v = (VariableDeclarator)var5.next();
            if (v.getName().getIdentifier().equals(name)) {
               return SymbolReference.solved(JavaParserSymbolDeclaration.localVar(v, typeSolver));
            }
         }
      }
   }

   public SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> argumentsTypes, boolean staticOnly, TypeSolver typeSolver) {
      return this.getParent().solveMethod(name, argumentsTypes, false, typeSolver);
   }

   public List<VariableDeclarator> localVariablesExposedToChild(Node child) {
      NodeList<Expression> resources = ((TryStmt)this.wrappedNode).getResources();

      for(int i = 0; i < resources.size(); ++i) {
         if (child == resources.get(i)) {
            return (List)resources.subList(0, i).stream().map((e) -> {
               return (List)(e instanceof VariableDeclarationExpr ? ((VariableDeclarationExpr)e).getVariables() : Collections.emptyList());
            }).flatMap(Collection::stream).collect(Collectors.toList());
         }
      }

      if (child == ((TryStmt)this.wrappedNode).getTryBlock()) {
         List<VariableDeclarator> res = new LinkedList();
         Iterator var4 = resources.iterator();

         while(var4.hasNext()) {
            Expression expr = (Expression)var4.next();
            if (expr instanceof VariableDeclarationExpr) {
               res.addAll(((VariableDeclarationExpr)expr).getVariables());
            }
         }

         return res;
      } else {
         return Collections.emptyList();
      }
   }
}
