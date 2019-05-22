package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class StronglyConnectedComponentsFast<N> {
   protected final List<List<N>> componentList = new ArrayList();
   protected final List<List<N>> trueComponentList = new ArrayList();
   protected int index = 0;
   protected Map<N, Integer> indexForNode;
   protected Map<N, Integer> lowlinkForNode;
   protected Stack<N> s;
   protected DirectedGraph<N> g;

   public StronglyConnectedComponentsFast(DirectedGraph<N> g) {
      this.g = g;
      this.s = new Stack();
      this.indexForNode = new HashMap();
      this.lowlinkForNode = new HashMap();
      Iterator var2 = g.iterator();

      while(var2.hasNext()) {
         N node = var2.next();
         if (!this.indexForNode.containsKey(node)) {
            if (g.size() > 1000) {
               this.iterate(node);
            } else {
               this.recurse(node);
            }
         }
      }

      this.indexForNode = null;
      this.lowlinkForNode = null;
      this.s = null;
      g = null;
   }

   protected void recurse(N v) {
      this.indexForNode.put(v, this.index);
      int lowLinkForNodeV;
      this.lowlinkForNode.put(v, lowLinkForNodeV = this.index);
      ++this.index;
      this.s.push(v);
      Iterator var3 = this.g.getSuccsOf(v).iterator();

      Object v2;
      while(var3.hasNext()) {
         v2 = var3.next();
         Integer indexForNodeSucc = (Integer)this.indexForNode.get(v2);
         if (indexForNodeSucc == null) {
            this.recurse(v2);
            this.lowlinkForNode.put(v, lowLinkForNodeV = Math.min(lowLinkForNodeV, (Integer)this.lowlinkForNode.get(v2)));
         } else if (this.s.contains(v2)) {
            this.lowlinkForNode.put(v, lowLinkForNodeV = Math.min(lowLinkForNodeV, indexForNodeSucc));
         }
      }

      if (lowLinkForNodeV == (Integer)this.indexForNode.get(v)) {
         ArrayList scc = new ArrayList();

         do {
            v2 = this.s.pop();
            scc.add(v2);
         } while(v != v2);

         this.componentList.add(scc);
         if (scc.size() > 1) {
            this.trueComponentList.add(scc);
         } else {
            N n = scc.get(0);
            if (this.g.getSuccsOf(n).contains(n)) {
               this.trueComponentList.add(scc);
            }
         }
      }

   }

   protected void iterate(N x) {
      List<N> workList = new ArrayList();
      List<N> backtrackList = new ArrayList();
      workList.add(x);

      while(true) {
         while(!workList.isEmpty()) {
            N v = workList.remove(0);
            boolean hasChildren = false;
            boolean isForward = false;
            if (!this.indexForNode.containsKey(v)) {
               this.indexForNode.put(v, this.index);
               this.lowlinkForNode.put(v, this.index);
               ++this.index;
               this.s.push(v);
               isForward = true;
            }

            Iterator var7 = this.g.getSuccsOf(v).iterator();

            while(var7.hasNext()) {
               N succ = var7.next();
               Integer indexForNodeSucc = (Integer)this.indexForNode.get(succ);
               if (indexForNodeSucc == null) {
                  workList.add(0, succ);
                  hasChildren = true;
                  break;
               }

               int lowLinkForNodeV;
               if (!isForward) {
                  lowLinkForNodeV = (Integer)this.lowlinkForNode.get(v);
                  this.lowlinkForNode.put(v, Math.min(lowLinkForNodeV, (Integer)this.lowlinkForNode.get(succ)));
               } else if (isForward && this.s.contains(succ)) {
                  lowLinkForNodeV = (Integer)this.lowlinkForNode.get(v);
                  this.lowlinkForNode.put(v, Math.min(lowLinkForNodeV, indexForNodeSucc));
               }
            }

            if (hasChildren) {
               backtrackList.add(0, v);
            } else {
               if (!backtrackList.isEmpty()) {
                  workList.add(0, backtrackList.remove(0));
               }

               int lowLinkForNodeV = (Integer)this.lowlinkForNode.get(v);
               if (lowLinkForNodeV == (Integer)this.indexForNode.get(v)) {
                  ArrayList scc = new ArrayList();

                  Object v2;
                  do {
                     v2 = this.s.pop();
                     scc.add(v2);
                  } while(v != v2);

                  this.componentList.add(scc);
                  if (scc.size() > 1) {
                     this.trueComponentList.add(scc);
                  } else {
                     N n = scc.get(0);
                     if (this.g.getSuccsOf(n).contains(n)) {
                        this.trueComponentList.add(scc);
                     }
                  }
               }
            }
         }

         return;
      }
   }

   public List<List<N>> getComponents() {
      return this.componentList;
   }

   public List<List<N>> getTrueComponents() {
      return this.trueComponentList;
   }
}
