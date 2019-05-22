package soot.jimple.toolkits.annotation.purity;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootMethod;
import soot.SourceLocator;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.PseudoTopologicalOrderer;
import soot.util.dot.DotGraph;
import soot.util.dot.DotGraphEdge;
import soot.util.dot.DotGraphNode;

public abstract class AbstractInterproceduralAnalysis<S> {
   private static final Logger logger = LoggerFactory.getLogger(AbstractInterproceduralAnalysis.class);
   public static final boolean doCheck = false;
   protected final CallGraph cg;
   protected final DirectedGraph<SootMethod> dg;
   protected final Map<SootMethod, S> data;
   protected final Map<SootMethod, Integer> order;
   protected final Map<SootMethod, S> unanalysed;

   public AbstractInterproceduralAnalysis(CallGraph cg, SootMethodFilter filter, Iterator<SootMethod> heads, boolean verbose) {
      this.cg = cg;
      System.out.println("this.cg = " + System.identityHashCode(this.cg));
      this.dg = new DirectedCallGraph(cg, filter, heads, verbose);
      this.data = new HashMap();
      this.unanalysed = new HashMap();
      this.order = new HashMap();
      int i = 0;

      for(Iterator var6 = (new PseudoTopologicalOrderer()).newList(this.dg, true).iterator(); var6.hasNext(); ++i) {
         SootMethod m = (SootMethod)var6.next();
         this.order.put(m, i);
      }

   }

   protected abstract S newInitialSummary();

   protected abstract S summaryOfUnanalysedMethod(SootMethod var1);

   protected abstract void analyseMethod(SootMethod var1, S var2);

   protected abstract void applySummary(S var1, Stmt var2, S var3, S var4);

   protected abstract void merge(S var1, S var2, S var3);

   protected abstract void copy(S var1, S var2);

   protected void fillDotGraph(String prefix, S o, DotGraph out) {
      throw new Error("abstract function AbstractInterproceduralAnalysis.fillDotGraph called but not implemented.");
   }

   public void analyseCall(S src, Stmt callStmt, S dst) {
      S accum = this.newInitialSummary();
      this.copy(accum, dst);
      System.out.println("Edges out of " + callStmt + "...");
      Iterator it = this.cg.edgesOutOf((Unit)callStmt);

      while(it.hasNext()) {
         Edge edge = (Edge)it.next();
         SootMethod m = edge.tgt();
         System.out.println("\t-> " + m.getSignature());
         Object elem;
         if (this.data.containsKey(m)) {
            elem = this.data.get(m);
         } else {
            if (!this.unanalysed.containsKey(m)) {
               this.unanalysed.put(m, this.summaryOfUnanalysedMethod(m));
            }

            elem = this.unanalysed.get(m);
         }

         this.applySummary(src, callStmt, elem, accum);
         this.merge(dst, accum, dst);
      }

   }

   public void drawAsOneDot(String name) {
      DotGraph dot = new DotGraph(name);
      dot.setGraphLabel(name);
      dot.setGraphAttribute("compound", "true");
      int id = 0;
      Map<SootMethod, Integer> idmap = new HashMap();

      Iterator var5;
      SootMethod m;
      for(var5 = this.dg.iterator(); var5.hasNext(); ++id) {
         m = (SootMethod)var5.next();
         DotGraph sub = dot.createSubGraph("cluster" + id);
         DotGraphNode label = sub.drawNode("head" + id);
         idmap.put(m, id);
         sub.setGraphLabel("");
         label.setLabel("(" + this.order.get(m) + ") " + m.toString());
         label.setAttribute("fontsize", "18");
         label.setShape("box");
         if (this.data.containsKey(m)) {
            this.fillDotGraph("X" + id, this.data.get(m), sub);
         }
      }

      var5 = this.dg.iterator();

      while(var5.hasNext()) {
         m = (SootMethod)var5.next();
         Iterator var11 = this.dg.getSuccsOf(m).iterator();

         while(var11.hasNext()) {
            SootMethod mm = (SootMethod)var11.next();
            DotGraphEdge edge = dot.drawEdge("head" + idmap.get(m), "head" + idmap.get(mm));
            edge.setAttribute("ltail", "cluster" + idmap.get(m));
            edge.setAttribute("lhead", "cluster" + idmap.get(mm));
         }
      }

      File f = new File(SourceLocator.v().getOutputDir(), name + ".dot");
      dot.plot(f.getPath());
   }

   public void drawAsManyDot(String prefix, boolean drawUnanalysed) {
      Iterator var3 = this.data.keySet().iterator();

      SootMethod m;
      DotGraph dot;
      File f;
      while(var3.hasNext()) {
         m = (SootMethod)var3.next();
         dot = new DotGraph(m.toString());
         dot.setGraphLabel(m.toString());
         this.fillDotGraph("X", this.data.get(m), dot);
         f = new File(SourceLocator.v().getOutputDir(), prefix + m.toString() + ".dot");
         dot.plot(f.getPath());
      }

      if (drawUnanalysed) {
         var3 = this.unanalysed.keySet().iterator();

         while(var3.hasNext()) {
            m = (SootMethod)var3.next();
            dot = new DotGraph(m.toString());
            dot.setGraphLabel(m.toString() + " (unanalysed)");
            this.fillDotGraph("X", this.unanalysed.get(m), dot);
            f = new File(SourceLocator.v().getOutputDir(), prefix + m.toString() + "_u" + ".dot");
            dot.plot(f.getPath());
         }
      }

   }

   public S getSummaryFor(SootMethod m) {
      if (this.data.containsKey(m)) {
         return this.data.get(m);
      } else {
         return this.unanalysed.containsKey(m) ? this.unanalysed.get(m) : this.newInitialSummary();
      }
   }

   public Iterator<SootMethod> getAnalysedMethods() {
      return this.data.keySet().iterator();
   }

   protected void doAnalysis(boolean verbose) {
      class IntComparator implements Comparator<SootMethod> {
         public int compare(SootMethod o1, SootMethod o2) {
            return (Integer)AbstractInterproceduralAnalysis.this.order.get(o1) - (Integer)AbstractInterproceduralAnalysis.this.order.get(o2);
         }
      }

      SortedSet<SootMethod> queue = new TreeSet(new IntComparator());
      Iterator var3 = this.order.keySet().iterator();

      SootMethod m;
      while(var3.hasNext()) {
         m = (SootMethod)var3.next();
         this.data.put(m, this.newInitialSummary());
         queue.add(m);
      }

      HashMap nb = new HashMap();

      while(!queue.isEmpty()) {
         m = (SootMethod)queue.first();
         queue.remove(m);
         S newSummary = this.newInitialSummary();
         S oldSummary = this.data.get(m);
         if (nb.containsKey(m)) {
            nb.put(m, (Integer)nb.get(m) + 1);
         } else {
            nb.put(m, 1);
         }

         if (verbose) {
            logger.debug(" |- processing " + m.toString() + " (" + nb.get(m) + "-st time)");
         }

         this.analyseMethod(m, newSummary);
         if (!oldSummary.equals(newSummary)) {
            this.data.put(m, newSummary);
            queue.addAll(this.dg.getPredsOf(m));
         }
      }

   }
}
