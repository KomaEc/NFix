package corg.vfix.sa.vfg.build;

import corg.vfix.fl.spectrum.FLSpectrum;
import corg.vfix.fl.stack.StackFrame;
import corg.vfix.fl.stack.StackTrace;
import corg.vfix.sa.analysis.AliasQuery;
import corg.vfix.sa.cg.CG;
import corg.vfix.sa.cg.CGEdge;
import corg.vfix.sa.cg.CGNode;
import corg.vfix.sa.plot.CGPlot;
import corg.vfix.sa.vfg.NodeType;
import corg.vfix.sa.vfg.VFG;
import corg.vfix.sa.vfg.VFGEdge;
import corg.vfix.sa.vfg.VFGNode;
import corg.vfix.sa.vfg.build.reach.InterReach;
import java.util.ArrayList;
import java.util.Iterator;
import soot.Local;
import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LengthExpr;
import soot.jimple.NullConstant;
import soot.jimple.ParameterRef;
import soot.jimple.Stmt;
import soot.jimple.ThisRef;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

public class InterVFGConstructor {
   private static CallGraph sootcg;
   private static CG callGraph;

   public static void build(VFG vfg) throws Exception {
      sootcg = Scene.v().getCallGraph();
      setSootMethodInTrace();
      setStmtInTrace();
      callGraph = buildCG();
      CGPlot.plot(callGraph);
      updateSrcAndSink(vfg);
      removeParameterStmt(vfg);
      removeTestNode(vfg);
   }

   private static void removeTestNode(VFG vfg) {
      SootClass mainClass = Scene.v().getMainClass();
      ArrayList<VFGNode> testNodes = new ArrayList();
      ArrayList<VFGEdge> testEdges = new ArrayList();
      Iterator var5 = vfg.getNodes().iterator();

      while(var5.hasNext()) {
         VFGNode node = (VFGNode)var5.next();
         if (node.getMethod().getDeclaringClass().equals(mainClass)) {
            testNodes.add(node);
         }
      }

      var5 = vfg.getEdges().iterator();

      while(true) {
         VFGNode from;
         VFGNode to;
         VFGEdge edge;
         do {
            if (!var5.hasNext()) {
               vfg.removeNodes(testNodes);
               vfg.removeEdges(testEdges);
               return;
            }

            edge = (VFGEdge)var5.next();
            from = edge.getFrom();
            to = edge.getTo();
         } while(!testNodes.contains(from) && !testNodes.contains(to));

         testEdges.add(edge);
      }
   }

   private static void removeParameterStmt(VFG vfg) {
      ArrayList<VFGNode> pNodes = new ArrayList();
      ArrayList<VFGEdge> pEdges = new ArrayList();
      Iterator var4 = vfg.getNodes().iterator();

      while(var4.hasNext()) {
         VFGNode node = (VFGNode)var4.next();
         if (isParameterRef(node.getStmt()) && !pNodes.contains(node)) {
            pNodes.add(node);
         }
      }

      var4 = vfg.getEdges().iterator();

      while(true) {
         VFGNode from;
         VFGNode to;
         VFGEdge edge1;
         do {
            if (!var4.hasNext()) {
               var4 = pEdges.iterator();

               while(var4.hasNext()) {
                  edge1 = (VFGEdge)var4.next();
                  Iterator var11 = pEdges.iterator();

                  while(var11.hasNext()) {
                     VFGEdge edge2 = (VFGEdge)var11.next();
                     if (!edge1.equals(edge2)) {
                        VFGNode to = edge1.getTo();
                        VFGNode from = edge2.getFrom();
                        if (to.equals(from) && pNodes.contains(to)) {
                           vfg.addEdge(edge1.getFrom(), edge2.getTo(), edge1.getValue(), 1);
                        }
                     }
                  }
               }

               vfg.removeEdges(pEdges);
               vfg.removeNodes(pNodes);
               return;
            }

            edge1 = (VFGEdge)var4.next();
            from = edge1.getFrom();
            to = edge1.getTo();
         } while(!pNodes.contains(from) && !pNodes.contains(to));

         pEdges.add(edge1);
      }
   }

   private static void updateSinkAndConnectVFG(VFGNode src, Value targetValue, VFG vfg) throws Exception {
      ArrayList<VFGNode> useNodes = getNewUses(targetValue);
      Iterator var5 = useNodes.iterator();

      while(var5.hasNext()) {
         VFGNode newUse = (VFGNode)var5.next();
         if (!vfg.containNode(newUse) && InterReach.immediateReach(src, newUse, targetValue, callGraph, useNodes)) {
            VFG vfg2 = IntraVFGConstructor.buildFromSrc(newUse.getMethod(), newUse.getStmt());
            VFGNode node2 = vfg2.getNodeByStmt(newUse.getStmt());
            connectVFG(vfg, vfg2, src, node2, targetValue);
            NodeType.setType(vfg);
         }
      }

   }

