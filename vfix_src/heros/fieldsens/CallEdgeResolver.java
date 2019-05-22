package heros.fieldsens;

import com.google.common.collect.Lists;
import heros.fieldsens.structs.WrappedFactAtStatement;
import java.util.Iterator;

class CallEdgeResolver<Field, Fact, Stmt, Method> extends ResolverTemplate<Field, Fact, Stmt, Method, CallEdge<Field, Fact, Stmt, Method>> {
   public CallEdgeResolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Debugger<Field, Fact, Stmt, Method> debugger) {
      this(analyzer, debugger, (CallEdgeResolver)null);
   }

   public CallEdgeResolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Debugger<Field, Fact, Stmt, Method> debugger, CallEdgeResolver<Field, Fact, Stmt, Method> parent) {
      super(analyzer, analyzer.getAccessPath(), parent, debugger);
   }

   protected AccessPath<Field> getAccessPathOf(CallEdge<Field, Fact, Stmt, Method> inc) {
      return inc.getCalleeSourceFact().getAccessPath();
   }

   protected void processIncomingGuaranteedPrefix(CallEdge<Field, Fact, Stmt, Method> inc) {
      this.analyzer.applySummaries(inc);
   }

   protected void processIncomingPotentialPrefix(CallEdge<Field, Fact, Stmt, Method> inc) {
      this.lock();
      inc.registerInterestCallback(this.analyzer);
      this.unlock();
   }

   protected ResolverTemplate<Field, Fact, Stmt, Method, CallEdge<Field, Fact, Stmt, Method>> createNestedResolver(AccessPath<Field> newAccPath) {
      return this.analyzer.createWithAccessPath(newAccPath).getCallEdgeResolver();
   }

   public void applySummaries(WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
      Iterator var2 = Lists.newLinkedList(this.incomingEdges).iterator();

      while(var2.hasNext()) {
         CallEdge<Field, Fact, Stmt, Method> incEdge = (CallEdge)var2.next();
         this.analyzer.applySummary(incEdge, factAtStmt);
      }

   }

   public String toString() {
      return "<" + this.analyzer.getAccessPath() + ":" + this.analyzer.getMethod() + ">";
   }

   protected void log(String message) {
      this.analyzer.log(message);
   }

   public boolean hasIncomingEdges() {
      return !this.incomingEdges.isEmpty();
   }
}
