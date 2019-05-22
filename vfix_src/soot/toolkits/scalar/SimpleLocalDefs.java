package soot.toolkits.scalar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.IdentityUnit;
import soot.Local;
import soot.Timers;
import soot.Trap;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.options.Options;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalGraph;
import soot.toolkits.graph.UnitGraph;

public class SimpleLocalDefs implements LocalDefs {
   private LocalDefs def;

   public SimpleLocalDefs(UnitGraph graph) {
      this(graph, SimpleLocalDefs.FlowAnalysisMode.Automatic);
   }

   public SimpleLocalDefs(UnitGraph graph, SimpleLocalDefs.FlowAnalysisMode mode) {
      this(graph, (Collection)graph.getBody().getLocals(), mode);
   }

   SimpleLocalDefs(DirectedGraph<Unit> graph, Collection<Local> locals, SimpleLocalDefs.FlowAnalysisMode mode) {
      this(graph, (Local[])locals.toArray(new Local[locals.size()]), mode);
   }

   SimpleLocalDefs(DirectedGraph<Unit> graph, Local[] locals, boolean omitSSA) {
      this(graph, locals, omitSSA ? SimpleLocalDefs.FlowAnalysisMode.OmitSSA : SimpleLocalDefs.FlowAnalysisMode.Automatic);
   }

   SimpleLocalDefs(DirectedGraph<Unit> graph, Local[] locals, SimpleLocalDefs.FlowAnalysisMode mode) {
      Options options = Options.v();
      if (options.time()) {
         Timers.v().defsTimer.start();
      }

      int N = locals.length;
      int[] oldNumbers = new int[N];

      int i;
      for(i = 0; i < N; ++i) {
         oldNumbers[i] = locals[i].getNumber();
         locals[i].setNumber(i);
      }

      this.init(graph, locals, mode);

      for(i = 0; i < N; ++i) {
         locals[i].setNumber(oldNumbers[i]);
      }

      if (options.time()) {
         Timers.v().defsTimer.end();
      }

   }

   private void init(DirectedGraph<Unit> graph, Local[] locals, SimpleLocalDefs.FlowAnalysisMode mode) {
      List<Unit>[] unitList = new List[locals.length];
      Arrays.fill(unitList, Collections.emptyList());
      boolean omitSSA = mode == SimpleLocalDefs.FlowAnalysisMode.OmitSSA;
      boolean doFlowAnalsis = omitSSA;
      int units = 0;
      Iterator var8 = graph.iterator();

      while(var8.hasNext()) {
         Unit unit = (Unit)var8.next();
         Iterator var10 = unit.getDefBoxes().iterator();

         while(var10.hasNext()) {
            ValueBox box = (ValueBox)var10.next();
            Value v = box.getValue();
            if (v instanceof Local) {
               Local l = (Local)v;
               int lno = l.getNumber();
               switch(unitList[lno].size()) {
               case 0:
                  unitList[lno] = Collections.singletonList(unit);
                  if (omitSSA) {
                     ++units;
                  }
                  break;
               case 1:
                  if (!omitSSA) {
                     ++units;
                  }

                  unitList[lno] = new ArrayList(unitList[lno]);
                  doFlowAnalsis = true;
               default:
                  unitList[lno].add(unit);
                  ++units;
               }
            }
         }
      }

      if (doFlowAnalsis && mode != SimpleLocalDefs.FlowAnalysisMode.FlowInsensitive) {
         this.def = new SimpleLocalDefs.FlowAssignment(graph, locals, unitList, units, omitSSA);
      } else {
         this.def = new SimpleLocalDefs.StaticSingleAssignment(locals, unitList);
      }

   }

   public List<Unit> getDefsOfAt(Local l, Unit s) {
      return this.def.getDefsOfAt(l, s);
   }

   public List<Unit> getDefsOf(Local l) {
      return this.def.getDefsOf(l);
   }

   static enum FlowAnalysisMode {
      Automatic,
      OmitSSA,
      FlowInsensitive;
   }

