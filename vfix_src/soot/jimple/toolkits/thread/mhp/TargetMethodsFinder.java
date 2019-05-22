package soot.jimple.toolkits.thread.mhp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.Kind;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

public class TargetMethodsFinder {
   public List<SootMethod> find(Unit unit, CallGraph cg, boolean canBeNullList, boolean canBeNative) {
      List<SootMethod> target = new ArrayList();
      Iterator it = cg.edgesOutOf(unit);

      while(true) {
         Edge edge;
         SootMethod targetMethod;
         do {
            if (!it.hasNext()) {
               if (target.size() < 1 && !canBeNullList) {
                  throw new RuntimeException("No target method for: " + unit);
               }

               return target;
            }

            edge = (Edge)it.next();
            targetMethod = edge.tgt();
         } while(targetMethod.isNative() && !canBeNative);

         if (edge.kind() != Kind.CLINIT) {
            target.add(targetMethod);
         }
      }
   }
}
