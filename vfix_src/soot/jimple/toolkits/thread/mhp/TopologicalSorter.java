package soot.jimple.toolkits.thread.mhp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import soot.util.Chain;

public class TopologicalSorter {
   Chain chain;
   PegGraph pg;
   LinkedList<Object> sorter = new LinkedList();
   List<Object> visited = new ArrayList();

   public TopologicalSorter(Chain chain, PegGraph pg) {
      this.chain = chain;
      this.pg = pg;
      this.go();
   }

   private void go() {
      Iterator it = this.chain.iterator();

      while(it.hasNext()) {
         Object node = it.next();
         this.dfsVisit(node);
      }

   }

   private void dfsVisit(Object m) {
      if (!this.visited.contains(m)) {
         this.visited.add(m);
         Iterator targetsIt = this.pg.getSuccsOf(m).iterator();

         while(targetsIt.hasNext()) {
            Object target = targetsIt.next();
            this.dfsVisit(target);
         }

         this.sorter.addFirst(m);
      }
   }

   public List<Object> sorter() {
      return this.sorter;
   }
}
