package heros.fieldsens;

import heros.InterproceduralCFG;
import heros.fieldsens.structs.WrappedFactAtStatement;

public interface Debugger<Field, Fact, Stmt, Method> {
   void setICFG(InterproceduralCFG<Stmt, Method> var1);

   void initialSeed(Stmt var1);

   void edgeTo(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> var1, WrappedFactAtStatement<Field, Fact, Stmt, Method> var2);

   void newResolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> var1, Resolver<Field, Fact, Stmt, Method> var2);

   void newJob(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> var1, WrappedFactAtStatement<Field, Fact, Stmt, Method> var2);

   void jobStarted(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> var1, WrappedFactAtStatement<Field, Fact, Stmt, Method> var2);

   void jobFinished(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> var1, WrappedFactAtStatement<Field, Fact, Stmt, Method> var2);

   void askedToResolve(Resolver<Field, Fact, Stmt, Method> var1, FlowFunction.Constraint<Field> var2);

   public static class NullDebugger<Field, Fact, Stmt, Method> implements Debugger<Field, Fact, Stmt, Method> {
      public void setICFG(InterproceduralCFG<Stmt, Method> icfg) {
      }

      public void initialSeed(Stmt stmt) {
      }

      public void edgeTo(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
      }

      public void newResolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, Resolver<Field, Fact, Stmt, Method> resolver) {
      }

      public void newJob(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
      }

      public void jobStarted(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
      }

      public void jobFinished(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, WrappedFactAtStatement<Field, Fact, Stmt, Method> factAtStmt) {
      }

      public void askedToResolve(Resolver<Field, Fact, Stmt, Method> resolver, FlowFunction.Constraint<Field> constraint) {
      }
   }
}
