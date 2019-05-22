package corg.vfix.sa.vfg;

import soot.Value;

public class VFGEdge {
   private VFGNode from;
   private VFGNode to;
   private Value value;
   private int type;
   private boolean flag;

   VFGEdge(VFGNode f, VFGNode t, Value v, int edgeType) {
      this.from = f;
      this.to = t;
      this.value = v;
      this.type = edgeType;
      this.flag = false;
   }

   public boolean getFlag() {
      return this.flag;
   }

   public void setFlag() {
      this.flag = true;
   }

   public void setType(int t) {
      this.type = t;
   }

   public int getType() {
      return this.type;
   }

   public Value getValue() {
      return this.value;
   }

   public VFGNode getFrom() {
      return this.from;
   }

   public VFGNode getTo() {
      return this.to;
   }

   public boolean equals(VFGEdge edge) {
      return edge.getFrom().equals(this.from) && edge.getTo().equals(this.to);
   }
}
