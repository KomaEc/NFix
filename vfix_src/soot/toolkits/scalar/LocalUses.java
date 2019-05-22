package soot.toolkits.scalar;

import java.util.List;
import soot.Body;
import soot.Unit;
import soot.toolkits.graph.UnitGraph;

public interface LocalUses {
   List<UnitValueBoxPair> getUsesOf(Unit var1);

   public static final class Factory {
      private Factory() {
      }

      public static LocalUses newLocalUses(Body body) {
         return newLocalUses(body, LocalDefs.Factory.newLocalDefs(body));
      }

      public static LocalUses newLocalUses(Body body, LocalDefs localDefs) {
         return new SimpleLocalUses(body, localDefs);
      }

      public static LocalUses newLocalUses(UnitGraph graph) {
         return newLocalUses(graph.getBody(), LocalDefs.Factory.newLocalDefs(graph));
      }

      public static LocalUses newLocalUses(UnitGraph graph, LocalDefs localDefs) {
         return newLocalUses(graph.getBody(), localDefs);
      }
   }
}
