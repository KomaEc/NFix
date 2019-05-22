package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import soot.SootClass;
import soot.SootMethodRef;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Jimple;
import soot.options.Options;
import soot.tagkit.SourceFileTag;

public class JVirtualInvokeExpr extends AbstractVirtualInvokeExpr {
   public JVirtualInvokeExpr(Value base, SootMethodRef methodRef, List<? extends Value> args) {
      super(Jimple.v().newLocalBox(base), methodRef, new ValueBox[args.size()]);
      if (!Options.v().ignore_resolution_errors()) {
         methodRef.declaringClass().checkLevelIgnoreResolving(1);
         if (methodRef.declaringClass().isInterface()) {
            SootClass sc = methodRef.declaringClass();
            String path = sc.hasTag("SourceFileTag") ? ((SourceFileTag)sc.getTag("SourceFileTag")).getAbsolutePath() : "uknown";
            throw new RuntimeException("Trying to create virtual invoke expression for interface type (" + methodRef.declaringClass().getName() + " in file " + path + "). Use JInterfaceInvokeExpr instead!");
         }
      }

      for(int i = 0; i < args.size(); ++i) {
         this.argBoxes[i] = Jimple.v().newImmediateBox((Value)args.get(i));
      }

   }

   public Object clone() {
      ArrayList<Value> clonedArgs = new ArrayList(this.getArgCount());

      for(int i = 0; i < this.getArgCount(); ++i) {
         clonedArgs.add(i, this.getArg(i));
      }

      return new JVirtualInvokeExpr(this.getBase(), this.methodRef, clonedArgs);
   }
}
