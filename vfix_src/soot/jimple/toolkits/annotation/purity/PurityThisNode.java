package soot.jimple.toolkits.annotation.purity;

public class PurityThisNode extends PurityParamNode {
   public static PurityThisNode node = new PurityThisNode();

   private PurityThisNode() {
      super(-1);
   }

   public String toString() {
      return "this";
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
