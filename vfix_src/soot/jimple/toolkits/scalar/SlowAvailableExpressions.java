package soot.jimple.toolkits.scalar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.EquivalentValue;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.Stmt;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.UnitValueBoxPair;
import soot.util.Chain;
import soot.util.HashChain;

public class SlowAvailableExpressions implements AvailableExpressions {
   Map<Unit, List<UnitValueBoxPair>> unitToPairsAfter;
   Map<Unit, List<UnitValueBoxPair>> unitToPairsBefore;
   Map<Unit, Chain<EquivalentValue>> unitToEquivsAfter;
   Map<Unit, Chain<EquivalentValue>> unitToEquivsBefore;

   public SlowAvailableExpressions(Body b) {
      SlowAvailableExpressionsAnalysis analysis = new SlowAvailableExpressionsAnalysis(new ExceptionalUnitGraph(b));
      this.unitToPairsAfter = new HashMap(b.getUnits().size() * 2 + 1, 0.7F);
      this.unitToPairsBefore = new HashMap(b.getUnits().size() * 2 + 1, 0.7F);
      this.unitToEquivsAfter = new HashMap(b.getUnits().size() * 2 + 1, 0.7F);
      this.unitToEquivsBefore = new HashMap(b.getUnits().size() * 2 + 1, 0.7F);
      Iterator var3 = b.getUnits().iterator();

      while(var3.hasNext()) {
         Unit s = (Unit)var3.next();
         FlowSet<Value> set = (FlowSet)analysis.getFlowBefore(s);
         List<UnitValueBoxPair> pairsBefore = new ArrayList();
         List<UnitValueBoxPair> pairsAfter = new ArrayList();
         Chain<EquivalentValue> equivsBefore = new HashChain();
         Chain<EquivalentValue> equivsAfter = new HashChain();
         Iterator var10 = set.iterator();

         Value v;
         Stmt containingStmt;
         UnitValueBoxPair p;
         EquivalentValue ev;
         while(var10.hasNext()) {
            v = (Value)var10.next();
            containingStmt = (Stmt)analysis.rhsToContainingStmt.get(v);
            p = new UnitValueBoxPair(containingStmt, ((AssignStmt)containingStmt).getRightOpBox());
            ev = new EquivalentValue(v);
            pairsBefore.add(p);
            if (!equivsBefore.contains(ev)) {
               equivsBefore.add(ev);
            }
         }

         this.unitToPairsBefore.put(s, pairsBefore);
         this.unitToEquivsBefore.put(s, equivsBefore);
         var10 = ((FlowSet)analysis.getFlowAfter(s)).iterator();

         while(var10.hasNext()) {
            v = (Value)var10.next();
            containingStmt = (Stmt)analysis.rhsToContainingStmt.get(v);
            p = new UnitValueBoxPair(containingStmt, ((AssignStmt)containingStmt).getRightOpBox());
            ev = new EquivalentValue(v);
            pairsAfter.add(p);
            if (!equivsAfter.contains(ev)) {
               equivsAfter.add(ev);
            }
         }

         this.unitToPairsAfter.put(s, pairsAfter);
         this.unitToEquivsAfter.put(s, equivsAfter);
      }

   }

   public List<UnitValueBoxPair> getAvailablePairsBefore(Unit u) {
      return (List)this.unitToPairsBefore.get(u);
   }

   public List<UnitValueBoxPair> getAvailablePairsAfter(Unit u) {
      return (List)this.unitToPairsAfter.get(u);
   }

   public Chain<EquivalentValue> getAvailableEquivsBefore(Unit u) {
      return (Chain)this.unitToEquivsBefore.get(u);
   }

   public Chain<EquivalentValue> getAvailableEquivsAfter(Unit u) {
      return (Chain)this.unitToEquivsAfter.get(u);
   }
}
