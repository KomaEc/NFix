package soot.shimple;

import soot.jimple.ExprSwitch;

public interface ShimpleExprSwitch extends ExprSwitch {
   void casePhiExpr(PhiExpr var1);
}
