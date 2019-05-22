package soot.toolkits.scalar;

import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Local;
import soot.Timers;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.options.Options;
import soot.toolkits.graph.UnitGraph;

public class SimpleLiveLocals implements LiveLocals {
   private static final Logger logger = LoggerFactory.getLogger(SimpleLiveLocals.class);
   final FlowAnalysis<Unit, FlowSet<Local>> analysis;

   public SimpleLiveLocals(UnitGraph graph) {
      if (Options.v().time()) {
         Timers.v().liveTimer.start();
      }

      if (Options.v().verbose()) {
         logger.debug("[" + graph.getBody().getMethod().getName() + "]     Constructing SimpleLiveLocals...");
      }

      this.analysis = new SimpleLiveLocals.Analysis(graph);
      if (Options.v().time()) {
         Timers.v().liveAnalysisTimer.start();
      }

      this.analysis.doAnalysis();
      if (Options.v().time()) {
         Timers.v().liveAnalysisTimer.end();
      }

      if (Options.v().time()) {
         Timers.v().liveTimer.end();
      }

   }

   public List<Local> getLiveLocalsAfter(Unit s) {
      return ((FlowSet)this.analysis.getFlowAfter(s)).toList();
   }

   public List<Local> getLiveLocalsBefore(Unit s) {
      return ((FlowSet)this.analysis.getFlowBefore(s)).toList();
   }

   static class Analysis extends BackwardFlowAnalysis<Unit, FlowSet<Local>> {
      Analysis(UnitGraph g) {
         super(g);
      }

      protected FlowSet<Local> newInitialFlow() {
         return new ArraySparseSet();
      }

      protected void flowThrough(FlowSet<Local> in, Unit unit, FlowSet<Local> out) {
         in.copy(out);
         Iterator var4 = unit.getDefBoxes().iterator();

         ValueBox box;
         Value v;
         Local l;
         while(var4.hasNext()) {
            box = (ValueBox)var4.next();
            v = box.getValue();
            if (v instanceof Local) {
               l = (Local)v;
               out.remove(l);
            }
         }

         var4 = unit.getUseBoxes().iterator();

         while(var4.hasNext()) {
            box = (ValueBox)var4.next();
            v = box.getValue();
            if (v instanceof Local) {
               l = (Local)v;
               out.add(l);
            }
         }

      }

      protected void mergeInto(Unit succNode, FlowSet<Local> inout, FlowSet<Local> in) {
         inout.union(in);
      }

      protected void merge(FlowSet<Local> in1, FlowSet<Local> in2, FlowSet<Local> out) {
         in1.union(in2, out);
      }

      protected void copy(FlowSet<Local> source, FlowSet<Local> dest) {
         source.copy(dest);
      }
   }
}
