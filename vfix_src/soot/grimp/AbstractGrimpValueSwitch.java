package soot.grimp;

import soot.jimple.AbstractJimpleValueSwitch;

public abstract class AbstractGrimpValueSwitch extends AbstractJimpleValueSwitch implements GrimpValueSwitch {
   public void caseNewInvokeExpr(NewInvokeExpr e) {
      this.defaultCase(e);
   }
}