   private static void connectVFG(VFG vfg1, VFG vfg2, VFGNode node1, VFGNode node2, Value value) {
      Iterator var6 = vfg2.getNodes().iterator();

      while(var6.hasNext()) {
         VFGNode node = (VFGNode)var6.next();
         vfg1.addNode(node);
      }

      var6 = vfg2.getEdges().iterator();

      while(var6.hasNext()) {
         VFGEdge edge = (VFGEdge)var6.next();
         vfg1.addEdge(edge);
      }

      vfg1.addEdge(node1, node2, value, 1);
   }

   private static void updateSrcAndConnectVFG(VFGNode caller, VFGNode callee, Value arg, VFG vfg) throws Exception {
      if (!vfg.containNode(caller)) {
         VFG vfg2 = IntraVFGConstructor.buildFromCaller(caller.getMethod(), caller.getStmt(), arg);
         connectVFG(vfg, vfg2, vfg2.getNodeByStmt(caller.getStmt()), callee, arg);
         NodeType.setType(vfg);
      }
   }

   private static void updateSrcAndSink(VFG vfg) throws Exception {
      boolean finished = false;

      label52:
      while(!finished) {
         finished = true;
         Iterator var3 = vfg.getSrcs().iterator();

         while(true) {
            while(true) {
               VFGNode oldSrc;
               Stmt srcStmt;
               do {
                  if (!var3.hasNext()) {
                     continue label52;
                  }

                  oldSrc = (VFGNode)var3.next();
                  srcStmt = oldSrc.getStmt();
               } while(srcStmt == null);

               ArrayList defNodes;
               VFGNode newSrc;
               Iterator var8;
               if (isParameterRef(srcStmt)) {
                  ParameterRef arg = getArgFromIdentity((IdentityStmt)srcStmt);
                  defNodes = getCallerNodes(oldSrc.getMethod());
                  var8 = defNodes.iterator();

                  while(var8.hasNext()) {
                     newSrc = (VFGNode)var8.next();
                     if (!vfg.containNode(newSrc)) {
                        Value argValue = getArgFromCaller(newSrc, arg);
                        updateSrcAndConnectVFG(newSrc, oldSrc, argValue, vfg);
                        finished = false;
                     }
                  }
               } else {
                  Value targetValue = getTargetValue(oldSrc);
                  defNodes = getNewSrcs(targetValue);
                  var8 = defNodes.iterator();

                  while(var8.hasNext()) {
                     newSrc = (VFGNode)var8.next();
                     if (!vfg.containNode(newSrc) && InterReach.immediateReach(newSrc, oldSrc, targetValue, callGraph, defNodes)) {
                        addSrc(newSrc, oldSrc, targetValue, vfg);
                        updateSinkAndConnectVFG(newSrc, targetValue, vfg);
                        finished = false;
                     }
                  }
               }
            }
         }
      }

   }

   private static Value getArgFromCaller(VFGNode newSrc, ParameterRef arg) throws Exception {
      Stmt stmt = newSrc.getStmt();
      InvokeExpr invokeExpr = null;
      if (stmt instanceof InvokeStmt) {
         invokeExpr = ((InvokeStmt)stmt).getInvokeExpr();
      } else if (stmt instanceof AssignStmt) {
         Value rightValue = ((AssignStmt)stmt).getRightOp();
         if (rightValue instanceof InvokeExpr) {
            invokeExpr = (InvokeExpr)rightValue;
         }
      }

      if (invokeExpr == null) {
         System.out.println("Cannot find arg from caller");
         throw new Exception();
      } else {
         return invokeExpr.getArg(arg.getIndex());
      }
   }

   private static ArrayList<VFGNode> getCallerNodes(SootMethod thisMtd) {
      ArrayList<CGEdge> intoEdges = callGraph.getInTo(thisMtd);
      ArrayList<VFGNode> nodes = new ArrayList();
      Iterator var4 = intoEdges.iterator();

      while(var4.hasNext()) {
         CGEdge edge = (CGEdge)var4.next();
         Unit unit = edge.getUnit();
         SootMethod mtd = edge.getSrc().getMtd();
         VFGNode node = new VFGNode(mtd, (Stmt)unit);
         nodes.add(node);
      }

      return nodes;
   }

