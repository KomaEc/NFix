package soot.jimple.internal;

import soot.AbstractTrap;
import soot.SootClass;
import soot.Unit;
import soot.UnitBox;
import soot.jimple.Jimple;

public class JTrap extends AbstractTrap {
   public JTrap(SootClass exception, Unit beginStmt, Unit endStmt, Unit handlerStmt) {
      super(exception, Jimple.v().newStmtBox(beginStmt), Jimple.v().newStmtBox(endStmt), Jimple.v().newStmtBox(handlerStmt));
   }

   public JTrap(SootClass exception, UnitBox beginStmt, UnitBox endStmt, UnitBox handlerStmt) {
      super(exception, beginStmt, endStmt, handlerStmt);
   }

   public Object clone() {
      return new JTrap(this.exception, this.getBeginUnit(), this.getEndUnit(), this.getHandlerUnit());
   }

   public String toString() {
      StringBuffer buf = new StringBuffer("Trap :");
      buf.append("\nbegin  : ");
      buf.append(this.getBeginUnit());
      buf.append("\nend    : ");
      buf.append(this.getEndUnit());
      buf.append("\nhandler: ");
      buf.append(this.getHandlerUnit());
      return new String(buf);
   }
}
