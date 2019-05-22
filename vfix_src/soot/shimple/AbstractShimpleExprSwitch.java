package soot.shimple;

import soot.jimple.AbstractExprSwitch;

public abstract class AbstractShimpleExprSwitch extends AbstractExprSwitch implements ShimpleExprSwitch {
   public void casePhiExpr(PhiExpr v) {
      this.defaultCase(v);
   }
}
