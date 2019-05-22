package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.SootField;
import soot.SootFieldRef;
import soot.UnitPrinter;
import soot.baf.InstSwitch;
import soot.baf.StaticGetInst;
import soot.util.Switch;

public class BStaticGetInst extends AbstractInst implements StaticGetInst {
   SootFieldRef fieldRef;

   public BStaticGetInst(SootFieldRef fieldRef) {
      if (!fieldRef.isStatic()) {
         throw new RuntimeException("wrong static-ness");
      } else {
         this.fieldRef = fieldRef;
      }
   }

   public int getInCount() {
      return 0;
   }

   public Object clone() {
      return new BStaticGetInst(this.fieldRef);
   }

   public int getInMachineCount() {
      return 0;
   }

   public int getOutCount() {
      return 1;
   }

   public int getOutMachineCount() {
      return AbstractJasminClass.sizeOfType(this.fieldRef.type());
   }

   public final String getName() {
      return "staticget";
   }

   final String getParameters() {
      return " " + this.fieldRef.getSignature();
   }

   protected void getParameters(UnitPrinter up) {
      up.literal(" ");
      up.fieldRef(this.fieldRef);
   }

   public SootFieldRef getFieldRef() {
      return this.fieldRef;
   }

   public SootField getField() {
      return this.fieldRef.resolve();
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseStaticGetInst(this);
   }

   public boolean containsFieldRef() {
      return true;
   }
}