   private static class FlowAssignment extends ForwardFlowAnalysis<Unit, SimpleLocalDefs.FlowAssignment.FlowBitSet> implements LocalDefs {
      final Map<Local, Integer> locals;
      final List<Unit>[] unitList;
      final int[] localRange;
      final Unit[] universe;
      private Map<Unit, Integer> indexOfUnit;

      FlowAssignment(DirectedGraph<Unit> graph, Local[] locals, List<Unit>[] unitList, int units, boolean omitSSA) {
         super(graph);
         int N = locals.length;
         this.locals = new HashMap(N * 3 / 2 + 7);
         this.unitList = unitList;
         this.universe = new Unit[units];
         this.indexOfUnit = new HashMap(units);
         this.localRange = new int[N + 1];
         int j = 0;

         for(int i = 0; i < N; this.localRange[i] = j) {
            if (!unitList[i].isEmpty()) {
               this.locals.put(locals[i], i);
               Unit u;
               if (unitList[i].size() >= 2) {
                  for(Iterator var9 = unitList[i].iterator(); var9.hasNext(); this.universe[j++] = u) {
                     u = (Unit)var9.next();
                     this.indexOfUnit.put(u, j);
                  }
               } else if (omitSSA) {
                  this.universe[j++] = (Unit)unitList[i].get(0);
               }
            }

            ++i;
         }

         assert this.localRange[N] == units;

         this.doAnalysis();
         this.indexOfUnit.clear();
         this.indexOfUnit = null;
      }

      public List<Unit> getDefsOfAt(Local l, Unit s) {
         Integer lno = (Integer)this.locals.get(l);
         if (lno == null) {
            return Collections.emptyList();
         } else {
            int from = this.localRange[lno];
            int to = this.localRange[lno + 1];

            assert from <= to;

            if (from == to) {
               assert this.unitList[lno].size() == 1;

               return this.unitList[lno];
            } else {
               return ((SimpleLocalDefs.FlowAssignment.FlowBitSet)this.getFlowBefore(s)).asList(from, to);
            }
         }
      }

      protected boolean omissible(Unit u) {
         if (u.getDefBoxes().isEmpty()) {
            return true;
         } else {
            Iterator var2 = u.getDefBoxes().iterator();

            Value v;
            do {
               if (!var2.hasNext()) {
                  return true;
               }

               ValueBox vb = (ValueBox)var2.next();
               v = vb.getValue();
            } while(!(v instanceof Local));

            Local l = (Local)v;
            int lno = l.getNumber();
            return this.localRange[lno] == this.localRange[lno + 1];
         }
      }

      protected FlowAnalysis.Flow getFlow(Unit from, Unit to) {
         if (to instanceof IdentityUnit && this.graph instanceof ExceptionalGraph) {
            ExceptionalGraph<Unit> g = (ExceptionalGraph)this.graph;
            if (!g.getExceptionalPredsOf(to).isEmpty()) {
               Iterator var4 = g.getExceptionDests(from).iterator();

               while(var4.hasNext()) {
                  ExceptionalGraph.ExceptionDest<Unit> exd = (ExceptionalGraph.ExceptionDest)var4.next();
                  Trap trap = exd.getTrap();
                  if (null != trap && trap.getHandlerUnit() == to) {
                     return FlowAnalysis.Flow.IN;
                  }
               }
            }
         }

         return FlowAnalysis.Flow.OUT;
      }

      protected void flowThrough(SimpleLocalDefs.FlowAssignment.FlowBitSet in, Unit unit, SimpleLocalDefs.FlowAssignment.FlowBitSet out) {
         this.copy(in, out);
         Iterator var4 = unit.getDefBoxes().iterator();

         while(var4.hasNext()) {
            ValueBox vb = (ValueBox)var4.next();
            Value v = vb.getValue();
            if (v instanceof Local) {
               Local l = (Local)v;
               int lno = l.getNumber();
               int from = this.localRange[lno];
               int to = this.localRange[1 + lno];
               if (from != to) {
                  assert from <= to;

                  if (to - from == 1) {
                     out.set(from);
                  } else {
                     out.clear(from, to);
                     out.set((Integer)this.indexOfUnit.get(unit));
                  }
               }
            }
         }

      }

