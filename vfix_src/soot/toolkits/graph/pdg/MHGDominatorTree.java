package soot.toolkits.graph.pdg;

import soot.toolkits.graph.DominatorTree;
import soot.toolkits.graph.DominatorsFinder;

public class MHGDominatorTree<N> extends DominatorTree<N> {
   public MHGDominatorTree(DominatorsFinder<N> dominators) {
      super(dominators);
   }
}
