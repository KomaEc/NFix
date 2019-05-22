package soot.toolkits.scalar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.Local;
import soot.Unit;
import soot.ValueBox;
import soot.toolkits.graph.DominatorsFinder;
import soot.toolkits.graph.MHGDominatorsFinder;
import soot.toolkits.graph.UnitGraph;

class GuaranteedDefsAnalysis extends ForwardFlowAnalysis {
   FlowSet emptySet = new ArraySparseSet();
   Map<Unit, FlowSet> unitToGenerateSet;

   GuaranteedDefsAnalysis(UnitGraph graph) {
      super(graph);
      DominatorsFinder df = new MHGDominatorsFinder(graph);
      this.unitToGenerateSet = new HashMap(graph.size() * 2 + 1, 0.7F);
      Iterator unitIt = graph.iterator();

      while(unitIt.hasNext()) {
         Unit s = (Unit)unitIt.next();
         FlowSet genSet = this.emptySet.clone();
         Iterator domsIt = df.getDominators(s).iterator();

         while(domsIt.hasNext()) {
            Unit dom = (Unit)domsIt.next();
            Iterator boxIt = dom.getDefBoxes().iterator();

            while(boxIt.hasNext()) {
               ValueBox box = (ValueBox)boxIt.next();
               if (box.getValue() instanceof Local) {
                  genSet.add(box.getValue(), genSet);
               }
            }
         }

         this.unitToGenerateSet.put(s, genSet);
      }

      this.doAnalysis();
   }

   protected Object newInitialFlow() {
      return this.emptySet.clone();
   }

   protected Object entryInitialFlow() {
      return this.emptySet.clone();
   }

   protected void flowThrough(Object inValue, Object unit, Object outValue) {
      FlowSet in = (FlowSet)inValue;
      FlowSet out = (FlowSet)outValue;
      in.union((FlowSet)this.unitToGenerateSet.get(unit), out);
   }

   protected void merge(Object in1, Object in2, Object out) {
      FlowSet inSet1 = (FlowSet)in1;
      FlowSet inSet2 = (FlowSet)in2;
      FlowSet outSet = (FlowSet)out;
      inSet1.intersection(inSet2, outSet);
   }

   protected void copy(Object source, Object dest) {
      FlowSet sourceSet = (FlowSet)source;
      FlowSet destSet = (FlowSet)dest;
      sourceSet.copy(destSet);
   }
}
