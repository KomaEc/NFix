package soot.toolkits.astmetrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import polyglot.ast.Block;
import polyglot.ast.Branch;
import polyglot.ast.CodeDecl;
import polyglot.ast.Expr;
import polyglot.ast.Formal;
import polyglot.ast.If;
import polyglot.ast.Initializer;
import polyglot.ast.Labeled;
import polyglot.ast.LocalClassDecl;
import polyglot.ast.Loop;
import polyglot.ast.Node;
import polyglot.ast.ProcedureDecl;
import polyglot.ast.Stmt;
import polyglot.ast.Switch;
import polyglot.ast.Synchronized;
import polyglot.ast.Try;
import polyglot.util.CodeWriter;
import polyglot.visit.NodeVisitor;

public class StmtSumWeightedByDepth extends ASTMetric {
   int currentDepth;
   int sum;
   int maxDepth;
   int numNodes;
   Stack<ArrayList> labelNodesSoFar = new Stack();
   ArrayList<Node> blocksWithAbruptFlow = new ArrayList();
   HashMap<Node, Integer> stmtToMetric = new HashMap();
   HashMap<Node, Integer> stmtToMetricDepth = new HashMap();
   public static boolean tmpAbruptChecker = false;

   public StmtSumWeightedByDepth(Node node) {
      super(node);
   }

   public void printAstMetric(Node n, CodeWriter w) {
      if (n instanceof Stmt && this.stmtToMetric.containsKey(n)) {
         w.write(" // sum= " + this.stmtToMetric.get(n) + " : depth= " + this.stmtToMetricDepth.get(n) + "\t");
      }

   }

   public void reset() {
      this.currentDepth = 1;
      this.maxDepth = 1;
      this.sum = 0;
      this.numNodes = 0;
   }

   public void addMetrics(ClassData data) {
      data.addMetric(new MetricData("D-W-Complexity", new Double((double)this.sum)));
      data.addMetric(new MetricData("AST-Node-Count", new Integer(this.numNodes)));
   }

   private void increaseDepth() {
      System.out.println("Increasing depth");
      ++this.currentDepth;
      if (this.currentDepth > this.maxDepth) {
         this.maxDepth = this.currentDepth;
      }

   }

   private void decreaseDepth() {
      System.out.println("Decreasing depth");
      --this.currentDepth;
   }

   public NodeVisitor enter(Node parent, Node n) {
      ++this.numNodes;
      if (n instanceof CodeDecl) {
         this.labelNodesSoFar.push(new ArrayList());
      } else if (n instanceof Labeled) {
         ((ArrayList)this.labelNodesSoFar.peek()).add(((Labeled)n).label());
      }

      if (!(n instanceof If) && !(n instanceof Loop) && !(n instanceof Try) && !(n instanceof Switch) && !(n instanceof LocalClassDecl) && !(n instanceof Synchronized) && !(n instanceof ProcedureDecl) && !(n instanceof Initializer)) {
         if (parent instanceof Block && n instanceof Block) {
            tmpAbruptChecker = false;
            n.visit(new NodeVisitor() {
               public NodeVisitor enter(Node parent, Node node) {
                  if (node instanceof Branch) {
                     Branch b = (Branch)node;
                     if (b.label() != null && ((ArrayList)StmtSumWeightedByDepth.this.labelNodesSoFar.peek()).contains(b.label())) {
                        StmtSumWeightedByDepth.tmpAbruptChecker = true;
                     }
                  }

                  return this.enter(node);
               }

               public Node override(Node parent, Node node) {
                  return StmtSumWeightedByDepth.tmpAbruptChecker ? node : null;
               }
            });
            if (tmpAbruptChecker) {
               this.blocksWithAbruptFlow.add(n);
               this.sum += this.currentDepth * 2;
               System.out.println(n);
               this.increaseDepth();
            }
         } else if (n instanceof Expr || n instanceof Formal) {
            System.out.print(this.sum + "  " + n + "  ");
            this.sum += this.currentDepth * 2;
            System.out.println(this.sum);
         }
      } else {
         this.sum += this.currentDepth * 2;
         System.out.println(n);
         this.increaseDepth();
      }

      if (n instanceof Stmt) {
         this.stmtToMetric.put(n, new Integer(this.sum));
         this.stmtToMetricDepth.put(n, new Integer(this.currentDepth));
      }

      return this.enter(n);
   }

   public Node leave(Node old, Node n, NodeVisitor v) {
      if (n instanceof CodeDecl) {
         this.labelNodesSoFar.pop();
      }

      if (!(n instanceof If) && !(n instanceof Loop) && !(n instanceof Try) && !(n instanceof Switch) && !(n instanceof LocalClassDecl) && !(n instanceof Synchronized) && !(n instanceof ProcedureDecl) && !(n instanceof Initializer)) {
         if (n instanceof Block && this.blocksWithAbruptFlow.contains(n)) {
            this.decreaseDepth();
         }
      } else {
         this.decreaseDepth();
      }

      return n;
   }
}
