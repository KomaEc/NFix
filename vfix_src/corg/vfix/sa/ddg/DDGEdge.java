package corg.vfix.sa.ddg;

import soot.Unit;
import soot.Value;

public class DDGEdge {
   private DDGNode from;
   private DDGNode to;
   private Value value;

   DDGEdge(DDGNode f, DDGNode t, Value v) {
      this.from = f;
      this.to = t;
      this.value = v;
   }

   public Unit getFromUnit() {
      return this.from.getUnit();
   }

   public Unit getToUnit() {
      return this.to.getUnit();
   }

   public Value getValue() {
      return this.value;
   }

   public DDGNode getFrom() {
      return this.from;
   }

   public DDGNode getTo() {
      return this.to;
   }
}
