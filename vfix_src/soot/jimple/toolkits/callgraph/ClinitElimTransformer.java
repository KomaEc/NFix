package soot.jimple.toolkits.callgraph;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SootMethod;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.scalar.FlowSet;

public class ClinitElimTransformer extends BodyTransformer {
   protected void internalTransform(Body b, String phaseName, Map options) {
      ClinitElimAnalysis a = new ClinitElimAnalysis(new BriefUnitGraph(b));
      CallGraph cg = Scene.v().getCallGraph();
      SootMethod m = b.getMethod();
      Iterator edgeIt = cg.edgesOutOf((MethodOrMethodContext)m);

      while(edgeIt.hasNext()) {
         Edge e = (Edge)edgeIt.next();
         if (e.srcStmt() != null && e.isClinit()) {
            FlowSet methods = (FlowSet)a.getFlowBefore(e.srcStmt());
            if (methods.contains(e.tgt())) {
               cg.removeEdge(e);
            }
         }
      }

   }
}
