package soot.jimple.toolkits.thread.synchronization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.EquivalentValue;
import soot.Scene;
import soot.Unit;
import soot.Value;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.sets.HashPointsToSet;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.toolkits.callgraph.Filter;
import soot.jimple.toolkits.callgraph.TransitiveTargets;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.HashMutableDirectedGraph;
import soot.toolkits.graph.HashMutableEdgeLabelledDirectedGraph;
import soot.toolkits.graph.MutableDirectedGraph;
import soot.toolkits.graph.MutableEdgeLabelledDirectedGraph;

public class DeadlockDetector {
   private static final Logger logger = LoggerFactory.getLogger(DeadlockDetector.class);
   boolean optionPrintDebug;
   boolean optionRepairDeadlock;
   boolean optionAllowSelfEdges;
   List<CriticalSection> criticalSections;
   TransitiveTargets tt;

   public DeadlockDetector(boolean optionPrintDebug, boolean optionRepairDeadlock, boolean optionAllowSelfEdges, List<CriticalSection> criticalSections) {
      this.optionPrintDebug = optionPrintDebug;
      this.optionRepairDeadlock = optionRepairDeadlock;
      this.optionAllowSelfEdges = optionAllowSelfEdges && !optionRepairDeadlock;
      this.criticalSections = criticalSections;
      this.tt = new TransitiveTargets(Scene.v().getCallGraph(), new Filter(new CriticalSectionVisibleEdgesPred((Collection)null)));
   }

   public MutableDirectedGraph<CriticalSectionGroup> detectComponentBasedDeadlock() {
      int iteration = 0;

      HashMutableDirectedGraph lockOrder;
      boolean foundDeadlock;
      label131:
      do {
         ++iteration;
         logger.debug("[DeadlockDetector] Deadlock Iteration #" + iteration);
         foundDeadlock = false;
         lockOrder = new HashMutableDirectedGraph();
         Iterator deadlockIt1 = this.criticalSections.iterator();

         label124:
         while(true) {
            CriticalSection tn1;
            do {
               if (!deadlockIt1.hasNext() || foundDeadlock) {
                  continue label131;
               }

               tn1 = (CriticalSection)deadlockIt1.next();
            } while(tn1.setNumber <= 0);

            if (!lockOrder.containsNode(tn1.group)) {
               lockOrder.addNode(tn1.group);
            }

            Iterator deadlockIt2;
            if (tn1.transitiveTargets == null) {
               tn1.transitiveTargets = new HashSet();
               deadlockIt2 = tn1.invokes.iterator();

               while(deadlockIt2.hasNext()) {
                  Unit tn1Invoke = (Unit)deadlockIt2.next();
                  Iterator targetIt = this.tt.iterator(tn1Invoke);

                  while(targetIt.hasNext()) {
                     tn1.transitiveTargets.add(targetIt.next());
                  }
               }
            }

            deadlockIt2 = this.criticalSections.iterator();

            while(true) {
               CriticalSection tn2;
               do {
                  do {
                     do {
                        if (!deadlockIt2.hasNext() || this.optionRepairDeadlock && foundDeadlock) {
                           continue label124;
                        }

                        tn2 = (CriticalSection)deadlockIt2.next();
                     } while(tn2.setNumber <= 0);
                  } while(tn2.setNumber == tn1.setNumber && !this.optionAllowSelfEdges);

                  if (!lockOrder.containsNode(tn2.group)) {
                     lockOrder.addNode(tn2.group);
                  }
               } while(!tn1.transitiveTargets.contains(tn2.method));

               if (this.optionPrintDebug) {
                  logger.debug("group" + tn1.setNumber + " before group" + tn2.setNumber + ": outer: " + tn1.name + " inner: " + tn2.name);
               }

               List<CriticalSectionGroup> afterTn2 = new ArrayList();
               afterTn2.addAll(lockOrder.getSuccsOf(tn2.group));

               for(int i = 0; i < afterTn2.size(); ++i) {
                  Iterator var10 = lockOrder.getSuccsOf(afterTn2.get(i)).iterator();

                  while(var10.hasNext()) {
                     CriticalSectionGroup o = (CriticalSectionGroup)var10.next();
                     if (!afterTn2.contains(o)) {
                        afterTn2.add(o);
                     }
                  }
               }

               if (afterTn2.contains(tn1.group)) {
                  if (!this.optionRepairDeadlock) {
                     logger.debug("[DeadlockDetector]  DEADLOCK HAS BEEN DETECTED: not correcting");
                     foundDeadlock = true;
                  } else {
                     logger.debug("[DeadlockDetector]  DEADLOCK HAS BEEN DETECTED: merging group" + tn1.setNumber + " and group" + tn2.setNumber + " and restarting deadlock detection");
                     if (this.optionPrintDebug) {
                        logger.debug("tn1.setNumber was " + tn1.setNumber + " and tn2.setNumber was " + tn2.setNumber);
                        logger.debug("tn1.group.size was " + tn1.group.criticalSections.size() + " and tn2.group.size was " + tn2.group.criticalSections.size());
                        logger.debug("tn1.group.num was  " + tn1.group.num() + " and tn2.group.num was  " + tn2.group.num());
                     }

                     tn1.group.mergeGroups(tn2.group);
                     if (this.optionPrintDebug) {
                        logger.debug("tn1.setNumber is  " + tn1.setNumber + " and tn2.setNumber is  " + tn2.setNumber);
                        logger.debug("tn1.group.size is  " + tn1.group.criticalSections.size() + " and tn2.group.size is  " + tn2.group.criticalSections.size());
                     }

                     foundDeadlock = true;
                  }
               }

               lockOrder.addEdge(tn1.group, tn2.group);
            }
         }
      } while(foundDeadlock && this.optionRepairDeadlock);

      return lockOrder;
   }

