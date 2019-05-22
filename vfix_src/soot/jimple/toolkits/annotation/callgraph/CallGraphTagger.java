package soot.jimple.toolkits.annotation.callgraph;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.MethodOrMethodContext;
import soot.MethodToContexts;
import soot.Scene;
import soot.Singletons;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.tagkit.Host;
import soot.tagkit.LinkTag;

public class CallGraphTagger extends BodyTransformer {
   private MethodToContexts methodToContexts;

   public CallGraphTagger(Singletons.Global g) {
   }

   public static CallGraphTagger v() {
      return G.v().soot_jimple_toolkits_annotation_callgraph_CallGraphTagger();
   }

   protected void internalTransform(Body b, String phaseName, Map options) {
      CallGraph cg = Scene.v().getCallGraph();
      if (this.methodToContexts == null) {
         this.methodToContexts = new MethodToContexts(Scene.v().getReachableMethods().listener());
      }

      Iterator stmtIt = b.getUnits().iterator();

      Iterator momcIt;
      while(stmtIt.hasNext()) {
         Stmt s = (Stmt)stmtIt.next();
         momcIt = cg.edgesOutOf((Unit)s);

         while(momcIt.hasNext()) {
            Edge e = (Edge)momcIt.next();
            SootMethod m = e.tgt();
            s.addTag(new LinkTag("CallGraph: Type: " + e.kind() + " Target Method/Context: " + e.getTgt().toString(), m, m.getDeclaringClass().getName(), "Call Graph"));
         }
      }

      SootMethod m = b.getMethod();
      momcIt = this.methodToContexts.get(m).iterator();

      while(momcIt.hasNext()) {
         MethodOrMethodContext momc = (MethodOrMethodContext)momcIt.next();

         Edge callEdge;
         SootMethod methodCaller;
         Object src;
         for(Iterator callerEdges = cg.edgesInto(momc); callerEdges.hasNext(); m.addTag(new LinkTag("CallGraph: Source Type: " + callEdge.kind() + " Source Method/Context: " + callEdge.getSrc().toString(), (Host)src, methodCaller.getDeclaringClass().getName(), "Call Graph"))) {
            callEdge = (Edge)callerEdges.next();
            methodCaller = callEdge.src();
            src = methodCaller;
            if (callEdge.srcUnit() != null) {
               src = callEdge.srcUnit();
            }
         }
      }

   }
}
