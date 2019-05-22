package heros.solver;

import com.google.common.collect.Maps;
import heros.EdgeFunction;
import heros.EdgeFunctions;
import heros.FlowFunction;
import heros.FlowFunctions;
import heros.IDETabulationProblem;
import heros.InterproceduralCFG;
import heros.JoinLattice;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class BiDiIDESolver<N, D, M, V, I extends InterproceduralCFG<N, M>> {
   private final IDETabulationProblem<N, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, M, V, I> forwardProblem;
   private final IDETabulationProblem<N, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, M, V, I> backwardProblem;
   private final CountingThreadPoolExecutor sharedExecutor;
   protected BiDiIDESolver<N, D, M, V, I>.SingleDirectionSolver fwSolver;
   protected BiDiIDESolver<N, D, M, V, I>.SingleDirectionSolver bwSolver;

   public BiDiIDESolver(IDETabulationProblem<N, D, M, V, I> forwardProblem, IDETabulationProblem<N, D, M, V, I> backwardProblem) {
      if (forwardProblem.followReturnsPastSeeds() && backwardProblem.followReturnsPastSeeds()) {
         this.forwardProblem = new BiDiIDESolver.AugmentedTabulationProblem(forwardProblem);
         this.backwardProblem = new BiDiIDESolver.AugmentedTabulationProblem(backwardProblem);
         this.sharedExecutor = new CountingThreadPoolExecutor(1, Math.max(1, forwardProblem.numThreads()), 30L, TimeUnit.SECONDS, new LinkedBlockingQueue());
      } else {
         throw new IllegalArgumentException("This solver is only meant for bottom-up problems, so followReturnsPastSeeds() should return true.");
      }
   }

   public void solve() {
      this.fwSolver = this.createSingleDirectionSolver(this.forwardProblem, "FW");
      this.bwSolver = this.createSingleDirectionSolver(this.backwardProblem, "BW");
      this.fwSolver.otherSolver = this.bwSolver;
      this.bwSolver.otherSolver = this.fwSolver;
      this.bwSolver.submitInitialSeeds();
      this.fwSolver.solve();
   }

   protected BiDiIDESolver<N, D, M, V, I>.SingleDirectionSolver createSingleDirectionSolver(IDETabulationProblem<N, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, M, V, I> problem, String debugName) {
      return new BiDiIDESolver.SingleDirectionSolver(problem, debugName);
   }

   private class AugmentedTabulationProblem implements IDETabulationProblem<N, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, M, V, I> {
      private final IDETabulationProblem<N, D, M, V, I> delegate;
      private final BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt ZERO;
      private final FlowFunctions<N, D, M> originalFunctions;

      public AugmentedTabulationProblem(IDETabulationProblem<N, D, M, V, I> delegate) {
         this.delegate = delegate;
         this.originalFunctions = this.delegate.flowFunctions();
         this.ZERO = BiDiIDESolver.this.new AbstractionWithSourceStmt(delegate.zeroValue(), (Object)null);
      }

      public FlowFunctions<N, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, M> flowFunctions() {
         return new FlowFunctions<N, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, M>() {
            public FlowFunction<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt> getNormalFlowFunction(final N curr, final N succ) {
               return new FlowFunction<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt>() {
                  public Set<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt> computeTargets(BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt source) {
                     return copyOverSourceStmts(source, AugmentedTabulationProblem.this.originalFunctions.getNormalFlowFunction(curr, succ));
                  }
               };
            }

            public FlowFunction<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt> getCallFlowFunction(final N callStmt, final M destinationMethod) {
               return new FlowFunction<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt>() {
                  public Set<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt> computeTargets(BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt source) {
                     Set<D> origTargets = AugmentedTabulationProblem.this.originalFunctions.getCallFlowFunction(callStmt, destinationMethod).computeTargets(source.getAbstraction());
                     Set<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt> res = new HashSet();
                     Iterator var4 = origTargets.iterator();

                     while(var4.hasNext()) {
                        D d = var4.next();
                        res.add(BiDiIDESolver.this.new AbstractionWithSourceStmt(d, (Object)null));
                     }

                     return res;
                  }
               };
            }

            public FlowFunction<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt> getReturnFlowFunction(final N callSite, final M calleeMethod, final N exitStmt, final N returnSite) {
               return new FlowFunction<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt>() {
                  public Set<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt> computeTargets(BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt source) {
                     return copyOverSourceStmts(source, AugmentedTabulationProblem.this.originalFunctions.getReturnFlowFunction(callSite, calleeMethod, exitStmt, returnSite));
                  }
               };
            }

            public FlowFunction<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt> getCallToReturnFlowFunction(final N callSite, final N returnSite) {
               return new FlowFunction<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt>() {
                  public Set<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt> computeTargets(BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt source) {
                     return copyOverSourceStmts(source, AugmentedTabulationProblem.this.originalFunctions.getCallToReturnFlowFunction(callSite, returnSite));
                  }
               };
            }

            private Set<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt> copyOverSourceStmts(BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt source, FlowFunction<D> originalFunction) {
               D originalAbstraction = source.getAbstraction();
               Set<D> origTargets = originalFunction.computeTargets(originalAbstraction);
               Set<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt> res = new HashSet();
               Iterator var6 = origTargets.iterator();

               while(var6.hasNext()) {
                  D d = var6.next();
                  res.add(BiDiIDESolver.this.new AbstractionWithSourceStmt(d, source.getSourceStmt()));
               }

               return res;
            }
         };
      }

      public boolean followReturnsPastSeeds() {
         return this.delegate.followReturnsPastSeeds();
      }

      public boolean autoAddZero() {
         return this.delegate.autoAddZero();
      }

      public int numThreads() {
         return this.delegate.numThreads();
      }

      public boolean computeValues() {
         return this.delegate.computeValues();
      }

      public I interproceduralCFG() {
         return this.delegate.interproceduralCFG();
      }

      public Map<N, Set<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt>> initialSeeds() {
         Map<N, Set<D>> originalSeeds = this.delegate.initialSeeds();
         Map<N, Set<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt>> res = new HashMap();
         Iterator var3 = originalSeeds.entrySet().iterator();

         while(var3.hasNext()) {
            Entry<N, Set<D>> entry = (Entry)var3.next();
            N stmt = entry.getKey();
            Set<D> seeds = (Set)entry.getValue();
            Set<BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt> resSet = new HashSet();
            Iterator var8 = seeds.iterator();

            while(var8.hasNext()) {
               D d = var8.next();
               resSet.add(BiDiIDESolver.this.new AbstractionWithSourceStmt(d, stmt));
            }

            res.put(stmt, resSet);
         }

         return res;
      }

      public BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt zeroValue() {
         return this.ZERO;
      }

      public EdgeFunctions<N, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, M, V> edgeFunctions() {
         return new EdgeFunctions<N, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, M, V>() {
            public EdgeFunction<V> getNormalEdgeFunction(N curr, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt currNode, N succ, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt succNode) {
               return AugmentedTabulationProblem.this.delegate.edgeFunctions().getNormalEdgeFunction(curr, currNode.getAbstraction(), succ, succNode.getAbstraction());
            }

            public EdgeFunction<V> getCallEdgeFunction(N callStmt, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt srcNode, M destinationMethod, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt destNode) {
               return AugmentedTabulationProblem.this.delegate.edgeFunctions().getCallEdgeFunction(callStmt, srcNode.getAbstraction(), destinationMethod, destNode.getAbstraction());
            }

            public EdgeFunction<V> getReturnEdgeFunction(N callSite, M calleeMethod, N exitStmt, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt exitNode, N returnSite, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt retNode) {
               return AugmentedTabulationProblem.this.delegate.edgeFunctions().getReturnEdgeFunction(callSite, calleeMethod, exitStmt, exitNode.getAbstraction(), returnSite, retNode.getAbstraction());
            }

            public EdgeFunction<V> getCallToReturnEdgeFunction(N callSite, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt callNode, N returnSite, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt returnSideNode) {
               return AugmentedTabulationProblem.this.delegate.edgeFunctions().getCallToReturnEdgeFunction(callSite, callNode.getAbstraction(), returnSite, returnSideNode.getAbstraction());
            }
         };
      }

      public JoinLattice<V> joinLattice() {
         return this.delegate.joinLattice();
      }

      public EdgeFunction<V> allTopFunction() {
         return this.delegate.allTopFunction();
      }

      public boolean recordEdges() {
         return this.delegate.recordEdges();
      }
   }

   public class AbstractionWithSourceStmt {
      protected final D abstraction;
      protected final N source;

      private AbstractionWithSourceStmt(D abstraction, N source) {
         this.abstraction = abstraction;
         this.source = source;
      }

      public D getAbstraction() {
         return this.abstraction;
      }

      public N getSourceStmt() {
         return this.source;
      }

      public String toString() {
         return this.source != null ? "" + this.abstraction + "-@-" + this.source + "" : this.abstraction.toString();
      }

      public int hashCode() {
         int prime = true;
         int resultx = 1;
         int result = 31 * resultx + (this.abstraction == null ? 0 : this.abstraction.hashCode());
         result = 31 * result + (this.source == null ? 0 : this.source.hashCode());
         return result;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (obj == null) {
            return false;
         } else if (this.getClass() != obj.getClass()) {
            return false;
         } else {
            BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt other = (BiDiIDESolver.AbstractionWithSourceStmt)obj;
            if (this.abstraction == null) {
               if (other.abstraction != null) {
                  return false;
               }
            } else if (!this.abstraction.equals(other.abstraction)) {
               return false;
            }

            if (this.source == null) {
               if (other.source != null) {
                  return false;
               }
            } else if (!this.source.equals(other.source)) {
               return false;
            }

            return true;
         }
      }

      // $FF: synthetic method
      AbstractionWithSourceStmt(Object x1, Object x2, Object x3) {
         this(x1, x2);
      }
   }

   protected class SingleDirectionSolver extends IDESolver<N, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, M, V, I> {
      private final String debugName;
      private BiDiIDESolver<N, D, M, V, I>.SingleDirectionSolver otherSolver;
      private Set<BiDiIDESolver.LeakKey<N>> leakedSources = Collections.newSetFromMap(Maps.newConcurrentMap());
      private ConcurrentMap<BiDiIDESolver.LeakKey<N>, Set<BiDiIDESolver<N, D, M, V, I>.PausedEdge>> pausedPathEdges = Maps.newConcurrentMap();

      public SingleDirectionSolver(IDETabulationProblem<N, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt, M, V, I> ifdsProblem, String debugName) {
         super(ifdsProblem);
         this.debugName = debugName;
      }

      protected void propagateUnbalancedReturnFlow(N retSiteC, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt targetVal, EdgeFunction<V> edgeFunction, N relatedCallSite) {
         N sourceStmt = targetVal.getSourceStmt();
         BiDiIDESolver.LeakKey<N> leakKey = new BiDiIDESolver.LeakKey(sourceStmt, relatedCallSite);
         this.leakedSources.add(leakKey);
         if (this.otherSolver.hasLeaked(leakKey)) {
            this.otherSolver.unpausePathEdgesForSource(leakKey);
            super.propagateUnbalancedReturnFlow(retSiteC, targetVal, edgeFunction, relatedCallSite);
         } else {
            Set<BiDiIDESolver<N, D, M, V, I>.PausedEdge> newPausedEdges = Collections.newSetFromMap(Maps.newConcurrentMap());
            Set<BiDiIDESolver<N, D, M, V, I>.PausedEdge> existingPausedEdges = (Set)this.pausedPathEdges.putIfAbsent(leakKey, newPausedEdges);
            if (existingPausedEdges == null) {
               existingPausedEdges = newPausedEdges;
            }

            BiDiIDESolver<N, D, M, V, I>.PausedEdge edge = BiDiIDESolver.this.new PausedEdge(retSiteC, targetVal, edgeFunction, relatedCallSite);
            existingPausedEdges.add(edge);
            if (this.otherSolver.hasLeaked(leakKey) && existingPausedEdges.remove(edge)) {
               super.propagateUnbalancedReturnFlow(retSiteC, targetVal, edgeFunction, relatedCallSite);
            }

            logger.debug((String)" ++ PAUSE {}: {}", (Object)this.debugName, (Object)edge);
         }

      }

      protected void propagate(BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt sourceVal, N target, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt targetVal, EdgeFunction<V> f, N relatedCallSite, boolean isUnbalancedReturn) {
         if (isUnbalancedReturn) {
            assert sourceVal.getSourceStmt() == null : "source value should have no statement attached";

            targetVal = BiDiIDESolver.this.new AbstractionWithSourceStmt(targetVal.getAbstraction(), relatedCallSite);
            super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);
         } else {
            super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);
         }

      }

      protected BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt restoreContextOnReturnedFact(N callSite, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt d4, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt d5) {
         return BiDiIDESolver.this.new AbstractionWithSourceStmt(d5.getAbstraction(), d4.getSourceStmt());
      }

      private boolean hasLeaked(BiDiIDESolver.LeakKey<N> leakKey) {
         return this.leakedSources.contains(leakKey);
      }

      private void unpausePathEdgesForSource(BiDiIDESolver.LeakKey<N> leakKey) {
         Set<BiDiIDESolver<N, D, M, V, I>.PausedEdge> pausedEdges = (Set)this.pausedPathEdges.get(leakKey);
         if (pausedEdges != null) {
            Iterator var3 = pausedEdges.iterator();

            while(var3.hasNext()) {
               BiDiIDESolver<N, D, M, V, I>.PausedEdge edge = (BiDiIDESolver.PausedEdge)var3.next();
               if (pausedEdges.remove(edge)) {
                  if (DEBUG) {
                     logger.debug((String)"-- UNPAUSE {}: {}", (Object)this.debugName, (Object)edge);
                  }

                  super.propagateUnbalancedReturnFlow(edge.retSiteC, edge.targetVal, edge.edgeFunction, edge.relatedCallSite);
               }
            }
         }

      }

      protected CountingThreadPoolExecutor getExecutor() {
         return BiDiIDESolver.this.sharedExecutor;
      }

      protected String getDebugName() {
         return this.debugName;
      }
   }

   private static class LeakKey<N> {
      private N sourceStmt;
      private N relatedCallSite;

      public LeakKey(N sourceStmt, N relatedCallSite) {
         this.sourceStmt = sourceStmt;
         this.relatedCallSite = relatedCallSite;
      }

      public int hashCode() {
         int prime = true;
         int result = 1;
         int result = 31 * result + (this.relatedCallSite == null ? 0 : this.relatedCallSite.hashCode());
         result = 31 * result + (this.sourceStmt == null ? 0 : this.sourceStmt.hashCode());
         return result;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (obj == null) {
            return false;
         } else if (!(obj instanceof BiDiIDESolver.LeakKey)) {
            return false;
         } else {
            BiDiIDESolver.LeakKey other = (BiDiIDESolver.LeakKey)obj;
            if (this.relatedCallSite == null) {
               if (other.relatedCallSite != null) {
                  return false;
               }
            } else if (!this.relatedCallSite.equals(other.relatedCallSite)) {
               return false;
            }

            if (this.sourceStmt == null) {
               if (other.sourceStmt != null) {
                  return false;
               }
            } else if (!this.sourceStmt.equals(other.sourceStmt)) {
               return false;
            }

            return true;
         }
      }
   }

   private class PausedEdge {
      private N retSiteC;
      private BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt targetVal;
      private EdgeFunction<V> edgeFunction;
      private N relatedCallSite;

      public PausedEdge(N retSiteC, BiDiIDESolver<N, D, M, V, I>.AbstractionWithSourceStmt targetVal, EdgeFunction<V> edgeFunction, N relatedCallSite) {
         this.retSiteC = retSiteC;
         this.targetVal = targetVal;
         this.edgeFunction = edgeFunction;
         this.relatedCallSite = relatedCallSite;
      }
   }
}
