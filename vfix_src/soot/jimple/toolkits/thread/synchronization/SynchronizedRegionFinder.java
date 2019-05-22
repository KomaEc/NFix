package soot.jimple.toolkits.thread.synchronization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.G;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.AssignStmt;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.GotoStmt;
import soot.jimple.JimpleBody;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.jimple.internal.JNopStmt;
import soot.jimple.toolkits.pointer.FullObjectSet;
import soot.jimple.toolkits.pointer.RWSet;
import soot.jimple.toolkits.pointer.Union;
import soot.jimple.toolkits.pointer.UnionFactory;
import soot.jimple.toolkits.thread.ThreadLocalObjectsAnalysis;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.toolkits.scalar.LocalUses;
import soot.toolkits.scalar.Pair;
import soot.toolkits.scalar.UnitValueBoxPair;
import soot.util.Chain;

public class SynchronizedRegionFinder extends ForwardFlowAnalysis<Unit, FlowSet<SynchronizedRegionFlowPair>> {
   private static final Logger logger = LoggerFactory.getLogger(SynchronizedRegionFinder.class);
   FlowSet<SynchronizedRegionFlowPair> emptySet = new ArraySparseSet();
   Map unitToGenerateSet;
   Body body;
   Chain<Unit> units;
   SootMethod method;
   ExceptionalUnitGraph egraph;
   LocalUses slu;
   CriticalSectionAwareSideEffectAnalysis tasea;
   List<Object> prepUnits;
   CriticalSection methodTn;
   public boolean optionPrintDebug = false;
   public boolean optionOpenNesting = true;

   SynchronizedRegionFinder(UnitGraph graph, Body b, boolean optionPrintDebug, boolean optionOpenNesting, ThreadLocalObjectsAnalysis tlo) {
      super(graph);
      this.optionPrintDebug = optionPrintDebug;
      this.optionOpenNesting = optionOpenNesting;
      this.body = b;
      this.units = b.getUnits();
      this.method = this.body.getMethod();
      if (graph instanceof ExceptionalUnitGraph) {
         this.egraph = (ExceptionalUnitGraph)graph;
      } else {
         this.egraph = new ExceptionalUnitGraph(b);
      }

      this.slu = LocalUses.Factory.newLocalUses((UnitGraph)this.egraph);
      if (G.v().Union_factory == null) {
         G.v().Union_factory = new UnionFactory() {
            public Union newUnion() {
               return FullObjectSet.v();
            }
         };
      }

      this.tasea = new CriticalSectionAwareSideEffectAnalysis(Scene.v().getPointsToAnalysis(), Scene.v().getCallGraph(), (Collection)null, tlo);
      this.prepUnits = new ArrayList();
      this.methodTn = null;
      if (this.method.isSynchronized()) {
         this.methodTn = new CriticalSection(true, this.method, 1);
         this.methodTn.beginning = ((JimpleBody)this.body).getFirstNonIdentityStmt();
      }

      this.doAnalysis();
      if (this.method.isSynchronized() && this.methodTn != null) {
         Iterator tailIt = graph.getTails().iterator();

         while(tailIt.hasNext()) {
            Stmt tail = (Stmt)tailIt.next();
            this.methodTn.earlyEnds.add(new Pair(tail, (Object)null));
         }
      }

   }

   protected FlowSet<SynchronizedRegionFlowPair> newInitialFlow() {
      FlowSet<SynchronizedRegionFlowPair> ret = this.emptySet.clone();
      if (this.method.isSynchronized() && this.methodTn != null) {
         ret.add(new SynchronizedRegionFlowPair(this.methodTn, true));
      }

      return ret;
   }

