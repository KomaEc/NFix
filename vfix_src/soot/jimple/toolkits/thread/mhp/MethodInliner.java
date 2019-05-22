package soot.jimple.toolkits.thread.mhp;

import java.util.ArrayList;
import java.util.Iterator;
import soot.jimple.toolkits.thread.mhp.stmt.JPegStmt;
import soot.util.Chain;

public class MethodInliner {
   MethodInliner() {
   }

   public static void inline(ArrayList sites) {
      Iterator it = sites.iterator();

      while(it.hasNext()) {
         ArrayList element = (ArrayList)it.next();
         JPegStmt stmt = (JPegStmt)element.get(0);
         Chain chain = (Chain)element.get(1);
         PegGraph p1 = (PegGraph)element.get(2);
         PegGraph p2 = (PegGraph)element.get(3);
         inline(stmt, chain, p1, p2);
      }

   }

   private static void inline(JPegStmt invokeStmt, Chain chain, PegGraph container, PegGraph inlinee) {
      if (!container.addPeg(inlinee, chain)) {
         throw new RuntimeException("heads >1 stm: " + invokeStmt);
      } else {
         container.buildSuccsForInlining(invokeStmt, chain, inlinee);
         container.buildMaps(inlinee);
         container.buildPreds();
      }
   }
}
