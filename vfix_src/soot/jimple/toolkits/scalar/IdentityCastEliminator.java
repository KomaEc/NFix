package soot.jimple.toolkits.scalar;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.Singletons;
import soot.Type;
import soot.Unit;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;

public class IdentityCastEliminator extends BodyTransformer {
   public IdentityCastEliminator(Singletons.Global g) {
   }

   public static IdentityCastEliminator v() {
      return G.v().soot_jimple_toolkits_scalar_IdentityCastEliminator();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      Iterator unitIt = b.getUnits().iterator();

      while(unitIt.hasNext()) {
         Unit curUnit = (Unit)unitIt.next();
         if (curUnit instanceof AssignStmt) {
            AssignStmt assignStmt = (AssignStmt)curUnit;
            if (assignStmt.getLeftOp() instanceof Local && assignStmt.getRightOp() instanceof CastExpr) {
               CastExpr ce = (CastExpr)assignStmt.getRightOp();
               Type orgType = ce.getOp().getType();
               Type newType = ce.getCastType();
               if (orgType == newType) {
                  if (assignStmt.getLeftOp() == ce.getOp()) {
                     unitIt.remove();
                  } else {
                     assignStmt.setRightOp(ce.getOp());
                  }
               }
            }
         }
      }

   }
}
