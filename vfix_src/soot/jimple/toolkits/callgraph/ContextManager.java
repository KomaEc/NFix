package soot.jimple.toolkits.callgraph;

import soot.Context;
import soot.Kind;
import soot.MethodOrMethodContext;
import soot.SootMethod;
import soot.Unit;

public interface ContextManager {
   void addStaticEdge(MethodOrMethodContext var1, Unit var2, SootMethod var3, Kind var4);

   void addVirtualEdge(MethodOrMethodContext var1, Unit var2, SootMethod var3, Kind var4, Context var5);

   CallGraph callGraph();
}
