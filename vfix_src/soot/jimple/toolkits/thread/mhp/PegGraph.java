package soot.jimple.toolkits.thread.mhp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import soot.Body;
import soot.Hierarchy;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.PAG;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.thread.mhp.stmt.JPegStmt;
import soot.jimple.toolkits.thread.mhp.stmt.StartStmt;
import soot.tagkit.StringTag;
import soot.tagkit.Tag;
import soot.toolkits.graph.CompleteUnitGraph;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.util.Chain;

public class PegGraph implements DirectedGraph {
   private List heads;
   private List tails;
   protected HashMap<Object, List> unitToSuccs;
   protected HashMap<Object, List> unitToPreds;
   private HashMap unitToPegMap;
   public HashMap<JPegStmt, List> startToThread;
   public HashMap startToAllocNodes;
   private HashMap<String, FlowSet> waitingNodes;
   private Map startToBeginNodes;
   private HashMap<String, Set<JPegStmt>> notifyAll;
   private Set methodsNeedingInlining;
   private boolean needInlining;
   private Set<List> synch;
   private Set<JPegStmt> specialJoin;
   private Body body;
   private Chain unitChain;
   private Chain mainPegChain;
   private FlowSet allNodes;
   private Map<String, FlowSet> monitor;
   private Set canNotBeCompacted;
   private Set threadAllocSites;
   private File logFile;
   private FileWriter fileWriter;
   private Set<Object> monitorObjs;
   private Set<Unit> exceHandlers;
   protected Map threadNo;
   protected Map threadNameToStart;
   protected Map<AllocNode, String> allocNodeToObj;
   protected Map<AllocNode, PegChain> allocNodeToThread;
   protected Map<JPegStmt, Chain> joinStmtToThread;
   Set allocNodes;
   List<List> inlineSites;
   Map<SootMethod, String> synchObj;
   Set multiRunAllocNodes;

   public PegGraph(CallGraph callGraph, Hierarchy hierarchy, PAG pag, Set<Object> methodsNeedingInlining, Set<AllocNode> allocNodes, List inlineSites, Map synchObj, Set<AllocNode> multiRunAllocNodes, Map allocNodeToObj, Body unitBody, SootMethod sm, boolean addExceptionEdges, boolean dontAddEdgeFromStmtBeforeAreaOfProtectionToCatchBlock) {
      this(callGraph, hierarchy, pag, methodsNeedingInlining, allocNodes, inlineSites, synchObj, multiRunAllocNodes, allocNodeToObj, unitBody, "main", sm, addExceptionEdges, dontAddEdgeFromStmtBeforeAreaOfProtectionToCatchBlock);
   }

