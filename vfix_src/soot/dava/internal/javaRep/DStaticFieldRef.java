package soot.dava.internal.javaRep;

import soot.SootFieldRef;
import soot.UnitPrinter;
import soot.jimple.StaticFieldRef;

public class DStaticFieldRef extends StaticFieldRef {
   private boolean supressDeclaringClass;

   public void toString(UnitPrinter up) {
      if (!this.supressDeclaringClass) {
         up.type(this.fieldRef.declaringClass().getType());
         up.literal(".");
      }

      up.fieldRef(this.fieldRef);
   }

   public DStaticFieldRef(SootFieldRef fieldRef, String myClassName) {
      super(fieldRef);
      this.supressDeclaringClass = myClassName.equals(fieldRef.declaringClass().getName());
   }

   public DStaticFieldRef(SootFieldRef fieldRef, boolean supressDeclaringClass) {
      super(fieldRef);
      this.supressDeclaringClass = supressDeclaringClass;
   }

   public Object clone() {
      return new DStaticFieldRef(this.fieldRef, this.supressDeclaringClass);
   }
}
