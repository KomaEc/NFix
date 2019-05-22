package soot.jimple.toolkits.annotation.purity;

public class PurityEdge {
   private String field;
   private PurityNode source;
   private PurityNode target;
   private boolean inside;

   PurityEdge(PurityNode source, String field, PurityNode target, boolean inside) {
      this.source = source;
      this.field = field;
      this.target = target;
      this.inside = inside;
   }

   public String getField() {
      return this.field;
   }

   public PurityNode getTarget() {
      return this.target;
   }

   public PurityNode getSource() {
      return this.source;
   }

   public boolean isInside() {
      return this.inside;
   }

   public int hashCode() {
      return this.field.hashCode() + this.target.hashCode() + this.source.hashCode() + (this.inside ? 69 : 0);
   }

   public boolean equals(Object o) {
      if (!(o instanceof PurityEdge)) {
         return false;
      } else {
         PurityEdge e = (PurityEdge)o;
         return this.source.equals(e.source) && this.field.equals(e.field) && this.target.equals(e.target) && this.inside == e.inside;
      }
   }

   public String toString() {
      return this.inside ? this.source.toString() + " = " + this.field + " => " + this.target.toString() : this.source.toString() + " - " + this.field + " -> " + this.target.toString();
   }
}