   public PegGraph(CallGraph callGraph, Hierarchy hierarchy, PAG pag, Set methodsNeedingInlining, Set allocNodes, List<List> inlineSites, Map<SootMethod, String> synchObj, Set multiRunAllocNodes, Map<AllocNode, String> allocNodeToObj, Body unitBody, String threadName, SootMethod sm, boolean addExceEdge, boolean dontAddEdgeFromStmtBeforeAreaOfProtectionToCatchBlock) {
      this.allocNodeToObj = allocNodeToObj;
      this.multiRunAllocNodes = multiRunAllocNodes;
      this.synchObj = synchObj;
      this.inlineSites = inlineSites;
      this.allocNodes = allocNodes;
      this.methodsNeedingInlining = methodsNeedingInlining;
      this.logFile = new File("log.txt");

      try {
         this.fileWriter = new FileWriter(this.logFile);
      } catch (IOException var19) {
         System.err.println("Errors occur during create FileWriter !");
      }

      this.body = unitBody;
      this.synch = new HashSet();
      this.exceHandlers = new HashSet();
      this.needInlining = true;
      this.monitorObjs = new HashSet();
      this.startToBeginNodes = new HashMap();
      this.unitChain = this.body.getUnits();
      int size = this.unitChain.size();
      this.unitToSuccs = new HashMap(size * 2 + 1, 0.7F);
      this.unitToPreds = new HashMap(size * 2 + 1, 0.7F);
      this.unitToPegMap = new HashMap(size * 2 + 1, 0.7F);
      this.startToThread = new HashMap(size * 2 + 1, 0.7F);
      this.startToAllocNodes = new HashMap(size * 2 + 1, 0.7F);
      this.waitingNodes = new HashMap(size * 2 + 1, 0.7F);
      this.joinStmtToThread = new HashMap();
      this.threadNo = new HashMap();
      this.threadNameToStart = new HashMap();
      this.allocNodeToObj = new HashMap(size * 2 + 1, 0.7F);
      this.allocNodeToThread = new HashMap(size * 2 + 1, 0.7F);
      this.notifyAll = new HashMap(size * 2 + 1, 0.7F);
      Set methodsNeedingInlining = new HashSet();
      this.allNodes = new ArraySparseSet();
      this.canNotBeCompacted = new HashSet();
      this.threadAllocSites = new HashSet();
      this.specialJoin = new HashSet();
      new CompleteUnitGraph(this.body);
      this.mainPegChain = new PegChain(callGraph, hierarchy, pag, this.threadAllocSites, methodsNeedingInlining, allocNodes, inlineSites, synchObj, multiRunAllocNodes, allocNodeToObj, this.body, sm, threadName, true, this);
      this.buildSuccessor(this.mainPegChain);
      this.buildPredecessor(this.mainPegChain);
      this.addMonitorStmt();
      this.addTag();
      this.buildHeadsAndTails();

      try {
         this.fileWriter.flush();
         this.fileWriter.close();
      } catch (IOException var18) {
         System.err.println("Errors occur during close file  " + this.logFile.getName());
      }

   }

   protected Map getStartToBeginNodes() {
      return this.startToBeginNodes;
   }

   protected Map<JPegStmt, Chain> getJoinStmtToThread() {
      return this.joinStmtToThread;
   }

   protected Map getUnitToPegMap() {
      return this.unitToPegMap;
   }

   protected void addMonitorStmt() {
      if (this.synch.size() > 0) {
         Iterator it = this.synch.iterator();

         while(it.hasNext()) {
            List list = (List)it.next();
            JPegStmt node = (JPegStmt)list.get(0);
            JPegStmt enter = (JPegStmt)list.get(1);
            JPegStmt exit = (JPegStmt)list.get(2);
            if (this.mainPegChain.contains(node)) {
               this.mainPegChain.add(enter);
               this.mainPegChain.add(exit);
            } else {
               boolean find = false;
               Set maps = this.startToThread.entrySet();
               Iterator iter = maps.iterator();

               while(true) {
                  while(iter.hasNext()) {
                     Entry entry = (Entry)iter.next();
                     Object startNode = entry.getKey();
                     Iterator runIt = ((List)entry.getValue()).iterator();

                     while(runIt.hasNext()) {
                        Chain chain = (Chain)runIt.next();
                        if (chain.contains(node)) {
                           find = true;
                           chain.add(enter);
                           chain.add(exit);
                           break;
                        }
                     }
                  }

                  if (!find) {
                     throw new RuntimeException("fail to find stmt: " + node + " in chains!");
                  }
                  break;
               }
            }

            this.allNodes.add(enter);
            this.allNodes.add(exit);
            this.insertBefore(node, enter);
            this.insertAfter(node, exit);
         }
      }

   }

   private void insertBefore(JPegStmt node, JPegStmt enter) {
      List predOfBefore = new ArrayList();
      predOfBefore.addAll(this.getPredsOf(node));
      this.unitToPreds.put(enter, predOfBefore);
      Iterator predsIt = this.getPredsOf(node).iterator();

      while(predsIt.hasNext()) {
         Object pred = predsIt.next();
         List succ = this.getSuccsOf(pred);
         succ.remove(node);
         succ.add(enter);
      }

      List succOfBefore = new ArrayList();
      succOfBefore.add(node);
      this.unitToSuccs.put(enter, succOfBefore);
      List predOfNode = new ArrayList();
      predOfNode.add(enter);
      this.unitToPreds.put(node, predOfNode);
   }

