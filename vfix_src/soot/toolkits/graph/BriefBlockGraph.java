package soot.toolkits.graph;

import soot.Body;
import soot.util.PhaseDumper;

public class BriefBlockGraph extends BlockGraph {
   public BriefBlockGraph(Body body) {
      this(new BriefUnitGraph(body));
   }

   public BriefBlockGraph(BriefUnitGraph unitGraph) {
      super(unitGraph);
      PhaseDumper.v().dumpGraph(this, this.mBody);
   }
}
