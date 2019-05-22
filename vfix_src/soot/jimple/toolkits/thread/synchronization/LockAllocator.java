package soot.jimple.toolkits.thread.synchronization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.EquivalentValue;
import soot.G;
import soot.Local;
import soot.PhaseOptions;
import soot.PointsToAnalysis;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.Ref;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.sets.HashPointsToSet;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.jimple.toolkits.infoflow.ClassInfoFlowAnalysis;
import soot.jimple.toolkits.infoflow.FakeJimpleLocal;
import soot.jimple.toolkits.infoflow.SmartMethodInfoFlowAnalysis;
import soot.jimple.toolkits.pointer.RWSet;
import soot.jimple.toolkits.thread.ThreadLocalObjectsAnalysis;
import soot.jimple.toolkits.thread.mhp.MhpTester;
import soot.jimple.toolkits.thread.mhp.SynchObliviousMhpAnalysis;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.HashMutableEdgeLabelledDirectedGraph;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.LocalDefs;
import soot.util.Chain;

public class LockAllocator extends SceneTransformer {
   private static final Logger logger = LoggerFactory.getLogger(LockAllocator.class);
   List<CriticalSection> criticalSections = null;
   CriticalSectionInterferenceGraph interferenceGraph = null;
   DirectedGraph deadlockGraph = null;
   boolean optionOneGlobalLock = false;
   boolean optionStaticLocks = false;
   boolean optionUseLocksets = false;
   boolean optionLeaveOriginalLocks = false;
   boolean optionIncludeEmptyPossibleEdges = false;
   boolean optionAvoidDeadlock = true;
   boolean optionOpenNesting = true;
   boolean optionDoMHP = false;
   boolean optionDoTLO = false;
   boolean optionOnFlyTLO = false;
   boolean optionPrintMhpSummary = true;
   boolean optionPrintGraph = false;
   boolean optionPrintTable = false;
   boolean optionPrintDebug = false;

   public LockAllocator(Singletons.Global g) {
   }

   public static LockAllocator v() {
      return G.v().soot_jimple_toolkits_thread_synchronization_LockAllocator();
   }

