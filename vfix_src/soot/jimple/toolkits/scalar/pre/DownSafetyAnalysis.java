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
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.BoundedFlowSet;
import soot.toolkits.scalar.CollectionFlowUniverse;
import soot.toolkits.scalar.FlowSet;

public class DownSafetyAnalysis extends BackwardFlowAnalysis<Unit, FlowSet<EquivalentValue>> {
   private SideEffectTester sideEffect;
   private Map<Unit, EquivalentValue> unitToGenerateMap;
   private BoundedFlowSet<EquivalentValue> set;

   public DownSafetyAnalysis(DirectedGraph<Unit> dg) {
      super(dg);
      this.sideEffect = null;
      throw new RuntimeException("Don't use this Constructor!");
   }

   public DownSafetyAnalysis(DirectedGraph<Unit> dg, Map<Unit, EquivalentValue> unitToGen, SideEffectTester sideEffect) {
      this(dg, unitToGen, sideEffect, new ArrayPackedSet(new CollectionFlowUniverse(unitToGen.values())));
   }

   public DownSafetyAnalysis(DirectedGraph<Unit> dg, Map<Unit, EquivalentValue> unitToGen, SideEffectTester sideEffect, BoundedFlowSet<EquivalentValue> set) {
      super(dg);
      this.sideEffect = null;
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
               Iterator usesIt = avail.getUseBoxes().iterator();

               while(usesIt.hasNext()) {
                  Value use = ((ValueBox)usesIt.next()).getValue();
                  if (this.sideEffect.unitCanWriteTo(u, use)) {
                     outIt.remove();
                     break;
                  }
               }
            }
         }

         EquivalentValue add = (EquivalentValue)this.unitToGenerateMap.get(u);
         if (add != null) {
            out.add(add, out);
         }

         return;
      }
   }

   protected void merge(FlowSet<EquivalentValue> in1, FlowSet<EquivalentValue> in2, FlowSet<EquivalentValue> out) {
      in1.intersection(in2, out);
   }

   protected void copy(FlowSet<EquivalentValue> source, FlowSet<EquivalentValue> dest) {
      source.copy(dest);
   }
}
