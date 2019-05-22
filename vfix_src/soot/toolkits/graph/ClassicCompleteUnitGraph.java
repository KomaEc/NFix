package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.Trap;
import soot.Unit;

public class ClassicCompleteUnitGraph extends TrapUnitGraph {
   public ClassicCompleteUnitGraph(Body body) {
      super(body);
   }

   protected void buildExceptionalEdges(Map<Unit, List<Unit>> unitToSuccs, Map<Unit, List<Unit>> unitToPreds) {
      super.buildExceptionalEdges(unitToSuccs, unitToPreds);
      Iterator trapIt = this.body.getTraps().iterator();

      while(trapIt.hasNext()) {
         Trap trap = (Trap)trapIt.next();
         Unit firstTrapped = trap.getBeginUnit();
         Unit catcher = trap.getHandlerUnit();
         List<Unit> origPredsOfTrapped = new ArrayList(this.getPredsOf(firstTrapped));
         Iterator unitIt = origPredsOfTrapped.iterator();

         while(unitIt.hasNext()) {
            Unit pred = (Unit)unitIt.next();
            this.addEdge(unitToSuccs, unitToPreds, pred, catcher);
         }
      }

   }
}
