package soot.jimple;

import soot.Local;

public interface JimpleValueSwitch extends ConstantSwitch, ExprSwitch, RefSwitch {
   void caseLocal(Local var1);
}
