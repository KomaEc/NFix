package corg.vfix.sa.vfg.build.reach;

import corg.vfix.sa.ddg.DDG;
import corg.vfix.sa.vfg.VFGNode;
import soot.Body;
import soot.Unit;
import soot.Value;

public class IntraReach {
   private static DDG ddg;

   public static boolean immediateReach(VFGNode node1, VFGNode node2, Value value) {
      Body body = node1.getBody();
      if (!body.equals(node2.getBody())) {
         return false;
      } else {
         if (ddg == null || !ddg.getBody().equals(body)) {
            ddg = new DDG(body);
         }

         return ddg.immediateDefUse((Unit)node1.getStmt(), (Unit)node2.getStmt(), value);
      }
   }
}
