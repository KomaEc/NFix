package soot.toolkits.scalar;

import java.util.Iterator;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.toolkits.graph.UnitGraph;

public class InitAnalysis extends ForwardFlowAnalysis<Unit, FlowSet<Local>> {
   FlowSet<Local> allLocals = new ArraySparseSet();

   public InitAnalysis(UnitGraph g) {
      super(g);
      Iterator var2 = g.getBody().getLocals().iterator();

      while(var2.hasNext()) {
         Local loc = (Local)var2.next();
         this.allLocals.add(loc);
      }

      this.doAnalysis();
   }

   protected FlowSet<Local> entryInitialFlow() {
      return new ArraySparseSet();
   }

   protected FlowSet<Local> newInitialFlow() {
      FlowSet<Local> ret = new ArraySparseSet();
      this.allLocals.copy(ret);
      return ret;
   }

   protected void flowThrough(FlowSet<Local> in, Unit unit, FlowSet<Local> out) {
      in.copy(out);
      Iterator var4 = unit.getDefBoxes().iterator();

      while(var4.hasNext()) {
         ValueBox defBox = (ValueBox)var4.next();
         Value lhs = defBox.getValue();
         if (lhs instanceof Local) {
            out.add((Local)lhs);
         }
      }

   }

   protected void merge(FlowSet<Local> in1, FlowSet<Local> in2, FlowSet<Local> out) {
      in1.intersection(in2, out);
   }

   protected void copy(FlowSet<Local> source, FlowSet<Local> dest) {
      source.copy(dest);
   }
}
