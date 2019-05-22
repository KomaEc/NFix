package soot.jimple.toolkits.thread.mhp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.Hierarchy;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.RefType;
import soot.SootClass;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.MonitorStmt;
import soot.jimple.NewExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.internal.JIdentityStmt;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.thread.mhp.stmt.BeginStmt;
import soot.jimple.toolkits.thread.mhp.stmt.JPegStmt;
import soot.jimple.toolkits.thread.mhp.stmt.JoinStmt;
import soot.jimple.toolkits.thread.mhp.stmt.MonitorEntryStmt;
import soot.jimple.toolkits.thread.mhp.stmt.MonitorExitStmt;
import soot.jimple.toolkits.thread.mhp.stmt.NotifiedEntryStmt;
import soot.jimple.toolkits.thread.mhp.stmt.NotifyAllStmt;
import soot.jimple.toolkits.thread.mhp.stmt.NotifyStmt;
import soot.jimple.toolkits.thread.mhp.stmt.OtherStmt;
import soot.jimple.toolkits.thread.mhp.stmt.StartStmt;
import soot.jimple.toolkits.thread.mhp.stmt.WaitStmt;
import soot.jimple.toolkits.thread.mhp.stmt.WaitingStmt;
import soot.tagkit.StringTag;
import soot.toolkits.graph.CompleteUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.util.Chain;
import soot.util.HashChain;

public class PegChain extends HashChain {
   CallGraph callGraph;
   private final List heads = new ArrayList();
   private final List tails = new ArrayList();
   private final FlowSet pegNodes = new ArraySparseSet();
   private final Map<Unit, JPegStmt> unitToPeg = new HashMap();
   private final Map<String, FlowSet> waitingNodes;
   private final PegGraph pg;
   private final Set<List<Object>> joinNeedReconsidered = new HashSet();
   public Body body;
   Hierarchy hierarchy;
   PAG pag;
   Set threadAllocSites;
   Set methodsNeedingInlining;
   Set allocNodes;
   List<List> inlineSites;
   Map<SootMethod, String> synchObj;
   Set multiRunAllocNodes;
   Map<AllocNode, String> allocNodeToObj;

   PegChain(CallGraph callGraph, Hierarchy hierarchy, PAG pag, Set threadAllocSites, Set methodsNeedingInlining, Set allocNodes, List<List> inlineSites, Map<SootMethod, String> synchObj, Set multiRunAllocNodes, Map<AllocNode, String> allocNodeToObj, Body unitBody, SootMethod sm, String threadName, boolean addBeginNode, PegGraph pegGraph) {
      this.allocNodeToObj = allocNodeToObj;
      this.multiRunAllocNodes = multiRunAllocNodes;
      this.synchObj = synchObj;
      this.inlineSites = inlineSites;
      this.allocNodes = allocNodes;
      this.methodsNeedingInlining = methodsNeedingInlining;
      this.threadAllocSites = threadAllocSites;
      this.hierarchy = hierarchy;
      this.pag = pag;
      this.callGraph = callGraph;
      this.body = unitBody;
      this.pg = pegGraph;
      this.waitingNodes = pegGraph.getWaitingNodes();
      Iterator trapIt = unitBody.getTraps().iterator();
      Set exceHandlers = this.pg.getExceHandlers();

      while(trapIt.hasNext()) {
         Trap trap = (Trap)trapIt.next();
         Unit handlerUnit = trap.getHandlerUnit();
         exceHandlers.add(handlerUnit);
      }

      UnitGraph graph = new CompleteUnitGraph(unitBody);
      Iterator unitIt = graph.iterator();
      if (addBeginNode) {
         JPegStmt beginStmt = new BeginStmt("*", threadName, graph, sm);
         this.pg.getCanNotBeCompacted().add(beginStmt);
         this.addNode(beginStmt);
         this.heads.add(beginStmt);
      }

      Iterator it = graph.getHeads().iterator();

      while(it.hasNext()) {
         Object head = it.next();
         Set<Unit> gray = new HashSet();
         LinkedList<Object> queue = new LinkedList();
         queue.add(head);
         this.visit((Unit)queue.getFirst(), graph, sm, threadName, addBeginNode);

         while(queue.size() > 0) {
            Unit root = (Unit)queue.getFirst();
            Iterator succsIt = graph.getSuccsOf(root).iterator();

            while(succsIt.hasNext()) {
               Unit succ = (Unit)succsIt.next();
               if (!gray.contains(succ)) {
                  gray.add(succ);
                  queue.addLast(succ);
                  this.visit(succ, graph, sm, threadName, addBeginNode);
               }
            }

            queue.remove(root);
         }
      }

      this.postHandleJoinStmt();
      this.pg.getUnitToPegMap().put(this, this.unitToPeg);
   }

