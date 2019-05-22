package soot.baf.internal;

import soot.SootMethodRef;
import soot.baf.InstSwitch;
import soot.baf.VirtualInvokeInst;
import soot.util.Switch;

public class BVirtualInvokeInst extends AbstractInvokeInst implements VirtualInvokeInst {
   public BVirtualInvokeInst(SootMethodRef methodRef) {
      if (methodRef.isStatic()) {
         throw new RuntimeException("wrong static-ness");
      } else {
         this.methodRef = methodRef;
      }
   }

   public int getInMachineCount() {
      return super.getInMachineCount() + 1;
   }

   public int getInCount() {
      return super.getInCount() + 1;
   }

   public Object clone() {
      return new BVirtualInvokeInst(this.methodRef);
   }

   public final String getName() {
      return "virtualinvoke";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseVirtualInvokeInst(this);
   }
}
