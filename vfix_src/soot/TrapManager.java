package soot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import soot.util.Chain;

public class TrapManager {
   public static boolean isExceptionCaughtAt(SootClass e, Unit u, Body b) {
      Hierarchy h = Scene.v().getActiveHierarchy();
      Chain<Unit> units = b.getUnits();
      Iterator var5 = b.getTraps().iterator();

      while(true) {
         Trap t;
         do {
            if (!var5.hasNext()) {
               return false;
            }

            t = (Trap)var5.next();
         } while(!h.isClassSubclassOfIncluding(e, t.getException()));

         Iterator it = units.iterator(t.getBeginUnit(), units.getPredOf(t.getEndUnit()));

         while(it.hasNext()) {
            if (u.equals(it.next())) {
               return true;
            }
         }
      }
   }

   public static List<Trap> getTrapsAt(Unit unit, Body b) {
      List<Trap> trapsList = new ArrayList();
      Chain<Unit> units = b.getUnits();
      Iterator var4 = b.getTraps().iterator();

      while(var4.hasNext()) {
         Trap t = (Trap)var4.next();
         Iterator it = units.iterator(t.getBeginUnit(), units.getPredOf(t.getEndUnit()));

         while(it.hasNext()) {
            if (unit.equals(it.next())) {
               trapsList.add(t);
            }
         }
      }

      return trapsList;
   }

   public static Set<Unit> getTrappedUnitsOf(Body b) {
      Set<Unit> trapsSet = new HashSet();
      Chain<Unit> units = b.getUnits();
      Iterator var3 = b.getTraps().iterator();

      while(var3.hasNext()) {
         Trap t = (Trap)var3.next();
         Iterator it = units.iterator(t.getBeginUnit(), units.getPredOf(t.getEndUnit()));

         while(it.hasNext()) {
            trapsSet.add(it.next());
         }
      }

      return trapsSet;
   }

   public static void splitTrapsAgainst(Body b, Unit rangeStart, Unit rangeEnd) {
      Chain<Trap> traps = b.getTraps();
      Chain<Unit> units = b.getUnits();
      Iterator trapsIt = traps.snapshotIterator();

      while(trapsIt.hasNext()) {
         Trap t = (Trap)trapsIt.next();
         Iterator<Unit> unitIt = units.iterator(t.getBeginUnit(), t.getEndUnit());
         boolean insideRange = false;

         while(unitIt.hasNext()) {
            Unit u = (Unit)unitIt.next();
            if (u.equals(rangeStart)) {
               insideRange = true;
            }

            Trap firstTrap;
            if (!unitIt.hasNext()) {
               if (!insideRange) {
                  break;
               }

               firstTrap = (Trap)t.clone();
               t.setBeginUnit(rangeStart);
               firstTrap.setEndUnit(rangeStart);
               traps.insertAfter((Object)firstTrap, t);
            }

            if (u.equals(rangeEnd)) {
               if (!insideRange) {
                  throw new RuntimeException("inversed range?");
               }

               firstTrap = (Trap)t.clone();
               Trap secondTrap = (Trap)t.clone();
               firstTrap.setEndUnit(rangeStart);
               secondTrap.setBeginUnit(rangeStart);
               secondTrap.setEndUnit(rangeEnd);
               t.setBeginUnit(rangeEnd);
               traps.insertAfter((Object)firstTrap, t);
               traps.insertAfter((Object)secondTrap, t);
            }
         }
      }

   }

   public static List<RefType> getExceptionTypesOf(Unit u, Body body) {
      List<RefType> possibleTypes = new ArrayList();
      Iterator var3 = body.getTraps().iterator();

      while(var3.hasNext()) {
         Trap trap = (Trap)var3.next();
         if (trap.getHandlerUnit() == u) {
            possibleTypes.add(RefType.v(trap.getException().getName()));
         }
      }

      return possibleTypes;
   }
}
