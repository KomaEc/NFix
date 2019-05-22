package soot.toDex;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;
import soot.Unit;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;

public class SynchronizedMethodTransformer extends BodyTransformer {
   public SynchronizedMethodTransformer(Singletons.Global g) {
   }

   public static SynchronizedMethodTransformer v() {
      return G.v().soot_toDex_SynchronizedMethodTransformer();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      if (b.getMethod().isSynchronized() && !b.getMethod().isStatic()) {
         Iterator it = b.getUnits().snapshotIterator();

         while(it.hasNext()) {
            Unit u = (Unit)it.next();
            if (!(u instanceof IdentityStmt)) {
               if (!(u instanceof EnterMonitorStmt)) {
                  b.getUnits().insertBeforeNoRedirect(Jimple.v().newEnterMonitorStmt(b.getThisLocal()), u);
                  UnitGraph graph = new ExceptionalUnitGraph(b);
                  Iterator var7 = graph.getTails().iterator();

                  while(var7.hasNext()) {
                     Unit tail = (Unit)var7.next();
                     b.getUnits().insertBefore((Unit)Jimple.v().newExitMonitorStmt(b.getThisLocal()), (Unit)tail);
                  }
               }
               break;
            }
         }

      }
   }
}
