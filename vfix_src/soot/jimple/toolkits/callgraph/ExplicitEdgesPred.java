package soot.jimple.toolkits.callgraph;

public class ExplicitEdgesPred implements EdgePredicate {
   public boolean want(Edge e) {
      return e.isExplicit();
   }
}
