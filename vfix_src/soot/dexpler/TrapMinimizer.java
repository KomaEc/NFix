package soot.dexpler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.G;
import soot.Singletons;
import soot.Trap;
import soot.Unit;
import soot.jimple.Jimple;
import soot.options.Options;
import soot.toolkits.exceptions.TrapTransformer;
import soot.toolkits.graph.ExceptionalGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;

public class TrapMinimizer extends TrapTransformer {
   public TrapMinimizer(Singletons.Global g) {
   }

   public static TrapMinimizer v() {
      return G.v().soot_dexpler_TrapMinimizer();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      if (b.getTraps().size() != 0) {
         ExceptionalUnitGraph eug = new ExceptionalUnitGraph(b, DalvikThrowAnalysis.v(), Options.v().omit_excepting_unit_edges());
         Set<Unit> unitsWithMonitor = this.getUnitsWithMonitor(eug);
         Map<Trap, List<Trap>> replaceTrapBy = new HashMap(b.getTraps().size());
         boolean updateTrap = false;
         Iterator var8 = b.getTraps().iterator();

         Trap tr;
         while(var8.hasNext()) {
            tr = (Trap)var8.next();
            List<Trap> newTraps = new ArrayList();
            Unit firstTrapStmt = tr.getBeginUnit();
            boolean goesToHandler = false;
            updateTrap = false;

            for(Unit u = tr.getBeginUnit(); u != tr.getEndUnit(); u = b.getUnits().getSuccOf(u)) {
               if (goesToHandler) {
                  goesToHandler = false;
               } else {
                  firstTrapStmt = u;
               }

               if (tr.getException().getName().equals("java.lang.Throwable") && unitsWithMonitor.contains(u)) {
                  goesToHandler = true;
               }

               if (!goesToHandler && DalvikThrowAnalysis.v().mightThrow(u).catchableAs(tr.getException().getType())) {
                  Iterator var14 = eug.getExceptionDests(u).iterator();

                  while(var14.hasNext()) {
                     ExceptionalGraph.ExceptionDest<Unit> ed = (ExceptionalGraph.ExceptionDest)var14.next();
                     if (ed.getTrap() == tr) {
                        goesToHandler = true;
                        break;
                     }
                  }
               }

               Trap t;
               if (!goesToHandler) {
                  updateTrap = true;
                  if (firstTrapStmt != u) {
                     t = Jimple.v().newTrap(tr.getException(), firstTrapStmt, u, tr.getHandlerUnit());
                     newTraps.add(t);
                  }
               } else if (b.getUnits().getSuccOf(u) == tr.getEndUnit() && updateTrap) {
                  t = Jimple.v().newTrap(tr.getException(), firstTrapStmt, tr.getEndUnit(), tr.getHandlerUnit());
                  newTraps.add(t);
               }
            }

            if (updateTrap) {
               replaceTrapBy.put(tr, newTraps);
            }
         }

         var8 = replaceTrapBy.keySet().iterator();

         while(var8.hasNext()) {
            tr = (Trap)var8.next();
            b.getTraps().insertAfter((List)((List)replaceTrapBy.get(tr)), tr);
            b.getTraps().remove(tr);
         }

      }
   }
}
