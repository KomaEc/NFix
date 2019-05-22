package soot.toolkits.exceptions;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;
import soot.Trap;
import soot.Unit;

public class DuplicateCatchAllTrapRemover extends BodyTransformer {
   public DuplicateCatchAllTrapRemover(Singletons.Global g) {
   }

   public static DuplicateCatchAllTrapRemover v() {
      return G.v().soot_toolkits_exceptions_DuplicateCatchAllTrapRemover();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      Iterator t1It = b.getTraps().snapshotIterator();

      label70:
      while(true) {
         Trap t1;
         do {
            if (!t1It.hasNext()) {
               return;
            }

            t1 = (Trap)t1It.next();
         } while(!t1.getException().getName().equals("java.lang.Throwable"));

         Iterator t2It = b.getTraps().snapshotIterator();

         while(true) {
            while(true) {
               Trap t2;
               do {
                  do {
                     do {
                        do {
                           if (!t2It.hasNext()) {
                              continue label70;
                           }

                           t2 = (Trap)t2It.next();
                        } while(t1 == t2);
                     } while(t1.getBeginUnit() != t2.getBeginUnit());
                  } while(t1.getEndUnit() != t2.getEndUnit());
               } while(!t2.getException().getName().equals("java.lang.Throwable"));

               Iterator var8 = b.getTraps().iterator();

               while(var8.hasNext()) {
                  Trap t3 = (Trap)var8.next();
                  if (t3 != t1 && t3 != t2 && t3.getException().getName().equals("java.lang.Throwable")) {
                     if (this.trapCoversUnit(b, t3, t1.getHandlerUnit()) && t3.getHandlerUnit() == t2.getHandlerUnit()) {
                        b.getTraps().remove(t2);
                        break;
                     }

                     if (this.trapCoversUnit(b, t3, t2.getHandlerUnit()) && t3.getHandlerUnit() == t1.getHandlerUnit()) {
                        b.getTraps().remove(t1);
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   private boolean trapCoversUnit(Body b, Trap trap, Unit unit) {
      Iterator unitIt = b.getUnits().iterator(trap.getBeginUnit(), trap.getEndUnit());

      Unit u;
      do {
         if (!unitIt.hasNext()) {
            return false;
         }

         u = (Unit)unitIt.next();
      } while(u != unit);

      return true;
   }
}
