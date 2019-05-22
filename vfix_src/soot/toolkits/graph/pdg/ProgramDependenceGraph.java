package soot.toolkits.graph.pdg;

import java.util.List;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.MutableEdgeLabelledDirectedGraph;

public interface ProgramDependenceGraph extends MutableEdgeLabelledDirectedGraph<PDGNode, String> {
   List<Region> getWeakRegions();

   List<Region> getStrongRegions();

   List<PDGRegion> getPDGRegions();

   IRegion GetStartRegion();

   BlockGraph getBlockGraph();

   PDGNode GetStartNode();

   boolean dependentOn(PDGNode var1, PDGNode var2);

   List<PDGNode> getDependents(PDGNode var1);

   PDGNode getPDGNode(Object var1);

   String toString();
}
