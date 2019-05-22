package soot.grimp.internal;

import java.util.ArrayList;
import java.util.List;
import soot.SootMethodRef;
import soot.Value;
import soot.ValueBox;
import soot.grimp.Grimp;
import soot.jimple.internal.AbstractStaticInvokeExpr;

public class GStaticInvokeExpr extends AbstractStaticInvokeExpr {
   public GStaticInvokeExpr(SootMethodRef methodRef, List args) {
      super(methodRef, new ValueBox[args.size()]);

      for(int i = 0; i < args.size(); ++i) {
         this.argBoxes[i] = Grimp.v().newExprBox((Value)args.get(i));
      }

   }

   public Object clone() {
      ArrayList clonedArgs = new ArrayList(this.getArgCount());

      for(int i = 0; i < this.getArgCount(); ++i) {
         clonedArgs.add(i, Grimp.cloneIfNecessary(this.getArg(i)));
      }

      return new GStaticInvokeExpr(this.methodRef, clonedArgs);
   }
}
