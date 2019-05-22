package soot.dexpler;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.NullConstant;

public class DexNullInstanceofTransformer extends BodyTransformer {
   public static DexNullInstanceofTransformer v() {
      return new DexNullInstanceofTransformer();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      Iterator unitIt = b.getUnits().snapshotIterator();

      while(unitIt.hasNext()) {
         Unit u = (Unit)unitIt.next();
         if (u instanceof AssignStmt) {
            AssignStmt assignStmt = (AssignStmt)u;
            if (assignStmt.getRightOp() instanceof InstanceOfExpr) {
               InstanceOfExpr iof = (InstanceOfExpr)assignStmt.getRightOp();
               if (iof.getOp() == NullConstant.v()) {
                  assignStmt.setRightOp(IntConstant.v(0));
               }

               if (iof.getOp() instanceof IntConstant && ((IntConstant)iof.getOp()).value == 0) {
                  assignStmt.setRightOp(IntConstant.v(0));
               }
            }
         }
      }

   }
}
