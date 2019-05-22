package soot.toolkits.graph;

import java.util.Iterator;
import java.util.List;
import soot.Unit;
import soot.jimple.Stmt;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;

/** @deprecated */
@Deprecated
public class DominatorAnalysis extends ForwardFlowAnalysis<Unit, FlowSet<Unit>> {
   private UnitGraph g;
   private FlowSet<Unit> allNodes;

   public DominatorAnalysis(UnitGraph g) {
      super(g);
      this.g = g;
      this.initAllNodes();
      this.doAnalysis();
   }

   private void initAllNodes() {
      this.allNodes = new ArraySparseSet();
      Iterator var1 = this.g.iterator();

      while(var1.hasNext()) {
         Unit u = (Unit)var1.next();
         this.allNodes.add(u);
      }

   }

   protected void merge(FlowSet<Unit> in1, FlowSet<Unit> in2, FlowSet<Unit> out) {
      in1.intersection(in2, out);
   }

   protected void copy(FlowSet<Unit> source, FlowSet<Unit> dest) {
      source.copy(dest);
   }

   protected void flowThrough(FlowSet<Unit> in, Unit s, FlowSet<Unit> out) {
      if (this.isUnitStartNode(s)) {
         out.clear();
         out.add(s);
      } else {
         Iterator var4 = this.g.getPredsOf(s).iterator();

         while(var4.hasNext()) {
            Unit pred = (Unit)var4.next();
            FlowSet<Unit> next = (FlowSet)this.getFlowAfter(pred);
            in.intersection(next, in);
         }

         out.intersection(in, out);
         out.add(s);
      }

   }

   private boolean isUnitStartNode(Unit s) {
      return s.equals(this.g.getHeads().get(0));
   }

   protected FlowSet<Unit> entryInitialFlow() {
      FlowSet<Unit> fs = new ArraySparseSet();
      List<Unit> heads = this.g.getHeads();
      if (heads.size() != 1) {
         throw new RuntimeException("Expect one start node only.");
      } else {
         fs.add(heads.get(0));
         return fs;
      }
   }

   protected FlowSet<Unit> newInitialFlow() {
      return this.allNodes.clone();
   }

   public boolean dominates(Stmt s, Stmt t) {
      return ((FlowSet)this.getFlowBefore(t)).contains(s);
   }
}
