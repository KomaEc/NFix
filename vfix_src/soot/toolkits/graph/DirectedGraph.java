package soot.toolkits.graph;

import java.util.Iterator;
import java.util.List;

public interface DirectedGraph<N> extends Iterable<N> {
   List<N> getHeads();

   List<N> getTails();

   List<N> getPredsOf(N var1);

   List<N> getSuccsOf(N var1);

   int size();

   Iterator<N> iterator();
}
