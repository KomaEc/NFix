package soot.jimple.toolkits.thread.mhp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import soot.toolkits.graph.DirectedGraph;

public class SCC {
   private Set<Object> gray = new HashSet();
   private final LinkedList<Object> finishedOrder = new LinkedList();
   private final List<List<Object>> sccList = new ArrayList();

   public SCC(Iterator it, DirectedGraph g) {
      while(it.hasNext()) {
         Object s = it.next();
         if (!this.gray.contains(s)) {
            this.visitNode(g, s);
         }
      }

      this.gray = new HashSet();
      Iterator revNodeIt = this.finishedOrder.iterator();

      while(revNodeIt.hasNext()) {
         Object s = revNodeIt.next();
         if (!this.gray.contains(s)) {
            List<Object> scc = new ArrayList();
            this.visitRevNode(g, s, scc);
            this.sccList.add(scc);
         }
      }

   }

   private void visitNode(DirectedGraph g, Object s) {
      this.gray.add(s);
      Iterator it = g.getSuccsOf(s).iterator();
      if (g.getSuccsOf(s).size() > 0) {
         while(it.hasNext()) {
            Object succ = it.next();
            if (!this.gray.contains(succ)) {
               this.visitNode(g, succ);
            }
         }
      }

      this.finishedOrder.addFirst(s);
   }

   private void visitRevNode(DirectedGraph g, Object s, List<Object> scc) {
      scc.add(s);
      this.gray.add(s);
      if (g.getPredsOf(s) != null) {
         Iterator predsIt = g.getPredsOf(s).iterator();
         if (g.getPredsOf(s).size() > 0) {
            while(predsIt.hasNext()) {
               Object pred = predsIt.next();
               if (!this.gray.contains(pred)) {
                  this.visitRevNode(g, pred, scc);
               }
            }
         }
      }

   }

   public List<List<Object>> getSccList() {
      return this.sccList;
   }

   public LinkedList<Object> getFinishedOrder() {
      return this.finishedOrder;
   }
}
