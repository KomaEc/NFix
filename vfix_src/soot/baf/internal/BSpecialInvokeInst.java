package soot.baf.internal;

import soot.SootMethodRef;
import soot.baf.InstSwitch;
import soot.baf.SpecialInvokeInst;
import soot.util.Switch;

public class BSpecialInvokeInst extends AbstractInvokeInst implements SpecialInvokeInst {
   public BSpecialInvokeInst(SootMethodRef methodRef) {
      if (methodRef.isStatic()) {
         throw new RuntimeException("wrong static-ness");
      } else {
         this.methodRef = methodRef;
      }
   }

   public int getInCount() {
      return super.getInCount() + 1;
   }

   public int getInMachineCount() {
      return super.getInMachineCount() + 1;
   }

   public Object clone() {
      return new BSpecialInvokeInst(this.methodRef);
   }

   public String getName() {
      return "specialinvoke";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseSpecialInvokeInst(this);
   }
}
