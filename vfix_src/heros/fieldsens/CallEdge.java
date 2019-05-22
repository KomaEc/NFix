package heros.fieldsens;

import heros.fieldsens.structs.DeltaConstraint;
import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;

public class CallEdge<Field, Fact, Stmt, Method> {
   private WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact;
   private PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> callerAnalyzer;
   private WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtCallSite;

   public CallEdge(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> callerAnalyzer, WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtCallSite, WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact) {
      this.callerAnalyzer = callerAnalyzer;
      this.factAtCallSite = factAtCallSite;
      this.calleeSourceFact = calleeSourceFact;
   }

   public WrappedFact<Field, Fact, Stmt, Method> getCalleeSourceFact() {
      return this.calleeSourceFact;
   }

   public WrappedFact<Field, Fact, Stmt, Method> getCallerCallSiteFact() {
      return this.factAtCallSite.getWrappedFact();
   }

   public WrappedFact<Field, Fact, Stmt, Method> getCallerSourceFact() {
      return this.callerAnalyzer.wrappedSource();
   }

   public Stmt getCallSite() {
      return this.factAtCallSite.getStatement();
   }

   public PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> getCallerAnalyzer() {
      return this.callerAnalyzer;
   }

   public void registerInterestCallback(final PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> interestedAnalyzer) {
      final AccessPath.Delta<Field> delta = this.calleeSourceFact.getAccessPath().getDeltaTo(interestedAnalyzer.getAccessPath());
      if (this.factAtCallSite.canDeltaBeApplied(delta)) {
         this.factAtCallSite.getWrappedFact().getResolver().resolve(new DeltaConstraint(delta), new InterestCallback<Field, Fact, Stmt, Method>() {
            public void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
               WrappedFact<Field, Fact, Stmt, Method> calleeSourceFactWithDelta = new WrappedFact(CallEdge.this.calleeSourceFact.getFact(), delta.applyTo(CallEdge.this.calleeSourceFact.getAccessPath()), resolver);

               assert interestedAnalyzer.getAccessPath().isPrefixOf(calleeSourceFactWithDelta.getAccessPath()) == AccessPath.PrefixTestResult.GUARANTEED_PREFIX;

               CallEdge<Field, Fact, Stmt, Method> newCallEdge = new CallEdge(analyzer, new WrappedFactAtStatement(CallEdge.this.factAtCallSite.getStatement(), new WrappedFact(CallEdge.this.factAtCallSite.getWrappedFact().getFact(), delta.applyTo(CallEdge.this.factAtCallSite.getWrappedFact().getAccessPath()), resolver)), calleeSourceFactWithDelta);
               if (resolver instanceof ZeroCallEdgeResolver) {
                  interestedAnalyzer.getCallEdgeResolver().incomingEdges.add(newCallEdge);
                  interestedAnalyzer.getCallEdgeResolver().interest(((ZeroCallEdgeResolver)resolver).copyWithAnalyzer(interestedAnalyzer));
                  interestedAnalyzer.getCallEdgeResolver().processIncomingGuaranteedPrefix(newCallEdge);
               } else {
                  interestedAnalyzer.addIncomingEdge(newCallEdge);
               }

            }

            public void canBeResolvedEmpty() {
               CallEdge.this.callerAnalyzer.getCallEdgeResolver().resolve(new DeltaConstraint(delta), this);
            }
         });
      }
   }

   public String toString() {
      return "[IncEdge CSite:" + this.getCallSite() + ", Caller-Edge: " + this.getCallerSourceFact() + "->" + this.getCallerCallSiteFact() + ",  CalleeFact: " + this.calleeSourceFact + "]";
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.calleeSourceFact == null ? 0 : this.calleeSourceFact.hashCode());
      result = 31 * result + (this.callerAnalyzer == null ? 0 : this.callerAnalyzer.hashCode());
      result = 31 * result + (this.factAtCallSite == null ? 0 : this.factAtCallSite.hashCode());
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
         CallEdge other = (CallEdge)obj;
         if (this.calleeSourceFact == null) {
            if (other.calleeSourceFact != null) {
               return false;
            }
         } else if (!this.calleeSourceFact.equals(other.calleeSourceFact)) {
            return false;
         }

         if (this.callerAnalyzer == null) {
            if (other.callerAnalyzer != null) {
               return false;
            }
         } else if (!this.callerAnalyzer.equals(other.callerAnalyzer)) {
            return false;
         }

         if (this.factAtCallSite == null) {
            if (other.factAtCallSite != null) {
               return false;
            }
         } else if (!this.factAtCallSite.equals(other.factAtCallSite)) {
            return false;
         }

         return true;
      }
   }
}
