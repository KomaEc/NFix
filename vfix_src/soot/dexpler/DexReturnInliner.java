package soot.dexpler;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.Trap;
import soot.Unit;
import soot.UnitBox;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;

public class DexReturnInliner extends DexTransformer {
   public static DexReturnInliner v() {
      return new DexReturnInliner();
   }

   private boolean isInstanceofReturn(Unit u) {
      return u instanceof ReturnStmt || u instanceof ReturnVoidStmt;
   }

   private boolean isInstanceofFlowChange(Unit u) {
      return u instanceof GotoStmt || this.isInstanceofReturn(u);
   }

   protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
      Set<Unit> duplicateIfTargets = this.getFallThroughReturns(body);
      Iterator<Unit> it = body.getUnits().snapshotIterator();
      boolean mayBeMore = false;
      Unit last = null;

      Unit u;
      do {
         for(mayBeMore = false; it.hasNext(); last = u) {
            u = (Unit)it.next();
            Stmt stmt;
            if (!(u instanceof GotoStmt)) {
               if (u instanceof IfStmt) {
                  IfStmt ifstmt = (IfStmt)u;
                  stmt = ifstmt.getTarget();
                  if (this.isInstanceofReturn(stmt)) {
                     if (duplicateIfTargets == null) {
                        duplicateIfTargets = new HashSet();
                     }

                     if (!((Set)duplicateIfTargets).add(stmt)) {
                        Unit newTarget = (Unit)stmt.clone();
                        body.getUnits().addLast(newTarget);
                        ifstmt.setTarget(newTarget);
                     }
                  }
               } else if (this.isInstanceofReturn(u) && last != null) {
                  u.removeAllTags();
                  u.addAllTagsOf(last);
               }
            } else {
               GotoStmt gtStmt = (GotoStmt)u;
               if (this.isInstanceofReturn(gtStmt.getTarget())) {
                  stmt = (Stmt)gtStmt.getTarget().clone();
                  Iterator var11 = body.getTraps().iterator();

                  while(var11.hasNext()) {
                     Trap t = (Trap)var11.next();
                     Iterator var13 = t.getUnitBoxes().iterator();

                     while(var13.hasNext()) {
                        UnitBox ubox = (UnitBox)var13.next();
                        if (ubox.getUnit() == u) {
                           ubox.setUnit(stmt);
                        }
                     }
                  }

                  while(!u.getBoxesPointingToThis().isEmpty()) {
                     ((UnitBox)u.getBoxesPointingToThis().get(0)).setUnit(stmt);
                  }

                  stmt.addAllTagsOf(u);
                  body.getUnits().swapWith((Unit)u, (Unit)stmt);
                  mayBeMore = true;
               }
            }
         }
      } while(mayBeMore);

   }

   private Set<Unit> getFallThroughReturns(Body body) {
      Set<Unit> fallThroughReturns = null;
      Unit lastUnit = null;

      Unit u;
      for(Iterator var4 = body.getUnits().iterator(); var4.hasNext(); lastUnit = u) {
         u = (Unit)var4.next();
         if (lastUnit != null && this.isInstanceofReturn(u) && !this.isInstanceofFlowChange(lastUnit)) {
            if (fallThroughReturns == null) {
               fallThroughReturns = new HashSet();
            }

            fallThroughReturns.add(u);
         }
      }

      return fallThroughReturns;
   }
}
