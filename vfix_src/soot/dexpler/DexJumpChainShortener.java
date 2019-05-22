package soot.dexpler;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;

public class DexJumpChainShortener extends BodyTransformer {
   public static DexJumpChainShortener v() {
      return new DexJumpChainShortener();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      Iterator unitIt = b.getUnits().snapshotIterator();

      while(true) {
         while(unitIt.hasNext()) {
            Unit u = (Unit)unitIt.next();
            GotoStmt nextTarget;
            if (u instanceof GotoStmt) {
               GotoStmt stmt = (GotoStmt)u;

               while(stmt.getTarget() instanceof GotoStmt) {
                  nextTarget = (GotoStmt)stmt.getTarget();
                  stmt.setTarget(nextTarget.getTarget());
               }
            } else if (u instanceof IfStmt) {
               IfStmt stmt = (IfStmt)u;

               while(stmt.getTarget() instanceof GotoStmt) {
                  nextTarget = (GotoStmt)stmt.getTarget();
                  stmt.setTarget(nextTarget.getTarget());
               }
            }
         }

         return;
      }
   }
}
