package soot.toolkits.graph;

import java.util.Collection;
import java.util.List;

public interface DominatorsFinder<N> {
   DirectedGraph<N> getGraph();

   List<N> getDominators(N var1);

   N getImmediateDominator(N var1);

   boolean isDominatedBy(N var1, N var2);

   boolean isDominatedByAll(N var1, Collection<N> var2);
}
