package soot.toolkits.scalar;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.RandomAccess;
import java.util.Set;
import soot.baf.GotoInst;
import soot.jimple.GotoStmt;
import soot.options.Options;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.interaction.FlowInfo;
import soot.toolkits.graph.interaction.InteractionHandler;
import soot.util.Numberable;
import soot.util.PriorityQueue;

public abstract class FlowAnalysis<N, A> extends AbstractFlowAnalysis<N, A> {
   protected Map<N, A> unitToAfterFlow;
   protected Map<N, A> filterUnitToAfterFlow = Collections.emptyMap();

   public FlowAnalysis(DirectedGraph<N> graph) {
      super(graph);
      this.unitToAfterFlow = new IdentityHashMap(graph.size() * 2 + 1);
   }

   protected abstract void flowThrough(A var1, N var2, A var3);

   public A getFlowAfter(N s) {
      A a = this.unitToAfterFlow.get(s);
      return a == null ? this.newInitialFlow() : a;
   }

   public A getFlowBefore(N s) {
      A a = this.unitToBeforeFlow.get(s);
      return a == null ? this.newInitialFlow() : a;
   }

   private void initFlow(Iterable<FlowAnalysis.Entry<N, A>> universe, Map<N, A> in, Map<N, A> out) {
      assert universe != null;

      assert in != null;

      assert out != null;

      Iterator var4 = universe.iterator();

      while(var4.hasNext()) {
         FlowAnalysis.Entry<N, A> n = (FlowAnalysis.Entry)var4.next();
         boolean omit = true;
         if (n.in.length > 1) {
            n.inFlow = this.newInitialFlow();
            omit = !n.isRealStronglyConnected;
         } else {
            assert n.in.length == 1 : "missing superhead";

            n.inFlow = this.getFlow(n.in[0], n);

            assert n.inFlow != null : "topological order is broken";
         }

         if (omit && this.omissible(n.data)) {
            n.outFlow = n.inFlow;
         } else {
            n.outFlow = this.newInitialFlow();
         }

         in.put(n.data, n.inFlow);
         out.put(n.data, n.outFlow);
      }

   }

   protected boolean omissible(N n) {
      return false;
   }

   protected FlowAnalysis.Flow getFlow(N from, N mergeNode) {
      return FlowAnalysis.Flow.OUT;
   }

   private A getFlow(FlowAnalysis.Entry<N, A> o, FlowAnalysis.Entry<N, A> e) {
      return o.inFlow == o.outFlow ? o.outFlow : this.getFlow(o.data, e.data).getFlow(o);
   }

