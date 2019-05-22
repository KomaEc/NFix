package soot.shimple;

import soot.jimple.AbstractJimpleValueSwitch;

public abstract class AbstractShimpleValueSwitch extends AbstractJimpleValueSwitch implements ShimpleValueSwitch {
   public void casePhiExpr(PhiExpr e) {
      this.defaultCase(e);
   }
}
