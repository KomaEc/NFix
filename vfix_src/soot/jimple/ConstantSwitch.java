package soot.jimple;

import soot.util.Switch;

public interface ConstantSwitch extends Switch {
   void caseDoubleConstant(DoubleConstant var1);

   void caseFloatConstant(FloatConstant var1);

   void caseIntConstant(IntConstant var1);

   void caseLongConstant(LongConstant var1);

   void caseNullConstant(NullConstant var1);

   void caseStringConstant(StringConstant var1);

   void caseClassConstant(ClassConstant var1);

   void caseMethodHandle(MethodHandle var1);

   void defaultCase(Object var1);
}
