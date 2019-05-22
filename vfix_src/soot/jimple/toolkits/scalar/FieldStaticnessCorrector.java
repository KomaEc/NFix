package soot.jimple.toolkits.scalar;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.ConflictingFieldRefException;
import soot.G;
import soot.Singletons;
import soot.SootField;
import soot.jimple.AssignStmt;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.Jimple;
import soot.jimple.Stmt;

public class FieldStaticnessCorrector extends AbstractStaticnessCorrector {
   public FieldStaticnessCorrector(Singletons.Global g) {
   }

   public static FieldStaticnessCorrector v() {
      return G.v().soot_jimple_toolkits_scalar_FieldStaticnessCorrector();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      Iterator unitIt = b.getUnits().iterator();

      while(unitIt.hasNext()) {
         Stmt s = (Stmt)unitIt.next();
         if (s.containsFieldRef() && s instanceof AssignStmt) {
            FieldRef ref = s.getFieldRef();
            if (this.isTypeLoaded(ref.getFieldRef().type())) {
               try {
                  if (ref instanceof InstanceFieldRef) {
                     SootField fld = ref.getField();
                     if (fld != null && fld.isStatic()) {
                        AssignStmt assignStmt = (AssignStmt)s;
                        if (assignStmt.getLeftOp() == ref) {
                           assignStmt.setLeftOp(Jimple.v().newStaticFieldRef(ref.getField().makeRef()));
                        } else if (assignStmt.getRightOp() == ref) {
                           assignStmt.setRightOp(Jimple.v().newStaticFieldRef(ref.getField().makeRef()));
                        }
                     }
                  }
               } catch (ConflictingFieldRefException var9) {
               }
            }
         }
      }

   }
}
