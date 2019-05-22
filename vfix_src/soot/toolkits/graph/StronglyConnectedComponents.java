package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.options.Options;
import soot.util.StationaryArrayList;

/** @deprecated */
@Deprecated
public class StronglyConnectedComponents {
   private static final Logger logger = LoggerFactory.getLogger(StronglyConnectedComponents.class);
   private HashMap<Object, Object> nodeToColor;
   private static final Object Visited = new Object();
   private static final Object Black = new Object();
   private final LinkedList<Object> finishingOrder;
   private List<List> componentList = new ArrayList();
   private final HashMap<Object, List<Object>> nodeToComponent = new HashMap();
   MutableDirectedGraph sccGraph = new HashMutableDirectedGraph();
   private final int[] indexStack;
   private final Object[] nodeStack;
   private int last;

   public StronglyConnectedComponents(DirectedGraph g) {
      this.nodeToColor = new HashMap(3 * g.size() / 2, 0.7F);
      this.indexStack = new int[g.size()];
      this.nodeStack = new Object[g.size()];
      this.finishingOrder = new LinkedList();
      Iterator revNodeIt = g.iterator();

      Object s;
      while(revNodeIt.hasNext()) {
         s = revNodeIt.next();
         if (this.nodeToColor.get(s) == null) {
            this.visitNode(g, s);
         }
      }

      this.nodeToColor = new HashMap(3 * g.size(), 0.7F);
      revNodeIt = this.finishingOrder.iterator();

      while(revNodeIt.hasNext()) {
         s = revNodeIt.next();
         if (this.nodeToColor.get(s) == null) {
            List<Object> currentComponent = null;
            currentComponent = new StationaryArrayList();
            this.nodeToComponent.put(s, currentComponent);
            this.sccGraph.addNode(currentComponent);
            this.componentList.add(currentComponent);
            this.visitRevNode(g, s, currentComponent);
         }
      }

      this.componentList = Collections.unmodifiableList(this.componentList);
      if (Options.v().verbose()) {
         logger.debug("Done computing scc components");
         logger.debug("number of nodes in underlying graph: " + g.size());
         logger.debug("number of components: " + this.sccGraph.size());
      }

   }

   private void visitNode(DirectedGraph graph, Object startNode) {
      this.last = 0;
      this.nodeToColor.put(startNode, Visited);
      this.nodeStack[this.last] = startNode;
      this.indexStack[this.last++] = -1;

      while(this.last > 0) {
         int toVisitIndex = ++this.indexStack[this.last - 1];
         Object toVisitNode = this.nodeStack[this.last - 1];
         if (toVisitIndex >= graph.getSuccsOf(toVisitNode).size()) {
            this.finishingOrder.addFirst(toVisitNode);
            --this.last;
         } else {
            Object childNode = graph.getSuccsOf(toVisitNode).get(toVisitIndex);
            if (this.nodeToColor.get(childNode) == null) {
               this.nodeToColor.put(childNode, Visited);
               this.nodeStack[this.last] = childNode;
               this.indexStack[this.last++] = -1;
            }
         }
      }

   }

   private void visitRevNode(DirectedGraph graph, Object startNode, List<Object> currentComponent) {
      this.last = 0;
      this.nodeToColor.put(startNode, Visited);
      this.nodeStack[this.last] = startNode;
      this.indexStack[this.last++] = -1;

      while(this.last > 0) {
         int toVisitIndex = ++this.indexStack[this.last - 1];
         Object toVisitNode = this.nodeStack[this.last - 1];
         if (toVisitIndex >= graph.getPredsOf(toVisitNode).size()) {
            currentComponent.add(toVisitNode);
            this.nodeToComponent.put(toVisitNode, currentComponent);
            this.nodeToColor.put(toVisitNode, Black);
            --this.last;
         } else {
            Object childNode = graph.getPredsOf(toVisitNode).get(toVisitIndex);
            if (this.nodeToColor.get(childNode) == null) {
               this.nodeToColor.put(childNode, Visited);
               this.nodeStack[this.last] = childNode;
               this.indexStack[this.last++] = -1;
            } else if (this.nodeToColor.get(childNode) == Black && this.nodeToComponent.get(childNode) != currentComponent) {
               this.sccGraph.addEdge(this.nodeToComponent.get(childNode), currentComponent);
            }
         }
      }

   }

   public boolean equivalent(Object a, Object b) {
      return this.nodeToComponent.get(a) == this.nodeToComponent.get(b);
   }

   public List<List> getComponents() {
      return this.componentList;
   }

   public List getComponentOf(Object a) {
      return (List)this.nodeToComponent.get(a);
   }

   public DirectedGraph getSuperGraph() {
      return this.sccGraph;
   }
}
