package corg.vfix.pg.java.visitor;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MethodCallExprVisitor extends VoidVisitorAdapter<JavaBox> {
   public static boolean found = false;

   public void visit(BlockStmt block, JavaBox javaBox) {
      if (!found) {
         super.visit((BlockStmt)block, javaBox);
      }
   }

   public void visit(MethodCallExpr expr, JavaBox javaBox) {
      if (!found) {
         if (expr.toString().equals(javaBox.methodCallExpr.toString())) {
            found = true;
            System.out.println("#Expr Found: " + expr);
         } else {
            super.visit((MethodCallExpr)expr, javaBox);
            if (this.hasSameBase(expr, javaBox.methodCallExpr)) {
               System.out.println("Expr1: " + expr);
               System.out.println("Expr2: " + javaBox.methodCallExpr);
               System.out.println("have the same base");
               if (this.hasSameName(expr, javaBox.methodCallExpr)) {
                  System.out.println("Expr1: " + expr);
                  System.out.println("Expr2: " + javaBox.methodCallExpr);
                  System.out.println("****have the same name");
                  if (this.hasSameArgTypes(expr, javaBox.methodCallExpr)) {
                     System.out.println("Expr1: " + expr);
                     System.out.println("Expr2: " + javaBox.methodCallExpr);
                     System.out.println("****have the same arg types");
                     System.out.println("MethodCallExpr Found: " + expr);
                     javaBox.methodCallExpr = expr;
                     found = true;
                  }
               }
            }
         }
      }
   }

   private boolean hasSameName(MethodCallExpr expr1, MethodCallExpr expr2) {
      return expr1.getName().equals(expr2.getName());
   }

   private boolean hasSameArgTypes(MethodCallExpr expr1, MethodCallExpr expr2) {
      if (!expr1.getTypeArguments().isPresent()) {
         return !expr2.getTypeArguments().isPresent();
      } else {
         NodeList<Type> argTypes1 = (NodeList)expr1.getTypeArguments().get();
         if (!expr2.getScope().isPresent()) {
            return false;
         } else {
            NodeList<Type> argTypes2 = (NodeList)expr2.getTypeArguments().get();
            if (argTypes1.size() != argTypes2.size()) {
               return false;
            } else {
               for(int i = 0; i < argTypes1.size(); ++i) {
                  if (!((Type)argTypes1.get(i)).equals(argTypes2.get(i))) {
                     return false;
                  }
               }

               return true;
            }
         }
      }
   }

   private boolean hasSameBase(MethodCallExpr expr1, MethodCallExpr expr2) {
      if (expr1.getScope().isPresent()) {
         Expression base1 = (Expression)expr1.getScope().get();
         if (expr2.getScope().isPresent()) {
            Expression base2 = (Expression)expr2.getScope().get();
            return base1.toString().equals(base2.toString());
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