   protected void internalTransform(String phaseName, Map<String, String> options) {
      String lockingScheme = PhaseOptions.getString(options, "locking-scheme");
      if (lockingScheme.equals("fine-grained")) {
         this.optionOneGlobalLock = false;
         this.optionStaticLocks = false;
         this.optionUseLocksets = true;
         this.optionLeaveOriginalLocks = false;
      }

      if (lockingScheme.equals("medium-grained")) {
         this.optionOneGlobalLock = false;
         this.optionStaticLocks = false;
         this.optionUseLocksets = false;
         this.optionLeaveOriginalLocks = false;
      }

      if (lockingScheme.equals("coarse-grained")) {
         this.optionOneGlobalLock = false;
         this.optionStaticLocks = true;
         this.optionUseLocksets = false;
         this.optionLeaveOriginalLocks = false;
      }

      if (lockingScheme.equals("single-static")) {
         this.optionOneGlobalLock = true;
         this.optionStaticLocks = true;
         this.optionUseLocksets = false;
         this.optionLeaveOriginalLocks = false;
      }

      if (lockingScheme.equals("leave-original")) {
         this.optionOneGlobalLock = false;
         this.optionStaticLocks = false;
         this.optionUseLocksets = false;
         this.optionLeaveOriginalLocks = true;
      }

      this.optionAvoidDeadlock = PhaseOptions.getBoolean(options, "avoid-deadlock");
      this.optionOpenNesting = PhaseOptions.getBoolean(options, "open-nesting");
      this.optionDoMHP = PhaseOptions.getBoolean(options, "do-mhp");
      this.optionDoTLO = PhaseOptions.getBoolean(options, "do-tlo");
      this.optionPrintGraph = PhaseOptions.getBoolean(options, "print-graph");
      this.optionPrintTable = PhaseOptions.getBoolean(options, "print-table");
      this.optionPrintDebug = PhaseOptions.getBoolean(options, "print-debug");
      MhpTester mhp = null;
      if (this.optionDoMHP && Scene.v().getPointsToAnalysis() instanceof PAG) {
         logger.debug("[wjtp.tn] *** Build May-Happen-in-Parallel Info *** " + new Date());
         mhp = new SynchObliviousMhpAnalysis();
         if (this.optionPrintMhpSummary) {
            mhp.printMhpSummary();
         }
      }

      ThreadLocalObjectsAnalysis tlo = null;
      if (this.optionDoTLO) {
         logger.debug("[wjtp.tn] *** Find Thread-Local Objects *** " + new Date());
         if (mhp != null) {
            tlo = new ThreadLocalObjectsAnalysis(mhp);
         } else {
            tlo = new ThreadLocalObjectsAnalysis(new SynchObliviousMhpAnalysis());
         }

         if (!this.optionOnFlyTLO) {
            tlo.precompute();
            logger.debug("[wjtp.tn] TLO totals (#analyzed/#encountered): " + SmartMethodInfoFlowAnalysis.counter + "/" + ClassInfoFlowAnalysis.methodCount);
         } else {
            logger.debug("[wjtp.tn] TLO so far (#analyzed/#encountered): " + SmartMethodInfoFlowAnalysis.counter + "/" + ClassInfoFlowAnalysis.methodCount);
         }
      }

      Date start = new Date();
      logger.debug("[wjtp.tn] *** Find and Name Transactions *** " + start);
      Map<SootMethod, FlowSet> methodToFlowSet = new HashMap();
      Map<SootMethod, ExceptionalUnitGraph> methodToExcUnitGraph = new HashMap();
      Iterator runAnalysisClassesIt = Scene.v().getApplicationClasses().iterator();

      Iterator tasea;
      while(runAnalysisClassesIt.hasNext()) {
         SootClass appClass = (SootClass)runAnalysisClassesIt.next();
         tasea = appClass.getMethods().iterator();

         while(tasea.hasNext()) {
            SootMethod method = (SootMethod)tasea.next();
            if (method.isConcrete()) {
               Body b = method.retrieveActiveBody();
               ExceptionalUnitGraph eug = new ExceptionalUnitGraph(b);
               methodToExcUnitGraph.put(method, eug);
               SynchronizedRegionFinder ta = new SynchronizedRegionFinder(eug, b, this.optionPrintDebug, this.optionOpenNesting, tlo);
               Chain<Unit> units = b.getUnits();
               Unit lastUnit = (Unit)units.getLast();
               FlowSet fs = (FlowSet)ta.getFlowBefore(lastUnit);
               methodToFlowSet.put(method, fs);
            }
         }
      }

      this.criticalSections = new Vector();
      Iterator var26 = methodToFlowSet.values().iterator();

      while(var26.hasNext()) {
         FlowSet fs = (FlowSet)var26.next();
         List fList = fs.toList();

         for(int i = 0; i < fList.size(); ++i) {
            this.criticalSections.add(((SynchronizedRegionFlowPair)fList.get(i)).tn);
         }
      }

      this.assignNamesToTransactions(this.criticalSections);
      if (this.optionOnFlyTLO) {
         logger.debug("[wjtp.tn] TLO so far (#analyzed/#encountered): " + SmartMethodInfoFlowAnalysis.counter + "/" + ClassInfoFlowAnalysis.methodCount);
      }

      logger.debug("[wjtp.tn] *** Find Transitive Read/Write Sets *** " + new Date());
      PointsToAnalysis pta = Scene.v().getPointsToAnalysis();
      tasea = null;
      CriticalSectionAwareSideEffectAnalysis tasea = new CriticalSectionAwareSideEffectAnalysis(pta, Scene.v().getCallGraph(), this.optionOpenNesting ? this.criticalSections : null, tlo);
      Iterator tnIt = this.criticalSections.iterator();

      while(tnIt.hasNext()) {
         CriticalSection tn = (CriticalSection)tnIt.next();
         Iterator var35 = tn.invokes.iterator();

         while(var35.hasNext()) {
            Unit unit = (Unit)var35.next();
            Stmt stmt = (Stmt)unit;
            HashSet uses = new HashSet();
            RWSet stmtRead = tasea.readSet(tn.method, stmt, tn, uses);
            if (stmtRead != null) {
               tn.read.union(stmtRead);
            }

            RWSet stmtWrite = tasea.writeSet(tn.method, stmt, tn, uses);
            if (stmtWrite != null) {
               tn.write.union(stmtWrite);
            }
         }
      }

      long longTime = ((new Date()).getTime() - start.getTime()) / 100L;
      float time = (float)longTime / 10.0F;
      if (this.optionOnFlyTLO) {
         logger.debug("[wjtp.tn] TLO totals (#analyzed/#encountered): " + SmartMethodInfoFlowAnalysis.counter + "/" + ClassInfoFlowAnalysis.methodCount);
         logger.debug("[wjtp.tn] Time for stages utilizing on-fly TLO: " + time + "s");
      }

      logger.debug("[wjtp.tn] *** Calculate Locking Groups *** " + new Date());
      CriticalSectionInterferenceGraph ig = new CriticalSectionInterferenceGraph(this.criticalSections, mhp, this.optionOneGlobalLock, this.optionLeaveOriginalLocks, this.optionIncludeEmptyPossibleEdges);
      this.interferenceGraph = ig;
      logger.debug("[wjtp.tn] *** Detect the Possibility of Deadlock *** " + new Date());
      DeadlockDetector dd = new DeadlockDetector(this.optionPrintDebug, this.optionAvoidDeadlock, true, this.criticalSections);
      if (!this.optionUseLocksets) {
         this.deadlockGraph = dd.detectComponentBasedDeadlock();
      }

      logger.debug("[wjtp.tn] *** Calculate Locking Objects *** " + new Date());
      Iterator var20;
      if (!this.optionStaticLocks) {
         Iterator var43 = this.criticalSections.iterator();

         label202:
         while(true) {
            CriticalSection tn;
            do {
               if (!var43.hasNext()) {
                  break label202;
               }

               tn = (CriticalSection)var43.next();
            } while(tn.setNumber <= 0);

            var20 = tn.edges.iterator();

            while(var20.hasNext()) {
               CriticalSectionDataDependency tdd = (CriticalSectionDataDependency)var20.next();
               tn.group.rwSet.union(tdd.rw);
            }
         }
      }

      Map<Value, Integer> lockToLockNum = null;
      List<PointsToSetInternal> lockPTSets = null;
      if (this.optionLeaveOriginalLocks) {
         this.analyzeExistingLocks(this.criticalSections, ig);
      } else if (this.optionStaticLocks) {
         this.setFlagsForStaticAllocations(ig);
      } else {
         this.setFlagsForDynamicAllocations(ig);
         lockPTSets = new ArrayList();
         lockToLockNum = new HashMap();
         this.findLockableReferences(this.criticalSections, pta, tasea, lockToLockNum, lockPTSets);
         if (this.optionUseLocksets) {
            var20 = this.criticalSections.iterator();

            while(var20.hasNext()) {
               CriticalSection tn = (CriticalSection)var20.next();
               if (tn.group != null) {
                  logger.debug("[wjtp.tn] " + tn.name + " lockset: " + locksetToLockNumString(tn.lockset, lockToLockNum) + (tn.group.useLocksets ? "" : " (placeholder)"));
               }
            }
         }
      }

      if (this.optionUseLocksets) {
         logger.debug("[wjtp.tn] *** Detect " + (this.optionAvoidDeadlock ? "and Correct " : "") + "the Possibility of Deadlock for Locksets *** " + new Date());
         this.deadlockGraph = dd.detectLocksetDeadlock(lockToLockNum, lockPTSets);
         if (this.optionPrintDebug) {
            ((HashMutableEdgeLabelledDirectedGraph)this.deadlockGraph).printGraph();
         }

         logger.debug("[wjtp.tn] *** Reorder Locksets to Avoid Deadlock *** " + new Date());
         dd.reorderLocksets(lockToLockNum, (HashMutableEdgeLabelledDirectedGraph)this.deadlockGraph);
      }

      logger.debug("[wjtp.tn] *** Print Output and Transform Program *** " + new Date());
      if (this.optionPrintGraph) {
         this.printGraph(this.criticalSections, ig, lockToLockNum);
      }

      if (this.optionPrintTable) {
         this.printTable(this.criticalSections, mhp);
         this.printGroups(this.criticalSections, ig);
      }

      if (!this.optionLeaveOriginalLocks) {
         boolean[] insertedGlobalLock = new boolean[ig.groupCount()];
         insertedGlobalLock[0] = false;

         for(int i = 1; i < ig.groupCount(); ++i) {
            CriticalSectionGroup tnGroup = (CriticalSectionGroup)ig.groups().get(i);
            insertedGlobalLock[i] = !this.optionOneGlobalLock && (tnGroup.useDynamicLock || tnGroup.useLocksets);
         }

         Iterator var50 = Scene.v().getApplicationClasses().iterator();

         while(var50.hasNext()) {
            SootClass appClass = (SootClass)var50.next();
            Iterator var23 = appClass.getMethods().iterator();

            while(var23.hasNext()) {
               SootMethod method = (SootMethod)var23.next();
               if (method.isConcrete()) {
                  FlowSet fs = (FlowSet)methodToFlowSet.get(method);
                  if (fs != null) {
                     LockAllocationBodyTransformer.v().internalTransform(method.getActiveBody(), fs, ig.groups(), insertedGlobalLock);
                  }
               }
            }
         }
      }

   }

