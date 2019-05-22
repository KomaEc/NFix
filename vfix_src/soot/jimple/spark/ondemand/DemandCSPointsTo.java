package soot.jimple.spark.ondemand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.AnySubType;
import soot.ArrayType;
import soot.Context;
import soot.Local;
import soot.PointsToAnalysis;
import soot.PointsToSet;
import soot.RefType;
import soot.Scene;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.jimple.spark.ondemand.genericutil.ArraySet;
import soot.jimple.spark.ondemand.genericutil.HashSetMultiMap;
import soot.jimple.spark.ondemand.genericutil.ImmutableStack;
import soot.jimple.spark.ondemand.genericutil.Predicate;
import soot.jimple.spark.ondemand.genericutil.Propagator;
import soot.jimple.spark.ondemand.genericutil.Stack;
import soot.jimple.spark.ondemand.pautil.AssignEdge;
import soot.jimple.spark.ondemand.pautil.ContextSensitiveInfo;
import soot.jimple.spark.ondemand.pautil.OTFMethodSCCManager;
import soot.jimple.spark.ondemand.pautil.SootUtil;
import soot.jimple.spark.ondemand.pautil.ValidMatches;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.GlobalVarNode;
import soot.jimple.spark.pag.LocalVarNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.EmptyPointsToSet;
import soot.jimple.spark.sets.EqualsSupportingPointsToSet;
import soot.jimple.spark.sets.HybridPointsToSet;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetEqualsWrapper;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.toolkits.callgraph.VirtualCalls;
import soot.toolkits.scalar.Pair;
import soot.util.NumberedString;

public final class DemandCSPointsTo implements PointsToAnalysis {
   private static final Logger logger = LoggerFactory.getLogger(DemandCSPointsTo.class);
   public static boolean DEBUG = false;
   protected static final int DEBUG_NESTING = 15;
   protected static final int DEBUG_PASS = -1;
   protected static final boolean DEBUG_VIRT;
   protected static final int DEFAULT_MAX_PASSES = 10;
   protected static final int DEFAULT_MAX_TRAVERSAL = 75000;
   protected static final boolean DEFAULT_LAZY = true;
   private boolean refineCallGraph;
   protected static final ImmutableStack<Integer> EMPTY_CALLSTACK;
   protected final DemandCSPointsTo.AllocAndContextCache allocAndContextCache;
   protected Stack<Pair<Integer, ImmutableStack<Integer>>> callGraphStack;
   protected final DemandCSPointsTo.CallSiteToTargetsMap callSiteToResolvedTargets;
   protected HashMap<List<Object>, Set<SootMethod>> callTargetsArgCache;
   protected final Stack<DemandCSPointsTo.VarAndContext> contextForAllocsStack;
   protected Map<DemandCSPointsTo.VarAndContext, Pair<PointsToSetInternal, AllocAndContextSet>> contextsForAllocsCache;
   protected final ContextSensitiveInfo csInfo;
   protected boolean doPointsTo;
   protected FieldCheckHeuristic fieldCheckHeuristic;
   protected HeuristicType heuristicType;
   protected SootUtil.FieldToEdgesMap fieldToLoads;
   protected SootUtil.FieldToEdgesMap fieldToStores;
   protected final int maxNodesPerPass;
   protected final int maxPasses;
   protected int nesting;
   protected int numNodesTraversed;
   protected int numPasses;
   protected final PAG pag;
   protected AllocAndContextSet pointsTo;
   protected final Set<DemandCSPointsTo.CallSiteAndContext> queriedCallSites;
   protected int recursionDepth;
   protected boolean refiningCallSite;
   protected OTFMethodSCCManager sccManager;
   protected Map<DemandCSPointsTo.VarContextAndUp, Map<AllocAndContext, DemandCSPointsTo.CallingContextSet>> upContextCache;
   protected ValidMatches vMatches;
   protected Map<Local, PointsToSet> reachingObjectsCache;
   protected Map<Local, PointsToSet> reachingObjectsCacheNoCGRefinement;
   protected boolean useCache;
   private final boolean lazy;

   public static DemandCSPointsTo makeDefault() {
      return makeWithBudget(75000, 10, true);
   }

   public static DemandCSPointsTo makeWithBudget(int maxTraversal, int maxPasses, boolean lazy) {
      PAG pag = (PAG)Scene.v().getPointsToAnalysis();
      ContextSensitiveInfo csInfo = new ContextSensitiveInfo(pag);
      return new DemandCSPointsTo(csInfo, pag, maxTraversal, maxPasses, lazy);
   }

   public DemandCSPointsTo(ContextSensitiveInfo csInfo, PAG pag) {
      this(csInfo, pag, 75000, 10, true);
   }

   public DemandCSPointsTo(ContextSensitiveInfo csInfo, PAG pag, int maxTraversal, int maxPasses, boolean lazy) {
      this.refineCallGraph = true;
      this.allocAndContextCache = new DemandCSPointsTo.AllocAndContextCache();
      this.callGraphStack = new Stack();
      this.callSiteToResolvedTargets = new DemandCSPointsTo.CallSiteToTargetsMap();
      this.callTargetsArgCache = new HashMap();
      this.contextForAllocsStack = new Stack();
      this.contextsForAllocsCache = new HashMap();
      this.nesting = 0;
      this.numPasses = 0;
      this.pointsTo = null;
      this.queriedCallSites = new HashSet();
      this.recursionDepth = -1;
      this.refiningCallSite = false;
      this.upContextCache = new HashMap();
      this.csInfo = csInfo;
      this.pag = pag;
      this.maxPasses = maxPasses;
      this.lazy = lazy;
      this.maxNodesPerPass = maxTraversal / maxPasses;
      this.heuristicType = HeuristicType.INCR;
      this.reachingObjectsCache = new HashMap();
      this.reachingObjectsCacheNoCGRefinement = new HashMap();
      this.useCache = true;
   }

   private void init() {
      this.fieldToStores = SootUtil.storesOnField(this.pag);
      this.fieldToLoads = SootUtil.loadsOnField(this.pag);
      this.vMatches = new ValidMatches(this.pag, this.fieldToStores);
   }

   public PointsToSet reachingObjects(Local l) {
      return (PointsToSet)(this.lazy ? new LazyContextSensitivePointsToSet(l, new WrappedPointsToSet((PointsToSetInternal)this.pag.reachingObjects(l)), this) : this.doReachingObjects(l));
   }

   public PointsToSet doReachingObjects(Local l) {
      if (this.fieldToStores == null) {
         this.init();
      }

      Map cache;
      if (this.refineCallGraph) {
         cache = this.reachingObjectsCache;
      } else {
         cache = this.reachingObjectsCacheNoCGRefinement;
      }

      PointsToSet result = (PointsToSet)cache.get(l);
      if (result == null) {
         result = this.computeReachingObjects(l);
         if (this.useCache) {
            cache.put(l, result);
         }
      }

      assert this.consistentResult(l, result);

      return result;
   }

   private boolean consistentResult(Local l, PointsToSet result) {
      PointsToSet result2 = this.computeReachingObjects(l);
      if (result instanceof EqualsSupportingPointsToSet && result2 instanceof EqualsSupportingPointsToSet) {
         EqualsSupportingPointsToSet eq1 = (EqualsSupportingPointsToSet)result;
         EqualsSupportingPointsToSet eq2 = (EqualsSupportingPointsToSet)result2;
         return (new PointsToSetEqualsWrapper(eq1)).equals(new PointsToSetEqualsWrapper(eq2));
      } else {
         return true;
      }
   }

   protected PointsToSet computeReachingObjects(Local l) {
      VarNode v = this.pag.findLocalVarNode(l);
      if (v == null) {
         return EmptyPointsToSet.v();
      } else {
         PointsToSet contextSensitiveResult = this.computeRefinedReachingObjects(v);
         return (PointsToSet)(contextSensitiveResult == null ? new WrappedPointsToSet(v.getP2Set()) : contextSensitiveResult);
      }
   }

   protected PointsToSet computeRefinedReachingObjects(VarNode v) {
      this.fieldCheckHeuristic = HeuristicType.getHeuristic(this.heuristicType, this.pag.getTypeManager(), this.getMaxPasses());
      this.doPointsTo = true;
      this.numPasses = 0;
      AllocAndContextSet contextSensitiveResult = null;

      do {
         ++this.numPasses;
         if (this.numPasses > this.maxPasses) {
            break;
         }

         if (DEBUG) {
            logger.debug("PASS " + this.numPasses);
            logger.debug("" + this.fieldCheckHeuristic);
         }

         this.clearState();
         this.pointsTo = new AllocAndContextSet();

         try {
            this.refineP2Set(new DemandCSPointsTo.VarAndContext(v, EMPTY_CALLSTACK), (PointsToSetInternal)null);
            contextSensitiveResult = this.pointsTo;
         } catch (TerminateEarlyException var4) {
            logger.debug((String)var4.getMessage(), (Throwable)var4);
         }
      } while(this.fieldCheckHeuristic.runNewPass());

      return contextSensitiveResult;
   }

   protected boolean callEdgeInSCC(AssignEdge assignEdge) {
      boolean sameSCCAlready = false;

      assert assignEdge.isCallEdge();

      if (assignEdge.getSrc() instanceof LocalVarNode && assignEdge.getDst() instanceof LocalVarNode) {
         LocalVarNode src = (LocalVarNode)assignEdge.getSrc();
         LocalVarNode dst = (LocalVarNode)assignEdge.getDst();
         if (this.sccManager.inSameSCC(src.getMethod(), dst.getMethod())) {
            sameSCCAlready = true;
         }

         return sameSCCAlready;
      } else {
         return false;
      }
   }

