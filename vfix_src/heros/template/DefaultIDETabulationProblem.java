package heros.template;

import heros.EdgeFunction;
import heros.EdgeFunctions;
import heros.IDETabulationProblem;
import heros.InterproceduralCFG;
import heros.JoinLattice;

public abstract class DefaultIDETabulationProblem<N, D, M, V, I extends InterproceduralCFG<N, M>> extends DefaultIFDSTabulationProblem<N, D, M, I> implements IDETabulationProblem<N, D, M, V, I> {
   private final EdgeFunction<V> allTopFunction = this.createAllTopFunction();
   private final JoinLattice<V> joinLattice = this.createJoinLattice();
   private final EdgeFunctions<N, D, M, V> edgeFunctions = this.createEdgeFunctionsFactory();

   public DefaultIDETabulationProblem(I icfg) {
      super(icfg);
   }

   protected abstract EdgeFunction<V> createAllTopFunction();

   protected abstract JoinLattice<V> createJoinLattice();

   protected abstract EdgeFunctions<N, D, M, V> createEdgeFunctionsFactory();

   public final EdgeFunction<V> allTopFunction() {
      return this.allTopFunction;
   }

   public final JoinLattice<V> joinLattice() {
      return this.joinLattice;
   }

   public final EdgeFunctions<N, D, M, V> edgeFunctions() {
      return this.edgeFunctions;
   }
}
