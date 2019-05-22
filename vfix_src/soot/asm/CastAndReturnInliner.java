package soot.asm;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Trap;
import soot.Unit;
import soot.UnitBox;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.GotoStmt;
import soot.jimple.ReturnStmt;

public class CastAndReturnInliner extends BodyTransformer {
   protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
      Iterator it = body.getUnits().snapshotIterator();

      while(true) {
         GotoStmt gtStmt;
         AssignStmt assign;
         CastExpr ce;
         ReturnStmt retStmt;
         do {
            Unit nextStmt;
            do {
               do {
                  do {
                     Unit u;
                     do {
                        if (!it.hasNext()) {
                           return;
                        }

                        u = (Unit)it.next();
                     } while(!(u instanceof GotoStmt));

                     gtStmt = (GotoStmt)u;
                  } while(!(gtStmt.getTarget() instanceof AssignStmt));

                  assign = (AssignStmt)gtStmt.getTarget();
               } while(!(assign.getRightOp() instanceof CastExpr));

               ce = (CastExpr)assign.getRightOp();
               nextStmt = body.getUnits().getSuccOf((Unit)assign);
            } while(!(nextStmt instanceof ReturnStmt));

            retStmt = (ReturnStmt)nextStmt;
         } while(retStmt.getOp() != assign.getLeftOp());

         ReturnStmt newStmt = (ReturnStmt)retStmt.clone();
         newStmt.setOp(ce.getOp());
         Iterator var12 = body.getTraps().iterator();

         while(var12.hasNext()) {
            Trap t = (Trap)var12.next();
            Iterator var14 = t.getUnitBoxes().iterator();

            while(var14.hasNext()) {
               UnitBox ubox = (UnitBox)var14.next();
               if (ubox.getUnit() == gtStmt) {
                  ubox.setUnit(newStmt);
               }
            }
         }

         while(!gtStmt.getBoxesPointingToThis().isEmpty()) {
            ((UnitBox)gtStmt.getBoxesPointingToThis().get(0)).setUnit(newStmt);
         }

         body.getUnits().swapWith((Unit)gtStmt, (Unit)newStmt);
      }
   }
}
