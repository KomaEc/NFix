package soot.baf.internal;

import soot.SootMethodRef;
import soot.UnitPrinter;
import soot.baf.InstSwitch;
import soot.baf.InterfaceInvokeInst;
import soot.util.Switch;

public class BInterfaceInvokeInst extends AbstractInvokeInst implements InterfaceInvokeInst {
   int argCount;

   public int getInCount() {
      return this.methodRef.parameterTypes().size() + 1;
   }

   public int getInMachineCount() {
      return super.getInMachineCount() + 1;
   }

   public BInterfaceInvokeInst(SootMethodRef methodRef, int argCount) {
      if (methodRef.isStatic()) {
         throw new RuntimeException("wrong static-ness");
      } else {
         this.methodRef = methodRef;
         this.argCount = argCount;
      }
   }

   public Object clone() {
      return new BInterfaceInvokeInst(this.methodRef, this.getArgCount());
   }

   public final String getName() {
      return "interfaceinvoke";
   }

   final String getParameters() {
      return super.getParameters() + " " + this.argCount;
   }

   protected void getParameters(UnitPrinter up) {
      super.getParameters(up);
      up.literal(" ");
      up.literal((new Integer(this.argCount)).toString());
   }

   public int getArgCount() {
      return this.argCount;
   }

   public void setArgCount(int x) {
      this.argCount = x;
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseInterfaceInvokeInst(this);
   }
}
