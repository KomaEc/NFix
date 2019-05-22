package soot.baf.internal;

import java.util.Collections;
import java.util.List;
import soot.Local;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.baf.IncInst;
import soot.baf.InstSwitch;
import soot.jimple.Constant;
import soot.util.Switch;

public class BIncInst extends AbstractInst implements IncInst {
   final ValueBox localBox;
   final ValueBox defLocalBox;
   final List<ValueBox> useBoxes;
   final List<ValueBox> mDefBoxes;
   Constant mConstant;

   public BIncInst(Local local, Constant constant) {
      this.mConstant = constant;
      this.localBox = new BafLocalBox(local);
      this.useBoxes = Collections.singletonList(this.localBox);
      this.defLocalBox = new BafLocalBox(local);
      this.mDefBoxes = Collections.singletonList(this.defLocalBox);
   }

   public int getInCount() {
      return 0;
   }

   public Object clone() {
      return new BIncInst(this.getLocal(), this.getConstant());
   }

   public int getInMachineCount() {
      return 0;
   }

   public int getOutCount() {
      return 0;
   }

   public int getOutMachineCount() {
      return 0;
   }

   public Constant getConstant() {
      return this.mConstant;
   }

   public void setConstant(Constant aConstant) {
      this.mConstant = aConstant;
   }

   public final String getName() {
      return "inc.i";
   }

   final String getParameters() {
      return " " + this.localBox.getValue().toString();
   }

   protected void getParameters(UnitPrinter up) {
      up.literal(" ");
      this.localBox.toString(up);
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseIncInst(this);
   }

   public void setLocal(Local l) {
      this.localBox.setValue(l);
   }

   public Local getLocal() {
      return (Local)this.localBox.getValue();
   }

   public List<ValueBox> getUseBoxes() {
      return this.useBoxes;
   }

   public List<ValueBox> getDefBoxes() {
      return this.mDefBoxes;
   }

   public String toString() {
      return "inc.i " + this.getLocal() + " " + this.getConstant();
   }

   public void toString(UnitPrinter up) {
      up.literal("inc.i");
      up.literal(" ");
      this.localBox.toString(up);
      up.literal(" ");
      up.constant(this.mConstant);
   }
}
