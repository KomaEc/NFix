package soot.toolkits.astmetrics;

import polyglot.ast.Node;
import polyglot.util.CodeWriter;
import polyglot.visit.PrettyPrinter;

public class metricPrettyPrinter extends PrettyPrinter {
   ASTMetric astMetric;

   public metricPrettyPrinter(ASTMetric astMetric) {
      this.astMetric = astMetric;
   }

   public void print(Node parent, Node child, CodeWriter w) {
      this.astMetric.printAstMetric(child, w);
      super.print(parent, child, w);
   }
}
