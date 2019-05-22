package soot.jimple;

public abstract class AbstractConstantSwitch implements ConstantSwitch {
   Object result;

   public void caseDoubleConstant(DoubleConstant v) {
      this.defaultCase(v);
   }

   public void caseFloatConstant(FloatConstant v) {
      this.defaultCase(v);
   }

   public void caseIntConstant(IntConstant v) {
      this.defaultCase(v);
   }

   public void caseLongConstant(LongConstant v) {
      this.defaultCase(v);
   }

   public void caseNullConstant(NullConstant v) {
      this.defaultCase(v);
   }

   public void caseStringConstant(StringConstant v) {
      this.defaultCase(v);
   }

   public void caseClassConstant(ClassConstant v) {
      this.defaultCase(v);
   }

   public void caseMethodHandle(MethodHandle v) {
      this.defaultCase(v);
   }

   public void defaultCase(Object v) {
   }

   public Object getResult() {
      return this.result;
   }

   public void setResult(Object result) {
      this.result = result;
   }
}