   protected DemandCSPointsTo.CallingContextSet checkAllocAndContextCache(AllocAndContext allocAndContext, VarNode targetVar) {
      if (this.allocAndContextCache.containsKey(allocAndContext)) {
         Map<VarNode, DemandCSPointsTo.CallingContextSet> m = (Map)this.allocAndContextCache.get(allocAndContext);
         if (m.containsKey(targetVar)) {
            return (DemandCSPointsTo.CallingContextSet)m.get(targetVar);
         }
      } else {
         this.allocAndContextCache.put(allocAndContext, new HashMap());
      }

      return null;
   }

   protected PointsToSetInternal checkContextsForAllocsCache(DemandCSPointsTo.VarAndContext varAndContext, AllocAndContextSet ret, PointsToSetInternal locs) {
      PointsToSetInternal retSet = null;
      if (this.contextsForAllocsCache.containsKey(varAndContext)) {
         Iterator var5 = ((AllocAndContextSet)((Pair)this.contextsForAllocsCache.get(varAndContext)).getO2()).iterator();

         while(var5.hasNext()) {
            AllocAndContext allocAndContext = (AllocAndContext)var5.next();
            if (locs.contains(allocAndContext.alloc)) {
               ret.add(allocAndContext);
            }
         }

         final PointsToSetInternal oldLocs = (PointsToSetInternal)((Pair)this.contextsForAllocsCache.get(varAndContext)).getO1();
         final PointsToSetInternal tmpSet = new HybridPointsToSet(locs.getType(), this.pag);
         locs.forall(new P2SetVisitor() {
            public void visit(Node n) {
               if (!oldLocs.contains(n)) {
                  tmpSet.add(n);
               }

            }
         });
         retSet = tmpSet;
         oldLocs.addAll(tmpSet, (PointsToSetInternal)null);
      } else {
         PointsToSetInternal storedSet = new HybridPointsToSet(locs.getType(), this.pag);
         storedSet.addAll(locs, (PointsToSetInternal)null);
         this.contextsForAllocsCache.put(varAndContext, new Pair(storedSet, new AllocAndContextSet()));
         retSet = locs;
      }

      return (PointsToSetInternal)retSet;
   }

   protected boolean checkP2Set(VarNode v, HeuristicType heuristic, Predicate<Set<AllocAndContext>> p2setPred) {
      this.doPointsTo = true;
      this.fieldCheckHeuristic = HeuristicType.getHeuristic(heuristic, this.pag.getTypeManager(), this.getMaxPasses());
      this.numPasses = 0;

      while(true) {
         ++this.numPasses;
         if (this.numPasses > this.maxPasses) {
            return true;
         }

         if (DEBUG) {
            logger.debug("PASS " + this.numPasses);
            logger.debug("" + this.fieldCheckHeuristic);
         }

         this.clearState();
         this.pointsTo = new AllocAndContextSet();
         boolean success = false;

         try {
            success = this.refineP2Set(new DemandCSPointsTo.VarAndContext(v, EMPTY_CALLSTACK), (PointsToSetInternal)null);
         } catch (TerminateEarlyException var6) {
            success = false;
         }

         if (success) {
            if (p2setPred.test(this.pointsTo)) {
               return false;
            }
         } else if (!this.fieldCheckHeuristic.runNewPass()) {
            return true;
         }
      }
   }

   protected DemandCSPointsTo.CallingContextSet checkUpContextCache(DemandCSPointsTo.VarContextAndUp varContextAndUp, AllocAndContext allocAndContext) {
      if (this.upContextCache.containsKey(varContextAndUp)) {
         Map<AllocAndContext, DemandCSPointsTo.CallingContextSet> allocAndContextMap = (Map)this.upContextCache.get(varContextAndUp);
         if (allocAndContextMap.containsKey(allocAndContext)) {
            return (DemandCSPointsTo.CallingContextSet)allocAndContextMap.get(allocAndContext);
         }
      } else {
         this.upContextCache.put(varContextAndUp, new HashMap());
      }

      return null;
   }

   protected void clearState() {
      this.allocAndContextCache.clear();
      this.callGraphStack.clear();
      this.callSiteToResolvedTargets.clear();
      this.queriedCallSites.clear();
      this.contextsForAllocsCache.clear();
      this.contextForAllocsStack.clear();
      this.upContextCache.clear();
      this.callTargetsArgCache.clear();
      this.sccManager = new OTFMethodSCCManager();
      this.numNodesTraversed = 0;
      this.nesting = 0;
      this.recursionDepth = -1;
   }

   protected Set<VarNode> computeFlowsTo(AllocNode alloc, HeuristicType heuristic) {
      this.fieldCheckHeuristic = HeuristicType.getHeuristic(heuristic, this.pag.getTypeManager(), this.getMaxPasses());
      this.numPasses = 0;
      Set smallest = null;

      do {
         ++this.numPasses;
         if (this.numPasses > this.maxPasses) {
            return smallest;
         }

         if (DEBUG) {
            logger.debug("PASS " + this.numPasses);
            logger.debug("" + this.fieldCheckHeuristic);
         }

         this.clearState();
         Set result = null;

         try {
            result = this.getFlowsToHelper(new AllocAndContext(alloc, EMPTY_CALLSTACK));
         } catch (TerminateEarlyException var6) {
            logger.debug((String)var6.getMessage(), (Throwable)var6);
         }

         if (result != null && (smallest == null || result.size() < smallest.size())) {
            smallest = result;
         }
      } while(this.fieldCheckHeuristic.runNewPass());

      return smallest;
   }

   protected void debugPrint(String str) {
      if (this.nesting <= 15) {
         logger.debug(":" + this.nesting + " " + str);
      }

   }

   protected void dumpPathForLoc(VarNode v, final AllocNode badLoc, String filePrefix) {
      final HashSet<VarNode> visited = new HashSet();
      final DotPointerGraph dotGraph = new DotPointerGraph();

      final class Helper {
         boolean handle(VarNode curNode) {
            assert curNode.getP2Set().contains(badLoc);

            visited.add(curNode);
            Node[] newEdges = DemandCSPointsTo.this.pag.allocInvLookup(curNode);

            for(int i = 0; i < newEdges.length; ++i) {
               AllocNode alloc = (AllocNode)newEdges[i];
               if (alloc.equals(badLoc)) {
                  dotGraph.addNew(alloc, curNode);
                  return true;
               }
            }

            Iterator var12 = DemandCSPointsTo.this.csInfo.getAssignEdges(curNode).iterator();

            while(var12.hasNext()) {
               AssignEdge assignEdge = (AssignEdge)var12.next();
               VarNode other = assignEdge.getSrc();
               if (other.getP2Set().contains(badLoc) && !visited.contains(other) && this.handle(other)) {
                  if (assignEdge.isCallEdge()) {
                     dotGraph.addCall(other, curNode, assignEdge.getCallSite());
                  } else {
                     dotGraph.addAssign(other, curNode);
                  }

                  return true;
               }
            }

            Node[] loadEdges = DemandCSPointsTo.this.pag.loadInvLookup(curNode);

            for(int ix = 0; ix < loadEdges.length; ++ix) {
               FieldRefNode frNode = (FieldRefNode)loadEdges[ix];
               SparkField field = frNode.getField();
               VarNode base = frNode.getBase();
               PointsToSetInternal baseP2Set = base.getP2Set();
               Iterator var9 = DemandCSPointsTo.this.fieldToStores.get(field).iterator();

               while(var9.hasNext()) {
                  Pair<VarNode, VarNode> store = (Pair)var9.next();
                  if (((VarNode)store.getO2()).getP2Set().hasNonEmptyIntersection(baseP2Set)) {
                     VarNode matchSrc = (VarNode)store.getO1();
                     if (matchSrc.getP2Set().contains(badLoc) && !visited.contains(matchSrc) && this.handle(matchSrc)) {
                        dotGraph.addMatch(matchSrc, curNode);
                        return true;
                     }
                  }
               }
            }

            return false;
         }
      }

      Helper h = new Helper();
      h.handle(v);
      dotGraph.dump("tmp/" + filePrefix + v.getNumber() + "_" + badLoc.getNumber() + ".dot");
   }

