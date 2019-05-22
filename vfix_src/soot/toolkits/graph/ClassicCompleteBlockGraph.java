package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Unit;
import soot.util.PhaseDumper;

public class ClassicCompleteBlockGraph extends BlockGraph {
   public ClassicCompleteBlockGraph(Body body) {
      super(new ClassicCompleteUnitGraph(body));
   }

   public ClassicCompleteBlockGraph(ClassicCompleteUnitGraph unitGraph) {
      super(unitGraph);
      Unit entryPoint = this.getBody().getUnits().getFirst();
      List<Block> newHeads = new ArrayList(1);
      Iterator var4 = this.getBlocks().iterator();

      while(var4.hasNext()) {
         Block b = (Block)var4.next();
         if (b.getHead() == entryPoint) {
            newHeads.add(b);
            break;
         }
      }

      this.mHeads = Collections.unmodifiableList(newHeads);
      this.mTails = Collections.emptyList();
      PhaseDumper.v().dumpGraph(this, this.mBody);
   }
}
