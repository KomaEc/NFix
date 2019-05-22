package soot.jimple.toolkits.thread.mhp;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import soot.jimple.toolkits.thread.mhp.stmt.JPegStmt;
import soot.tagkit.Tag;
import soot.util.Chain;
import soot.util.dot.DotGraph;
import soot.util.dot.DotGraphEdge;
import soot.util.dot.DotGraphNode;

public class PegToDotFile {
   public static final int UNITGRAPH = 0;
   public static final int BLOCKGRAPH = 1;
   public static final int ARRAYBLOCK = 2;
   public static int graphtype = 0;
   public static boolean isBrief = false;
   private static final Map<Object, String> listNodeName = new HashMap();
   private static final Map<Object, String> startNodeToName = new HashMap();
   public static boolean onepage = true;
   private static int nodecount = 0;

   public PegToDotFile(PegGraph graph, boolean onepage, String name) {
      PegToDotFile.onepage = onepage;
      toDotFile(name, graph, "PEG graph");
   }

   public static void toDotFile(String methodname, PegGraph graph, String graphname) {
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
      String nodename;
      while(nodesIt.hasNext()) {
         node = nodesIt.next();
         if (node instanceof List) {
            nodeName = "list" + (new Integer(sequence++)).toString();
            nodename = makeNodeName(getNodeOrder(nodeindex, nodeName));
            listNodeName.put(node, nodeName);
         }
      }

      nodesIt = graph.mainIterator();

      Object s;
      String startNodeName;
      while(nodesIt.hasNext()) {
         node = nodesIt.next();
         nodeName = null;
         if (node instanceof List) {
            nodeName = makeNodeName(getNodeOrder(nodeindex, listNodeName.get(node)));
         } else {
            Tag tag = (Tag)((JPegStmt)node).getTags().get(0);
            nodeName = makeNodeName(getNodeOrder(nodeindex, tag + " " + node));
            if (((JPegStmt)node).getName().equals("start")) {
               startNodeToName.put(node, nodeName);
            }
         }

         for(Iterator succsIt = graph.getSuccsOf(node).iterator(); succsIt.hasNext(); canvas.drawEdge(nodeName, startNodeName)) {
            s = succsIt.next();
            startNodeName = null;
            if (s instanceof List) {
               startNodeName = makeNodeName(getNodeOrder(nodeindex, listNodeName.get(s)));
            } else {
               JPegStmt succ = (JPegStmt)s;
               Tag succTag = (Tag)succ.getTags().get(0);
               startNodeName = makeNodeName(getNodeOrder(nodeindex, succTag + " " + succ));
            }
         }
      }

      System.out.println("Drew main chain");
      System.out.println("while printing, startToThread has size " + graph.getStartToThread().size());
      Set maps = graph.getStartToThread().entrySet();
      System.out.println("maps has size " + maps.size());
      Iterator iter = maps.iterator();

      while(iter.hasNext()) {
         Entry entry = (Entry)iter.next();
         s = entry.getKey();
         System.out.println("startNode is: " + s);
         startNodeName = (String)startNodeToName.get(s);
         System.out.println("startNodeName is: " + startNodeName);
         List runMethodChainList = (List)entry.getValue();
         Iterator it = runMethodChainList.iterator();

         while(it.hasNext()) {
            Chain chain = (Chain)it.next();
            Iterator subNodesIt = chain.iterator();
            boolean firstNode = false;

            while(subNodesIt.hasNext()) {
               Object node = subNodesIt.next();
               String nodeName = null;
               if (node instanceof List) {
                  nodeName = makeNodeName(getNodeOrder(nodeindex, listNodeName.get(node)));
                  System.out.println("Didn't draw list node");
               } else {
                  if (((JPegStmt)node).getName().equals("begin")) {
                     firstNode = true;
                  }

                  Tag tag = (Tag)((JPegStmt)node).getTags().get(0);
                  nodeName = makeNodeName(getNodeOrder(nodeindex, tag + " " + node));
                  if (((JPegStmt)node).getName().equals("start")) {
                     startNodeToName.put(node, nodeName);
                  }

                  if (firstNode) {
                     if (startNodeName == null) {
                        System.out.println("00000000startNodeName is null ");
                     }

                     if (nodeName == null) {
                        System.out.println("00000000nodeName is null ");
                     }

                     DotGraphEdge startThreadEdge = canvas.drawEdge(startNodeName, nodeName);
                     startThreadEdge.setStyle("dotted");
                     firstNode = false;
                  }
               }

               String threadNodeName;
               for(Iterator succsIt = graph.getSuccsOf(node).iterator(); succsIt.hasNext(); canvas.drawEdge(nodeName, threadNodeName)) {
                  Object succ = succsIt.next();
                  threadNodeName = null;
                  if (succ instanceof List) {
                     threadNodeName = makeNodeName(getNodeOrder(nodeindex, listNodeName.get(succ)));
                  } else {
                     JPegStmt succStmt = (JPegStmt)succ;
                     Tag succTag = (Tag)succStmt.getTags().get(0);
                     threadNodeName = makeNodeName(getNodeOrder(nodeindex, succTag + " " + succStmt));
                  }
               }
            }
         }
      }

      if (!isBrief) {
         nodesIt = nodeindex.keySet().iterator();

         while(nodesIt.hasNext()) {
            Object node = nodesIt.next();
            nodename = makeNodeName(getNodeOrder(nodeindex, node));
            DotGraphNode dotnode = canvas.getNode(nodename);
            dotnode.setLabel(node.toString());
         }
      }

      canvas.plot("peg.dot");
      listNodeName.clear();
      startNodeToName.clear();
   }

   private static int getNodeOrder(Hashtable<Object, Integer> nodeindex, Object node) {
      if (node == null) {
         System.out.println("----node is null-----");
         return 0;
      } else {
         Integer index = (Integer)nodeindex.get(node);
         if (index == null) {
            index = new Integer(nodecount++);
            nodeindex.put(node, index);
         }

         return index;
      }
   }

   private static String makeNodeName(int index) {
      return "N" + index;
   }
}
