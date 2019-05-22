package soot.shimple.toolkits.scalar;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.shimple.ShimpleBody;
import soot.toolkits.scalar.LocalDefs;
import soot.util.Chain;

public class ShimpleLocalDefs implements LocalDefs {
   protected Map<Value, List<Unit>> localToDefs;

   public ShimpleLocalDefs(ShimpleBody sb) {
      if (!sb.isSSA()) {
         throw new RuntimeException("ShimpleBody is not in proper SSA form as required by ShimpleLocalDefs.  You may need to rebuild it or use SimpleLocalDefs instead.");
      } else {
         Chain<Unit> unitsChain = sb.getUnits();
         Iterator<Unit> unitsIt = unitsChain.iterator();
         this.localToDefs = new HashMap(unitsChain.size() * 2 + 1, 0.7F);

         while(unitsIt.hasNext()) {
            Unit unit = (Unit)unitsIt.next();
            Iterator defBoxesIt = unit.getDefBoxes().iterator();

            while(defBoxesIt.hasNext()) {
               Value value = ((ValueBox)defBoxesIt.next()).getValue();
               if (value instanceof Local) {
                  this.localToDefs.put(value, Collections.singletonList(unit));
               }
            }
         }

      }
   }

   public List<Unit> getDefsOf(Local l) {
      List<Unit> defs = (List)this.localToDefs.get(l);
      if (defs == null) {
         throw new RuntimeException("Local not found in Body.");
      } else {
         return defs;
      }
   }

   public List<Unit> getDefsOfAt(Local l, Unit s) {
      Iterator<ValueBox> boxIt = s.getUseBoxes().iterator();
      boolean defined = false;

      while(boxIt.hasNext()) {
         Value value = ((ValueBox)boxIt.next()).getValue();
         if (value.equals(l)) {
            defined = true;
            break;
         }
      }

      if (!defined) {
         throw new RuntimeException("Illegal LocalDefs query; local " + l + " is not being used at " + s);
      } else {
         return this.getDefsOf(l);
      }
   }
}
