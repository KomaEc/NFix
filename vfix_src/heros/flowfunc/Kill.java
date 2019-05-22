package heros.flowfunc;

import heros.FlowFunction;
import java.util.Collections;
import java.util.Set;

public class Kill<D> implements FlowFunction<D> {
   private final D killValue;

   public Kill(D killValue) {
      this.killValue = killValue;
   }

   public Set<D> computeTargets(D source) {
      return source.equals(this.killValue) ? Collections.emptySet() : Collections.singleton(source);
   }
}