   private void visit(Unit unit, UnitGraph graph, SootMethod sm, String threadName, boolean addBeginNode) {
      if (unit instanceof MonitorStmt) {
         Value value = ((MonitorStmt)unit).getOp();
         if (value instanceof Local) {
            Type type = ((Local)value).getType();
            if (type instanceof RefType) {
               SootClass sc = ((RefType)type).getSootClass();
               String objName;
               if (unit instanceof EnterMonitorStmt) {
                  objName = this.makeObjName(value, type, unit);
                  JPegStmt pegStmt = new MonitorEntryStmt(objName, threadName, unit, graph, sm);
                  this.addAndPutNonCompacted(unit, pegStmt);
                  return;
               }

               if (unit instanceof ExitMonitorStmt) {
                  objName = this.makeObjName(value, type, unit);
                  JPegStmt pegStmt = new MonitorExitStmt(objName, threadName, unit, graph, sm);
                  this.addAndPutNonCompacted(unit, pegStmt);
                  return;
               }
            }
         }
      }

      if (((Stmt)unit).containsInvokeExpr()) {
         Value invokeExpr = ((Stmt)unit).getInvokeExpr();
         SootMethod method = ((InvokeExpr)invokeExpr).getMethod();
         String name = method.getName();
         Value value = null;
         Type type = null;
         List paras = method.getParameterTypes();
         String objName = null;
         if (invokeExpr instanceof InstanceInvokeExpr) {
            value = ((InstanceInvokeExpr)invokeExpr).getBase();
            if (value instanceof Local) {
               type = ((Local)value).getType();
               if (type instanceof RefType) {
                  SootClass sc = ((RefType)type).getSootClass();
                  objName = sc.getName();
               }
            }
         } else if (!(invokeExpr instanceof StaticInvokeExpr)) {
            throw new RuntimeException("Error: new type of invokeExpre: " + invokeExpr);
         }

         boolean find = false;
         List targetList;
         if (method.getName().equals("start")) {
            targetList = this.hierarchy.getSuperclassesOfIncluding(method.getDeclaringClass());
            Iterator it = targetList.iterator();

            while(it.hasNext()) {
               String className = ((SootClass)it.next()).getName();
               if (className.equals("java.lang.Thread")) {
                  find = true;
                  break;
               }
            }
         }

         if (method.getName().equals("run") && method.getDeclaringClass().getName().equals("java.lang.Runnable")) {
            find = true;
         }

         if (name.equals("wait") && (paras.size() == 0 || paras.size() == 1 && (Type)paras.get(0) instanceof LongType || paras.size() == 2 && (Type)paras.get(0) instanceof LongType && (Type)paras.get(1) instanceof IntType)) {
            objName = this.makeObjName(value, type, unit);
            this.transformWaitNode(objName, name, threadName, unit, graph, sm);
         } else if ((name.equals("start") || name.equals("run")) && find) {
            targetList = null;
            PointsToSetInternal pts = (PointsToSetInternal)this.pag.reachingObjects((Local)value);
            targetList = this.findMayAlias(pts, unit);
            JPegStmt pegStmt = new StartStmt(value.toString(), threadName, unit, graph, sm);
            if (this.pg.getStartToThread().containsKey(pegStmt)) {
               throw new RuntimeException("map startToThread contain duplicated start() method call");
            }

            this.pg.getCanNotBeCompacted().add(pegStmt);
            this.addAndPut(unit, pegStmt);
            List<PegChain> runMethodChainList = new ArrayList();
            List<AllocNode> threadAllocNodesList = new ArrayList();
            if (targetList.size() < 1) {
               throw new RuntimeException("The may alias set of " + unit + "is empty!");
            }

            Iterator mayAliasIt = targetList.iterator();

            while(mayAliasIt.hasNext()) {
               AllocNode allocNode = (AllocNode)mayAliasIt.next();
               RefType refType = ((NewExpr)allocNode.getNewExpr()).getBaseType();
               SootClass maySootClass = refType.getSootClass();
               SootMethod meth = this.hierarchy.resolveConcreteDispatch(maySootClass, method.getDeclaringClass().getMethodByName("run"));
               Body mBody = meth.getActiveBody();
               int threadNo = Counter.getThreadNo();
               String callerName = "thread" + threadNo;
               this.pg.getThreadNameToStart().put(callerName, pegStmt);
               PegChain newChain = new PegChain(this.callGraph, this.hierarchy, this.pag, this.threadAllocSites, this.methodsNeedingInlining, this.allocNodes, this.inlineSites, this.synchObj, this.multiRunAllocNodes, this.allocNodeToObj, mBody, sm, callerName, true, this.pg);
               this.pg.getAllocNodeToThread().put(allocNode, newChain);
               runMethodChainList.add(newChain);
               threadAllocNodesList.add(allocNode);
            }

            this.pg.getStartToThread().put(pegStmt, runMethodChainList);
            this.pg.getStartToAllocNodes().put(pegStmt, threadAllocNodesList);
         } else if (name.equals("join") && method.getDeclaringClass().getName().equals("java.lang.Thread")) {
            PointsToSetInternal pts = (PointsToSetInternal)this.pag.reachingObjects((Local)value);
            List<AllocNode> mayAlias = this.findMayAlias(pts, unit);
            if (mayAlias.size() != 1) {
               if (mayAlias.size() < 1) {
                  throw new RuntimeException("==threadAllocaSits==\n" + this.threadAllocSites.toString());
               }

               JPegStmt pegStmt = new JoinStmt(value.toString(), threadName, unit, graph, sm);
               this.addAndPutNonCompacted(unit, pegStmt);
               this.pg.getSpecialJoin().add(pegStmt);
            } else {
               Iterator mayAliasIt = mayAlias.iterator();

               while(mayAliasIt.hasNext()) {
                  AllocNode allocNode = (AllocNode)mayAliasIt.next();
                  JPegStmt pegStmt = new JoinStmt(value.toString(), threadName, unit, graph, sm);
                  if (!this.pg.getAllocNodeToThread().containsKey(allocNode)) {
                     List<Object> list = new ArrayList();
                     list.add(pegStmt);
                     list.add(allocNode);
                     list.add(unit);
                     this.joinNeedReconsidered.add(list);
                  } else {
                     Chain thread = (Chain)this.pg.getAllocNodeToThread().get(allocNode);
                     this.addAndPutNonCompacted(unit, pegStmt);
                     this.pg.getJoinStmtToThread().put(pegStmt, thread);
                  }
               }
            }
         } else if (name.equals("notifyAll") && paras.size() == 0) {
            objName = this.makeObjName(value, type, unit);
            JPegStmt pegStmt = new NotifyAllStmt(objName, threadName, unit, graph, sm);
            this.addAndPutNonCompacted(unit, pegStmt);
            if (this.pg.getNotifyAll().containsKey(objName)) {
               Set<JPegStmt> notifyAllSet = (Set)this.pg.getNotifyAll().get(objName);
               notifyAllSet.add(pegStmt);
               this.pg.getNotifyAll().put(objName, notifyAllSet);
            } else {
               Set<JPegStmt> notifyAllSet = new HashSet();
               notifyAllSet.add(pegStmt);
               this.pg.getNotifyAll().put(objName, notifyAllSet);
            }
         } else if (name.equals("notify") && paras.size() == 0 && method.getDeclaringClass().getName().equals("java.lang.Thread")) {
            objName = this.makeObjName(value, type, unit);
            JPegStmt pegStmt = new NotifyStmt(objName, threadName, unit, graph, sm);
            this.addAndPutNonCompacted(unit, pegStmt);
         } else if (method.isConcrete() && !method.getDeclaringClass().isLibraryClass()) {
            new LinkedList();
            SootMethod targetMethod = null;
            if (invokeExpr instanceof StaticInvokeExpr) {
               targetMethod = method;
            } else {
               TargetMethodsFinder tmd = new TargetMethodsFinder();
               targetList = tmd.find(unit, this.callGraph, true, false);
               if (targetList.size() > 1) {
                  System.out.println("target: " + targetList);
                  System.out.println("unit is: " + unit);
                  System.err.println("exit because target is bigger than 1.");
                  System.exit(1);
               } else if (targetList.size() < 1) {
                  System.err.println("targetList size <1");
               } else {
                  targetMethod = (SootMethod)targetList.get(0);
               }
            }

            if (this.methodsNeedingInlining == null) {
               System.err.println("methodsNeedingInlining is null at " + unit);
            } else if (targetMethod == null) {
               System.err.println("targetMethod is null at " + unit);
            } else if (this.methodsNeedingInlining.contains(targetMethod)) {
               this.inlineMethod(targetMethod, objName, name, threadName, unit, graph, sm);
            } else {
               JPegStmt pegStmt = new OtherStmt(objName, name, threadName, unit, graph, sm);
               this.addAndPut(unit, pegStmt);
            }
         } else {
            JPegStmt pegStmt = new OtherStmt(objName, name, threadName, unit, graph, sm);
            this.addAndPut(unit, pegStmt);
         }
      } else {
         this.newAndAddElement(unit, graph, threadName, sm);
      }

   }

