package soot.jimple.internal;

import soot.AbstractUnitBox;
import soot.Unit;
import soot.jimple.Stmt;

public class StmtBox extends AbstractUnitBox {
   public StmtBox(Stmt s) {
      this.setUnit(s);
   }

   public boolean canContainUnit(Unit u) {
      return u instanceof Stmt || u == null;
   }
}
