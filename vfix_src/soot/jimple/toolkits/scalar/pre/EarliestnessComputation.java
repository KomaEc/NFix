package soot.jimple.toolkits.scalar.pre;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.EquivalentValue;
import soot.SideEffectTester;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.FieldRef;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;

public class EarliestnessComputation {
   private Map<Unit, FlowSet<EquivalentValue>> unitToEarliest;

   public EarliestnessComputation(UnitGraph unitGraph, UpSafetyAnalysis upSafe, DownSafetyAnalysis downSafe, SideEffectTester sideEffect) {
      this(unitGraph, upSafe, downSafe, sideEffect, new ArraySparseSet());
   }

   public EarliestnessComputation(UnitGraph unitGraph, UpSafetyAnalysis upSafe, DownSafetyAnalysis downSafe, SideEffectTester sideEffect, FlowSet<EquivalentValue> set) {
      this.unitToEarliest = new HashMap(unitGraph.size() + 1, 0.7F);
      Iterator var6 = unitGraph.iterator();

      while(true) {
         while(var6.hasNext()) {
            Unit currentUnit = (Unit)var6.next();
            FlowSet<EquivalentValue> earliest = set.emptySet();
            this.unitToEarliest.put(currentUnit, earliest);
            FlowSet<EquivalentValue> downSafeSet = ((FlowSet)downSafe.getFlowBefore(currentUnit)).clone();
            List<Unit> predList = unitGraph.getPredsOf(currentUnit);
            if (predList.isEmpty()) {
               earliest.union(downSafeSet);
            } else {
               Iterator var11 = predList.iterator();

               while(var11.hasNext()) {
                  Unit predecessor = (Unit)var11.next();
                  Iterator downSafeIt = downSafeSet.iterator();

                  while(true) {
                     EquivalentValue equiVal;
                     while(downSafeIt.hasNext()) {
                        equiVal = (EquivalentValue)downSafeIt.next();
                        Value avail = equiVal.getValue();
                        if (avail instanceof FieldRef) {
                           if (sideEffect.unitCanWriteTo(predecessor, avail)) {
                              earliest.add(equiVal);
                              downSafeIt.remove();
                           }
                        } else {
                           Iterator var16 = avail.getUseBoxes().iterator();

                           while(var16.hasNext()) {
                              ValueBox useBox = (ValueBox)var16.next();
                              Value use = useBox.getValue();
                              if (sideEffect.unitCanWriteTo(predecessor, use)) {
                                 earliest.add(equiVal);
                                 downSafeIt.remove();
                                 break;
                              }
                           }
                        }
                     }

                     downSafeIt = downSafeSet.iterator();

                     while(downSafeIt.hasNext()) {
                        equiVal = (EquivalentValue)downSafeIt.next();
                        FlowSet<EquivalentValue> preDown = (FlowSet)downSafe.getFlowBefore(predecessor);
                        FlowSet<EquivalentValue> preUp = (FlowSet)upSafe.getFlowBefore(predecessor);
                        if (!preDown.contains(equiVal) && !preUp.contains(equiVal)) {
                           earliest.add(equiVal);
                           downSafeIt.remove();
                        }
                     }
                     break;
                  }
               }
            }
         }

         return;
      }
   }

   public FlowSet<EquivalentValue> getFlowBefore(Object node) {
      return (FlowSet)this.unitToEarliest.get(node);
   }
}
