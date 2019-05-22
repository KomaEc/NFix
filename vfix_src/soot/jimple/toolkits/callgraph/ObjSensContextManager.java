package soot.jimple.toolkits.callgraph;

import soot.Context;
import soot.Kind;
import soot.MethodContext;
import soot.MethodOrMethodContext;
import soot.SootMethod;
import soot.Unit;

public class ObjSensContextManager implements ContextManager {
   private CallGraph cg;

   public ObjSensContextManager(CallGraph cg) {
      this.cg = cg;
   }

   public void addStaticEdge(MethodOrMethodContext src, Unit srcUnit, SootMethod target, Kind kind) {
      this.cg.addEdge(new Edge(src, srcUnit, target, kind));
   }

   public void addVirtualEdge(MethodOrMethodContext src, Unit srcUnit, SootMethod target, Kind kind, Context typeContext) {
      this.cg.addEdge(new Edge(src, srcUnit, MethodContext.v(target, typeContext), kind));
   }

   public CallGraph callGraph() {
      return this.cg;
   }
}
