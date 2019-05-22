package com.github.javaparser.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithStatements;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFactory;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.resolution.Value;
import com.github.javaparser.symbolsolver.resolution.SymbolDeclarator;
import java.util.List;
import java.util.Optional;

public class StatementContext<N extends Statement> extends AbstractJavaParserContext<N> {
   public StatementContext(N wrappedNode, TypeSolver typeSolver) {
      super(wrappedNode, typeSolver);
   }

   public static SymbolReference<? extends ResolvedValueDeclaration> solveInBlock(String name, TypeSolver typeSolver, Statement stmt) {
      if (!(Navigator.requireParentNode(stmt) instanceof NodeWithStatements)) {
         throw new IllegalArgumentException();
      } else {
         NodeWithStatements<?> blockStmt = (NodeWithStatements)Navigator.requireParentNode(stmt);
         int position = -1;

         int i;
         for(i = 0; i < blockStmt.getStatements().size(); ++i) {
            if (((Statement)blockStmt.getStatements().get(i)).equals(stmt)) {
               position = i;
            }
         }

         if (position == -1) {
            throw new RuntimeException();
         } else {
            for(i = position - 1; i >= 0; --i) {
               SymbolDeclarator symbolDeclarator = JavaParserFactory.getSymbolDeclarator(blockStmt.getStatements().get(i), typeSolver);
               SymbolReference<? extends ResolvedValueDeclaration> symbolReference = solveWith(symbolDeclarator, name);
               if (symbolReference.isSolved()) {
                  return symbolReference;
               }
            }

            return JavaParserFactory.getContext(Navigator.requireParentNode(stmt), typeSolver).solveSymbol(name, typeSolver);
         }
      }
   }

   public static Optional<Value> solveInBlockAsValue(String name, TypeSolver typeSolver, Statement stmt) {
      if (!(Navigator.requireParentNode(stmt) instanceof NodeWithStatements)) {
         throw new IllegalArgumentException();
      } else {
         NodeWithStatements<?> blockStmt = (NodeWithStatements)Navigator.requireParentNode(stmt);
         int position = -1;

         int i;
         for(i = 0; i < blockStmt.getStatements().size(); ++i) {
            if (((Statement)blockStmt.getStatements().get(i)).equals(stmt)) {
               position = i;
            }
         }

         if (position == -1) {
            throw new RuntimeException();
         } else {
            for(i = position - 1; i >= 0; --i) {
               SymbolDeclarator symbolDeclarator = JavaParserFactory.getSymbolDeclarator(blockStmt.getStatements().get(i), typeSolver);
               SymbolReference<? extends ResolvedValueDeclaration> symbolReference = solveWith(symbolDeclarator, name);
               if (symbolReference.isSolved()) {
                  return Optional.of(Value.from((ResolvedValueDeclaration)symbolReference.getCorrespondingDeclaration()));
               }
            }

            return JavaParserFactory.getContext(Navigator.requireParentNode(stmt), typeSolver).solveSymbolAsValue(name, typeSolver);
         }
      }
   }

   public Optional<Value> solveSymbolAsValue(String name, TypeSolver typeSolver) {
      SymbolDeclarator symbolDeclarator = JavaParserFactory.getSymbolDeclarator(this.wrappedNode, typeSolver);
      Optional<Value> symbolReference = this.solveWithAsValue(symbolDeclarator, name, typeSolver);
      if (symbolReference.isPresent()) {
         return symbolReference;
      } else if (Navigator.requireParentNode(this.wrappedNode) instanceof MethodDeclaration) {
         return this.getParent().solveSymbolAsValue(name, typeSolver);
      } else if (Navigator.requireParentNode(this.wrappedNode) instanceof LambdaExpr) {
         return this.getParent().solveSymbolAsValue(name, typeSolver);
      } else if (Navigator.requireParentNode(this.wrappedNode) instanceof IfStmt) {
         return this.getParent().solveSymbolAsValue(name, typeSolver);
      } else if (!(Navigator.requireParentNode(this.wrappedNode) instanceof NodeWithStatements)) {
         return this.getParent().solveSymbolAsValue(name, typeSolver);
      } else {
         NodeWithStatements<?> nodeWithStmt = (NodeWithStatements)Navigator.requireParentNode(this.wrappedNode);
         int position = -1;

         int i;
         for(i = 0; i < nodeWithStmt.getStatements().size(); ++i) {
            if (((Statement)nodeWithStmt.getStatements().get(i)).equals(this.wrappedNode)) {
               position = i;
            }
         }

         if (position == -1) {
            throw new RuntimeException();
         } else {
            for(i = position - 1; i >= 0; --i) {
               symbolDeclarator = JavaParserFactory.getSymbolDeclarator(nodeWithStmt.getStatements().get(i), typeSolver);
               symbolReference = this.solveWithAsValue(symbolDeclarator, name, typeSolver);
               if (symbolReference.isPresent()) {
                  return symbolReference;
               }
            }

            Context parentContext = this.getParent();
            return parentContext.solveSymbolAsValue(name, typeSolver);
         }
      }
   }

   public SymbolReference<? extends ResolvedValueDeclaration> solveSymbol(String name, TypeSolver typeSolver) {
      SymbolDeclarator symbolDeclarator = JavaParserFactory.getSymbolDeclarator(this.wrappedNode, typeSolver);
      SymbolReference<? extends ResolvedValueDeclaration> symbolReference = solveWith(symbolDeclarator, name);
      if (symbolReference.isSolved()) {
         return symbolReference;
      } else if (Navigator.requireParentNode(this.wrappedNode) instanceof MethodDeclaration) {
         return this.getParent().solveSymbol(name, typeSolver);
      } else if (Navigator.requireParentNode(this.wrappedNode) instanceof ConstructorDeclaration) {
         return this.getParent().solveSymbol(name, typeSolver);
      } else if (Navigator.requireParentNode(this.wrappedNode) instanceof LambdaExpr) {
         return this.getParent().solveSymbol(name, typeSolver);
      } else if (!(Navigator.requireParentNode(this.wrappedNode) instanceof NodeWithStatements)) {
         return this.getParent().solveSymbol(name, typeSolver);
      } else {
         NodeWithStatements<?> nodeWithStmt = (NodeWithStatements)Navigator.requireParentNode(this.wrappedNode);
         int position = -1;

         int i;
         for(i = 0; i < nodeWithStmt.getStatements().size(); ++i) {
            if (((Statement)nodeWithStmt.getStatements().get(i)).equals(this.wrappedNode)) {
               position = i;
            }
         }

         if (position == -1) {
            throw new RuntimeException();
         } else {
            for(i = position - 1; i >= 0; --i) {
               symbolDeclarator = JavaParserFactory.getSymbolDeclarator(nodeWithStmt.getStatements().get(i), typeSolver);
               symbolReference = solveWith(symbolDeclarator, name);
               if (symbolReference.isSolved()) {
                  return symbolReference;
               }
            }

            return this.getParent().solveSymbol(name, typeSolver);
         }
      }
   }

   public SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> argumentsTypes, boolean staticOnly, TypeSolver typeSolver) {
      return this.getParent().solveMethod(name, argumentsTypes, false, typeSolver);
   }

   public SymbolReference<ResolvedTypeDeclaration> solveType(String name, TypeSolver typeSolver) {
      return this.getParent().solveType(name, typeSolver);
   }
}
