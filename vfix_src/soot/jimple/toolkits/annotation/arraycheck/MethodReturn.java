package soot.jimple.toolkits.annotation.arraycheck;

import soot.SootMethod;
import soot.Type;

class MethodReturn {
   private SootMethod m;

   public MethodReturn(SootMethod m) {
      this.m = m;
   }

   public SootMethod getMethod() {
      return this.m;
   }

   public Type getType() {
      return this.m.getReturnType();
   }

   public int hashCode() {
      return this.m.hashCode() + this.m.getParameterCount();
   }

   public boolean equals(Object other) {
      return other instanceof MethodReturn ? this.m.equals(((MethodReturn)other).getMethod()) : false;
   }

   public String toString() {
      return "[" + this.m.getSignature() + " : R]";
   }
}
