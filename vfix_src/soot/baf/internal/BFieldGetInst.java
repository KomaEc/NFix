package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.SootField;
import soot.SootFieldRef;
import soot.UnitPrinter;
import soot.baf.FieldGetInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BFieldGetInst extends AbstractInst implements FieldGetInst {
   SootFieldRef fieldRef;

   public BFieldGetInst(SootFieldRef fieldRef) {
      if (fieldRef.isStatic()) {
         throw new RuntimeException("wrong static-ness");
      } else {
         this.fieldRef = fieldRef;
      }
   }

   public int getInCount() {
      return 1;
   }

   public Object clone() {
      return new BFieldGetInst(this.fieldRef);
   }

   public int getInMachineCount() {
      return 1;
   }

   public int getOutCount() {
      return 1;
   }

   public int getOutMachineCount() {
      return AbstractJasminClass.sizeOfType(this.fieldRef.type());
   }

   public final String getName() {
      return "fieldget";
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
      ((InstSwitch)sw).caseFieldGetInst(this);
   }

   public boolean containsFieldRef() {
      return true;
   }
}
