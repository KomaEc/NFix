package soot.jimple.toolkits.annotation.purity;

public class PurityParamNode implements PurityNode {
   private int id;

   PurityParamNode(int id) {
      this.id = id;
   }

   public String toString() {
      return "P_" + this.id;
   }

   public int hashCode() {
      return this.id;
   }

   public boolean equals(Object o) {
      if (o instanceof PurityParamNode) {
         return ((PurityParamNode)o).id == this.id;
      } else {
         return false;
      }
   }

   public boolean isInside() {
      return false;
   }

   public boolean isLoad() {
      return false;
   }

   public boolean isParam() {
      return true;
   }
}
