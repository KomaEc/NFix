package soot.jimple.toolkits.thread.mhp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import soot.jimple.toolkits.thread.mhp.stmt.BeginStmt;
import soot.jimple.toolkits.thread.mhp.stmt.JPegStmt;
import soot.jimple.toolkits.thread.mhp.stmt.JoinStmt;
import soot.jimple.toolkits.thread.mhp.stmt.MonitorEntryStmt;
import soot.jimple.toolkits.thread.mhp.stmt.NotifiedEntryStmt;
import soot.jimple.toolkits.thread.mhp.stmt.NotifyAllStmt;
import soot.jimple.toolkits.thread.mhp.stmt.NotifyStmt;
import soot.jimple.toolkits.thread.mhp.stmt.StartStmt;
import soot.jimple.toolkits.thread.mhp.stmt.WaitingStmt;
import soot.tagkit.Tag;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.util.Chain;

class MhpAnalysis {
   private PegGraph g;
   private final Map<Object, FlowSet> unitToGen;
   private final Map<Object, FlowSet> unitToKill;
   private final Map<Object, FlowSet> unitToM;
   private final Map<Object, FlowSet> unitToOut;
   private final Map<Object, FlowSet> notifySucc;
   private final Map<String, FlowSet> monitor;
   private final Map<JPegStmt, Set<JPegStmt>> notifyPred;
   FlowSet fullSet = new ArraySparseSet();
   LinkedList<Object> workList = new LinkedList();

   MhpAnalysis(PegGraph g) {
      this.g = g;
      int size = g.size();
      Map startToThread = g.getStartToThread();
      this.unitToGen = new HashMap(size * 2 + 1, 0.7F);
      this.unitToKill = new HashMap(size * 2 + 1, 0.7F);
      this.unitToM = new HashMap(size * 2 + 1, 0.7F);
      this.unitToOut = new HashMap(size * 2 + 1, 0.7F);
      this.notifySucc = new HashMap(size * 2 + 1, 0.7F);
      this.notifyPred = new HashMap(size * 2 + 1, 0.7F);
      this.monitor = g.getMonitor();
      Iterator it = g.iterator();

      ArraySparseSet genSet;
      while(it.hasNext()) {
         Object stmt = it.next();
         FlowSet genSet = new ArraySparseSet();
         genSet = new ArraySparseSet();
         FlowSet mSet = new ArraySparseSet();
         FlowSet outSet = new ArraySparseSet();
         FlowSet notifySuccSet = new ArraySparseSet();
         this.unitToGen.put(stmt, genSet);
         this.unitToKill.put(stmt, genSet);
         this.unitToM.put(stmt, mSet);
         this.unitToOut.put(stmt, outSet);
         this.notifySucc.put(stmt, notifySuccSet);
      }

      Set keys = startToThread.keySet();
      Iterator keysIt = keys.iterator();

      while(keysIt.hasNext()) {
         JPegStmt stmt = (JPegStmt)keysIt.next();
         if (!this.workList.contains(stmt)) {
            this.workList.addLast(stmt);
         }
      }

      it = g.iterator();

      while(true) {
         Object killSet;
         JPegStmt s;
         do {
            while(true) {
               Object o;
               do {
                  if (!it.hasNext()) {
                     this.doAnalysis();
                     long beginTime = System.currentTimeMillis();
                     this.computeMPairs();
                     this.computeMSet();
                     long buildPegDuration = System.currentTimeMillis() - beginTime;
                     System.err.println("compute parir + mset: " + buildPegDuration);
                     return;
                  }

                  genSet = new ArraySparseSet();
                  killSet = new ArraySparseSet();
                  o = it.next();
               } while(!(o instanceof JPegStmt));

               s = (JPegStmt)o;
               if (s instanceof JoinStmt) {
                  break;
               }

               Iterator chainIt;
               if (!(s instanceof MonitorEntryStmt) && !(s instanceof NotifiedEntryStmt)) {
                  Iterator beginNodesIt;
                  Map waitingNodes;
                  FlowSet killNodes;
                  if (s instanceof NotifyAllStmt) {
                     waitingNodes = g.getWaitingNodes();
                     if (waitingNodes.containsKey(s.getObject())) {
                        killNodes = (FlowSet)waitingNodes.get(s.getObject());
                        beginNodesIt = killNodes.iterator();

                        while(beginNodesIt.hasNext()) {
                           ((FlowSet)killSet).add(beginNodesIt.next());
                        }
                     }

                     this.unitToGen.put(s, genSet);
                     this.unitToKill.put(s, killSet);
                  } else if (s instanceof NotifyStmt) {
                     waitingNodes = g.getWaitingNodes();
                     if (waitingNodes.containsKey(s.getObject())) {
                        killNodes = (FlowSet)waitingNodes.get(s.getObject());
                        if (killNodes.size() == 1) {
                           beginNodesIt = killNodes.iterator();

                           while(beginNodesIt.hasNext()) {
                              ((FlowSet)killSet).add(beginNodesIt.next());
                           }
                        }
                     }

                     this.unitToGen.put(s, genSet);
                     this.unitToKill.put(s, killSet);
                  } else if (s instanceof StartStmt && g.getStartToThread().containsKey(s)) {
                     chainIt = ((List)g.getStartToThread().get(s)).iterator();

                     while(chainIt.hasNext()) {
                        PegChain chain = (PegChain)chainIt.next();
                        beginNodesIt = chain.getHeads().iterator();

                        while(beginNodesIt.hasNext()) {
                           genSet.add(beginNodesIt.next());
                        }
                     }

                     this.unitToGen.put(s, genSet);
                     this.unitToKill.put(s, killSet);
                  }
               } else {
                  chainIt = g.iterator();
                  if (this.monitor.containsKey(s.getObject())) {
                     killSet = (FlowSet)this.monitor.get(s.getObject());
                  }

                  this.unitToGen.put(s, genSet);
                  this.unitToKill.put(s, killSet);
               }
            }
         } while(g.getSpecialJoin().contains(s));

         Chain chain = (Chain)g.getJoinStmtToThread().get(s);
         Iterator nodesIt = chain.iterator();
         if (nodesIt.hasNext()) {
            while(nodesIt.hasNext()) {
               ((FlowSet)killSet).add(nodesIt.next());
            }
         }

         this.unitToGen.put(s, genSet);
         this.unitToKill.put(s, killSet);
      }
   }

