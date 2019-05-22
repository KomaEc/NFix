package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;

public interface BranchPropagation {
   void collectBranches(Collection var1);

   Stmt branchTarget(Stmt var1);

   void collectFinally(Stmt var1, ArrayList var2);

   Collection targetContinues();

   Collection targetBreaks();

   Collection targetBranches();

   Collection escapedBranches();

   Collection branches();

   boolean targetOf(ContinueStmt var1);

   boolean targetOf(BreakStmt var1);
}
