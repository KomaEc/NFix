package soot.jimple.toolkits.annotation.arraycheck;

class WeightedDirectedEdge {
   Object from;
   Object to;
   int weight;

   public WeightedDirectedEdge(Object from, Object to, int weight) {
      this.from = from;
      this.to = to;
      this.weight = weight;
   }

   public int hashCode() {
      return this.from.hashCode() + this.to.hashCode() + this.weight;
   }

   public boolean equals(Object other) {
      if (!(other instanceof WeightedDirectedEdge)) {
         return false;
      } else {
         WeightedDirectedEdge another = (WeightedDirectedEdge)other;
         return this.from == another.from && this.to == another.to && this.weight == another.weight;
      }
   }

   public String toString() {
      return this.from + "->" + this.to + "=" + this.weight;
   }
}
