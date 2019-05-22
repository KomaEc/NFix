package soot.toolkits.astmetrics;

import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.visit.NodeVisitor;

public class NumLocalsMetric extends ASTMetric {
   int numLocals;

   public NumLocalsMetric(Node node) {
      super(node);
   }

   public void reset() {
      this.numLocals = 0;
   }

   public void addMetrics(ClassData data) {
      data.addMetric(new MetricData("Number-Locals", new Integer(this.numLocals)));
   }

   public NodeVisitor enter(Node parent, Node n) {
      if (n instanceof LocalDecl) {
         ++this.numLocals;
      }

      return this.enter(n);
   }
}
