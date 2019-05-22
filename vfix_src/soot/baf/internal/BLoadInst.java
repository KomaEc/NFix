package soot.baf.internal;

import java.util.Collections;
import java.util.List;
import soot.Local;
import soot.Type;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.baf.InstSwitch;
import soot.baf.LoadInst;
import soot.util.Switch;

public class BLoadInst extends AbstractOpTypeInst implements LoadInst {
   ValueBox localBox;
   List useBoxes;

   public BLoadInst(Type opType, Local local) {
      super(opType);
      this.localBox = new BafLocalBox(local);
      this.useBoxes = Collections.singletonList(this.localBox);
   }

   public int getInCount() {
      return 0;
   }

   public Object clone() {
      return new BLoadInst(this.getOpType(), this.getLocal());
   }

   public int getInMachineCount() {
      return 0;
   }

   public int getOutCount() {
      return 1;
   }

   public final String getName() {
      return "load";
   }

   final String getParameters() {
      return " " + this.localBox.getValue().toString();
   }

   protected void getParameters(UnitPrinter up) {
      up.literal(" ");
      this.localBox.toString(up);
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseLoadInst(this);
   }

   public void setLocal(Local l) {
      this.localBox.setValue(l);
   }

   public Local getLocal() {
      return (Local)this.localBox.getValue();
   }

   public List getUseBoxes() {
      return this.useBoxes;
   }
}
