package soot.jimple.toolkits.thread.mhp;

import heros.util.SootThreadGroup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Kind;
import soot.PointsToAnalysis;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.spark.ondemand.DemandCSPointsTo;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.PAG;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.thread.AbstractRuntimeThread;
import soot.jimple.toolkits.thread.mhp.findobject.AllocNodesFinder;
import soot.jimple.toolkits.thread.mhp.findobject.MultiRunStatementsFinder;
import soot.jimple.toolkits.thread.mhp.pegcallgraph.PegCallGraph;
import soot.options.SparkOptions;
import soot.toolkits.graph.CompleteUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.FlowSet;

public class SynchObliviousMhpAnalysis implements MhpTester, Runnable {
   private static final Logger logger = LoggerFactory.getLogger(SynchObliviousMhpAnalysis.class);
   List<AbstractRuntimeThread> threadList = new ArrayList();
   boolean optionPrintDebug = false;
   boolean optionThreaded = false;
   Thread self = null;

   public SynchObliviousMhpAnalysis() {
      this.buildThreadList();
   }

   protected void buildThreadList() {
      if (this.optionThreaded) {
         if (this.self != null) {
            return;
         }

         this.self = new Thread(new SootThreadGroup(), this);
         this.self.start();
      } else {
         this.run();
      }

   }

