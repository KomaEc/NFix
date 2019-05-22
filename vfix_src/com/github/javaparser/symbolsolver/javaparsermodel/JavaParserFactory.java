package com.github.javaparser.symbolsolver.javaparsermodel;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntryStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.AnonymousClassDeclarationContext;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.BlockStmtContext;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.CatchClauseContext;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.ClassOrInterfaceDeclarationContext;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.CompilationUnitContext;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.ConstructorContext;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.EnumDeclarationContext;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.FieldAccessContext;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.ForStatementContext;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.ForechStatementContext;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.LambdaExprContext;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.MethodCallExprContext;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.MethodContext;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.ObjectCreationContext;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.StatementContext;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.SwitchEntryContext;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.TryWithResourceContext;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.VariableDeclarationExprContext;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.VariableDeclaratorContext;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserAnnotationDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserClassDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserEnumDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserInterfaceDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserTypeParameter;
import com.github.javaparser.symbolsolver.javaparsermodel.declarators.FieldSymbolDeclarator;
import com.github.javaparser.symbolsolver.javaparsermodel.declarators.NoSymbolDeclarator;
import com.github.javaparser.symbolsolver.javaparsermodel.declarators.ParameterSymbolDeclarator;
import com.github.javaparser.symbolsolver.javaparsermodel.declarators.VariableSymbolDeclarator;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.SymbolDeclarator;

public class JavaParserFactory {
   public static Context getContext(Node node, TypeSolver typeSolver) {
      if (node == null) {
         throw new NullPointerException("Node should not be null");
      } else if (node instanceof BlockStmt) {
         return new BlockStmtContext((BlockStmt)node, typeSolver);
      } else if (node instanceof CompilationUnit) {
         return new CompilationUnitContext((CompilationUnit)node, typeSolver);
      } else if (node instanceof ForeachStmt) {
         return new ForechStatementContext((ForeachStmt)node, typeSolver);
      } else if (node instanceof ForStmt) {
         return new ForStatementContext((ForStmt)node, typeSolver);
      } else if (node instanceof LambdaExpr) {
         return new LambdaExprContext((LambdaExpr)node, typeSolver);
      } else if (node instanceof MethodDeclaration) {
         return new MethodContext((MethodDeclaration)node, typeSolver);
      } else if (node instanceof ConstructorDeclaration) {
         return new ConstructorContext((ConstructorDeclaration)node, typeSolver);
      } else if (node instanceof ClassOrInterfaceDeclaration) {
         return new ClassOrInterfaceDeclarationContext((ClassOrInterfaceDeclaration)node, typeSolver);
      } else if (node instanceof MethodCallExpr) {
         return new MethodCallExprContext((MethodCallExpr)node, typeSolver);
      } else if (node instanceof EnumDeclaration) {
         return new EnumDeclarationContext((EnumDeclaration)node, typeSolver);
      } else if (node instanceof FieldAccessExpr) {
         return new FieldAccessContext((FieldAccessExpr)node, typeSolver);
      } else if (node instanceof SwitchEntryStmt) {
         return new SwitchEntryContext((SwitchEntryStmt)node, typeSolver);
      } else if (node instanceof TryStmt) {
         return new TryWithResourceContext((TryStmt)node, typeSolver);
      } else if (node instanceof Statement) {
         return new StatementContext((Statement)node, typeSolver);
      } else if (node instanceof CatchClause) {
         return new CatchClauseContext((CatchClause)node, typeSolver);
      } else if (node instanceof VariableDeclarator) {
         return new VariableDeclaratorContext((VariableDeclarator)node, typeSolver);
      } else if (node instanceof VariableDeclarationExpr) {
         return new VariableDeclarationExprContext((VariableDeclarationExpr)node, typeSolver);
      } else if (node instanceof ObjectCreationExpr && ((ObjectCreationExpr)node).getAnonymousClassBody().isPresent()) {
         return new AnonymousClassDeclarationContext((ObjectCreationExpr)node, typeSolver);
      } else if (node instanceof ObjectCreationExpr) {
         return new ObjectCreationContext((ObjectCreationExpr)node, typeSolver);
      } else {
         if (node instanceof NameExpr) {
            if (node.getParentNode().isPresent() && node.getParentNode().get() instanceof FieldAccessExpr && ((Node)node.getParentNode().get()).getParentNode().isPresent()) {
               return getContext((Node)((Node)node.getParentNode().get()).getParentNode().get(), typeSolver);
            }

            if (node.getParentNode().isPresent() && node.getParentNode().get() instanceof ObjectCreationExpr && ((Node)node.getParentNode().get()).getParentNode().isPresent()) {
               return getContext((Node)((Node)node.getParentNode().get()).getParentNode().get(), typeSolver);
            }
         }

         Node parentNode = Navigator.requireParentNode(node);
         if (!(parentNode instanceof ObjectCreationExpr) || node != ((ObjectCreationExpr)parentNode).getType() && !((ObjectCreationExpr)parentNode).getArguments().contains((Object)node)) {
            if (parentNode == null) {
               throw new IllegalStateException("The AST node does not appear to be inserted in a propert AST, therefore we cannot resolve symbols correctly");
            } else {
               return getContext(parentNode, typeSolver);
            }
         } else {
            return getContext(Navigator.requireParentNode(parentNode), typeSolver);
         }
      }
   }

   public static SymbolDeclarator getSymbolDeclarator(Node node, TypeSolver typeSolver) {
      if (node instanceof FieldDeclaration) {
         return new FieldSymbolDeclarator((FieldDeclaration)node, typeSolver);
      } else if (node instanceof Parameter) {
         return new ParameterSymbolDeclarator((Parameter)node, typeSolver);
      } else if (node instanceof ExpressionStmt) {
         ExpressionStmt expressionStmt = (ExpressionStmt)node;
         return (SymbolDeclarator)(expressionStmt.getExpression() instanceof VariableDeclarationExpr ? new VariableSymbolDeclarator((VariableDeclarationExpr)((VariableDeclarationExpr)expressionStmt.getExpression()), typeSolver) : new NoSymbolDeclarator(expressionStmt, typeSolver));
      } else if (node instanceof IfStmt) {
         return new NoSymbolDeclarator((IfStmt)node, typeSolver);
      } else if (node instanceof ForeachStmt) {
         ForeachStmt foreachStmt = (ForeachStmt)node;
         return new VariableSymbolDeclarator(foreachStmt.getVariable(), typeSolver);
      } else {
         return new NoSymbolDeclarator(node, typeSolver);
      }
   }

   public static ResolvedReferenceTypeDeclaration toTypeDeclaration(Node node, TypeSolver typeSolver) {
      if (node instanceof ClassOrInterfaceDeclaration) {
         return (ResolvedReferenceTypeDeclaration)(((ClassOrInterfaceDeclaration)node).isInterface() ? new JavaParserInterfaceDeclaration((ClassOrInterfaceDeclaration)node, typeSolver) : new JavaParserClassDeclaration((ClassOrInterfaceDeclaration)node, typeSolver));
      } else if (node instanceof TypeParameter) {
         return new JavaParserTypeParameter((TypeParameter)node, typeSolver);
      } else if (node instanceof EnumDeclaration) {
         return new JavaParserEnumDeclaration((EnumDeclaration)node, typeSolver);
      } else if (node instanceof AnnotationDeclaration) {
         return new JavaParserAnnotationDeclaration((AnnotationDeclaration)node, typeSolver);
      } else {
         throw new IllegalArgumentException(node.getClass().getCanonicalName());
      }
   }
}
