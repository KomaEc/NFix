package soot.jimple.toolkits.scalar;

import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;
import soot.Trap;
import soot.Unit;
import soot.jimple.NopStmt;
import soot.options.Options;
import soot.util.Chain;

public class NopEliminator extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(NopEliminator.class);

   public NopEliminator(Singletons.Global g) {
   }

   public static NopEliminator v() {
      return G.v().soot_jimple_toolkits_scalar_NopEliminator();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      if (Options.v().verbose()) {
         logger.debug("[" + b.getMethod().getName() + "] Removing nops...");
      }

      Chain<Unit> units = b.getUnits();
      Iterator stmtIt = units.snapshotIterator();

      while(true) {
         Unit u;
         do {
            if (!stmtIt.hasNext()) {
               return;
            }

            u = (Unit)stmtIt.next();
         } while(!(u instanceof NopStmt));

         boolean keepNop = false;
         if (b.getUnits().getLast() == u) {
            Iterator var8 = b.getTraps().iterator();

            while(var8.hasNext()) {
               Trap t = (Trap)var8.next();
               if (t.getEndUnit() == u) {
                  keepNop = true;
               }
            }
         }

         if (!keepNop) {
            units.remove(u);
         }
      }
   }
}
