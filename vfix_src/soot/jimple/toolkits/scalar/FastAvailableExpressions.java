package soot.jimple.toolkits.scalar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.EquivalentValue;
import soot.SideEffectTester;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.UnitValueBoxPair;
import soot.util.Chain;
import soot.util.HashChain;

public class FastAvailableExpressions implements AvailableExpressions {
   private static final Logger logger = LoggerFactory.getLogger(FastAvailableExpressions.class);
   Map<Unit, List<UnitValueBoxPair>> unitToPairsAfter;
   Map<Unit, List<UnitValueBoxPair>> unitToPairsBefore;
   Map<Unit, Chain<EquivalentValue>> unitToEquivsAfter;
   Map<Unit, Chain<EquivalentValue>> unitToEquivsBefore;

   public FastAvailableExpressions(Body b, SideEffectTester st) {
      if (Options.v().verbose()) {
         logger.debug("[" + b.getMethod().getName() + "] Finding available expressions...");
      }

      FastAvailableExpressionsAnalysis analysis = new FastAvailableExpressionsAnalysis(new ExceptionalUnitGraph(b), b.getMethod(), st);
      this.unitToPairsAfter = new HashMap(b.getUnits().size() * 2 + 1, 0.7F);
      this.unitToPairsBefore = new HashMap(b.getUnits().size() * 2 + 1, 0.7F);
      this.unitToEquivsAfter = new HashMap(b.getUnits().size() * 2 + 1, 0.7F);
      this.unitToEquivsBefore = new HashMap(b.getUnits().size() * 2 + 1, 0.7F);
      Iterator var4 = b.getUnits().iterator();

      while(var4.hasNext()) {
         Unit s = (Unit)var4.next();
         FlowSet<Value> set = (FlowSet)analysis.getFlowBefore(s);
         List<UnitValueBoxPair> pairsBefore = new ArrayList();
         List<UnitValueBoxPair> pairsAfter = new ArrayList();
         Chain<EquivalentValue> equivsBefore = new HashChain();
         Chain<EquivalentValue> equivsAfter = new HashChain();
         if (set instanceof ToppedSet && ((ToppedSet)set).isTop()) {
            throw new RuntimeException("top! on " + s);
         }

         Iterator var11 = set.iterator();

         Value v;
         Stmt containingStmt;
         UnitValueBoxPair p;
         EquivalentValue ev;
         while(var11.hasNext()) {
            v = (Value)var11.next();
            containingStmt = (Stmt)analysis.rhsToContainingStmt.get(v);
            p = new UnitValueBoxPair(containingStmt, ((AssignStmt)containingStmt).getRightOpBox());
            pairsBefore.add(p);
            ev = new EquivalentValue(v);
            if (!equivsBefore.contains(ev)) {
               equivsBefore.add(ev);
            }
         }

         this.unitToPairsBefore.put(s, pairsBefore);
         this.unitToEquivsBefore.put(s, equivsBefore);
         var11 = ((FlowSet)analysis.getFlowAfter(s)).iterator();

         while(var11.hasNext()) {
            v = (Value)var11.next();
            containingStmt = (Stmt)analysis.rhsToContainingStmt.get(v);
            p = new UnitValueBoxPair(containingStmt, ((AssignStmt)containingStmt).getRightOpBox());
            pairsAfter.add(p);
            ev = new EquivalentValue(v);
            if (!equivsAfter.contains(ev)) {
               equivsAfter.add(ev);
            }
         }

         this.unitToPairsAfter.put(s, pairsAfter);
         this.unitToEquivsAfter.put(s, equivsAfter);
      }

      if (Options.v().verbose()) {
         logger.debug("[" + b.getMethod().getName() + "]     Found available expressions...");
      }

   }

   public List<UnitValueBoxPair> getAvailablePairsBefore(Unit u) {
      return (List)this.unitToPairsBefore.get(u);
   }

   public Chain<EquivalentValue> getAvailableEquivsBefore(Unit u) {
      return (Chain)this.unitToEquivsBefore.get(u);
   }

   public List<UnitValueBoxPair> getAvailablePairsAfter(Unit u) {
      return (List)this.unitToPairsAfter.get(u);
   }

   public Chain<EquivalentValue> getAvailableEquivsAfter(Unit u) {
      return (Chain)this.unitToEquivsAfter.get(u);
   }
}
