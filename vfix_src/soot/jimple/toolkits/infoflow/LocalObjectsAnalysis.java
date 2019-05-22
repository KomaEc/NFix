package soot.jimple.toolkits.infoflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.EquivalentValue;
import soot.Local;
import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Value;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.ParameterRef;
import soot.jimple.Ref;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.toolkits.graph.MutableDirectedGraph;
import soot.toolkits.scalar.Pair;

public class LocalObjectsAnalysis {
   private static final Logger logger = LoggerFactory.getLogger(LocalObjectsAnalysis.class);
   public InfoFlowAnalysis dfa;
   UseFinder uf;
   CallGraph cg;
   Map<SootClass, ClassLocalObjectsAnalysis> classToClassLocalObjectsAnalysis;
   Map<SootMethod, SmartMethodLocalObjectsAnalysis> mloaCache;
   Map<SootMethod, ReachableMethods> rmCache = new HashMap();
   Map callChainsCache = new HashMap();

   public LocalObjectsAnalysis(InfoFlowAnalysis dfa) {
      this.dfa = dfa;
      this.uf = new UseFinder();
      this.cg = Scene.v().getCallGraph();
      this.classToClassLocalObjectsAnalysis = new HashMap();
      this.mloaCache = new HashMap();
   }

   public ClassLocalObjectsAnalysis getClassLocalObjectsAnalysis(SootClass sc) {
      if (!this.classToClassLocalObjectsAnalysis.containsKey(sc)) {
         ClassLocalObjectsAnalysis cloa = this.newClassLocalObjectsAnalysis(this, this.dfa, this.uf, sc);
         this.classToClassLocalObjectsAnalysis.put(sc, cloa);
      }

      return (ClassLocalObjectsAnalysis)this.classToClassLocalObjectsAnalysis.get(sc);
   }

   protected ClassLocalObjectsAnalysis newClassLocalObjectsAnalysis(LocalObjectsAnalysis loa, InfoFlowAnalysis dfa, UseFinder uf, SootClass sc) {
      return new ClassLocalObjectsAnalysis(loa, dfa, uf, sc);
   }

   public boolean isObjectLocalToParent(Value localOrRef, SootMethod sm) {
      if (localOrRef instanceof StaticFieldRef) {
         return false;
      } else {
         ClassLocalObjectsAnalysis cloa = this.getClassLocalObjectsAnalysis(sm.getDeclaringClass());
         return cloa.isObjectLocal(localOrRef, sm);
      }
   }

   public boolean isFieldLocalToParent(SootField sf) {
      if (sf.isStatic()) {
         return false;
      } else {
         ClassLocalObjectsAnalysis cloa = this.getClassLocalObjectsAnalysis(sf.getDeclaringClass());
         return cloa.isFieldLocal(sf);
      }
   }

