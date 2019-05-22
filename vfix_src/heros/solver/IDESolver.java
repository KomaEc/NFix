package heros.solver;

import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import heros.DontSynchronize;
import heros.EdgeFunction;
import heros.EdgeFunctionCache;
import heros.EdgeFunctions;
import heros.FlowFunction;
import heros.FlowFunctionCache;
import heros.FlowFunctions;
import heros.IDETabulationProblem;
import heros.InterproceduralCFG;
import heros.JoinLattice;
import heros.SynchronizedBy;
import heros.ZeroedFlowFunctions;
import heros.edgefunc.EdgeIdentity;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IDESolver<N, D, M, V, I extends InterproceduralCFG<N, M>> {
   public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
   protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);
   @SynchronizedBy("consistent lock on field")
   protected Table<N, N, Map<D, Set<D>>> computedIntraPEdges;
   @SynchronizedBy("consistent lock on field")
   protected Table<N, N, Map<D, Set<D>>> computedInterPEdges;
   public static final boolean DEBUG;
   protected CountingThreadPoolExecutor executor;
   @DontSynchronize("only used by single thread")
   protected int numThreads;
   @SynchronizedBy("thread safe data structure, consistent locking when used")
   protected final JumpFunctions<N, D, V> jumpFn;
   @SynchronizedBy("thread safe data structure, only modified internally")
   protected final I icfg;
   @SynchronizedBy("consistent lock on 'incoming'")
   protected final Table<N, D, Table<N, D, EdgeFunction<V>>> endSummary;
   @SynchronizedBy("consistent lock on field")
   protected final Table<N, D, Map<N, Set<D>>> incoming;
   @SynchronizedBy("use of ConcurrentHashMap")
   protected final Set<N> unbalancedRetSites;
   @DontSynchronize("stateless")
   protected final FlowFunctions<N, D, M> flowFunctions;
   @DontSynchronize("stateless")
   protected final EdgeFunctions<N, D, M, V> edgeFunctions;
   @DontSynchronize("only used by single thread")
   protected final Map<N, Set<D>> initialSeeds;
   @DontSynchronize("stateless")
   protected final JoinLattice<V> valueLattice;
   @DontSynchronize("stateless")
   protected final EdgeFunction<V> allTop;
   @SynchronizedBy("consistent lock on field")
   protected final Table<N, D, V> val;
   @DontSynchronize("benign races")
   public long flowFunctionApplicationCount;
   @DontSynchronize("benign races")
   public long flowFunctionConstructionCount;
   @DontSynchronize("benign races")
   public long propagationCount;
   @DontSynchronize("benign races")
   public long durationFlowFunctionConstruction;
   @DontSynchronize("benign races")
   public long durationFlowFunctionApplication;
   @DontSynchronize("stateless")
   protected final D zeroValue;
   @DontSynchronize("readOnly")
   protected final FlowFunctionCache<N, D, M> ffCache;
   @DontSynchronize("readOnly")
   protected final EdgeFunctionCache<N, D, M, V> efCache;
   @DontSynchronize("readOnly")
   protected final boolean followReturnsPastSeeds;
   @DontSynchronize("readOnly")
   protected final boolean computeValues;
   private boolean recordEdges;

   public IDESolver(IDETabulationProblem<N, D, M, V, I> tabulationProblem) {
      this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);
   }

   public IDESolver(IDETabulationProblem<N, D, M, V, I> tabulationProblem, CacheBuilder flowFunctionCacheBuilder, CacheBuilder edgeFunctionCacheBuilder) {
      this.computedIntraPEdges = HashBasedTable.create();
      this.computedInterPEdges = HashBasedTable.create();
      this.endSummary = HashBasedTable.create();
      this.incoming = HashBasedTable.create();
      this.val = HashBasedTable.create();
      if (logger.isDebugEnabled()) {
         if (flowFunctionCacheBuilder != null) {
            flowFunctionCacheBuilder = flowFunctionCacheBuilder.recordStats();
         }

         if (edgeFunctionCacheBuilder != null) {
            edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();
         }
      }

      this.zeroValue = tabulationProblem.zeroValue();
      this.icfg = tabulationProblem.interproceduralCFG();
      FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ? new ZeroedFlowFunctions(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue()) : tabulationProblem.flowFunctions();
      EdgeFunctions<N, D, M, V> edgeFunctions = tabulationProblem.edgeFunctions();
      if (flowFunctionCacheBuilder != null) {
         this.ffCache = new FlowFunctionCache((FlowFunctions)flowFunctions, flowFunctionCacheBuilder);
         flowFunctions = this.ffCache;
      } else {
         this.ffCache = null;
      }

      if (edgeFunctionCacheBuilder != null) {
         this.efCache = new EdgeFunctionCache((EdgeFunctions)edgeFunctions, edgeFunctionCacheBuilder);
         edgeFunctions = this.efCache;
      } else {
         this.efCache = null;
      }

      this.flowFunctions = (FlowFunctions)flowFunctions;
      this.edgeFunctions = (EdgeFunctions)edgeFunctions;
      this.initialSeeds = tabulationProblem.initialSeeds();
      this.unbalancedRetSites = Collections.newSetFromMap(new ConcurrentHashMap());
      this.valueLattice = tabulationProblem.joinLattice();
      this.allTop = tabulationProblem.allTopFunction();
      this.jumpFn = new JumpFunctions(this.allTop);
      this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();
      this.numThreads = Math.max(1, tabulationProblem.numThreads());
      this.computeValues = tabulationProblem.computeValues();
      this.executor = this.getExecutor();
      this.recordEdges = tabulationProblem.recordEdges();
   }

   public void solve() {
      this.submitInitialSeeds();
      this.awaitCompletionComputeValuesAndShutdown();
   }

   protected void submitInitialSeeds() {
      Iterator var1 = this.initialSeeds.entrySet().iterator();

      while(var1.hasNext()) {
         Entry<N, Set<D>> seed = (Entry)var1.next();
         N startPoint = seed.getKey();
         Iterator var4 = ((Set)seed.getValue()).iterator();

         while(var4.hasNext()) {
            D val = var4.next();
            this.propagate(this.zeroValue, startPoint, val, EdgeIdentity.v(), (Object)null, false);
         }

         this.jumpFn.addFunction(this.zeroValue, startPoint, this.zeroValue, EdgeIdentity.v());
      }

   }

   protected void awaitCompletionComputeValuesAndShutdown() {
      long before = System.currentTimeMillis();
      this.runExecutorAndAwaitCompletion();
      this.durationFlowFunctionConstruction = System.currentTimeMillis() - before;
      if (this.computeValues) {
         before = System.currentTimeMillis();
         this.computeValues();
         this.durationFlowFunctionApplication = System.currentTimeMillis() - before;
      }

      if (logger.isDebugEnabled()) {
         this.printStats();
      }

      this.executor.shutdown();
      this.runExecutorAndAwaitCompletion();
   }

   private void runExecutorAndAwaitCompletion() {
      try {
         this.executor.awaitCompletion();
      } catch (InterruptedException var2) {
         var2.printStackTrace();
      }

      Throwable exception = this.executor.getException();
      if (exception != null) {
         throw new RuntimeException("There were exceptions during IDE analysis. Exiting.", exception);
      }
   }

   protected void scheduleEdgeProcessing(PathEdge<N, D> edge) {
      if (!this.executor.isTerminating()) {
         this.executor.execute(new IDESolver.PathEdgeProcessingTask(edge));
         ++this.propagationCount;
      }
   }

   private void scheduleValueProcessing(IDESolver<N, D, M, V, I>.ValuePropagationTask vpt) {
      if (!this.executor.isTerminating()) {
         this.executor.execute(vpt);
      }
   }

   private void scheduleValueComputationTask(IDESolver<N, D, M, V, I>.ValueComputationTask task) {
      if (!this.executor.isTerminating()) {
         this.executor.execute(task);
      }
   }

   private void saveEdges(N sourceNode, N sinkStmt, D sourceVal, Set<D> destVals, boolean interP) {
      if (this.recordEdges) {
         Table<N, N, Map<D, Set<D>>> tgtMap = interP ? this.computedInterPEdges : this.computedIntraPEdges;
         synchronized(tgtMap) {
            Map<D, Set<D>> map = (Map)tgtMap.get(sourceNode, sinkStmt);
            if (map == null) {
               map = new HashMap();
               tgtMap.put(sourceNode, sinkStmt, map);
            }

            ((Map)map).put(sourceVal, new HashSet(destVals));
         }
      }
   }

   private void processCall(PathEdge<N, D> edge) {
      D d1 = edge.factAtSource();
      N n = edge.getTarget();
      logger.trace("Processing call to {}", n);
      D d2 = edge.factAtTarget();
      EdgeFunction<V> f = this.jumpFunction(edge);
      Collection<N> returnSiteNs = this.icfg.getReturnSitesOfCallAt(n);
      Collection<M> callees = this.icfg.getCalleesOfCallAt(n);
      Iterator var8 = callees.iterator();

      Object sCalledProcN;
      FlowFunction callToReturnFlowFunction;
      Set returnFacts;
      while(var8.hasNext()) {
         sCalledProcN = var8.next();
         callToReturnFlowFunction = this.flowFunctions.getCallFlowFunction(n, sCalledProcN);
         ++this.flowFunctionConstructionCount;
         returnFacts = this.computeCallFlowFunction(callToReturnFlowFunction, d1, d2);
         Collection<N> startPointsOf = this.icfg.getStartPointsOf(sCalledProcN);
         Iterator var13 = startPointsOf.iterator();

         while(var13.hasNext()) {
            N sP = var13.next();
            this.saveEdges(n, sP, d2, returnFacts, true);
            Iterator var15 = returnFacts.iterator();

            while(var15.hasNext()) {
               D d3 = var15.next();
               this.propagate(d3, sP, d3, EdgeIdentity.v(), n, false);
               HashSet endSumm;
               synchronized(this.incoming) {
                  this.addIncoming(sP, d3, n, d2);
                  endSumm = new HashSet(this.endSummary(sP, d3));
               }

               Iterator var18 = endSumm.iterator();

               while(var18.hasNext()) {
                  Table.Cell<N, D, EdgeFunction<V>> entry = (Table.Cell)var18.next();
                  N eP = entry.getRowKey();
                  D d4 = entry.getColumnKey();
                  EdgeFunction<V> fCalleeSummary = (EdgeFunction)entry.getValue();
                  Iterator var23 = returnSiteNs.iterator();

                  while(var23.hasNext()) {
                     N retSiteN = var23.next();
                     FlowFunction<D> retFunction = this.flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
                     ++this.flowFunctionConstructionCount;
                     Set<D> returnedFacts = this.computeReturnFlowFunction(retFunction, d3, d4, n, Collections.singleton(d2));
                     this.saveEdges(eP, retSiteN, d4, returnedFacts, true);
                     Iterator var27 = returnedFacts.iterator();

                     while(var27.hasNext()) {
                        D d5 = var27.next();
                        EdgeFunction<V> f4 = this.edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);
                        EdgeFunction<V> f5 = this.edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);
                        EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5);
                        D d5_restoredCtx = this.restoreContextOnReturnedFact(n, d2, d5);
                        this.propagate(d1, retSiteN, d5_restoredCtx, f.composeWith(fPrime), n, false);
                     }
                  }
               }
            }
         }
      }

      var8 = returnSiteNs.iterator();

      while(var8.hasNext()) {
         sCalledProcN = var8.next();
         callToReturnFlowFunction = this.flowFunctions.getCallToReturnFlowFunction(n, sCalledProcN);
         ++this.flowFunctionConstructionCount;
         returnFacts = this.computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2);
         this.saveEdges(n, sCalledProcN, d2, returnFacts, false);
         Iterator var34 = returnFacts.iterator();

         while(var34.hasNext()) {
            D d3 = var34.next();
            EdgeFunction<V> edgeFnE = this.edgeFunctions.getCallToReturnEdgeFunction(n, d2, sCalledProcN, d3);
            this.propagate(d1, sCalledProcN, d3, f.composeWith(edgeFnE), n, false);
         }
      }

   }

   protected Set<D> computeCallFlowFunction(FlowFunction<D> callFlowFunction, D d1, D d2) {
      return callFlowFunction.computeTargets(d2);
   }

   protected Set<D> computeCallToReturnFlowFunction(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
      return callToReturnFlowFunction.computeTargets(d2);
   }

   protected void processExit(PathEdge<N, D> edge) {
      N n = edge.getTarget();
      EdgeFunction<V> f = this.jumpFunction(edge);
      M methodThatNeedsSummary = this.icfg.getMethodOf(n);
      D d1 = edge.factAtSource();
      D d2 = edge.factAtTarget();
      Collection<N> startPointsOf = this.icfg.getStartPointsOf(methodThatNeedsSummary);
      Map<N, Set<D>> inc = new HashMap();
      Iterator var9 = startPointsOf.iterator();

      Iterator var12;
      while(var9.hasNext()) {
         N sP = var9.next();
         synchronized(this.incoming) {
            this.addEndSummary(sP, d1, n, d2, f);
            var12 = this.incoming(d1, sP).entrySet().iterator();

            while(var12.hasNext()) {
               Entry<N, Set<D>> entry = (Entry)var12.next();
               inc.put(entry.getKey(), new HashSet((Collection)entry.getValue()));
            }
         }
      }

      var9 = inc.entrySet().iterator();

      Object c;
      FlowFunction retFunction;
      Object retSiteC;
      while(var9.hasNext()) {
         Entry<N, Set<D>> entry = (Entry)var9.next();
         c = entry.getKey();
         var12 = this.icfg.getReturnSitesOfCallAt(c).iterator();

         while(var12.hasNext()) {
            retSiteC = var12.next();
            retFunction = this.flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary, n, retSiteC);
            ++this.flowFunctionConstructionCount;
            Iterator var15 = ((Set)entry.getValue()).iterator();

            while(var15.hasNext()) {
               D d4 = var15.next();
               Set<D> targets = this.computeReturnFlowFunction(retFunction, d1, d2, c, (Set)entry.getValue());
               this.saveEdges(n, retSiteC, d2, targets, true);
               Iterator var18 = targets.iterator();

               while(var18.hasNext()) {
                  D d5 = var18.next();
                  EdgeFunction<V> f4 = this.edgeFunctions.getCallEdgeFunction(c, d4, this.icfg.getMethodOf(n), d1);
                  EdgeFunction<V> f5 = this.edgeFunctions.getReturnEdgeFunction(c, this.icfg.getMethodOf(n), n, d2, retSiteC, d5);
                  EdgeFunction<V> fPrime = f4.composeWith(f).composeWith(f5);
                  synchronized(this.jumpFn) {
                     Iterator var24 = this.jumpFn.reverseLookup(c, d4).entrySet().iterator();

                     while(var24.hasNext()) {
                        Entry<D, EdgeFunction<V>> valAndFunc = (Entry)var24.next();
                        EdgeFunction<V> f3 = (EdgeFunction)valAndFunc.getValue();
                        if (!f3.equalTo(this.allTop)) {
                           D d3 = valAndFunc.getKey();
                           D d5_restoredCtx = this.restoreContextOnReturnedFact(c, d4, d5);
                           this.propagate(d3, retSiteC, d5_restoredCtx, f3.composeWith(fPrime), c, false);
                        }
                     }
                  }
               }
            }
         }
      }

      if (this.followReturnsPastSeeds && inc.isEmpty() && d1.equals(this.zeroValue)) {
         Collection<N> callers = this.icfg.getCallersOf(methodThatNeedsSummary);
         Iterator var34 = callers.iterator();

         while(var34.hasNext()) {
            c = var34.next();
            var12 = this.icfg.getReturnSitesOfCallAt(c).iterator();

            while(var12.hasNext()) {
               retSiteC = var12.next();
               retFunction = this.flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary, n, retSiteC);
               ++this.flowFunctionConstructionCount;
               Set<D> targets = this.computeReturnFlowFunction(retFunction, d1, d2, c, Collections.singleton(this.zeroValue));
               this.saveEdges(n, retSiteC, d2, targets, true);
               Iterator var38 = targets.iterator();

               while(var38.hasNext()) {
                  D d5 = var38.next();
                  EdgeFunction<V> f5 = this.edgeFunctions.getReturnEdgeFunction(c, this.icfg.getMethodOf(n), n, d2, retSiteC, d5);
                  this.propagateUnbalancedReturnFlow(retSiteC, d5, f.composeWith(f5), c);
                  this.unbalancedRetSites.add(retSiteC);
               }
            }
         }

         if (callers.isEmpty()) {
            FlowFunction<D> retFunction = this.flowFunctions.getReturnFlowFunction((Object)null, methodThatNeedsSummary, n, (Object)null);
            ++this.flowFunctionConstructionCount;
            retFunction.computeTargets(d2);
         }
      }

   }

   protected void propagateUnbalancedReturnFlow(N retSiteC, D targetVal, EdgeFunction<V> edgeFunction, N relatedCallSite) {
      this.propagate(this.zeroValue, retSiteC, targetVal, edgeFunction, relatedCallSite, true);
   }

   protected D restoreContextOnReturnedFact(N callSite, D d4, D d5) {
      if (d5 instanceof LinkedNode) {
         ((LinkedNode)d5).setCallingContext(d4);
      }

      if (d5 instanceof JoinHandlingNode) {
         ((JoinHandlingNode)d5).setCallingContext(d4);
      }

      return d5;
   }

   protected Set<D> computeReturnFlowFunction(FlowFunction<D> retFunction, D d1, D d2, N callSite, Set<D> callerSideDs) {
      return retFunction.computeTargets(d2);
   }

   private void processNormalFlow(PathEdge<N, D> edge) {
      D d1 = edge.factAtSource();
      N n = edge.getTarget();
      D d2 = edge.factAtTarget();
      EdgeFunction<V> f = this.jumpFunction(edge);
      Iterator var6 = this.icfg.getSuccsOf(n).iterator();

      while(var6.hasNext()) {
         N m = var6.next();
         FlowFunction<D> flowFunction = this.flowFunctions.getNormalFlowFunction(n, m);
         ++this.flowFunctionConstructionCount;
         Set<D> res = this.computeNormalFlowFunction(flowFunction, d1, d2);
         this.saveEdges(n, m, d2, res, false);
         Iterator var10 = res.iterator();

         while(var10.hasNext()) {
            D d3 = var10.next();
            EdgeFunction<V> fprime = f.composeWith(this.edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));
            this.propagate(d1, m, d3, fprime, (Object)null, false);
         }
      }

   }

   protected Set<D> computeNormalFlowFunction(FlowFunction<D> flowFunction, D d1, D d2) {
      return flowFunction.computeTargets(d2);
   }

   protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f, N relatedCallSite, boolean isUnbalancedReturn) {
      EdgeFunction fPrime;
      boolean newFunction;
      synchronized(this.jumpFn) {
         EdgeFunction<V> jumpFnE = (EdgeFunction)this.jumpFn.reverseLookup(target, targetVal).get(sourceVal);
         if (jumpFnE == null) {
            jumpFnE = this.allTop;
         }

         fPrime = jumpFnE.joinWith(f);
         newFunction = !fPrime.equalTo(jumpFnE);
         if (newFunction) {
            this.jumpFn.addFunction(sourceVal, target, targetVal, fPrime);
         }
      }

      if (newFunction) {
         PathEdge<N, D> edge = new PathEdge(sourceVal, target, targetVal);
         this.scheduleEdgeProcessing(edge);
         if (targetVal != this.zeroValue) {
            logger.trace("{} - EDGE: <{},{}> -> <{},{}> - {}", this.getDebugName(), this.icfg.getMethodOf(target), sourceVal, target, targetVal, fPrime);
         }
      }

   }

   private void computeValues() {
      logger.debug("Computing the final values for the edge functions");
      Map<N, Set<D>> allSeeds = new HashMap(this.initialSeeds);

      Iterator var2;
      Object startPoint;
      for(var2 = this.unbalancedRetSites.iterator(); var2.hasNext(); ((Set)startPoint).add(this.zeroValue)) {
         N unbalancedRetSite = var2.next();
         startPoint = (Set)allSeeds.get(unbalancedRetSite);
         if (startPoint == null) {
            startPoint = new HashSet();
            allSeeds.put(unbalancedRetSite, startPoint);
         }
      }

      var2 = allSeeds.entrySet().iterator();

      Iterator var5;
      Object n;
      while(var2.hasNext()) {
         Entry<N, Set<D>> seed = (Entry)var2.next();
         startPoint = seed.getKey();
         var5 = ((Set)seed.getValue()).iterator();

         while(var5.hasNext()) {
            n = var5.next();
            this.setVal(startPoint, n, this.valueLattice.bottomElement());
            Pair<N, D> superGraphNode = new Pair(startPoint, n);
            this.scheduleValueProcessing(new IDESolver.ValuePropagationTask(superGraphNode));
         }
      }

      logger.debug("Computed the final values of the edge functions");

      try {
         this.executor.awaitCompletion();
      } catch (InterruptedException var9) {
         var9.printStackTrace();
      }

      Set<N> allNonCallStartNodes = this.icfg.allNonCallStartNodes();
      N[] nonCallStartNodesArray = (Object[])(new Object[allNonCallStartNodes.size()]);
      int i = 0;

      for(var5 = allNonCallStartNodes.iterator(); var5.hasNext(); ++i) {
         n = var5.next();
         nonCallStartNodesArray[i] = n;
      }

      for(int t = 0; t < this.numThreads; ++t) {
         IDESolver<N, D, M, V, I>.ValueComputationTask task = new IDESolver.ValueComputationTask(nonCallStartNodesArray, t);
         this.scheduleValueComputationTask(task);
      }

      try {
         this.executor.awaitCompletion();
      } catch (InterruptedException var8) {
         var8.printStackTrace();
      }

   }

   private void propagateValueAtStart(Pair<N, D> nAndD, N n) {
      D d = nAndD.getO2();
      M p = this.icfg.getMethodOf(n);
      Iterator var5 = this.icfg.getCallsFromWithin(p).iterator();

      while(var5.hasNext()) {
         N c = var5.next();
         synchronized(this.jumpFn) {
            Set<Entry<D, EdgeFunction<V>>> entries = this.jumpFn.forwardLookup(d, c).entrySet();

            for(Iterator var9 = entries.iterator(); var9.hasNext(); ++this.flowFunctionApplicationCount) {
               Entry<D, EdgeFunction<V>> dPAndFP = (Entry)var9.next();
               D dPrime = dPAndFP.getKey();
               EdgeFunction<V> fPrime = (EdgeFunction)dPAndFP.getValue();
               this.propagateValue(c, dPrime, fPrime.computeTarget(this.val(n, d)));
            }
         }
      }

   }

   private void propagateValueAtCall(Pair<N, D> nAndD, N n) {
      D d = nAndD.getO2();
      Iterator var4 = this.icfg.getCalleesOfCallAt(n).iterator();

      while(var4.hasNext()) {
         M q = var4.next();
         FlowFunction<D> callFlowFunction = this.flowFunctions.getCallFlowFunction(n, q);
         ++this.flowFunctionConstructionCount;
         Iterator var7 = callFlowFunction.computeTargets(d).iterator();

         while(var7.hasNext()) {
            D dPrime = var7.next();
            EdgeFunction<V> edgeFn = this.edgeFunctions.getCallEdgeFunction(n, d, q, dPrime);

            for(Iterator var10 = this.icfg.getStartPointsOf(q).iterator(); var10.hasNext(); ++this.flowFunctionApplicationCount) {
               N startPoint = var10.next();
               this.propagateValue(startPoint, dPrime, edgeFn.computeTarget(this.val(n, d)));
            }
         }
      }

   }

   protected V joinValueAt(N unit, D fact, V curr, V newVal) {
      return this.valueLattice.join(curr, newVal);
   }

   private void propagateValue(N nHashN, D nHashD, V v) {
      synchronized(this.val) {
         V valNHash = this.val(nHashN, nHashD);
         V vPrime = this.joinValueAt(nHashN, nHashD, valNHash, v);
         if (!vPrime.equals(valNHash)) {
            this.setVal(nHashN, nHashD, vPrime);
            this.scheduleValueProcessing(new IDESolver.ValuePropagationTask(new Pair(nHashN, nHashD)));
         }

      }
   }

   private V val(N nHashN, D nHashD) {
      Object l;
      synchronized(this.val) {
         l = this.val.get(nHashN, nHashD);
      }

      return l == null ? this.valueLattice.topElement() : l;
   }

   private void setVal(N nHashN, D nHashD, V l) {
      synchronized(this.val) {
         if (l == this.valueLattice.topElement()) {
            this.val.remove(nHashN, nHashD);
         } else {
            this.val.put(nHashN, nHashD, l);
         }
      }

      logger.debug("VALUE: {} {} {} {}", this.icfg.getMethodOf(nHashN), nHashN, nHashD, l);
   }

   private EdgeFunction<V> jumpFunction(PathEdge<N, D> edge) {
      synchronized(this.jumpFn) {
         EdgeFunction<V> function = (EdgeFunction)this.jumpFn.forwardLookup(edge.factAtSource(), edge.getTarget()).get(edge.factAtTarget());
         return function == null ? this.allTop : function;
      }
   }

   protected Set<Table.Cell<N, D, EdgeFunction<V>>> endSummary(N sP, D d3) {
      Table<N, D, EdgeFunction<V>> map = (Table)this.endSummary.get(sP, d3);
      return map == null ? Collections.emptySet() : map.cellSet();
   }

   private void addEndSummary(N sP, D d1, N eP, D d2, EdgeFunction<V> f) {
      Table<N, D, EdgeFunction<V>> summaries = (Table)this.endSummary.get(sP, d1);
      if (summaries == null) {
         summaries = HashBasedTable.create();
         this.endSummary.put(sP, d1, summaries);
      }

      ((Table)summaries).put(eP, d2, f);
   }

   protected Map<N, Set<D>> incoming(D d1, N sP) {
      synchronized(this.incoming) {
         Map<N, Set<D>> map = (Map)this.incoming.get(sP, d1);
         return map == null ? Collections.emptyMap() : map;
      }
   }

   protected void addIncoming(N sP, D d3, N n, D d2) {
      synchronized(this.incoming) {
         Map<N, Set<D>> summaries = (Map)this.incoming.get(sP, d3);
         if (summaries == null) {
            summaries = new HashMap();
            this.incoming.put(sP, d3, summaries);
         }

         Set<D> set = (Set)((Map)summaries).get(n);
         if (set == null) {
            set = new HashSet();
            ((Map)summaries).put(n, set);
         }

         ((Set)set).add(d2);
      }
   }

   public V resultAt(N stmt, D value) {
      return this.val.get(stmt, value);
   }

   public Map<D, V> resultsAt(N stmt) {
      return Maps.filterKeys(this.val.row(stmt), new Predicate<D>() {
         public boolean apply(D val) {
            return val != IDESolver.this.zeroValue;
         }
      });
   }

   protected CountingThreadPoolExecutor getExecutor() {
      return new CountingThreadPoolExecutor(1, this.numThreads, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue());
   }

   protected String getDebugName() {
      return "";
   }

   public void printStats() {
      if (logger.isDebugEnabled()) {
         if (this.ffCache != null) {
            this.ffCache.printStats();
         }

         if (this.efCache != null) {
            this.efCache.printStats();
         }
      } else {
         logger.info("No statistics were collected, as DEBUG is disabled.");
      }

   }

   static {
      DEBUG = logger.isDebugEnabled();
   }

   private class ValueComputationTask implements Runnable {
      private final N[] values;
      final int num;

      public ValueComputationTask(N[] values, int num) {
         this.values = values;
         this.num = num;
      }

      public void run() {
         int sectionSize = (int)Math.floor((double)(this.values.length / IDESolver.this.numThreads)) + IDESolver.this.numThreads;

         for(int i = sectionSize * this.num; i < Math.min(sectionSize * (this.num + 1), this.values.length); ++i) {
            N n = this.values[i];
            Iterator var4 = IDESolver.this.icfg.getStartPointsOf(IDESolver.this.icfg.getMethodOf(n)).iterator();

            while(var4.hasNext()) {
               N sP = var4.next();
               Set<Table.Cell<D, D, EdgeFunction<V>>> lookupByTarget = IDESolver.this.jumpFn.lookupByTarget(n);

               for(Iterator var7 = lookupByTarget.iterator(); var7.hasNext(); ++IDESolver.this.flowFunctionApplicationCount) {
                  Table.Cell<D, D, EdgeFunction<V>> sourceValTargetValAndFunction = (Table.Cell)var7.next();
                  D dPrime = sourceValTargetValAndFunction.getRowKey();
                  D d = sourceValTargetValAndFunction.getColumnKey();
                  EdgeFunction<V> fPrime = (EdgeFunction)sourceValTargetValAndFunction.getValue();
                  synchronized(IDESolver.this.val) {
                     IDESolver.this.setVal(n, d, IDESolver.this.valueLattice.join(IDESolver.this.val(n, d), fPrime.computeTarget(IDESolver.this.val(sP, dPrime))));
                  }
               }
            }
         }

      }
   }

   private class ValuePropagationTask implements Runnable {
      private final Pair<N, D> nAndD;

      public ValuePropagationTask(Pair<N, D> nAndD) {
         this.nAndD = nAndD;
      }

      public void run() {
         N n = this.nAndD.getO1();
         if (IDESolver.this.icfg.isStartPoint(n) || IDESolver.this.initialSeeds.containsKey(n) || IDESolver.this.unbalancedRetSites.contains(n)) {
            IDESolver.this.propagateValueAtStart(this.nAndD, n);
         }

         if (IDESolver.this.icfg.isCallStmt(n)) {
            IDESolver.this.propagateValueAtCall(this.nAndD, n);
         }

      }
   }

   private class PathEdgeProcessingTask implements Runnable {
      private final PathEdge<N, D> edge;

      public PathEdgeProcessingTask(PathEdge<N, D> edge) {
         this.edge = edge;
      }

      public void run() {
         if (IDESolver.this.icfg.isCallStmt(this.edge.getTarget())) {
            IDESolver.this.processCall(this.edge);
         } else {
            if (IDESolver.this.icfg.isExitStmt(this.edge.getTarget())) {
               IDESolver.this.processExit(this.edge);
            }

            if (!IDESolver.this.icfg.getSuccsOf(this.edge.getTarget()).isEmpty()) {
               IDESolver.this.processNormalFlow(this.edge);
            }
         }

      }
   }
}
