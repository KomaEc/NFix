package soot.toolkits.graph.pdg;

import soot.Unit;

class GuardedBlock {
   Unit start;
   Unit end;

   public GuardedBlock(Unit s, Unit e) {
      this.start = s;
      this.end = e;
   }

   public int hashCode() {
      int result = 17;
      int result = 37 * result + this.start.hashCode();
      result = 37 * result + this.end.hashCode();
      return result;
   }

   public boolean equals(Object rhs) {
      if (rhs == this) {
         return true;
      } else if (!(rhs instanceof GuardedBlock)) {
         return false;
      } else {
         GuardedBlock rhsGB = (GuardedBlock)rhs;
         return this.start == rhsGB.start && this.end == rhsGB.end;
      }
   }
}
