package soot.toolkits.astmetrics;

import polyglot.ast.Branch;
import polyglot.ast.Node;
import polyglot.visit.NodeVisitor;

public class AbruptEdgesMetric extends ASTMetric {
   private int iBreaks;
   private int eBreaks;
   private int iContinues;
   private int eContinues;

   public AbruptEdgesMetric(Node astNode) {
      super(astNode);
   }

   public void reset() {
      this.iBreaks = this.eBreaks = this.iContinues = this.eContinues = 0;
   }

   public void addMetrics(ClassData data) {
      data.addMetric(new MetricData("Total-breaks", new Integer(this.iBreaks + this.eBreaks)));
      data.addMetric(new MetricData("I-breaks", new Integer(this.iBreaks)));
      data.addMetric(new MetricData("E-breaks", new Integer(this.eBreaks)));
      data.addMetric(new MetricData("Total-continues", new Integer(this.iContinues + this.eContinues)));
      data.addMetric(new MetricData("I-continues", new Integer(this.iContinues)));
      data.addMetric(new MetricData("E-continues", new Integer(this.eContinues)));
      data.addMetric(new MetricData("Total-Abrupt", new Integer(this.iBreaks + this.eBreaks + this.iContinues + this.eContinues)));
   }

   public NodeVisitor enter(Node parent, Node n) {
      if (n instanceof Branch) {
         Branch branch = (Branch)n;
         if (branch.kind().equals(Branch.BREAK)) {
            if (branch.label() != null) {
               ++this.eBreaks;
            } else {
               ++this.iBreaks;
            }
         } else if (branch.kind().equals(Branch.CONTINUE)) {
            if (branch.label() != null) {
               ++this.eContinues;
            } else {
               ++this.iContinues;
            }
         } else {
            System.out.println("\t Error:'" + branch.toString() + "'");
         }
      }

      return this.enter(n);
   }
}
