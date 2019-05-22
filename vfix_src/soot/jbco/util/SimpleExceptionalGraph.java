package soot.jbco.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.Trap;
import soot.Unit;
import soot.toolkits.graph.TrapUnitGraph;

public class SimpleExceptionalGraph extends TrapUnitGraph {
   public SimpleExceptionalGraph(Body body) {
      super(body);
      int size = this.unitChain.size();
      this.unitToSuccs = new HashMap(size * 2 + 1, 0.7F);
      this.unitToPreds = new HashMap(size * 2 + 1, 0.7F);
      this.buildUnexceptionalEdges(this.unitToSuccs, this.unitToPreds);
      this.buildSimpleExceptionalEdges(this.unitToSuccs, this.unitToPreds);
      this.buildHeadsAndTails();
   }

   protected void buildSimpleExceptionalEdges(Map unitToSuccs, Map unitToPreds) {
      Iterator trapIt = this.body.getTraps().iterator();

      while(trapIt.hasNext()) {
         Trap trap = (Trap)trapIt.next();
         Unit handler = trap.getHandlerUnit();
         Iterator predIt = ((List)unitToPreds.get(trap.getBeginUnit())).iterator();

         while(predIt.hasNext()) {
            Unit pred = (Unit)predIt.next();
            this.addEdge(unitToSuccs, unitToPreds, pred, handler);
         }
      }

   }
}
