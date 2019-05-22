package soot.jimple.toolkits.scalar.pre;

import java.util.Iterator;
import java.util.Map;
import soot.EquivalentValue;
import soot.SideEffectTester;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.FieldRef;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ArrayPackedSet;
import soot.toolkits.scalar.BoundedFlowSet;
import soot.toolkits.scalar.CollectionFlowUniverse;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;

public class UpSafetyAnalysis extends ForwardFlowAnalysis<Unit, FlowSet<EquivalentValue>> {
   private SideEffectTester sideEffect;
   private Map<Unit, EquivalentValue> unitToGenerateMap;
   private BoundedFlowSet<EquivalentValue> set;

   public UpSafetyAnalysis(DirectedGraph<Unit> dg) {
      super(dg);
      throw new RuntimeException("Don't use this Constructor!");
   }

   public UpSafetyAnalysis(DirectedGraph<Unit> dg, Map<Unit, EquivalentValue> unitToGen, SideEffectTester sideEffect) {
      this(dg, unitToGen, sideEffect, new ArrayPackedSet(new CollectionFlowUniverse(unitToGen.values())));
   }

   public UpSafetyAnalysis(DirectedGraph<Unit> dg, Map<Unit, EquivalentValue> unitToGen, SideEffectTester sideEffect, BoundedFlowSet<EquivalentValue> set) {
      super(dg);
      this.sideEffect = sideEffect;
      this.set = set;
      this.unitToGenerateMap = unitToGen;
      this.doAnalysis();
   }

   protected FlowSet<EquivalentValue> newInitialFlow() {
      return this.set.topSet();
   }

   protected FlowSet<EquivalentValue> entryInitialFlow() {
      return this.set.emptySet();
   }

   protected void flowThrough(FlowSet<EquivalentValue> in, Unit u, FlowSet<EquivalentValue> out) {
      in.copy(out);
      EquivalentValue add = (EquivalentValue)this.unitToGenerateMap.get(u);
      if (add != null) {
         out.add(add, out);
      }

      Iterator outIt = out.iterator();

      while(true) {
         while(outIt.hasNext()) {
            EquivalentValue equiVal = (EquivalentValue)outIt.next();
            Value avail = equiVal.getValue();
            if (avail instanceof FieldRef) {
               if (this.sideEffect.unitCanWriteTo(u, avail)) {
                  outIt.remove();
               }
            } else {
               Iterator var8 = avail.getUseBoxes().iterator();

               while(var8.hasNext()) {
                  ValueBox useBox = (ValueBox)var8.next();
                  Value use = useBox.getValue();
                  if (this.sideEffect.unitCanWriteTo(u, use)) {
                     outIt.remove();
                     break;
                  }
               }
            }
         }

         return;
      }
   }

   protected void merge(FlowSet<EquivalentValue> inSet1, FlowSet<EquivalentValue> inSet2, FlowSet<EquivalentValue> outSet) {
      inSet1.intersection(inSet2, outSet);
   }

   protected void copy(FlowSet<EquivalentValue> sourceSet, FlowSet<EquivalentValue> destSet) {
      sourceSet.copy(destSet);
   }
}