   private void insertAfter(JPegStmt node, JPegStmt after) {
      List succOfAfter = new ArrayList();
      succOfAfter.addAll(this.getSuccsOf(node));
      this.unitToSuccs.put(after, succOfAfter);
      Iterator succsIt = this.getSuccsOf(node).iterator();

      while(succsIt.hasNext()) {
         Object succ = succsIt.next();
         List pred = this.getPredsOf(succ);
         pred.remove(node);
         pred.add(after);
      }

      List succOfNode = new ArrayList();
      succOfNode.add(after);
      this.unitToSuccs.put(node, succOfNode);
      List predOfAfter = new ArrayList();
      predOfAfter.add(node);
      this.unitToPreds.put(after, predOfAfter);
   }

   private void buildSuccessor(Chain pegChain) {
      HashMap unitToPeg = (HashMap)this.unitToPegMap.get(pegChain);
      Iterator pegIt = pegChain.iterator();
      JPegStmt currentNode = pegIt.hasNext() ? (JPegStmt)pegIt.next() : null;
      if (currentNode != null) {
         JPegStmt nextNode = pegIt.hasNext() ? (JPegStmt)pegIt.next() : null;
         ArrayList successors;
         if (currentNode.getName().equals("begin")) {
            successors = new ArrayList();
            successors.add(nextNode);
            this.unitToSuccs.put(currentNode, successors);
            currentNode = nextNode;
         }

         while(true) {
            label99:
            while(currentNode != null) {
               if (this.unitToSuccs.containsKey(currentNode) && !currentNode.getName().equals("wait")) {
                  currentNode = pegIt.hasNext() ? (JPegStmt)pegIt.next() : null;
               } else {
                  successors = new ArrayList();
                  Unit unit = currentNode.getUnit();
                  UnitGraph unitGraph = currentNode.getUnitGraph();
                  List unitSucc = unitGraph.getSuccsOf(unit);
                  Iterator succIt = unitSucc.iterator();

                  while(true) {
                     Unit un;
                     do {
                        if (!succIt.hasNext()) {
                           if (!currentNode.getName().equals("wait")) {
                              this.unitToSuccs.put(currentNode, successors);
                           } else {
                              while(!currentNode.getName().equals("notified-entry")) {
                                 currentNode = pegIt.hasNext() ? (JPegStmt)pegIt.next() : null;
                              }

                              this.unitToSuccs.put(currentNode, successors);
                           }

                           if (currentNode.getName().equals("start") && this.startToThread.containsKey(currentNode)) {
                              List runMethodChainList = (List)this.startToThread.get(currentNode);
                              Iterator possibleMethodIt = runMethodChainList.iterator();

                              while(possibleMethodIt.hasNext()) {
                                 Chain subChain = (Chain)possibleMethodIt.next();
                                 if (subChain != null) {
                                    this.buildSuccessor(subChain);
                                 } else {
                                    System.out.println("*********subgraph is null!!!");
                                 }
                              }
                           }

                           currentNode = pegIt.hasNext() ? (JPegStmt)pegIt.next() : null;
                           continue label99;
                        }

                        un = (Unit)succIt.next();
                     } while(unit instanceof ExitMonitorStmt && this.exceHandlers.contains(un));

                     if (unitToPeg.containsKey(un)) {
                        JPegStmt pp = (JPegStmt)((JPegStmt)unitToPeg.get(un));
                        if (pp != null && !successors.contains(pp)) {
                           successors.add(pp);
                        }
                     }
                  }
               }
            }

            return;
         }
      }
   }

