package soot.jimple.toolkits.typing;

import soot.Body;
import soot.Unit;
import soot.jimple.IdentityStmt;
import soot.jimple.Stmt;

public class Util {
   public static Unit findLastIdentityUnit(Body b, Stmt s) {
      Unit u2 = s;

      for(Object u1 = s; u1 instanceof IdentityStmt; u1 = b.getUnits().getSuccOf((Unit)u1)) {
         u2 = u1;
      }

      return (Unit)u2;
   }

   public static Unit findFirstNonIdentityUnit(Body b, Stmt s) {
      Object u1;
      for(u1 = s; u1 instanceof IdentityStmt; u1 = b.getUnits().getSuccOf((Unit)u1)) {
      }

      return (Unit)u1;
   }
}