   public void run() {
      SootMethod mainMethod = Scene.v().getMainClass().getMethodByName("main");
      PointsToAnalysis pta = Scene.v().getPointsToAnalysis();
      if (pta instanceof DemandCSPointsTo) {
         DemandCSPointsTo demandCSPointsTo = (DemandCSPointsTo)pta;
         pta = demandCSPointsTo.getPAG();
      }

      if (!(pta instanceof PAG)) {
         throw new RuntimeException("You must use Spark for points-to analysis when computing MHP information!");
      } else {
         PAG pag = (PAG)pta;
         SparkOptions so = pag.getOpts();
         if (so.rta()) {
            throw new RuntimeException("MHP cannot be calculated using RTA due to incomplete call graph");
         } else {
            CallGraph callGraph = Scene.v().getCallGraph();
            PegCallGraph pecg = new PegCallGraph(callGraph);
            AllocNodesFinder anf = new AllocNodesFinder(pecg, callGraph, (PAG)pta);
            Set<AllocNode> multiRunAllocNodes = anf.getMultiRunAllocNodes();
            Set<SootMethod> multiCalledMethods = anf.getMultiCalledMethods();
            StartJoinFinder sjf = new StartJoinFinder(callGraph, (PAG)pta);
            Map<Stmt, List<AllocNode>> startToAllocNodes = sjf.getStartToAllocNodes();
            Map<Stmt, List<SootMethod>> startToRunMethods = sjf.getStartToRunMethods();
            Map<Stmt, SootMethod> startToContainingMethod = sjf.getStartToContainingMethod();
            Map<Stmt, Stmt> startToJoin = sjf.getStartToJoin();
            List<AbstractRuntimeThread> runAtOnceCandidates = new ArrayList();
            Iterator threadIt = startToRunMethods.entrySet().iterator();

            AbstractRuntimeThread thread;
            Iterator edgeInIt;
            for(int var17 = 0; threadIt.hasNext(); ++var17) {
               Entry e = (Entry)threadIt.next();
               Stmt startStmt = (Stmt)e.getKey();
               List runMethods = (List)e.getValue();
               List threadAllocNodes = (List)startToAllocNodes.get(e.getKey());
               thread = new AbstractRuntimeThread();
               thread.setStartStmt(startStmt);
               edgeInIt = runMethods.iterator();

               while(edgeInIt.hasNext()) {
                  SootMethod method = (SootMethod)edgeInIt.next();
                  if (!thread.containsMethod(method)) {
                     thread.addMethod(method);
                     thread.addRunMethod(method);
                  }
               }

               SootMethod startStmtMethod;
               boolean mayBeRunMultipleTimes;
               int methodNum;
               for(methodNum = 0; methodNum < thread.methodCount(); ++methodNum) {
                  Iterator succMethodsIt = pecg.getSuccsOf(thread.getMethod(methodNum)).iterator();

                  while(succMethodsIt.hasNext()) {
                     startStmtMethod = (SootMethod)succMethodsIt.next();
                     mayBeRunMultipleTimes = true;
                     Iterator edgeInIt = callGraph.edgesInto(startStmtMethod);

                     while(edgeInIt.hasNext()) {
                        Edge edge = (Edge)edgeInIt.next();
                        if (edge.kind() != Kind.THREAD && edge.kind() != Kind.EXECUTOR && edge.kind() != Kind.ASYNCTASK && thread.containsMethod(edge.src())) {
                           mayBeRunMultipleTimes = false;
                        }
                     }

                     if (!mayBeRunMultipleTimes && !thread.containsMethod(startStmtMethod)) {
                        thread.addMethod(startStmtMethod);
                     }
                  }
               }

               this.threadList.add(thread);
               if (this.optionPrintDebug) {
                  System.out.println(thread.toString());
               }

               boolean mayStartMultipleThreadObjects = threadAllocNodes.size() > 1 || so.types_for_sites();
               if (!mayStartMultipleThreadObjects && multiRunAllocNodes.contains(threadAllocNodes.iterator().next())) {
                  mayStartMultipleThreadObjects = true;
               }

               if (mayStartMultipleThreadObjects) {
                  thread.setStartStmtHasMultipleReachingObjects();
               }

               startStmtMethod = (SootMethod)startToContainingMethod.get(startStmt);
               thread.setStartStmtMethod(startStmtMethod);
               mayBeRunMultipleTimes = multiCalledMethods.contains(startStmtMethod);
               if (!mayBeRunMultipleTimes) {
                  UnitGraph graph = new CompleteUnitGraph(startStmtMethod.getActiveBody());
                  MultiRunStatementsFinder finder = new MultiRunStatementsFinder(graph, startStmtMethod, multiCalledMethods, callGraph);
                  FlowSet multiRunStatements = finder.getMultiRunStatements();
                  if (multiRunStatements.contains(startStmt)) {
                     mayBeRunMultipleTimes = true;
                  }
               }

               if (mayBeRunMultipleTimes) {
                  thread.setStartStmtMayBeRunMultipleTimes();
               }

               if (mayBeRunMultipleTimes && startToJoin.containsKey(startStmt)) {
                  thread.setJoinStmt((Stmt)startToJoin.get(startStmt));
                  mayBeRunMultipleTimes = false;
                  methodNum = 0;
                  List<SootMethod> containingMethodCalls = new ArrayList();
                  containingMethodCalls.add(startStmtMethod);

                  while(true) {
                     if (methodNum >= containingMethodCalls.size()) {
                        if (!mayBeRunMultipleTimes) {
                           runAtOnceCandidates.add(thread);
                        }
                        break;
                     }

                     Iterator succMethodsIt = pecg.getSuccsOf(containingMethodCalls.get(methodNum)).iterator();

                     while(succMethodsIt.hasNext()) {
                        SootMethod method = (SootMethod)succMethodsIt.next();
                        if (method == startStmtMethod) {
                           mayBeRunMultipleTimes = true;
                           thread.setStartMethodIsReentrant();
                           thread.setRunsMany();
                           break;
                        }

                        if (!containingMethodCalls.contains(method)) {
                           containingMethodCalls.add(method);
                        }
                     }

                     ++methodNum;
                  }
               }

               if (this.optionPrintDebug) {
                  System.out.println("Start Stmt " + startStmt.toString() + " mayStartMultipleThreadObjects=" + mayStartMultipleThreadObjects + " mayBeRunMultipleTimes=" + mayBeRunMultipleTimes);
               }

               if (mayStartMultipleThreadObjects && mayBeRunMultipleTimes) {
                  this.threadList.add(thread);
                  thread.setRunsMany();
                  if (this.optionPrintDebug) {
                     System.out.println(thread.toString());
                  }
               } else {
                  thread.setRunsOnce();
               }
            }

            AbstractRuntimeThread mainThread = new AbstractRuntimeThread();
            this.threadList.add(mainThread);
            mainThread.setRunsOnce();
            mainThread.addMethod(mainMethod);
            mainThread.addRunMethod(mainMethod);
            mainThread.setIsMainThread();

            for(int methodNum = 0; methodNum < mainThread.methodCount(); ++methodNum) {
               Iterator succMethodsIt = pecg.getSuccsOf(mainThread.getMethod(methodNum)).iterator();

               while(succMethodsIt.hasNext()) {
                  SootMethod method = (SootMethod)succMethodsIt.next();
                  boolean ignoremethod = true;
                  edgeInIt = callGraph.edgesInto(method);

                  while(edgeInIt.hasNext()) {
                     if (((Edge)edgeInIt.next()).kind() != Kind.THREAD) {
                        ignoremethod = false;
                     }
                  }

                  if (!ignoremethod && !mainThread.containsMethod(method)) {
                     mainThread.addMethod(method);
                  }
               }
            }

            if (this.optionPrintDebug) {
               logger.debug("" + mainThread.toString());
            }

            boolean addedNew = true;

            while(addedNew) {
               addedNew = false;
               ListIterator it = runAtOnceCandidates.listIterator();

               while(it.hasNext()) {
                  thread = (AbstractRuntimeThread)it.next();
                  SootMethod someStartMethod = thread.getStartStmtMethod();
                  if (this.mayHappenInParallelInternal(someStartMethod, someStartMethod)) {
                     this.threadList.add(thread);
                     thread.setStartMethodMayHappenInParallel();
                     thread.setRunsMany();
                     it.remove();
                     if (this.optionPrintDebug) {
                        logger.debug("" + thread.toString());
                     }

                     addedNew = true;
                  }
               }
            }

            Iterator it = runAtOnceCandidates.iterator();

            while(it.hasNext()) {
               thread = (AbstractRuntimeThread)it.next();
               thread.setRunsOneAtATime();
            }

         }
      }
   }

