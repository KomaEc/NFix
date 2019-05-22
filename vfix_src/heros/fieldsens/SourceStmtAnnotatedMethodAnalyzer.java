package heros.fieldsens;

import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;
import heros.utilities.DefaultValueMap;

public class SourceStmtAnnotatedMethodAnalyzer<Field, Fact, Stmt, Method> implements MethodAnalyzer<Field, Fact, Stmt, Method> {
   private Method method;
   private DefaultValueMap<SourceStmtAnnotatedMethodAnalyzer.Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = new DefaultValueMap<SourceStmtAnnotatedMethodAnalyzer.Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() {
      protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(SourceStmtAnnotatedMethodAnalyzer.Key<Fact, Stmt> key) {
         return new PerAccessPathMethodAnalyzer(SourceStmtAnnotatedMethodAnalyzer.this.method, key.fact, SourceStmtAnnotatedMethodAnalyzer.this.context, SourceStmtAnnotatedMethodAnalyzer.this.debugger);
      }
   };
   private Context<Field, Fact, Stmt, Method> context;
   private SourceStmtAnnotatedMethodAnalyzer.Synchronizer<Stmt> synchronizer;
   private Debugger<Field, Fact, Stmt, Method> debugger;

   public SourceStmtAnnotatedMethodAnalyzer(Method method, Context<Field, Fact, Stmt, Method> context, SourceStmtAnnotatedMethodAnalyzer.Synchronizer<Stmt> synchronizer, Debugger<Field, Fact, Stmt, Method> debugger) {
      this.method = method;
      this.context = context;
      this.synchronizer = synchronizer;
      this.debugger = debugger;
   }

   public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {
      WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();
      SourceStmtAnnotatedMethodAnalyzer.Key<Fact, Stmt> key = new SourceStmtAnnotatedMethodAnalyzer.Key(calleeSourceFact.getFact(), (Object)null);
      PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = (PerAccessPathMethodAnalyzer)this.perSourceAnalyzer.getOrCreate(key);
      analyzer.addIncomingEdge(incEdge);
   }

   public void addInitialSeed(Stmt startPoint, Fact val) {
      SourceStmtAnnotatedMethodAnalyzer.Key<Fact, Stmt> key = new SourceStmtAnnotatedMethodAnalyzer.Key(val, startPoint);
      ((PerAccessPathMethodAnalyzer)this.perSourceAnalyzer.getOrCreate(key)).addInitialSeed(startPoint);
   }

   public void addUnbalancedReturnFlow(final WrappedFactAtStatement<Field, Fact, Stmt, Method> target, final Stmt callSite) {
      this.synchronizer.synchronizeOnStmt(callSite, new Runnable() {
         public void run() {
            SourceStmtAnnotatedMethodAnalyzer.Key<Fact, Stmt> key = new SourceStmtAnnotatedMethodAnalyzer.Key(SourceStmtAnnotatedMethodAnalyzer.this.context.zeroValue, callSite);
            ((PerAccessPathMethodAnalyzer)SourceStmtAnnotatedMethodAnalyzer.this.perSourceAnalyzer.getOrCreate(key)).scheduleUnbalancedReturnEdgeTo(target);
         }
      });
   }

   private static class Key<Fact, Stmt> {
      private Fact fact;
      private Stmt stmt;

      private Key(Fact fact, Stmt stmt) {
         this.fact = fact;
         this.stmt = stmt;
      }

      public int hashCode() {
         int prime = true;
         int result = 1;
         int result = 31 * result + (this.fact == null ? 0 : this.fact.hashCode());
         result = 31 * result + (this.stmt == null ? 0 : this.stmt.hashCode());
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
            SourceStmtAnnotatedMethodAnalyzer.Key other = (SourceStmtAnnotatedMethodAnalyzer.Key)obj;
            if (this.fact == null) {
               if (other.fact != null) {
                  return false;
               }
            } else if (!this.fact.equals(other.fact)) {
               return false;
            }

            if (this.stmt == null) {
               if (other.stmt != null) {
                  return false;
               }
            } else if (!this.stmt.equals(other.stmt)) {
               return false;
            }

            return true;
         }
      }

      // $FF: synthetic method
      Key(Object x0, Object x1, Object x2) {
         this(x0, x1);
      }
   }

   public interface Synchronizer<Stmt> {
      void synchronizeOnStmt(Stmt var1, Runnable var2);
   }
}
