package soot.jimple.toolkits.thread.mhp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.util.Chain;

public class DominatorsFinder {
   private final Map<Object, FlowSet> unitToDominators = new HashMap();
   private final DirectedGraph peg;

   DominatorsFinder(Chain chain, DirectedGraph pegGraph) {
      this.peg = pegGraph;
      this.find(chain);
   }

   private void find(Chain chain) {
      boolean change = true;
      FlowSet fullSet = new ArraySparseSet();
      FlowSet temp = new ArraySparseSet();
      Iterator chainIt = chain.iterator();

      while(chainIt.hasNext()) {
         fullSet.add(chainIt.next());
      }

      List heads = this.peg.getHeads();
      if (heads.size() != 1) {
         throw new RuntimeException("The size of heads of peg is not equal to 1!");
      } else {
         FlowSet dominators = new ArraySparseSet();
         Object n = heads.get(0);
         dominators.add(n);
         this.unitToDominators.put(n, dominators);
         chainIt = chain.iterator();

         while(chainIt.hasNext()) {
            Object n = chainIt.next();
            if (!heads.contains(n)) {
               FlowSet domin = new ArraySparseSet();
               fullSet.copy(domin);
               this.unitToDominators.put(n, domin);
            }
         }

         System.out.println("===finish init unitToDominators===");
         System.err.println("===finish init unitToDominators===");

         label48:
         do {
            change = false;
            Iterator it = chain.iterator();

            while(true) {
               do {
                  if (!it.hasNext()) {
                     continue label48;
                  }

                  n = it.next();
               } while(heads.contains(n));

               fullSet.copy(temp);
               Iterator predsIt = this.peg.getPredsOf(n).iterator();

               while(predsIt.hasNext()) {
                  Object p = predsIt.next();
                  FlowSet dom = this.getDominatorsOf(p);
                  temp.intersection(dom);
               }

               FlowSet d = new ArraySparseSet();
               FlowSet nSet = new ArraySparseSet();
               nSet.add(n);
               nSet.union(temp, d);
               FlowSet dominN = this.getDominatorsOf(n);
               if (!d.equals(dominN)) {
                  change = true;
               }
            }
         } while(!change);

      }
   }

   public FlowSet getDominatorsOf(Object s) {
      if (!this.unitToDominators.containsKey(s)) {
         throw new RuntimeException("Invalid stmt" + s);
      } else {
         return (FlowSet)this.unitToDominators.get(s);
      }
   }
}
