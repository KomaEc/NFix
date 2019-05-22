package heros.fieldsens;

import heros.InterproceduralCFG;
import heros.utilities.DefaultValueMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FieldSensitiveIFDSSolver<FieldRef, D, N, M, I extends InterproceduralCFG<N, M>> {
   protected static final Logger logger = LoggerFactory.getLogger(FieldSensitiveIFDSSolver.class);
   private DefaultValueMap<M, MethodAnalyzer<FieldRef, D, N, M>> methodAnalyzers = new DefaultValueMap<M, MethodAnalyzer<FieldRef, D, N, M>>() {
      protected MethodAnalyzer<FieldRef, D, N, M> createItem(M key) {
         return FieldSensitiveIFDSSolver.this.createMethodAnalyzer(key);
      }
   };
   private IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem;
   protected Context<FieldRef, D, N, M> context;
   protected Debugger<FieldRef, D, N, M> debugger;
   private Scheduler scheduler;

   public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem, FactMergeHandler<D> factHandler, Debugger<FieldRef, D, N, M> debugger, Scheduler scheduler) {
      this.tabulationProblem = tabulationProblem;
      this.scheduler = scheduler;
      this.debugger = (Debugger)(debugger == null ? new Debugger.NullDebugger() : debugger);
      this.debugger.setICFG(tabulationProblem.interproceduralCFG());
      this.context = this.initContext(tabulationProblem, factHandler);
      this.submitInitialSeeds();
   }

   private Context<FieldRef, D, N, M> initContext(IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem, FactMergeHandler<D> factHandler) {
      return new Context<FieldRef, D, N, M>(tabulationProblem, this.scheduler, factHandler) {
         public MethodAnalyzer<FieldRef, D, N, M> getAnalyzer(M method) {
            if (method == null) {
               throw new IllegalArgumentException("Method must be not null");
            } else {
               return (MethodAnalyzer)FieldSensitiveIFDSSolver.this.methodAnalyzers.getOrCreate(method);
            }
         }
      };
   }

   protected MethodAnalyzer<FieldRef, D, N, M> createMethodAnalyzer(M method) {
      return new MethodAnalyzerImpl(method, this.context, this.debugger);
   }

   private void submitInitialSeeds() {
      Iterator var1 = this.tabulationProblem.initialSeeds().entrySet().iterator();

      while(var1.hasNext()) {
         Entry<N, Set<D>> seed = (Entry)var1.next();
         N startPoint = seed.getKey();
         MethodAnalyzer<FieldRef, D, N, M> analyzer = (MethodAnalyzer)this.methodAnalyzers.getOrCreate(this.tabulationProblem.interproceduralCFG().getMethodOf(startPoint));
         Iterator var5 = ((Set)seed.getValue()).iterator();

         while(var5.hasNext()) {
            D val = var5.next();
            analyzer.addInitialSeed(startPoint, val);
            this.debugger.initialSeed(startPoint);
         }
      }

   }
}