   protected void findLockableReferences(List<CriticalSection> AllTransactions, PointsToAnalysis pta, CriticalSectionAwareSideEffectAnalysis tasea, Map<Value, Integer> lockToLockNum, List<PointsToSetInternal> lockPTSets) {
      Iterator tnIt9 = AllTransactions.iterator();

      while(true) {
         while(true) {
            label95:
            while(true) {
               CriticalSection tn;
               int group;
               do {
                  do {
                     if (!tnIt9.hasNext()) {
                        if (this.optionUseLocksets) {
                           for(int i = 0; i < lockPTSets.size(); ++i) {
                              PointsToSetInternal pts = (PointsToSetInternal)lockPTSets.get(i);
                              if (pts.size() == 1) {
                              }
                           }
                        }

                        return;
                     }

                     tn = (CriticalSection)tnIt9.next();
                     group = tn.setNumber - 1;
                  } while(group < 0);
               } while(!tn.group.useDynamicLock && !tn.group.useLocksets);

               logger.debug("[wjtp.tn] * " + tn.name + " *");
               LockableReferenceAnalysis la = new LockableReferenceAnalysis(new BriefUnitGraph(tn.method.retrieveActiveBody()));
               tn.lockset = la.getLocksetOf(tasea, tn.group.rwSet, tn);
               if (this.optionUseLocksets) {
                  EquivalentValue lockEqVal;
                  if (tn.lockset != null && tn.lockset.size() > 0) {
                     Iterator var19 = tn.lockset.iterator();

                     while(true) {
                        while(true) {
                           if (!var19.hasNext()) {
                              continue label95;
                           }

                           lockEqVal = (EquivalentValue)var19.next();
                           Value lock = lockEqVal.getValue();
                           PointsToSetInternal lockPT;
                           if (lock instanceof Local) {
                              lockPT = (PointsToSetInternal)pta.reachingObjects((Local)lock);
                           } else if (lock instanceof StaticFieldRef) {
                              lockPT = null;
                           } else if (lock instanceof InstanceFieldRef) {
                              Local base = (Local)((InstanceFieldRef)lock).getBase();
                              if (base instanceof FakeJimpleLocal) {
                                 lockPT = (PointsToSetInternal)pta.reachingObjects(((FakeJimpleLocal)base).getRealLocal(), ((FieldRef)lock).getField());
                              } else {
                                 lockPT = (PointsToSetInternal)pta.reachingObjects(base, ((FieldRef)lock).getField());
                              }
                           } else if (lock instanceof NewStaticLock) {
                              lockPT = null;
                           } else {
                              lockPT = null;
                           }

                           HashPointsToSet otherLockPT;
                           if (lockPT != null) {
                              boolean foundLock = false;

                              for(int i = 0; i < lockPTSets.size(); ++i) {
                                 PointsToSetInternal otherLockPT = (PointsToSetInternal)lockPTSets.get(i);
                                 if (lockPT.hasNonEmptyIntersection(otherLockPT)) {
                                    logger.debug("[wjtp.tn] Lock: num " + i + " type " + lock.getType() + " obj " + lock);
                                    lockToLockNum.put(lock, new Integer(i));
                                    otherLockPT.addAll(lockPT, (PointsToSetInternal)null);
                                    foundLock = true;
                                    break;
                                 }
                              }

                              if (!foundLock) {
                                 logger.debug("[wjtp.tn] Lock: num " + lockPTSets.size() + " type " + lock.getType() + " obj " + lock);
                                 lockToLockNum.put(lock, new Integer(lockPTSets.size()));
                                 otherLockPT = new HashPointsToSet(lockPT.getType(), (PAG)pta);
                                 lockPTSets.add(otherLockPT);
                                 otherLockPT.addAll(lockPT, (PointsToSetInternal)null);
                              }
                           } else {
                              Integer lockNum;
                              if (lockToLockNum.get(lockEqVal) != null) {
                                 lockNum = (Integer)lockToLockNum.get(lockEqVal);
                                 logger.debug("[wjtp.tn] Lock: num " + lockNum + " type " + lock.getType() + " obj " + lock);
                                 lockToLockNum.put(lock, lockNum);
                              } else {
                                 lockNum = new Integer(-lockPTSets.size());
                                 logger.debug("[wjtp.tn] Lock: num " + lockNum + " type " + lock.getType() + " obj " + lock);
                                 lockToLockNum.put(lockEqVal, lockNum);
                                 lockToLockNum.put(lock, lockNum);
                                 otherLockPT = new HashPointsToSet(lock.getType(), (PAG)pta);
                                 lockPTSets.add(otherLockPT);
                              }
                           }
                        }
                     }
                  } else {
                     tn.group.useLocksets = false;
                     Value newStaticLock = new NewStaticLock(tn.method.getDeclaringClass());
                     lockEqVal = new EquivalentValue(newStaticLock);
                     Iterator var12 = tn.group.iterator();

                     while(var12.hasNext()) {
                        CriticalSection groupTn = (CriticalSection)var12.next();
                        groupTn.lockset = new ArrayList();
                        groupTn.lockset.add(lockEqVal);
                     }

                     Integer lockNum = new Integer(-lockPTSets.size());
                     logger.debug("[wjtp.tn] Lock: num " + lockNum + " type " + newStaticLock.getType() + " obj " + newStaticLock);
                     lockToLockNum.put(lockEqVal, lockNum);
                     lockToLockNum.put(newStaticLock, lockNum);
                     PointsToSetInternal dummyLockPT = new HashPointsToSet(newStaticLock.getType(), (PAG)pta);
                     lockPTSets.add(dummyLockPT);
                  }
               } else if (tn.lockset != null && tn.lockset.size() == 1) {
                  tn.lockObject = (Value)tn.lockset.get(0);
                  if (tn.group.lockObject == null || tn.lockObject instanceof Ref) {
                     tn.group.lockObject = tn.lockObject;
                  }
               } else {
                  tn.lockObject = null;
                  tn.group.useDynamicLock = false;
                  tn.group.lockObject = null;
               }
            }
         }
      }
   }

