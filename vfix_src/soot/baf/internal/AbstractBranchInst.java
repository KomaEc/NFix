package soot.baf.internal;

import java.util.Collections;
import java.util.List;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPrinter;
import soot.util.Switch;

public abstract class AbstractBranchInst extends AbstractInst {
   UnitBox targetBox;
   final List<UnitBox> targetBoxes;

   AbstractBranchInst(UnitBox targetBox) {
      this.targetBox = targetBox;
      this.targetBoxes = Collections.singletonList(targetBox);
   }

   public abstract String getName();

   public String toString() {
      String target = "";
      Unit targetUnit = this.getTarget();
      if (this == targetUnit) {
         target = this.getName();
      } else {
         target = this.getTarget().toString();
      }

      return this.getName() + " " + target;
   }

   public void toString(UnitPrinter up) {
      up.literal(this.getName());
      up.literal(" ");
      this.targetBox.toString(up);
   }

   public Unit getTarget() {
      return this.targetBox.getUnit();
   }

   public void setTarget(Unit target) {
      this.targetBox.setUnit(target);
   }

   public UnitBox getTargetBox() {
      return this.targetBox;
   }

   public List<UnitBox> getUnitBoxes() {
      return this.targetBoxes;
   }

   public abstract void apply(Switch var1);

   public boolean branches() {
      return true;
   }
}