   protected Collection<AssignEdge> filterAssigns(VarNode v, ImmutableStack<Integer> callingContext, boolean forward, boolean refineVirtCalls) {
      Set<AssignEdge> assigns = forward ? this.csInfo.getAssignEdges(v) : this.csInfo.getAssignBarEdges(v);
      boolean exitNode = forward ? SootUtil.isParamNode(v) : SootUtil.isRetNode(v);
      boolean backward = !forward;
      Object realAssigns;
      Integer callSite;
      if (exitNode && !callingContext.isEmpty()) {
         Integer topCallSite = (Integer)callingContext.peek();
         realAssigns = new ArrayList();
         Iterator var16 = assigns.iterator();

         while(var16.hasNext()) {
            AssignEdge assignEdge = (AssignEdge)var16.next();

            assert forward && assignEdge.isParamEdge() || backward && assignEdge.isReturnEdge() : assignEdge;

            callSite = assignEdge.getCallSite();

            assert this.csInfo.getCallSiteTargets(callSite).contains(((LocalVarNode)v).getMethod()) : assignEdge;

            if (topCallSite.equals(callSite) || this.callEdgeInSCC(assignEdge)) {
               ((Collection)realAssigns).add(assignEdge);
            }
         }
      } else if (assigns.size() > 1) {
         realAssigns = new ArrayList();
         Iterator var9 = assigns.iterator();

         while(true) {
            while(true) {
               while(var9.hasNext()) {
                  AssignEdge assignEdge = (AssignEdge)var9.next();
                  boolean enteringCall = forward ? assignEdge.isReturnEdge() : assignEdge.isParamEdge();
                  if (enteringCall) {
                     callSite = assignEdge.getCallSite();
                     if (this.csInfo.isVirtCall(callSite) && refineVirtCalls) {
                        Set<SootMethod> targets = this.refineCallSite(assignEdge.getCallSite(), callingContext);
                        LocalVarNode nodeInTargetMethod = forward ? (LocalVarNode)assignEdge.getSrc() : (LocalVarNode)assignEdge.getDst();
                        if (targets.contains(nodeInTargetMethod.getMethod())) {
                           ((Collection)realAssigns).add(assignEdge);
                        }
                     } else {
                        ((Collection)realAssigns).add(assignEdge);
                     }
                  } else {
                     ((Collection)realAssigns).add(assignEdge);
                  }
               }

               return (Collection)realAssigns;
            }
         }
      } else {
         realAssigns = assigns;
      }

      return (Collection)realAssigns;
   }

   protected AllocAndContextSet findContextsForAllocs(DemandCSPointsTo.VarAndContext varAndContext, PointsToSetInternal locs) {
      if (this.contextForAllocsStack.contains(varAndContext)) {
         int oldIndex = this.contextForAllocsStack.indexOf(varAndContext);
         if (oldIndex != this.contextForAllocsStack.size() - 1) {
            if (this.recursionDepth == -1) {
               this.recursionDepth = oldIndex + 1;
               if (DEBUG) {
                  this.debugPrint("RECURSION depth = " + this.recursionDepth);
               }
            } else if (this.contextForAllocsStack.size() - oldIndex > 5) {
               throw new TerminateEarlyException();
            }
         }
      }

      this.contextForAllocsStack.push(varAndContext);
      final AllocAndContextSet ret = new AllocAndContextSet();
      final PointsToSetInternal realLocs = this.checkContextsForAllocsCache(varAndContext, ret, locs);
      if (realLocs.isEmpty()) {
         if (DEBUG) {
            this.debugPrint("cached result " + ret);
         }

         this.contextForAllocsStack.pop();
         return ret;
      } else {
         ++this.nesting;
         if (DEBUG) {
            this.debugPrint("finding alloc contexts for " + varAndContext);
         }

         AllocAndContextSet var16;
         try {
            Set<DemandCSPointsTo.VarAndContext> marked = new HashSet();
            Stack<DemandCSPointsTo.VarAndContext> worklist = new Stack();
            final Propagator<DemandCSPointsTo.VarAndContext> p = new Propagator(marked, worklist);
            p.prop(varAndContext);
            DemandCSPointsTo.IncomingEdgeHandler edgeHandler = new DemandCSPointsTo.IncomingEdgeHandler() {
               public void handleAlloc(AllocNode allocNode, DemandCSPointsTo.VarAndContext origVarAndContext) {
                  if (realLocs.contains(allocNode)) {
                     if (DemandCSPointsTo.DEBUG) {
                        DemandCSPointsTo.this.debugPrint("found alloc " + allocNode);
                     }

                     ret.add(new AllocAndContext(allocNode, origVarAndContext.context));
                  }

               }

               public void handleMatchSrc(VarNode matchSrc, PointsToSetInternal intersection, VarNode loadBase, VarNode storeBase, DemandCSPointsTo.VarAndContext origVarAndContext, SparkField field, boolean refine) {
                  if (DemandCSPointsTo.DEBUG) {
                     DemandCSPointsTo.this.debugPrint("handling src " + matchSrc);
                     DemandCSPointsTo.this.debugPrint("intersection " + intersection);
                  }

                  if (!refine) {
                     p.prop(new DemandCSPointsTo.VarAndContext(matchSrc, DemandCSPointsTo.EMPTY_CALLSTACK));
                  } else {
                     AllocAndContextSet allocContexts = DemandCSPointsTo.this.findContextsForAllocs(new DemandCSPointsTo.VarAndContext(loadBase, origVarAndContext.context), intersection);
                     if (DemandCSPointsTo.DEBUG) {
                        DemandCSPointsTo.this.debugPrint("alloc contexts " + allocContexts);
                     }

                     Iterator var9 = allocContexts.iterator();

                     while(var9.hasNext()) {
                        AllocAndContext allocAndContext = (AllocAndContext)var9.next();
                        if (DemandCSPointsTo.DEBUG) {
                           DemandCSPointsTo.this.debugPrint("alloc and context " + allocAndContext);
                        }

                        DemandCSPointsTo.CallingContextSet matchSrcContexts;
                        if (DemandCSPointsTo.this.fieldCheckHeuristic.validFromBothEnds(field)) {
                           matchSrcContexts = DemandCSPointsTo.this.findUpContextsForVar(allocAndContext, new DemandCSPointsTo.VarContextAndUp(storeBase, DemandCSPointsTo.EMPTY_CALLSTACK, DemandCSPointsTo.EMPTY_CALLSTACK));
                        } else {
                           matchSrcContexts = DemandCSPointsTo.this.findVarContextsFromAlloc(allocAndContext, storeBase);
                        }

                        Iterator var12 = matchSrcContexts.iterator();

                        while(var12.hasNext()) {
                           ImmutableStack<Integer> matchSrcContext = (ImmutableStack)var12.next();
                           p.prop(new DemandCSPointsTo.VarAndContext(matchSrc, matchSrcContext));
                        }
                     }

                  }
               }

               Object getResult() {
                  return ret;
               }

               void handleAssignSrc(DemandCSPointsTo.VarAndContext newVarAndContext, DemandCSPointsTo.VarAndContext origVarAndContext, AssignEdge assignEdge) {
                  p.prop(newVarAndContext);
               }

               boolean shouldHandleSrc(VarNode src) {
                  return realLocs.hasNonEmptyIntersection(src.getP2Set());
               }
            };
            this.processIncomingEdges(edgeHandler, worklist);
            HybridPointsToSet storedSet;
            if (this.recursionDepth != -1) {
               if (this.contextForAllocsStack.size() > this.recursionDepth) {
                  if (DEBUG) {
                     this.debugPrint("REMOVING " + varAndContext);
                     this.debugPrint(this.contextForAllocsStack.toString());
                  }

                  this.contextsForAllocsCache.remove(varAndContext);
               } else {
                  assert this.contextForAllocsStack.size() == this.recursionDepth : this.recursionDepth + " " + this.contextForAllocsStack;

                  this.recursionDepth = -1;
                  if (this.contextsForAllocsCache.containsKey(varAndContext)) {
                     ((AllocAndContextSet)((Pair)this.contextsForAllocsCache.get(varAndContext)).getO2()).addAll(ret);
                  } else {
                     storedSet = new HybridPointsToSet(locs.getType(), this.pag);
                     storedSet.addAll(locs, (PointsToSetInternal)null);
                     this.contextsForAllocsCache.put(varAndContext, new Pair(storedSet, ret));
                  }
               }
            } else if (this.contextsForAllocsCache.containsKey(varAndContext)) {
               ((AllocAndContextSet)((Pair)this.contextsForAllocsCache.get(varAndContext)).getO2()).addAll(ret);
            } else {
               storedSet = new HybridPointsToSet(locs.getType(), this.pag);
               storedSet.addAll(locs, (PointsToSetInternal)null);
               this.contextsForAllocsCache.put(varAndContext, new Pair(storedSet, ret));
            }

            --this.nesting;
            var16 = ret;
         } catch (CallSiteException var13) {
            this.contextsForAllocsCache.remove(varAndContext);
            throw var13;
         } finally {
            this.contextForAllocsStack.pop();
         }

         return var16;
      }
   }

