package soot.jimple.toolkits.callgraph;

import java.util.Iterator;
import soot.MethodOrMethodContext;
import soot.Scene;
import soot.Unit;
import soot.jimple.Stmt;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;

public class ClinitElimAnalysis extends ForwardFlowAnalysis {
   private UnitGraph g;

   public ClinitElimAnalysis(UnitGraph g) {
      super(g);
      this.g = g;
      this.doAnalysis();
   }

   public void merge(Object in1, Object in2, Object out) {
      FlowSet inSet1 = (FlowSet)in1;
      FlowSet inSet2 = (FlowSet)in2;
      FlowSet outSet = (FlowSet)out;
      inSet1.intersection(inSet2, outSet);
   }

   public void copy(Object src, Object dest) {
      FlowSet srcIn = (FlowSet)src;
      FlowSet destOut = (FlowSet)dest;
      srcIn.copy(destOut);
   }

   protected void flowThrough(Object inVal, Object stmt, Object outVal) {
      FlowSet in = (FlowSet)inVal;
      FlowSet out = (FlowSet)outVal;
      Stmt s = (Stmt)stmt;
      in.copy(out);
      CallGraph cg = Scene.v().getCallGraph();
      Iterator edges = cg.edgesOutOf((Unit)s);

      while(edges.hasNext()) {
         Edge e = (Edge)edges.next();
         if (e.isClinit()) {
            out.add(e.tgt());
         }
      }

   }

   protected Object entryInitialFlow() {
      return new ArraySparseSet();
   }

   protected Object newInitialFlow() {
      ArraySparseSet set = new ArraySparseSet();
      CallGraph cg = Scene.v().getCallGraph();
      Iterator mIt = cg.edgesOutOf((MethodOrMethodContext)this.g.getBody().getMethod());

      while(mIt.hasNext()) {
         Edge edge = (Edge)mIt.next();
         if (edge.isClinit()) {
            set.add(edge.tgt());
         }
      }

      return set;
   }
}
