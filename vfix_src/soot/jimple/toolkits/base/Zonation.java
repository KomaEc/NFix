package soot.jimple.toolkits.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Trap;
import soot.Unit;
import soot.jimple.StmtBody;
import soot.util.Chain;

public class Zonation {
   private int zoneCount;
   private Map<Unit, Zone> unitToZone;

   public Zonation(StmtBody body) {
      Chain<Unit> units = body.getUnits();
      Map<Unit, List<Trap>> unitToTrapBoundaries = new HashMap();
      Iterator var4 = body.getTraps().iterator();

      while(var4.hasNext()) {
         Trap t = (Trap)var4.next();
         this.addTrapBoundary(t.getBeginUnit(), t, unitToTrapBoundaries);
         this.addTrapBoundary(t.getEndUnit(), t, unitToTrapBoundaries);
      }

      Map<List<Trap>, Zone> trapListToZone = new HashMap(10, 0.7F);
      List<Trap> currentTraps = new ArrayList();
      this.zoneCount = 0;
      this.unitToZone = new HashMap(units.size() * 2 + 1, 0.7F);
      Zone currentZone = new Zone("0");
      trapListToZone.put(new ArrayList(), currentZone);

      Unit u;
      for(Iterator var7 = units.iterator(); var7.hasNext(); this.unitToZone.put(u, currentZone)) {
         u = (Unit)var7.next();
         List<Trap> trapBoundaries = (List)unitToTrapBoundaries.get(u);
         if (trapBoundaries != null && !trapBoundaries.isEmpty()) {
            Iterator var10 = trapBoundaries.iterator();

            while(var10.hasNext()) {
               Trap trap = (Trap)var10.next();
               if (currentTraps.contains(trap)) {
                  currentTraps.remove(trap);
               } else {
                  currentTraps.add(trap);
               }
            }

            if (trapListToZone.containsKey(currentTraps)) {
               currentZone = (Zone)trapListToZone.get(currentTraps);
            } else {
               ++this.zoneCount;
               currentZone = new Zone((new Integer(this.zoneCount)).toString());
               trapListToZone.put(currentTraps, currentZone);
            }
         }
      }

   }

   private void addTrapBoundary(Unit unit, Trap t, Map<Unit, List<Trap>> unitToTrapBoundaries) {
      List<Trap> boundary = (List)unitToTrapBoundaries.get(unit);
      if (boundary == null) {
         boundary = new ArrayList();
         unitToTrapBoundaries.put(unit, boundary);
      }

      ((List)boundary).add(t);
   }

   public Zone getZoneOf(Unit u) {
      Zone z = (Zone)this.unitToZone.get(u);
      if (z == null) {
         throw new RuntimeException("null zone!");
      } else {
         return z;
      }
   }

   public int getZoneCount() {
      return this.zoneCount;
   }
}
