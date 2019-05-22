package soot.jimple.toolkits.thread.mhp.pegcallgraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import soot.jimple.toolkits.thread.mhp.SCC;

public class CheckRecursiveCalls {
   List<List> newSccList = null;

   public CheckRecursiveCalls(PegCallGraph pcg, Set<Object> methodNeedExtent) {
      Iterator it = pcg.iterator();
      SCC scc = new SCC(it, pcg);
      List<List<Object>> sccList = scc.getSccList();
      this.newSccList = this.updateScc(sccList, pcg);
      this.check(this.newSccList, methodNeedExtent);
   }

   private List<List> updateScc(List<List<Object>> sccList, PegCallGraph pcg) {
      List<List> newList = new ArrayList();
      Iterator listIt = sccList.iterator();

      while(true) {
         List s;
         Object o;
         label25:
         do {
            while(listIt.hasNext()) {
               s = (List)listIt.next();
               if (s.size() == 1) {
                  o = s.get(0);
                  continue label25;
               }

               newList.add(s);
            }

            return newList;
         } while(!pcg.getSuccsOf(o).contains(o) && !pcg.getPredsOf(o).contains(o));

         newList.add(s);
      }
   }

   private void check(List<List> sccList, Set<Object> methodNeedExtent) {
      Iterator listIt = sccList.iterator();

      while(true) {
         List s;
         do {
            if (!listIt.hasNext()) {
               return;
            }

            s = (List)listIt.next();
         } while(s.size() <= 0);

         Iterator it = s.iterator();

         while(it.hasNext()) {
            Object o = it.next();
            if (methodNeedExtent.contains(o)) {
               System.err.println("Fail to compute MHP because interested method call relate to recursive calls!");
               System.err.println("interested method: " + o);
               throw new RuntimeException("Fail to compute MHP because interested method call relate to recursive calls!");
            }
         }
      }
   }
}
