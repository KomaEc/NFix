package soot.baf.internal;

import soot.AbstractTrap;
import soot.SootClass;
import soot.Unit;
import soot.baf.Baf;

public class BTrap extends AbstractTrap {
   public BTrap(SootClass exception, Unit beginStmt, Unit endStmt, Unit handlerStmt) {
      super(exception, Baf.v().newInstBox(beginStmt), Baf.v().newInstBox(endStmt), Baf.v().newInstBox(handlerStmt));
   }

   public Object clone() {
      return new BTrap(this.exception, this.getBeginUnit(), this.getEndUnit(), this.getHandlerUnit());
   }
}
