package soot.jimple.toolkits.scalar;

import soot.SideEffectTester;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.FlowSet;

public class PessimisticAvailableExpressionsAnalysis extends SlowAvailableExpressionsAnalysis {
   public PessimisticAvailableExpressionsAnalysis(DirectedGraph<Unit> dg, SootMethod m, SideEffectTester st) {
      super(dg);
   }

   protected FlowSet<Value> newInitialFlow() {
      FlowSet<Value> newSet = this.emptySet.clone();
      return newSet;
   }
}