   private void meetFlows(FlowAnalysis.Entry<N, A> e) {
      assert e.in.length >= 1;

      if (e.in.length > 1) {
         boolean copy = true;
         FlowAnalysis.Entry[] var3 = e.in;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            FlowAnalysis.Entry<N, A> o = var3[var5];
            A flow = this.getFlow(o, e);
            if (copy) {
               copy = false;
               this.copy(flow, e.inFlow);
            } else {
               this.mergeInto(e.data, e.inFlow, flow);
            }
         }
      }

   }

   final int doAnalysis(FlowAnalysis.GraphView gv, FlowAnalysis.InteractionFlowHandler ifh, Map<N, A> inFlow, Map<N, A> outFlow) {
      assert gv != null;

      assert ifh != null;

      ifh = Options.v().interactive_mode() ? ifh : FlowAnalysis.InteractionFlowHandler.NONE;
      List<FlowAnalysis.Entry<N, A>> universe = FlowAnalysis.Orderer.INSTANCE.newUniverse(this.graph, gv, this.entryInitialFlow(), this.isForward());
      this.initFlow(universe, inFlow, outFlow);
      Queue<FlowAnalysis.Entry<N, A>> q = PriorityQueue.of(universe, true);
      int numComputations = 0;

      while(true) {
         FlowAnalysis.Entry<N, A> e = (FlowAnalysis.Entry)q.poll();
         if (e == null) {
            return numComputations;
         }

         this.meetFlows(e);
         ifh.handleFlowIn(this, e.data);
         boolean hasChanged = this.flowThrough(e);
         ifh.handleFlowOut(this, e.data);
         if (hasChanged) {
            q.addAll(Arrays.asList(e.out));
         }

         ++numComputations;
      }
   }

   private boolean flowThrough(FlowAnalysis.Entry<N, A> d) {
      if (d.inFlow == d.outFlow) {
         assert !d.isRealStronglyConnected || d.in.length == 1;

         return true;
      } else if (d.isRealStronglyConnected) {
         A out = this.newInitialFlow();
         this.flowThrough(d.inFlow, d.data, out);
         if (out.equals(d.outFlow)) {
            return false;
         } else {
            this.copy(out, d.outFlow);
            return true;
         }
      } else {
         this.flowThrough(d.inFlow, d.data, d.outFlow);
         return true;
      }
   }

   static enum GraphView {
      BACKWARD {
         <N> List<N> getEntries(DirectedGraph<N> g) {
            return g.getTails();
         }

         <N> List<N> getOut(DirectedGraph<N> g, N s) {
            return g.getPredsOf(s);
         }
      },
      FORWARD {
         <N> List<N> getEntries(DirectedGraph<N> g) {
            return g.getHeads();
         }

         <N> List<N> getOut(DirectedGraph<N> g, N s) {
            return g.getSuccsOf(s);
         }
      };

      private GraphView() {
      }

      abstract <N> List<N> getEntries(DirectedGraph<N> var1);

      abstract <N> List<N> getOut(DirectedGraph<N> var1, N var2);

      // $FF: synthetic method
      GraphView(Object x2) {
         this();
      }
   }

   static enum InteractionFlowHandler {
      NONE,
      FORWARD {
         public <A, N> void handleFlowIn(FlowAnalysis<N, A> a, N s) {
            this.beforeEvent(this.stop(s), a, s);
         }

         public <A, N> void handleFlowOut(FlowAnalysis<N, A> a, N s) {
            this.afterEvent(InteractionHandler.v(), a, s);
         }
      },
      BACKWARD {
         public <A, N> void handleFlowIn(FlowAnalysis<N, A> a, N s) {
            this.afterEvent(this.stop(s), a, s);
         }

         public <A, N> void handleFlowOut(FlowAnalysis<N, A> a, N s) {
            this.beforeEvent(InteractionHandler.v(), a, s);
         }
      };

      private InteractionFlowHandler() {
      }

      <A, N> void beforeEvent(InteractionHandler i, FlowAnalysis<N, A> a, N s) {
         A savedFlow = a.filterUnitToBeforeFlow.get(s);
         if (savedFlow == null) {
            savedFlow = a.newInitialFlow();
         }

         a.copy(a.unitToBeforeFlow.get(s), savedFlow);
         i.handleBeforeAnalysisEvent(new FlowInfo(savedFlow, s, true));
      }

      <A, N> void afterEvent(InteractionHandler i, FlowAnalysis<N, A> a, N s) {
         A savedFlow = a.filterUnitToAfterFlow.get(s);
         if (savedFlow == null) {
            savedFlow = a.newInitialFlow();
         }

         a.copy(a.unitToAfterFlow.get(s), savedFlow);
         i.handleAfterAnalysisEvent(new FlowInfo(savedFlow, s, false));
      }

      InteractionHandler stop(Object s) {
         InteractionHandler h = InteractionHandler.v();
         List<?> stopList = h.getStopUnitList();
         if (stopList != null && stopList.contains(s)) {
            h.handleStopAtNodeEvent(s);
         }

         return h;
      }

      public <A, N> void handleFlowIn(FlowAnalysis<N, A> a, N s) {
      }

      public <A, N> void handleFlowOut(FlowAnalysis<N, A> a, N s) {
      }

      // $FF: synthetic method
      InteractionFlowHandler(Object x2) {
         this();
      }
   }

   static enum Orderer {
      INSTANCE;

      <D, F> List<FlowAnalysis.Entry<D, F>> newUniverse(DirectedGraph<D> g, FlowAnalysis.GraphView gv, F entryFlow, boolean isForward) {
         int n = g.size();
         Deque<FlowAnalysis.Entry<D, F>> s = new ArrayDeque(n);
         List<FlowAnalysis.Entry<D, F>> universe = new ArrayList(n);
         Map<D, FlowAnalysis.Entry<D, F>> visited = new HashMap((n + 1) * 4 / 3);
         FlowAnalysis.Entry<D, F> superEntry = new FlowAnalysis.Entry((Object)null, (FlowAnalysis.Entry)null);
         List<D> entries = null;
         List<D> actualEntries = gv.getEntries(g);
         if (!actualEntries.isEmpty()) {
            entries = actualEntries;
         } else {
            if (isForward) {
               throw new RuntimeException("error: no entry point for method in forward analysis");
            }

            entries = new ArrayList();

            assert g.getHeads().size() == 1;

            D head = g.getHeads().get(0);
            Set<D> visitedNodes = new HashSet();
            List<D> workList = new ArrayList();
            D current = null;
            workList.add(head);

            while(!workList.isEmpty()) {
               current = workList.remove(0);
               visitedNodes.add(current);
               if (current instanceof GotoInst || current instanceof GotoStmt) {
                  ((List)entries).add(current);
               }

               Iterator var16 = g.getSuccsOf(current).iterator();

               while(var16.hasNext()) {
                  D next = var16.next();
                  if (!visitedNodes.contains(next)) {
                     workList.add(next);
                  }
               }
            }

            if (((List)entries).isEmpty()) {
               throw new RuntimeException("error: backward analysis on an empty entry set.");
            }
         }

         this.visitEntry(visited, superEntry, (List)entries);
         superEntry.inFlow = entryFlow;
         superEntry.outFlow = entryFlow;
         FlowAnalysis.Entry<D, F>[] sv = new FlowAnalysis.Entry[g.size()];
         int[] si = new int[g.size()];
         int index = 0;
         int i = 0;
         FlowAnalysis.Entry v = superEntry;

         while(true) {
            while(i >= v.out.length) {
               if (index == 0) {
                  assert universe.size() <= g.size();

                  Collections.reverse(universe);
                  return universe;
               }

               universe.add(v);
               this.sccPop(s, v);
               --index;
               v = sv[index];
               i = si[index];
            }

            FlowAnalysis.Entry<D, F> w = v.out[i++];
            if (w.number == Integer.MIN_VALUE) {
               w.number = s.size();
               s.add(w);
               this.visitEntry(visited, w, gv.getOut(g, w.data));
               si[index] = i;
               sv[index] = v;
               ++index;
               i = 0;
               v = w;
            }
         }
      }

      private <D, F> FlowAnalysis.Entry<D, F>[] visitEntry(Map<D, FlowAnalysis.Entry<D, F>> visited, FlowAnalysis.Entry<D, F> v, List<D> out) {
         int n = out.size();
         FlowAnalysis.Entry<D, F>[] a = new FlowAnalysis.Entry[n];

         assert out instanceof RandomAccess;

         for(int i = 0; i < n; ++i) {
            a[i] = this.getEntryOf(visited, out.get(i), v);
         }

         return v.out = a;
      }

      private <D, F> FlowAnalysis.Entry<D, F> getEntryOf(Map<D, FlowAnalysis.Entry<D, F>> visited, D d, FlowAnalysis.Entry<D, F> v) {
         FlowAnalysis.Entry<D, F> newEntry = new FlowAnalysis.Entry(d, v);
         FlowAnalysis.Entry<D, F> oldEntry = (FlowAnalysis.Entry)visited.put(d, newEntry);
         if (oldEntry == null) {
            return newEntry;
         } else {
            visited.put(d, oldEntry);
            if (oldEntry == v) {
               oldEntry.isRealStronglyConnected = true;
            }

            int l = oldEntry.in.length;
            oldEntry.in = (FlowAnalysis.Entry[])Arrays.copyOf(oldEntry.in, l + 1);
            oldEntry.in[l] = v;
            return oldEntry;
         }
      }

      private <D, F> void sccPop(Deque<FlowAnalysis.Entry<D, F>> s, FlowAnalysis.Entry<D, F> v) {
         int min = v.number;
         FlowAnalysis.Entry[] var4 = v.out;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            FlowAnalysis.Entry<D, F> e = var4[var6];

            assert e.number > Integer.MIN_VALUE;

            min = Math.min(min, e.number);
         }

         if (min != v.number) {
            v.number = min;
         } else {
            FlowAnalysis.Entry<D, F> w = (FlowAnalysis.Entry)s.removeLast();
            w.number = Integer.MAX_VALUE;
            if (w != v) {
               w.isRealStronglyConnected = true;

               do {
                  w = (FlowAnalysis.Entry)s.removeLast();

                  assert w.number >= v.number;

                  w.isRealStronglyConnected = true;
                  w.number = Integer.MAX_VALUE;
               } while(w != v);

               assert w.in.length >= 2;

            }
         }
      }
   }

   static class Entry<D, F> implements Numberable {
      final D data;
      int number;
      boolean isRealStronglyConnected;
      FlowAnalysis.Entry<D, F>[] in;
      FlowAnalysis.Entry<D, F>[] out;
      F inFlow;
      F outFlow;

      Entry(D u, FlowAnalysis.Entry<D, F> pred) {
         this.in = (FlowAnalysis.Entry[])(new FlowAnalysis.Entry[]{pred});
         this.data = u;
         this.number = Integer.MIN_VALUE;
         this.isRealStronglyConnected = false;
      }

      public String toString() {
         return this.data == null ? "" : this.data.toString();
      }

      public void setNumber(int n) {
         this.number = n;
      }

      public int getNumber() {
         return this.number;
      }
   }

   public static enum Flow {
      IN {
         <F> F getFlow(FlowAnalysis.Entry<?, F> e) {
            return e.inFlow;
         }
      },
      OUT {
         <F> F getFlow(FlowAnalysis.Entry<?, F> e) {
            return e.outFlow;
         }
      };

      private Flow() {
      }

      abstract <F> F getFlow(FlowAnalysis.Entry<?, F> var1);

      // $FF: synthetic method
      Flow(Object x2) {
         this();
      }
   }
}
