package soot.jimple.toolkits.callgraph;

public class InstanceInvokeEdgesPred implements EdgePredicate {
   public boolean want(Edge e) {
      return e.isInstance();
   }
}
