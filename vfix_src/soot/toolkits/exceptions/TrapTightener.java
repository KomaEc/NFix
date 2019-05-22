package soot.toolkits.exceptions;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.G;
import soot.Scene;
import soot.Singletons;
import soot.Trap;
import soot.Unit;
import soot.options.Options;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.util.Chain;

public final class TrapTightener extends TrapTransformer {
   private static final Logger logger = LoggerFactory.getLogger(TrapTightener.class);
   protected ThrowAnalysis throwAnalysis = null;

   public TrapTightener(Singletons.Global g) {
   }

   public static TrapTightener v() {
      return G.v().soot_toolkits_exceptions_TrapTightener();
   }

   public TrapTightener(ThrowAnalysis ta) {
      this.throwAnalysis = ta;
   }

   protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
      if (this.throwAnalysis == null) {
         this.throwAnalysis = Scene.v().getDefaultThrowAnalysis();
      }

      if (Options.v().verbose()) {
         logger.debug("[" + body.getMethod().getName() + "] Tightening trap boundaries...");
      }

      Chain<Trap> trapChain = body.getTraps();
      Chain<Unit> unitChain = body.getUnits();
      if (trapChain.size() > 0) {
         ExceptionalUnitGraph graph = new ExceptionalUnitGraph(body, this.throwAnalysis);
         Set<Unit> unitsWithMonitor = this.getUnitsWithMonitor(graph);
         Iterator trapIt = trapChain.iterator();

         while(trapIt.hasNext()) {
            Trap trap = (Trap)trapIt.next();
            boolean isCatchAll = trap.getException().getName().equals("java.lang.Throwable");
            Unit firstTrappedUnit = trap.getBeginUnit();
            Unit firstTrappedThrower = null;
            Unit firstUntrappedUnit = trap.getEndUnit();
            Unit lastTrappedUnit = (Unit)unitChain.getPredOf(firstUntrappedUnit);
            Unit lastTrappedThrower = null;

            Unit u;
            for(u = firstTrappedUnit; u != null && u != firstUntrappedUnit; u = (Unit)unitChain.getSuccOf(u)) {
               if (this.mightThrowTo(graph, u, trap)) {
                  firstTrappedThrower = u;
                  break;
               }

               if (isCatchAll && unitsWithMonitor.contains(u)) {
                  if (firstTrappedThrower == null) {
                     firstTrappedThrower = u;
                  }
                  break;
               }
            }

            if (firstTrappedThrower != null) {
               for(u = lastTrappedUnit; u != null; u = (Unit)unitChain.getPredOf(u)) {
                  if (this.mightThrowTo(graph, u, trap)) {
                     lastTrappedThrower = u;
                     break;
                  }

                  if (isCatchAll && unitsWithMonitor.contains(u)) {
                     lastTrappedThrower = u;
                     break;
                  }
               }
            }

            if (firstTrappedThrower == null) {
               trapIt.remove();
            } else {
               if (firstTrappedThrower != null && firstTrappedUnit != firstTrappedThrower) {
                  trap.setBeginUnit(firstTrappedThrower);
               }

               if (lastTrappedThrower == null) {
                  lastTrappedThrower = firstTrappedUnit;
               }

               if (lastTrappedUnit != lastTrappedThrower) {
                  trap.setEndUnit((Unit)unitChain.getSuccOf(lastTrappedThrower));
               }
            }
         }
      }

   }

   protected boolean mightThrowTo(ExceptionalUnitGraph g, Unit u, Trap t) {
      Iterator var4 = g.getExceptionDests(u).iterator();

      ExceptionalUnitGraph.ExceptionDest dest;
      do {
         if (!var4.hasNext()) {
            return false;
         }

         dest = (ExceptionalUnitGraph.ExceptionDest)var4.next();
      } while(dest.getTrap() != t);

      return true;
   }
}