   protected DemandCSPointsTo.CallingContextSet findUpContextsForVar(AllocAndContext allocAndContext, DemandCSPointsTo.VarContextAndUp varContextAndUp) {
      final AllocNode alloc = allocAndContext.alloc;
      final ImmutableStack<Integer> allocContext = allocAndContext.context;
      DemandCSPointsTo.CallingContextSet tmpSet = this.checkUpContextCache(varContextAndUp, allocAndContext);
      if (tmpSet != null) {
         return tmpSet;
      } else {
         final DemandCSPointsTo.CallingContextSet ret = new DemandCSPointsTo.CallingContextSet();
         ((Map)this.upContextCache.get(varContextAndUp)).put(allocAndContext, ret);
         ++this.nesting;
         if (DEBUG) {
            this.debugPrint("finding up context for " + varContextAndUp + " to " + alloc + " " + allocContext);
         }

         try {
            Set<DemandCSPointsTo.VarAndContext> marked = new HashSet();
            Stack<DemandCSPointsTo.VarAndContext> worklist = new Stack();
            final Propagator<DemandCSPointsTo.VarAndContext> p = new Propagator(marked, worklist);
            p.prop(varContextAndUp);

            class UpContextEdgeHandler extends DemandCSPointsTo.IncomingEdgeHandler {
               public void handleAlloc(AllocNode allocNode, DemandCSPointsTo.VarAndContext origVarAndContext) {
                  DemandCSPointsTo.VarContextAndUp contextAndUp = (DemandCSPointsTo.VarContextAndUp)origVarAndContext;
                  if (allocNode == alloc) {
                     ImmutableStack toAdd;
                     if (allocContext.topMatches(contextAndUp.context)) {
                        toAdd = contextAndUp.upContext.reverse();
                        ImmutableStack<Integer> toAddx = allocContext.popAll(contextAndUp.context).pushAll(toAdd);
                        if (DemandCSPointsTo.DEBUG) {
                           DemandCSPointsTo.this.debugPrint("found up context " + toAddx);
                        }

                        ret.add(toAddx);
                     } else if (contextAndUp.context.topMatches(allocContext)) {
                        toAdd = contextAndUp.upContext.reverse();
                        if (DemandCSPointsTo.DEBUG) {
                           DemandCSPointsTo.this.debugPrint("found up context " + toAdd);
                        }

                        ret.add(toAdd);
                     }
                  }

               }

               public void handleMatchSrc(VarNode matchSrc, PointsToSetInternal intersection, VarNode loadBase, VarNode storeBase, DemandCSPointsTo.VarAndContext origVarAndContext, SparkField field, boolean refine) {
                  DemandCSPointsTo.VarContextAndUp contextAndUp = (DemandCSPointsTo.VarContextAndUp)origVarAndContext;
                  if (DemandCSPointsTo.DEBUG) {
                     DemandCSPointsTo.this.debugPrint("CHECKING " + alloc);
                  }

                  PointsToSetInternal tmp = new HybridPointsToSet(alloc.getType(), DemandCSPointsTo.this.pag);
                  tmp.add(alloc);
                  AllocAndContextSet allocContexts = DemandCSPointsTo.this.findContextsForAllocs(new DemandCSPointsTo.VarAndContext(matchSrc, DemandCSPointsTo.EMPTY_CALLSTACK), tmp);
                  if (!refine) {
                     if (!allocContexts.isEmpty()) {
                        ret.add(contextAndUp.upContext.reverse());
                     }
                  } else if (!allocContexts.isEmpty()) {
                     Iterator var11 = allocContexts.iterator();

                     label50:
                     while(true) {
                        ImmutableStack discoveredAllocContext;
                        do {
                           if (!var11.hasNext()) {
                              return;
                           }

                           AllocAndContext t = (AllocAndContext)var11.next();
                           discoveredAllocContext = t.context;
                        } while(!allocContext.topMatches(discoveredAllocContext));

                        ImmutableStack<Integer> trueAllocContext = allocContext.popAll(discoveredAllocContext);
                        AllocAndContextSet allocAndContexts = DemandCSPointsTo.this.findContextsForAllocs(new DemandCSPointsTo.VarAndContext(storeBase, trueAllocContext), intersection);
                        Iterator var16 = allocAndContexts.iterator();

                        while(true) {
                           while(true) {
                              if (!var16.hasNext()) {
                                 continue label50;
                              }

                              AllocAndContext allocAndContext = (AllocAndContext)var16.next();
                              if (DemandCSPointsTo.this.fieldCheckHeuristic.validFromBothEnds(field)) {
                                 ret.addAll(DemandCSPointsTo.this.findUpContextsForVar(allocAndContext, new DemandCSPointsTo.VarContextAndUp(loadBase, contextAndUp.context, contextAndUp.upContext)));
                              } else {
                                 DemandCSPointsTo.CallingContextSet tmpContexts = DemandCSPointsTo.this.findVarContextsFromAlloc(allocAndContext, loadBase);
                                 Iterator var19 = tmpContexts.iterator();

                                 while(var19.hasNext()) {
                                    ImmutableStack<Integer> tmpContext = (ImmutableStack)var19.next();
                                    if (tmpContext.topMatches(contextAndUp.context)) {
                                       ImmutableStack<Integer> reverse = contextAndUp.upContext.reverse();
                                       ImmutableStack<Integer> toAdd = tmpContext.popAll(contextAndUp.context).pushAll(reverse);
                                       ret.add(toAdd);
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }

               }

               Object getResult() {
                  return ret;
               }

               void handleAssignSrc(DemandCSPointsTo.VarAndContext newVarAndContext, DemandCSPointsTo.VarAndContext origVarAndContext, AssignEdge assignEdge) {
                  DemandCSPointsTo.VarContextAndUp contextAndUp = (DemandCSPointsTo.VarContextAndUp)origVarAndContext;
                  ImmutableStack<Integer> upContext = contextAndUp.upContext;
                  ImmutableStack<Integer> newUpContext = upContext;
                  if (assignEdge.isParamEdge() && contextAndUp.context.isEmpty() && upContext.size() < ImmutableStack.getMaxSize()) {
                     newUpContext = DemandCSPointsTo.this.pushWithRecursionCheck(upContext, assignEdge);
                  }

                  p.prop(new DemandCSPointsTo.VarContextAndUp(newVarAndContext.var, newVarAndContext.context, newUpContext));
               }

               boolean shouldHandleSrc(VarNode src) {
                  if (src instanceof GlobalVarNode) {
                     throw new TerminateEarlyException();
                  } else {
                     return src.getP2Set().contains(alloc);
                  }
               }
            }

            UpContextEdgeHandler edgeHandler = new UpContextEdgeHandler();
            this.processIncomingEdges(edgeHandler, worklist);
            --this.nesting;
            return ret;
         } catch (CallSiteException var11) {
            this.upContextCache.remove(varContextAndUp);
            throw var11;
         }
      }
   }

   protected DemandCSPointsTo.CallingContextSet findVarContextsFromAlloc(AllocAndContext allocAndContext, VarNode targetVar) {
      DemandCSPointsTo.CallingContextSet tmpSet = this.checkAllocAndContextCache(allocAndContext, targetVar);
      if (tmpSet != null) {
         return tmpSet;
      } else {
         DemandCSPointsTo.CallingContextSet ret = new DemandCSPointsTo.CallingContextSet();
         ((Map)this.allocAndContextCache.get(allocAndContext)).put(targetVar, ret);

         try {
            HashSet<DemandCSPointsTo.VarAndContext> marked = new HashSet();
            Stack<DemandCSPointsTo.VarAndContext> worklist = new Stack();
            Propagator<DemandCSPointsTo.VarAndContext> p = new Propagator(marked, worklist);
            AllocNode alloc = allocAndContext.alloc;
            ImmutableStack<Integer> allocContext = allocAndContext.context;
            Node[] newBarNodes = this.pag.allocLookup(alloc);

            VarNode curVar;
            for(int i = 0; i < newBarNodes.length; ++i) {
               curVar = (VarNode)newBarNodes[i];
               p.prop(new DemandCSPointsTo.VarAndContext(curVar, allocContext));
            }

            label132:
            while(!worklist.isEmpty()) {
               this.incrementNodesTraversed();
               DemandCSPointsTo.VarAndContext curVarAndContext = (DemandCSPointsTo.VarAndContext)worklist.pop();
               if (DEBUG) {
                  this.debugPrint("looking at " + curVarAndContext);
               }

               curVar = curVarAndContext.var;
               ImmutableStack<Integer> curContext = curVarAndContext.context;
               if (curVar == targetVar) {
                  ret.add(curContext);
               }

               Collection<AssignEdge> assignEdges = this.filterAssigns(curVar, curContext, false, true);
               Iterator var15 = assignEdges.iterator();

               while(true) {
                  AssignEdge assignEdge;
                  VarNode dst;
                  ImmutableStack newContext;
                  Set targets;
                  do {
                     if (!var15.hasNext()) {
                        Set<VarNode> matchTargets = this.vMatches.vMatchLookup(curVar);
                        Node[] pfTargets = this.pag.storeLookup(curVar);

                        label130:
                        for(int i = 0; i < pfTargets.length; ++i) {
                           FieldRefNode frNode = (FieldRefNode)pfTargets[i];
                           VarNode storeBase = frNode.getBase();
                           SparkField field = frNode.getField();
                           Iterator var21 = this.fieldToLoads.get(field).iterator();

                           while(true) {
                              while(true) {
                                 VarNode loadBase;
                                 PointsToSetInternal loadBaseP2Set;
                                 PointsToSetInternal storeBaseP2Set;
                                 VarNode matchTgt;
                                 do {
                                    if (!var21.hasNext()) {
                                       continue label130;
                                    }

                                    Pair<VarNode, VarNode> load = (Pair)var21.next();
                                    loadBase = (VarNode)load.getO2();
                                    loadBaseP2Set = loadBase.getP2Set();
                                    storeBaseP2Set = storeBase.getP2Set();
                                    matchTgt = (VarNode)load.getO1();
                                 } while(!matchTargets.contains(matchTgt));

                                 if (DEBUG) {
                                    this.debugPrint("match source " + matchTgt);
                                 }

                                 PointsToSetInternal intersection = SootUtil.constructIntersection(storeBaseP2Set, loadBaseP2Set, this.pag);
                                 boolean checkField = this.fieldCheckHeuristic.validateMatchesForField(field);
                                 if (checkField) {
                                    AllocAndContextSet sharedAllocContexts = this.findContextsForAllocs(new DemandCSPointsTo.VarAndContext(storeBase, curContext), intersection);
                                    Iterator var30 = sharedAllocContexts.iterator();

                                    while(var30.hasNext()) {
                                       AllocAndContext curAllocAndContext = (AllocAndContext)var30.next();
                                       DemandCSPointsTo.CallingContextSet upContexts;
                                       if (this.fieldCheckHeuristic.validFromBothEnds(field)) {
                                          upContexts = this.findUpContextsForVar(curAllocAndContext, new DemandCSPointsTo.VarContextAndUp(loadBase, EMPTY_CALLSTACK, EMPTY_CALLSTACK));
                                       } else {
                                          upContexts = this.findVarContextsFromAlloc(curAllocAndContext, loadBase);
                                       }

                                       Iterator var33 = upContexts.iterator();

                                       while(var33.hasNext()) {
                                          ImmutableStack<Integer> upContext = (ImmutableStack)var33.next();
                                          p.prop(new DemandCSPointsTo.VarAndContext(matchTgt, upContext));
                                       }
                                    }
                                 } else {
                                    p.prop(new DemandCSPointsTo.VarAndContext(matchTgt, EMPTY_CALLSTACK));
                                 }
                              }
                           }
                        }
                        continue label132;
                     }

                     assignEdge = (AssignEdge)var15.next();
                     dst = assignEdge.getDst();
                     newContext = curContext;
                     if (assignEdge.isReturnEdge()) {
                        if (!curContext.isEmpty()) {
                           if (!this.callEdgeInSCC(assignEdge)) {
                              assert assignEdge.getCallSite().equals(curContext.peek()) : assignEdge + " " + curContext;

                              newContext = curContext.pop();
                           } else {
                              newContext = this.popRecursiveCallSites(curContext);
                           }
                        }
                     } else if (assignEdge.isParamEdge()) {
                        if (DEBUG) {
                           this.debugPrint("entering call site " + assignEdge.getCallSite());
                        }

                        newContext = this.pushWithRecursionCheck(curContext, assignEdge);
                     }

                     if (!assignEdge.isReturnEdge() || !curContext.isEmpty() || !this.csInfo.isVirtCall(assignEdge.getCallSite())) {
                        break;
                     }

                     targets = this.refineCallSite(assignEdge.getCallSite(), newContext);
                  } while(!targets.contains(((LocalVarNode)assignEdge.getDst()).getMethod()));

                  if (dst instanceof GlobalVarNode) {
                     newContext = EMPTY_CALLSTACK;
                  }

                  p.prop(new DemandCSPointsTo.VarAndContext(dst, newContext));
               }
            }

            return ret;
         } catch (CallSiteException var35) {
            this.allocAndContextCache.remove(allocAndContext);
            throw var35;
         }
      }
   }

   protected Set<SootMethod> getCallTargets(PointsToSetInternal p2Set, NumberedString methodStr, Type receiverType, Set<SootMethod> possibleTargets) {
      List<Object> args = Arrays.asList(p2Set, methodStr, receiverType, possibleTargets);
      if (this.callTargetsArgCache.containsKey(args)) {
         return (Set)this.callTargetsArgCache.get(args);
      } else {
         Set<Type> types = p2Set.possibleTypes();
         Set<SootMethod> ret = new HashSet();
         Iterator var8 = types.iterator();

         while(var8.hasNext()) {
            Type type = (Type)var8.next();
            ret.addAll(this.getCallTargetsForType(type, methodStr, receiverType, possibleTargets));
         }

         this.callTargetsArgCache.put(args, ret);
         return ret;
      }
   }

   protected Set<SootMethod> getCallTargetsForType(Type type, NumberedString methodStr, Type receiverType, Set<SootMethod> possibleTargets) {
      if (!this.pag.getTypeManager().castNeverFails((Type)type, receiverType)) {
         return Collections.emptySet();
      } else if (type instanceof AnySubType) {
         AnySubType any = (AnySubType)type;
         RefType refType = any.getBase();
         return !this.pag.getTypeManager().getFastHierarchy().canStoreType(receiverType, refType) && !this.pag.getTypeManager().getFastHierarchy().canStoreType(refType, receiverType) ? Collections.emptySet() : possibleTargets;
      } else {
         if (type instanceof ArrayType) {
            type = Scene.v().getSootClass("java.lang.Object").getType();
         }

         RefType refType = (RefType)type;
         SootMethod targetMethod = null;
         targetMethod = VirtualCalls.v().resolveNonSpecial(refType, methodStr);
         return Collections.singleton(targetMethod);
      }
   }

   protected Set<VarNode> getFlowsToHelper(AllocAndContext allocAndContext) {
      ArraySet ret = new ArraySet();

      try {
         HashSet<DemandCSPointsTo.VarAndContext> marked = new HashSet();
         Stack<DemandCSPointsTo.VarAndContext> worklist = new Stack();
         Propagator<DemandCSPointsTo.VarAndContext> p = new Propagator(marked, worklist);
         AllocNode alloc = allocAndContext.alloc;
         ImmutableStack<Integer> allocContext = allocAndContext.context;
         Node[] newBarNodes = this.pag.allocLookup(alloc);

         VarNode curVar;
         for(int i = 0; i < newBarNodes.length; ++i) {
            curVar = (VarNode)newBarNodes[i];
            ret.add(curVar);
            p.prop(new DemandCSPointsTo.VarAndContext(curVar, allocContext));
         }

         label126:
         while(!worklist.isEmpty()) {
            this.incrementNodesTraversed();
            DemandCSPointsTo.VarAndContext curVarAndContext = (DemandCSPointsTo.VarAndContext)worklist.pop();
            if (DEBUG) {
               this.debugPrint("looking at " + curVarAndContext);
            }

            curVar = curVarAndContext.var;
            ImmutableStack<Integer> curContext = curVarAndContext.context;
            ret.add(curVar);
            Collection<AssignEdge> assignEdges = this.filterAssigns(curVar, curContext, false, true);
            Iterator var13 = assignEdges.iterator();

            while(true) {
               AssignEdge assignEdge;
               VarNode dst;
               ImmutableStack newContext;
               Set targets;
               do {
                  if (!var13.hasNext()) {
                     Set<VarNode> matchTargets = this.vMatches.vMatchLookup(curVar);
                     Node[] pfTargets = this.pag.storeLookup(curVar);

                     label124:
                     for(int i = 0; i < pfTargets.length; ++i) {
                        FieldRefNode frNode = (FieldRefNode)pfTargets[i];
                        VarNode storeBase = frNode.getBase();
                        SparkField field = frNode.getField();
                        Iterator var19 = this.fieldToLoads.get(field).iterator();

                        while(true) {
                           while(true) {
                              VarNode loadBase;
                              PointsToSetInternal loadBaseP2Set;
                              PointsToSetInternal storeBaseP2Set;
                              VarNode matchTgt;
                              do {
                                 if (!var19.hasNext()) {
                                    continue label124;
                                 }

                                 Pair<VarNode, VarNode> load = (Pair)var19.next();
                                 loadBase = (VarNode)load.getO2();
                                 loadBaseP2Set = loadBase.getP2Set();
                                 storeBaseP2Set = storeBase.getP2Set();
                                 matchTgt = (VarNode)load.getO1();
                              } while(!matchTargets.contains(matchTgt));

                              if (DEBUG) {
                                 this.debugPrint("match source " + matchTgt);
                              }

                              PointsToSetInternal intersection = SootUtil.constructIntersection(storeBaseP2Set, loadBaseP2Set, this.pag);
                              boolean checkField = this.fieldCheckHeuristic.validateMatchesForField(field);
                              if (checkField) {
                                 AllocAndContextSet sharedAllocContexts = this.findContextsForAllocs(new DemandCSPointsTo.VarAndContext(storeBase, curContext), intersection);
                                 Iterator var28 = sharedAllocContexts.iterator();

                                 while(var28.hasNext()) {
                                    AllocAndContext curAllocAndContext = (AllocAndContext)var28.next();
                                    DemandCSPointsTo.CallingContextSet upContexts;
                                    if (this.fieldCheckHeuristic.validFromBothEnds(field)) {
                                       upContexts = this.findUpContextsForVar(curAllocAndContext, new DemandCSPointsTo.VarContextAndUp(loadBase, EMPTY_CALLSTACK, EMPTY_CALLSTACK));
                                    } else {
                                       upContexts = this.findVarContextsFromAlloc(curAllocAndContext, loadBase);
                                    }

                                    Iterator var31 = upContexts.iterator();

                                    while(var31.hasNext()) {
                                       ImmutableStack<Integer> upContext = (ImmutableStack)var31.next();
                                       p.prop(new DemandCSPointsTo.VarAndContext(matchTgt, upContext));
                                    }
                                 }
                              } else {
                                 p.prop(new DemandCSPointsTo.VarAndContext(matchTgt, EMPTY_CALLSTACK));
                              }
                           }
                        }
                     }
                     continue label126;
                  }

                  assignEdge = (AssignEdge)var13.next();
                  dst = assignEdge.getDst();
                  newContext = curContext;
                  if (assignEdge.isReturnEdge()) {
                     if (!curContext.isEmpty()) {
                        if (!this.callEdgeInSCC(assignEdge)) {
                           assert assignEdge.getCallSite().equals(curContext.peek()) : assignEdge + " " + curContext;

                           newContext = curContext.pop();
                        } else {
                           newContext = this.popRecursiveCallSites(curContext);
                        }
                     }
                  } else if (assignEdge.isParamEdge()) {
                     if (DEBUG) {
                        this.debugPrint("entering call site " + assignEdge.getCallSite());
                     }

                     newContext = this.pushWithRecursionCheck(curContext, assignEdge);
                  }

                  if (!assignEdge.isReturnEdge() || !curContext.isEmpty() || !this.csInfo.isVirtCall(assignEdge.getCallSite())) {
                     break;
                  }

                  targets = this.refineCallSite(assignEdge.getCallSite(), newContext);
               } while(!targets.contains(((LocalVarNode)assignEdge.getDst()).getMethod()));

               if (dst instanceof GlobalVarNode) {
                  newContext = EMPTY_CALLSTACK;
               }

               p.prop(new DemandCSPointsTo.VarAndContext(dst, newContext));
            }
         }

         return ret;
      } catch (CallSiteException var33) {
         this.allocAndContextCache.remove(allocAndContext);
         throw var33;
      }
   }

   protected int getMaxPasses() {
      return this.maxPasses;
   }

   protected void incrementNodesTraversed() {
      ++this.numNodesTraversed;
      if (this.numNodesTraversed > this.maxNodesPerPass) {
         throw new TerminateEarlyException();
      }
   }

   protected boolean isRecursive(ImmutableStack<Integer> context, AssignEdge assignEdge) {
      boolean sameSCCAlready = this.callEdgeInSCC(assignEdge);
      if (sameSCCAlready) {
         return true;
      } else {
         Integer callSite = assignEdge.getCallSite();
         if (!context.contains(callSite)) {
            return false;
         } else {
            Set<SootMethod> toBeCollapsed = new ArraySet();

            int callSiteInd;
            for(callSiteInd = 0; callSiteInd < context.size() && !((Integer)context.get(callSiteInd)).equals(callSite); ++callSiteInd) {
            }

            while(callSiteInd < context.size()) {
               toBeCollapsed.add(this.csInfo.getInvokingMethod((Integer)context.get(callSiteInd)));
               ++callSiteInd;
            }

            this.sccManager.makeSameSCC(toBeCollapsed);
            return true;
         }
      }
   }

   protected boolean isRecursiveCallSite(Integer callSite) {
      SootMethod invokingMethod = this.csInfo.getInvokingMethod(callSite);
      SootMethod invokedMethod = this.csInfo.getInvokedMethod(callSite);
      return this.sccManager.inSameSCC(invokingMethod, invokedMethod);
   }

   protected Set<VarNode> nodesPropagatedThrough(VarNode source, PointsToSetInternal allocs) {
      Set<VarNode> marked = new HashSet();
      Stack<VarNode> worklist = new Stack();
      Propagator<VarNode> p = new Propagator(marked, worklist);
      p.prop(source);

      while(!worklist.isEmpty()) {
         VarNode curNode = (VarNode)worklist.pop();
         Node[] assignSources = this.pag.simpleInvLookup(curNode);

         for(int i = 0; i < assignSources.length; ++i) {
            VarNode assignSrc = (VarNode)assignSources[i];
            if (assignSrc.getP2Set().hasNonEmptyIntersection(allocs)) {
               p.prop(assignSrc);
            }
         }

         Set<VarNode> matchSources = this.vMatches.vMatchInvLookup(curNode);
         Iterator var12 = matchSources.iterator();

         while(var12.hasNext()) {
            VarNode matchSrc = (VarNode)var12.next();
            if (matchSrc.getP2Set().hasNonEmptyIntersection(allocs)) {
               p.prop(matchSrc);
            }
         }
      }

      return marked;
   }

   protected ImmutableStack<Integer> popRecursiveCallSites(ImmutableStack<Integer> context) {
      ImmutableStack ret;
      for(ret = context; !ret.isEmpty() && this.isRecursiveCallSite((Integer)ret.peek()); ret = ret.pop()) {
      }

      return ret;
   }

   protected void processIncomingEdges(DemandCSPointsTo.IncomingEdgeHandler h, Stack<DemandCSPointsTo.VarAndContext> worklist) {
      label113:
      while(!worklist.isEmpty()) {
         this.incrementNodesTraversed();
         DemandCSPointsTo.VarAndContext varAndContext = (DemandCSPointsTo.VarAndContext)worklist.pop();
         if (DEBUG) {
            this.debugPrint("looking at " + varAndContext);
         }

         VarNode v = varAndContext.var;
         ImmutableStack<Integer> callingContext = varAndContext.context;
         Node[] newEdges = this.pag.allocInvLookup(v);

         for(int i = 0; i < newEdges.length; ++i) {
            AllocNode allocNode = (AllocNode)newEdges[i];
            h.handleAlloc(allocNode, varAndContext);
            if (h.terminate()) {
               return;
            }
         }

         Collection<AssignEdge> assigns = this.filterAssigns(v, callingContext, true, true);
         Iterator var23 = assigns.iterator();

         while(true) {
            while(true) {
               AssignEdge assignEdge;
               VarNode src;
               do {
                  if (!var23.hasNext()) {
                     Set<VarNode> matchSources = this.vMatches.vMatchInvLookup(v);
                     Node[] loads = this.pag.loadInvLookup(v);
                     int i = 0;

                     while(true) {
                        if (i >= loads.length) {
                           continue label113;
                        }

                        FieldRefNode frNode = (FieldRefNode)loads[i];
                        VarNode loadBase = frNode.getBase();
                        SparkField field = frNode.getField();
                        Iterator var30 = this.fieldToStores.get(field).iterator();

                        while(var30.hasNext()) {
                           Pair<VarNode, VarNode> store = (Pair)var30.next();
                           VarNode storeBase = (VarNode)store.getO2();
                           PointsToSetInternal storeBaseP2Set = storeBase.getP2Set();
                           PointsToSetInternal loadBaseP2Set = loadBase.getP2Set();
                           VarNode matchSrc = (VarNode)store.getO1();
                           if (matchSources.contains(matchSrc) && h.shouldHandleSrc(matchSrc)) {
                              if (DEBUG) {
                                 this.debugPrint("match source " + matchSrc);
                              }

                              PointsToSetInternal intersection = SootUtil.constructIntersection(storeBaseP2Set, loadBaseP2Set, this.pag);
                              boolean checkGetfield = this.fieldCheckHeuristic.validateMatchesForField(field);
                              h.handleMatchSrc(matchSrc, intersection, loadBase, storeBase, varAndContext, field, checkGetfield);
                              if (h.terminate()) {
                                 return;
                              }
                           }
                        }

                        ++i;
                     }
                  }

                  assignEdge = (AssignEdge)var23.next();
                  src = assignEdge.getSrc();
               } while(!h.shouldHandleSrc(src));

               ImmutableStack<Integer> newContext = callingContext;
               if (assignEdge.isParamEdge()) {
                  if (!callingContext.isEmpty()) {
                     if (!this.callEdgeInSCC(assignEdge)) {
                        assert assignEdge.getCallSite().equals(callingContext.peek()) : assignEdge + " " + callingContext;

                        newContext = callingContext.pop();
                     } else {
                        newContext = this.popRecursiveCallSites(callingContext);
                     }
                  }
               } else if (assignEdge.isReturnEdge()) {
                  if (DEBUG) {
                     this.debugPrint("entering call site " + assignEdge.getCallSite());
                  }

                  newContext = this.pushWithRecursionCheck(callingContext, assignEdge);
               }

               if (assignEdge.isParamEdge()) {
                  Integer callSite = assignEdge.getCallSite();
                  if (this.csInfo.isVirtCall(callSite) && !this.weirdCall(callSite)) {
                     Set<SootMethod> targets = this.refineCallSite(callSite, newContext);
                     if (DEBUG) {
                        this.debugPrint(targets.toString());
                     }

                     SootMethod targetMethod = ((LocalVarNode)assignEdge.getDst()).getMethod();
                     if (!targets.contains(targetMethod)) {
                        if (DEBUG) {
                           this.debugPrint("skipping call because of call graph");
                        }
                        continue;
                     }
                  }
               }

               if (src instanceof GlobalVarNode) {
                  newContext = EMPTY_CALLSTACK;
               }

               h.handleAssignSrc(new DemandCSPointsTo.VarAndContext(src, newContext), varAndContext, assignEdge);
               if (h.terminate()) {
                  return;
               }
            }
         }
      }

   }

   protected ImmutableStack<Integer> pushWithRecursionCheck(ImmutableStack<Integer> context, AssignEdge assignEdge) {
      boolean foundRecursion = this.callEdgeInSCC(assignEdge);
      if (!foundRecursion) {
         Integer callSite = assignEdge.getCallSite();
         if (context.contains(callSite)) {
            foundRecursion = true;
            if (DEBUG) {
               this.debugPrint("RECURSION!!!");
            }

            throw new TerminateEarlyException();
         }
      }

      if (foundRecursion) {
         ImmutableStack<Integer> popped = this.popRecursiveCallSites(context);
         if (DEBUG) {
            this.debugPrint("popped stack " + popped);
         }

         return popped;
      } else {
         return context.push(assignEdge.getCallSite());
      }
   }

   protected boolean refineAlias(VarNode v1, VarNode v2, PointsToSetInternal intersection, HeuristicType heuristic) {
      if (this.refineAliasInternal(v1, v2, intersection, heuristic)) {
         return true;
      } else {
         return this.refineAliasInternal(v2, v1, intersection, heuristic);
      }
   }

   protected boolean refineAliasInternal(VarNode v1, VarNode v2, PointsToSetInternal intersection, HeuristicType heuristic) {
      this.fieldCheckHeuristic = HeuristicType.getHeuristic(heuristic, this.pag.getTypeManager(), this.getMaxPasses());
      this.numPasses = 0;

      do {
         ++this.numPasses;
         if (this.numPasses > this.maxPasses) {
            return false;
         }

         if (DEBUG) {
            logger.debug("PASS " + this.numPasses);
            logger.debug("" + this.fieldCheckHeuristic);
         }

         this.clearState();
         boolean success = false;

         try {
            AllocAndContextSet allocAndContexts = this.findContextsForAllocs(new DemandCSPointsTo.VarAndContext(v1, EMPTY_CALLSTACK), intersection);
            boolean emptyIntersection = true;
            Iterator var8 = allocAndContexts.iterator();

            while(var8.hasNext()) {
               AllocAndContext allocAndContext = (AllocAndContext)var8.next();
               DemandCSPointsTo.CallingContextSet upContexts = this.findUpContextsForVar(allocAndContext, new DemandCSPointsTo.VarContextAndUp(v2, EMPTY_CALLSTACK, EMPTY_CALLSTACK));
               if (!upContexts.isEmpty()) {
                  emptyIntersection = false;
                  break;
               }
            }

            success = emptyIntersection;
         } catch (TerminateEarlyException var11) {
            success = false;
         }

         if (success) {
            logger.debug("took " + this.numPasses + " passes");
            return true;
         }
      } while(this.fieldCheckHeuristic.runNewPass());

      return false;
   }

   protected Set<SootMethod> refineCallSite(Integer callSite, ImmutableStack<Integer> origContext) {
      DemandCSPointsTo.CallSiteAndContext callSiteAndContext = new DemandCSPointsTo.CallSiteAndContext(callSite, origContext);
      if (this.queriedCallSites.contains(callSiteAndContext)) {
         return this.callSiteToResolvedTargets.get(callSiteAndContext);
      } else if (this.callGraphStack.contains(callSiteAndContext)) {
         return Collections.emptySet();
      } else {
         this.callGraphStack.push(callSiteAndContext);
         VarNode receiver = this.csInfo.getReceiverForVirtCallSite(callSite);
         Type receiverType = receiver.getType();
         SootMethod invokedMethod = this.csInfo.getInvokedMethod(callSite);
         NumberedString methodSig = invokedMethod.getNumberedSubSignature();
         Set<SootMethod> allTargets = this.csInfo.getCallSiteTargets(callSite);
         if (!this.refineCallGraph) {
            this.callGraphStack.pop();
            return allTargets;
         } else {
            if (DEBUG_VIRT) {
               this.debugPrint("refining call to " + invokedMethod + " on " + receiver + " " + origContext);
            }

            final HashSet<DemandCSPointsTo.VarAndContext> marked = new HashSet();
            final Stack<DemandCSPointsTo.VarAndContext> worklist = new Stack();

            final class Helper {
               void prop(DemandCSPointsTo.VarAndContext varAndContext) {
                  if (marked.add(varAndContext)) {
                     worklist.push(varAndContext);
                  }

               }
            }

            Helper h = new Helper();
            h.prop(new DemandCSPointsTo.VarAndContext(receiver, origContext));

            while(!worklist.isEmpty()) {
               this.incrementNodesTraversed();
               DemandCSPointsTo.VarAndContext curVarAndContext = (DemandCSPointsTo.VarAndContext)worklist.pop();
               if (DEBUG_VIRT) {
                  this.debugPrint("virt looking at " + curVarAndContext);
               }

               VarNode curVar = curVarAndContext.var;
               ImmutableStack<Integer> curContext = curVarAndContext.context;
               Node[] newNodes = this.pag.allocInvLookup(curVar);

               for(int i = 0; i < newNodes.length; ++i) {
                  AllocNode allocNode = (AllocNode)newNodes[i];
                  Iterator var18 = this.getCallTargetsForType(allocNode.getType(), methodSig, receiverType, allTargets).iterator();

                  while(var18.hasNext()) {
                     SootMethod method = (SootMethod)var18.next();
                     this.callSiteToResolvedTargets.put(callSiteAndContext, method);
                  }
               }

               Collection<AssignEdge> assigns = this.filterAssigns(curVar, curContext, true, true);
               Iterator var46 = assigns.iterator();

               while(true) {
                  while(var46.hasNext()) {
                     AssignEdge assignEdge = (AssignEdge)var46.next();
                     VarNode src = assignEdge.getSrc();
                     ImmutableStack<Integer> newContext = curContext;
                     if (assignEdge.isParamEdge()) {
                        if (curContext.isEmpty()) {
                           this.callSiteToResolvedTargets.putAll(callSiteAndContext, allTargets);
                           continue;
                        }

                        if (!this.callEdgeInSCC(assignEdge)) {
                           assert assignEdge.getCallSite().equals(curContext.peek());

                           newContext = curContext.pop();
                        } else {
                           newContext = this.popRecursiveCallSites(curContext);
                        }
                     } else if (assignEdge.isReturnEdge()) {
                        newContext = this.pushWithRecursionCheck(curContext, assignEdge);
                     } else if (src instanceof GlobalVarNode) {
                        newContext = EMPTY_CALLSTACK;
                     }

                     h.prop(new DemandCSPointsTo.VarAndContext(src, newContext));
                  }

                  Set<VarNode> matchSources = this.vMatches.vMatchInvLookup(curVar);
                  boolean oneMatch = matchSources.size() == 1;
                  Node[] loads = this.pag.loadInvLookup(curVar);

                  label346:
                  for(int i = 0; i < loads.length; ++i) {
                     FieldRefNode frNode = (FieldRefNode)loads[i];
                     VarNode loadBase = frNode.getBase();
                     SparkField field = frNode.getField();
                     Iterator var24 = this.fieldToStores.get(field).iterator();

                     while(true) {
                        VarNode storeBase;
                        VarNode matchSrc;
                        AllocAndContextSet allocContexts;
                        while(true) {
                           PointsToSetInternal storeBaseP2Set;
                           PointsToSetInternal loadBaseP2Set;
                           boolean skipMatch;
                           PointsToSetInternal intersection;
                           Set matchSrcCallTargets;
                           do {
                              do {
                                 if (!var24.hasNext()) {
                                    continue label346;
                                 }

                                 Pair<VarNode, VarNode> store = (Pair)var24.next();
                                 storeBase = (VarNode)store.getO2();
                                 storeBaseP2Set = storeBase.getP2Set();
                                 loadBaseP2Set = loadBase.getP2Set();
                                 matchSrc = (VarNode)store.getO1();
                              } while(!matchSources.contains(matchSrc));

                              skipMatch = false;
                              if (oneMatch) {
                                 intersection = matchSrc.getP2Set();
                                 matchSrcCallTargets = this.getCallTargets(intersection, methodSig, receiverType, allTargets);
                                 if (matchSrcCallTargets.size() <= 1) {
                                    skipMatch = true;
                                    Iterator var33 = matchSrcCallTargets.iterator();

                                    while(var33.hasNext()) {
                                       SootMethod method = (SootMethod)var33.next();
                                       this.callSiteToResolvedTargets.put(callSiteAndContext, method);
                                    }
                                 }
                              }
                           } while(skipMatch);

                           intersection = SootUtil.constructIntersection(storeBaseP2Set, loadBaseP2Set, this.pag);
                           matchSrcCallTargets = null;
                           boolean oldRefining = this.refiningCallSite;
                           int oldNesting = this.nesting;

                           try {
                              this.refiningCallSite = true;
                              allocContexts = this.findContextsForAllocs(new DemandCSPointsTo.VarAndContext(loadBase, curContext), intersection);
                              break;
                           } catch (CallSiteException var43) {
                              this.callSiteToResolvedTargets.putAll(callSiteAndContext, allTargets);
                           } finally {
                              this.refiningCallSite = oldRefining;
                              this.nesting = oldNesting;
                           }
                        }

                        Iterator var35 = allocContexts.iterator();

                        while(var35.hasNext()) {
                           AllocAndContext allocAndContext = (AllocAndContext)var35.next();
                           DemandCSPointsTo.CallingContextSet matchSrcContexts;
                           if (this.fieldCheckHeuristic.validFromBothEnds(field)) {
                              matchSrcContexts = this.findUpContextsForVar(allocAndContext, new DemandCSPointsTo.VarContextAndUp(storeBase, EMPTY_CALLSTACK, EMPTY_CALLSTACK));
                           } else {
                              matchSrcContexts = this.findVarContextsFromAlloc(allocAndContext, storeBase);
                           }

                           Iterator var38 = matchSrcContexts.iterator();

                           while(var38.hasNext()) {
                              ImmutableStack<Integer> matchSrcContext = (ImmutableStack)var38.next();
                              DemandCSPointsTo.VarAndContext newVarAndContext = new DemandCSPointsTo.VarAndContext(matchSrc, matchSrcContext);
                              h.prop(newVarAndContext);
                           }
                        }
                     }
                  }
                  break;
               }
            }

            if (DEBUG_VIRT) {
               this.debugPrint("call of " + invokedMethod + " on " + receiver + " " + origContext + " goes to " + this.callSiteToResolvedTargets.get(callSiteAndContext));
            }

            this.callGraphStack.pop();
            this.queriedCallSites.add(callSiteAndContext);
            return this.callSiteToResolvedTargets.get(callSiteAndContext);
         }
      }
   }

   protected boolean refineP2Set(DemandCSPointsTo.VarAndContext varAndContext, final PointsToSetInternal badLocs) {
      ++this.nesting;
      if (DEBUG) {
         this.debugPrint("refining " + varAndContext);
      }

      Set<DemandCSPointsTo.VarAndContext> marked = new HashSet();
      Stack<DemandCSPointsTo.VarAndContext> worklist = new Stack();
      final Propagator<DemandCSPointsTo.VarAndContext> p = new Propagator(marked, worklist);
      p.prop(varAndContext);
      DemandCSPointsTo.IncomingEdgeHandler edgeHandler = new DemandCSPointsTo.IncomingEdgeHandler() {
         boolean success = true;

         public void handleAlloc(AllocNode allocNode, DemandCSPointsTo.VarAndContext origVarAndContext) {
            if (DemandCSPointsTo.this.doPointsTo && DemandCSPointsTo.this.pointsTo != null) {
               DemandCSPointsTo.this.pointsTo.add(new AllocAndContext(allocNode, origVarAndContext.context));
            } else if (badLocs.contains(allocNode)) {
               this.success = false;
            }

         }

         public void handleMatchSrc(VarNode matchSrc, PointsToSetInternal intersection, VarNode loadBase, VarNode storeBase, DemandCSPointsTo.VarAndContext origVarAndContext, SparkField field, boolean refine) {
            AllocAndContextSet allocContexts = DemandCSPointsTo.this.findContextsForAllocs(new DemandCSPointsTo.VarAndContext(loadBase, origVarAndContext.context), intersection);
            Iterator var9 = allocContexts.iterator();

            while(var9.hasNext()) {
               AllocAndContext allocAndContext = (AllocAndContext)var9.next();
               if (DemandCSPointsTo.DEBUG) {
                  DemandCSPointsTo.this.debugPrint("alloc and context " + allocAndContext);
               }

               DemandCSPointsTo.CallingContextSet matchSrcContexts;
               if (DemandCSPointsTo.this.fieldCheckHeuristic.validFromBothEnds(field)) {
                  matchSrcContexts = DemandCSPointsTo.this.findUpContextsForVar(allocAndContext, new DemandCSPointsTo.VarContextAndUp(storeBase, DemandCSPointsTo.EMPTY_CALLSTACK, DemandCSPointsTo.EMPTY_CALLSTACK));
               } else {
                  matchSrcContexts = DemandCSPointsTo.this.findVarContextsFromAlloc(allocAndContext, storeBase);
               }

               Iterator var12 = matchSrcContexts.iterator();

               while(var12.hasNext()) {
                  ImmutableStack<Integer> matchSrcContext = (ImmutableStack)var12.next();
                  if (DemandCSPointsTo.DEBUG) {
                     DemandCSPointsTo.this.debugPrint("match source context " + matchSrcContext);
                  }

                  DemandCSPointsTo.VarAndContext newVarAndContext = new DemandCSPointsTo.VarAndContext(matchSrc, matchSrcContext);
                  p.prop(newVarAndContext);
               }
            }

         }

         Object getResult() {
            return this.success;
         }

         void handleAssignSrc(DemandCSPointsTo.VarAndContext newVarAndContext, DemandCSPointsTo.VarAndContext origVarAndContext, AssignEdge assignEdge) {
            p.prop(newVarAndContext);
         }

         boolean shouldHandleSrc(VarNode src) {
            return DemandCSPointsTo.this.doPointsTo ? true : src.getP2Set().hasNonEmptyIntersection(badLocs);
         }

         boolean terminate() {
            return !this.success;
         }
      };
      this.processIncomingEdges(edgeHandler, worklist);
      --this.nesting;
      return (Boolean)edgeHandler.getResult();
   }

   protected boolean refineP2Set(VarNode v, PointsToSetInternal badLocs, HeuristicType heuristic) {
      this.doPointsTo = false;
      this.fieldCheckHeuristic = HeuristicType.getHeuristic(heuristic, this.pag.getTypeManager(), this.getMaxPasses());
      this.numPasses = 0;

      do {
         ++this.numPasses;
         if (this.numPasses > this.maxPasses) {
            return false;
         }

         if (DEBUG) {
            logger.debug("PASS " + this.numPasses);
            logger.debug("" + this.fieldCheckHeuristic);
         }

         this.clearState();
         boolean success = false;

         try {
            success = this.refineP2Set(new DemandCSPointsTo.VarAndContext(v, EMPTY_CALLSTACK), badLocs);
         } catch (TerminateEarlyException var6) {
            success = false;
         }

         if (success) {
            return true;
         }
      } while(this.fieldCheckHeuristic.runNewPass());

      return false;
   }

   protected boolean weirdCall(Integer callSite) {
      SootMethod invokedMethod = this.csInfo.getInvokedMethod(callSite);
      return SootUtil.isThreadStartMethod(invokedMethod) || SootUtil.isNewInstanceMethod(invokedMethod);
   }

   public PointsToSet reachingObjects(Context c, Local l) {
      throw new UnsupportedOperationException();
   }

   public PointsToSet reachingObjects(Context c, Local l, SootField f) {
      throw new UnsupportedOperationException();
   }

   public PointsToSet reachingObjects(Local l, SootField f) {
      throw new UnsupportedOperationException();
   }

   public PointsToSet reachingObjects(PointsToSet s, SootField f) {
      throw new UnsupportedOperationException();
   }

   public PointsToSet reachingObjects(SootField f) {
      throw new UnsupportedOperationException();
   }

   public PointsToSet reachingObjectsOfArrayElement(PointsToSet s) {
      throw new UnsupportedOperationException();
   }

   public PAG getPAG() {
      return this.pag;
   }

   public boolean usesCache() {
      return this.useCache;
   }

   public void enableCache() {
      this.useCache = true;
   }

   public void disableCache() {
      this.useCache = false;
   }

   public void clearCache() {
      this.reachingObjectsCache.clear();
      this.reachingObjectsCacheNoCGRefinement.clear();
   }

   public boolean isRefineCallGraph() {
      return this.refineCallGraph;
   }

   public void setRefineCallGraph(boolean refineCallGraph) {
      this.refineCallGraph = refineCallGraph;
   }

   public HeuristicType getHeuristicType() {
      return this.heuristicType;
   }

   public void setHeuristicType(HeuristicType heuristicType) {
      this.heuristicType = heuristicType;
      this.clearCache();
   }

   static {
      DEBUG_VIRT = DEBUG;
      EMPTY_CALLSTACK = ImmutableStack.emptyStack();
   }

   protected static final class VarContextAndUp extends DemandCSPointsTo.VarAndContext {
      final ImmutableStack<Integer> upContext;

      public VarContextAndUp(VarNode var, ImmutableStack<Integer> context, ImmutableStack<Integer> upContext) {
         super(var, context);
         this.upContext = upContext;
      }

      public boolean equals(Object o) {
         if (o != null && o.getClass() == DemandCSPointsTo.VarContextAndUp.class) {
            DemandCSPointsTo.VarContextAndUp other = (DemandCSPointsTo.VarContextAndUp)o;
            return this.var.equals(other.var) && this.context.equals(other.context) && this.upContext.equals(other.upContext);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.var.hashCode() + this.context.hashCode() + this.upContext.hashCode();
      }

      public String toString() {
         return this.var + " " + this.context + " up " + this.upContext;
      }
   }

   protected static class VarAndContext {
      final ImmutableStack<Integer> context;
      final VarNode var;

      public VarAndContext(VarNode var, ImmutableStack<Integer> context) {
         assert var != null;

         assert context != null;

         this.var = var;
         this.context = context;
      }

      public boolean equals(Object o) {
         if (o != null && o.getClass() == DemandCSPointsTo.VarAndContext.class) {
            DemandCSPointsTo.VarAndContext other = (DemandCSPointsTo.VarAndContext)o;
            return this.var.equals(other.var) && this.context.equals(other.context);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.var.hashCode() + this.context.hashCode();
      }

      public String toString() {
         return this.var + " " + this.context;
      }
   }

   protected abstract static class IncomingEdgeHandler {
      public abstract void handleAlloc(AllocNode var1, DemandCSPointsTo.VarAndContext var2);

      public abstract void handleMatchSrc(VarNode var1, PointsToSetInternal var2, VarNode var3, VarNode var4, DemandCSPointsTo.VarAndContext var5, SparkField var6, boolean var7);

      abstract Object getResult();

      abstract void handleAssignSrc(DemandCSPointsTo.VarAndContext var1, DemandCSPointsTo.VarAndContext var2, AssignEdge var3);

      abstract boolean shouldHandleSrc(VarNode var1);

      boolean terminate() {
         return false;
      }
   }

   protected static final class CallSiteToTargetsMap extends HashSetMultiMap<DemandCSPointsTo.CallSiteAndContext, SootMethod> {
   }

   protected static final class CallSiteAndContext extends Pair<Integer, ImmutableStack<Integer>> {
      public CallSiteAndContext(Integer callSite, ImmutableStack<Integer> callingContext) {
         super(callSite, callingContext);
      }
   }

   protected static final class CallingContextSet extends ArraySet<ImmutableStack<Integer>> {
   }

   protected static final class AllocAndContextCache extends HashMap<AllocAndContext, Map<VarNode, DemandCSPointsTo.CallingContextSet>> {
   }
}