   public MutableEdgeLabelledDirectedGraph<Integer, CriticalSection> detectLocksetDeadlock(Map<Value, Integer> lockToLockNum, List<PointsToSetInternal> lockPTSets) {
      HashMutableEdgeLabelledDirectedGraph<Integer, CriticalSection> permanentOrder = new HashMutableEdgeLabelledDirectedGraph();
      int iteration = 0;

      HashMutableEdgeLabelledDirectedGraph lockOrder;
      boolean foundDeadlock;
      label265:
      do {
         ++iteration;
         logger.debug("[DeadlockDetector] Deadlock Iteration #" + iteration);
         foundDeadlock = false;
         lockOrder = permanentOrder.clone();
         Iterator deadlockIt1 = this.criticalSections.iterator();

         label258:
         while(true) {
            CriticalSection tn1;
            do {
               if (!deadlockIt1.hasNext() || foundDeadlock) {
                  continue label265;
               }

               tn1 = (CriticalSection)deadlockIt1.next();
            } while(tn1.group == null);

            Iterator deadlockIt2 = tn1.lockset.iterator();

            while(deadlockIt2.hasNext()) {
               EquivalentValue lockEqVal = (EquivalentValue)deadlockIt2.next();
               Value lock = lockEqVal.getValue();
               if (!lockOrder.containsNode(lockToLockNum.get(lock))) {
                  lockOrder.addNode(lockToLockNum.get(lock));
               }
            }

            Iterator targetIt;
            if (tn1.transitiveTargets == null) {
               tn1.transitiveTargets = new HashSet();
               deadlockIt2 = tn1.invokes.iterator();

               while(deadlockIt2.hasNext()) {
                  Unit tn1Invoke = (Unit)deadlockIt2.next();
                  targetIt = this.tt.iterator(tn1Invoke);

                  while(targetIt.hasNext()) {
                     tn1.transitiveTargets.add(targetIt.next());
                  }
               }
            }

            deadlockIt2 = this.criticalSections.iterator();

            while(true) {
               EquivalentValue lock2EqVal;
               Value lock2;
               CriticalSection tn2;
               do {
                  do {
                     do {
                        if (!deadlockIt2.hasNext() || foundDeadlock) {
                           continue label258;
                        }

                        tn2 = (CriticalSection)deadlockIt2.next();
                     } while(tn2.group == null);

                     targetIt = tn2.lockset.iterator();

                     while(targetIt.hasNext()) {
                        lock2EqVal = (EquivalentValue)targetIt.next();
                        lock2 = lock2EqVal.getValue();
                        if (!lockOrder.containsNode(lockToLockNum.get(lock2))) {
                           lockOrder.addNode(lockToLockNum.get(lock2));
                        }
                     }
                  } while(!tn1.transitiveTargets.contains(tn2.method));
               } while(foundDeadlock);

               logger.debug("[DeadlockDetector] locks in " + tn1.name + " before locks in " + tn2.name + ": outer: " + tn1.name + " inner: " + tn2.name);
               targetIt = tn2.lockset.iterator();

               while(targetIt.hasNext()) {
                  lock2EqVal = (EquivalentValue)targetIt.next();
                  lock2 = lock2EqVal.getValue();
                  Integer lock2Num = (Integer)lockToLockNum.get(lock2);
                  List<Integer> afterTn2 = new ArrayList();
                  afterTn2.addAll(lockOrder.getSuccsOf(lock2Num));
                  ListIterator lit = afterTn2.listIterator();

                  Iterator var26;
                  while(lit.hasNext()) {
                     Integer to = (Integer)lit.next();
                     List<CriticalSection> labels = lockOrder.getLabelsForEdges(lock2Num, to);
                     boolean keep = false;
                     if (labels != null) {
                        Iterator var20 = labels.iterator();

                        label193:
                        while(var20.hasNext()) {
                           CriticalSection labelTn = (CriticalSection)var20.next();
                           boolean tnsShareAStaticLock = false;
                           Iterator var23 = tn1.lockset.iterator();

                           while(true) {
                              Integer tn1LockNum;
                              do {
                                 if (!var23.hasNext()) {
                                    if (!tnsShareAStaticLock) {
                                       keep = true;
                                       break label193;
                                    }
                                    continue label193;
                                 }

                                 EquivalentValue tn1LockEqVal = (EquivalentValue)var23.next();
                                 tn1LockNum = (Integer)lockToLockNum.get(tn1LockEqVal.getValue());
                              } while(tn1LockNum >= 0);

                              var26 = labelTn.lockset.iterator();

                              while(var26.hasNext()) {
                                 EquivalentValue labelTnLockEqVal = (EquivalentValue)var26.next();
                                 if (Objects.equals(lockToLockNum.get(labelTnLockEqVal.getValue()), tn1LockNum)) {
                                    tnsShareAStaticLock = true;
                                 }
                              }
                           }
                        }
                     }

                     if (!keep) {
                        lit.remove();
                     }
                  }

                  Iterator var35 = tn1.lockset.iterator();

                  while(var35.hasNext()) {
                     EquivalentValue lock1EqVal = (EquivalentValue)var35.next();
                     Value lock1 = lock1EqVal.getValue();
                     Integer lock1Num = (Integer)lockToLockNum.get(lock1);
                     if ((!Objects.equals(lock1Num, lock2Num) || lock1Num > 0) && afterTn2.contains(lock1Num)) {
                        if (this.optionRepairDeadlock) {
                           logger.debug("[DeadlockDetector] DEADLOCK HAS BEEN DETECTED while inspecting " + lock1Num + " (" + lock1 + ") and " + lock2Num + " (" + lock2 + ") ");
                           DeadlockAvoidanceEdge dae = new DeadlockAvoidanceEdge(tn1.method.getDeclaringClass());
                           EquivalentValue daeEqVal = new EquivalentValue(dae);
                           Integer daeNum = -lockPTSets.size();
                           permanentOrder.addNode(daeNum);
                           lockToLockNum.put(dae, daeNum);
                           PointsToSetInternal dummyLockPT = new HashPointsToSet(lock1.getType(), (PAG)Scene.v().getPointsToAnalysis());
                           lockPTSets.add(dummyLockPT);

                           Integer lockNum;
                           for(Iterator var43 = tn1.lockset.iterator(); var43.hasNext(); permanentOrder.addEdge(daeNum, lockNum, tn1)) {
                              EquivalentValue lockEqVal = (EquivalentValue)var43.next();
                              lockNum = (Integer)lockToLockNum.get(lockEqVal.getValue());
                              if (!permanentOrder.containsNode(lockNum)) {
                                 permanentOrder.addNode(lockNum);
                              }
                           }

                           tn1.lockset.add(daeEqVal);
                           List<CriticalSection> forwardLabels = lockOrder.getLabelsForEdges(lock1Num, lock2Num);
                           if (forwardLabels != null) {
                              var26 = forwardLabels.iterator();

                              label232:
                              while(true) {
                                 CriticalSection tn;
                                 do {
                                    if (!var26.hasNext()) {
                                       break label232;
                                    }

                                    tn = (CriticalSection)var26.next();
                                 } while(tn.lockset.contains(daeEqVal));

                                 Integer lockNum;
                                 for(Iterator var28 = tn.lockset.iterator(); var28.hasNext(); permanentOrder.addEdge(daeNum, lockNum, tn)) {
                                    EquivalentValue lockEqVal = (EquivalentValue)var28.next();
                                    lockNum = (Integer)lockToLockNum.get(lockEqVal.getValue());
                                    if (!permanentOrder.containsNode(lockNum)) {
                                       permanentOrder.addNode(lockNum);
                                    }
                                 }

                                 tn.lockset.add(daeEqVal);
                              }
                           }

                           List<CriticalSection> backwardLabels = lockOrder.getLabelsForEdges(lock2Num, lock1Num);
                           if (backwardLabels != null) {
                              Iterator var49 = backwardLabels.iterator();

                              label214:
                              while(true) {
                                 CriticalSection tn;
                                 do {
                                    if (!var49.hasNext()) {
                                       logger.debug("[DeadlockDetector]   Restarting deadlock detection");
                                       break label214;
                                    }

                                    tn = (CriticalSection)var49.next();
                                 } while(tn.lockset.contains(daeEqVal));

                                 Integer lockNum;
                                 for(Iterator var51 = tn.lockset.iterator(); var51.hasNext(); permanentOrder.addEdge(daeNum, lockNum, tn)) {
                                    EquivalentValue lockEqVal = (EquivalentValue)var51.next();
                                    lockNum = (Integer)lockToLockNum.get(lockEqVal.getValue());
                                    if (!permanentOrder.containsNode(lockNum)) {
                                       permanentOrder.addNode(lockNum);
                                    }
                                 }

                                 tn.lockset.add(daeEqVal);
                                 logger.debug("[DeadlockDetector]   Adding deadlock avoidance edge between " + tn1.name + " and " + tn.name);
                              }
                           }

                           foundDeadlock = true;
                           break;
                        }

                        logger.debug("[DeadlockDetector] DEADLOCK HAS BEEN DETECTED: not correcting");
                        foundDeadlock = true;
                     }

                     if (!Objects.equals(lock1Num, lock2Num)) {
                        lockOrder.addEdge(lock1Num, lock2Num, tn1);
                     }
                  }

                  if (foundDeadlock) {
                     break;
                  }
               }
            }
         }
      } while(foundDeadlock && this.optionRepairDeadlock);

      return lockOrder;
   }