      protected void copy(SimpleLocalDefs.FlowAssignment.FlowBitSet source, SimpleLocalDefs.FlowAssignment.FlowBitSet dest) {
         if (dest != source) {
            dest.clear();
            dest.or(source);
         }
      }

      protected SimpleLocalDefs.FlowAssignment.FlowBitSet newInitialFlow() {
         return new SimpleLocalDefs.FlowAssignment.FlowBitSet();
      }

      protected void mergeInto(Unit succNode, SimpleLocalDefs.FlowAssignment.FlowBitSet inout, SimpleLocalDefs.FlowAssignment.FlowBitSet in) {
         inout.or(in);
      }

      protected void merge(SimpleLocalDefs.FlowAssignment.FlowBitSet in1, SimpleLocalDefs.FlowAssignment.FlowBitSet in2, SimpleLocalDefs.FlowAssignment.FlowBitSet out) {
         throw new UnsupportedOperationException("should never be called");
      }

      public List<Unit> getDefsOf(Local l) {
         List<Unit> defs = new ArrayList();
         Iterator var3 = this.graph.iterator();

         while(var3.hasNext()) {
            Unit u = (Unit)var3.next();
            List<Unit> defsOf = this.getDefsOfAt(l, u);
            if (defsOf != null) {
               defs.addAll(defsOf);
            }
         }

         return defs;
      }

      class FlowBitSet extends BitSet {
         private static final long serialVersionUID = -8348696077189400377L;

         FlowBitSet() {
            super(FlowAssignment.this.universe.length);
         }

         List<Unit> asList(int fromIndex, int toIndex) {
            BitSet bits = this;
            if (FlowAssignment.this.universe.length >= toIndex && toIndex >= fromIndex && fromIndex >= 0) {
               if (fromIndex == toIndex) {
                  return Collections.emptyList();
               } else if (fromIndex == toIndex - 1) {
                  return this.get(fromIndex) ? Collections.singletonList(FlowAssignment.this.universe[fromIndex]) : Collections.emptyList();
               } else {
                  int i = this.nextSetBit(fromIndex);
                  if (i >= 0 && i < toIndex) {
                     if (i == toIndex - 1) {
                        return Collections.singletonList(FlowAssignment.this.universe[i]);
                     } else {
                        ArrayList elements = new ArrayList(toIndex - i);

                        do {
                           int endOfRun = Math.min(toIndex, bits.nextClearBit(i + 1));

                           do {
                              elements.add(FlowAssignment.this.universe[i++]);
                           } while(i < endOfRun);

                           if (i >= toIndex) {
                              break;
                           }

                           i = bits.nextSetBit(i + 1);
                        } while(i >= 0 && i < toIndex);

                        return elements;
                     }
                  } else {
                     return Collections.emptyList();
                  }
               }
            } else {
               throw new IndexOutOfBoundsException();
            }
         }
      }
   }

   private static class StaticSingleAssignment implements LocalDefs {
      final Map<Local, List<Unit>> result;

      StaticSingleAssignment(Local[] locals, List<Unit>[] unitList) {
         assert locals.length == unitList.length;

         int N = locals.length;
         this.result = new HashMap(N * 3 / 2 + 7);

         for(int i = 0; i < N; ++i) {
            if (!unitList[i].isEmpty()) {
               assert unitList[i].size() == 1;

               this.result.put(locals[i], unitList[i]);
            }
         }

      }

      public List<Unit> getDefsOfAt(Local l, Unit s) {
         List<Unit> lst = (List)this.result.get(l);
         return lst == null ? Collections.emptyList() : lst;
      }

      public List<Unit> getDefsOf(Local l) {
         return this.getDefsOfAt(l, (Unit)null);
      }
   }
}
