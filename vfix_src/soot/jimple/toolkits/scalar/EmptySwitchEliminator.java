package soot.jimple.toolkits.scalar;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;
import soot.Unit;
import soot.jimple.Jimple;
import soot.jimple.LookupSwitchStmt;

public class EmptySwitchEliminator extends BodyTransformer {
   public EmptySwitchEliminator(Singletons.Global g) {
   }

   public static EmptySwitchEliminator v() {
      return G.v().soot_jimple_toolkits_scalar_EmptySwitchEliminator();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      Iterator it = b.getUnits().snapshotIterator();

      while(it.hasNext()) {
         Unit u = (Unit)it.next();
         if (u instanceof LookupSwitchStmt) {
            LookupSwitchStmt sw = (LookupSwitchStmt)u;
            if (sw.getTargetCount() == 0 && sw.getDefaultTarget() != null) {
               b.getUnits().swapWith((Unit)sw, (Unit)Jimple.v().newGotoStmt(sw.getDefaultTarget()));
            }
         }
      }

   }
}
