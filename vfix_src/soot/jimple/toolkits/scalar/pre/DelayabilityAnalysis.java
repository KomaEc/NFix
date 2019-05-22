package soot.jimple.toolkits.scalar.pre;

import java.util.Iterator;
import java.util.Map;
import soot.EquivalentValue;
import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArrayPackedSet;
import soot.toolkits.scalar.BoundedFlowSet;
import soot.toolkits.scalar.CollectionFlowUniverse;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;

public class DelayabilityAnalysis extends ForwardFlowAnalysis<Unit, FlowSet<EquivalentValue>> {
   private EarliestnessComputation earliest;
   private Map<Unit, EquivalentValue> unitToKillValue;
   private BoundedFlowSet<EquivalentValue> set;

   public DelayabilityAnalysis(DirectedGraph<Unit> dg) {
      super(dg);
      throw new RuntimeException("Don't use this Constructor!");
   }

   public DelayabilityAnalysis(DirectedGraph<Unit> dg, EarliestnessComputation earliest, Map<Unit, EquivalentValue> equivRhsMap) {
      this(dg, earliest, equivRhsMap, new ArrayPackedSet(new CollectionFlowUniverse(equivRhsMap.values())));
   }

   public DelayabilityAnalysis(DirectedGraph<Unit> dg, EarliestnessComputation earliest, Map<Unit, EquivalentValue> equivRhsMap, BoundedFlowSet<EquivalentValue> set) {
      super(dg);
      UnitGraph g = (UnitGraph)dg;
      this.set = set;
      this.unitToKillValue = equivRhsMap;
      this.earliest = earliest;
      this.doAnalysis();
      Iterator var6 = g.iterator();

      while(var6.hasNext()) {
         Unit currentUnit = (Unit)var6.next();
         FlowSet<EquivalentValue> beforeSet = (FlowSet)this.getFlowBefore(currentUnit);
         beforeSet.union(earliest.getFlowBefore(currentUnit));
      }

   }

   protected FlowSet<EquivalentValue> newInitialFlow() {
      return this.set.topSet();
   }

   protected FlowSet<EquivalentValue> entryInitialFlow() {
      return this.set.emptySet();
   }

   protected void flowThrough(FlowSet<EquivalentValue> in, Unit u, FlowSet<EquivalentValue> out) {
      in.copy(out);
      out.union(this.earliest.getFlowBefore(u));
      EquivalentValue equiVal = (EquivalentValue)this.unitToKillValue.get(u);
      if (equiVal != null) {
         out.remove(equiVal);
      }

   }

   protected void merge(FlowSet<EquivalentValue> inSet1, FlowSet<EquivalentValue> inSet2, FlowSet<EquivalentValue> outSet) {
      inSet1.intersection(inSet2, outSet);
   }

   protected void copy(FlowSet<EquivalentValue> sourceSet, FlowSet<EquivalentValue> destSet) {
      sourceSet.copy(destSet);
   }
}
