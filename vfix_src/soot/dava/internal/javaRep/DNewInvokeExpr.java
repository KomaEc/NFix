package soot.dava.internal.javaRep;

import java.util.ArrayList;
import java.util.List;
import soot.RefType;
import soot.SootMethodRef;
import soot.grimp.Grimp;
import soot.grimp.internal.GNewInvokeExpr;

public class DNewInvokeExpr extends GNewInvokeExpr {
   public DNewInvokeExpr(RefType type, SootMethodRef methodRef, List args) {
      super(type, methodRef, args);
   }

   public Object clone() {
      ArrayList clonedArgs = new ArrayList(this.getArgCount());

      for(int i = 0; i < this.getArgCount(); ++i) {
         clonedArgs.add(i, Grimp.cloneIfNecessary(this.getArg(i)));
      }

      return new DNewInvokeExpr((RefType)this.getType(), this.methodRef, clonedArgs);
   }
}
