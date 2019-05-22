package soot.jimple.toolkits.scalar;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.G;
import soot.Scene;
import soot.Singletons;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.InvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;

public class MethodStaticnessCorrector extends AbstractStaticnessCorrector {
   public MethodStaticnessCorrector(Singletons.Global g) {
   }

   public static MethodStaticnessCorrector v() {
      return G.v().soot_jimple_toolkits_scalar_MethodStaticnessCorrector();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      Iterator unitIt = b.getUnits().snapshotIterator();

      while(unitIt.hasNext()) {
         Unit u = (Unit)unitIt.next();
         if (u instanceof Stmt) {
            Stmt s = (Stmt)u;
            if (s.containsInvokeExpr()) {
               InvokeExpr iexpr = s.getInvokeExpr();
               if (iexpr instanceof StaticInvokeExpr && this.isClassLoaded(iexpr.getMethodRef().declaringClass())) {
                  SootMethod target = Scene.v().grabMethod(iexpr.getMethodRef().getSignature());
                  if (target != null && !target.isStatic() && this.canBeMadeStatic(target)) {
                     target.setModifiers(target.getModifiers() | 8);
                  }
               }
            }
         }
      }

   }

   private boolean canBeMadeStatic(SootMethod target) {
      if (!target.hasActiveBody()) {
         return false;
      } else {
         Body body = target.getActiveBody();
         Value thisLocal = body.getThisLocal();
         Iterator var4 = body.getUnits().iterator();

         while(var4.hasNext()) {
            Unit u = (Unit)var4.next();
            Iterator var6 = u.getUseBoxes().iterator();

            while(var6.hasNext()) {
               ValueBox vb = (ValueBox)var6.next();
               if (vb.getValue() == thisLocal) {
                  return false;
               }
            }
         }

         return true;
      }
   }
}