   private void transformWaitNode(String objName, String name, String threadName, Unit unit, UnitGraph graph, SootMethod sm) {
      JPegStmt pegStmt = new WaitStmt(objName, threadName, unit, graph, sm);
      this.addAndPutNonCompacted(unit, pegStmt);
      JPegStmt pegWaiting = new WaitingStmt(objName, threadName, sm);
      this.pg.getCanNotBeCompacted().add(pegWaiting);
      this.addNode(pegWaiting);
      if (this.waitingNodes.containsKey(objName)) {
         FlowSet waitingNodesSet = (FlowSet)this.waitingNodes.get(objName);
         if (!waitingNodesSet.contains(pegWaiting)) {
            waitingNodesSet.add(pegWaiting);
            this.waitingNodes.put(pegWaiting.getObject(), waitingNodesSet);
         }
      } else {
         FlowSet waitingNodesSet = new ArraySparseSet();
         waitingNodesSet.add(pegWaiting);
         this.waitingNodes.put(pegWaiting.getObject(), waitingNodesSet);
      }

      List successors = new ArrayList();
      successors.add(pegWaiting);
      this.pg.getUnitToSuccs().put(pegStmt, successors);
      JPegStmt pegNotify = new NotifiedEntryStmt(objName, threadName, sm);
      this.pg.getCanNotBeCompacted().add(pegNotify);
      this.addNode(pegNotify);
      List successors = new ArrayList();
      successors.add(pegNotify);
      this.pg.getUnitToSuccs().put(pegWaiting, successors);
   }

