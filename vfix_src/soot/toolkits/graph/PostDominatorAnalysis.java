package soot.toolkits.graph;

import java.util.Iterator;
import java.util.List;
import soot.Unit;
import soot.jimple.Stmt;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;

/** @deprecated */
@Deprecated
public class PostDominatorAnalysis extends BackwardFlowAnalysis<Unit, FlowSet<Unit>> {
   private UnitGraph g;
   private FlowSet<Unit> allNodes;

   public PostDominatorAnalysis(UnitGraph g) {
      super(g);
      this.g = g;
      this.initAllNodes();
      this.doAnalysis();
   }

   private void initAllNodes() {
      this.allNodes = new ArraySparseSet();
      Iterator it = this.g.iterator();

      while(it.hasNext()) {
         this.allNodes.add(it.next());
      }

   }

   protected void merge(FlowSet<Unit> in1, FlowSet<Unit> in2, FlowSet<Unit> out) {
      in1.intersection(in2, out);
   }

   protected void copy(FlowSet<Unit> source, FlowSet<Unit> dest) {
      source.copy(dest);
   }

   protected void flowThrough(FlowSet<Unit> in, Unit s, FlowSet<Unit> out) {
      if (this.isUnitEndNode(s)) {
         out.clear();
         out.add(s);
      } else {
         Iterator succsIt = this.g.getSuccsOf(s).iterator();

         while(succsIt.hasNext()) {
            Unit succ = (Unit)succsIt.next();
            FlowSet<Unit> next = (FlowSet)this.getFlowBefore(succ);
            in.intersection(next, in);
         }

         out.intersection(in, out);
         out.add(s);
      }

   }

   private boolean isUnitEndNode(Unit s) {
      return this.g.getTails().contains(s);
   }

   protected FlowSet<Unit> entryInitialFlow() {
      FlowSet<Unit> fs = new ArraySparseSet();
      List<Unit> tails = this.g.getTails();
      fs.add(tails.get(0));
      return fs;
   }

   protected FlowSet<Unit> newInitialFlow() {
      return this.allNodes.clone();
   }

   public boolean postDominates(Stmt s, Stmt t) {
      return ((FlowSet)this.getFlowBefore(t)).contains(s);
   }
}