   public boolean isObjectLocalToContext(Value localOrRef, SootMethod sm, SootMethod context) {
      if (sm == context) {
         boolean isLocal = this.isObjectLocalToParent(localOrRef, sm);
         if (this.dfa.printDebug()) {
            logger.debug("    " + (isLocal ? "LOCAL  (Directly Reachable from " + context.getDeclaringClass().getShortName() + "." + context.getName() + ")" : "SHARED (Directly Reachable from " + context.getDeclaringClass().getShortName() + "." + context.getName() + ")"));
         }

         return isLocal;
      } else if (localOrRef instanceof StaticFieldRef) {
         if (this.dfa.printDebug()) {
            logger.debug("    SHARED (Static             from " + context.getDeclaringClass().getShortName() + "." + context.getName() + ")");
         }

         return false;
      } else if (!sm.isConcrete()) {
         throw new RuntimeException("Attempted to check if a local variable in a non-concrete method is shared/local.");
      } else {
         Body b = sm.retrieveActiveBody();
         CallLocalityContext mergedContext = this.getClassLocalObjectsAnalysis(context.getDeclaringClass()).getMergedContext(sm);
         if (mergedContext == null) {
            if (this.dfa.printDebug()) {
               logger.debug("      ------ (Unreachable        from " + context.getDeclaringClass().getShortName() + "." + context.getName() + ")");
            }

            return true;
         } else if (localOrRef instanceof InstanceFieldRef) {
            InstanceFieldRef ifr = (InstanceFieldRef)localOrRef;
            Local thisLocal = null;

            try {
               thisLocal = b.getThisLocal();
            } catch (RuntimeException var10) {
            }

            boolean isLocal;
            if (ifr.getBase() == thisLocal) {
               isLocal = mergedContext.isFieldLocal(InfoFlowAnalysis.getNodeForFieldRef(sm, ifr.getField()));
               if (this.dfa.printDebug()) {
                  if (isLocal) {
                     logger.debug("      LOCAL  (this  .localField  from " + context.getDeclaringClass().getShortName() + "." + context.getName() + ")");
                  } else {
                     logger.debug("      SHARED (this  .sharedField from " + context.getDeclaringClass().getShortName() + "." + context.getName() + ")");
                  }
               }

               return isLocal;
            } else {
               isLocal = SmartMethodLocalObjectsAnalysis.isObjectLocal(this.dfa, sm, mergedContext, ifr.getBase());
               if (isLocal) {
                  ClassLocalObjectsAnalysis cloa = this.getClassLocalObjectsAnalysis(context.getDeclaringClass());
                  isLocal = !cloa.getInnerSharedFields().contains(ifr.getField());
                  if (this.dfa.printDebug()) {
                     if (isLocal) {
                        logger.debug("      LOCAL  (local .localField  from " + context.getDeclaringClass().getShortName() + "." + context.getName() + ")");
                     } else {
                        logger.debug("      SHARED (local .sharedField from " + context.getDeclaringClass().getShortName() + "." + context.getName() + ")");
                     }
                  }

                  return isLocal;
               } else {
                  if (this.dfa.printDebug()) {
                     logger.debug("      SHARED (shared.someField   from " + context.getDeclaringClass().getShortName() + "." + context.getName() + ")");
                  }

                  return isLocal;
               }
            }
         } else {
            boolean isLocal = SmartMethodLocalObjectsAnalysis.isObjectLocal(this.dfa, sm, mergedContext, localOrRef);
            if (this.dfa.printDebug()) {
               if (isLocal) {
                  logger.debug("      LOCAL  ( local             from " + context.getDeclaringClass().getShortName() + "." + context.getName() + ")");
               } else {
                  logger.debug("      SHARED (shared             from " + context.getDeclaringClass().getShortName() + "." + context.getName() + ")");
               }
            }

            return isLocal;
         }
      }
   }

   public CallChain getNextCallChainBetween(SootMethod start, SootMethod goal, List previouslyFound) {
      ReachableMethods rm = null;
      if (this.rmCache.containsKey(start)) {
         rm = (ReachableMethods)this.rmCache.get(start);
      } else {
         List<MethodOrMethodContext> entryPoints = new ArrayList();
         entryPoints.add(start);
         rm = new ReachableMethods(this.cg, entryPoints);
         rm.update();
         this.rmCache.put(start, rm);
      }

      return rm.contains(goal) ? this.getNextCallChainBetween(rm, start, goal, (Edge)null, (CallChain)null, previouslyFound) : null;
   }

   public CallChain getNextCallChainBetween(ReachableMethods rm, SootMethod start, SootMethod end, Edge endToPath, CallChain path, List previouslyFound) {
      Pair cacheKey = new Pair(start, end);
      if (this.callChainsCache.containsKey(cacheKey)) {
         return null;
      } else {
         path = new CallChain(endToPath, path);
         if (start == end) {
            return path;
         } else if (!rm.contains(end)) {
            return null;
         } else {
            Iterator edgeIt = this.cg.edgesInto(end);

            while(edgeIt.hasNext()) {
               Edge e = (Edge)edgeIt.next();
               SootMethod node = e.src();
               if (!path.containsMethod(node) && e.isExplicit() && e.srcStmt().containsInvokeExpr()) {
                  CallChain newpath = this.getNextCallChainBetween(rm, start, node, e, path, previouslyFound);
                  if (newpath != null && !previouslyFound.contains(newpath)) {
                     return newpath;
                  }
               }
            }

            if (previouslyFound.size() == 0) {
               this.callChainsCache.put(cacheKey, (Object)null);
            }

            return null;
         }
      }
   }

