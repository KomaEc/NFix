package soot.jimple.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.ValueBox;
import soot.jimple.SwitchStmt;

public abstract class AbstractSwitchStmt extends AbstractStmt implements SwitchStmt {
   final UnitBox defaultTargetBox;
   final ValueBox keyBox;
   final List<UnitBox> stmtBoxes;
   protected final UnitBox[] targetBoxes;

   protected AbstractSwitchStmt(ValueBox keyBox, UnitBox defaultTargetBox, UnitBox... targetBoxes) {
      this.keyBox = keyBox;
      this.defaultTargetBox = defaultTargetBox;
      this.targetBoxes = targetBoxes;
      List<UnitBox> list = new ArrayList();
      this.stmtBoxes = Collections.unmodifiableList(list);
      Collections.addAll(list, targetBoxes);
      list.add(defaultTargetBox);
   }

   public final Unit getDefaultTarget() {
      return this.defaultTargetBox.getUnit();
   }

   public final void setDefaultTarget(Unit defaultTarget) {
      this.defaultTargetBox.setUnit(defaultTarget);
   }

   public final UnitBox getDefaultTargetBox() {
      return this.defaultTargetBox;
   }

   public final Value getKey() {
      return this.keyBox.getValue();
   }

   public final void setKey(Value key) {
      this.keyBox.setValue(key);
   }

   public final ValueBox getKeyBox() {
      return this.keyBox;
   }

   public final List<ValueBox> getUseBoxes() {
      List<ValueBox> list = new ArrayList();
      list.addAll(this.keyBox.getValue().getUseBoxes());
      list.add(this.keyBox);
      return list;
   }

   public final int getTargetCount() {
      return this.targetBoxes.length;
   }

   public final Unit getTarget(int index) {
      return this.targetBoxes[index].getUnit();
   }

   public final UnitBox getTargetBox(int index) {
      return this.targetBoxes[index];
   }

   public final void setTarget(int index, Unit target) {
      this.targetBoxes[index].setUnit(target);
   }

   public final List<Unit> getTargets() {
      List<Unit> targets = new ArrayList();
      UnitBox[] var2 = this.targetBoxes;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         UnitBox element = var2[var4];
         targets.add(element.getUnit());
      }

      return targets;
   }

   public final void setTargets(List<? extends Unit> targets) {
      for(int i = 0; i < targets.size(); ++i) {
         this.targetBoxes[i].setUnit((Unit)targets.get(i));
      }

   }

   public final void setTargets(Unit[] targets) {
      for(int i = 0; i < targets.length; ++i) {
         this.targetBoxes[i].setUnit(targets[i]);
      }

   }

   public final List<UnitBox> getUnitBoxes() {
      return this.stmtBoxes;
   }

   public final boolean fallsThrough() {
      return false;
   }

   public final boolean branches() {
      return true;
   }
}