   protected void doAnalysis() {
      label435:
      while(this.workList.size() > 0) {
         Object currentObj = this.workList.removeFirst();
         FlowSet killSet = (FlowSet)this.unitToKill.get(currentObj);
         FlowSet genSet = (FlowSet)this.unitToGen.get(currentObj);
         FlowSet mSet = new ArraySparseSet();
         FlowSet outSet = (FlowSet)this.unitToOut.get(currentObj);
         FlowSet notifySuccSet = (FlowSet)this.notifySucc.get(currentObj);
         FlowSet mOld = (FlowSet)this.unitToM.get(currentObj);
         FlowSet outOld = outSet.clone();
         FlowSet notifySuccSetOld = notifySuccSet.clone();
         FlowSet genNotifyAllSet = new ArraySparseSet();
         JPegStmt waitingPred = null;
         FlowSet mSetMSym;
         if (!(currentObj instanceof JPegStmt)) {
            Iterator localPredIt = this.g.getPredsOf(currentObj).iterator();

            while(localPredIt.hasNext()) {
               Object tempStmt = localPredIt.next();
               FlowSet out = (FlowSet)this.unitToOut.get(tempStmt);
               if (out != null) {
                  mSet.union(out);
               }
            }

            mSet.union(mOld);
            this.unitToM.put(currentObj, mSet);
            mSet.union(genSet, outSet);
            Iterator localSuccIt;
            Object localSucc;
            if (killSet.size() > 0) {
               localSuccIt = killSet.iterator();

               while(localSuccIt.hasNext()) {
                  localSucc = localSuccIt.next();
                  if (outSet.contains(localSucc)) {
                     outSet.remove(localSucc);
                  }
               }
            }

            if (!mOld.equals(mSet)) {
               localSuccIt = mSet.iterator();

               while(localSuccIt.hasNext()) {
                  localSucc = localSuccIt.next();
                  if (!mOld.contains(localSucc) && this.unitToM.containsKey(localSucc)) {
                     mSetMSym = (FlowSet)this.unitToM.get(localSucc);
                     if (mSetMSym.size() != 0) {
                        if (!mSetMSym.contains(currentObj)) {
                           mSetMSym.add(currentObj);
                        }
                     } else {
                        mSetMSym.add(currentObj);
                     }
                  }

                  if (!this.workList.contains(localSucc)) {
                     this.workList.addLast(localSucc);
                  }
               }
            }

            if (!outOld.equals(outSet)) {
               localSuccIt = this.g.getSuccsOf(currentObj).iterator();

               while(localSuccIt.hasNext()) {
                  localSucc = localSuccIt.next();
                  if (localSucc instanceof JPegStmt) {
                     if (!((JPegStmt)localSucc instanceof NotifiedEntryStmt) && !this.workList.contains(localSucc)) {
                        this.workList.addLast(localSucc);
                     }
                  } else if (!this.workList.contains(localSucc)) {
                     this.workList.addLast(localSucc);
                  }
               }
            }
         } else {
            JPegStmt currentNode = (JPegStmt)currentObj;
            Tag tag = (Tag)currentNode.getTags().get(0);
            Iterator chainListIt;
            JPegStmt waitingPredNode;
            FlowSet mWaitingPredM;
            if (currentNode instanceof NotifyStmt || currentNode instanceof NotifyAllStmt) {
               Map<String, FlowSet> waitingNodes = this.g.getWaitingNodes();
               if (!waitingNodes.containsKey(currentNode.getObject())) {
                  throw new RuntimeException("Fail to find waiting node for: " + currentObj);
               }

               mSetMSym = (FlowSet)waitingNodes.get(currentNode.getObject());
               Iterator waitingNodesIt = mSetMSym.iterator();

               label427:
               while(true) {
                  JPegStmt tempNode;
                  do {
                     if (!waitingNodesIt.hasNext()) {
                        break label427;
                     }

                     tempNode = (JPegStmt)waitingNodesIt.next();
                  } while(!mOld.contains(tempNode));

                  List waitingSuccList = this.g.getSuccsOf(tempNode);
                  chainListIt = waitingSuccList.iterator();

                  while(chainListIt.hasNext()) {
                     waitingPredNode = (JPegStmt)chainListIt.next();
                     notifySuccSet.add(waitingPredNode);
                     if (waitingPredNode instanceof NotifiedEntryStmt) {
                        mWaitingPredM = (FlowSet)this.notifySucc.get(currentNode);
                        mWaitingPredM.add(waitingPredNode);
                        this.notifySucc.put(currentNode, mWaitingPredM);
                        if (this.notifyPred.containsKey(waitingPredNode)) {
                           Set<JPegStmt> notifyPredSet = (Set)this.notifyPred.get(waitingPredNode);
                           notifyPredSet.add(currentNode);
                           this.notifyPred.put(waitingPredNode, notifyPredSet);
                        } else {
                           Set<JPegStmt> notifyPredSet = new HashSet();
                           notifyPredSet.add(currentNode);
                           this.notifyPred.put(waitingPredNode, notifyPredSet);
                        }
                     }
                  }
               }
            }

            Iterator waitingPredIt;
            if (!notifySuccSetOld.equals(notifySuccSet)) {
               waitingPredIt = notifySuccSet.iterator();

               while(waitingPredIt.hasNext()) {
                  Object notifySuccNode = waitingPredIt.next();
                  if (!this.workList.contains(notifySuccNode)) {
                     this.workList.addLast(notifySuccNode);
                  }
               }
            }

            Map startToThread;
            FlowSet mSetMSym;
            Iterator it;
            Iterator chainIt;
            JPegStmt notifyEntry;
            Iterator chainListIt;
            if (currentNode instanceof NotifiedEntryStmt) {
               waitingPredIt = this.g.getPredsOf(currentNode).iterator();

               while(waitingPredIt.hasNext()) {
                  waitingPred = (JPegStmt)waitingPredIt.next();
                  if (waitingPred instanceof WaitingStmt && waitingPred.getObject().equals(currentNode.getObject()) && waitingPred.getCaller().equals(currentNode.getCaller())) {
                     break;
                  }
               }

               startToThread = this.g.getWaitingNodes();
               FlowSet notifyEntrySet = new ArraySparseSet();
               if (startToThread.containsKey(currentNode.getObject())) {
                  mSetMSym = (FlowSet)startToThread.get(currentNode.getObject());
                  chainIt = mSetMSym.iterator();

                  while(chainIt.hasNext()) {
                     List waitingNodesSucc = this.g.getSuccsOf(chainIt.next());
                     chainListIt = waitingNodesSucc.iterator();

                     while(chainListIt.hasNext()) {
                        JPegStmt notifyEntry = (JPegStmt)chainListIt.next();
                        if (notifyEntry instanceof NotifiedEntryStmt) {
                           notifyEntrySet.add(notifyEntry);
                        }
                     }
                  }
               }

               it = notifyEntrySet.iterator();

               label382:
               while(true) {
                  Map notifyAll;
                  do {
                     do {
                        do {
                           if (!it.hasNext()) {
                              break label382;
                           }

                           notifyEntry = (JPegStmt)it.next();
                           chainListIt = this.g.getPredsOf(notifyEntry).iterator();
                           waitingPredNode = null;

                           while(chainListIt.hasNext()) {
                              waitingPredNode = (JPegStmt)chainListIt.next();
                              if (waitingPredNode instanceof WaitingStmt && waitingPredNode.getObject().equals(currentNode.getObject()) && waitingPredNode.getCaller().equals(currentNode.getCaller())) {
                                 break;
                              }
                           }
                        } while(!this.unitToM.containsKey(waitingPredNode));

                        mWaitingPredM = (FlowSet)this.unitToM.get(waitingPredNode);
                     } while(!mWaitingPredM.contains(waitingPred));

                     notifyAll = this.g.getNotifyAll();
                  } while(!notifyAll.containsKey(currentNode.getObject()));

                  Set notifyAllSet = (Set)notifyAll.get(currentNode.getObject());
                  Iterator notifyAllIt = notifyAllSet.iterator();

                  while(notifyAllIt.hasNext()) {
                     JPegStmt notifyAllStmt = (JPegStmt)notifyAllIt.next();
                     if (this.unitToM.containsKey(waitingPred)) {
                        FlowSet mWaitingPredN = (FlowSet)this.unitToM.get(waitingPred);
                        if (mWaitingPredM.contains(notifyAllStmt) && mWaitingPredN.contains(notifyAllStmt)) {
                           genNotifyAllSet.add(notifyEntry);
                        }
                     }
                  }
               }
            }

            FlowSet notifyPredUnion = new ArraySparseSet();
            Iterator localSuccIt;
            Set notifyPredSet;
            Object localSucc;
            if (currentNode instanceof NotifiedEntryStmt) {
               if (!this.unitToOut.containsKey(waitingPred)) {
                  throw new RuntimeException("unitToOut does not contains " + waitingPred);
               }

               FlowSet mSetOfNotifyEntry = new ArraySparseSet();
               notifyPredSet = (Set)this.notifyPred.get(currentNode);
               if (notifyPredSet != null) {
                  it = notifyPredSet.iterator();

                  while(it.hasNext()) {
                     notifyEntry = (JPegStmt)it.next();
                     FlowSet outWaitingPredTemp = (FlowSet)this.unitToOut.get(notifyEntry);
                     outWaitingPredTemp.copy(notifyPredUnion);
                  }

                  FlowSet outWaitingPredSet = (FlowSet)this.unitToOut.get(waitingPred);
                  notifyPredUnion.intersection(outWaitingPredSet, mSetOfNotifyEntry);
                  mSetOfNotifyEntry.union(genNotifyAllSet, mSet);
               }
            } else if (currentNode instanceof BeginStmt) {
               mSet = new ArraySparseSet();
               startToThread = this.g.getStartToThread();
               notifyPredSet = startToThread.keySet();
               it = notifyPredSet.iterator();

               label339:
               while(it.hasNext()) {
                  notifyEntry = (JPegStmt)it.next();
                  chainListIt = ((List)startToThread.get(notifyEntry)).iterator();

                  while(true) {
                     List beginNodes;
                     do {
                        if (!chainListIt.hasNext()) {
                           continue label339;
                        }

                        beginNodes = ((PegChain)chainListIt.next()).getHeads();
                     } while(!beginNodes.contains(currentNode));

                     Iterator outStartPredIt = ((FlowSet)this.unitToOut.get(notifyEntry)).iterator();

                     while(outStartPredIt.hasNext()) {
                        Object startPred = outStartPredIt.next();
                        mSet.add(startPred);
                     }
                  }
               }

               chainIt = startToThread.keySet().iterator();

               label320:
               while(chainIt.hasNext()) {
                  JPegStmt tempStmt = (JPegStmt)chainIt.next();
                  chainListIt = ((List)startToThread.get(tempStmt)).iterator();

                  while(true) {
                     Chain chain;
                     do {
                        if (!chainListIt.hasNext()) {
                           continue label320;
                        }

                        chain = (Chain)chainListIt.next();
                     } while(!chain.contains(currentNode));

                     Iterator nodesIt = chain.iterator();

                     while(nodesIt.hasNext()) {
                        Object stmt = nodesIt.next();
                        if (mSet.contains(stmt)) {
                           mSet.remove(stmt);
                        }
                     }
                  }
               }
            } else {
               localSuccIt = this.g.getPredsOf(currentNode).iterator();
               if (!(currentNode instanceof NotifiedEntryStmt)) {
                  while(localSuccIt.hasNext()) {
                     localSucc = localSuccIt.next();
                     mSetMSym = (FlowSet)this.unitToOut.get(localSucc);
                     if (mSetMSym != null) {
                        mSet.union(mSetMSym);
                     }
                  }
               }
            }

            mSet.union(mOld);
            this.unitToM.put(currentNode, mSet);
            if (currentNode instanceof NotifyStmt || currentNode instanceof NotifyAllStmt) {
               notifySuccSet.copy(genSet);
               this.unitToGen.put(currentNode, genSet);
            }

            mSet.union(genSet, outSet);
            if (killSet.size() > 0) {
               localSuccIt = killSet.iterator();

               while(localSuccIt.hasNext()) {
                  localSucc = localSuccIt.next();
                  if (outSet.contains(localSucc)) {
                     outSet.remove(localSucc);
                  }
               }
            }

            if (!mOld.equals(mSet)) {
               localSuccIt = mSet.iterator();

               while(localSuccIt.hasNext()) {
                  localSucc = localSuccIt.next();
                  if (!mOld.contains(localSucc)) {
                     if (!this.unitToM.containsKey(localSucc)) {
                        throw new RuntimeException("unitToM does not contain: " + localSucc);
                     }

                     mSetMSym = (FlowSet)this.unitToM.get(localSucc);
                     if (mSetMSym.size() != 0) {
                        if (!mSetMSym.contains(currentNode)) {
                           mSetMSym.add(currentNode);
                        }
                     } else {
                        mSetMSym.add(currentNode);
                     }
                  }

                  if (!this.workList.contains(localSucc)) {
                     this.workList.addLast(localSucc);
                  }
               }
            }

            if (!outOld.equals(outSet)) {
               localSuccIt = this.g.getSuccsOf(currentNode).iterator();

               while(localSuccIt.hasNext()) {
                  localSucc = localSuccIt.next();
                  if (localSucc instanceof JPegStmt) {
                     if (!((JPegStmt)localSucc instanceof NotifiedEntryStmt) && !this.workList.contains(localSucc)) {
                        this.workList.addLast(localSucc);
                     }
                  } else if (!this.workList.contains(localSucc)) {
                     this.workList.addLast(localSucc);
                  }
               }

               if (currentNode instanceof StartStmt) {
                  Map<JPegStmt, List> startToThread = this.g.getStartToThread();
                  if (startToThread.containsKey(currentNode)) {
                     it = ((List)startToThread.get(currentNode)).iterator();

                     while(true) {
                        while(true) {
                           if (!it.hasNext()) {
                              continue label435;
                           }

                           chainIt = ((Chain)it.next()).iterator();

                           while(chainIt.hasNext()) {
                              Object tempStmt = chainIt.next();
                              if (tempStmt instanceof JPegStmt && (JPegStmt)tempStmt instanceof BeginStmt) {
                                 if (!this.workList.contains(tempStmt)) {
                                    this.workList.addLast(tempStmt);
                                 }
                                 break;
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

   }

   protected Object entryInitialFlow() {
      return new ArraySparseSet();
   }

   protected Object newInitialFlow() {
      return this.fullSet.clone();
   }

   protected Map<Object, FlowSet> getUnitToM() {
      return this.unitToM;
   }

   private void computeMPairs() {
      Set<Set<Object>> mSetPairs = new HashSet();
      Set maps = this.unitToM.entrySet();
      Iterator iter = maps.iterator();

      while(iter.hasNext()) {
         Entry entry = (Entry)iter.next();
         Object obj = entry.getKey();
         FlowSet fs = (FlowSet)entry.getValue();
         Iterator it = fs.iterator();

         while(it.hasNext()) {
            Object m = it.next();
            Set<Object> pair = new HashSet();
            pair.add(obj);
            pair.add(m);
            if (!mSetPairs.contains(pair)) {
               mSetPairs.add(pair);
            }
         }
      }

      System.err.println("Number of pairs: " + mSetPairs.size());
   }

   private void computeMSet() {
      long min = 0L;
      long max = 0L;
      long nodes = 0L;
      long totalNodes = 0L;
      Set maps = this.unitToM.entrySet();
      boolean first = true;
      Iterator iter = maps.iterator();

      while(iter.hasNext()) {
         Entry entry = (Entry)iter.next();
         Object obj = entry.getKey();
         FlowSet fs = (FlowSet)entry.getValue();
         if (fs.size() > 0) {
            totalNodes += (long)fs.size();
            ++nodes;
            if ((long)fs.size() > max) {
               max = (long)fs.size();
            }

            if (first) {
               min = (long)fs.size();
               first = false;
            } else if ((long)fs.size() < min) {
               min = (long)fs.size();
            }
         }
      }

      System.err.println("average: " + totalNodes / nodes);
      System.err.println("min: " + min);
      System.err.println("max: " + max);
   }
}
