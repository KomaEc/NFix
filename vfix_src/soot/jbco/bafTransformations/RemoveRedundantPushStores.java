package soot.jbco.bafTransformations;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.PatchingChain;
import soot.Trap;
import soot.Unit;
import soot.baf.PushInst;
import soot.baf.StoreInst;
import soot.jbco.IJbcoTransform;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.util.Chain;

public class RemoveRedundantPushStores extends BodyTransformer implements IJbcoTransform {
   public static String[] dependancies = new String[]{"bb.jbco_rrps"};
   public static String name = "bb.jbco_rrps";

   public String[] getDependencies() {
      return dependancies;
   }

   public String getName() {
      return name;
   }

   public void outputSummary() {
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      boolean changed = true;
      PatchingChain units = b.getUnits();

      while(true) {
         while(changed) {
            changed = false;
            Unit prevprevprev = null;
            Unit prevprev = null;
            Unit prev = null;
            ExceptionalUnitGraph eug = new ExceptionalUnitGraph(b);

            Unit u;
            for(Iterator it = units.snapshotIterator(); it.hasNext(); prev = u) {
               u = (Unit)it.next();
               if (prev != null && prev instanceof PushInst && u instanceof StoreInst && prevprev != null && prevprev instanceof StoreInst && prevprevprev != null && prevprevprev instanceof PushInst) {
                  Local lprev = ((StoreInst)prevprev).getLocal();
                  Local l = ((StoreInst)u).getLocal();
                  if (l == lprev && eug.getSuccsOf(prevprevprev).size() == 1 && eug.getSuccsOf(prevprev).size() == 1) {
                     this.fixJumps(prevprevprev, prev, b.getTraps());
                     this.fixJumps(prevprev, u, b.getTraps());
                     units.remove(prevprevprev);
                     units.remove(prevprev);
                     changed = true;
                     break;
                  }
               }

               prevprevprev = prevprev;
               prevprev = prev;
            }
         }

         return;
      }
   }

   private void fixJumps(Unit from, Unit to, Chain<Trap> t) {
      from.redirectJumpsToThisTo(to);
      Iterator var4 = t.iterator();

      while(var4.hasNext()) {
         Trap trap = (Trap)var4.next();
         if (trap.getBeginUnit() == from) {
            trap.setBeginUnit(to);
         }

         if (trap.getEndUnit() == from) {
            trap.setEndUnit(to);
         }

         if (trap.getHandlerUnit() == from) {
            trap.setHandlerUnit(to);
         }
      }

   }
}
