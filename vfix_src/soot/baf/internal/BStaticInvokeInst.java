package soot.baf.internal;

import soot.SootMethodRef;
import soot.VoidType;
import soot.baf.InstSwitch;
import soot.baf.StaticInvokeInst;
import soot.util.Switch;

public class BStaticInvokeInst extends AbstractInvokeInst implements StaticInvokeInst {
   public BStaticInvokeInst(SootMethodRef methodRef) {
      if (!methodRef.isStatic()) {
         throw new RuntimeException("wrong static-ness");
      } else {
         this.methodRef = methodRef;
      }
   }

   public int getInCount() {
      return this.methodRef.parameterTypes().size();
   }

   public Object clone() {
      return new BStaticInvokeInst(this.methodRef);
   }

   public int getOutCount() {
      return this.methodRef.returnType() instanceof VoidType ? 0 : 1;
   }

   public String getName() {
      return "staticinvoke";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseStaticInvokeInst(this);
   }
}
