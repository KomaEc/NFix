package soot.toolkits.scalar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.options.Options;
import soot.toolkits.graph.UnitGraph;
import soot.util.Cons;
import soot.util.HashMultiMap;
import soot.util.MultiMap;

public class CombinedDUAnalysis extends BackwardFlowAnalysis<Unit, FlowSet<ValueBox>> implements CombinedAnalysis, LocalDefs, LocalUses, LiveLocals {
   private static final Logger logger = LoggerFactory.getLogger(CombinedDUAnalysis.class);
   private final Map<Cons<Local, Unit>, List<Unit>> defsOfAt = new HashMap();
   private final Map<Unit, List<UnitValueBoxPair>> usesOf = new HashMap();
   private final Map<Unit, List<Local>> liveLocalsBefore = new HashMap();
   private final Map<Unit, List<Local>> liveLocalsAfter = new HashMap();
   private final Map<ValueBox, Unit> useBoxToUnit = new HashMap();
   private final Map<Unit, Value> unitToLocalDefed = new HashMap();
   private final Map<Unit, ArraySparseSet<ValueBox>> unitToLocalUseBoxes = new HashMap();
   private final MultiMap<Value, ValueBox> localToUseBoxes = new HashMultiMap();

   public List<Unit> getDefsOfAt(Local l, Unit s) {
      Cons<Local, Unit> cons = new Cons(l, s);
      List<Unit> ret = (List)this.defsOfAt.get(cons);
      if (ret == null) {
         this.defsOfAt.put(cons, ret = new ArrayList());
      }

      return (List)ret;
   }

   public List<Unit> getDefsOf(Local l) {
      throw new RuntimeException("Not implemented");
   }

   public List<UnitValueBoxPair> getUsesOf(Unit u) {
      List<UnitValueBoxPair> ret = (List)this.usesOf.get(u);
      if (ret == null) {
         Local def = this.localDefed(u);
         if (def == null) {
            this.usesOf.put(u, ret = Collections.emptyList());
         } else {
            this.usesOf.put(u, ret = new ArrayList());
            Iterator var4 = ((FlowSet)this.getFlowAfter(u)).iterator();

            while(var4.hasNext()) {
               ValueBox vb = (ValueBox)var4.next();
               if (vb.getValue() == def) {
                  ((List)ret).add(new UnitValueBoxPair((Unit)this.useBoxToUnit.get(vb), vb));
               }
            }
         }
      }

      return (List)ret;
   }

   public List<Local> getLiveLocalsBefore(Unit u) {
      List<Local> ret = (List)this.liveLocalsBefore.get(u);
      if (ret == null) {
         HashSet<Local> hs = new HashSet();
         Iterator var4 = ((FlowSet)this.getFlowBefore(u)).iterator();

         while(var4.hasNext()) {
            ValueBox vb = (ValueBox)var4.next();
            hs.add((Local)vb.getValue());
         }

         this.liveLocalsBefore.put(u, ret = new ArrayList(hs));
      }

      return (List)ret;
   }

   public List<Local> getLiveLocalsAfter(Unit u) {
      List<Local> ret = (List)this.liveLocalsAfter.get(u);
      if (ret == null) {
         HashSet<Local> hs = new HashSet();
         Iterator var4 = ((FlowSet)this.getFlowAfter(u)).iterator();

         while(var4.hasNext()) {
            ValueBox vb = (ValueBox)var4.next();
            hs.add((Local)vb.getValue());
         }

         this.liveLocalsAfter.put(u, ret = new ArrayList(hs));
      }

      return (List)ret;
   }

   private Local localDefed(Unit u) {
      return (Local)this.unitToLocalDefed.get(u);
   }

   public CombinedDUAnalysis(UnitGraph graph) {
      super(graph);
      if (Options.v().verbose()) {
         logger.debug("[" + graph.getBody().getMethod().getName() + "]     Constructing CombinedDUAnalysis...");
      }

      Iterator var2 = graph.iterator();

      Unit defUnit;
      while(var2.hasNext()) {
         defUnit = (Unit)var2.next();
         List<Value> defs = this.localsInBoxes(defUnit.getDefBoxes());
         if (defs.size() == 1) {
            this.unitToLocalDefed.put(defUnit, defs.get(0));
         } else if (defs.size() != 0) {
            throw new RuntimeException("Locals defed in " + defUnit + ": " + defs.size());
         }

         ArraySparseSet<ValueBox> localUseBoxes = new ArraySparseSet();
         Iterator var6 = defUnit.getUseBoxes().iterator();

         while(var6.hasNext()) {
            ValueBox vb = (ValueBox)var6.next();
            Value v = vb.getValue();
            if (v instanceof Local) {
               localUseBoxes.add(vb);
               if (this.useBoxToUnit.containsKey(vb)) {
                  throw new RuntimeException("Aliased ValueBox " + vb + " in Unit " + defUnit);
               }

               this.useBoxToUnit.put(vb, defUnit);
               this.localToUseBoxes.put(v, vb);
            }
         }

         this.unitToLocalUseBoxes.put(defUnit, localUseBoxes);
      }

      this.doAnalysis();
      var2 = graph.iterator();

      while(true) {
         Local localDefed;
         do {
            if (!var2.hasNext()) {
               if (Options.v().verbose()) {
                  logger.debug("[" + graph.getBody().getMethod().getName() + "]     Finished CombinedDUAnalysis...");
               }

               return;
            }

            defUnit = (Unit)var2.next();
            localDefed = this.localDefed(defUnit);
         } while(localDefed == null);

         Iterator var10 = ((FlowSet)this.getFlowAfter(defUnit)).iterator();

         while(var10.hasNext()) {
            ValueBox vb = (ValueBox)var10.next();
            if (vb.getValue() == localDefed) {
               Unit useUnit = (Unit)this.useBoxToUnit.get(vb);
               this.getDefsOfAt(localDefed, useUnit).add(defUnit);
            }
         }
      }
   }

   private List<Value> localsInBoxes(List<ValueBox> boxes) {
      List<Value> ret = new ArrayList();
      Iterator var3 = boxes.iterator();

      while(var3.hasNext()) {
         ValueBox vb = (ValueBox)var3.next();
         Value v = vb.getValue();
         if (v instanceof Local) {
            ret.add(v);
         }
      }

      return ret;
   }

   protected void merge(FlowSet<ValueBox> inout, FlowSet<ValueBox> in) {
      inout.union(in);
   }

   protected void merge(FlowSet<ValueBox> in1, FlowSet<ValueBox> in2, FlowSet<ValueBox> out) {
      in1.union(in2, out);
   }

   protected void flowThrough(FlowSet<ValueBox> out, Unit u, FlowSet<ValueBox> in) {
      Local def = this.localDefed(u);
      out.copy(in);
      if (def != null) {
         Collection<ValueBox> boxesDefed = this.localToUseBoxes.get(def);
         Iterator var6 = in.iterator();

         while(var6.hasNext()) {
            ValueBox vb = (ValueBox)var6.next();
            if (boxesDefed.contains(vb)) {
               in.remove(vb);
            }
         }
      }

      in.union((FlowSet)this.unitToLocalUseBoxes.get(u));
   }

   protected FlowSet<ValueBox> entryInitialFlow() {
      return new ArraySparseSet();
   }

   protected FlowSet<ValueBox> newInitialFlow() {
      return new ArraySparseSet();
   }

   protected void copy(FlowSet<ValueBox> source, FlowSet<ValueBox> dest) {
      source.copy(dest);
   }
}
