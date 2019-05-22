package soot.toolkits.graph;

import java.util.List;

public interface DominanceFrontier<N> {
   List<DominatorNode<N>> getDominanceFrontierOf(DominatorNode<N> var1);
}
