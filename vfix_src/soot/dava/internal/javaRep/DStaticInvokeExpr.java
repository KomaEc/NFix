package soot.dava.internal.javaRep;

import java.util.ArrayList;
import java.util.List;
import soot.SootMethodRef;
import soot.UnitPrinter;
import soot.grimp.Grimp;
import soot.grimp.internal.GStaticInvokeExpr;

public class DStaticInvokeExpr extends GStaticInvokeExpr {
   public DStaticInvokeExpr(SootMethodRef methodRef, List args) {
      super(methodRef, args);
   }

   public void toString(UnitPrinter up) {
      up.type(this.methodRef.declaringClass().getType());
      up.literal(".");
      super.toString(up);
   }

   public Object clone() {
      ArrayList clonedArgs = new ArrayList(this.getArgCount());

      for(int i = 0; i < this.getArgCount(); ++i) {
         clonedArgs.add(i, Grimp.cloneIfNecessary(this.getArg(i)));
      }

      return new DStaticInvokeExpr(this.methodRef, clonedArgs);
   }
}
