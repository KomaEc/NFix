package soot.jimple.toolkits.thread.mhp.findobject;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.thread.mhp.TargetMethodsFinder;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;

public class MultiRunStatementsFinder extends ForwardFlowAnalysis<Unit, BitSet> {
   Set<Unit> multiRunStatements = new HashSet();
   protected Map<Object, Integer> nodeToIndex = new HashMap();
   protected int lastIndex = 0;

   public MultiRunStatementsFinder(UnitGraph g, SootMethod sm, Set<SootMethod> multiCalledMethods, CallGraph cg) {
      super(g);
      this.doAnalysis();
      this.findMultiCalledMethodsIntra(multiCalledMethods, cg);
   }

   private void findMultiCalledMethodsIntra(Set<SootMethod> multiCalledMethods, CallGraph callGraph) {
      Iterator it = this.multiRunStatements.iterator();

      while(true) {
         Object targetList;
         do {
            Stmt stmt;
            do {
               if (!it.hasNext()) {
                  return;
               }

               stmt = (Stmt)it.next();
            } while(!stmt.containsInvokeExpr());

            InvokeExpr invokeExpr = stmt.getInvokeExpr();
            targetList = new ArrayList();
            SootMethod method = invokeExpr.getMethod();
            if (invokeExpr instanceof StaticInvokeExpr) {
               ((List)targetList).add(method);
            } else if (invokeExpr instanceof InstanceInvokeExpr && method.isConcrete() && !method.getDeclaringClass().isLibraryClass()) {
               TargetMethodsFinder tmd = new TargetMethodsFinder();
               targetList = tmd.find(stmt, callGraph, true, true);
            }
         } while(targetList == null);

         Iterator iterator = ((List)targetList).iterator();

         while(iterator.hasNext()) {
            SootMethod obj = (SootMethod)iterator.next();
            if (!obj.isNative()) {
               multiCalledMethods.add(obj);
            }
         }
      }
   }

   protected void merge(BitSet in1, BitSet in2, BitSet out) {
      out.clear();
      out.or(in1);
      out.or(in2);
   }

   protected void flowThrough(BitSet in, Unit unit, BitSet out) {
      out.clear();
      out.or(in);
      if (!out.get(this.indexOf(unit))) {
         out.set(this.indexOf(unit));
      } else {
         this.multiRunStatements.add(unit);
      }

   }

   protected void copy(BitSet source, BitSet dest) {
      dest.clear();
      dest.or(source);
   }

   protected BitSet entryInitialFlow() {
      return new BitSet();
   }

   protected BitSet newInitialFlow() {
      return new BitSet();
   }

   public FlowSet getMultiRunStatements() {
      FlowSet res = new ArraySparseSet();
      Iterator var2 = this.multiRunStatements.iterator();

      while(var2.hasNext()) {
         Unit u = (Unit)var2.next();
         res.add(u);
      }

      return res;
   }

   protected int indexOf(Object o) {
      Integer index = (Integer)this.nodeToIndex.get(o);
      if (index == null) {
         index = this.lastIndex;
         this.nodeToIndex.put(o, index);
         ++this.lastIndex;
      }

      return index;
   }
}
