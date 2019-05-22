package soot.toolkits.graph;

import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PseudoTopologicalOrderer<N> implements Orderer<N> {
   public static final boolean REVERSE = true;
   private Set<N> visited;
   private int[] indexStack;
   private N[] stmtStack;
   private N[] order;
   private int orderLength;
   private boolean mIsReversed = false;
   private DirectedGraph<N> graph;

   public PseudoTopologicalOrderer() {
   }

   private static <T> void reverseArray(T[] array) {
      int max = array.length >> 1;
      int i = 0;

      for(int j = array.length - 1; i < max; --j) {
         T temp = array[i];
         array[i] = array[j];
         array[j] = temp;
         ++i;
      }

   }

   public List<N> newList(DirectedGraph<N> g, boolean reverse) {
      this.mIsReversed = reverse;
      return this.computeOrder(g, !this.mIsReversed);
   }

   protected final List<N> computeOrder(DirectedGraph<N> g, boolean reverse) {
      int n = g.size();
      this.visited = Collections.newSetFromMap(new IdentityHashMap(n * 2 + 1));
      this.indexStack = new int[n];
      this.stmtStack = (Object[])(new Object[n]);
      this.order = (Object[])(new Object[n]);
      this.graph = g;
      this.orderLength = 0;
      Iterator var4 = g.iterator();

      while(var4.hasNext()) {
         N s = var4.next();
         if (this.visited.add(s)) {
            this.visitNode(s);
         }

         if (this.orderLength == n) {
            break;
         }
      }

      if (reverse) {
         reverseArray(this.order);
      }

      List<N> o = Arrays.asList(this.order);
      this.indexStack = null;
      this.stmtStack = null;
      this.visited = null;
      this.order = null;
      return o;
   }

   protected final void visitNode(N startStmt) {
      int last = 0;
      this.stmtStack[last] = startStmt;
      int last = last + 1;
      this.indexStack[last] = -1;

      while(last > 0) {
         int toVisitIndex = ++this.indexStack[last - 1];
         N toVisitNode = this.stmtStack[last - 1];
         List<N> succs = this.graph.getSuccsOf(toVisitNode);
         if (toVisitIndex >= succs.size()) {
            this.order[this.orderLength++] = toVisitNode;
            --last;
         } else {
            N childNode = succs.get(toVisitIndex);
            if (this.visited.add(childNode)) {
               this.stmtStack[last] = childNode;
               this.indexStack[last++] = -1;
            }
         }
      }

   }

   /** @deprecated */
   @Deprecated
   public PseudoTopologicalOrderer(boolean isReversed) {
      this.mIsReversed = isReversed;
   }

   /** @deprecated */
   @Deprecated
   public List<N> newList(DirectedGraph<N> g) {
      return this.computeOrder(g, !this.mIsReversed);
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
}
