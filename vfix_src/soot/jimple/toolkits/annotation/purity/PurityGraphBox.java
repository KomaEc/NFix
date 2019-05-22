package soot.jimple.toolkits.annotation.purity;

public class PurityGraphBox {
   public PurityGraph g = new PurityGraph();

   PurityGraphBox() {
   }

   public int hashCode() {
      return this.g.hashCode();
   }

   public boolean equals(Object o) {
      return this.g.equals(((PurityGraphBox)o).g);
   }
}
