package heros.flowfunc;

import heros.FlowFunction;
import java.util.Collections;
import java.util.Set;

public class KillAll<D> implements FlowFunction<D> {
   private static final KillAll instance = new KillAll();

   private KillAll() {
   }

   public Set<D> computeTargets(D source) {
      return Collections.emptySet();
   }

   public static <D> KillAll<D> v() {
      return instance;
   }
}
