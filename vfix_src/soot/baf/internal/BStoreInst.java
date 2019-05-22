package soot.baf.internal;

import java.util.Collections;
import java.util.List;
import soot.AbstractJasminClass;
import soot.Local;
import soot.Type;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.baf.InstSwitch;
import soot.baf.StoreInst;
import soot.util.Switch;

public class BStoreInst extends AbstractOpTypeInst implements StoreInst {
   ValueBox localBox;
   List defBoxes;

   public BStoreInst(Type opType, Local local) {
      super(opType);
      this.localBox = new BafLocalBox(local);
      this.defBoxes = Collections.singletonList(this.localBox);
   }

   public int getInCount() {
      return 1;
   }

   public Object clone() {
      return new BStoreInst(this.getOpType(), this.getLocal());
   }

   public int getInMachineCount() {
      return AbstractJasminClass.sizeOfType(this.getOpType());
   }

   public int getOutCount() {
      return 0;
   }

   public int getOutMachineCount() {
      return 0;
   }

   public final String getName() {
      return "store";
   }

   final String getParameters() {
      return " " + this.localBox.getValue().toString();
   }

   protected void getParameters(UnitPrinter up) {
      up.literal(" ");
      this.localBox.toString(up);
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseStoreInst(this);
   }

   public void setLocal(Local l) {
      this.localBox.setValue(l);
   }

   public Local getLocal() {
      return (Local)this.localBox.getValue();
   }

   public List getDefBoxes() {
      return this.defBoxes;
   }
}
