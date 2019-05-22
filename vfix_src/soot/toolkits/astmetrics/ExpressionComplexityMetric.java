package soot.toolkits.astmetrics;

import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.visit.NodeVisitor;

public class ExpressionComplexityMetric extends ASTMetric {
   int currentExprDepth;
   int exprDepthSum;
   int exprCount;
   int inExpr;

   public ExpressionComplexityMetric(Node node) {
      super(node);
   }

   public void reset() {
      this.currentExprDepth = 0;
      this.exprDepthSum = 0;
      this.exprCount = 0;
      this.inExpr = 0;
   }

   public void addMetrics(ClassData data) {
      double a = (double)this.exprDepthSum;
      double b = (double)this.exprCount;
      data.addMetric(new MetricData("Expr-Complexity", new Double(a)));
      data.addMetric(new MetricData("Expr-Count", new Double(b)));
   }

   public NodeVisitor enter(Node parent, Node n) {
      if (n instanceof Expr) {
         ++this.inExpr;
         ++this.currentExprDepth;
      }

      return this.enter(n);
   }

   public Node leave(Node old, Node n, NodeVisitor v) {
      if (n instanceof Expr) {
         if (this.currentExprDepth == 1) {
            ++this.exprCount;
            this.exprDepthSum += this.inExpr;
            this.inExpr = 0;
         }

         --this.currentExprDepth;
      }

      return super.leave(old, n, v);
   }
}
