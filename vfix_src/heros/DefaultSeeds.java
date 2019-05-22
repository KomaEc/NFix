package heros;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DefaultSeeds {
   public static <N, D> Map<N, Set<D>> make(Iterable<N> units, D zeroNode) {
      Map<N, Set<D>> res = new HashMap();
      Iterator var3 = units.iterator();

      while(var3.hasNext()) {
         N n = var3.next();
         res.put(n, Collections.singleton(zeroNode));
      }

      return res;
   }
}
