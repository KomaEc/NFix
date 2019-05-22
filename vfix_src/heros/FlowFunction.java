package heros;

import java.util.Set;

public interface FlowFunction<D> {
   Set<D> computeTargets(D var1);
}
