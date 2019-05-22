package soot.jimple.toolkits.scalar.pre;

import java.util.Map;
import soot.EquivalentValue;
import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ArrayPackedSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.BoundedFlowSet;
import soot.toolkits.scalar.CollectionFlowUniverse;
import soot.toolkits.scalar.FlowSet;

public class NotIsolatedAnalysis extends BackwardFlowAnalysis<Unit, FlowSet<EquivalentValue>> {
   private LatestComputation unitToLatest;
   private Map<Unit, EquivalentValue> unitToGen;
   private FlowSet<EquivalentValue> set;

   public NotIsolatedAnalysis(DirectedGraph<Unit> dg, LatestComputation latest, Map<Unit, EquivalentValue> equivRhsMap) {
      this(dg, latest, equivRhsMap, new ArrayPackedSet(new CollectionFlowUniverse(equivRhsMap.values())));
   }

   public NotIsolatedAnalysis(DirectedGraph<Unit> dg, LatestComputation latest, Map<Unit, EquivalentValue> equivRhsMap, BoundedFlowSet<EquivalentValue> set) {
      super(dg);
      this.set = set;
      this.unitToGen = equivRhsMap;
      this.unitToLatest = latest;
      this.doAnalysis();
   }

   protected FlowSet<EquivalentValue> newInitialFlow() {
      return this.set.emptySet();
   }

   protected FlowSet<EquivalentValue> entryInitialFlow() {
      return this.newInitialFlow();
   }

   protected void flowThrough(FlowSet<EquivalentValue> in, Unit unit, FlowSet<EquivalentValue> out) {
      in.copy(out);
      EquivalentValue rhs = (EquivalentValue)this.unitToGen.get(unit);
      if (rhs != null) {
         out.add(rhs);
      }

      FlowSet<EquivalentValue> latest = this.unitToLatest.getFlowBefore(unit);
      out.difference(latest);
   }

   protected void merge(FlowSet<EquivalentValue> inSet1, FlowSet<EquivalentValue> inSet2, FlowSet<EquivalentValue> outSet) {
      inSet1.union(inSet2, outSet);
   }

   protected void copy(FlowSet<EquivalentValue> sourceSet, FlowSet<EquivalentValue> destSet) {
      sourceSet.copy(destSet);
   }
}
