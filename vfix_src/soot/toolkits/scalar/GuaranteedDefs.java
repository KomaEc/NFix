package soot.toolkits.scalar;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Unit;
import soot.options.Options;
import soot.toolkits.graph.UnitGraph;

public class GuaranteedDefs {
   private static final Logger logger = LoggerFactory.getLogger(GuaranteedDefs.class);
   protected Map<Unit, List> unitToGuaranteedDefs;

   public GuaranteedDefs(UnitGraph graph) {
      if (Options.v().verbose()) {
         logger.debug("[" + graph.getBody().getMethod().getName() + "]     Constructing GuaranteedDefs...");
      }

      GuaranteedDefsAnalysis analysis = new GuaranteedDefsAnalysis(graph);
      this.unitToGuaranteedDefs = new HashMap(graph.size() * 2 + 1, 0.7F);
      Iterator unitIt = graph.iterator();

      while(unitIt.hasNext()) {
         Unit s = (Unit)unitIt.next();
         FlowSet set = (FlowSet)analysis.getFlowBefore(s);
         this.unitToGuaranteedDefs.put(s, Collections.unmodifiableList(set.toList()));
      }

   }

   public List getGuaranteedDefs(Unit s) {
      return (List)this.unitToGuaranteedDefs.get(s);
   }
}
