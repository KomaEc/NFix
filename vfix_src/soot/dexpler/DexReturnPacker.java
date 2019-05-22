package soot.dexpler;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;

public class DexReturnPacker extends BodyTransformer {
   public static DexReturnPacker v() {
      return new DexReturnPacker();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      Unit lastUnit = null;
      Iterator unitIt = b.getUnits().iterator();

      while(true) {
         while(unitIt.hasNext()) {
            Unit curUnit = (Unit)unitIt.next();
            if (!(curUnit instanceof ReturnStmt) && !(curUnit instanceof ReturnVoidStmt)) {
               lastUnit = null;
            } else if (lastUnit != null && this.isEqual(lastUnit, curUnit)) {
               curUnit.redirectJumpsToThisTo(lastUnit);
               unitIt.remove();
            } else {
               lastUnit = curUnit;
            }
         }

         return;
      }
   }

   private boolean isEqual(Unit unit1, Unit unit2) {
      if (unit1 != unit2 && !unit1.equals(unit2)) {
         if (unit1.getClass() == unit2.getClass()) {
            if (unit1 instanceof ReturnVoidStmt) {
               return true;
            }

            if (unit1 instanceof ReturnStmt) {
               return ((ReturnStmt)unit1).getOp() == ((ReturnStmt)unit2).getOp();
            }
         }

         return false;
      } else {
         return true;
      }
   }
}
