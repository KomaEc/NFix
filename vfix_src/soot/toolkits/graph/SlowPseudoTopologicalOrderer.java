package soot.toolkits.graph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import soot.G;
import soot.Singletons;

public class SlowPseudoTopologicalOrderer<N> implements Orderer<N> {
   private Map<N, Integer> stmtToColor;
   private static final int WHITE = 0;
   private static final int GRAY = 1;
   private static final int BLACK = 2;
   private LinkedList<N> order;
   private boolean mIsReversed = false;
   private DirectedGraph<N> graph;
   private List<N> reverseOrder;
   private final HashMap<N, List<N>> succsMap = new HashMap();

   public SlowPseudoTopologicalOrderer(Singletons.Global g) {
   }

   public static SlowPseudoTopologicalOrderer v() {
      return G.v().soot_toolkits_graph_SlowPseudoTopologicalOrderer();
   }

   public SlowPseudoTopologicalOrderer() {
   }

   public SlowPseudoTopologicalOrderer(boolean isReversed) {
      this.mIsReversed = isReversed;
   }

   public List<N> newList(DirectedGraph<N> g, boolean reverse) {
      this.mIsReversed = reverse;
      return this.computeOrder(g);
   }

   LinkedList<N> computeOrder(DirectedGraph<N> g) {
      this.stmtToColor = new HashMap();
      this.order = new LinkedList();
      this.graph = g;
      SlowPseudoTopologicalOrderer<N>.PseudoTopologicalReverseOrderer<N> orderer = new SlowPseudoTopologicalOrderer.PseudoTopologicalReverseOrderer();
      this.reverseOrder = orderer.newList(g);
      Iterator stmtIt = g.iterator();

      Object s;
      while(stmtIt.hasNext()) {
         s = stmtIt.next();
         this.stmtToColor.put(s, 0);
      }

      stmtIt = g.iterator();

      while(stmtIt.hasNext()) {
         s = stmtIt.next();
         if ((Integer)this.stmtToColor.get(s) == 0) {
            this.visitNode(s);
         }
      }

      return this.order;
   }

   private void visitNode(N startStmt) {
      LinkedList<N> stmtStack = new LinkedList();
      LinkedList<Integer> indexStack = new LinkedList();
      this.stmtToColor.put(startStmt, 1);
      stmtStack.addLast(startStmt);
      indexStack.addLast(-1);

      while(true) {
         while(!stmtStack.isEmpty()) {
            int toVisitIndex = (Integer)indexStack.removeLast();
            N toVisitNode = stmtStack.getLast();
            ++toVisitIndex;
            indexStack.addLast(toVisitIndex);
            if (toVisitIndex >= this.graph.getSuccsOf(toVisitNode).size()) {
               if (this.mIsReversed) {
                  this.order.addLast(toVisitNode);
               } else {
                  this.order.addFirst(toVisitNode);
               }

               this.stmtToColor.put(toVisitNode, 2);
               stmtStack.removeLast();
               indexStack.removeLast();
            } else {
               List<N> orderedSuccs = (List)this.succsMap.get(toVisitNode);
               if (orderedSuccs == null) {
                  orderedSuccs = new LinkedList();
                  this.succsMap.put(toVisitNode, orderedSuccs);
                  List<N> allsuccs = this.graph.getSuccsOf(toVisitNode);

                  for(int i = 0; i < allsuccs.size(); ++i) {
                     N cur = allsuccs.get(i);

                     int j;
                     for(j = 0; j < ((List)orderedSuccs).size(); ++j) {
                        N comp = ((List)orderedSuccs).get(j);
                        int idx1 = this.reverseOrder.indexOf(cur);
                        int idx2 = this.reverseOrder.indexOf(comp);
                        if (idx1 < idx2) {
                           break;
                        }
                     }

                     ((List)orderedSuccs).add(j, cur);
                  }
               }

               N childNode = ((List)orderedSuccs).get(toVisitIndex);
               if ((Integer)this.stmtToColor.get(childNode) == 0) {
                  this.stmtToColor.put(childNode, 1);
                  stmtStack.addLast(childNode);
                  indexStack.addLast(-1);
               }
            }
         }

         return;
      }
   }

   /** @deprecated */
   @Deprecated
   public List<N> newList(DirectedGraph<N> g) {
      return this.computeOrder(g);
   }

   /** @deprecated */
   @Deprecated
   public void setReverseOrder(boolean isReversed) {
      this.mIsReversed = isReversed;
   }

   /** @deprecated */
   @Deprecated
   public boolean isReverseOrder() {
      return this.mIsReversed;
   }

   private class PseudoTopologicalReverseOrderer<N> {
      private Map<N, Integer> stmtToColor;
      private static final int WHITE = 0;
      private static final int GRAY = 1;
      private static final int BLACK = 2;
      private LinkedList<N> order;
      private final boolean mIsReversed;
      private DirectedGraph<N> graph;

      private PseudoTopologicalReverseOrderer() {
         this.mIsReversed = false;
      }

      List<N> newList(DirectedGraph<N> g) {
         return this.computeOrder(g);
      }

      LinkedList<N> computeOrder(DirectedGraph<N> g) {
         this.stmtToColor = new HashMap();
         this.order = new LinkedList();
         this.graph = g;
         Iterator stmtIt = g.iterator();

         Object s;
         while(stmtIt.hasNext()) {
            s = stmtIt.next();
            this.stmtToColor.put(s, 0);
         }

         stmtIt = g.iterator();

         while(stmtIt.hasNext()) {
            s = stmtIt.next();
            if ((Integer)this.stmtToColor.get(s) == 0) {
               this.visitNode(s);
            }
         }

         return this.order;
      }

      private void visitNode(N startStmt) {
         LinkedList<N> stmtStack = new LinkedList();
         LinkedList<Integer> indexStack = new LinkedList();
         this.stmtToColor.put(startStmt, 1);
         stmtStack.addLast(startStmt);
         indexStack.addLast(-1);

         while(!stmtStack.isEmpty()) {
            int toVisitIndex = (Integer)indexStack.removeLast();
            N toVisitNode = stmtStack.getLast();
            ++toVisitIndex;
            indexStack.addLast(toVisitIndex);
            if (toVisitIndex >= this.graph.getPredsOf(toVisitNode).size()) {
               this.order.addFirst(toVisitNode);
               this.stmtToColor.put(toVisitNode, 2);
               stmtStack.removeLast();
               indexStack.removeLast();
            } else {
               N childNode = this.graph.getPredsOf(toVisitNode).get(toVisitIndex);
               if ((Integer)this.stmtToColor.get(childNode) == 0) {
                  this.stmtToColor.put(childNode, 1);
                  stmtStack.addLast(childNode);
                  indexStack.addLast(-1);
               }
            }
         }

      }

      // $FF: synthetic method
      PseudoTopologicalReverseOrderer(Object x1) {
         this();
      }
   }
}
