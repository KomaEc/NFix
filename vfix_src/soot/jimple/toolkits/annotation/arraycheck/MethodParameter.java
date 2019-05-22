package soot.jimple.toolkits.annotation.arraycheck;

import soot.SootMethod;
import soot.Type;

class MethodParameter {
   private SootMethod m;
   private int param;

   public MethodParameter(SootMethod m, int i) {
      this.m = m;
      this.param = i;
   }

   public Type getType() {
      return this.m.getParameterType(this.param);
   }

   public int hashCode() {
      return this.m.hashCode() + this.param;
   }

   public SootMethod getMethod() {
      return this.m;
   }

   public int getIndex() {
      return this.param;
   }

   public boolean equals(Object other) {
      if (!(other instanceof MethodParameter)) {
         return false;
      } else {
         MethodParameter another = (MethodParameter)other;
         return this.m.equals(another.getMethod()) && this.param == another.getIndex();
      }
   }

   public String toString() {
      return "[" + this.m.getSignature() + " : P" + this.param + "]";
   }
}
