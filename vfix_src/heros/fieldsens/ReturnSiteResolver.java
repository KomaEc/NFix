package heros.fieldsens;

import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.ReturnEdge;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;

public class ReturnSiteResolver<Field, Fact, Stmt, Method> extends ResolverTemplate<Field, Fact, Stmt, Method, ReturnEdge<Field, Fact, Stmt, Method>> {
   private Stmt returnSite;
   private boolean propagated;
   private Fact sourceFact;
   private FactMergeHandler<Fact> factMergeHandler;

   public ReturnSiteResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Stmt returnSite, Debugger<Field, Fact, Stmt, Method> debugger) {
      this(factMergeHandler, analyzer, returnSite, (Object)null, debugger, new AccessPath(), (ReturnSiteResolver)null);
      this.factMergeHandler = factMergeHandler;
      this.propagated = false;
   }

   private ReturnSiteResolver(FactMergeHandler<Fact> factMergeHandler, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Stmt returnSite, Fact sourceFact, Debugger<Field, Fact, Stmt, Method> debugger, AccessPath<Field> resolvedAccPath, ReturnSiteResolver<Field, Fact, Stmt, Method> parent) {
      super(analyzer, resolvedAccPath, parent, debugger);
      this.propagated = false;
      this.factMergeHandler = factMergeHandler;
      this.returnSite = returnSite;
      this.sourceFact = sourceFact;
      this.propagated = true;
   }

   public String toString() {
      return "<" + this.resolvedAccessPath + ":" + this.returnSite + " in " + this.analyzer.getMethod() + ">";
   }

   protected AccessPath<Field> getAccessPathOf(ReturnEdge<Field, Fact, Stmt, Method> inc) {
      return inc.usedAccessPathOfIncResolver.applyTo(inc.incAccessPath);
   }

   public void addIncoming(WrappedFact<Field, Fact, Stmt, Method> fact, Resolver<Field, Fact, Stmt, Method> resolverAtCaller, AccessPath.Delta<Field> callDelta) {
      this.addIncoming(new ReturnEdge(fact, resolverAtCaller, callDelta));
   }

   protected void processIncomingGuaranteedPrefix(ReturnEdge<Field, Fact, Stmt, Method> retEdge) {
      if (this.propagated) {
         this.factMergeHandler.merge(this.sourceFact, retEdge.incFact);
      } else {
         this.propagated = true;
         this.sourceFact = retEdge.incFact;
         this.analyzer.scheduleEdgeTo(new WrappedFactAtStatement(this.returnSite, new WrappedFact(retEdge.incFact, new AccessPath(), this)));
      }

   }

   protected void processIncomingPotentialPrefix(ReturnEdge<Field, Fact, Stmt, Method> retEdge) {
      this.log("Incoming potential prefix:  " + retEdge);
      this.resolveViaDelta(retEdge);
   }

   protected void log(String message) {
      this.analyzer.log("Return Site " + this.toString() + ": " + message);
   }

   protected ResolverTemplate<Field, Fact, Stmt, Method, ReturnEdge<Field, Fact, Stmt, Method>> createNestedResolver(AccessPath<Field> newAccPath) {
      return new ReturnSiteResolver(this.factMergeHandler, this.analyzer, this.returnSite, this.sourceFact, this.debugger, newAccPath, this);
   }

   public Stmt getReturnSite() {
      return this.returnSite;
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

   private void resolveViaDelta(final ReturnEdge<Field, Fact, Stmt, Method> retEdge) {
      if (this.isNullOrCallEdgeResolver(retEdge.incResolver)) {
         this.resolveViaDeltaAndPotentiallyDelegateToCallSite(retEdge);
      } else {
         AccessPath.Delta<Field> delta = retEdge.usedAccessPathOfIncResolver.applyTo(retEdge.incAccessPath).getDeltaTo(this.resolvedAccessPath);

         assert delta.accesses.length <= 1;

         retEdge.incResolver.resolve(new DeltaConstraint(delta), new InterestCallback<Field, Fact, Stmt, Method>() {
            public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
               if (resolver instanceof ZeroCallEdgeResolver) {
                  ReturnSiteResolver.this.interest(((ZeroCallEdgeResolver)resolver).copyWithAnalyzer(ReturnSiteResolver.this.analyzer));
               } else {
                  ReturnSiteResolver.this.incomingEdges.add(retEdge.copyWithIncomingResolver(resolver, retEdge.incAccessPath.getDeltaTo(ReturnSiteResolver.this.resolvedAccessPath)));
                  ReturnSiteResolver.this.interest(ReturnSiteResolver.this);
               }

            }

            public void canBeResolvedEmpty() {
               ReturnSiteResolver.this.resolveViaDeltaAndPotentiallyDelegateToCallSite(retEdge);
            }
         });
      }

   }

   private void resolveViaDeltaAndPotentiallyDelegateToCallSite(ReturnEdge<Field, Fact, Stmt, Method> retEdge) {
      AccessPath<Field> inc = retEdge.usedAccessPathOfIncResolver.applyTo(retEdge.incAccessPath);
      if (retEdge.callDelta.canBeAppliedTo(inc)) {
         AccessPath<Field> currAccPath = retEdge.callDelta.applyTo(inc);
         if (this.resolvedAccessPath.isPrefixOf(currAccPath) == AccessPath.PrefixTestResult.GUARANTEED_PREFIX) {
            this.incomingEdges.add(retEdge.copyWithIncomingResolver((Resolver)null, retEdge.usedAccessPathOfIncResolver));
            this.interest(this);
         } else if (currAccPath.isPrefixOf(this.resolvedAccessPath).atLeast(AccessPath.PrefixTestResult.POTENTIAL_PREFIX)) {
            this.resolveViaCallSiteResolver(retEdge, currAccPath);
         }

      }
   }

   protected void resolveViaCallSiteResolver(ReturnEdge<Field, Fact, Stmt, Method> retEdge, AccessPath<Field> currAccPath) {
      if (this.isNullOrCallEdgeResolver(retEdge.resolverAtCaller)) {
         this.canBeResolvedEmpty();
      } else {
         retEdge.resolverAtCaller.resolve(new DeltaConstraint(currAccPath.getDeltaTo(this.resolvedAccessPath)), new InterestCallback<Field, Fact, Stmt, Method>() {
            public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
               ReturnSiteResolver.this.interest(resolver);
            }

            public void canBeResolvedEmpty() {
               ReturnSiteResolver.this.canBeResolvedEmpty();
            }
         });
      }

   }
}