   private List<AllocNode> findMayAlias(PointsToSetInternal pts, Unit unit) {
      List<AllocNode> list = new ArrayList();
      Iterator it = this.makePtsIterator(pts);

      while(it.hasNext()) {
         AllocNode obj = (AllocNode)it.next();
         list.add(obj);
      }

      return list;
   }

   private void inlineMethod(SootMethod targetMethod, String objName, String name, String threadName, Unit unit, UnitGraph graph, SootMethod sm) {
      Body unitBody = targetMethod.getActiveBody();
      JPegStmt pegStmt = new OtherStmt(objName, name, threadName, unit, graph, sm);
      if (targetMethod.isSynchronized()) {
         String synchObj = this.findSynchObj(targetMethod);
         JPegStmt enter = new MonitorEntryStmt(synchObj, threadName, graph, sm);
         JPegStmt exit = new MonitorExitStmt(synchObj, threadName, graph, sm);
         this.pg.getCanNotBeCompacted().add(enter);
         this.pg.getCanNotBeCompacted().add(exit);
         List list = new ArrayList();
         list.add(pegStmt);
         list.add(enter);
         list.add(exit);
         this.pg.getSynch().add(list);
      }

      this.addAndPut(unit, pegStmt);
      PegGraph pG = new PegGraph(this.callGraph, this.hierarchy, this.pag, this.methodsNeedingInlining, this.allocNodes, this.inlineSites, this.synchObj, this.multiRunAllocNodes, this.allocNodeToObj, unitBody, threadName, targetMethod, true, false);
      List list = new ArrayList();
      list.add(pegStmt);
      list.add(this);
      list.add(this.pg);
      list.add(pG);
      this.inlineSites.add(list);
   }

