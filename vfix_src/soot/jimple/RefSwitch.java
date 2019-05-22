package soot.jimple;

import soot.util.Switch;

public interface RefSwitch extends Switch {
   void caseArrayRef(ArrayRef var1);

   void caseStaticFieldRef(StaticFieldRef var1);

   void caseInstanceFieldRef(InstanceFieldRef var1);

   void caseParameterRef(ParameterRef var1);

   void caseCaughtExceptionRef(CaughtExceptionRef var1);

   void caseThisRef(ThisRef var1);

   void defaultCase(Object var1);
}
