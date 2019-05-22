package soot.toDex;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;
import soot.Trap;
import soot.Unit;
import soot.jimple.Jimple;

public class TrapSplitter extends BodyTransformer {
   public TrapSplitter(Singletons.Global g) {
   }

   public static TrapSplitter v() {
      return G.v().soot_toDex_TrapSplitter();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      if (b.getTraps().size() >= 2) {
         while(true) {
            TrapSplitter.TrapOverlap to;
            while((to = this.getNextOverlap(b)) != null) {
               if (to.t1.getBeginUnit() == to.t1.getEndUnit()) {
                  b.getTraps().remove(to.t1);
               } else if (to.t2.getBeginUnit() == to.t2.getEndUnit()) {
                  b.getTraps().remove(to.t2);
               } else if (to.t1.getBeginUnit() != to.t2Start) {
                  Trap newTrap = Jimple.v().newTrap(to.t1.getException(), to.t1.getBeginUnit(), to.t2Start, to.t1.getHandlerUnit());
                  this.safeAddTrap(b, newTrap, to.t1);
                  to.t1.setBeginUnit(to.t2Start);
               } else if (to.t1.getBeginUnit() == to.t2.getBeginUnit()) {
                  Unit firstEndUnit;
                  for(firstEndUnit = to.t1.getBeginUnit(); firstEndUnit != to.t1.getEndUnit() && firstEndUnit != to.t2.getEndUnit(); firstEndUnit = b.getUnits().getSuccOf(firstEndUnit)) {
                  }

                  Trap newTrap;
                  if (firstEndUnit == to.t1.getEndUnit()) {
                     if (to.t1.getException() != to.t2.getException()) {
                        newTrap = Jimple.v().newTrap(to.t2.getException(), to.t1.getBeginUnit(), firstEndUnit, to.t2.getHandlerUnit());
                        this.safeAddTrap(b, newTrap, to.t2);
                     } else if (to.t1.getHandlerUnit() != to.t2.getHandlerUnit()) {
                        newTrap = Jimple.v().newTrap(to.t1.getException(), to.t1.getBeginUnit(), firstEndUnit, to.t1.getHandlerUnit());
                        this.safeAddTrap(b, newTrap, to.t1);
                     }

                     to.t2.setBeginUnit(firstEndUnit);
                  } else if (firstEndUnit == to.t2.getEndUnit()) {
                     if (to.t1.getException() != to.t2.getException()) {
                        newTrap = Jimple.v().newTrap(to.t1.getException(), to.t1.getBeginUnit(), firstEndUnit, to.t1.getHandlerUnit());
                        this.safeAddTrap(b, newTrap, to.t1);
                        to.t1.setBeginUnit(firstEndUnit);
                     } else if (to.t1.getHandlerUnit() != to.t2.getHandlerUnit()) {
                        b.getTraps().remove(to.t2);
                     } else {
                        to.t1.setBeginUnit(firstEndUnit);
                     }
                  }
               }
            }

            return;
         }
      }
   }

   private void safeAddTrap(Body b, Trap newTrap, Trap position) {
      if (newTrap.getBeginUnit() != newTrap.getEndUnit()) {
         if (position != null) {
            b.getTraps().insertAfter((Object)newTrap, position);
         } else {
            b.getTraps().add(newTrap);
         }
      }

   }

   private TrapSplitter.TrapOverlap getNextOverlap(Body b) {
      Iterator var2 = b.getTraps().iterator();

      while(var2.hasNext()) {
         Trap t1 = (Trap)var2.next();

         label41:
         for(Unit splitUnit = t1.getBeginUnit(); splitUnit != t1.getEndUnit(); splitUnit = b.getUnits().getSuccOf(splitUnit)) {
            Iterator var5 = b.getTraps().iterator();

            Trap t2;
            do {
               do {
                  do {
                     if (!var5.hasNext()) {
                        continue label41;
                     }

                     t2 = (Trap)var5.next();
                  } while(t1 == t2);
               } while(t1.getEndUnit() == t2.getEndUnit() && t1.getException() != t2.getException());
            } while(t2.getBeginUnit() != splitUnit);

            return new TrapSplitter.TrapOverlap(t1, t2, t2.getBeginUnit());
         }
      }

      return null;
   }

   private class TrapOverlap {
      private Trap t1;
      private Trap t2;
      private Unit t2Start;

      public TrapOverlap(Trap t1, Trap t2, Unit t2Start) {
         this.t1 = t1;
         this.t2 = t2;
         this.t2Start = t2Start;
      }
   }
}
