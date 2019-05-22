package soot.toolkits.graph;

import java.util.HashMap;
import soot.Body;
import soot.Timers;
import soot.options.Options;
import soot.util.PhaseDumper;

public class BriefUnitGraph extends UnitGraph {
   public BriefUnitGraph(Body body) {
      super(body);
      int size = this.unitChain.size();
      if (Options.v().time()) {
         Timers.v().graphTimer.start();
      }

      this.unitToSuccs = new HashMap(size * 2 + 1, 0.7F);
      this.unitToPreds = new HashMap(size * 2 + 1, 0.7F);
      this.buildUnexceptionalEdges(this.unitToSuccs, this.unitToPreds);
      this.buildHeadsAndTails();
      if (Options.v().time()) {
         Timers.v().graphTimer.end();
      }

      PhaseDumper.v().dumpGraph(this, body);
   }
}
