package soot.baf;

import soot.AbstractUnitBox;
import soot.Unit;

class InstBox extends AbstractUnitBox {
   InstBox(Inst s) {
      this.setUnit(s);
   }

   public boolean canContainUnit(Unit u) {
      return u instanceof Inst || u == null;
   }
}
