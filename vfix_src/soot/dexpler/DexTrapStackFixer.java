package soot.dexpler;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.Trap;
import soot.Unit;
import soot.javaToJimple.LocalGenerator;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.Stmt;

public class DexTrapStackFixer extends BodyTransformer {
   public static DexTrapStackFixer v() {
      return new DexTrapStackFixer();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      Iterator var4 = b.getTraps().iterator();

      while(var4.hasNext()) {
         Trap t = (Trap)var4.next();
         if (!this.isCaughtExceptionRef(t.getHandlerUnit())) {
            Local l = (new LocalGenerator(b)).generateLocal(t.getException().getType());
            Stmt caughtStmt = Jimple.v().newIdentityStmt(l, Jimple.v().newCaughtExceptionRef());
            b.getUnits().add((Unit)caughtStmt);
            b.getUnits().add((Unit)Jimple.v().newGotoStmt(t.getHandlerUnit()));
            t.setHandlerUnit(caughtStmt);
         }
      }

   }

   private boolean isCaughtExceptionRef(Unit handlerUnit) {
      if (!(handlerUnit instanceof IdentityStmt)) {
         return false;
      } else {
         IdentityStmt stmt = (IdentityStmt)handlerUnit;
         return stmt.getRightOp() instanceof CaughtExceptionRef;
      }
   }
}