   private void buildPredecessor(Chain pegChain) {
      JPegStmt s = null;
      Iterator unitIt = pegChain.iterator();

      while(unitIt.hasNext()) {
         s = (JPegStmt)unitIt.next();
         this.unitToPreds.put(s, new ArrayList());
      }

      Iterator unitIt = pegChain.iterator();

      label60:
      while(unitIt.hasNext()) {
         Object s = unitIt.next();
         if (!this.unitToSuccs.containsKey(s)) {
            throw new RuntimeException("unitToSuccs does not contains key" + s);
         }

         List succList = (List)this.unitToSuccs.get(s);
         Iterator succIt = succList.iterator();

         while(true) {
            JPegStmt successor;
            do {
               while(true) {
                  if (!succIt.hasNext()) {
                     continue label60;
                  }

                  successor = (JPegStmt)succIt.next();
                  List<Object> predList = (List)this.unitToPreds.get(successor);
                  if (predList != null && !predList.contains(s)) {
                     try {
                        predList.add(s);
                        break;
                     } catch (NullPointerException var11) {
                        System.out.println(s + "successor: " + successor);
                        throw var11;
                     }
                  }

                  System.err.println("predlist of " + s + " is null");
               }
            } while(!(successor instanceof StartStmt));

            List runMethodChainList = (List)this.startToThread.get(successor);
            if (runMethodChainList == null) {
               throw new RuntimeException("null runmehtodchain: \n" + successor.getUnit());
            }

            Iterator possibleMethodIt = runMethodChainList.iterator();

            while(possibleMethodIt.hasNext()) {
               Chain subChain = (Chain)possibleMethodIt.next();
               this.buildPredecessor(subChain);
            }
         }
      }

   }

   private void buildHeadsAndTails() {
      List tailList = new ArrayList();
      List headList = new ArrayList();
      Iterator tmpIt = this.mainPegChain.iterator();

      while(tmpIt.hasNext()) {
         JPegStmt s = (JPegStmt)tmpIt.next();
         List succs = (List)this.unitToSuccs.get(s);
         if (succs.size() == 0) {
            tailList.add(s);
         }

         if (!this.unitToPreds.containsKey(s)) {
            throw new RuntimeException("unitToPreds does not contain key: " + s);
         }

         List preds = (List)this.unitToPreds.get(s);
         if (preds.size() == 0) {
            headList.add(s);
         }
      }

      this.tails = tailList;
      this.heads = headList;

      Object var7;
      for(tmpIt = this.heads.iterator(); tmpIt.hasNext(); var7 = tmpIt.next()) {
      }

      this.buildPredecessor(this.mainPegChain);
   }

   public boolean addPeg(PegGraph pg, Chain chain) {
      if (!pg.removeBeginNode()) {
         return false;
      } else {
         Iterator mainIt = pg.mainIterator();

         while(mainIt.hasNext()) {
            JPegStmt s = (JPegStmt)mainIt.next();
            this.mainPegChain.addLast(s);
         }

         Iterator it = pg.iterator();

         while(it.hasNext()) {
            JPegStmt s = (JPegStmt)it.next();
            if (this.allNodes.contains(s)) {
               throw new RuntimeException("error! allNodes contains: " + s);
            }

            this.allNodes.add(s);
         }

         this.unitToSuccs.putAll(pg.getUnitToSuccs());
         this.unitToPreds.putAll(pg.getUnitToPreds());
         return true;
      }
   }

   private boolean removeBeginNode() {
      List heads = this.getHeads();
      if (heads.size() != 1) {
         return false;
      } else {
         JPegStmt head = (JPegStmt)heads.get(0);
         if (!head.getName().equals("begin")) {
            throw new RuntimeException("Error: the head is not begin node!");
         } else {
            heads.remove(0);
            Iterator succOfHeadIt = this.getSuccsOf(head).iterator();

            while(succOfHeadIt.hasNext()) {
               JPegStmt succOfHead = (JPegStmt)succOfHeadIt.next();
               this.unitToPreds.put(succOfHead, new ArrayList());
               heads.add(succOfHead);
            }

            if (!this.mainPegChain.remove(head)) {
               throw new RuntimeException("fail to remove begin node in from mainPegChain!");
            } else if (!this.allNodes.contains(head)) {
               throw new RuntimeException("fail to find begin node in FlowSet allNodes!");
            } else {
               this.allNodes.remove(head);
               if (this.unitToSuccs.containsKey(head)) {
                  this.unitToSuccs.remove(head);
               }

               return true;
            }
         }
      }
   }

