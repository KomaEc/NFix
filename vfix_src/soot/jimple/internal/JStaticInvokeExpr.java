package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import soot.SootMethodRef;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Jimple;

public class JStaticInvokeExpr extends AbstractStaticInvokeExpr {
   public JStaticInvokeExpr(SootMethodRef methodRef, List<? extends Value> args) {
      super(methodRef, new ValueBox[args.size()]);

      for(int i = 0; i < args.size(); ++i) {
         this.argBoxes[i] = Jimple.v().newImmediateBox((Value)args.get(i));
      }

   }

   public Object clone() {
      List<Value> clonedArgs = new ArrayList(this.getArgCount());

      for(int i = 0; i < this.getArgCount(); ++i) {
         clonedArgs.add(i, this.getArg(i));
      }

      return new JStaticInvokeExpr(this.methodRef, clonedArgs);
   }
}
