package soot.jimple;

public abstract class AbstractRefSwitch implements RefSwitch {
   public void caseArrayRef(ArrayRef v) {
      this.defaultCase(v);
   }

   public void caseStaticFieldRef(StaticFieldRef v) {
      this.defaultCase(v);
   }

   public void caseInstanceFieldRef(InstanceFieldRef v) {
      this.defaultCase(v);
   }

   public void caseParameterRef(ParameterRef v) {
      this.defaultCase(v);
   }

   public void caseCaughtExceptionRef(CaughtExceptionRef v) {
      this.defaultCase(v);
   }

   public void caseThisRef(ThisRef v) {
      this.defaultCase(v);
   }

   public void defaultCase(Object obj) {
   }
}