   protected void buildSuccsForInlining(JPegStmt stmt, Chain chain, PegGraph inlinee) {
      Tag tag = (Tag)stmt.getTags().get(0);
      Iterator predIt = this.getPredsOf(stmt).iterator();
      Iterator headsIt = inlinee.getHeads().iterator();

      while(predIt.hasNext()) {
         JPegStmt pred = (JPegStmt)predIt.next();
         List succList = this.getSuccsOf(pred);
         int pos = succList.indexOf(stmt);
         succList.remove(pos);

         while(headsIt.hasNext()) {
            succList.add(headsIt.next());
         }

         this.unitToSuccs.put(pred, succList);
      }

      while(headsIt.hasNext()) {
         Object head = headsIt.next();
         List predsOfHeads = new ArrayList();
         predsOfHeads.addAll(this.getPredsOf(head));
         this.unitToPreds.put(head, predsOfHeads);
      }

      Iterator tailsIt = inlinee.getTails().iterator();

      while(tailsIt.hasNext()) {
         Iterator succIt = this.getSuccsOf(stmt).iterator();
         JPegStmt tail = (JPegStmt)tailsIt.next();
         List succList = null;
         if (this.unitToSuccs.containsKey(tail)) {
            succList = this.getSuccsOf(tail);
         } else {
            succList = new ArrayList();
         }

         JPegStmt succ;
         List predListOfSucc;
         for(; succIt.hasNext(); this.unitToPreds.put(succ, predListOfSucc)) {
            succ = (JPegStmt)succIt.next();
            ((List)succList).add(succ);
            predListOfSucc = this.getPredsOf(succ);
            if (predListOfSucc == null) {
               throw new RuntimeException("Error: predListOfSucc is null!");
            }

            if (predListOfSucc.size() != 0) {
               int pos = predListOfSucc.indexOf(stmt);
               if (pos > 0 || pos == 0) {
                  predListOfSucc.remove(pos);
               }
            }
         }

         this.unitToSuccs.put(tail, succList);
      }

      tailsIt = inlinee.getTails().iterator();

      while(true) {
         JPegStmt s;
         do {
            if (!tailsIt.hasNext()) {
               if (!this.allNodes.contains(stmt)) {
                  throw new RuntimeException("fail to find begin node in  allNodes!");
               }

               this.allNodes.remove(stmt);
               if (!chain.contains(stmt)) {
                  throw new RuntimeException("Error! Chain does not contains stmt (extending point)!");
               }

               if (!chain.remove(stmt)) {
                  throw new RuntimeException("fail to remove invoke stmt in from Chain!");
               }

               if (this.unitToSuccs.containsKey(stmt)) {
                  this.unitToSuccs.remove(stmt);
               }

               if (this.unitToPreds.containsKey(stmt)) {
                  this.unitToPreds.remove(stmt);
               }

               return;
            }

            s = (JPegStmt)tailsIt.next();
         } while(!this.unitToSuccs.containsKey(s));

         Iterator succIt = ((List)this.unitToSuccs.get(s)).iterator();

         while(succIt.hasNext()) {
            JPegStmt successor = (JPegStmt)succIt.next();
            List<JPegStmt> predList = (List)this.unitToPreds.get(successor);
            if (predList != null && !predList.contains(s)) {
               try {
                  predList.add(s);
               } catch (NullPointerException var14) {
                  System.out.println(s + "successor: " + successor);
                  throw var14;
               }
            }
         }
      }
   }

   protected void buildMaps(PegGraph pg) {
      this.exceHandlers.addAll(pg.getExceHandlers());
      this.startToThread.putAll(pg.getStartToThread());
      this.startToAllocNodes.putAll(pg.getStartToAllocNodes());
      this.startToBeginNodes.putAll(pg.getStartToBeginNodes());
      this.waitingNodes.putAll(pg.getWaitingNodes());
      this.notifyAll.putAll(pg.getNotifyAll());
      this.canNotBeCompacted.addAll(pg.getCanNotBeCompacted());
      this.synch.addAll(pg.getSynch());
      this.threadNameToStart.putAll(pg.getThreadNameToStart());
      this.specialJoin.addAll(pg.getSpecialJoin());
      this.joinStmtToThread.putAll(pg.getJoinStmtToThread());
      this.threadAllocSites.addAll(pg.getThreadAllocSites());
      this.allocNodeToThread.putAll(pg.getAllocNodeToThread());
   }

