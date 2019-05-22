package soot.toolkits.astmetrics;

import java.util.Iterator;
import polyglot.ast.ClassDecl;
import polyglot.ast.Node;
import polyglot.util.CodeWriter;
import polyglot.visit.NodeVisitor;
import soot.G;

public abstract class ASTMetric extends NodeVisitor implements MetricInterface {
   Node astNode;
   String className = null;

   public ASTMetric(Node astNode) {
      this.astNode = astNode;
      this.reset();
   }

   public final NodeVisitor enter(Node n) {
      if (n instanceof ClassDecl) {
         this.className = ((ClassDecl)n).name();
         System.out.println("Starting processing: " + this.className);
      }

      return this;
   }

   public final Node leave(Node parent, Node old, Node n, NodeVisitor v) {
      if (n instanceof ClassDecl) {
         if (this.className == null) {
            throw new RuntimeException("className is null");
         }

         System.out.println("Done with class " + this.className);
         ClassData data = this.getClassData();
         this.addMetrics(data);
         this.reset();
      }

      return this.leave(old, n, v);
   }

   public abstract void reset();

   public abstract void addMetrics(ClassData var1);

   public final void execute() {
      this.astNode.visit(this);
      System.out.println("\n\n\n PRETTY P{RINTING");
      if (this instanceof StmtSumWeightedByDepth) {
         metricPrettyPrinter p = new metricPrettyPrinter(this);
         p.printAst(this.astNode, new CodeWriter(System.out, 80));
      }

   }

   public void printAstMetric(Node n, CodeWriter w) {
   }

   public final ClassData getClassData() {
      if (this.className == null) {
         throw new RuntimeException("className is null");
      } else {
         Iterator it = G.v().ASTMetricsData.iterator();

         ClassData tempData;
         do {
            if (!it.hasNext()) {
               tempData = new ClassData(this.className);
               G.v().ASTMetricsData.add(tempData);
               return tempData;
            }

            tempData = (ClassData)it.next();
         } while(!tempData.classNameEquals(this.className));

         return tempData;
      }
   }
}
