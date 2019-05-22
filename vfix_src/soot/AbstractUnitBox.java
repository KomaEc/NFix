package soot;

public abstract class AbstractUnitBox implements UnitBox {
   protected Unit unit;

   public abstract boolean canContainUnit(Unit var1);

   public boolean isBranchTarget() {
      return true;
   }

   public void setUnit(Unit unit) {
      if (!this.canContainUnit(unit)) {
         throw new RuntimeException("attempting to put invalid unit in UnitBox");
      } else {
         if (this.unit != null) {
            this.unit.removeBoxPointingToThis(this);
         }

         this.unit = unit;
         if (this.unit != null) {
            this.unit.addBoxPointingToThis(this);
         }

      }
   }

   public Unit getUnit() {
      return this.unit;
   }

   public void toString(UnitPrinter up) {
      up.startUnitBox(this);
      up.unitRef(this.unit, this.isBranchTarget());
      up.endUnitBox(this);
   }
}
