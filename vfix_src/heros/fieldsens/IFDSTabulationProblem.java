package heros.fieldsens;

import heros.InterproceduralCFG;
import heros.SolverConfiguration;
import java.util.Map;
import java.util.Set;

public interface IFDSTabulationProblem<N, FieldRef, D, M, I extends InterproceduralCFG<N, M>> extends SolverConfiguration {
   FlowFunctions<N, FieldRef, D, M> flowFunctions();

   I interproceduralCFG();

   Map<N, Set<D>> initialSeeds();

   D zeroValue();

   ZeroHandler<FieldRef> zeroHandler();
}