   public void setFlagsForDynamicAllocations(CriticalSectionInterferenceGraph ig) {
      for(int group = 0; group < ig.groupCount() - 1; ++group) {
         CriticalSectionGroup tnGroup = (CriticalSectionGroup)ig.groups().get(group + 1);
         if (this.optionUseLocksets) {
            tnGroup.useLocksets = true;
         } else {
            tnGroup.isDynamicLock = tnGroup.rwSet.getGlobals().size() == 0;
            tnGroup.useDynamicLock = true;
            tnGroup.lockObject = null;
         }

         if (tnGroup.rwSet.size() <= 0) {
            if (this.optionUseLocksets) {
               tnGroup.useLocksets = false;
            } else {
               tnGroup.isDynamicLock = false;
               tnGroup.useDynamicLock = false;
            }
         }
      }

   }

   public void setFlagsForStaticAllocations(CriticalSectionInterferenceGraph ig) {
      for(int group = 0; group < ig.groupCount() - 1; ++group) {
         CriticalSectionGroup tnGroup = (CriticalSectionGroup)ig.groups().get(group + 1);
         tnGroup.isDynamicLock = false;
         tnGroup.useDynamicLock = false;
         tnGroup.lockObject = null;
      }

   }

   private void analyzeExistingLocks(List<CriticalSection> AllTransactions, CriticalSectionInterferenceGraph ig) {
      this.setFlagsForStaticAllocations(ig);
      Iterator tnAIt = AllTransactions.iterator();

      while(true) {
         CriticalSection tn;
         List rDefs;
         do {
            LocalDefs ld;
            do {
               do {
                  do {
                     if (!tnAIt.hasNext()) {
                        return;
                     }

                     tn = (CriticalSection)tnAIt.next();
                  } while(tn.setNumber <= 0);

                  ld = LocalDefs.Factory.newLocalDefs(tn.method.retrieveActiveBody());
               } while(tn.origLock == null);
            } while(!(tn.origLock instanceof Local));

            rDefs = ld.getDefsOfAt((Local)tn.origLock, tn.entermonitor);
         } while(rDefs == null);

         Iterator var7 = rDefs.iterator();

         while(var7.hasNext()) {
            Unit u = (Unit)var7.next();
            Stmt next = (Stmt)u;
            if (next instanceof DefinitionStmt) {
               Value rightOp = ((DefinitionStmt)next).getRightOp();
               if (rightOp instanceof FieldRef) {
                  if (((FieldRef)rightOp).getField().isStatic()) {
                     tn.group.lockObject = rightOp;
                  } else {
                     tn.group.isDynamicLock = true;
                     tn.group.useDynamicLock = true;
                     tn.group.lockObject = tn.origLock;
                  }
               } else {
                  tn.group.isDynamicLock = true;
                  tn.group.useDynamicLock = true;
                  tn.group.lockObject = tn.origLock;
               }
            } else {
               tn.group.isDynamicLock = true;
               tn.group.useDynamicLock = true;
               tn.group.lockObject = tn.origLock;
            }
         }
      }
   }

