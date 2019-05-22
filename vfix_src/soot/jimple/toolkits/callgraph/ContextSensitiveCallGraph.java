package soot.jimple.toolkits.callgraph;

import java.util.Iterator;
import soot.Context;
import soot.SootMethod;
import soot.Unit;

public interface ContextSensitiveCallGraph {
   Iterator edgeSources();

   Iterator allEdges();

   Iterator edgesOutOf(Context var1, SootMethod var2, Unit var3);

   Iterator edgesOutOf(Context var1, SootMethod var2);

   Iterator edgesInto(Context var1, SootMethod var2);
}
