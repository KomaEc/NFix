package soot.baf.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPrinter;
import soot.baf.Baf;
import soot.baf.InstSwitch;
import soot.baf.TableSwitchInst;
import soot.util.Switch;

public class BTableSwitchInst extends AbstractInst implements TableSwitchInst {
   UnitBox defaultTargetBox;
   int lowIndex;
   int highIndex;
   UnitBox[] targetBoxes;
   List unitBoxes;

   public BTableSwitchInst(Unit defaultTarget, int lowIndex, int highIndex, List targets) {
      this.defaultTargetBox = Baf.v().newInstBox(defaultTarget);
      this.targetBoxes = new UnitBox[targets.size()];

      for(int i = 0; i < this.targetBoxes.length; ++i) {
         this.targetBoxes[i] = Baf.v().newInstBox((Unit)targets.get(i));
      }

      this.lowIndex = lowIndex;
      this.highIndex = highIndex;
      this.unitBoxes = new ArrayList();
      UnitBox[] var9 = this.targetBoxes;
      int var6 = var9.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         UnitBox element = var9[var7];
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

      return new BTableSwitchInst(this.defaultTargetBox.getUnit(), this.lowIndex, this.highIndex, list);
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

   public void setLowIndex(int lowIndex) {
      this.lowIndex = lowIndex;
   }

   public void setHighIndex(int highIndex) {
      this.highIndex = highIndex;
   }

   public int getLowIndex() {
      return this.lowIndex;
   }

   public int getHighIndex() {
      return this.highIndex;
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
      return "tableswitch";
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer();
      String endOfLine = " ";
      buffer.append("tableswitch" + endOfLine);
      buffer.append("{" + endOfLine);

      for(int i = this.lowIndex; i < this.highIndex; ++i) {
         buffer.append("    case " + i + ": goto " + this.getTarget(i - this.lowIndex) + ";" + endOfLine);
      }

      buffer.append("    case " + this.highIndex + ": goto " + this.getTarget(this.highIndex - this.lowIndex) + ";" + endOfLine);
      buffer.append("    default: goto " + this.getDefaultTarget() + ";" + endOfLine);
      buffer.append("}");
      return buffer.toString();
   }

   public void toString(UnitPrinter up) {
      up.literal("tableswitch");
      up.newline();
      up.literal("{");
      up.newline();

      for(int i = this.lowIndex; i < this.highIndex; ++i) {
         this.printCaseTarget(up, i);
      }

      this.printCaseTarget(up, this.highIndex);
      up.literal("    default: goto ");
      this.defaultTargetBox.toString(up);
      up.literal(";");
      up.newline();
      up.literal("}");
   }

   private void printCaseTarget(UnitPrinter up, int targetIndex) {
      up.literal("    case ");
      up.literal((new Integer(targetIndex)).toString());
      up.literal(": goto ");
      this.targetBoxes[targetIndex - this.lowIndex].toString(up);
      up.literal(";");
      up.newline();
   }

   public List getUnitBoxes() {
      return this.unitBoxes;
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseTableSwitchInst(this);
   }

   public boolean fallsThrough() {
      return false;
   }

   public boolean branches() {
      return true;
   }
}
