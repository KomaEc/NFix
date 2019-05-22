package soot.toolkits.graph;

import java.util.HashMap;

public class MemoryEfficientGraph<N> extends HashMutableDirectedGraph<N> {
   HashMap<N, N> self = new HashMap();

   public void addNode(N o) {
      super.addNode(o);
      this.self.put(o, o);
   }

   public void removeNode(N o) {
      super.removeNode(o);
      this.self.remove(o);
   }

   public void addEdge(N from, N to) {
      if (this.containsNode(from) && this.containsNode(to)) {
         super.addEdge(this.self.get(from), this.self.get(to));
      } else if (!this.containsNode(from)) {
         throw new RuntimeException(from.toString() + " not in graph!");
      } else {
         throw new RuntimeException(to.toString() + " not in graph!");
      }
   }

   public void removeEdge(N from, N to) {
      if (this.containsNode(from) && this.containsNode(to)) {
         super.removeEdge(this.self.get(from), this.self.get(to));
      } else if (!this.containsNode(from)) {
         throw new RuntimeException(from.toString() + " not in graph!");
      } else {
         throw new RuntimeException(to.toString() + " not in graph!");
      }
   }
}
