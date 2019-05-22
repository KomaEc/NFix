package soot.toolkits.scalar;

import java.util.BitSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Timers;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.Orderer;
import soot.toolkits.graph.PseudoTopologicalOrderer;

public abstract class ForwardFlowAnalysisExtended<N, A> {
   protected Map<N, Map<N, A>> unitToBeforeFlow;
   protected Map<N, Map<N, A>> unitToAfterFlow;
   protected DirectedGraph<N> graph;

   public ForwardFlowAnalysisExtended(DirectedGraph<N> graph) {
      this.graph = graph;
      this.unitToBeforeFlow = new IdentityHashMap(graph.size() * 2 + 1);
      this.unitToAfterFlow = new IdentityHashMap(graph.size() * 2 + 1);
   }

   protected Orderer<N> constructOrderer() {
      return new PseudoTopologicalOrderer();
   }

   protected abstract A newInitialFlow();

   protected abstract A entryInitialFlow();

   protected abstract void copy(A var1, A var2);

   protected abstract void merge(A var1, A var2, A var3);

   protected void merge(N succNode, A in1, A in2, A out) {
      this.merge(in1, in2, out);
   }

   protected void mergeInto(N succNode, A inout, A in) {
      A tmp = this.newInitialFlow();
      this.merge(succNode, inout, in, tmp);
      this.copy(tmp, inout);
   }

   public A getFromMap(Map<N, Map<N, A>> map, N s, N t) {
      Map<N, A> m = (Map)map.get(s);
      return m == null ? null : m.get(t);
   }

   public void putToMap(Map<N, Map<N, A>> map, N s, N t, A val) {
      Map<N, A> m = (Map)map.get(s);
      if (m == null) {
         m = new IdentityHashMap();
         map.put(s, m);
      }

      ((Map)m).put(t, val);
   }

   protected void doAnalysis() {
      List<N> orderedUnits = this.constructOrderer().newList(this.graph, false);
      int n = orderedUnits.size();
      BitSet head = new BitSet();
      BitSet work = new BitSet(n);
      work.set(0, n);
      Map<N, Integer> index = new IdentityHashMap(n * 2 + 1);
      int numComputations = 0;
      Iterator var7 = orderedUnits.iterator();

      while(var7.hasNext()) {
         N s = var7.next();
         index.put(s, numComputations++);
         Iterator var9 = this.graph.getSuccsOf(s).iterator();

         while(var9.hasNext()) {
            N v = var9.next();
            this.putToMap(this.unitToBeforeFlow, s, v, this.newInitialFlow());
            this.putToMap(this.unitToAfterFlow, s, v, this.newInitialFlow());
         }
      }

      Iterator var17 = this.graph.getHeads().iterator();

      Object previousFlow;
      Object s;
      while(var17.hasNext()) {
         previousFlow = var17.next();
         head.set((Integer)index.get(previousFlow));
         Iterator var19 = this.graph.getSuccsOf(previousFlow).iterator();

         while(var19.hasNext()) {
            s = var19.next();
            this.putToMap(this.unitToBeforeFlow, previousFlow, s, this.entryInitialFlow());
         }
      }

      numComputations = 0;
      previousFlow = this.newInitialFlow();

      for(int i = work.nextSetBit(0); i >= 0; i = work.nextSetBit(i + 1)) {
         work.clear(i);
         s = orderedUnits.get(i);
         int k = i;

         for(Iterator var11 = this.graph.getSuccsOf(s).iterator(); var11.hasNext(); ++numComputations) {
            N v = var11.next();
            A beforeFlow = this.getFromMap(this.unitToBeforeFlow, s, v);
            A afterFlow = this.getFromMap(this.unitToAfterFlow, s, v);
            this.copy(afterFlow, previousFlow);
            Iterator<N> it = this.graph.getPredsOf(s).iterator();
            if (it.hasNext()) {
               this.copy(this.getFromMap(this.unitToAfterFlow, it.next(), s), beforeFlow);

               while(it.hasNext()) {
                  this.mergeInto(s, beforeFlow, this.getFromMap(this.unitToAfterFlow, it.next(), s));
               }

               if (head.get(k)) {
                  this.mergeInto(s, beforeFlow, this.entryInitialFlow());
               }
            }

            this.flowThrough(beforeFlow, s, v, afterFlow);
            boolean hasChanged = !previousFlow.equals(afterFlow);
            if (hasChanged) {
               int j = (Integer)index.get(v);
               work.set(j);
               i = Math.min(i, j - 1);
            }
         }
      }

      Timers var10000 = Timers.v();
      var10000.totalFlowNodes += n;
      var10000 = Timers.v();
      var10000.totalFlowComputations += numComputations;
   }

   protected abstract void flowThrough(A var1, N var2, N var3, A var4);

   public A getFlowBefore(N s) {
      Iterator<N> it = this.graph.getPredsOf(s).iterator();
      A beforeFlow = null;
      if (it.hasNext()) {
         beforeFlow = this.getFromMap(this.unitToAfterFlow, it.next(), s);

         while(it.hasNext()) {
            this.mergeInto(s, beforeFlow, this.getFromMap(this.unitToAfterFlow, it.next(), s));
         }
      }

      return beforeFlow;
   }
}