   public boolean mayHappenInParallel(SootMethod m1, Unit u1, SootMethod m2, Unit u2) {
      if (this.optionThreaded) {
         if (this.self == null) {
            return true;
         }

         logger.debug("[mhp] waiting for analysis thread to finish");

         try {
            this.self.join();
         } catch (InterruptedException var6) {
            return true;
         }
      }

      return this.mayHappenInParallelInternal(m1, m2);
   }

   public boolean mayHappenInParallel(SootMethod m1, SootMethod m2) {
      if (this.optionThreaded) {
         if (this.self == null) {
            return true;
         }

         logger.debug("[mhp] waiting for thread to finish");

         try {
            this.self.join();
         } catch (InterruptedException var4) {
            return true;
         }
      }

      return this.mayHappenInParallelInternal(m1, m2);
   }

   private boolean mayHappenInParallelInternal(SootMethod m1, SootMethod m2) {
      if (this.threadList == null) {
         return true;
      } else {
         int size = this.threadList.size();

         for(int i = 0; i < size; ++i) {
            if (((AbstractRuntimeThread)this.threadList.get(i)).containsMethod(m1)) {
               for(int j = 0; j < size; ++j) {
                  if (((AbstractRuntimeThread)this.threadList.get(j)).containsMethod(m2) && i != j) {
                     return true;
                  }
               }
            }
         }

         return false;
      }
   }

   public void printMhpSummary() {
      if (this.optionThreaded) {
         if (this.self == null) {
            return;
         }

         logger.debug("[mhp] waiting for thread to finish");

         try {
            this.self.join();
         } catch (InterruptedException var4) {
            return;
         }
      }

      List<AbstractRuntimeThread> threads = new ArrayList();
      int size = this.threadList.size();
      logger.debug("[mhp]");

      for(int i = 0; i < size; ++i) {
         if (!threads.contains(this.threadList.get(i))) {
            logger.debug("[mhp] " + ((AbstractRuntimeThread)this.threadList.get(i)).toString().replaceAll("\n", "\n[mhp] ").replaceAll(">,", ">\n[mhp]  "));
            logger.debug("[mhp]");
         }

         threads.add(this.threadList.get(i));
      }

   }

   public List<SootClass> getThreadClassList() {
      if (this.optionThreaded) {
         if (this.self == null) {
            return null;
         }

         logger.debug("[mhp] waiting for thread to finish");

         try {
            this.self.join();
         } catch (InterruptedException var7) {
            return null;
         }
      }

      if (this.threadList == null) {
         return null;
      } else {
         List<SootClass> threadClasses = new ArrayList();
         int size = this.threadList.size();

         for(int i = 0; i < size; ++i) {
            AbstractRuntimeThread thread = (AbstractRuntimeThread)this.threadList.get(i);
            Iterator threadRunMethodIt = thread.getRunMethods().iterator();

            while(threadRunMethodIt.hasNext()) {
               SootClass threadClass = ((SootMethod)threadRunMethodIt.next()).getDeclaringClass();
               if (!threadClasses.contains(threadClass) && threadClass.isApplicationClass()) {
                  threadClasses.add(threadClass);
               }
            }
         }

         return threadClasses;
      }
   }

   public List<AbstractRuntimeThread> getThreads() {
      if (this.optionThreaded) {
         if (this.self == null) {
            return null;
         }

         logger.debug("[mhp] waiting for thread to finish");

         try {
            this.self.join();
         } catch (InterruptedException var4) {
            return null;
         }
      }

      if (this.threadList == null) {
         return null;
      } else {
         List<AbstractRuntimeThread> threads = new ArrayList();
         int size = this.threadList.size();

         for(int i = 0; i < size; ++i) {
            if (!threads.contains(this.threadList.get(i))) {
               threads.add(this.threadList.get(i));
            }
         }

         return threads;
      }
   }
}
