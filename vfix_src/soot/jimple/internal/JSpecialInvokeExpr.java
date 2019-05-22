package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import soot.Local;
import soot.SootMethodRef;
import soot.Value;
import soot.jimple.Jimple;

public class JSpecialInvokeExpr extends AbstractSpecialInvokeExpr {
   public JSpecialInvokeExpr(Local base, SootMethodRef methodRef, List<? extends Value> args) {
      super(Jimple.v().newLocalBox(base), methodRef, new ImmediateBox[args.size()]);

      for(int i = 0; i < args.size(); ++i) {
         this.argBoxes[i] = Jimple.v().newImmediateBox((Value)args.get(i));
      }

   }

   public Object clone() {
      List<Value> clonedArgs = new ArrayList(this.getArgCount());

      for(int i = 0; i < this.getArgCount(); ++i) {
         clonedArgs.add(i, this.getArg(i));
      }

      return new JSpecialInvokeExpr((Local)this.getBase(), this.methodRef, clonedArgs);
   }
}
