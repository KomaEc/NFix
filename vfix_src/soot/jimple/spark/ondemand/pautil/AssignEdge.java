package soot.jimple.spark.ondemand.pautil;

import soot.jimple.spark.pag.VarNode;

public final class AssignEdge {
   private static final int PARAM_MASK = 1;
   private static final int RETURN_MASK = 2;
   private static final int CALL_MASK = 3;
   private Integer callSite = null;
   private final VarNode src;
   private int scratch;
   private final VarNode dst;

   public AssignEdge(VarNode from, VarNode to) {
      this.src = from;
      this.dst = to;
   }

   public boolean isParamEdge() {
      return (this.scratch & 1) != 0;
   }

   public void setParamEdge() {
      this.scratch |= 1;
   }

   public boolean isReturnEdge() {
      return (this.scratch & 2) != 0;
   }

   public void setReturnEdge() {
      this.scratch |= 2;
   }

   public boolean isCallEdge() {
      return (this.scratch & 3) != 0;
   }

   public void clearCallEdge() {
      this.scratch = 0;
   }

   public Integer getCallSite() {
      assert this.callSite != null : this + " is not a call edge!";

      return this.callSite;
   }

   public void setCallSite(Integer i) {
      this.callSite = i;
   }

   public String toString() {
      String ret = this.src + " -> " + this.dst;
      if (this.isReturnEdge()) {
         ret = ret + "(* return" + this.callSite + " *)";
      } else if (this.isParamEdge()) {
         ret = ret + "(* param" + this.callSite + " *)";
      }

      return ret;
   }

   public VarNode getSrc() {
      return this.src;
   }

   public VarNode getDst() {
      return this.dst;
   }
}
