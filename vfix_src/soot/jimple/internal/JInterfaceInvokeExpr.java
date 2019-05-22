package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import soot.SootMethodRef;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Jimple;

public class JInterfaceInvokeExpr extends AbstractInterfaceInvokeExpr {
   public JInterfaceInvokeExpr(Value base, SootMethodRef methodRef, List<? extends Value> args) {
      super(Jimple.v().newLocalBox(base), methodRef, new ValueBox[args.size()]);
      methodRef.declaringClass().checkLevelIgnoreResolving(1);
      if (!methodRef.declaringClass().isInterface() && !methodRef.declaringClass().isPhantom()) {
         throw new RuntimeException("Trying to create interface invoke expression for non-interface type: " + methodRef.declaringClass() + " Use JVirtualInvokeExpr or JSpecialInvokeExpr instead!");
      } else {
         for(int i = 0; i < args.size(); ++i) {
            this.argBoxes[i] = Jimple.v().newImmediateBox((Value)args.get(i));
         }

      }
   }

   public Object clone() {
      List<Value> argList = new ArrayList(this.getArgCount());

      for(int i = 0; i < this.getArgCount(); ++i) {
         argList.add(i, Jimple.cloneIfNecessary(this.getArg(i)));
      }

      return new JInterfaceInvokeExpr(Jimple.cloneIfNecessary(this.getBase()), this.methodRef, argList);
   }
}
