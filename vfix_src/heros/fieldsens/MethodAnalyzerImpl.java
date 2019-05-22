package heros.fieldsens;

import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;
import heros.utilities.DefaultValueMap;

public class MethodAnalyzerImpl<Field, Fact, Stmt, Method> implements MethodAnalyzer<Field, Fact, Stmt, Method> {
   private Method method;
   private DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = new DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() {
      protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Fact key) {
         return new PerAccessPathMethodAnalyzer(MethodAnalyzerImpl.this.method, key, MethodAnalyzerImpl.this.context, MethodAnalyzerImpl.this.debugger);
      }
   };
   private Context<Field, Fact, Stmt, Method> context;
   private Debugger<Field, Fact, Stmt, Method> debugger;

   MethodAnalyzerImpl(Method method, Context<Field, Fact, Stmt, Method> context, Debugger<Field, Fact, Stmt, Method> debugger) {
      this.method = method;
      this.context = context;
      this.debugger = debugger;
   }

   public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {
      WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();
      PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = (PerAccessPathMethodAnalyzer)this.perSourceAnalyzer.getOrCreate(calleeSourceFact.getFact());
      analyzer.addIncomingEdge(incEdge);
   }

   public void addInitialSeed(Stmt startPoint, Fact val) {
      ((PerAccessPathMethodAnalyzer)this.perSourceAnalyzer.getOrCreate(val)).addInitialSeed(startPoint);
   }

   public void addUnbalancedReturnFlow(WrappedFactAtStatement<Field, Fact, Stmt, Method> target, Stmt callSite) {
      ((PerAccessPathMethodAnalyzer)this.perSourceAnalyzer.getOrCreate(this.context.zeroValue)).scheduleUnbalancedReturnEdgeTo(target);
   }
}
