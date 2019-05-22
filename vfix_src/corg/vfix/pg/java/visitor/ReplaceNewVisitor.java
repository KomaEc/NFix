package corg.vfix.pg.java.visitor;

import com.github.javaparser.JavaParser;
import com.github.javaparser.Range;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.Iterator;

public class ReplaceNewVisitor extends VoidVisitorAdapter<JavaBox> {
   public void visit(FieldAccessExpr expr, JavaBox javaBox) {
      super.visit((FieldAccessExpr)expr, javaBox);
      Expression base = expr.getScope();
      if (base.toString().equals(javaBox.var)) {
         System.out.println("replace " + javaBox.var + " in " + expr.toString() + " with " + javaBox.stmt.toString());
         this.replace(expr.toString(), javaBox);
      }

   }

   public void visit(MethodCallExpr expr, JavaBox javaBox) {
      super.visit((MethodCallExpr)expr, javaBox);
      if (expr.getScope().isPresent()) {
         Expression base = (Expression)expr.getScope().get();
         NodeList<Expression> args = expr.getArguments();
         if (expr.getRange().isPresent()) {
            if (base.toString().equals(javaBox.var) && ((Range)expr.getRange().get()).begin.line == javaBox.line) {
               System.out.println("replace " + expr.toString() + " in " + javaBox.stmt.toString() + " with " + javaBox.expr);
               this.replace(expr.toString(), javaBox);
            } else {
               Iterator var6 = args.iterator();

               Expression arg;
               do {
                  if (!var6.hasNext()) {
                     return;
                  }

                  arg = (Expression)var6.next();
               } while(!arg.toString().equals(javaBox.var) || ((Range)expr.getRange().get()).begin.line != javaBox.line);

               System.out.println("replace " + expr.toString() + " in " + javaBox.stmt.toString() + " with " + javaBox.expr);
               this.replace(expr.toString(), javaBox);
            }
         }
      }
   }

   private void replace(String oldStr, JavaBox javaBox) {
      String str = javaBox.stmt.toString();
      String newStr = str.replace(oldStr, javaBox.expr);

      try {
         javaBox.outputStmt = JavaParser.parseStatement(newStr);
      } catch (Exception var6) {
         System.out.println("Cannot parse new Statement: ");
         System.out.println(newStr);
         System.out.println("Skip...");
      }

   }
}
