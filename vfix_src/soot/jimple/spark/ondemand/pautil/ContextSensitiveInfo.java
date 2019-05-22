package soot.jimple.spark.ondemand.pautil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootMethod;
import soot.jimple.InvokeExpr;
import soot.jimple.spark.ondemand.genericutil.ArraySet;
import soot.jimple.spark.ondemand.genericutil.ArraySetMultiMap;
import soot.jimple.spark.pag.GlobalVarNode;
import soot.jimple.spark.pag.LocalVarNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.VarNode;
import soot.toolkits.scalar.Pair;
import soot.util.HashMultiMap;

public class ContextSensitiveInfo {
   private static final Logger logger = LoggerFactory.getLogger(ContextSensitiveInfo.class);
   private static final boolean SKIP_STRING_NODES = false;
   private static final boolean SKIP_EXCEPTION_NODES = false;
   private static final boolean SKIP_THREAD_GLOBALS = false;
   private static final boolean PRINT_CALL_SITE_INFO = false;
   private final ArraySetMultiMap<VarNode, AssignEdge> contextSensitiveAssignEdges = new ArraySetMultiMap();
   private final ArraySetMultiMap<VarNode, AssignEdge> contextSensitiveAssignBarEdges = new ArraySetMultiMap();
   private final ArraySetMultiMap<SootMethod, VarNode> methodToNodes = new ArraySetMultiMap();
   private final ArraySetMultiMap<SootMethod, VarNode> methodToOutPorts = new ArraySetMultiMap();
   private final ArraySetMultiMap<SootMethod, VarNode> methodToInPorts = new ArraySetMultiMap();
   private final ArraySetMultiMap<SootMethod, Integer> callSitesInMethod = new ArraySetMultiMap();
   private final ArraySetMultiMap<SootMethod, Integer> callSitesInvokingMethod = new ArraySetMultiMap();
   private final ArraySetMultiMap<Integer, SootMethod> callSiteToTargets = new ArraySetMultiMap();
   private final ArraySetMultiMap<Integer, AssignEdge> callSiteToEdges = new ArraySetMultiMap();
   private final Map<Integer, LocalVarNode> virtCallSiteToReceiver = new HashMap();
   private final Map<Integer, SootMethod> callSiteToInvokedMethod = new HashMap();
   private final Map<Integer, SootMethod> callSiteToInvokingMethod = new HashMap();
   private final ArraySetMultiMap<LocalVarNode, Integer> receiverToVirtCallSites = new ArraySetMultiMap();

