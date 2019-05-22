package heros.flowfunc;

import heros.FlowFunction;
import heros.TwoElementSet;
import java.util.Collections;
import java.util.Set;

public class Transfer<D> implements FlowFunction<D> {
   private final D toValue;
   private final D fromValue;

   public Transfer(D toValue, D fromValue) {
      this.toValue = toValue;
      this.fromValue = fromValue;
   }

   public Set<D> computeTargets(D source) {
      if (source.equals(this.fromValue)) {
         return TwoElementSet.twoElementSet(source, this.toValue);
      } else {
         return source.equals(this.toValue) ? Collections.emptySet() : Collections.singleton(source);
      }
   }
}
