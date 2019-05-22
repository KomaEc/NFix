package soot.shimple;

import soot.shimple.toolkits.graph.GlobalValueNumberer;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.DominanceFrontier;
import soot.toolkits.graph.DominatorTree;
import soot.toolkits.graph.DominatorsFinder;
import soot.toolkits.graph.ReversibleGraph;
import soot.toolkits.graph.UnitGraph;

public interface ShimpleFactory {
   void clearCache();

   UnitGraph getUnitGraph();

   BlockGraph getBlockGraph();

   DominatorsFinder<Block> getDominatorsFinder();

   DominatorTree<Block> getDominatorTree();

   DominanceFrontier<Block> getDominanceFrontier();

   GlobalValueNumberer getGlobalValueNumberer();

   ReversibleGraph<Block> getReverseBlockGraph();

   DominatorsFinder<Block> getReverseDominatorsFinder();

   DominatorTree<Block> getReverseDominatorTree();

   DominanceFrontier<Block> getReverseDominanceFrontier();
}
