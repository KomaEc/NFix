package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.SootField;
import soot.SootFieldRef;
import soot.UnitPrinter;
import soot.baf.FieldPutInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BFieldPutInst extends AbstractInst implements FieldPutInst {
   SootFieldRef fieldRef;

   public BFieldPutInst(SootFieldRef fieldRef) {
      if (fieldRef.isStatic()) {
         throw new RuntimeException("wrong static-ness");
      } else {
         this.fieldRef = fieldRef;
      }
   }

   public int getInCount() {
      return 2;
   }

   public int getOutCount() {
      return 0;
   }

   public Object clone() {
      return new BFieldPutInst(this.fieldRef);
   }

   public int getInMachineCount() {
      return AbstractJasminClass.sizeOfType(this.fieldRef.type()) + 1;
   }

   public int getOutMachineCount() {
      return 0;
   }

   public final String getName() {
      return "fieldput";
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
      ((InstSwitch)sw).caseFieldPutInst(this);
   }

   public boolean containsFieldRef() {
      return true;
   }
}
