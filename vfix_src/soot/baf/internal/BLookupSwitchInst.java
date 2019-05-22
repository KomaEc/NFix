package soot.baf.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPrinter;
import soot.baf.Baf;
import soot.baf.InstSwitch;
import soot.baf.LookupSwitchInst;
import soot.jimple.Constant;
import soot.util.Switch;

public class BLookupSwitchInst extends AbstractInst implements LookupSwitchInst {
   UnitBox defaultTargetBox;
   List lookupValues;
   UnitBox[] targetBoxes;
   List unitBoxes;

   public BLookupSwitchInst(Unit defaultTarget, List lookupValues, List targets) {
      this.defaultTargetBox = Baf.v().newInstBox(defaultTarget);
      this.targetBoxes = new UnitBox[targets.size()];

      for(int i = 0; i < this.targetBoxes.length; ++i) {
         this.targetBoxes[i] = Baf.v().newInstBox((Unit)targets.get(i));
      }

      this.lookupValues = new ArrayList();
      this.lookupValues.addAll(lookupValues);
      this.unitBoxes = new ArrayList();
      UnitBox[] var8 = this.targetBoxes;
      int var5 = var8.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         UnitBox element = var8[var6];
         this.unitBoxes.add(element);
      }

      this.unitBoxes.add(this.defaultTargetBox);
      this.unitBoxes = Collections.unmodifiableList(this.unitBoxes);
   }

   public Object clone() {
      List list = new ArrayList();
      UnitBox[] var2 = this.targetBoxes;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         UnitBox element = var2[var4];
         list.add(element.getUnit());
      }

      return new BLookupSwitchInst(this.defaultTargetBox.getUnit(), this.lookupValues, list);
   }

   public int getInCount() {
      return 1;
   }

   public int getInMachineCount() {
      return 1;
   }

   public int getOutCount() {
      return 0;
   }

   public int getOutMachineCount() {
      return 0;
   }

   public Unit getDefaultTarget() {
      return this.defaultTargetBox.getUnit();
   }

   public void setDefaultTarget(Unit defaultTarget) {
      this.defaultTargetBox.setUnit(defaultTarget);
   }

   public UnitBox getDefaultTargetBox() {
      return this.defaultTargetBox;
   }

   public void setLookupValues(List lookupValues) {
      this.lookupValues = new ArrayList();
      this.lookupValues.addAll(lookupValues);
   }

   public void setLookupValue(int index, int value) {
      this.lookupValues.set(index, new Integer(value));
   }

   public int getLookupValue(int index) {
      return (Integer)this.lookupValues.get(index);
   }

   public List getLookupValues() {
      return Collections.unmodifiableList(this.lookupValues);
   }

   public int getTargetCount() {
      return this.targetBoxes.length;
   }

   public Unit getTarget(int index) {
      return this.targetBoxes[index].getUnit();
   }

   public void setTarget(int index, Unit target) {
      this.targetBoxes[index].setUnit(target);
   }

   public void setTargets(List<Unit> targets) {
      for(int i = 0; i < targets.size(); ++i) {
         this.targetBoxes[i].setUnit((Unit)targets.get(i));
      }

   }

   public UnitBox getTargetBox(int index) {
      return this.targetBoxes[index];
   }

   public List getTargets() {
      List targets = new ArrayList();
      UnitBox[] var2 = this.targetBoxes;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         UnitBox element = var2[var4];
         targets.add(element.getUnit());
      }

      return targets;
   }

   public String getName() {
      return "lookupswitch";
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer();
      String endOfLine = " ";
      buffer.append("lookupswitch" + endOfLine);
      buffer.append("{" + endOfLine);

      for(int i = 0; i < this.lookupValues.size(); ++i) {
         buffer.append("    case " + this.lookupValues.get(i) + ": goto " + this.getTarget(i) + ";" + endOfLine);
      }

      buffer.append("    default: goto " + this.getDefaultTarget() + ";" + endOfLine);
      buffer.append("}");
      return buffer.toString();
   }

   public void toString(UnitPrinter up) {
      up.literal("lookupswitch");
      up.newline();
      up.literal("{");
      up.newline();

      for(int i = 0; i < this.lookupValues.size(); ++i) {
         up.literal("    case ");
         up.constant((Constant)this.lookupValues.get(i));
         up.literal(": goto ");
         this.targetBoxes[i].toString(up);
         up.literal(";");
         up.newline();
      }

      up.literal("    default: goto ");
      this.defaultTargetBox.toString(up);
      up.literal(";");
      up.newline();
      up.literal("}");
   }

   public List getUnitBoxes() {
      return this.unitBoxes;
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseLookupSwitchInst(this);
   }

   public boolean fallsThrough() {
      return false;
   }

   public boolean branches() {
      return true;
   }
}