   public static String locksetToLockNumString(List<EquivalentValue> lockset, Map<Value, Integer> lockToLockNum) {
      if (lockset == null) {
         return "null";
      } else {
         String ret = "[";
         boolean first = true;

         EquivalentValue lockEqVal;
         for(Iterator var4 = lockset.iterator(); var4.hasNext(); ret = ret + lockToLockNum.get(lockEqVal.getValue())) {
            lockEqVal = (EquivalentValue)var4.next();
            if (!first) {
               ret = ret + " ";
            }

            first = false;
         }

         return ret + "]";
      }
   }

   public void assignNamesToTransactions(List<CriticalSection> AllTransactions) {
      List<String> methodNamesTemp = new ArrayList();
      Iterator tnIt5 = AllTransactions.iterator();

      while(tnIt5.hasNext()) {
         CriticalSection tn1 = (CriticalSection)tnIt5.next();
         String mname = tn1.method.getSignature();
         if (!methodNamesTemp.contains(mname)) {
            methodNamesTemp.add(mname);
         }
      }

      String[] methodNames = new String[1];
      methodNames = (String[])methodNamesTemp.toArray(methodNames);
      Arrays.sort(methodNames);
      int[][] identMatrix = new int[methodNames.length][CriticalSection.nextIDNum - methodNames.length + 2];

      int j;
      for(int i = 0; i < methodNames.length; ++i) {
         identMatrix[i][0] = 0;

         for(j = 1; j < CriticalSection.nextIDNum - methodNames.length + 1; ++j) {
            identMatrix[i][j] = 50000;
         }
      }

      int methodNum;
      CriticalSection tn1;
      for(Iterator tnIt0 = AllTransactions.iterator(); tnIt0.hasNext(); identMatrix[methodNum][identMatrix[methodNum][0]] = tn1.IDNum) {
         tn1 = (CriticalSection)tnIt0.next();
         methodNum = Arrays.binarySearch(methodNames, tn1.method.getSignature());
         int var10002 = identMatrix[methodNum][0]++;
      }

      for(j = 0; j < methodNames.length; ++j) {
         identMatrix[j][0] = 0;
         Arrays.sort(identMatrix[j]);
      }

      int methodNum;
      int tnNum;
      CriticalSection tn1;
      for(Iterator tnIt4 = AllTransactions.iterator(); tnIt4.hasNext(); tn1.name = "m" + (methodNum < 10 ? "00" : (methodNum < 100 ? "0" : "")) + methodNum + "n" + (tnNum < 10 ? "0" : "") + tnNum) {
         tn1 = (CriticalSection)tnIt4.next();
         methodNum = Arrays.binarySearch(methodNames, tn1.method.getSignature());
         tnNum = Arrays.binarySearch(identMatrix[methodNum], tn1.IDNum) - 1;
      }

   }

