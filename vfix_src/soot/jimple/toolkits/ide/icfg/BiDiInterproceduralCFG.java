package soot.jimple.toolkits.ide.icfg;

import heros.InterproceduralCFG;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import soot.Value;
import soot.toolkits.graph.DirectedGraph;

public interface BiDiInterproceduralCFG<N, M> extends InterproceduralCFG<N, M> {
   List<N> getPredsOf(N var1);

   Collection<N> getEndPointsOf(M var1);

   List<N> getPredsOfCallAt(N var1);

   Set<N> allNonCallEndNodes();

   DirectedGraph<N> getOrCreateUnitGraph(M var1);

   List<Value> getParameterRefs(M var1);

   boolean isReturnSite(N var1);

   boolean isReachable(N var1);
}
