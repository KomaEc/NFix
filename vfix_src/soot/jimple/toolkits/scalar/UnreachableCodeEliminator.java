package soot.jimple.toolkits.scalar;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.PhaseOptions;
import soot.Scene;
import soot.Singletons;
import soot.Trap;
import soot.Unit;
import soot.options.Options;
import soot.toolkits.exceptions.PedanticThrowAnalysis;
import soot.toolkits.exceptions.ThrowAnalysis;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.util.Chain;

public class UnreachableCodeEliminator extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(UnreachableCodeEliminator.class);
   protected ThrowAnalysis throwAnalysis = null;

   public UnreachableCodeEliminator(Singletons.Global g) {
   }

   public static UnreachableCodeEliminator v() {
      return G.v().soot_jimple_toolkits_scalar_UnreachableCodeEliminator();
   }

   public UnreachableCodeEliminator(ThrowAnalysis ta) {
      this.throwAnalysis = ta;
   }

   protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
      if (Options.v().verbose()) {
         logger.debug("[" + body.getMethod().getName() + "] Eliminating unreachable code...");
      }

      if (this.throwAnalysis == null) {
         this.throwAnalysis = (ThrowAnalysis)(PhaseOptions.getBoolean(options, "remove-unreachable-traps", true) ? Scene.v().getDefaultThrowAnalysis() : PedanticThrowAnalysis.v());
      }

      ExceptionalUnitGraph graph = new ExceptionalUnitGraph(body, this.throwAnalysis, false);
      Chain<Unit> units = body.getUnits();
      int numPruned = units.size();
      Set<Unit> reachable = units.isEmpty() ? Collections.emptySet() : this.reachable(units.getFirst(), graph);
      Iterator it = body.getTraps().iterator();

      while(true) {
         Trap t;
         do {
            if (!it.hasNext()) {
               it = body.getTraps().iterator();

               while(it.hasNext()) {
                  t = (Trap)it.next();
                  if (t.getEndUnit() == body.getUnits().getLast()) {
                     reachable.add(t.getEndUnit());
                  }
               }

               Set<Unit> notReachable = new HashSet();
               Unit u;
               Iterator var12;
               if (Options.v().verbose()) {
                  var12 = units.iterator();

                  while(var12.hasNext()) {
                     u = (Unit)var12.next();
                     if (!reachable.contains(u)) {
                        notReachable.add(u);
                     }
                  }
               }

               units.retainAll(reachable);
               numPruned -= units.size();
               if (Options.v().verbose()) {
                  logger.debug("[" + body.getMethod().getName() + "]\t Removed " + numPruned + " statements: ");
                  var12 = notReachable.iterator();

                  while(var12.hasNext()) {
                     u = (Unit)var12.next();
                     logger.debug("[" + body.getMethod().getName() + "]\t         " + u);
                  }
               }

               return;
            }

            t = (Trap)it.next();
         } while(t.getBeginUnit() != t.getEndUnit() && reachable.contains(t.getHandlerUnit()));

         it.remove();
      }
   }

   private <T> Set<T> reachable(T first, DirectedGraph<T> g) {
      if (first != null && g != null) {
         Set<T> visited = new HashSet(g.size());
         Deque<T> q = new ArrayDeque();
         q.addFirst(first);

         do {
            T t = q.removeFirst();
            if (visited.add(t)) {
               q.addAll(g.getSuccsOf(t));
            }
         } while(!q.isEmpty());

         return visited;
      } else {
         return Collections.emptySet();
      }
   }
}
