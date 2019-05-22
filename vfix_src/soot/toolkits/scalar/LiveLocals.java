package soot.toolkits.scalar;

import java.util.List;
import soot.Local;
import soot.Unit;
import soot.toolkits.graph.UnitGraph;

public interface LiveLocals {
   List<Local> getLiveLocalsBefore(Unit var1);

   List<Local> getLiveLocalsAfter(Unit var1);

   public static final class Factory {
      private Factory() {
      }

      public static LiveLocals newLiveLocals(UnitGraph graph) {
         return new SimpleLiveLocals(graph);
      }
   }
}
