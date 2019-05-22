package heros;

public interface IDETabulationProblem<N, D, M, V, I extends InterproceduralCFG<N, M>> extends IFDSTabulationProblem<N, D, M, I> {
   EdgeFunctions<N, D, M, V> edgeFunctions();

   JoinLattice<V> joinLattice();

   EdgeFunction<V> allTopFunction();
}
