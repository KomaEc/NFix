package soot.toolkits.scalar;

import java.util.List;
import soot.Body;
import soot.Local;
import soot.Unit;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;

public interface LocalDefs {
   List<Unit> getDefsOfAt(Local var1, Unit var2);

   List<Unit> getDefsOf(Local var1);

   public static final class Factory {
      private Factory() {
      }

      public static LocalDefs newLocalDefs(Body body) {
         return newLocalDefs(body, false);
      }

      public static LocalDefs newLocalDefs(Body body, boolean expectUndefined) {
         return newLocalDefs((UnitGraph)(new ExceptionalUnitGraph(body)), expectUndefined);
      }

      public static LocalDefs newLocalDefs(UnitGraph graph) {
         return newLocalDefs(graph, false);
      }

      public static LocalDefs newLocalDefs(UnitGraph graph, boolean expectUndefined) {
         return new SimpleLocalDefs(graph, expectUndefined ? SimpleLocalDefs.FlowAnalysisMode.OmitSSA : SimpleLocalDefs.FlowAnalysisMode.Automatic);
      }

      public static LocalDefs newLocalDefsFlowInsensitive(UnitGraph graph) {
         return new SimpleLocalDefs(graph, SimpleLocalDefs.FlowAnalysisMode.FlowInsensitive);
      }
   }
}