   protected void buildPreds() {
      this.buildPredecessor(this.mainPegChain);
      Set maps = this.getStartToThread().entrySet();
      Iterator iter = maps.iterator();

      while(iter.hasNext()) {
         Entry entry = (Entry)iter.next();
         List runMethodChainList = (List)entry.getValue();
         Iterator it = runMethodChainList.iterator();

         while(it.hasNext()) {
            Chain chain = (Chain)it.next();
            this.buildPredecessor(chain);
         }
      }

   }

   public void computeMonitorObjs() {
      Set maps = this.monitor.entrySet();
      Iterator iter = maps.iterator();

      while(iter.hasNext()) {
         Entry entry = (Entry)iter.next();
         FlowSet fs = (FlowSet)entry.getValue();
         Iterator it = fs.iterator();

         while(it.hasNext()) {
            Object obj = it.next();
            if (!this.monitorObjs.contains(obj)) {
               this.monitorObjs.add(obj);
            }
         }
      }

   }

   protected boolean getNeedInlining() {
      return this.needInlining;
   }

   protected FlowSet getAllNodes() {
      return this.allNodes;
   }

   protected HashMap getUnitToSuccs() {
      return this.unitToSuccs;
   }

   protected HashMap getUnitToPreds() {
      return this.unitToPreds;
   }

   public Body getBody() {
      return this.body;
   }

   public List getHeads() {
      return this.heads;
   }

   public List getTails() {
      return this.tails;
   }

   public List getPredsOf(Object s) {
      if (!this.unitToPreds.containsKey(s)) {
         throw new RuntimeException("Invalid stmt" + s);
      } else {
         return (List)this.unitToPreds.get(s);
      }
   }

   public List getSuccsOf(Object s) {
      return (List)(!this.unitToSuccs.containsKey(s) ? new ArrayList() : (List)this.unitToSuccs.get(s));
   }

   public Set getCanNotBeCompacted() {
      return this.canNotBeCompacted;
   }

   public int size() {
      return this.allNodes.size();
   }

   public Iterator mainIterator() {
      return this.mainPegChain.iterator();
   }

   public Iterator iterator() {
      return this.allNodes.iterator();
   }

   public String toString() {
      Iterator it = this.iterator();
      StringBuffer buf = new StringBuffer();

      while(it.hasNext()) {
         JPegStmt u = (JPegStmt)it.next();
         buf.append("u is: " + u + "\n");
         List l = new ArrayList();
         l.addAll(this.getPredsOf(u));
         buf.append("preds: " + l + "\n");
         l = new ArrayList();
         l.addAll(this.getSuccsOf(u));
         buf.append("succs: " + l + "\n");
      }

      return buf.toString();
   }

   protected Set<Unit> getExceHandlers() {
      return this.exceHandlers;
   }

   protected void setMonitor(Map<String, FlowSet> m) {
      this.monitor = m;
   }

   public Map<String, FlowSet> getMonitor() {
      return this.monitor;
   }

   public Set<Object> getMonitorObjs() {
      return this.monitorObjs;
   }

   protected Set getThreadAllocSites() {
      return this.threadAllocSites;
   }

   protected Set<JPegStmt> getSpecialJoin() {
      return this.specialJoin;
   }

   public HashSet<List> getSynch() {
      return (HashSet)this.synch;
   }

   public Map<JPegStmt, List> getStartToThread() {
      return this.startToThread;
   }

   public Map getStartToAllocNodes() {
      return this.startToAllocNodes;
   }

   protected Map<String, FlowSet> getWaitingNodes() {
      return this.waitingNodes;
   }

   public Map<String, Set<JPegStmt>> getNotifyAll() {
      return this.notifyAll;
   }

   protected Map<AllocNode, String> getAllocNodeToObj() {
      return this.allocNodeToObj;
   }

   public Map<AllocNode, PegChain> getAllocNodeToThread() {
      return this.allocNodeToThread;
   }

   protected Map getThreadNameToStart() {
      return this.threadNameToStart;
   }

