package soot.shimple.toolkits.scalar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.shimple.ShimpleBody;
import soot.toolkits.scalar.LocalUses;
import soot.toolkits.scalar.UnitValueBoxPair;

public class ShimpleLocalUses implements LocalUses {
   private static final Logger logger = LoggerFactory.getLogger(ShimpleLocalUses.class);
   protected Map<Local, ArrayList> localToUses;

   public ShimpleLocalUses(ShimpleBody sb) {
      if (!sb.isSSA()) {
         throw new RuntimeException("ShimpleBody is not in proper SSA form as required by ShimpleLocalUses.  You may need to rebuild it or use SimpleLocalUses instead.");
      } else {
         this.localToUses = new HashMap();
         Iterator localsIt = sb.getLocals().iterator();

         while(localsIt.hasNext()) {
            Local local = (Local)localsIt.next();
            this.localToUses.put(local, new ArrayList());
         }

         Iterator unitsIt = sb.getUnits().iterator();

         while(unitsIt.hasNext()) {
            Unit unit = (Unit)unitsIt.next();
            Iterator boxIt = unit.getUseBoxes().iterator();

            while(boxIt.hasNext()) {
               ValueBox box = (ValueBox)boxIt.next();
               Value value = box.getValue();
               if (value instanceof Local) {
                  List<UnitValueBoxPair> useList = (List)this.localToUses.get(value);
                  useList.add(new UnitValueBoxPair(unit, box));
               }
            }
         }

      }
   }

   public List getUsesOf(Local local) {
      List uses = (List)this.localToUses.get(local);
      return uses == null ? Collections.EMPTY_LIST : uses;
   }

   public List getUsesOf(Unit unit) {
      List defBoxes = unit.getDefBoxes();
      switch(defBoxes.size()) {
      case 0:
         return Collections.EMPTY_LIST;
      case 1:
         Value local = ((ValueBox)defBoxes.get(0)).getValue();
         if (!(local instanceof Local)) {
            return Collections.EMPTY_LIST;
         }

         return this.getUsesOf((Local)local);
      default:
         logger.warn("Unit has multiple definition boxes?");
         List usesList = new ArrayList();
         Iterator defBoxesIt = defBoxes.iterator();

         while(defBoxesIt.hasNext()) {
            Value def = ((ValueBox)defBoxesIt.next()).getValue();
            if (def instanceof Local) {
               usesList.addAll(this.getUsesOf((Local)def));
            }
         }

         return usesList;
      }
   }
}
