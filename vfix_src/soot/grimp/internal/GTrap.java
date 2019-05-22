package soot.grimp.internal;

import soot.AbstractTrap;
import soot.SootClass;
import soot.Unit;
import soot.grimp.Grimp;

public class GTrap extends AbstractTrap {
   public GTrap(SootClass exception, Unit beginStmt, Unit endStmt, Unit handlerStmt) {
      super(exception, Grimp.v().newStmtBox(beginStmt), Grimp.v().newStmtBox(endStmt), Grimp.v().newStmtBox(handlerStmt));
   }

   public Object clone() {
      return new GTrap(this.exception, this.getBeginUnit(), this.getEndUnit(), this.getHandlerUnit());
   }
}
