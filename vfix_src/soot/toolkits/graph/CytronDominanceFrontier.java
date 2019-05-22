package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CytronDominanceFrontier<N> implements DominanceFrontier<N> {
   protected DominatorTree<N> dt;
   protected Map<DominatorNode<N>, List<DominatorNode<N>>> nodeToFrontier;

   public CytronDominanceFrontier(DominatorTree<N> dt) {
      this.dt = dt;
      this.nodeToFrontier = new HashMap();
      Iterator var2 = dt.getHeads().iterator();

      while(var2.hasNext()) {
         DominatorNode<N> head = (DominatorNode)var2.next();
         this.bottomUpDispatch(head);
      }

      var2 = dt.graph.iterator();

      DominatorNode dode;
      do {
         if (!var2.hasNext()) {
            return;
         }

         N gode = var2.next();
         dode = dt.fetchDode(gode);
         if (dode == null) {
            throw new RuntimeException("dode == null");
         }
      } while(this.isFrontierKnown(dode));

      System.out.print("'");
      System.out.print(dode);
      System.out.println("'");
      throw new RuntimeException("frontier did not have dode> ");
   }

   public List<DominatorNode<N>> getDominanceFrontierOf(DominatorNode<N> node) {
      List<DominatorNode<N>> frontier = (List)this.nodeToFrontier.get(node);
      if (frontier == null) {
         throw new RuntimeException("Frontier not defined for node: " + node);
      } else {
         return new ArrayList(frontier);
      }
   }

   protected boolean isFrontierKnown(DominatorNode<N> node) {
      return this.nodeToFrontier.containsKey(node);
   }

   protected void bottomUpDispatch(DominatorNode<N> node) {
      if (!this.isFrontierKnown(node)) {
         Iterator var2 = this.dt.getChildrenOf(node).iterator();

         while(var2.hasNext()) {
            DominatorNode<N> child = (DominatorNode)var2.next();
            if (!this.isFrontierKnown(child)) {
               this.bottomUpDispatch(child);
            }
         }

         this.processNode(node);
      }
   }

   protected void processNode(DominatorNode<N> node) {
      List<DominatorNode<N>> dominanceFrontier = new ArrayList();
      Iterator succsIt = this.dt.getSuccsOf(node).iterator();

      DominatorNode child;
      while(succsIt.hasNext()) {
         child = (DominatorNode)succsIt.next();
         if (!this.dt.isImmediateDominatorOf(node, child)) {
            dominanceFrontier.add(child);
         }
      }

      succsIt = this.dt.getChildrenOf(node).iterator();

      while(succsIt.hasNext()) {
         child = (DominatorNode)succsIt.next();
         Iterator var5 = this.getDominanceFrontierOf(child).iterator();

         while(var5.hasNext()) {
            DominatorNode<N> childFront = (DominatorNode)var5.next();
            if (!this.dt.isImmediateDominatorOf(node, childFront)) {
               dominanceFrontier.add(childFront);
            }
         }
      }

      this.nodeToFrontier.put(node, dominanceFrontier);
   }
}
