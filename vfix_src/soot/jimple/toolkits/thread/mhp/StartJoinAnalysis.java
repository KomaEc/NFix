package soot.jimple.toolkits.thread.mhp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Hierarchy;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Filter;
import soot.jimple.toolkits.callgraph.TransitiveTargets;
import soot.jimple.toolkits.pointer.LocalMustAliasAnalysis;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.MHGPostDominatorsFinder;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;

public class StartJoinAnalysis extends ForwardFlowAnalysis {
   private static final Logger logger = LoggerFactory.getLogger(StartJoinAnalysis.class);
   Set<Stmt> startStatements = new HashSet();
   Set<Stmt> joinStatements = new HashSet();
   Hierarchy hierarchy = Scene.v().getActiveHierarchy();
   Map<Stmt, List<SootMethod>> startToRunMethods = new HashMap();
   Map<Stmt, List<AllocNode>> startToAllocNodes = new HashMap();
   Map<Stmt, Stmt> startToJoin = new HashMap();

   public StartJoinAnalysis(UnitGraph g, SootMethod sm, CallGraph callGraph, PAG pag) {
      super(g);
      this.doFlowInsensitiveSingleIterationAnalysis();
      if (!this.startStatements.isEmpty()) {
         MHGPostDominatorsFinder pd = new MHGPostDominatorsFinder(new BriefUnitGraph(sm.getActiveBody()));
         LocalMustAliasAnalysis lma = new LocalMustAliasAnalysis(g);
         TransitiveTargets runMethodTargets = new TransitiveTargets(callGraph, new Filter(new RunMethodsPred()));
         Iterator startIt = this.startStatements.iterator();

         while(true) {
            Stmt start;
            ArrayList runMethodsList;
            ArrayList allocNodesList;
            Value startObject;
            List mayAlias;
            do {
               if (!startIt.hasNext()) {
                  return;
               }

               start = (Stmt)startIt.next();
               runMethodsList = new ArrayList();
               allocNodesList = new ArrayList();
               startObject = ((InstanceInvokeExpr)start.getInvokeExpr()).getBase();
               PointsToSetInternal pts = (PointsToSetInternal)pag.reachingObjects((Local)startObject);
               mayAlias = this.getMayAliasList(pts);
            } while(mayAlias.size() < 1);

            Iterator mayRunIt = runMethodTargets.iterator((Unit)start);

            while(mayRunIt.hasNext()) {
               SootMethod runMethod = (SootMethod)mayRunIt.next();
               if (runMethod.getSubSignature().equals("void run()")) {
                  runMethodsList.add(runMethod);
               }
            }

            Iterator joinIt;
            if (runMethodsList.isEmpty() && ((RefType)startObject.getType()).getSootClass().isApplicationClass()) {
               List<SootClass> threadClasses = this.hierarchy.getSubclassesOfIncluding(((RefType)startObject.getType()).getSootClass());
               joinIt = threadClasses.iterator();

               while(joinIt.hasNext()) {
                  SootClass currentClass = (SootClass)joinIt.next();
                  SootMethod currentMethod = currentClass.getMethodUnsafe("void run()");
                  if (currentMethod != null) {
                     runMethodsList.add(currentMethod);
                  }
               }
            }

            Iterator mayAliasIt = mayAlias.iterator();

            while(mayAliasIt.hasNext()) {
               AllocNode allocNode = (AllocNode)mayAliasIt.next();
               allocNodesList.add(allocNode);
               if (runMethodsList.isEmpty()) {
                  throw new RuntimeException("Can't find run method for: " + startObject);
               }
            }

            this.startToRunMethods.put(start, runMethodsList);
            this.startToAllocNodes.put(start, allocNodesList);
            joinIt = this.joinStatements.iterator();

            while(joinIt.hasNext()) {
               Stmt join = (Stmt)joinIt.next();
               Value joinObject = ((InstanceInvokeExpr)join.getInvokeExpr()).getBase();
               List barriers = new ArrayList();
               barriers.addAll(g.getSuccsOf((Unit)join));
               if (lma.mustAlias((Local)startObject, start, (Local)joinObject, join) && pd.getDominators(start).contains(join)) {
                  this.startToJoin.put(start, join);
               }
            }
         }
      }
   }

