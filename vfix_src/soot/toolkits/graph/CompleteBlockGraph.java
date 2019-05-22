package soot.toolkits.graph;

import soot.Body;

public class CompleteBlockGraph extends ExceptionalBlockGraph {
   public CompleteBlockGraph(Body b) {
      super((ExceptionalUnitGraph)(new CompleteUnitGraph(b)));
   }
}
