package soot.jimple.toolkits.annotation.arraycheck;

import soot.Local;
import soot.SootMethod;

class ArrayReferenceNode {
   private final SootMethod m;
   private final Local l;

   public ArrayReferenceNode(SootMethod method, Local local) {
      this.m = method;
      this.l = local;
   }

   public SootMethod getMethod() {
      return this.m;
   }

   public Local getLocal() {
      return this.l;
   }

   public int hashCode() {
      return this.m.hashCode() + this.l.hashCode() + 1;
   }

   public boolean equals(Object other) {
      if (!(other instanceof ArrayReferenceNode)) {
         return false;
      } else {
         ArrayReferenceNode another = (ArrayReferenceNode)other;
         return this.m.equals(another.getMethod()) && this.l.equals(another.getLocal());
      }
   }

   public String toString() {
      return "[" + this.m.getSignature() + " : " + this.l.toString() + "[ ]";
   }
}
