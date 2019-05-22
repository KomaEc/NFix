package soot.toDex;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;
import soot.Trap;
import soot.Unit;
import soot.jimple.IdentityStmt;
import soot.jimple.ParameterRef;
import soot.jimple.ThisRef;

public class FastDexTrapTightener extends BodyTransformer {
   public FastDexTrapTightener(Singletons.Global g) {
   }

   public static FastDexTrapTightener v() {
      return G.v().soot_toDex_FastDexTrapTightener();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      Iterator trapIt = b.getTraps().snapshotIterator();

      while(trapIt.hasNext()) {
         Trap t = (Trap)trapIt.next();

         Unit beginUnit;
         while(!this.isDexInstruction(beginUnit = t.getBeginUnit()) && t.getBeginUnit() != t.getEndUnit()) {
            t.setBeginUnit(b.getUnits().getSuccOf(beginUnit));
         }

         if (t.getBeginUnit() == t.getEndUnit()) {
            trapIt.remove();
         }
      }

   }

   private boolean isDexInstruction(Unit unit) {
      if (!(unit instanceof IdentityStmt)) {
         return true;
      } else {
         IdentityStmt is = (IdentityStmt)unit;
         return !(is.getRightOp() instanceof ThisRef) && !(is.getRightOp() instanceof ParameterRef);
      }
   }
}
