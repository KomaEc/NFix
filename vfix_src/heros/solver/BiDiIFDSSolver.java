package heros.solver;

import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BiDiIFDSSolver<N, D extends JoinHandlingNode<D>, M, I extends InterproceduralCFG<N, M>> extends BiDiIDESolver<N, D, M, IFDSSolver.BinaryDomain, I> {
   public BiDiIFDSSolver(IFDSTabulationProblem<N, D, M, I> forwardProblem, IFDSTabulationProblem<N, D, M, I> backwardProblem) {
      super(IFDSSolver.createIDETabulationProblem(forwardProblem), IFDSSolver.createIDETabulationProblem(backwardProblem));
   }

   public Set<D> fwIFDSResultAt(N stmt) {
      return this.extractResults(this.fwSolver.resultsAt(stmt).keySet());
   }

   public Set<D> bwIFDSResultAt(N stmt) {
      return this.extractResults(this.bwSolver.resultsAt(stmt).keySet());
   }

   private Set<D> extractResults(Set<BiDiIDESolver<N, D, M, IFDSSolver.BinaryDomain, I>.AbstractionWithSourceStmt> annotatedResults) {
      Set<D> res = new HashSet();
      Iterator var3 = annotatedResults.iterator();

      while(var3.hasNext()) {
         BiDiIDESolver<N, D, M, IFDSSolver.BinaryDomain, I>.AbstractionWithSourceStmt abstractionWithSourceStmt = (BiDiIDESolver.AbstractionWithSourceStmt)var3.next();
         res.add(abstractionWithSourceStmt.getAbstraction());
      }

      return res;
   }
}
