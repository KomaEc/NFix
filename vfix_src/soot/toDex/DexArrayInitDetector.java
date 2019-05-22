package soot.toDex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.Trap;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.NewArrayExpr;

public class DexArrayInitDetector {
   private Map<Unit, List<Value>> arrayInitToFillValues = new HashMap();
   private Set<Unit> ignoreUnits = new HashSet();

   public void constructArrayInitializations(Body body) {
      Unit arrayInitStmt = null;
      List<Value> arrayValues = null;
      Set<Unit> curIgnoreUnits = null;
      int arraySize = 0;
      Iterator var6 = body.getUnits().iterator();

      while(true) {
         while(var6.hasNext()) {
            Unit u = (Unit)var6.next();
            if (!(u instanceof AssignStmt)) {
               arrayValues = null;
            } else {
               AssignStmt assignStmt = (AssignStmt)u;
               IntConstant intConst;
               if (assignStmt.getRightOp() instanceof NewArrayExpr) {
                  NewArrayExpr newArrayExp = (NewArrayExpr)assignStmt.getRightOp();
                  if (newArrayExp.getSize() instanceof IntConstant) {
                     intConst = (IntConstant)newArrayExp.getSize();
                     arrayValues = new ArrayList();
                     arraySize = intConst.value;
                     curIgnoreUnits = new HashSet();
                  } else {
                     arrayValues = null;
                  }
               } else if (assignStmt.getLeftOp() instanceof ArrayRef && assignStmt.getRightOp() instanceof IntConstant && arrayValues != null) {
                  ArrayRef aref = (ArrayRef)assignStmt.getLeftOp();
                  if (aref.getIndex() instanceof IntConstant) {
                     intConst = (IntConstant)aref.getIndex();
                     if (intConst.value == arrayValues.size()) {
                        arrayValues.add(assignStmt.getRightOp());
                        if (intConst.value == 0) {
                           arrayInitStmt = u;
                        } else if (intConst.value == arraySize - 1) {
                           curIgnoreUnits.add(u);
                           this.checkAndSave(arrayInitStmt, arrayValues, arraySize, curIgnoreUnits);
                           arrayValues = null;
                        } else {
                           curIgnoreUnits.add(u);
                        }
                     } else {
                        arrayValues = null;
                     }
                  } else {
                     arrayValues = null;
                  }
               } else {
                  arrayValues = null;
               }
            }
         }

         return;
      }
   }

   private void checkAndSave(Unit arrayInitStmt, List<Value> arrayValues, int arraySize, Set<Unit> curIgnoreUnits) {
      if (arrayValues != null && arrayValues.size() == arraySize && arrayInitStmt != null) {
         this.arrayInitToFillValues.put(arrayInitStmt, arrayValues);
         this.ignoreUnits.addAll(curIgnoreUnits);
      }

   }

   public List<Value> getValuesForArrayInit(Unit arrayInit) {
      return (List)this.arrayInitToFillValues.get(arrayInit);
   }

   public Set<Unit> getIgnoreUnits() {
      return this.ignoreUnits;
   }

   public void fixTraps(Body activeBody) {
      Iterator trapIt = activeBody.getTraps().iterator();

      while(true) {
         label33:
         while(trapIt.hasNext()) {
            Trap t = (Trap)trapIt.next();
            Unit beginUnit = t.getBeginUnit();
            Unit endUnit = t.getEndUnit();

            while(this.ignoreUnits.contains(beginUnit)) {
               beginUnit = activeBody.getUnits().getPredOf(beginUnit);
               if (beginUnit == endUnit) {
                  trapIt.remove();
                  continue label33;
               }

               if (this.arrayInitToFillValues.containsKey(beginUnit)) {
                  break;
               }
            }

            do {
               if (!this.ignoreUnits.contains(endUnit)) {
                  t.setBeginUnit(beginUnit);
                  t.setEndUnit(endUnit);
                  continue label33;
               }

               endUnit = activeBody.getUnits().getSuccOf(endUnit);
            } while(beginUnit != endUnit);

            trapIt.remove();
         }

         return;
      }
   }
}
