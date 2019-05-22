package heros.flowfunc;

import heros.FlowFunction;
import heros.TwoElementSet;
import java.util.Collections;
import java.util.Set;

public class Gen<D> implements FlowFunction<D> {
   private final D genValue;
   private final D zeroValue;

   public Gen(D genValue, D zeroValue) {
      this.genValue = genValue;
      this.zeroValue = zeroValue;
   }

   public Set<D> computeTargets(D source) {
      return (Set)(source.equals(this.zeroValue) ? TwoElementSet.twoElementSet(source, this.genValue) : Collections.singleton(source));
   }
}
