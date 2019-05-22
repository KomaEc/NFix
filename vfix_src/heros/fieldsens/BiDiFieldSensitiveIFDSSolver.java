package heros.fieldsens;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Sets;
import heros.InterproceduralCFG;
import java.util.Iterator;
import java.util.Set;

public class BiDiFieldSensitiveIFDSSolver<Field, Fact, Stmt, Method, I extends InterproceduralCFG<Stmt, Method>> {
   private FieldSensitiveIFDSSolver<Field, Fact, Stmt, Method, I> forwardSolver;
   private FieldSensitiveIFDSSolver<Field, Fact, Stmt, Method, I> backwardSolver;
   private Scheduler scheduler;
   private BiDiFieldSensitiveIFDSSolver.SynchronizerImpl<Stmt> forwardSynchronizer;
   private BiDiFieldSensitiveIFDSSolver.SynchronizerImpl<Stmt> backwardSynchronizer;

   public BiDiFieldSensitiveIFDSSolver(IFDSTabulationProblem<Stmt, Field, Fact, Method, I> forwardProblem, IFDSTabulationProblem<Stmt, Field, Fact, Method, I> backwardProblem, FactMergeHandler<Fact> factHandler, Debugger<Field, Fact, Stmt, Method> debugger, Scheduler scheduler) {
      this.scheduler = scheduler;
      this.forwardSynchronizer = new BiDiFieldSensitiveIFDSSolver.SynchronizerImpl();
      this.backwardSynchronizer = new BiDiFieldSensitiveIFDSSolver.SynchronizerImpl();
      this.forwardSynchronizer.otherSynchronizer = this.backwardSynchronizer;
      this.backwardSynchronizer.otherSynchronizer = this.forwardSynchronizer;
      this.forwardSolver = this.createSolver(forwardProblem, factHandler, debugger, this.forwardSynchronizer);
      this.backwardSolver = this.createSolver(backwardProblem, factHandler, debugger, this.backwardSynchronizer);
   }

   private FieldSensitiveIFDSSolver<Field, Fact, Stmt, Method, I> createSolver(IFDSTabulationProblem<Stmt, Field, Fact, Method, I> problem, FactMergeHandler<Fact> factHandler, Debugger<Field, Fact, Stmt, Method> debugger, final BiDiFieldSensitiveIFDSSolver.SynchronizerImpl<Stmt> synchronizer) {
      return new FieldSensitiveIFDSSolver<Field, Fact, Stmt, Method, I>(problem, factHandler, debugger, this.scheduler) {
         protected MethodAnalyzer<Field, Fact, Stmt, Method> createMethodAnalyzer(Method method) {
            return new SourceStmtAnnotatedMethodAnalyzer(method, this.context, synchronizer, this.debugger);
         }
      };
   }

   private static class SynchronizerImpl<Stmt> implements SourceStmtAnnotatedMethodAnalyzer.Synchronizer<Stmt> {
      private BiDiFieldSensitiveIFDSSolver.SynchronizerImpl<Stmt> otherSynchronizer;
      private Set<Stmt> leakedSources;
      private HashMultimap<Stmt, Runnable> pausedJobs;

      private SynchronizerImpl() {
         this.leakedSources = Sets.newHashSet();
         this.pausedJobs = HashMultimap.create();
      }

      public void synchronizeOnStmt(Stmt stmt, Runnable job) {
         this.leakedSources.add(stmt);
         if (this.otherSynchronizer.leakedSources.contains(stmt)) {
            job.run();
            Iterator var3 = this.otherSynchronizer.pausedJobs.get(stmt).iterator();

            while(var3.hasNext()) {
               Runnable runnable = (Runnable)var3.next();
               runnable.run();
            }

            this.otherSynchronizer.pausedJobs.removeAll(stmt);
         } else {
            this.pausedJobs.put(stmt, job);
         }

      }

      // $FF: synthetic method
      SynchronizerImpl(Object x0) {
         this();
      }
   }
}
