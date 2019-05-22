package soot.toolkits.graph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.Timers;
import soot.Trap;
import soot.Unit;
import soot.options.Options;
import soot.util.PhaseDumper;

public class TrapUnitGraph extends UnitGraph {
   public TrapUnitGraph(Body body) {
      super(body);
      int size = this.unitChain.size();
      if (Options.v().time()) {
         Timers.v().graphTimer.start();
      }

      this.unitToSuccs = new HashMap(size * 2 + 1, 0.7F);
      this.unitToPreds = new HashMap(size * 2 + 1, 0.7F);
      this.buildUnexceptionalEdges(this.unitToSuccs, this.unitToPreds);
      this.buildExceptionalEdges(this.unitToSuccs, this.unitToPreds);
      this.buildHeadsAndTails();
      if (Options.v().time()) {
         Timers.v().graphTimer.end();
      }

      PhaseDumper.v().dumpGraph(this, body);
   }

   protected void buildExceptionalEdges(Map<Unit, List<Unit>> unitToSuccs, Map<Unit, List<Unit>> unitToPreds) {
      Iterator var3 = this.body.getTraps().iterator();

      while(var3.hasNext()) {
         Trap trap = (Trap)var3.next();
         Unit first = trap.getBeginUnit();
         Unit last = (Unit)this.unitChain.getPredOf(trap.getEndUnit());
         Unit catcher = trap.getHandlerUnit();
         Iterator unitIt = this.unitChain.iterator(first, last);

         while(unitIt.hasNext()) {
            Unit trapped = (Unit)unitIt.next();
            this.addEdge(unitToSuccs, unitToPreds, trapped, catcher);
         }
      }

   }
}
