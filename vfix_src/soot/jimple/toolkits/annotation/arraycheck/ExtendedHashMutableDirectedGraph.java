package soot.jimple.toolkits.annotation.arraycheck;

import java.util.Iterator;
import java.util.List;
import soot.toolkits.graph.HashMutableDirectedGraph;

class ExtendedHashMutableDirectedGraph extends HashMutableDirectedGraph {
   public ExtendedHashMutableDirectedGraph() {
   }

   public void addEdge(Object from, Object to) {
      if (!super.containsNode(from)) {
         super.addNode(from);
      }

      if (!super.containsNode(to)) {
         super.addNode(to);
      }

      super.addEdge(from, to);
   }

   public void addMutualEdge(Object from, Object to) {
      if (!super.containsNode(from)) {
         super.addNode(from);
      }

      if (!super.containsNode(to)) {
         super.addNode(to);
      }

      super.addEdge(from, to);
      super.addEdge(to, from);
   }

   public void skipNode(Object node) {
      if (super.containsNode(node)) {
         Object[] preds = this.getPredsOf(node).toArray();
         Object[] succs = this.getSuccsOf(node).toArray();
         Object[] var4 = preds;
         int var5 = preds.length;

         int var6;
         Object element;
         for(var6 = 0; var6 < var5; ++var6) {
            element = var4[var6];
            Object[] var8 = succs;
            int var9 = succs.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               Object element0 = var8[var10];
               if (element != element0) {
                  super.addEdge(element, element0);
               }
            }
         }

         var4 = preds;
         var5 = preds.length;

         for(var6 = 0; var6 < var5; ++var6) {
            element = var4[var6];
            super.removeEdge(element, node);
         }

         var4 = succs;
         var5 = succs.length;

         for(var6 = 0; var6 < var5; ++var6) {
            element = var4[var6];
            super.removeEdge(node, element);
         }

         super.removeNode(node);
      }
   }

   public void mergeWith(ExtendedHashMutableDirectedGraph other) {
      List<Object> nodes = other.getNodes();
      Iterator nodesIt = nodes.iterator();

      while(nodesIt.hasNext()) {
         Object node = nodesIt.next();
         List succs = other.getSuccsOf(node);
         Iterator succsIt = succs.iterator();

         while(succsIt.hasNext()) {
            Object succ = succsIt.next();
            this.addEdge(node, succ);
         }
      }

   }

   public String toString() {
      String rtn = "Graph:\n";
      List nodes = super.getNodes();
      Iterator nodesIt = nodes.iterator();

      while(nodesIt.hasNext()) {
         Object node = nodesIt.next();
         List succs = super.getSuccsOf(node);

         Object succ;
         for(Iterator succsIt = succs.iterator(); succsIt.hasNext(); rtn = rtn + node + "\t --- \t" + succ + "\n") {
            succ = succsIt.next();
         }
      }

      return rtn;
   }
}
