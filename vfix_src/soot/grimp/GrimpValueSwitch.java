package soot.grimp;

import soot.jimple.JimpleValueSwitch;

public interface GrimpValueSwitch extends JimpleValueSwitch {
   void caseNewInvokeExpr(NewInvokeExpr var1);
}