   public PegChain getMainPegChain() {
      return (PegChain)this.mainPegChain;
   }

   public Set getMethodsNeedingInlining() {
      return this.methodsNeedingInlining;
   }

   protected void testIterator() {
      System.out.println("********begin test iterator*******");
      Iterator testIt = this.iterator();

      while(testIt.hasNext()) {
         System.out.println(testIt.next());
      }

      System.out.println("********end test iterator*******");
      System.out.println("=======size is: " + this.size());
   }

   public void testWaitingNodes() {
      System.out.println("------waiting---begin");
      Set maps = this.waitingNodes.entrySet();
      Iterator iter = maps.iterator();

      while(true) {
         FlowSet fs;
         do {
            if (!iter.hasNext()) {
               System.out.println("------------waitingnodes---ends--------");
               return;
            }

            Entry entry = (Entry)iter.next();
            System.out.println("---key=  " + entry.getKey());
            fs = (FlowSet)entry.getValue();
         } while(fs.size() <= 0);

         System.out.println("**waiting nodes set:");
         Iterator it = fs.iterator();

         while(it.hasNext()) {
            JPegStmt unit = (JPegStmt)it.next();
            System.out.println(unit.toString());
         }
      }
   }

   protected void testStartToThread() {
      System.out.println("=====test startToThread ");
      Set maps = this.startToThread.entrySet();
      Iterator iter = maps.iterator();

      while(iter.hasNext()) {
         Entry entry = (Entry)iter.next();
         JPegStmt key = (JPegStmt)entry.getKey();
         Tag tag = (Tag)key.getTags().get(0);
         System.out.println("---key=  " + tag + " " + key);
      }

      System.out.println("=========startToThread--ends--------");
   }

   protected void testUnitToPeg(HashMap unitToPeg) {
      System.out.println("=====test unitToPeg ");
      Set maps = unitToPeg.entrySet();
      Iterator iter = maps.iterator();

      while(iter.hasNext()) {
         Entry entry = (Entry)iter.next();
         System.out.println("---key=  " + entry.getKey());
         JPegStmt s = (JPegStmt)entry.getValue();
         System.out.println("--value= " + s);
      }

      System.out.println("=========unitToPeg--ends--------");
   }

   protected void testUnitToSucc() {
      System.out.println("=====test unitToSucc ");
      Set maps = this.unitToSuccs.entrySet();
      Iterator iter = maps.iterator();

      while(true) {
         List list;
         do {
            if (!iter.hasNext()) {
               System.out.println("=========unitToSucc--ends--------");
               return;
            }

            Entry entry = (Entry)iter.next();
            JPegStmt key = (JPegStmt)entry.getKey();
            Tag tag = (Tag)key.getTags().get(0);
            System.out.println("---key=  " + tag + " " + key);
            list = (List)entry.getValue();
         } while(list.size() <= 0);

         System.out.println("**succ set: size: " + list.size());
         Iterator it = list.iterator();

         while(it.hasNext()) {
            JPegStmt stmt = (JPegStmt)it.next();
            Tag tag1 = (Tag)stmt.getTags().get(0);
            System.out.println(tag1 + " " + stmt);
         }
      }
   }

   protected void testUnitToPred() {
      System.out.println("=====test unitToPred ");
      Set maps = this.unitToPreds.entrySet();
      Iterator iter = maps.iterator();

      while(iter.hasNext()) {
         Entry entry = (Entry)iter.next();
         JPegStmt key = (JPegStmt)entry.getKey();
         Tag tag = (Tag)key.getTags().get(0);
         System.out.println("---key=  " + tag + " " + key);
         List list = (List)entry.getValue();
         System.out.println("**pred set: size: " + list.size());
         Iterator it = list.iterator();

         while(it.hasNext()) {
            JPegStmt stmt = (JPegStmt)it.next();
            Tag tag1 = (Tag)stmt.getTags().get(0);
            System.out.println(tag1 + " " + stmt);
         }
      }

      System.out.println("=========unitToPred--ends--------");
   }