   public List<SootMethod> getAllMethodsForClass(SootClass sootClass) {
      ReachableMethods rm = Scene.v().getReachableMethods();
      List<SootMethod> scopeMethods = new ArrayList();
      Iterator scopeMethodsIt = sootClass.methodIterator();

      while(scopeMethodsIt.hasNext()) {
         SootMethod scopeMethod = (SootMethod)scopeMethodsIt.next();
         if (rm.contains(scopeMethod)) {
            scopeMethods.add(scopeMethod);
         }
      }

      SootClass superclass = sootClass;
      if (sootClass.hasSuperclass()) {
         superclass = sootClass.getSuperclass();
      }

      while(superclass.hasSuperclass()) {
         Iterator scMethodsIt = superclass.methodIterator();

         while(scMethodsIt.hasNext()) {
            SootMethod scMethod = (SootMethod)scMethodsIt.next();
            if (rm.contains(scMethod)) {
               scopeMethods.add(scMethod);
            }
         }

         superclass = superclass.getSuperclass();
      }

      return scopeMethods;
   }

   public boolean hasNonLocalEffects(SootMethod containingMethod, InvokeExpr ie, SootMethod context) {
      SootMethod target = ie.getMethodRef().resolve();
      MutableDirectedGraph dataFlowGraph = this.dfa.getMethodInfoFlowSummary(target);
      if (ie instanceof StaticInvokeExpr) {
         Iterator graphIt = dataFlowGraph.iterator();

         while(true) {
            while(graphIt.hasNext()) {
               EquivalentValue nodeEqVal = (EquivalentValue)graphIt.next();
               Ref node = (Ref)nodeEqVal.getValue();
               if (node instanceof FieldRef) {
                  if (dataFlowGraph.getPredsOf(nodeEqVal).size() > 0 || dataFlowGraph.getSuccsOf(nodeEqVal).size() > 0) {
                     return true;
                  }
               } else if (node instanceof ParameterRef && (dataFlowGraph.getPredsOf(nodeEqVal).size() > 0 || dataFlowGraph.getSuccsOf(nodeEqVal).size() > 0)) {
                  ParameterRef pr = (ParameterRef)node;
                  if (pr.getIndex() != -1 && !this.isObjectLocalToContext(ie.getArg(pr.getIndex()), containingMethod, context)) {
                     return true;
                  }
               }
            }

            return false;
         }
      } else if (ie instanceof InstanceInvokeExpr) {
         InstanceInvokeExpr iie = (InstanceInvokeExpr)ie;
         Iterator graphIt;
         EquivalentValue nodeEqVal;
         Ref node;
         ParameterRef pr;
         if (this.isObjectLocalToContext(iie.getBase(), containingMethod, context)) {
            graphIt = dataFlowGraph.iterator();

            while(true) {
               while(graphIt.hasNext()) {
                  nodeEqVal = (EquivalentValue)graphIt.next();
                  node = (Ref)nodeEqVal.getValue();
                  if (node instanceof StaticFieldRef) {
                     if (dataFlowGraph.getPredsOf(nodeEqVal).size() > 0 || dataFlowGraph.getSuccsOf(nodeEqVal).size() > 0) {
                        return true;
                     }
                  } else if (node instanceof ParameterRef && (dataFlowGraph.getPredsOf(nodeEqVal).size() > 0 || dataFlowGraph.getSuccsOf(nodeEqVal).size() > 0)) {
                     pr = (ParameterRef)node;
                     if (pr.getIndex() != -1 && !this.isObjectLocalToContext(ie.getArg(pr.getIndex()), containingMethod, context)) {
                        return true;
                     }
                  }
               }

               return false;
            }
         } else {
            graphIt = dataFlowGraph.iterator();

            while(true) {
               while(graphIt.hasNext()) {
                  nodeEqVal = (EquivalentValue)graphIt.next();
                  node = (Ref)nodeEqVal.getValue();
                  if (node instanceof FieldRef) {
                     if (dataFlowGraph.getPredsOf(nodeEqVal).size() > 0 || dataFlowGraph.getSuccsOf(nodeEqVal).size() > 0) {
                        return true;
                     }
                  } else if (node instanceof ParameterRef && (dataFlowGraph.getPredsOf(nodeEqVal).size() > 0 || dataFlowGraph.getSuccsOf(nodeEqVal).size() > 0)) {
                     pr = (ParameterRef)node;
                     if (pr.getIndex() != -1 && !this.isObjectLocalToContext(ie.getArg(pr.getIndex()), containingMethod, context)) {
                        return true;
                     }
                  }
               }

               return false;
            }
         }
      } else {
         return false;
      }
   }
}