   public void printGraph(Collection<CriticalSection> AllTransactions, CriticalSectionInterferenceGraph ig, Map<Value, Integer> lockToLockNum) {
      String[] colors = new String[]{"black", "blue", "blueviolet", "chartreuse", "crimson", "darkgoldenrod1", "darkseagreen", "darkslategray", "deeppink", "deepskyblue1", "firebrick1", "forestgreen", "gold", "gray80", "navy", "pink", "red", "sienna", "turquoise1", "yellow"};
      Map<Integer, String> lockColors = new HashMap();
      int colorNum = 0;
      HashSet<CriticalSection> visited = new HashSet();
      logger.debug("[transaction-graph]" + (this.optionUseLocksets ? "" : " strict") + " graph transactions {");

      label206:
      for(int group = 0; group < ig.groups().size(); ++group) {
         boolean printedHeading = false;
         Iterator tnIt = AllTransactions.iterator();

         while(true) {
            label177:
            while(true) {
               CriticalSection tn;
               do {
                  if (!tnIt.hasNext()) {
                     if (printedHeading) {
                        logger.debug("[transaction-graph] }");
                     }
                     continue label206;
                  }

                  tn = (CriticalSection)tnIt.next();
               } while(tn.setNumber != group + 1);

               if (!printedHeading) {
                  String objString;
                  if (tn.group.useDynamicLock && tn.group.lockObject != null) {
                     objString = "";
                     if (tn.group.lockObject.getType() instanceof RefType) {
                        objString = ((RefType)tn.group.lockObject.getType()).getSootClass().getShortName();
                     } else {
                        objString = tn.group.lockObject.getType().toString();
                     }

                     logger.debug("[transaction-graph] subgraph cluster_" + (group + 1) + " {\n[transaction-graph] color=blue;\n[transaction-graph] label=\"Lock: a \\n" + objString + " object\";");
                  } else if (tn.group.useLocksets) {
                     logger.debug("[transaction-graph] subgraph cluster_" + (group + 1) + " {\n[transaction-graph] color=blue;\n[transaction-graph] label=\"Locksets\";");
                  } else {
                     objString = "";
                     if (tn.group.lockObject == null) {
                        objString = "lockObj" + (group + 1);
                     } else if (tn.group.lockObject instanceof FieldRef) {
                        SootField field = ((FieldRef)tn.group.lockObject).getField();
                        objString = field.getDeclaringClass().getShortName() + "." + field.getName();
                     } else {
                        objString = tn.group.lockObject.toString();
                     }

                     logger.debug("[transaction-graph] subgraph cluster_" + (group + 1) + " {\n[transaction-graph] color=blue;\n[transaction-graph] label=\"Lock: \\n" + objString + "\";");
                  }

                  printedHeading = true;
               }

               if (Scene.v().getReachableMethods().contains(tn.method)) {
                  logger.debug("[transaction-graph] " + tn.name + " [name=\"" + tn.method.toString() + "\" style=\"setlinewidth(3)\"];");
               } else {
                  logger.debug("[transaction-graph] " + tn.name + " [name=\"" + tn.method.toString() + "\" color=cadetblue1 style=\"setlinewidth(1)\"];");
               }

               Iterator tnedgeit;
               if (tn.group.useLocksets) {
                  label201:
                  for(tnedgeit = tn.lockset.iterator(); tnedgeit.hasNext(); visited.add(tn)) {
                     EquivalentValue lockEqVal = (EquivalentValue)tnedgeit.next();
                     Integer lockNum = (Integer)lockToLockNum.get(lockEqVal.getValue());
                     Iterator var15 = tn.group.iterator();

                     while(true) {
                        CriticalSection tn2;
                        do {
                           do {
                              if (!var15.hasNext()) {
                                 continue label201;
                              }

                              tn2 = (CriticalSection)var15.next();
                           } while(visited.contains(tn2));
                        } while(!ig.mayHappenInParallel(tn, tn2));

                        Iterator var17 = tn2.lockset.iterator();

                        while(var17.hasNext()) {
                           EquivalentValue lock2EqVal = (EquivalentValue)var17.next();
                           Integer lock2Num = (Integer)lockToLockNum.get(lock2EqVal.getValue());
                           if (lockNum == lock2Num) {
                              if (!lockColors.containsKey(lockNum)) {
                                 lockColors.put(lockNum, colors[colorNum % colors.length]);
                                 ++colorNum;
                              }

                              String color = (String)lockColors.get(lockNum);
                              logger.debug("[transaction-graph] " + tn.name + " -- " + tn2.name + " [color=" + color + " style=" + (lockNum >= 0 ? "dashed" : "solid") + " exactsize=1 style=\"setlinewidth(3)\"];");
                           }
                        }
                     }
                  }
               } else {
                  tnedgeit = tn.edges.iterator();

                  while(true) {
                     CriticalSection tnedge;
                     CriticalSectionDataDependency edge;
                     do {
                        if (!tnedgeit.hasNext()) {
                           continue label177;
                        }

                        edge = (CriticalSectionDataDependency)tnedgeit.next();
                        tnedge = edge.other;
                     } while(tnedge.setNumber != group + 1);

                     logger.debug("[transaction-graph] " + tn.name + " -- " + tnedge.name + " [color=" + (edge.size > 0 ? "black" : "cadetblue1") + " style=" + (tn.setNumber > 0 && tn.group.useDynamicLock ? "dashed" : "solid") + " exactsize=" + edge.size + " style=\"setlinewidth(3)\"];");
                  }
               }
            }
         }
      }

      boolean printedHeading = false;
      Iterator tnIt = AllTransactions.iterator();

      label139:
      while(true) {
         CriticalSection tn;
         do {
            if (!tnIt.hasNext()) {
               if (printedHeading) {
                  logger.debug("[transaction-graph] }");
               }

               logger.debug("[transaction-graph] }");
               return;
            }

            tn = (CriticalSection)tnIt.next();
         } while(tn.setNumber != -1);

         if (!printedHeading) {
            logger.debug("[transaction-graph] subgraph lone {\n[transaction-graph] rank=source;");
            printedHeading = true;
         }

         if (Scene.v().getReachableMethods().contains(tn.method)) {
            logger.debug("[transaction-graph] " + tn.name + " [name=\"" + tn.method.toString() + "\" style=\"setlinewidth(3)\"];");
         } else {
            logger.debug("[transaction-graph] " + tn.name + " [name=\"" + tn.method.toString() + "\" color=cadetblue1 style=\"setlinewidth(1)\"];");
         }

         Iterator tnedgeit = tn.edges.iterator();

         while(true) {
            CriticalSection tnedge;
            CriticalSectionDataDependency edge;
            do {
               if (!tnedgeit.hasNext()) {
                  continue label139;
               }

               edge = (CriticalSectionDataDependency)tnedgeit.next();
               tnedge = edge.other;
            } while(tnedge.setNumber == tn.setNumber && tnedge.setNumber != -1);

            logger.debug("[transaction-graph] " + tn.name + " -- " + tnedge.name + " [color=" + (edge.size > 0 ? "black" : "cadetblue1") + " style=" + (tn.setNumber > 0 && tn.group.useDynamicLock ? "dashed" : "solid") + " exactsize=" + edge.size + " style=\"setlinewidth(1)\"];");
         }
      }
   }

