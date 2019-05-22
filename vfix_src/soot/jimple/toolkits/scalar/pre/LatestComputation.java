package soot.jimple.toolkits.scalar.pre;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.EquivalentValue;
import soot.Unit;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArrayPackedSet;
import soot.toolkits.scalar.BoundedFlowSet;
import soot.toolkits.scalar.CollectionFlowUniverse;
import soot.toolkits.scalar.FlowSet;

public class LatestComputation {
   private Map<Unit, FlowSet<EquivalentValue>> unitToLatest;

   public LatestComputation(UnitGraph unitGraph, DelayabilityAnalysis delayed, Map<Unit, EquivalentValue> equivRhsMap) {
      this(unitGraph, delayed, equivRhsMap, new ArrayPackedSet(new CollectionFlowUniverse(equivRhsMap.values())));
   }

   public LatestComputation(UnitGraph unitGraph, DelayabilityAnalysis delayed, Map<Unit, EquivalentValue> equivRhsMap, BoundedFlowSet<EquivalentValue> set) {
      this.unitToLatest = new HashMap(unitGraph.size() + 1, 0.7F);
      Iterator var5 = unitGraph.iterator();

      while(var5.hasNext()) {
         Unit currentUnit = (Unit)var5.next();
         FlowSet<EquivalentValue> delaySet = (FlowSet)delayed.getFlowBefore(currentUnit);
         FlowSet<EquivalentValue> succCompSet = set.topSet();
         Iterator var9 = unitGraph.getSuccsOf(currentUnit).iterator();

         while(var9.hasNext()) {
            Unit successor = (Unit)var9.next();
            succCompSet.intersection((FlowSet)delayed.getFlowBefore(successor), succCompSet);
         }

         if (equivRhsMap.get(currentUnit) != null) {
            succCompSet.remove(equivRhsMap.get(currentUnit));
         }

         FlowSet<EquivalentValue> latest = delaySet.emptySet();
         delaySet.difference(succCompSet, latest);
         this.unitToLatest.put(currentUnit, latest);
      }

   }

   public FlowSet<EquivalentValue> getFlowBefore(Object node) {
      return (FlowSet)this.unitToLatest.get(node);
   }
}