   public ContextSensitiveInfo(PAG pag) {
      Iterator iter = pag.getVarNodeNumberer().iterator();

      SootMethod method;
      while(iter.hasNext()) {
         VarNode varNode = (VarNode)iter.next();
         if (varNode instanceof LocalVarNode) {
            LocalVarNode local = (LocalVarNode)varNode;
            method = local.getMethod();

            assert method != null : local;

            this.methodToNodes.put(method, local);
            if (SootUtil.isRetNode(local)) {
               this.methodToOutPorts.put(method, local);
            }

            if (SootUtil.isParamNode(local)) {
               this.methodToInPorts.put(method, local);
            }
         }
      }

      int callSiteNum = 0;
      Set assignSources = pag.simpleSources();
      Iterator iter = assignSources.iterator();

      while(true) {
         VarNode assignSource;
         do {
            if (!iter.hasNext()) {
               HashMultiMap callAssigns = pag.callAssigns;
               method = null;
               Iterator iter = callAssigns.keySet().iterator();

               label147:
               while(iter.hasNext()) {
                  InvokeExpr ie = (InvokeExpr)iter.next();
                  Integer callSite = new Integer(callSiteNum++);
                  this.callSiteToInvokedMethod.put(callSite, ie.getMethod());
                  SootMethod invokingMethod = (SootMethod)pag.callToMethod.get(ie);
                  this.callSiteToInvokingMethod.put(callSite, invokingMethod);
                  if (pag.virtualCallsToReceivers.containsKey(ie)) {
                     LocalVarNode receiver = (LocalVarNode)pag.virtualCallsToReceivers.get(ie);

                     assert receiver != null;

                     this.virtCallSiteToReceiver.put(callSite, receiver);
                     this.receiverToVirtCallSites.put(receiver, callSite);
                  }

                  Set curEdges = callAssigns.get(ie);
                  Iterator iterator = curEdges.iterator();

                  while(true) {
                     VarNode dst;
                     Pair callAssign;
                     VarNode src;
                     do {
                        do {
                           if (!iterator.hasNext()) {
                              continue label147;
                           }

                           callAssign = (Pair)iterator.next();
                        } while(!(callAssign.getO1() instanceof VarNode));

                        src = (VarNode)callAssign.getO1();
                        dst = (VarNode)callAssign.getO2();
                     } while(this.skipNode(src));

                     ArraySet edges = this.getAssignBarEdges(src);
                     AssignEdge edge = null;

                     for(int i = 0; i < edges.size() && edge == null; ++i) {
                        AssignEdge curEdge = (AssignEdge)edges.get(i);
                        if (curEdge.getDst() == dst) {
                           edge = curEdge;
                        }
                     }

                     assert edge != null : "no edge from " + src + " to " + dst;

                     boolean edgeFromOtherCallSite = edge.isCallEdge();
                     if (edgeFromOtherCallSite) {
                        edge = new AssignEdge(src, dst);
                     }

                     edge.setCallSite(callSite);
                     this.callSiteToEdges.put(callSite, edge);
                     SootMethod invokedMethod;
                     if (SootUtil.isParamNode(dst)) {
                        edge.setParamEdge();
                        invokedMethod = ((LocalVarNode)dst).getMethod();
                        this.callSiteToTargets.put(callSite, invokedMethod);
                        this.callSitesInvokingMethod.put(invokedMethod, callSite);
                        if (src instanceof LocalVarNode) {
                           this.callSitesInMethod.put(((LocalVarNode)src).getMethod(), callSite);
                        }
                     } else if (SootUtil.isRetNode(src)) {
                        edge.setReturnEdge();
                        invokedMethod = ((LocalVarNode)src).getMethod();
                        this.callSiteToTargets.put(callSite, invokedMethod);
                        this.callSitesInvokingMethod.put(invokedMethod, callSite);
                        if (dst instanceof LocalVarNode) {
                           this.callSitesInMethod.put(((LocalVarNode)dst).getMethod(), callSite);
                        }
                     } else {
                        assert false : "weird call edge " + callAssign;
                     }

                     if (edgeFromOtherCallSite) {
                        this.addAssignEdge(edge);
                     }
                  }
               }

               assert this.callEdgesReasonable();

               return;
            }

            assignSource = (VarNode)iter.next();
         } while(this.skipNode(assignSource));

         boolean sourceGlobal = assignSource instanceof GlobalVarNode;
         Node[] assignTargets = pag.simpleLookup(assignSource);

         for(int i = 0; i < assignTargets.length; ++i) {
            VarNode assignTarget = (VarNode)assignTargets[i];
            if (!this.skipNode(assignTarget)) {
               boolean isFinalizerNode = false;
               if (assignTarget instanceof LocalVarNode) {
                  LocalVarNode local = (LocalVarNode)assignTarget;
                  SootMethod method = local.getMethod();
                  if (method.toString().indexOf("finalize()") != -1 && SootUtil.isThisNode(local)) {
                     isFinalizerNode = true;
                  }
               }

               boolean targetGlobal = assignTarget instanceof GlobalVarNode;
               AssignEdge assignEdge = new AssignEdge(assignSource, assignTarget);
               if (isFinalizerNode) {
                  assignEdge.setParamEdge();
                  Integer callSite = new Integer(callSiteNum++);
                  assignEdge.setCallSite(callSite);
               }

               this.addAssignEdge(assignEdge);
               SootMethod method;
               if (sourceGlobal) {
                  if (!targetGlobal) {
                     method = ((LocalVarNode)assignTarget).getMethod();
                     if (!this.methodToInPorts.get(method).contains(assignTarget)) {
                        this.methodToInPorts.put(method, assignSource);
                     }
                  }
               } else if (targetGlobal) {
                  method = ((LocalVarNode)assignSource).getMethod();
                  if (!this.methodToOutPorts.get(method).contains(assignSource)) {
                     this.methodToOutPorts.put(method, assignTarget);
                  }
               }
            }
         }
      }
   }

