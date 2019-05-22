package soot.jimple.toolkits.annotation.purity;

import java.util.HashMap;
import java.util.Map;
import soot.SootMethod;

public class PurityMethodNode implements PurityNode {
   private SootMethod id;
   private static final Map<SootMethod, Integer> nMap = new HashMap();
   private static int n = 0;

   PurityMethodNode(SootMethod id) {
      this.id = id;
      if (!nMap.containsKey(id)) {
         nMap.put(id, new Integer(n));
         ++n;
      }

   }

   public String toString() {
      return "M_" + nMap.get(this.id);
   }

   public int hashCode() {
      return this.id.hashCode();
   }

   public boolean equals(Object o) {
      if (o instanceof PurityMethodNode) {
         PurityMethodNode oo = (PurityMethodNode)o;
         return this.id.equals(oo.id);
      } else {
         return false;
      }
   }

   public boolean isInside() {
      return true;
   }

   public boolean isLoad() {
      return false;
   }

   public boolean isParam() {
      return false;
   }
}
