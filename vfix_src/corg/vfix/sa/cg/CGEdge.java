package corg.vfix.sa.cg;

import soot.Unit;

public class CGEdge {
   private Unit unit;
   private CGNode src;
   private CGNode tgt;

   public CGEdge(CGNode s, CGNode t, Unit u) {
      this.src = s;
      this.tgt = t;
      this.unit = u;
   }

   public Unit getUnit() {
      return this.unit;
   }

   public CGNode getSrc() {
      return this.src;
   }

   public CGNode getTgt() {
      return this.tgt;
   }

   public boolean equals(CGEdge edge) {
      return edge.getSrc().equals(this.src) && edge.getTgt().equals(this.tgt) && edge.getUnit().equals(this.unit);
   }

   public String toString() {
      return "From: " + this.src.toString() + " To " + this.tgt.toString();
   }
}
