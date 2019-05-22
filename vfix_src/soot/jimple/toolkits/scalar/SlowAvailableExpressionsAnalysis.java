package soot.jimple.toolkits.scalar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.EquivalentValue;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.Expr;
import soot.jimple.InvokeExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.Stmt;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArrayFlowUniverse;
import soot.toolkits.scalar.ArrayPackedSet;
import soot.toolkits.scalar.BoundedFlowSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.FlowUniverse;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.util.Chain;
import soot.util.HashChain;

public class SlowAvailableExpressionsAnalysis extends ForwardFlowAnalysis<Unit, FlowSet<Value>> {
   Map<Unit, BoundedFlowSet<Value>> unitToGenerateSet;
   Map<Unit, BoundedFlowSet<Value>> unitToPreserveSet;
   Map<Value, Stmt> rhsToContainingStmt;
   private final HashMap<Value, EquivalentValue> valueToEquivValue;
   FlowSet<Value> emptySet;

   public SlowAvailableExpressionsAnalysis(DirectedGraph<Unit> dg) {
      super(dg);
      UnitGraph g = (UnitGraph)dg;
      ArrayList<Value> exprs = new ArrayList();
      HashMap<EquivalentValue, Chain<EquivalentValue>> containingExprs = new HashMap();
      this.valueToEquivValue = new HashMap();
      this.rhsToContainingStmt = new HashMap();
      HashMap<EquivalentValue, Chain<Value>> equivValToSiblingList = new HashMap();
      Iterator var6 = g.getBody().getUnits().iterator();

      while(true) {
         Value v;
         EquivalentValue ev;
         Object sibList;
         do {
            do {
               Stmt s;
               do {
                  if (!var6.hasNext()) {
                     FlowUniverse<Value> exprUniv = new ArrayFlowUniverse(exprs.toArray(new Value[exprs.size()]));
                     this.emptySet = new ArrayPackedSet(exprUniv);
                     this.unitToPreserveSet = new HashMap(g.size() * 2 + 1, 0.7F);
                     Iterator var20 = g.iterator();

                     Unit s;
                     ArrayPackedSet genSet;
                     label113:
                     while(var20.hasNext()) {
                        s = (Unit)var20.next();
                        genSet = new ArrayPackedSet(exprUniv);
                        Iterator var23 = s.getDefBoxes().iterator();

                        while(true) {
                           Chain c;
                           do {
                              if (!var23.hasNext()) {
                                 genSet.complement(genSet);
                                 this.unitToPreserveSet.put(s, genSet);
                                 continue label113;
                              }

                              ValueBox box = (ValueBox)var23.next();
                              Value v = box.getValue();
                              EquivalentValue ev = (EquivalentValue)this.valueToEquivValue.get(v);
                              c = (Chain)containingExprs.get(ev);
                           } while(c == null);

                           Iterator var31 = c.iterator();

                           while(var31.hasNext()) {
                              EquivalentValue container = (EquivalentValue)var31.next();
                              Iterator var17 = ((Chain)equivValToSiblingList.get(container)).iterator();

                              while(var17.hasNext()) {
                                 Value sibVal = (Value)var17.next();
                                 genSet.add(sibVal, genSet);
                              }
                           }
                        }
                     }

                     this.unitToGenerateSet = new HashMap(g.size() * 2 + 1, 0.7F);
                     var20 = g.iterator();

                     while(var20.hasNext()) {
                        s = (Unit)var20.next();
                        genSet = new ArrayPackedSet(exprUniv);
                        if (s instanceof AssignStmt) {
                           AssignStmt as = (AssignStmt)s;
                           if (as.getRightOp() instanceof Expr) {
                              Value gen = as.getRightOp();
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

                        genSet.intersection((FlowSet)this.unitToPreserveSet.get(s), genSet);
                        this.unitToGenerateSet.put(s, genSet);
                     }

                     this.doAnalysis();
                     return;
                  }

                  Unit u = (Unit)var6.next();
                  s = (Stmt)u;
               } while(!(s instanceof AssignStmt));

               v = ((AssignStmt)s).getRightOp();
               this.rhsToContainingStmt.put(v, s);
               ev = (EquivalentValue)this.valueToEquivValue.get(v);
               if (ev == null) {
                  ev = new EquivalentValue(v);
                  this.valueToEquivValue.put(v, ev);
               }

               sibList = null;
               if (equivValToSiblingList.get(ev) == null) {
                  sibList = new HashChain();
                  equivValToSiblingList.put(ev, sibList);
               } else {
                  sibList = (Chain)equivValToSiblingList.get(ev);
               }

               if (!((Chain)sibList).contains(v)) {
                  ((Chain)sibList).add(v);
               }
            } while(!(v instanceof Expr));
         } while(exprs.contains(v));

         exprs.add(v);
         Iterator var12 = v.getUseBoxes().iterator();

         while(var12.hasNext()) {
            ValueBox vb = (ValueBox)var12.next();
            Value o = vb.getValue();
            EquivalentValue eo = (EquivalentValue)this.valueToEquivValue.get(o);
            if (eo == null) {
               eo = new EquivalentValue(o);
               this.valueToEquivValue.put(o, eo);
            }

            if (equivValToSiblingList.get(eo) == null) {
               sibList = new HashChain();
               equivValToSiblingList.put(eo, sibList);
            } else {
               sibList = (Chain)equivValToSiblingList.get(eo);
            }

            if (!((Chain)sibList).contains(o)) {
               ((Chain)sibList).add(o);
            }

            Chain<EquivalentValue> l = null;
            if (containingExprs.containsKey(eo)) {
               l = (Chain)containingExprs.get(eo);
            } else {
               l = new HashChain();
               containingExprs.put(eo, l);
            }

            if (!((Chain)l).contains(ev)) {
               ((Chain)l).add(ev);
            }
         }
      }
   }

   protected FlowSet<Value> newInitialFlow() {
      BoundedFlowSet<Value> out = (BoundedFlowSet)this.emptySet.clone();
      out.complement(out);
      return out;
   }

   protected FlowSet<Value> entryInitialFlow() {
      return this.emptySet.clone();
   }

   protected void flowThrough(FlowSet<Value> in, Unit unit, FlowSet<Value> out) {
      in.intersection((FlowSet)this.unitToPreserveSet.get(unit), out);
      out.union((FlowSet)this.unitToGenerateSet.get(unit), out);
   }

   protected void merge(FlowSet<Value> inSet1, FlowSet<Value> inSet2, FlowSet<Value> outSet) {
      inSet1.intersection(inSet2, outSet);
   }

   protected void copy(FlowSet<Value> sourceSet, FlowSet<Value> destSet) {
      sourceSet.copy(destSet);
   }
}
