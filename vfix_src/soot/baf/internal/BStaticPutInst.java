package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.SootField;
import soot.SootFieldRef;
import soot.UnitPrinter;
import soot.baf.InstSwitch;
import soot.baf.StaticPutInst;
import soot.util.Switch;

public class BStaticPutInst extends AbstractInst implements StaticPutInst {
   SootFieldRef fieldRef;

   public BStaticPutInst(SootFieldRef fieldRef) {
      if (!fieldRef.isStatic()) {
         throw new RuntimeException("wrong static-ness");
      } else {
         this.fieldRef = fieldRef;
      }
   }

   public int getInCount() {
      return 1;
   }

   public Object clone() {
      return new BStaticPutInst(this.fieldRef);
   }

   public int getInMachineCount() {
      return AbstractJasminClass.sizeOfType(this.fieldRef.type());
   }

   public int getOutCount() {
      return 0;
   }

   public int getOutMachineCount() {
      return 0;
   }

   public final String getName() {
      return "staticput";
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
      ((InstSwitch)sw).caseStaticPutInst(this);
   }

   public boolean containsFieldRef() {
      return true;
   }
}
