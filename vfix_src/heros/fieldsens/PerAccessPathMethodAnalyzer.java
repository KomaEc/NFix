package heros.fieldsens;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import heros.fieldsens.structs.FactAtStatement;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;
import heros.utilities.DefaultValueMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> {
   private static final Logger logger = LoggerFactory.getLogger(PerAccessPathMethodAnalyzer.class);
   private Fact sourceFact;
   private final AccessPath<Field> accessPath;
   private Map<WrappedFactAtStatement<Field, Fact, Stmt, Method>, WrappedFactAtStatement<Field, Fact, Stmt, Method>> reachableStatements;
   private List<WrappedFactAtStatement<Field, Fact, Stmt, Method>> summaries;
   private Context<Field, Fact, Stmt, Method> context;
   private Method method;
   private DefaultValueMap<FactAtStatement<Fact, Stmt>, ReturnSiteResolver<Field, Fact, Stmt, Method>> returnSiteResolvers;
   private DefaultValueMap<FactAtStatement<Fact, Stmt>, ControlFlowJoinResolver<Field, Fact, Stmt, Method>> ctrFlowJoinResolvers;
   private CallEdgeResolver<Field, Fact, Stmt, Method> callEdgeResolver;
   private PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> parent;
   private Debugger<Field, Fact, Stmt, Method> debugger;

   public PerAccessPathMethodAnalyzer(Method method, Fact sourceFact, Context<Field, Fact, Stmt, Method> context, Debugger<Field, Fact, Stmt, Method> debugger) {
      this(method, sourceFact, context, debugger, new AccessPath(), (PerAccessPathMethodAnalyzer)null);
   }

   private PerAccessPathMethodAnalyzer(Method method, Fact sourceFact, Context<Field, Fact, Stmt, Method> context, Debugger<Field, Fact, Stmt, Method> debugger, AccessPath<Field> accPath, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> parent) {
      this.reachableStatements = Maps.newHashMap();
      this.summaries = Lists.newLinkedList();
      this.returnSiteResolvers = new DefaultValueMap<FactAtStatement<Fact, Stmt>, ReturnSiteResolver<Field, Fact, Stmt, Method>>() {
         protected ReturnSiteResolver<Field, Fact, Stmt, Method> createItem(FactAtStatement<Fact, Stmt> key) {
            return new ReturnSiteResolver(PerAccessPathMethodAnalyzer.this.context.factHandler, PerAccessPathMethodAnalyzer.this, key.stmt, PerAccessPathMethodAnalyzer.this.debugger);
         }
      };
      this.ctrFlowJoinResolvers = new DefaultValueMap<FactAtStatement<Fact, Stmt>, ControlFlowJoinResolver<Field, Fact, Stmt, Method>>() {
         protected ControlFlowJoinResolver<Field, Fact, Stmt, Method> createItem(FactAtStatement<Fact, Stmt> key) {
            return new ControlFlowJoinResolver(PerAccessPathMethodAnalyzer.this.context.factHandler, PerAccessPathMethodAnalyzer.this, key.stmt, PerAccessPathMethodAnalyzer.this.debugger);
         }
      };
      this.debugger = debugger;
      if (method == null) {
         throw new IllegalArgumentException("Method must be not null");
      } else {
         this.parent = parent;
         this.method = method;
         this.sourceFact = sourceFact;
         this.accessPath = accPath;
         this.context = context;
         if (parent == null) {
            this.callEdgeResolver = (CallEdgeResolver)(this.isZeroSource() ? new ZeroCallEdgeResolver(this, context.zeroHandler, debugger) : new CallEdgeResolver(this, debugger));
         } else {
            this.callEdgeResolver = this.isZeroSource() ? parent.callEdgeResolver : new CallEdgeResolver(this, debugger, parent.callEdgeResolver);
         }

         this.log("initialized");
      }
   }

   public PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createWithAccessPath(AccessPath<Field> accPath) {
      return new PerAccessPathMethodAnalyzer(this.method, this.sourceFact, this.context, this.debugger, accPath, this);
   }

   WrappedFact<Field, Fact, Stmt, Method> wrappedSource() {
      return new WrappedFact(this.sourceFact, this.accessPath, this.callEdgeResolver);
   }

   public AccessPath<Field> getAccessPath() {
      return this.accessPath;
   }

   private boolean isBootStrapped() {
      return this.callEdgeResolver.hasIncomingEdges() || !this.accessPath.isEmpty();
   }

   private void bootstrapAtMethodStartPoints() {
      this.callEdgeResolver.interest(this.callEdgeResolver);
      Iterator var1 = this.context.icfg.getStartPointsOf(this.method).iterator();

      while(var1.hasNext()) {
         Stmt startPoint = var1.next();
         WrappedFactAtStatement<Field, Fact, Stmt, Method> target = new WrappedFactAtStatement(startPoint, this.wrappedSource());
         if (!this.reachableStatements.containsKey(target)) {
            this.scheduleEdgeTo(target);
         }
      }

   }

   public void addInitialSeed(Stmt stmt) {
      this.scheduleEdgeTo(new WrappedFactAtStatement(stmt, this.wrappedSource()));
   }

   private void scheduleEdgeTo(Collection<Stmt> successors, WrappedFact<Field, Fact, Stmt, Method> fact) {
      Iterator var3 = successors.iterator();

      while(var3.hasNext()) {
         Stmt stmt = var3.next();
         this.scheduleEdgeTo(new WrappedFactAtStatement(stmt, fact));
      }

   }

   void scheduleEdgeTo(WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
      assert this.context.icfg.getMethodOf(factAtStmt.getStatement()).equals(this.method);

      if (this.reachableStatements.containsKey(factAtStmt)) {
         this.log("Merging " + factAtStmt);
         this.context.factHandler.merge(((WrappedFactAtStatement)this.reachableStatements.get(factAtStmt)).getWrappedFact().getFact(), factAtStmt.getWrappedFact().getFact());
      } else {
         this.log("Edge to " + factAtStmt);
         this.reachableStatements.put(factAtStmt, factAtStmt);
         this.context.scheduler.schedule(new PerAccessPathMethodAnalyzer.Job(factAtStmt));
         this.debugger.edgeTo(this, factAtStmt);
      }

   }

   void log(String message) {
      logger.trace("[{}; {}{}: " + message + "]", this.method, this.sourceFact, this.accessPath);
   }

   public String toString() {
      return this.method + "; " + this.sourceFact + this.accessPath;
   }

   void processCall(WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
      Collection<Method> calledMethods = this.context.icfg.getCalleesOfCallAt(factAtStmt.getStatement());
      Iterator var3 = calledMethods.iterator();

      while(var3.hasNext()) {
         Method calledMethod = var3.next();
         FlowFunction<Field, Fact, Stmt, Method> flowFunction = this.context.flowFunctions.getCallFlowFunction(factAtStmt.getStatement(), calledMethod);
         Collection<FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method>> targetFacts = flowFunction.computeTargets(factAtStmt.getFact(), new AccessPathHandler(factAtStmt.getAccessPath(), factAtStmt.getResolver(), this.debugger));
         Iterator var7 = targetFacts.iterator();

         while(var7.hasNext()) {
            FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method> targetFact = (FlowFunction.ConstrainedFact)var7.next();
            MethodAnalyzer<Field, Fact, Stmt, Method> analyzer = this.context.getAnalyzer(calledMethod);
            analyzer.addIncomingEdge(new CallEdge(this, factAtStmt, targetFact.getFact()));
         }
      }

      this.processCallToReturnEdge(factAtStmt);
   }

   void processExit(WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
      this.log("New Summary: " + factAtStmt);
      if (!this.summaries.add(factAtStmt)) {
         throw new AssertionError();
      } else {
         this.callEdgeResolver.applySummaries(factAtStmt);
         if (this.context.followReturnsPastSeeds && this.isZeroSource()) {
            Collection<Stmt> callSites = this.context.icfg.getCallersOf(this.method);
            Iterator var3 = callSites.iterator();

            while(var3.hasNext()) {
               Stmt callSite = var3.next();
               Collection<Stmt> returnSites = this.context.icfg.getReturnSitesOfCallAt(callSite);
               Iterator var6 = returnSites.iterator();

               while(var6.hasNext()) {
                  Stmt returnSite = var6.next();
                  FlowFunction<Field, Fact, Stmt, Method> flowFunction = this.context.flowFunctions.getReturnFlowFunction(callSite, this.method, factAtStmt.getStatement(), returnSite);
                  Collection<FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method>> targetFacts = flowFunction.computeTargets(factAtStmt.getFact(), new AccessPathHandler(factAtStmt.getAccessPath(), factAtStmt.getResolver(), this.debugger));
                  Iterator var10 = targetFacts.iterator();

                  while(var10.hasNext()) {
                     FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method> targetFact = (FlowFunction.ConstrainedFact)var10.next();
                     this.context.getAnalyzer(this.context.icfg.getMethodOf(callSite)).addUnbalancedReturnFlow(new WrappedFactAtStatement(returnSite, targetFact.getFact()), callSite);
                  }
               }
            }

            if (callSites.isEmpty()) {
               FlowFunction<Field, Fact, Stmt, Method> flowFunction = this.context.flowFunctions.getReturnFlowFunction((Object)null, this.method, factAtStmt.getStatement(), (Object)null);
               flowFunction.computeTargets(factAtStmt.getFact(), new AccessPathHandler(factAtStmt.getAccessPath(), factAtStmt.getResolver(), this.debugger));
            }
         }

      }
   }

   private void processCallToReturnEdge(WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
      if (this.isLoopStart(factAtStmt.getStatement())) {
         ((ControlFlowJoinResolver)this.ctrFlowJoinResolvers.getOrCreate(factAtStmt.getAsFactAtStatement())).addIncoming(factAtStmt.getWrappedFact());
      } else {
         this.processNonJoiningCallToReturnFlow(factAtStmt);
      }

   }

   private void processNonJoiningCallToReturnFlow(WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
      Collection<Stmt> returnSites = this.context.icfg.getReturnSitesOfCallAt(factAtStmt.getStatement());
      Iterator var3 = returnSites.iterator();

      while(var3.hasNext()) {
         Stmt returnSite = var3.next();
         FlowFunction<Field, Fact, Stmt, Method> flowFunction = this.context.flowFunctions.getCallToReturnFlowFunction(factAtStmt.getStatement(), returnSite);
         Collection<FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method>> targetFacts = flowFunction.computeTargets(factAtStmt.getFact(), new AccessPathHandler(factAtStmt.getAccessPath(), factAtStmt.getResolver(), this.debugger));
         Iterator var7 = targetFacts.iterator();

         while(var7.hasNext()) {
            FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method> targetFact = (FlowFunction.ConstrainedFact)var7.next();
            this.scheduleEdgeTo(new WrappedFactAtStatement(returnSite, targetFact.getFact()));
         }
      }

   }

   private void processNormalFlow(WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
      if (this.isLoopStart(factAtStmt.getStatement())) {
         ((ControlFlowJoinResolver)this.ctrFlowJoinResolvers.getOrCreate(factAtStmt.getAsFactAtStatement())).addIncoming(factAtStmt.getWrappedFact());
      } else {
         this.processNormalNonJoiningFlow(factAtStmt);
      }

   }

   private boolean isLoopStart(Stmt stmt) {
      int numberOfPredecessors = this.context.icfg.getPredsOf(stmt).size();
      if (numberOfPredecessors > 1 && !this.context.icfg.isExitStmt(stmt) || this.context.icfg.isStartPoint(stmt) && numberOfPredecessors > 0) {
         Set<Stmt> visited = Sets.newHashSet();
         List<Stmt> worklist = Lists.newLinkedList();
         worklist.addAll(this.context.icfg.getPredsOf(stmt));

         while(!worklist.isEmpty()) {
            Stmt current = worklist.remove(0);
            if (current.equals(stmt)) {
               return true;
            }

            if (visited.add(current)) {
               worklist.addAll(this.context.icfg.getPredsOf(current));
            }
         }
      }

      return false;
   }

   void processFlowFromJoinStmt(WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
      if (this.context.icfg.isCallStmt(factAtStmt.getStatement())) {
         this.processNonJoiningCallToReturnFlow(factAtStmt);
      } else {
         this.processNormalNonJoiningFlow(factAtStmt);
      }

   }

   private void processNormalNonJoiningFlow(WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
      final List<Stmt> successors = this.context.icfg.getSuccsOf(factAtStmt.getStatement());
      FlowFunction<Field, Fact, Stmt, Method> flowFunction = this.context.flowFunctions.getNormalFlowFunction(factAtStmt.getStatement());
      Collection<FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method>> targetFacts = flowFunction.computeTargets(factAtStmt.getFact(), new AccessPathHandler(factAtStmt.getAccessPath(), factAtStmt.getResolver(), this.debugger));
      Iterator var5 = targetFacts.iterator();

      while(var5.hasNext()) {
         final FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method> targetFact = (FlowFunction.ConstrainedFact)var5.next();
         if (targetFact.getConstraint() == null) {
            this.scheduleEdgeTo(successors, targetFact.getFact());
         } else {
            targetFact.getFact().getResolver().resolve(targetFact.getConstraint(), new InterestCallback<Field, Fact, Stmt, Method>() {
               public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
                  analyzer.scheduleEdgeTo(successors, new WrappedFact(targetFact.getFact().getFact(), targetFact.getFact().getAccessPath(), resolver));
               }

               public void canBeResolvedEmpty() {
                  PerAccessPathMethodAnalyzer.this.callEdgeResolver.resolve(targetFact.getConstraint(), this);
               }
            });
         }
      }

   }

   public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {
      if (this.isBootStrapped()) {
         this.context.factHandler.merge(this.sourceFact, incEdge.getCalleeSourceFact().getFact());
      } else {
         this.bootstrapAtMethodStartPoints();
      }

      this.callEdgeResolver.addIncoming(incEdge);
   }

   void applySummary(CallEdge<Field, Fact, Stmt, Method> incEdge, WrappedFactAtStatement<Field, Fact, Stmt, Method> exitFact) {
      Collection<Stmt> returnSites = this.context.icfg.getReturnSitesOfCallAt(incEdge.getCallSite());
      Iterator var4 = returnSites.iterator();

      while(var4.hasNext()) {
         Stmt returnSite = var4.next();
         FlowFunction<Field, Fact, Stmt, Method> flowFunction = this.context.flowFunctions.getReturnFlowFunction(incEdge.getCallSite(), this.method, exitFact.getStatement(), returnSite);
         Set<FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method>> targets = flowFunction.computeTargets(exitFact.getFact(), new AccessPathHandler(exitFact.getAccessPath(), exitFact.getResolver(), this.debugger));
         Iterator var8 = targets.iterator();

         while(var8.hasNext()) {
            FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method> targetFact = (FlowFunction.ConstrainedFact)var8.next();
            this.context.factHandler.restoreCallingContext(targetFact.getFact().getFact(), incEdge.getCallerCallSiteFact().getFact());
            this.scheduleReturnEdge(incEdge, targetFact.getFact(), returnSite);
         }
      }

   }

   public void scheduleUnbalancedReturnEdgeTo(WrappedFactAtStatement<Field, Fact, Stmt, Method> fact) {
      ReturnSiteResolver<Field, Fact, Stmt, Method> resolver = (ReturnSiteResolver)this.returnSiteResolvers.getOrCreate(fact.getAsFactAtStatement());
      resolver.addIncoming(new WrappedFact(fact.getWrappedFact().getFact(), fact.getWrappedFact().getAccessPath(), fact.getWrappedFact().getResolver()), (Resolver)null, AccessPath.Delta.empty());
   }

   private void scheduleReturnEdge(CallEdge<Field, Fact, Stmt, Method> incEdge, WrappedFact<Field, Fact, Stmt, Method> fact, Stmt returnSite) {
      AccessPath.Delta<Field> delta = this.accessPath.getDeltaTo(incEdge.getCalleeSourceFact().getAccessPath());
      ReturnSiteResolver<Field, Fact, Stmt, Method> returnSiteResolver = (ReturnSiteResolver)incEdge.getCallerAnalyzer().returnSiteResolvers.getOrCreate(new FactAtStatement(fact.getFact(), returnSite));
      returnSiteResolver.addIncoming(fact, incEdge.getCalleeSourceFact().getResolver(), delta);
   }

   void applySummaries(CallEdge<Field, Fact, Stmt, Method> incEdge) {
      Iterator var2 = this.summaries.iterator();

      while(var2.hasNext()) {
         WrappedFactAtStatement<Field, Fact, Stmt, Method> summary = (WrappedFactAtStatement)var2.next();
         this.applySummary(incEdge, summary);
      }

   }

   public boolean isZeroSource() {
      return this.sourceFact.equals(this.context.zeroValue);
   }

   public CallEdgeResolver<Field, Fact, Stmt, Method> getCallEdgeResolver() {
      return this.callEdgeResolver;
   }

   public Method getMethod() {
      return this.method;
   }

   private class Job implements Runnable {
      private WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt;

      public Job(WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
         this.factAtStmt = factAtStmt;
         PerAccessPathMethodAnalyzer.this.debugger.newJob(PerAccessPathMethodAnalyzer.this, factAtStmt);
      }

      public void run() {
         PerAccessPathMethodAnalyzer.this.debugger.jobStarted(PerAccessPathMethodAnalyzer.this, this.factAtStmt);
         if (PerAccessPathMethodAnalyzer.this.context.icfg.isCallStmt(this.factAtStmt.getStatement())) {
            PerAccessPathMethodAnalyzer.this.processCall(this.factAtStmt);
         } else {
            if (PerAccessPathMethodAnalyzer.this.context.icfg.isExitStmt(this.factAtStmt.getStatement())) {
               PerAccessPathMethodAnalyzer.this.processExit(this.factAtStmt);
            }

            if (!PerAccessPathMethodAnalyzer.this.context.icfg.getSuccsOf(this.factAtStmt.getStatement()).isEmpty()) {
               PerAccessPathMethodAnalyzer.this.processNormalFlow(this.factAtStmt);
            }
         }

         PerAccessPathMethodAnalyzer.this.debugger.jobFinished(PerAccessPathMethodAnalyzer.this, this.factAtStmt);
      }

      public String toString() {
         return "Job: " + this.factAtStmt;
      }
   }
}
