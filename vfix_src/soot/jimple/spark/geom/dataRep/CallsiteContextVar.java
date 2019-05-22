package soot.jimple.spark.geom.dataRep;

import soot.jimple.spark.pag.Node;

public class CallsiteContextVar extends ContextVar {
   public CgEdge context = null;

   public CallsiteContextVar() {
   }

   public CallsiteContextVar(CgEdge c, Node v) {
      this.context = c;
      this.var = v;
   }

   public CallsiteContextVar(CallsiteContextVar o) {
      this.context = o.context;
      this.var = o.var;
   }

   public String toString() {
      return "<" + this.context.toString() + ", " + this.var.toString() + ">";
   }

   public boolean equals(Object o) {
      CallsiteContextVar other = (CallsiteContextVar)o;
      return other.context == this.context && other.var == this.var;
   }

   public int hashCode() {
      int ch = 0;
      if (this.context != null) {
         ch = this.context.hashCode();
      }

      int ans = this.var.hashCode() + ch;
      if (ans < 0) {
         ans = -ans;
      }

      return ans;
   }

   public boolean contains(ContextVar cv) {
      CallsiteContextVar ccv = (CallsiteContextVar)cv;
      return this.context == ccv.context;
   }

   public boolean merge(ContextVar cv) {
      return false;
   }

   public boolean intersect(ContextVar cv) {
      return this.contains(cv);
   }
}
