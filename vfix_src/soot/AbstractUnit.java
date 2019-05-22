package soot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import soot.tagkit.AbstractHost;
import soot.util.Switch;

public abstract class AbstractUnit extends AbstractHost implements Unit {
   List<UnitBox> boxesPointingToThis = null;

   public abstract Object clone();

   public List<ValueBox> getUseBoxes() {
      return Collections.emptyList();
   }

   public List<ValueBox> getDefBoxes() {
      return Collections.emptyList();
   }

   public List<UnitBox> getUnitBoxes() {
      return Collections.emptyList();
   }

   public List<UnitBox> getBoxesPointingToThis() {
      return this.boxesPointingToThis == null ? Collections.emptyList() : Collections.unmodifiableList(this.boxesPointingToThis);
   }

   public void addBoxPointingToThis(UnitBox b) {
      if (this.boxesPointingToThis == null) {
         this.boxesPointingToThis = new ArrayList();
      }

      this.boxesPointingToThis.add(b);
   }

   public void removeBoxPointingToThis(UnitBox b) {
      if (this.boxesPointingToThis != null) {
         this.boxesPointingToThis.remove(b);
      }

   }

   public void clearUnitBoxes() {
      Iterator var1 = this.getUnitBoxes().iterator();

      while(var1.hasNext()) {
         UnitBox ub = (UnitBox)var1.next();
         ub.setUnit((Unit)null);
      }

   }

   public List<ValueBox> getUseAndDefBoxes() {
      List<ValueBox> useBoxes = this.getUseBoxes();
      List<ValueBox> defBoxes = this.getDefBoxes();
      if (useBoxes.isEmpty()) {
         return defBoxes;
      } else if (defBoxes.isEmpty()) {
         return useBoxes;
      } else {
         List<ValueBox> valueBoxes = new ArrayList();
         valueBoxes.addAll(defBoxes);
         valueBoxes.addAll(useBoxes);
         return valueBoxes;
      }
   }

   public void apply(Switch sw) {
   }

   public void redirectJumpsToThisTo(Unit newLocation) {
      List<UnitBox> boxesPointing = this.getBoxesPointingToThis();
      UnitBox[] boxes = (UnitBox[])boxesPointing.toArray(new UnitBox[boxesPointing.size()]);
      UnitBox[] var4 = boxes;
      int var5 = boxes.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         UnitBox element = var4[var6];
         if (element.getUnit() != this) {
            throw new RuntimeException("Something weird's happening");
         }

         if (element.isBranchTarget()) {
            element.setUnit(newLocation);
         }
      }

   }
}
