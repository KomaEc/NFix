package soot.jimple.toolkits.thread.synchronization;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.G;
import soot.Scene;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.toolkits.pointer.FullObjectSet;
import soot.jimple.toolkits.pointer.RWSet;
import soot.jimple.toolkits.pointer.SideEffectAnalysis;
import soot.jimple.toolkits.pointer.Union;
import soot.jimple.toolkits.pointer.UnionFactory;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;

public class StrayRWFinder extends BackwardFlowAnalysis {
   FlowSet emptySet = new ArraySparseSet();
   Map unitToGenerateSet;
   Body body;
   SideEffectAnalysis sea;
   List tns;

   StrayRWFinder(UnitGraph graph, Body b, List tns) {
      super(graph);
      this.body = b;
      this.tns = tns;
      if (G.v().Union_factory == null) {
         G.v().Union_factory = new UnionFactory() {
            public Union newUnion() {
               return FullObjectSet.v();
            }
         };
      }

      this.sea = Scene.v().getSideEffectAnalysis();
      this.sea.findNTRWSets(this.body.getMethod());
      this.doAnalysis();
   }

   protected Object newInitialFlow() {
      return this.emptySet.clone();
   }

   protected Object entryInitialFlow() {
      return this.emptySet.clone();
   }

   protected void flowThrough(Object inValue, Object unit, Object outValue) {
      FlowSet in = (FlowSet)inValue;
      FlowSet out = (FlowSet)outValue;
      RWSet stmtRead = this.sea.readSet(this.body.getMethod(), (Stmt)unit);
      RWSet stmtWrite = this.sea.writeSet(this.body.getMethod(), (Stmt)unit);
      Boolean addSelf = Boolean.FALSE;
      Iterator tnIt = this.tns.iterator();

      while(true) {
         CriticalSection tn;
         do {
            if (!tnIt.hasNext()) {
               in.copy(out);
               if (addSelf) {
                  tn = new CriticalSection(false, this.body.getMethod(), 0);
                  tn.entermonitor = (Stmt)unit;
                  tn.units.add((Unit)unit);
                  tn.read.union(stmtRead);
                  tn.write.union(stmtWrite);
                  out.add(tn);
               }

               return;
            }

            tn = (CriticalSection)tnIt.next();
         } while(!stmtRead.hasNonEmptyIntersection(tn.write) && !stmtWrite.hasNonEmptyIntersection(tn.read) && !stmtWrite.hasNonEmptyIntersection(tn.write));

         addSelf = Boolean.TRUE;
      }
   }

   protected void merge(Object in1, Object in2, Object out) {
      FlowSet inSet1 = ((FlowSet)in1).clone();
      FlowSet inSet2 = ((FlowSet)in2).clone();
      FlowSet outSet = (FlowSet)out;
      inSet1.union(inSet2, outSet);
   }

   protected void copy(Object source, Object dest) {
      FlowSet sourceSet = (FlowSet)source;
      FlowSet destSet = (FlowSet)dest;
      sourceSet.copy(destSet);
   }
}
