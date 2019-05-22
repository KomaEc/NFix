package soot.jimple.toolkits.callgraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SootMethod;
import soot.util.NumberedSet;

public class TopologicalOrderer {
   CallGraph cg;
   List<SootMethod> order = new ArrayList();
   NumberedSet<SootMethod> visited = new NumberedSet(Scene.v().getMethodNumberer());

   public TopologicalOrderer(CallGraph cg) {
      this.cg = cg;
   }

   public void go() {
      Iterator methods = this.cg.sourceMethods();

      while(methods.hasNext()) {
         SootMethod m = (SootMethod)methods.next();
         this.dfsVisit(m);
      }

   }

   private void dfsVisit(SootMethod m) {
      if (!this.visited.contains(m)) {
         this.visited.add(m);
         Targets targets = new Targets(this.cg.edgesOutOf((MethodOrMethodContext)m));

         while(targets.hasNext()) {
            SootMethod target = (SootMethod)targets.next();
            this.dfsVisit(target);
         }

         this.order.add(m);
      }
   }

   public List<SootMethod> order() {
      return this.order;
   }
}
