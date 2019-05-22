package soot.jimple.toolkits.annotation.arraycheck;

class FlowGraphEdge {
   Object from;
   Object to;

   public FlowGraphEdge() {
      this.from = null;
      this.to = null;
   }

   public FlowGraphEdge(Object from, Object to) {
      this.from = from;
      this.to = to;
   }

   public int hashCode() {
      return this.from.hashCode() ^ this.to.hashCode();
   }

   public Object getStartUnit() {
      return this.from;
   }

   public Object getTargetUnit() {
      return this.to;
   }

   public void changeEndUnits(Object from, Object to) {
      this.from = from;
      this.to = to;
   }

   public boolean equals(Object other) {
      if (other == null) {
         return false;
      } else if (!(other instanceof FlowGraphEdge)) {
         return false;
      } else {
         Object otherstart = ((FlowGraphEdge)other).getStartUnit();
         Object othertarget = ((FlowGraphEdge)other).getTargetUnit();
         return this.from.equals(otherstart) && this.to.equals(othertarget);
      }
   }
}