   private static boolean isParameterRef(Stmt stmt) {
      if (stmt instanceof IdentityStmt) {
         IdentityStmt idStmt = (IdentityStmt)stmt;
         Value rightValue = idStmt.getRightOp();
         if (rightValue instanceof ParameterRef) {
            return true;
         }
      }

      return false;
   }

   private static ParameterRef getArgFromIdentity(IdentityStmt srcStmt) {
      return (ParameterRef)srcStmt.getRightOp();
   }

   private static ArrayList<VFGNode> getNewUses(Value targetValue) {
      if (targetValue != null) {
         ArrayList<SootMethod> useMtds = getUseMethod(targetValue, callGraph);
         if (useMtds.contains(StackTrace.getNullMtd())) {
            useMtds.remove(StackTrace.getNullMtd());
         }

         return getUseNodes(useMtds, targetValue);
      } else {
         return new ArrayList();
      }
   }

   private static ArrayList<VFGNode> getNewSrcs(Value targetValue) {
      if (targetValue != null) {
         ArrayList<SootMethod> defMtds = getDefMethod(targetValue, callGraph);
         if (defMtds.contains(StackTrace.getNullMtd())) {
            defMtds.remove(StackTrace.getNullMtd());
         }

         return getDefNodes(defMtds, targetValue);
      } else {
         return new ArrayList();
      }
   }

   private static void addSrc(VFGNode newSrc, VFGNode oldSrc, Value value, VFG vfg) {
      vfg.addNode(newSrc);
      vfg.addEdge(newSrc, oldSrc, value, 1);
      newSrc.setNodeType(0);
      oldSrc.setNodeType(1);
      vfg.setSrcs();
   }

   private static ArrayList<VFGNode> getDefNodes(ArrayList<SootMethod> defMtds, Value tgt) {
      ArrayList<VFGNode> defNodes = new ArrayList();
      Iterator var4 = defMtds.iterator();

      while(var4.hasNext()) {
         SootMethod mtd = (SootMethod)var4.next();
         Iterator var6 = mtd.getActiveBody().getUnits().iterator();

         while(var6.hasNext()) {
            Unit unit = (Unit)var6.next();
            if (hasDef(unit, tgt) && isNullDef(unit, mtd)) {
               defNodes.add(new VFGNode(mtd, (Stmt)unit));
            }
         }
      }

      return defNodes;
   }

   private static ArrayList<VFGNode> getUseNodes(ArrayList<SootMethod> useMtds, Value tgt) {
      ArrayList<VFGNode> useNodes = new ArrayList();
      Iterator var4 = useMtds.iterator();

      while(var4.hasNext()) {
         SootMethod mtd = (SootMethod)var4.next();
         Iterator var6 = mtd.getActiveBody().getUnits().iterator();

         while(var6.hasNext()) {
            Unit unit = (Unit)var6.next();
            if (hasUse(unit, tgt) && isNullUse(unit, mtd)) {
               useNodes.add(new VFGNode(mtd, (Stmt)unit));
            }
         }
      }

      return useNodes;
   }

   public static ArrayList<SootMethod> getRelatedMethod(Value tgt, CG callGraph) {
      ArrayList<SootMethod> mtds = new ArrayList();
      Iterator var4 = callGraph.getMtds().iterator();

      while(var4.hasNext()) {
         SootMethod mtd = (SootMethod)var4.next();
         if (hasDefOrUseStmt(mtd, tgt)) {
            mtds.add(mtd);
         }
      }

      return mtds;
   }

   public static ArrayList<SootMethod> getDefMethod(Value tgt, CG callGraph) {
      ArrayList<SootMethod> mtds = new ArrayList();
      Iterator var4 = callGraph.getMtds().iterator();

      while(var4.hasNext()) {
         SootMethod mtd = (SootMethod)var4.next();
         if (hasDefStmt(mtd, tgt)) {
            mtds.add(mtd);
         }
      }

      return mtds;
   }

   public static ArrayList<SootMethod> getUseMethod(Value tgt, CG callGraph) {
      ArrayList<SootMethod> mtds = new ArrayList();
      Iterator var4 = callGraph.getMtds().iterator();

      while(var4.hasNext()) {
         SootMethod mtd = (SootMethod)var4.next();
         if (hasUseStmt(mtd, tgt)) {
            mtds.add(mtd);
         }
      }

      return mtds;
   }

