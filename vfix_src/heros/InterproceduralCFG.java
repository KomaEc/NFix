package heros;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface InterproceduralCFG<N, M> {
   M getMethodOf(N var1);

   List<N> getPredsOf(N var1);

   List<N> getSuccsOf(N var1);

   Collection<M> getCalleesOfCallAt(N var1);

   Collection<N> getCallersOf(M var1);

   Set<N> getCallsFromWithin(M var1);

   Collection<N> getStartPointsOf(M var1);

   Collection<N> getReturnSitesOfCallAt(N var1);

   boolean isCallStmt(N var1);

   boolean isExitStmt(N var1);

   boolean isStartPoint(N var1);

   Set<N> allNonCallStartNodes();

   boolean isFallThroughSuccessor(N var1, N var2);

   boolean isBranchTarget(N var1, N var2);
}
