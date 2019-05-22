package com.github.javaparser.symbolsolver.resolution.typeinference;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.UnknownType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.List;

public class ExpressionHelper {
   public static boolean isStandaloneExpression(Expression expression) {
      return !isPolyExpression(expression);
   }

   public static boolean isPolyExpression(Expression expression) {
      if (expression instanceof EnclosedExpr) {
         throw new UnsupportedOperationException(expression.toString());
      } else if (expression instanceof ObjectCreationExpr) {
         ObjectCreationExpr objectCreationExpr = (ObjectCreationExpr)expression;
         if (objectCreationExpr.isUsingDiamondOperator()) {
            throw new UnsupportedOperationException(expression.toString());
         } else {
            return false;
         }
      } else if (expression instanceof MethodCallExpr) {
         MethodCallExpr methodCallExpr = (MethodCallExpr)expression;
         if (appearsInAssignmentContext(expression) && !appearsInInvocationContext(expression)) {
            if (isQualified(methodCallExpr) && !elidesTypeArguments(methodCallExpr)) {
               return false;
            } else {
               throw new UnsupportedOperationException(expression.toString());
            }
         } else {
            return false;
         }
      } else if (expression instanceof MethodReferenceExpr) {
         throw new UnsupportedOperationException(expression.toString());
      } else if (expression instanceof ConditionalExpr) {
         throw new UnsupportedOperationException(expression.toString());
      } else {
         return expression instanceof LambdaExpr;
      }
   }

   private static boolean elidesTypeArguments(MethodCallExpr methodCall) {
      throw new UnsupportedOperationException();
   }

   private static boolean isQualified(MethodCallExpr methodCall) {
      throw new UnsupportedOperationException();
   }

   private static boolean appearsInAssignmentContext(Expression expression) {
      if (expression.getParentNode().isPresent()) {
         Node parent = (Node)expression.getParentNode().get();
         if (parent instanceof ExpressionStmt) {
            return false;
         } else if (parent instanceof MethodCallExpr) {
            return false;
         } else if (parent instanceof ReturnStmt) {
            return false;
         } else {
            throw new UnsupportedOperationException(parent.getClass().getCanonicalName());
         }
      } else {
         return false;
      }
   }

   private static boolean appearsInInvocationContext(Expression expression) {
      if (expression.getParentNode().isPresent()) {
         Node parent = (Node)expression.getParentNode().get();
         if (parent instanceof ExpressionStmt) {
            return false;
         } else if (parent instanceof MethodCallExpr) {
            return true;
         } else {
            throw new UnsupportedOperationException(parent.getClass().getCanonicalName());
         }
      } else {
         return false;
      }
   }

   public static boolean isExplicitlyTyped(LambdaExpr lambdaExpr) {
      return lambdaExpr.getParameters().stream().allMatch((p) -> {
         return !(p.getType() instanceof UnknownType);
      });
   }

   public static List<Expression> getResultExpressions(BlockStmt blockStmt) {
      throw new UnsupportedOperationException();
   }

   public static boolean isCompatibleInAssignmentContext(Expression expression, ResolvedType type, TypeSolver typeSolver) {
      return type.isAssignableBy(JavaParserFacade.get(typeSolver).getType(expression, false));
   }
}
