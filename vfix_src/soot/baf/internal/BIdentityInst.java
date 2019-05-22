package soot.baf.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.baf.IdentityInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BIdentityInst extends AbstractInst implements IdentityInst {
   ValueBox leftBox;
   ValueBox rightBox;
   List defBoxes;

   public Value getLeftOp() {
      return this.leftBox.getValue();
   }

   public int getInCount() {
      return 0;
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

   public Value getRightOp() {
      return this.rightBox.getValue();
   }

   public ValueBox getLeftOpBox() {
      return this.leftBox;
   }

   public ValueBox getRightOpBox() {
      return this.rightBox;
   }

   public List getDefBoxes() {
      return this.defBoxes;
   }

   public List getUseBoxes() {
      List list = new ArrayList();
      list.addAll(this.rightBox.getValue().getUseBoxes());
      list.add(this.rightBox);
      list.addAll(this.leftBox.getValue().getUseBoxes());
      return list;
   }

   public BIdentityInst(Value local, Value identityValue) {
      this(Baf.v().newLocalBox(local), Baf.v().newIdentityRefBox(identityValue));
   }

   protected BIdentityInst(ValueBox localBox, ValueBox identityValueBox) {
      this.leftBox = localBox;
      this.rightBox = identityValueBox;
      this.defBoxes = Collections.singletonList(this.leftBox);
   }

   public Object clone() {
      return new BIdentityInst(this.getLeftOp(), this.getRightOp());
   }

   public String toString() {
      return this.leftBox.getValue().toString() + " := " + this.rightBox.getValue().toString();
   }

   public void toString(UnitPrinter up) {
      this.leftBox.toString(up);
      up.literal(" := ");
      this.rightBox.toString(up);
   }

   public final String getName() {
      return ":=";
   }

   public void setLeftOp(Value local) {
      this.leftBox.setValue(local);
   }

   public void setRightOp(Value identityRef) {
      this.rightBox.setValue(identityRef);
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseIdentityInst(this);
   }
}
