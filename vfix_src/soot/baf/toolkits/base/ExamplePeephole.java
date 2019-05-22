package soot.baf.toolkits.base;

import java.util.Iterator;
import soot.Body;
import soot.Unit;
import soot.baf.InstanceCastInst;

public class ExamplePeephole implements Peephole {
   public boolean apply(Body b) {
      boolean changed = false;
      Iterator it = b.getUnits().iterator();

      while(it.hasNext()) {
         Unit u = (Unit)it.next();
         if (u instanceof InstanceCastInst) {
            it.remove();
            changed = true;
         }
      }

      return changed;
   }
}
