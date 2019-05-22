package soot.jimple.toolkits.annotation.purity;

public class PurityGlobalNode implements PurityNode {
   public static PurityGlobalNode node = new PurityGlobalNode();

   private PurityGlobalNode() {
   }

   public String toString() {
      return "GBL";
   }

   public int hashCode() {
      return 0;
   }

   public boolean equals(Object o) {
      return o instanceof PurityGlobalNode;
   }

   public boolean isInside() {
      return false;
   }

   public boolean isLoad() {
      return false;
   }

   public boolean isParam() {
      return false;
   }
}
