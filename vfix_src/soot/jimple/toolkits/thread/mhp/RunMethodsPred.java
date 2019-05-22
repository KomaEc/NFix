package soot.jimple.toolkits.thread.mhp;

import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.EdgePredicate;

public class RunMethodsPred implements EdgePredicate {
   public boolean want(Edge e) {
      String tgtSubSignature = e.tgt().getSubSignature();
      return tgtSubSignature.equals("void run()");
   }
}
