package soot.jimple.toolkits.thread.mhp;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.toolkits.graph.DirectedGraph;
import soot.util.dot.DotGraph;
import soot.util.dot.DotGraphNode;

public class PegCallGraphToDot {
   public static boolean isBrief = false;
   private static final Map<Object, String> listNodeName = new HashMap();
   public static boolean onepage = true;
   private static int nodecount = 0;

   public PegCallGraphToDot(DirectedGraph graph, boolean onepage, String name) {
      PegCallGraphToDot.onepage = onepage;
      toDotFile(name, graph, "PegCallGraph");
   }

   public static void toDotFile(String methodname, DirectedGraph graph, String graphname) {
      int sequence = 0;
      nodecount = 0;
      Hashtable nodeindex = new Hashtable(graph.size());
      DotGraph canvas = new DotGraph(methodname);
      if (!onepage) {
         canvas.setPageSize(8.5D, 11.0D);
      }

      canvas.setNodeShape("box");
      canvas.setGraphLabel(graphname);
      Iterator nodesIt = graph.iterator();

      Object node;
      String nodeName;
      while(nodesIt.hasNext()) {
         node = nodesIt.next();
         if (node instanceof List) {
            nodeName = "list" + (new Integer(sequence++)).toString();
            String nodeName = makeNodeName(getNodeOrder(nodeindex, nodeName));
            listNodeName.put(node, nodeName);
         }
      }

      nodesIt = graph.iterator();

      while(nodesIt.hasNext()) {
         node = nodesIt.next();
         nodeName = null;
         if (node instanceof List) {
            nodeName = makeNodeName(getNodeOrder(nodeindex, listNodeName.get(node)));
         } else {
            nodeName = makeNodeName(getNodeOrder(nodeindex, node));
         }

         String succName;
         for(Iterator succsIt = graph.getSuccsOf(node).iterator(); succsIt.hasNext(); canvas.drawEdge(nodeName, succName)) {
            Object s = succsIt.next();
            succName = null;
            if (s instanceof List) {
               succName = makeNodeName(getNodeOrder(nodeindex, listNodeName.get(s)));
            } else {
               succName = makeNodeName(getNodeOrder(nodeindex, s));
            }
         }
      }

      if (!isBrief) {
         nodesIt = nodeindex.keySet().iterator();

         while(nodesIt.hasNext()) {
            node = nodesIt.next();
            if (node != null) {
               nodeName = makeNodeName(getNodeOrder(nodeindex, node));
               DotGraphNode dotnode = canvas.getNode(nodeName);
               if (dotnode != null) {
                  dotnode.setLabel(node.toString());
               }
            }
         }
      }

      canvas.plot("pecg.dot");
      listNodeName.clear();
   }

   private static int getNodeOrder(Hashtable<Object, Integer> nodeindex, Object node) {
      Integer index = (Integer)nodeindex.get(node);
      if (index == null) {
         index = new Integer(nodecount++);
         nodeindex.put(node, index);
      }

      return index;
   }

   private static String makeNodeName(int index) {
      return "N" + index;
   }
}
