package soot.jbco.jimpleTransformations;

import java.util.Iterator;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.DefinitionStmt;
import soot.jimple.NewExpr;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;

public class New2InitFlowAnalysis extends BackwardFlowAnalysis<Unit, FlowSet> {
   FlowSet emptySet = new ArraySparseSet();

   public New2InitFlowAnalysis(DirectedGraph<Unit> graph) {
      super(graph);
      this.doAnalysis();
   }

   protected void flowThrough(FlowSet in, Unit d, FlowSet out) {
      in.copy(out);
      if (d instanceof DefinitionStmt) {
         DefinitionStmt ds = (DefinitionStmt)d;
         if (ds.getRightOp() instanceof NewExpr) {
            Value v = ds.getLeftOp();
            if (v instanceof Local && in.contains(v)) {
               out.remove(v);
            }
         }
      } else {
         Iterator var7 = d.getUseBoxes().iterator();

         while(var7.hasNext()) {
            ValueBox useBox = (ValueBox)var7.next();
            Value v = useBox.getValue();
            if (v instanceof Local) {
               out.add(v);
            }
         }
      }

   }

   protected FlowSet newInitialFlow() {
      return this.emptySet.clone();
   }

   protected FlowSet entryInitialFlow() {
      return this.emptySet.clone();
   }

   protected void merge(FlowSet in1, FlowSet in2, FlowSet out) {
      in1.union(in2, out);
   }

   protected void copy(FlowSet source, FlowSet dest) {
      source.copy(dest);
   }
}
