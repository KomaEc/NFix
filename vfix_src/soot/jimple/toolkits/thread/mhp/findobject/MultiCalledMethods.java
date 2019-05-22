package soot.jimple.toolkits.thread.mhp.findobject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import soot.Scene;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.thread.mhp.pegcallgraph.PegCallGraph;
import soot.toolkits.graph.CompleteUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.FlowSet;

public class MultiCalledMethods {
   Set<SootMethod> multiCalledMethods = new HashSet();

   MultiCalledMethods(PegCallGraph pcg, Set<SootMethod> mcm) {
      this.multiCalledMethods = mcm;
      this.byMCalledS0(pcg);
      this.finder1(pcg);
      this.finder2(pcg);
      this.propagate(pcg);
   }

   private void byMCalledS0(PegCallGraph pcg) {
      MultiRunStatementsFinder finder;
      FlowSet var7;
      for(Iterator it = pcg.iterator(); it.hasNext(); var7 = finder.getMultiRunStatements()) {
         SootMethod sm = (SootMethod)it.next();
         UnitGraph graph = new CompleteUnitGraph(sm.getActiveBody());
         CallGraph callGraph = Scene.v().getCallGraph();
         finder = new MultiRunStatementsFinder(graph, sm, this.multiCalledMethods, callGraph);
      }

   }

   private void propagate(PegCallGraph pcg) {
      Set<SootMethod> visited = new HashSet();
      List<SootMethod> reachable = new ArrayList();
      reachable.addAll(this.multiCalledMethods);

      while(true) {
         SootMethod popped;
         do {
            if (reachable.size() < 1) {
               return;
            }

            popped = (SootMethod)reachable.remove(0);
         } while(visited.contains(popped));

         if (!this.multiCalledMethods.contains(popped)) {
            this.multiCalledMethods.add(popped);
         }

         visited.add(popped);
         Iterator succIt = pcg.getSuccsOf(popped).iterator();

         while(succIt.hasNext()) {
            Object succ = succIt.next();
            reachable.add((SootMethod)succ);
         }
      }
   }

   private void finder1(PegCallGraph pcg) {
      Set clinitMethods = pcg.getClinitMethods();
      Iterator it = pcg.iterator();

      while(it.hasNext()) {
         Object head = it.next();
         Set<Object> gray = new HashSet();
         LinkedList<Object> queue = new LinkedList();
         queue.add(head);

         while(queue.size() > 0) {
            Object root = queue.getFirst();
            Iterator succsIt = pcg.getSuccsOf(root).iterator();

            while(succsIt.hasNext()) {
               Object succ = succsIt.next();
               if (!gray.contains(succ)) {
                  gray.add(succ);
                  queue.addLast(succ);
               } else if (!clinitMethods.contains(succ)) {
                  this.multiCalledMethods.add((SootMethod)succ);
               }
            }

            queue.remove(root);
         }
      }

   }

   private void finder2(PegCallGraph pcg) {
      pcg.trim();
      Set<SootMethod> first = new HashSet();
      Set<SootMethod> second = new HashSet();
      Iterator it = pcg.iterator();

      while(it.hasNext()) {
         SootMethod s = (SootMethod)it.next();
         if (!second.contains(s)) {
            this.visitNode(s, pcg, first, second);
         }
      }

   }

   private void visitNode(SootMethod node, PegCallGraph pcg, Set<SootMethod> first, Set<SootMethod> second) {
      if (first.contains(node)) {
         second.add(node);
         if (!this.multiCalledMethods.contains(node)) {
            this.multiCalledMethods.add(node);
         }
      } else {
         first.add(node);
      }

      Iterator it = pcg.getTrimSuccsOf(node).iterator();

      while(it.hasNext()) {
         SootMethod succ = (SootMethod)it.next();
         if (!second.contains(succ)) {
            this.visitNode(succ, pcg, first, second);
         }
      }

   }

   public Set<SootMethod> getMultiCalledMethods() {
      return this.multiCalledMethods;
   }
}
