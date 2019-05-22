package soot.jimple.toolkits.scalar;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.DoubleType;
import soot.FloatType;
import soot.G;
import soot.Singletons;
import soot.Unit;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.Constant;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;

public class ConstantCastEliminator extends BodyTransformer {
   public ConstantCastEliminator(Singletons.Global g) {
   }

   public static ConstantCastEliminator v() {
      return G.v().soot_jimple_toolkits_scalar_ConstantCastEliminator();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      Iterator var4 = b.getUnits().iterator();

      while(true) {
         while(true) {
            AssignStmt assign;
            CastExpr ce;
            do {
               do {
                  Unit u;
                  do {
                     if (!var4.hasNext()) {
                        return;
                     }

                     u = (Unit)var4.next();
                  } while(!(u instanceof AssignStmt));

                  assign = (AssignStmt)u;
               } while(!(assign.getRightOp() instanceof CastExpr));

               ce = (CastExpr)assign.getRightOp();
            } while(!(ce.getOp() instanceof Constant));

            IntConstant it;
            if (ce.getType() instanceof FloatType && ce.getOp() instanceof IntConstant) {
               it = (IntConstant)ce.getOp();
               assign.setRightOp(FloatConstant.v((float)it.value));
            } else if (ce.getType() instanceof DoubleType && ce.getOp() instanceof IntConstant) {
               it = (IntConstant)ce.getOp();
               assign.setRightOp(DoubleConstant.v((double)it.value));
            }
         }
      }
   }
}
