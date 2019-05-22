package soot.jimple;

import soot.RefType;
import soot.SootMethodRef;
import soot.Type;
import soot.util.Switch;

public class MethodHandle extends Constant {
   public final SootMethodRef methodRef;
   public int tag;

   private MethodHandle(SootMethodRef ref, int tag) {
      this.methodRef = ref;
      this.tag = tag;
   }

   public static MethodHandle v(SootMethodRef ref, int tag) {
      return new MethodHandle(ref, tag);
   }

   public String toString() {
      return "handle: " + this.methodRef;
   }

   public Type getType() {
      return RefType.v("java.lang.invoke.MethodHandle");
   }

   public SootMethodRef getMethodRef() {
      return this.methodRef;
   }

   public void apply(Switch sw) {
      ((ConstantSwitch)sw).caseMethodHandle(this);
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.methodRef == null ? 0 : this.methodRef.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         MethodHandle other = (MethodHandle)obj;
         if (this.methodRef == null) {
            if (other.methodRef != null) {
               return false;
            }
         } else if (!this.methodRef.equals(other.methodRef)) {
            return false;
         }

         return true;
      }
   }
}
