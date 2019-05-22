package soot.baf.toolkits.base;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.Singletons;
import soot.Unit;
import soot.baf.PushInst;
import soot.baf.StoreInst;
import soot.toolkits.scalar.Pair;

public class StoreChainOptimizer extends BodyTransformer {
   public StoreChainOptimizer(Singletons.Global g) {
   }

   public static StoreChainOptimizer v() {
      return G.v().soot_baf_toolkits_base_StoreChainOptimizer();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      Map<Local, Pair<Unit, Unit>> stores = new HashMap();
      Set<Unit> toRemove = new HashSet();
      Unit lastPush = null;
      Iterator var7 = b.getUnits().iterator();

      while(true) {
         while(var7.hasNext()) {
            Unit u = (Unit)var7.next();
            if (!u.getBoxesPointingToThis().isEmpty()) {
               stores.clear();
               lastPush = null;
            } else if (u instanceof PushInst) {
               lastPush = u;
            } else if (u instanceof StoreInst && lastPush != null) {
               StoreInst si = (StoreInst)u;
               Pair<Unit, Unit> pushStorePair = (Pair)stores.get(si.getLocal());
               if (pushStorePair != null) {
                  toRemove.add(pushStorePair.getO1());
                  toRemove.add(pushStorePair.getO2());
               }

               stores.put(si.getLocal(), new Pair(lastPush, u));
            } else {
               stores.clear();
               lastPush = null;
            }
         }

         b.getUnits().removeAll(toRemove);
         return;
      }
   }
}
