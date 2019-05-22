package soot.jimple.spark.geom.utils;

import java.util.Iterator;
import soot.Scene;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;

public class SootInfo {
   public static int countCallEdgesForCallsite(Stmt callsite, boolean stopForMutiple) {
      CallGraph cg = Scene.v().getCallGraph();
      int count = 0;
      Iterator it = cg.edgesOutOf((Unit)callsite);

      while(it.hasNext()) {
         it.next();
         ++count;
         if (stopForMutiple && count > 1) {
            break;
         }
      }

      return count;
   }
}