   public void printTable(Collection<CriticalSection> AllTransactions, MhpTester mhp) {
      logger.debug("[transaction-table] ");

      CriticalSection tn;
      for(Iterator tnIt7 = AllTransactions.iterator(); tnIt7.hasNext(); logger.debug("[transaction-table] Group: " + tn.setNumber + "\n[transaction-table] ")) {
         tn = (CriticalSection)tnIt7.next();
         boolean reachable = false;
         boolean mhpself = false;
         ReachableMethods rm = Scene.v().getReachableMethods();
         reachable = rm.contains(tn.method);
         if (mhp != null) {
            mhpself = mhp.mayHappenInParallel(tn.method, tn.method);
         }

         logger.debug("[transaction-table] Transaction " + tn.name + (reachable ? " reachable" : " dead") + (mhpself ? " [called from >= 2 threads]" : " [called from <= 1 thread]"));
         logger.debug("[transaction-table] Where: " + tn.method.getDeclaringClass().toString() + ":" + tn.method.toString() + ":  ");
         logger.debug("[transaction-table] Orig : " + tn.origLock);
         logger.debug("[transaction-table] Prep : " + tn.prepStmt);
         logger.debug("[transaction-table] Begin: " + tn.entermonitor);
         logger.debug("[transaction-table] End  : early:" + tn.earlyEnds.toString() + " exc:" + tn.exceptionalEnd + " through:" + tn.end + " \n");
         logger.debug("[transaction-table] Size : " + tn.units.size());
         if (tn.read.size() < 100) {
            logger.debug("[transaction-table] Read : " + tn.read.size() + "\n[transaction-table] " + tn.read.toString().replaceAll("\\[", "     : [").replaceAll("\n", "\n[transaction-table] "));
         } else {
            logger.debug("[transaction-table] Read : " + tn.read.size() + "  \n[transaction-table] ");
         }

         if (tn.write.size() < 100) {
            logger.debug("Write: " + tn.write.size() + "\n[transaction-table] " + tn.write.toString().replaceAll("\\[", "     : [").replaceAll("\n", "\n[transaction-table] "));
         } else {
            logger.debug("Write: " + tn.write.size() + "\n[transaction-table] ");
         }

         logger.debug("Edges: (" + tn.edges.size() + ") ");
         Iterator tnedgeit = tn.edges.iterator();

         while(tnedgeit.hasNext()) {
            logger.debug("" + ((CriticalSectionDataDependency)tnedgeit.next()).other.name + " ");
         }

         if (tn.group != null && tn.group.useLocksets) {
            logger.debug("\n[transaction-table] Locks: " + tn.lockset);
         } else {
            logger.debug("\n[transaction-table] Lock : " + (tn.setNumber == -1 ? "-" : (tn.lockObject == null ? "Global" : tn.lockObject.toString() + (tn.lockObjectArrayIndex == null ? "" : "[" + tn.lockObjectArrayIndex + "]"))));
         }
      }

   }