   private static boolean hasDefOrUseStmt(SootMethod mtd, Value tgt) {
      if (!mtd.hasActiveBody()) {
         return false;
      } else {
         Iterator var3 = mtd.getActiveBody().getUnits().iterator();

         while(var3.hasNext()) {
            Unit unit = (Unit)var3.next();
            if (hasDefOrUse(unit, tgt)) {
               return true;
            }
         }

         return false;
      }
   }

   private static boolean hasDefStmt(SootMethod mtd, Value tgt) {
      if (!mtd.hasActiveBody()) {
         return false;
      } else {
         Iterator var3 = mtd.getActiveBody().getUnits().iterator();

         Unit unit;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            unit = (Unit)var3.next();
         } while(!hasDef(unit, tgt) || !isNullDef(unit, mtd));

         return true;
      }
   }

   private static boolean hasUseStmt(SootMethod mtd, Value tgt) {
      if (!mtd.hasActiveBody()) {
         return false;
      } else {
         Iterator var3 = mtd.getActiveBody().getUnits().iterator();

         while(var3.hasNext()) {
            Unit unit = (Unit)var3.next();
            if (hasUse(unit, tgt)) {
               return true;
            }
         }

         return false;
      }
   }

   private static boolean hasDef(Unit unit, Value tgt) {
      Iterator var3 = unit.getDefBoxes().iterator();

      Value v;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         ValueBox vb = (ValueBox)var3.next();
         v = vb.getValue();
      } while(!(tgt instanceof InstanceFieldRef) || !(v instanceof InstanceFieldRef) || !isSameInstanceFieldRef((InstanceFieldRef)tgt, (InstanceFieldRef)v));

      return true;
   }

   private static boolean isNullDef(Unit unit, SootMethod mtd) {
      if (!(unit instanceof AssignStmt)) {
         return false;
      } else {
         AssignStmt stmt = (AssignStmt)unit;
         if (stmt.getRightOp() instanceof NullConstant) {
            return true;
         } else {
            return FLSpectrum.query(unit, mtd);
         }
      }
   }

   private static boolean isNullUse(Unit unit, SootMethod mtd) {
      return !FLSpectrum.query(unit, mtd);
   }

   private static boolean hasUse(Unit unit, Value tgt) {
      Iterator var3 = unit.getUseBoxes().iterator();

      Value v;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         ValueBox vb = (ValueBox)var3.next();
         v = vb.getValue();
      } while(!(tgt instanceof InstanceFieldRef) || !(v instanceof InstanceFieldRef) || !isSameInstanceFieldRef((InstanceFieldRef)tgt, (InstanceFieldRef)v));

      return true;
   }

   private static boolean hasDefOrUse(Unit unit, Value tgt) {
      return hasDef(unit, tgt) || hasUse(unit, tgt);
   }

   private static boolean isSameInstanceFieldRef(InstanceFieldRef tgt, InstanceFieldRef v) {
      Value base1 = tgt.getBase();
      Value base2 = v.getBase();
      SootField field1 = tgt.getField();
      SootField field2 = v.getField();
      if (!field1.equals(field2)) {
         return false;
      } else {
         return AliasQuery.isAlias(base1, base2);
      }
   }

   private static Value getTargetValue(VFGNode node) {
      Stmt stmt = node.getStmt();
      return !(stmt instanceof AssignStmt) ? null : getTargetValue((AssignStmt)stmt);
   }

   private static Value getTargetValue(AssignStmt stmt) {
      Value right = stmt.getRightOp();
      if (right instanceof Local) {
         return null;
      } else if (right instanceof CastExpr) {
         return ((CastExpr)right).getOp();
      } else if (right instanceof LengthExpr) {
         return ((LengthExpr)right).getOp();
      } else if (right instanceof NullConstant) {
         return null;
      } else if (right instanceof ThisRef) {
         return right;
      } else if (right instanceof InstanceFieldRef) {
         InstanceFieldRef fieldRef = (InstanceFieldRef)right;
         return fieldRef;
      } else {
         return null;
      }
   }

   private static CG buildCG() {
      CGNode mainNode = new CGNode(StackTrace.getHead().getMtd());
      CGNode sinkNode = new CGNode(StackTrace.getNullMtd());
      CG callGraph = new CG(mainNode, sinkNode);
      ArrayList<CGNode> frontier = new ArrayList();
      ArrayList<CGNode> nextFrontier = new ArrayList();
      Iterator edges = sootcg.edgesInto(sinkNode.getMtd());

      while(edges.hasNext()) {
         Edge edge = (Edge)edges.next();
         SootMethod srcMtd = edge.src();
         System.out.println("Caller: " + srcMtd);
      }

      frontier.add(mainNode);

      label51:
      while(!frontier.isEmpty()) {
         Iterator var13 = frontier.iterator();

         while(true) {
            CGNode node;
            do {
               do {
                  if (!var13.hasNext()) {
                     frontier = nextFrontier;
                     nextFrontier = new ArrayList();
                     continue label51;
                  }

                  node = (CGNode)var13.next();
               } while(callGraph.isVisited(node));

               callGraph.addVisited(node);
            } while(node.getMtd() == null);

            Iterator edges = sootcg.edgesOutOf((MethodOrMethodContext)node.getMtd());

            while(edges.hasNext()) {
               Edge edge = (Edge)edges.next();
               SootMethod tgtMtd = edge.tgt();
               if (tgtMtd.getDeclaringClass().isApplicationClass()) {
                  CGNode newNode;
                  if (tgtMtd.equals(sinkNode.getMtd())) {
                     newNode = sinkNode;
                  } else {
                     newNode = new CGNode(tgtMtd);
                  }

                  CGEdge newEdge = new CGEdge(node, newNode, edge.srcUnit());
                  if (callGraph.addEdge(newEdge)) {
                     nextFrontier.add(newNode);
                  }
               }
            }
         }
      }

      addStackTraceToCG(callGraph);
      return callGraph;
   }

   private static void addStackTraceToCG(CG callGraph) {
      StackFrame head = StackTrace.getHead();
      StackFrame cur = head;

      while(cur != null) {
         StackFrame next = cur.getNext();
         if (next == null) {
            cur = next;
         } else {
            CGNode node1 = new CGNode(cur.getMtd());
            CGNode node2 = new CGNode(next.getMtd());
            if (!callGraph.containNode(node1)) {
               callGraph.addNode(node1);
            }

            if (!callGraph.containNode(node2)) {
               callGraph.addNode(node2);
            }

            CGEdge edge = new CGEdge(node1, node2, cur.getStmt());
            if (!callGraph.containEdge(edge)) {
               callGraph.addEdge(edge);
            }

            cur = cur.getNext();
         }
      }

   }

   private static void setSootMethodInTrace() throws Exception {
      StackFrame head = StackTrace.getHead();
      StackFrame cur = head;

      while(true) {
         while(cur != null) {
            if (cur.getMtd() != null) {
               cur = cur.getNext();
            } else {
               SootClass cls = Scene.v().getSootClass(cur.getClsName());
               SootMethod mtd = NPELocator.getMtdByNumber(cls, cur.getLineNumber());
               if (mtd != null && mtd.getName().equals(cur.getMtdName())) {
                  cur.setCls(cls);
                  cur.setMtd(mtd);
               } else {
                  System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                  System.out.println("Cannot find " + cur.getLineNumber() + ": " + cur.getMtdName() + " in " + cls);
               }

               cur = cur.getNext();
            }
         }

         return;
      }
   }

   private static void setStmtInTrace() throws Exception {
      StackFrame head = StackTrace.getHead();
      StackFrame cur = head;

      while(true) {
         while(cur != null) {
            SootMethod mtd = cur.getMtd();
            if (mtd != null && cur.getStmt() == null) {
               Iterator var4 = mtd.getActiveBody().getUnits().iterator();

               while(var4.hasNext()) {
                  Unit unit = (Unit)var4.next();
                  if (unit.getJavaSourceStartLineNumber() == cur.getLineNumber() && isRightStmt(unit, cur.getNext().getMtd())) {
                     cur.setStmt((Stmt)unit);
                  }
               }

               if (cur.getStmt() == null) {
                  System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                  System.out.println("Cannot find stmt " + cur.getLineNumber() + ": in method " + mtd.getName());
                  cur = cur.getNext();
               } else {
                  cur = cur.getNext();
               }
            } else {
               cur = cur.getNext();
            }
         }

         return;
      }
   }

   private static boolean isRightStmt(Unit unit, SootMethod tgt) {
      Iterator edges = sootcg.edgesOutOf(unit);

      while(edges.hasNext()) {
         Edge edge = (Edge)edges.next();
         if (edge.getTgt() instanceof SootMethod) {
            SootMethod mtd = (SootMethod)edge.getTgt();
            if (mtd.equals(tgt)) {
               return true;
            }
         }
      }

      if (tgt == null) {
         return false;
      } else if (unit instanceof InvokeStmt && tgt.equals(getMtdFromInvokeStmt((InvokeStmt)unit))) {
         return true;
      } else {
         return false;
      }
   }

   private static SootMethod getMtdFromInvokeStmt(InvokeStmt invokeStmt) {
      return invokeStmt.getInvokeExpr().getMethod();
   }
}
