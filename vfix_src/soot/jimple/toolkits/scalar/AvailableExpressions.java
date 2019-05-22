package soot.jimple.toolkits.scalar;

import java.util.List;
import soot.Unit;
import soot.util.Chain;

public interface AvailableExpressions {
   List getAvailablePairsBefore(Unit var1);

   List getAvailablePairsAfter(Unit var1);

   Chain getAvailableEquivsBefore(Unit var1);

   Chain getAvailableEquivsAfter(Unit var1);
}
