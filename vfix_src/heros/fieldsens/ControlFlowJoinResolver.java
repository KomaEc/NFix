package heros.fieldsens;

import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;

public class ControlFlowJoinResolver<Field, Fact, Stmt, Method> extends ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> {
   private Stmt joinStmt;
   private boolean propagated;
   private Fact sourceFact;
   private FactMergeHandler<Fact> factMergeHandler;

   public ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Stmt joinStmt, Debugger<Field, Fact, Stmt, Method> debugger) {
      this(factMergeHandler, analyzer, joinStmt, (Object)null, new AccessPath(), debugger, (ControlFlowJoinResolver)null);
      this.factMergeHandler = factMergeHandler;
      this.propagated = false;
   }

   private ControlFlowJoinResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Stmt joinStmt, Fact sourceFact, AccessPath<Field> resolvedAccPath, Debugger<Field, Fact, Stmt, Method> debugger, ControlFlowJoinResolver<Field, Fact, Stmt, Method> parent) {
      super(analyzer, resolvedAccPath, parent, debugger);
      this.propagated = false;
      this.factMergeHandler = factMergeHandler;
      this.joinStmt = joinStmt;
      this.sourceFact = sourceFact;
      this.propagated = true;
   }

   protected AccessPath<Field> getAccessPathOf(WrappedFact<Field, Fact, Stmt, Method> inc) {
      return inc.getAccessPath();
   }

   protected void processIncomingGuaranteedPrefix(WrappedFact<Field, Fact, Stmt, Method> fact) {
      if (this.propagated) {
         this.factMergeHandler.merge(this.sourceFact, fact.getFact());
      } else {
         this.propagated = true;
         this.sourceFact = fact.getFact();
         this.analyzer.processFlowFromJoinStmt(new WrappedFactAtStatement(this.joinStmt, new WrappedFact(fact.getFact(), new AccessPath(), this)));
      }

   }

   private boolean isNullOrCallEdgeResolver(Resolver<Field, Fact, Stmt, Method> resolver) {
      if (resolver == null) {
         return true;
      } else if (resolver instanceof CallEdgeResolver) {
         return !(resolver instanceof ZeroCallEdgeResolver);
      } else {
         return false;
      }
   }

   protected void processIncomingPotentialPrefix(WrappedFact<Field, Fact, Stmt, Method> fact) {
      if (this.isNullOrCallEdgeResolver(fact.getResolver())) {
         this.canBeResolvedEmpty();
      } else {
         this.lock();
         AccessPath.Delta<Field> delta = fact.getAccessPath().getDeltaTo(this.resolvedAccessPath);
         fact.getResolver().resolve(new DeltaConstraint(delta), new InterestCallback<Field, Fact, Stmt, Method>() {
            public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
               ControlFlowJoinResolver.this.interest(resolver);
            }

            public void canBeResolvedEmpty() {
               ControlFlowJoinResolver.this.canBeResolvedEmpty();
            }
         });
         this.unlock();
      }

   }

   protected ResolverTemplate<Field, Fact, Stmt, Method, WrappedFact<Field, Fact, Stmt, Method>> createNestedResolver(AccessPath<Field> newAccPath) {
      return new ControlFlowJoinResolver(this.factMergeHandler, this.analyzer, this.joinStmt, this.sourceFact, newAccPath, this.debugger, this);
   }

   protected void log(String message) {
      this.analyzer.log("Join Stmt " + this.toString() + ": " + message);
   }

   public String toString() {
      return "<" + this.resolvedAccessPath + ":" + this.joinStmt + " in " + this.analyzer.getMethod() + ">";
   }

   public Stmt getJoinStmt() {
      return this.joinStmt;
   }
}
