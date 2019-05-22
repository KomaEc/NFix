package soot.jimple.toolkits.annotation.purity;

import java.util.HashMap;
import java.util.Map;
import soot.jimple.Stmt;

public class PurityStmtNode implements PurityNode {
   private Stmt id;
   private boolean inside;
   private static final Map<Stmt, Integer> nMap = new HashMap();
   private static int n = 0;

   PurityStmtNode(Stmt id, boolean inside) {
      this.id = id;
      this.inside = inside;
      if (!nMap.containsKey(id)) {
         nMap.put(id, new Integer(n));
         ++n;
      }

   }

   public String toString() {
      return this.inside ? "I_" + nMap.get(this.id) : "L_" + nMap.get(this.id);
   }

   public int hashCode() {
      return this.id.hashCode();
   }

   public boolean equals(Object o) {
      if (!(o instanceof PurityStmtNode)) {
         return false;
      } else {
         PurityStmtNode oo = (PurityStmtNode)o;
         return this.id.equals(oo.id) && oo.inside == this.inside;
      }
   }

   public boolean isInside() {
      return this.inside;
   }

   public boolean isLoad() {
      return !this.inside;
   }

   public boolean isParam() {
      return false;
   }
}
