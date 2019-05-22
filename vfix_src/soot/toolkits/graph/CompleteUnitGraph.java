package soot.toolkits.graph;

import soot.Body;
import soot.toolkits.exceptions.PedanticThrowAnalysis;

public class CompleteUnitGraph extends ExceptionalUnitGraph {
   public CompleteUnitGraph(Body b) {
      super(b, PedanticThrowAnalysis.v(), false);
   }
}
