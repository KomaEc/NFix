package soot.toolkits.scalar;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Local;
import soot.Timers;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.options.Options;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.util.Cons;
import soot.util.LocalBitSetPacker;

public class SmartLocalDefs implements LocalDefs {
   private static final Logger logger = LoggerFactory.getLogger(SmartLocalDefs.class);
   private final Map<Cons<Unit, Local>, List<Unit>> answer;
   private final Map<Local, Set<Unit>> localToDefs;
   private final UnitGraph graph;
   private final SmartLocalDefs.LocalDefsAnalysis analysis;
   private final Map<Unit, BitSet> liveLocalsAfter;

   public void printAnswer() {
      System.out.println(this.answer.toString());
   }

   private static <T> List<T> asList(Set<T> a, Set<T> b) {
      if (a != null && b != null && !a.isEmpty() && !b.isEmpty()) {
         ArrayList c;
         if (a.size() < b.size()) {
            c = new ArrayList(a);
            c.retainAll(b);
            return c;
         } else {
            c = new ArrayList(b);
            c.retainAll(a);
            return c;
         }
      } else {
         return Collections.emptyList();
      }
   }

   public SmartLocalDefs(UnitGraph g, LiveLocals live) {
      this.graph = g;
      if (Options.v().time()) {
         Timers.v().defsTimer.start();
      }

      if (Options.v().verbose()) {
         logger.debug("[" + g.getBody().getMethod().getName() + "]     Constructing SmartLocalDefs...");
      }

      LocalBitSetPacker localPacker = new LocalBitSetPacker(g.getBody());
      localPacker.pack();
      this.localToDefs = new HashMap();
      this.liveLocalsAfter = new HashMap();
      Iterator var4 = this.graph.iterator();

      Unit u;
      Iterator var7;
      while(var4.hasNext()) {
         u = (Unit)var4.next();
         BitSet set = new BitSet(localPacker.getLocalCount());
         var7 = live.getLiveLocalsAfter(u).iterator();

         while(var7.hasNext()) {
            Local l = (Local)var7.next();
            set.set(l.getNumber());
         }

         this.liveLocalsAfter.put(u, set);
         Local l = this.localDef(u);
         if (l != null) {
            this.addDefOf(l, u);
         }
      }

      if (Options.v().verbose()) {
         logger.debug("[" + g.getBody().getMethod().getName() + "]        done localToDefs map...");
      }

      if (Options.v().verbose()) {
         logger.debug("[" + g.getBody().getMethod().getName() + "]        done unitToMask map...");
      }

      this.analysis = new SmartLocalDefs.LocalDefsAnalysis(this.graph);
      this.answer = new HashMap();
      var4 = this.graph.iterator();

      while(true) {
         Set s1;
         do {
            do {
               if (!var4.hasNext()) {
                  localPacker.unpack();
                  if (Options.v().time()) {
                     Timers.v().defsTimer.end();
                  }

                  if (Options.v().verbose()) {
                     logger.debug("[" + g.getBody().getMethod().getName() + "]     SmartLocalDefs finished.");
                  }

                  return;
               }

               u = (Unit)var4.next();
               s1 = (Set)this.analysis.getFlowBefore(u);
            } while(s1 == null);
         } while(s1.isEmpty());

         var7 = u.getUseBoxes().iterator();

         while(var7.hasNext()) {
            ValueBox vb = (ValueBox)var7.next();
            Value v = vb.getValue();
            if (v instanceof Local) {
               Local l = (Local)v;
               Set<Unit> s2 = this.defsOf(l);
               if (s2 != null && !s2.isEmpty()) {
                  List<Unit> lst = asList(s1, s2);
                  if (!lst.isEmpty()) {
                     Cons<Unit, Local> key = new Cons(u, l);
                     if (!this.answer.containsKey(key)) {
                        this.answer.put(key, lst);
                     }
                  }
               }
            }
         }
      }
   }

