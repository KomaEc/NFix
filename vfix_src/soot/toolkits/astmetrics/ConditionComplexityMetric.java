package soot.toolkits.astmetrics;

import polyglot.ast.Binary;
import polyglot.ast.Expr;
import polyglot.ast.If;
import polyglot.ast.Loop;
import polyglot.ast.Node;
import polyglot.ast.Unary;
import polyglot.visit.NodeVisitor;

public class ConditionComplexityMetric extends ASTMetric {
   int loopComplexity;
   int ifComplexity;

   public ConditionComplexityMetric(Node node) {
      super(node);
   }

   public void reset() {
      this.loopComplexity = this.ifComplexity = 0;
   }

   public void addMetrics(ClassData data) {
      data.addMetric(new MetricData("Loop-Cond-Complexity", new Integer(this.loopComplexity)));
      data.addMetric(new MetricData("If-Cond-Complexity", new Integer(this.ifComplexity)));
      data.addMetric(new MetricData("Total-Cond-Complexity", new Integer(this.loopComplexity + this.ifComplexity)));
   }

   public NodeVisitor enter(Node parent, Node n) {
      Expr expr;
      if (n instanceof Loop) {
         expr = ((Loop)n).cond();
         this.loopComplexity = (int)((double)this.loopComplexity + this.condComplexity(expr));
      } else if (n instanceof If) {
         expr = ((If)n).cond();
         this.ifComplexity = (int)((double)this.ifComplexity + this.condComplexity(expr));
      }

      return this.enter(n);
   }

   private double condComplexity(Expr expr) {
      if (expr instanceof Binary) {
         Binary b = (Binary)expr;
         return b.operator() != Binary.COND_AND && b.operator() != Binary.COND_OR ? 0.5D + this.condComplexity(b.left()) + this.condComplexity(b.right()) : 1.0D + this.condComplexity(b.left()) + this.condComplexity(b.right());
      } else if (expr instanceof Unary) {
         return ((Unary)expr).operator() == Unary.NOT ? 0.5D + this.condComplexity(((Unary)expr).expr()) : this.condComplexity(((Unary)expr).expr());
      } else {
         return 1.0D;
      }
   }
}
