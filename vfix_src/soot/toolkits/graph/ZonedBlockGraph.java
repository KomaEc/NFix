package soot.toolkits.graph;

import java.util.Iterator;
import java.util.Set;
import soot.Body;
import soot.Trap;
import soot.Unit;
import soot.util.PhaseDumper;

public class ZonedBlockGraph extends BlockGraph {
   public ZonedBlockGraph(Body body) {
      this(new BriefUnitGraph(body));
   }

   public ZonedBlockGraph(BriefUnitGraph unitGraph) {
      super(unitGraph);
      PhaseDumper.v().dumpGraph(this, this.mBody);
   }

   protected Set<Unit> computeLeaders(UnitGraph unitGraph) {
      Body body = unitGraph.getBody();
      if (body != this.mBody) {
         throw new RuntimeException("ZonedBlockGraph.computeLeaders() called with a UnitGraph that doesn't match its mBody.");
      } else {
         Set<Unit> leaders = super.computeLeaders(unitGraph);
         Iterator it = body.getTraps().iterator();

         while(it.hasNext()) {
            Trap trap = (Trap)it.next();
            leaders.add(trap.getBeginUnit());
            leaders.add(trap.getEndUnit());
         }

         return leaders;
      }
   }
}
