package heros;

import java.util.Map;
import java.util.Set;

public interface IFDSTabulationProblem<N, D, M, I extends InterproceduralCFG<N, M>> extends SolverConfiguration {
   FlowFunctions<N, D, M> flowFunctions();

   I interproceduralCFG();

   Map<N, Set<D>> initialSeeds();

   D zeroValue();
}