   public void reorderLocksets(Map<Value, Integer> lockToLockNum, MutableEdgeLabelledDirectedGraph<Integer, CriticalSection> lockOrder) {
      Iterator var3 = this.criticalSections.iterator();

      label115:
      while(true) {
         CriticalSection tn;
         HashMutableDirectedGraph visibleOrder;
         do {
            if (!var3.hasNext()) {
               return;
            }

            tn = (CriticalSection)var3.next();
            visibleOrder = new HashMutableDirectedGraph();
         } while(tn.group == null);

         Iterator var6 = this.criticalSections.iterator();

         while(true) {
            CriticalSection otherTn;
            boolean tnsShareAStaticLock;
            Integer node1;
            Iterator var12;
            label84:
            do {
               if (!var6.hasNext()) {
                  logger.debug("VISIBLE ORDER FOR " + tn.name);
                  visibleOrder.printGraph();
                  List<EquivalentValue> newLockset = new ArrayList();

                  EquivalentValue lockEqVal;
                  int i;
                  for(Iterator var16 = tn.lockset.iterator(); var16.hasNext(); newLockset.add(i, lockEqVal)) {
                     lockEqVal = (EquivalentValue)var16.next();
                     Value lockToInsert = lockEqVal.getValue();
                     Integer lockNumToInsert = (Integer)lockToLockNum.get(lockToInsert);

                     for(i = 0; i < newLockset.size(); ++i) {
                        EquivalentValue existingLockEqVal = (EquivalentValue)newLockset.get(i);
                        Value existingLock = existingLockEqVal.getValue();
                        Integer existingLockNum = (Integer)lockToLockNum.get(existingLock);
                        if (visibleOrder.containsEdge(lockNumToInsert, existingLockNum) || lockNumToInsert < existingLockNum) {
                           break;
                        }
                     }
                  }

                  logger.debug("reordered from " + LockAllocator.locksetToLockNumString(tn.lockset, lockToLockNum) + " to " + LockAllocator.locksetToLockNumString(newLockset, lockToLockNum));
                  tn.lockset = newLockset;
                  continue label115;
               }

               otherTn = (CriticalSection)var6.next();
               tnsShareAStaticLock = false;
               Iterator var9 = tn.lockset.iterator();

               while(true) {
                  while(true) {
                     do {
                        if (!var9.hasNext()) {
                           continue label84;
                        }

                        EquivalentValue tnLockEqVal = (EquivalentValue)var9.next();
                        node1 = (Integer)lockToLockNum.get(tnLockEqVal.getValue());
                     } while(node1 >= 0);

                     if (otherTn.group != null) {
                        var12 = otherTn.lockset.iterator();

                        while(var12.hasNext()) {
                           EquivalentValue otherTnLockEqVal = (EquivalentValue)var12.next();
                           if (Objects.equals(lockToLockNum.get(otherTnLockEqVal.getValue()), node1)) {
                              tnsShareAStaticLock = true;
                           }
                        }
                     } else {
                        tnsShareAStaticLock = true;
                     }
                  }
               }
            } while(tnsShareAStaticLock && tn != otherTn);

            DirectedGraph<Integer> orderings = lockOrder.getEdgesForLabel(otherTn);
            Iterator var20 = orderings.iterator();

            while(var20.hasNext()) {
               node1 = (Integer)var20.next();
               if (!visibleOrder.containsNode(node1)) {
                  visibleOrder.addNode(node1);
               }

               Integer node2;
               for(var12 = orderings.getSuccsOf(node1).iterator(); var12.hasNext(); visibleOrder.addEdge(node1, node2)) {
                  node2 = (Integer)var12.next();
                  if (!visibleOrder.containsNode(node2)) {
                     visibleOrder.addNode(node2);
                  }
               }
            }
         }
      }
   }
}