   private String findSynchObj(SootMethod targetMethod) {
      if (this.synchObj.containsKey(targetMethod)) {
         return (String)this.synchObj.get(targetMethod);
      } else {
         String objName = null;
         if (targetMethod.isStatic()) {
            objName = targetMethod.getDeclaringClass().getName();
         } else {
            Iterator it = targetMethod.getActiveBody().getUnits().iterator();

            while(it.hasNext()) {
               Object obj = it.next();
               if (obj instanceof JIdentityStmt) {
                  Value thisRef = ((JIdentityStmt)obj).getLeftOp();
                  if (thisRef instanceof Local) {
                     Type type = ((Local)thisRef).getType();
                     if (type instanceof RefType) {
                        objName = this.makeObjName(thisRef, type, (Unit)obj);
                        this.synchObj.put(targetMethod, objName);
                        break;
                     }
                  }
               }
            }
         }

         return objName;
      }
   }

   private void addNode(JPegStmt stmt) {
      this.addLast(stmt);
      this.pegNodes.add(stmt);
      this.pg.getAllNodes().add(stmt);
   }

   private void addAndPut(Unit unit, JPegStmt stmt) {
      this.unitToPeg.put(unit, stmt);
      this.addNode(stmt);
   }

   private void addAndPutNonCompacted(Unit unit, JPegStmt stmt) {
      this.pg.getCanNotBeCompacted().add(stmt);
      this.addAndPut(unit, stmt);
   }

   private void newAndAddElement(Unit unit, UnitGraph graph, String threadName, SootMethod sm) {
      JPegStmt pegStmt = new OtherStmt("*", unit.toString(), threadName, unit, graph, sm);
      this.addAndPut(unit, pegStmt);
   }

   public List getHeads() {
      return this.heads;
   }

   public List getTails() {
      return this.tails;
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

   private Iterator<AllocNode> makePtsIterator(PointsToSetInternal pts) {
      final HashSet<AllocNode> ret = new HashSet();
      pts.forall(new P2SetVisitor() {
         public void visit(Node n) {
            ret.add((AllocNode)n);
         }
      });
      return ret.iterator();
   }

   private void postHandleJoinStmt() {
      Iterator it = this.joinNeedReconsidered.iterator();

      while(it.hasNext()) {
         List list = (List)it.next();
         JPegStmt pegStmt = (JPegStmt)list.get(0);
         AllocNode allocNode = (AllocNode)list.get(1);
         Unit unit = (Unit)list.get(2);
         if (!this.pg.getAllocNodeToThread().containsKey(allocNode)) {
            throw new RuntimeException("allocNodeToThread does not contains key: " + allocNode);
         }

         Chain thread = (Chain)this.pg.getAllocNodeToThread().get(allocNode);
         this.addAndPutNonCompacted(unit, pegStmt);
         this.pg.getJoinStmtToThread().put(pegStmt, thread);
      }

   }

   private String makeObjName(Value value, Type type, Unit unit) {
      PointsToSetInternal pts = (PointsToSetInternal)this.pag.reachingObjects((Local)value);
      List<AllocNode> mayAlias = this.findMayAlias(pts, unit);
      String objName = null;
      if (this.allocNodeToObj == null) {
         throw new RuntimeException("allocNodeToObj is null!");
      } else {
         AllocNode an;
         if (mayAlias.size() == 1) {
            an = (AllocNode)mayAlias.get(0);
            if (this.allocNodeToObj.containsKey(an)) {
               objName = (String)this.allocNodeToObj.get(an);
            } else {
               objName = "obj" + Counter.getObjNo();
               this.allocNodeToObj.put(an, objName);
            }
         } else {
            an = (AllocNode)mayAlias.get(0);
            if (this.allocNodeToObj.containsKey(an)) {
               objName = "MULTI" + (String)this.allocNodeToObj.get(an);
            } else {
               objName = "MULTIobj" + Counter.getObjNo();
               this.allocNodeToObj.put(an, objName);
            }
         }

         if (objName == null) {
            throw new RuntimeException("Can not find target object for " + unit);
         } else {
            return objName;
         }
      }
   }

   protected Map<String, FlowSet> getWaitingNodes() {
      return this.waitingNodes;
   }

   protected void testChain() {
      System.out.println("******** chain********");
      Iterator it = this.iterator();

      while(it.hasNext()) {
         JPegStmt stmt = (JPegStmt)it.next();
         System.out.println(stmt.toString());
      }

   }
}