   private boolean callEdgesReasonable() {
      Set<VarNode> vars = this.contextSensitiveAssignEdges.keySet();
      Iterator var2 = vars.iterator();

      while(var2.hasNext()) {
         VarNode node = (VarNode)var2.next();
         ArraySet<AssignEdge> assigns = this.contextSensitiveAssignEdges.get(node);
         Iterator var5 = assigns.iterator();

         while(var5.hasNext()) {
            AssignEdge edge = (AssignEdge)var5.next();
            if (edge.isCallEdge() && edge.getCallSite() == null) {
               logger.debug("" + edge + " is weird!!");
               return false;
            }
         }
      }

      return true;
   }

   private String assignEdgesWellFormed(PAG pag) {
      Iterator iter = pag.getVarNodeNumberer().iterator();

      while(iter.hasNext()) {
         VarNode v = (VarNode)iter.next();
         Set<AssignEdge> outgoingAssigns = this.getAssignBarEdges(v);
         Iterator var5 = outgoingAssigns.iterator();

         while(var5.hasNext()) {
            AssignEdge edge = (AssignEdge)var5.next();
            if (edge.getSrc() != v) {
               return edge + " src should be " + v;
            }
         }

         Set<AssignEdge> incomingAssigns = this.getAssignEdges(v);
         Iterator var9 = incomingAssigns.iterator();

         while(var9.hasNext()) {
            AssignEdge edge = (AssignEdge)var9.next();
            if (edge.getDst() != v) {
               return edge + " dst should be " + v;
            }
         }
      }

      return null;
   }

   private boolean skipNode(VarNode node) {
      return false;
   }

   private void addAssignEdge(AssignEdge assignEdge) {
      this.contextSensitiveAssignEdges.put(assignEdge.getSrc(), assignEdge);
      this.contextSensitiveAssignBarEdges.put(assignEdge.getDst(), assignEdge);
   }

   public ArraySet<AssignEdge> getAssignBarEdges(VarNode node) {
      return this.contextSensitiveAssignEdges.get(node);
   }

   public ArraySet<AssignEdge> getAssignEdges(VarNode node) {
      return this.contextSensitiveAssignBarEdges.get(node);
   }

   public Set<SootMethod> methods() {
      return this.methodToNodes.keySet();
   }

   public ArraySet<VarNode> getNodesForMethod(SootMethod method) {
      return this.methodToNodes.get(method);
   }

   public ArraySet<VarNode> getInPortsForMethod(SootMethod method) {
      return this.methodToInPorts.get(method);
   }

   public ArraySet<VarNode> getOutPortsForMethod(SootMethod method) {
      return this.methodToOutPorts.get(method);
   }

   public ArraySet<Integer> getCallSitesInMethod(SootMethod method) {
      return this.callSitesInMethod.get(method);
   }

   public Set<Integer> getCallSitesInvokingMethod(SootMethod method) {
      return this.callSitesInvokingMethod.get(method);
   }

   public ArraySet<AssignEdge> getCallSiteEdges(Integer callSite) {
      return this.callSiteToEdges.get(callSite);
   }

   public ArraySet<SootMethod> getCallSiteTargets(Integer callSite) {
      return this.callSiteToTargets.get(callSite);
   }

   public LocalVarNode getReceiverForVirtCallSite(Integer callSite) {
      LocalVarNode ret = (LocalVarNode)this.virtCallSiteToReceiver.get(callSite);

      assert ret != null;

      return ret;
   }

   public Set<Integer> getVirtCallSitesForReceiver(LocalVarNode receiver) {
      return this.receiverToVirtCallSites.get(receiver);
   }

   public SootMethod getInvokedMethod(Integer callSite) {
      return (SootMethod)this.callSiteToInvokedMethod.get(callSite);
   }

   public SootMethod getInvokingMethod(Integer callSite) {
      return (SootMethod)this.callSiteToInvokingMethod.get(callSite);
   }

   public boolean isVirtCall(Integer callSite) {
      return this.virtCallSiteToReceiver.containsKey(callSite);
   }
}