   protected void addTag() {
      Iterator it = this.iterator();

      while(it.hasNext()) {
         JPegStmt stmt = (JPegStmt)it.next();
         int count = Counter.getTagNo();
         StringTag t = new StringTag(Integer.toString(count));
         stmt.addTag(t);
      }

   }

   protected void testSynch() {
      Iterator<List> it = this.synch.iterator();
      System.out.println("========test synch======");

      while(it.hasNext()) {
         System.out.println(it.next());
      }

      System.out.println("========end test synch======");
   }

   protected void testThreadNameToStart() {
      System.out.println("=====test ThreadNameToStart");
      Set maps = this.threadNameToStart.entrySet();
      Iterator iter = maps.iterator();

      while(iter.hasNext()) {
         Entry entry = (Entry)iter.next();
         Object key = entry.getKey();
         System.out.println("---key=  " + key);
         JPegStmt stmt = (JPegStmt)entry.getValue();
         Tag tag1 = (Tag)stmt.getTags().get(0);
         System.out.println("value: " + tag1 + " " + stmt);
      }

      System.out.println("=========ThreadNameToStart--ends--------");
   }

   protected void testJoinStmtToThread() {
      System.out.println("=====test JoinStmtToThread");
      Set maps = this.threadNameToStart.entrySet();
      Iterator iter = maps.iterator();

      while(iter.hasNext()) {
         Entry entry = (Entry)iter.next();
         Object key = entry.getKey();
         System.out.println("---key=  " + key);
         System.out.println("value: " + entry.getValue());
      }

      System.out.println("=========JoinStmtToThread--ends--------");
   }

   protected void testPegChain(Chain chain) {
      System.out.println("******** chain********");
      Iterator it = chain.iterator();

      while(it.hasNext()) {
         JPegStmt stmt = (JPegStmt)it.next();
         System.out.println(stmt.toString());
      }

   }

   protected void computeEdgeAndThreadNo() {
      Iterator it = this.iterator();

      int numberOfEdge;
      List succList;
      for(numberOfEdge = 0; it.hasNext(); numberOfEdge += succList.size()) {
         succList = this.getSuccsOf(it.next());
      }

      numberOfEdge += this.startToThread.size();
      System.err.println("**number of edges: " + numberOfEdge);
      System.err.println("**number of threads: " + (this.startToThread.size() + 1));
   }

   protected void testList(List list) {
      Iterator listIt = list.iterator();

      while(listIt.hasNext()) {
         System.out.println(listIt.next());
      }

   }

   protected void testSet(Set set, String name) {
      System.out.println("$test set " + name);
      Iterator setIt = set.iterator();

      while(setIt.hasNext()) {
         Object s = setIt.next();
         System.out.println(s);
      }

   }

   public void testMonitor() {
      System.out.println("=====test monitor size: " + this.monitor.size());
      Set maps = this.monitor.entrySet();
      Iterator iter = maps.iterator();

      label44:
      while(true) {
         FlowSet list;
         do {
            if (!iter.hasNext()) {
               System.out.println("=========monitor--ends--------");
               return;
            }

            Entry entry = (Entry)iter.next();
            String key = (String)entry.getKey();
            System.out.println("---key=  " + key);
            list = (FlowSet)entry.getValue();
         } while(list.size() <= 0);

         System.out.println("**set:  " + list.size());
         Iterator it = list.iterator();

         while(true) {
            while(true) {
               if (!it.hasNext()) {
                  continue label44;
               }

               Object obj = it.next();
               if (obj instanceof JPegStmt) {
                  JPegStmt stmt = (JPegStmt)obj;
                  Tag tag1 = (Tag)stmt.getTags().get(0);
                  System.out.println(tag1 + " " + stmt);
               } else {
                  System.out.println("---list---");
                  Iterator listIt = ((List)obj).iterator();

                  while(listIt.hasNext()) {
                     Object oo = listIt.next();
                     if (oo instanceof JPegStmt) {
                        JPegStmt unit = (JPegStmt)oo;
                        Tag tag = (Tag)unit.getTags().get(0);
                        System.out.println(tag + " " + unit);
                     } else {
                        System.out.println(oo);
                     }
                  }

                  System.out.println("---list--end-");
               }
            }
         }
      }
   }
}
