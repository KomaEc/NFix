package heros.template;

import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;

public abstract class DefaultIFDSTabulationProblem<N, D, M, I extends InterproceduralCFG<N, M>> implements IFDSTabulationProblem<N, D, M, I> {
   private final I icfg;
   private FlowFunctions<N, D, M> flowFunctions;
   private D zeroValue;

   public DefaultIFDSTabulationProblem(I icfg) {
      this.icfg = icfg;
   }

   protected abstract FlowFunctions<N, D, M> createFlowFunctionsFactory();

   protected abstract D createZeroValue();

   public final FlowFunctions<N, D, M> flowFunctions() {
      if (this.flowFunctions == null) {
         this.flowFunctions = this.createFlowFunctionsFactory();
      }

      return this.flowFunctions;
   }

   public I interproceduralCFG() {
      return this.icfg;
   }

   public final D zeroValue() {
      if (this.zeroValue == null) {
         this.zeroValue = this.createZeroValue();
      }

      return this.zeroValue;
   }

   public boolean followReturnsPastSeeds() {
      return false;
   }

   public boolean autoAddZero() {
      return true;
   }

   public int numThreads() {
      return Runtime.getRuntime().availableProcessors();
   }

   public boolean computeValues() {
      return true;
   }

   public boolean recordEdges() {
      return false;
   }
}