   private Local localDef(Unit u) {
      List<ValueBox> defBoxes = u.getDefBoxes();
      int size = defBoxes.size();
      if (size == 0) {
         return null;
      } else if (size != 1) {
         throw new RuntimeException();
      } else {
         ValueBox vb = (ValueBox)defBoxes.get(0);
         Value v = vb.getValue();
         return !(v instanceof Local) ? null : (Local)v;
      }
   }

   private Set<Unit> defsOf(Local l) {
      Set<Unit> s = (Set)this.localToDefs.get(l);
      return s == null ? Collections.emptySet() : s;
   }

   private void addDefOf(Local l, Unit u) {
      Set<Unit> s = (Set)this.localToDefs.get(l);
      if (s == null) {
         this.localToDefs.put(l, s = new HashSet());
      }

      ((Set)s).add(u);
   }

   public List<Unit> getDefsOfAt(Local l, Unit s) {
      List<Unit> lst = (List)this.answer.get(new Cons(s, l));
      return lst == null ? Collections.emptyList() : lst;
   }

   public List<Unit> getDefsOf(Local l) {
      List<Unit> result = new ArrayList();
      Iterator var3 = this.answer.keySet().iterator();

      while(var3.hasNext()) {
         Cons<Unit, Local> cons = (Cons)var3.next();
         if (cons.cdr() == l) {
            result.addAll((Collection)this.answer.get(cons));
         }
      }

      return result;
   }

   public UnitGraph getGraph() {
      return this.graph;
   }

   class LocalDefsAnalysis extends ForwardFlowAnalysisExtended<Unit, Set<Unit>> {
      LocalDefsAnalysis(UnitGraph g) {
         super(g);
         this.doAnalysis();
      }

      protected void mergeInto(Unit succNode, Set<Unit> inout, Set<Unit> in) {
         inout.addAll(in);
      }

      protected void merge(Set<Unit> in1, Set<Unit> in2, Set<Unit> out) {
         throw new RuntimeException("should never be called");
      }

      protected void flowThrough(Set<Unit> in, Unit u, Unit succ, Set<Unit> out) {
         ExceptionalUnitGraph exGraph = this.graph instanceof ExceptionalUnitGraph ? (ExceptionalUnitGraph)this.graph : null;
         out.clear();
         BitSet liveLocals = (BitSet)SmartLocalDefs.this.liveLocalsAfter.get(u);
         Local l = SmartLocalDefs.this.localDef(u);
         if (l == null) {
            Iterator var8 = in.iterator();

            while(var8.hasNext()) {
               Unit inUx = (Unit)var8.next();
               if (liveLocals.get(SmartLocalDefs.this.localDef(inUx).getNumber())) {
                  out.add(inUx);
               }
            }

         } else {
            Set<Unit> allDefUnits = SmartLocalDefs.this.defsOf(l);
            boolean isExceptionalTarget = false;
            Iterator var10;
            if (exGraph != null) {
               var10 = exGraph.getExceptionDests(u).iterator();

               while(var10.hasNext()) {
                  ExceptionalUnitGraph.ExceptionDest ed = (ExceptionalUnitGraph.ExceptionDest)var10.next();
                  if (ed.getTrap() != null && ed.getTrap().getHandlerUnit() == succ) {
                     isExceptionalTarget = true;
                  }
               }
            }

            var10 = in.iterator();

            while(true) {
               Unit inU;
               do {
                  do {
                     if (!var10.hasNext()) {
                        assert isExceptionalTarget || !out.removeAll(allDefUnits);

                        if (liveLocals.get(l.getNumber()) && !isExceptionalTarget) {
                           out.add(u);
                        }

                        return;
                     }

                     inU = (Unit)var10.next();
                  } while(!liveLocals.get(SmartLocalDefs.this.localDef(inU).getNumber()));
               } while(!isExceptionalTarget && allDefUnits.contains(inU));

               out.add(inU);
            }
         }
      }

      protected void copy(Set<Unit> sourceSet, Set<Unit> destSet) {
         destSet.clear();
         destSet.addAll(sourceSet);
      }

      protected Set<Unit> newInitialFlow() {
         return new HashSet();
      }

      protected Set<Unit> entryInitialFlow() {
         return new HashSet();
      }
   }
}
