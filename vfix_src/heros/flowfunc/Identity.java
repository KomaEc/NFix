package heros.flowfunc;

import heros.FlowFunction;
import java.util.Collections;
import java.util.Set;

public class Identity<D> implements FlowFunction<D> {
   private static final Identity instance = new Identity();

   private Identity() {
   }

   public Set<D> computeTargets(D source) {
      return Collections.singleton(source);
   }

   public static <D> Identity<D> v() {
      return instance;
   }
}