   private List<AllocNode> getMayAliasList(PointsToSetInternal pts) {
      List<AllocNode> list = new ArrayList();
      final HashSet<AllocNode> ret = new HashSet();
      pts.forall(new P2SetVisitor() {
         public void visit(Node n) {
            ret.add((AllocNode)n);
         }
      });
      Iterator it = ret.iterator();

      while(it.hasNext()) {
         list.add(it.next());
      }

      return list;
   }

   public Set<Stmt> getStartStatements() {
      return this.startStatements;
   }

   public Set<Stmt> getJoinStatements() {
      return this.joinStatements;
   }

   public Map<Stmt, List<SootMethod>> getStartToRunMethods() {
      return this.startToRunMethods;
   }

   public Map<Stmt, List<AllocNode>> getStartToAllocNodes() {
      return this.startToAllocNodes;
   }

   public Map<Stmt, Stmt> getStartToJoin() {
      return this.startToJoin;
   }

   public void doFlowInsensitiveSingleIterationAnalysis() {
      FlowSet fs = (FlowSet)this.newInitialFlow();
      Iterator stmtIt = this.graph.iterator();

      while(stmtIt.hasNext()) {
         Stmt s = (Stmt)stmtIt.next();
         this.flowThrough(fs, s, fs);
      }

   }

   protected void merge(Object in1, Object in2, Object out) {
      FlowSet inSet1 = (FlowSet)in1;
      FlowSet inSet2 = (FlowSet)in2;
      FlowSet outSet = (FlowSet)out;
      inSet1.intersection(inSet2, outSet);
   }

   protected void flowThrough(Object inValue, Object unit, Object outValue) {
      Stmt stmt = (Stmt)unit;
      if (stmt.containsInvokeExpr()) {
         InvokeExpr ie = stmt.getInvokeExpr();
         if (ie instanceof InstanceInvokeExpr) {
            InstanceInvokeExpr iie = (InstanceInvokeExpr)ie;
            SootMethod invokeMethod = ie.getMethod();
            RefType baseType;
            List superClasses;
            Iterator it;
            if (invokeMethod.getName().equals("start")) {
               baseType = (RefType)iie.getBase().getType();
               if (!baseType.getSootClass().isInterface()) {
                  superClasses = this.hierarchy.getSuperclassesOfIncluding(baseType.getSootClass());
                  it = superClasses.iterator();

                  while(it.hasNext()) {
                     if (((SootClass)it.next()).getName().equals("java.lang.Thread") && !this.startStatements.contains(stmt)) {
                        this.startStatements.add(stmt);
                     }
                  }
               }
            }

            if (invokeMethod.getName().equals("join")) {
               baseType = (RefType)iie.getBase().getType();
               if (!baseType.getSootClass().isInterface()) {
                  superClasses = this.hierarchy.getSuperclassesOfIncluding(baseType.getSootClass());
                  it = superClasses.iterator();

                  while(it.hasNext()) {
                     if (((SootClass)it.next()).getName().equals("java.lang.Thread") && !this.joinStatements.contains(stmt)) {
                        this.joinStatements.add(stmt);
                     }
                  }
               }
            }
         }
      }

   }

   protected void copy(Object source, Object dest) {
      FlowSet sourceSet = (FlowSet)source;
      FlowSet destSet = (FlowSet)dest;
      sourceSet.copy(destSet);
   }

   protected Object entryInitialFlow() {
      return new ArraySparseSet();
   }

   protected Object newInitialFlow() {
      return new ArraySparseSet();
   }
}
