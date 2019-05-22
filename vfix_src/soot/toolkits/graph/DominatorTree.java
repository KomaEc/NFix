package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DominatorTree<N> implements Iterable<DominatorNode<N>> {
   private static final Logger logger = LoggerFactory.getLogger(DominatorTree.class);
   protected DominatorsFinder<N> dominators;
   protected DirectedGraph<N> graph;
   protected List<DominatorNode<N>> heads;
   protected List<DominatorNode<N>> tails;
   protected Map<N, DominatorNode<N>> godeToDode;

   public DominatorTree(DominatorsFinder dominators) {
      this.dominators = dominators;
      this.graph = dominators.getGraph();
      this.heads = new ArrayList();
      this.tails = new ArrayList();
      this.godeToDode = new HashMap();
      this.buildTree();
   }

   public DirectedGraph<N> getGraph() {
      return this.dominators.getGraph();
   }

   public List<DominatorNode<N>> getHeads() {
      return new ArrayList(this.heads);
   }

   public DominatorNode<N> getHead() {
      return this.heads.isEmpty() ? null : (DominatorNode)this.heads.get(0);
   }

   public List<DominatorNode<N>> getTails() {
      return new ArrayList(this.tails);
   }

   public DominatorNode<N> getParentOf(DominatorNode<N> node) {
      return node.getParent();
   }

   public List<DominatorNode<N>> getChildrenOf(DominatorNode<N> node) {
      return new ArrayList(node.getChildren());
   }

   public List<DominatorNode<N>> getPredsOf(DominatorNode<N> node) {
      List<N> preds = this.graph.getPredsOf(node.getGode());
      List<DominatorNode<N>> predNodes = new ArrayList();
      Iterator var4 = preds.iterator();

      while(var4.hasNext()) {
         N pred = var4.next();
         predNodes.add(this.getDode(pred));
      }

      return predNodes;
   }

   public List<DominatorNode<N>> getSuccsOf(DominatorNode<N> node) {
      List<N> succs = this.graph.getSuccsOf(node.getGode());
      List<DominatorNode<N>> succNodes = new ArrayList();
      Iterator var4 = succs.iterator();

      while(var4.hasNext()) {
         N succ = var4.next();
         succNodes.add(this.getDode(succ));
      }

      return succNodes;
   }

   public boolean isImmediateDominatorOf(DominatorNode<N> idom, DominatorNode<N> node) {
      return node.getParent() == idom;
   }

   public boolean isDominatorOf(DominatorNode<N> dom, DominatorNode<N> node) {
      return this.dominators.isDominatedBy(node.getGode(), dom.getGode());
   }

   public DominatorNode<N> getDode(N gode) {
      DominatorNode<N> dode = (DominatorNode)this.godeToDode.get(gode);
      if (dode == null) {
         throw new RuntimeException("Assertion failed: Dominator tree does not have a corresponding dode for gode (" + gode + ")");
      } else {
         return dode;
      }
   }

   public Iterator<DominatorNode<N>> iterator() {
      return this.godeToDode.values().iterator();
   }

   public int size() {
      return this.godeToDode.size();
   }

   protected void buildTree() {
      Iterator var1 = this.graph.iterator();

      while(var1.hasNext()) {
         N gode = var1.next();
         DominatorNode<N> dode = this.fetchDode(gode);
         DominatorNode<N> parent = this.fetchParent(gode);
         if (parent == null) {
            this.heads.add(dode);
         } else {
            parent.addChild(dode);
            dode.setParent(parent);
         }
      }

      var1 = this.iterator();

      while(var1.hasNext()) {
         DominatorNode dode = (DominatorNode)var1.next();
         if (dode.isTail()) {
            this.tails.add(dode);
         }
      }

   }

   protected DominatorNode<N> fetchDode(N gode) {
      DominatorNode dode;
      if (this.godeToDode.containsKey(gode)) {
         dode = (DominatorNode)this.godeToDode.get(gode);
      } else {
         dode = new DominatorNode(gode);
         this.godeToDode.put(gode, dode);
      }

      return dode;
   }

   protected DominatorNode<N> fetchParent(N gode) {
      N immediateDominator = this.dominators.getImmediateDominator(gode);
      return immediateDominator == null ? null : this.fetchDode(immediateDominator);
   }
}
