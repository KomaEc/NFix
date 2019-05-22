package soot.jimple.toolkits.scalar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import soot.SideEffectTester;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.Expr;
import soot.jimple.FieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;

public class FastAvailableExpressionsAnalysis extends ForwardFlowAnalysis<Unit, FlowSet<Value>> {
   SideEffectTester st;
   Map<Unit, FlowSet<Value>> unitToGenerateSet;
   Map<Unit, FlowSet<Value>> unitToPreserveSet;
   Map<Value, Unit> rhsToContainingStmt;
   FlowSet<Value> emptySet;

   public FastAvailableExpressionsAnalysis(DirectedGraph<Unit> dg, SootMethod m, SideEffectTester st) {
      super(dg);
      this.st = st;
      ExceptionalUnitGraph g = (ExceptionalUnitGraph)dg;
      this.rhsToContainingStmt = new HashMap();
      this.emptySet = new ToppedSet(new ArraySparseSet());
      this.unitToGenerateSet = new HashMap(g.size() * 2 + 1, 0.7F);

      Unit s;
      FlowSet genSet;
      for(Iterator var5 = g.iterator(); var5.hasNext(); this.unitToGenerateSet.put(s, genSet)) {
         s = (Unit)var5.next();
         genSet = this.emptySet.clone();
         if (s instanceof AssignStmt) {
            AssignStmt as = (AssignStmt)s;
            if (as.getRightOp() instanceof Expr || as.getRightOp() instanceof FieldRef) {
               Value gen = as.getRightOp();
               this.rhsToContainingStmt.put(gen, s);
               boolean cantAdd = false;
               if (gen instanceof NewExpr || gen instanceof NewArrayExpr || gen instanceof NewMultiArrayExpr) {
                  cantAdd = true;
               }

               if (gen instanceof InvokeExpr) {
                  cantAdd = true;
               }

               if (!cantAdd) {
                  genSet.add(gen, genSet);
               }
            }
         }
      }

      this.doAnalysis();
   }

   protected FlowSet<Value> newInitialFlow() {
      FlowSet<Value> newSet = this.emptySet.clone();
      ((ToppedSet)newSet).setTop(true);
      return newSet;
   }

   protected FlowSet<Value> entryInitialFlow() {
      return this.emptySet.clone();
   }

   protected void flowThrough(FlowSet<Value> in, Unit u, FlowSet<Value> out) {
      in.copy(out);
      if (!((ToppedSet)in).isTop()) {
         out.union((FlowSet)this.unitToGenerateSet.get(u), out);
         if (((ToppedSet)out).isTop()) {
            throw new RuntimeException("trying to kill on topped set!");
         } else {
            List<Value> l = new LinkedList(out.toList());
            Iterator var5 = l.iterator();

            while(true) {
               while(var5.hasNext()) {
                  Value avail = (Value)var5.next();
                  if (avail instanceof FieldRef) {
                     if (this.st.unitCanWriteTo(u, avail)) {
                        out.remove(avail, out);
                     }
                  } else {
                     Iterator var7 = avail.getUseBoxes().iterator();

                     while(var7.hasNext()) {
                        ValueBox vb = (ValueBox)var7.next();
                        Value use = vb.getValue();
                        if (this.st.unitCanWriteTo(u, use)) {
                           out.remove(avail, out);
                        }
                     }
                  }
               }

               return;
            }
         }
      }
   }

   protected void merge(FlowSet<Value> inSet1, FlowSet<Value> inSet2, FlowSet<Value> outSet) {
      inSet1.intersection(inSet2, outSet);
   }

   protected void copy(FlowSet<Value> sourceSet, FlowSet<Value> destSet) {
      sourceSet.copy(destSet);
   }
}