   protected void flowThrough(FlowSet<SynchronizedRegionFlowPair> in, Unit unit, FlowSet<SynchronizedRegionFlowPair> out) {
      Stmt stmt = (Stmt)unit;
      this.copy(in, out);
      boolean addSelf;
      if (unit instanceof AssignStmt) {
         addSelf = true;
         Iterator<UnitValueBoxPair> uses = this.slu.getUsesOf(unit).iterator();
         if (!uses.hasNext()) {
            addSelf = false;
         }

         while(uses.hasNext()) {
            UnitValueBoxPair use = (UnitValueBoxPair)uses.next();
            Unit useStmt = use.getUnit();
            if (!(useStmt instanceof EnterMonitorStmt) && !(useStmt instanceof ExitMonitorStmt)) {
               addSelf = false;
               break;
            }
         }

         if (addSelf) {
            this.prepUnits.add(unit);
            if (this.optionPrintDebug) {
               logger.debug("prep: " + unit.toString());
            }

            return;
         }
      }

      addSelf = unit instanceof EnterMonitorStmt;
      int nestLevel = 0;
      Iterator outIt0 = out.iterator();

      while(outIt0.hasNext()) {
         SynchronizedRegionFlowPair srfp = (SynchronizedRegionFlowPair)outIt0.next();
         if (srfp.tn.nestLevel > nestLevel && srfp.inside) {
            nestLevel = srfp.tn.nestLevel;
         }
      }

      RWSet stmtRead = null;
      RWSet stmtWrite = null;
      Iterator outIt = out.iterator();
      boolean printed = false;

      while(outIt.hasNext()) {
         SynchronizedRegionFlowPair srfp = (SynchronizedRegionFlowPair)outIt.next();
         CriticalSection tn = srfp.tn;
         if (tn.entermonitor == stmt) {
            srfp.inside = true;
            addSelf = false;
         }

         if (srfp.inside && (tn.nestLevel == nestLevel || !this.optionOpenNesting)) {
            printed = true;
            if (!tn.units.contains(unit)) {
               tn.units.add(unit);
            }

            if (stmt.containsInvokeExpr()) {
               String InvokeSig = stmt.getInvokeExpr().getMethod().getSubSignature();
               if ((InvokeSig.equals("void notify()") || InvokeSig.equals("void notifyAll()")) && tn.nestLevel == nestLevel) {
                  if (!tn.notifys.contains(unit)) {
                     tn.notifys.add(unit);
                  }

                  if (this.optionPrintDebug) {
                     logger.debug("{x,x} ");
                  }
               } else if ((InvokeSig.equals("void wait()") || InvokeSig.equals("void wait(long)") || InvokeSig.equals("void wait(long,int)")) && tn.nestLevel == nestLevel) {
                  if (!tn.waits.contains(unit)) {
                     tn.waits.add(unit);
                  }

                  if (this.optionPrintDebug) {
                     logger.debug("{x,x} ");
                  }
               }

               if (!tn.invokes.contains(unit)) {
                  tn.invokes.add(unit);
                  if (this.optionPrintDebug) {
                     stmtRead = this.tasea.readSet(tn.method, stmt, tn, new HashSet());
                     stmtWrite = this.tasea.writeSet(tn.method, stmt, tn, new HashSet());
                     logger.debug("{");
                     if (stmtRead != null) {
                        logger.debug("" + ((stmtRead.getGlobals() != null ? stmtRead.getGlobals().size() : 0) + (stmtRead.getFields() != null ? stmtRead.getFields().size() : 0)));
                     } else {
                        logger.debug("0");
                     }

                     logger.debug(",");
                     if (stmtWrite != null) {
                        logger.debug("" + ((stmtWrite.getGlobals() != null ? stmtWrite.getGlobals().size() : 0) + (stmtWrite.getFields() != null ? stmtWrite.getFields().size() : 0)));
                     } else {
                        logger.debug("0");
                     }

                     logger.debug("} ");
                  }
               }
            } else if (unit instanceof ExitMonitorStmt && tn.nestLevel == nestLevel) {
               srfp.inside = false;
               Stmt nextUnit = stmt;

               do {
                  nextUnit = (Stmt)this.units.getSuccOf(nextUnit);
               } while(nextUnit instanceof JNopStmt);

               if (!(nextUnit instanceof ReturnStmt) && !(nextUnit instanceof ReturnVoidStmt) && !(nextUnit instanceof ExitMonitorStmt)) {
                  if (nextUnit instanceof GotoStmt) {
                     tn.end = new Pair(nextUnit, stmt);
                     tn.after = (Stmt)((GotoStmt)nextUnit).getTarget();
                  } else {
                     if (!(nextUnit instanceof ThrowStmt)) {
                        throw new RuntimeException("Unknown bytecode pattern: exitmonitor not followed by return, exitmonitor, goto, or throw");
                     }

                     tn.exceptionalEnd = new Pair(nextUnit, stmt);
                  }
               } else {
                  tn.earlyEnds.add(new Pair(nextUnit, stmt));
               }

               if (this.optionPrintDebug) {
                  logger.debug("[0,0] ");
               }
            } else {
               HashSet uses = new HashSet();
               stmtRead = this.tasea.readSet(this.method, stmt, tn, uses);
               stmtWrite = this.tasea.writeSet(this.method, stmt, tn, uses);
               tn.read.union(stmtRead);
               tn.write.union(stmtWrite);
               if (this.optionPrintDebug) {
                  logger.debug("[");
                  if (stmtRead != null) {
                     logger.debug("" + ((stmtRead.getGlobals() != null ? stmtRead.getGlobals().size() : 0) + (stmtRead.getFields() != null ? stmtRead.getFields().size() : 0)));
                  } else {
                     logger.debug("0");
                  }

                  logger.debug(",");
                  if (stmtWrite != null) {
                     logger.debug("" + ((stmtWrite.getGlobals() != null ? stmtWrite.getGlobals().size() : 0) + (stmtWrite.getFields() != null ? stmtWrite.getFields().size() : 0)));
                  } else {
                     logger.debug("0");
                  }

                  logger.debug("] ");
               }
            }
         }
      }

      if (this.optionPrintDebug) {
         if (!printed) {
            logger.debug("[0,0] ");
         }

         logger.debug("" + unit.toString());
         if (stmt.containsInvokeExpr() && stmt.getInvokeExpr().getMethod().getDeclaringClass().toString().startsWith("java.") && stmtRead != null && stmtWrite != null && stmtRead.size() < 25 && stmtWrite.size() < 25) {
            logger.debug("        Read/Write Set for LibInvoke:");
            logger.debug("Read Set:(" + stmtRead.size() + ")" + stmtRead.toString().replaceAll("\n", "\n        "));
            logger.debug("Write Set:(" + stmtWrite.size() + ")" + stmtWrite.toString().replaceAll("\n", "\n        "));
         }
      }

      if (addSelf) {
         CriticalSection newTn = new CriticalSection(false, this.method, nestLevel + 1);
         newTn.entermonitor = stmt;
         newTn.beginning = (Stmt)this.units.getSuccOf(stmt);
         if (stmt instanceof EnterMonitorStmt) {
            newTn.origLock = ((EnterMonitorStmt)stmt).getOp();
         }

         if (this.optionPrintDebug) {
            logger.debug("Transaction found in method: " + newTn.method.toString());
         }

         out.add(new SynchronizedRegionFlowPair(newTn, true));
         Iterator prepUnitsIt = this.prepUnits.iterator();

         while(prepUnitsIt.hasNext()) {
            Unit prepUnit = (Unit)prepUnitsIt.next();
            Iterator uses = this.slu.getUsesOf(prepUnit).iterator();

            while(uses.hasNext()) {
               UnitValueBoxPair use = (UnitValueBoxPair)uses.next();
               if (use.getUnit() == unit) {
                  newTn.prepStmt = (Stmt)prepUnit;
               }
            }
         }
      }

   }

   protected void merge(FlowSet<SynchronizedRegionFlowPair> inSet1, FlowSet<SynchronizedRegionFlowPair> inSet2, FlowSet<SynchronizedRegionFlowPair> outSet) {
      inSet1.union(inSet2, outSet);
   }

   protected void copy(FlowSet<SynchronizedRegionFlowPair> sourceSet, FlowSet<SynchronizedRegionFlowPair> destSet) {
      destSet.clear();
      Iterator it = sourceSet.iterator();

      while(it.hasNext()) {
         SynchronizedRegionFlowPair tfp = (SynchronizedRegionFlowPair)it.next();
         destSet.add(tfp.clone());
      }

   }
}