   public void printGroups(Collection<CriticalSection> AllTransactions, CriticalSectionInterferenceGraph ig) {
      logger.debug("[transaction-groups] Group Summaries\n[transaction-groups] ");

      for(int group = 0; group < ig.groupCount() - 1; ++group) {
         CriticalSectionGroup tnGroup = (CriticalSectionGroup)ig.groups().get(group + 1);
         if (tnGroup.size() > 0) {
            logger.debug("Group " + (group + 1) + " ");
            logger.debug("Locking: " + (tnGroup.useLocksets ? "using " : (tnGroup.isDynamicLock && tnGroup.useDynamicLock ? "Dynamic on " : "Static on ")) + (tnGroup.useLocksets ? "locksets" : (tnGroup.lockObject == null ? "null" : tnGroup.lockObject.toString())));
            logger.debug("\n[transaction-groups]      : ");
            Iterator tnIt = AllTransactions.iterator();

            while(tnIt.hasNext()) {
               CriticalSection tn = (CriticalSection)tnIt.next();
               if (tn.setNumber == group + 1) {
                  logger.debug("" + tn.name + " ");
               }
            }

            logger.debug("\n[transaction-groups] " + tnGroup.rwSet.toString().replaceAll("\\[", "     : [").replaceAll("\n", "\n[transaction-groups] "));
         }
      }

      logger.debug("Erasing \n[transaction-groups]      : ");
      Iterator tnIt = AllTransactions.iterator();

      while(tnIt.hasNext()) {
         CriticalSection tn = (CriticalSection)tnIt.next();
         if (tn.setNumber == -1) {
            logger.debug("" + tn.name + " ");
         }
      }

      logger.debug("\n[transaction-groups] ");
   }

   public CriticalSectionInterferenceGraph getInterferenceGraph() {
      return this.interferenceGraph;
   }

   public DirectedGraph getDeadlockGraph() {
      return this.deadlockGraph;
   }

   public List<CriticalSection> getCriticalSections() {
      return this.criticalSections;
   }
}
