package soot.tagkit;

public abstract class AbstractAnnotationElemTypeSwitch implements IAnnotationElemTypeSwitch {
   Object result;

   public void caseAnnotationAnnotationElem(AnnotationAnnotationElem v) {
      this.defaultCase(v);
   }

   public void caseAnnotationArrayElem(AnnotationArrayElem v) {
      this.defaultCase(v);
   }

   public void caseAnnotationBooleanElem(AnnotationBooleanElem v) {
      this.defaultCase(v);
   }

   public void caseAnnotationClassElem(AnnotationClassElem v) {
      this.defaultCase(v);
   }

   public void caseAnnotationDoubleElem(AnnotationDoubleElem v) {
      this.defaultCase(v);
   }

   public void caseAnnotationEnumElem(AnnotationEnumElem v) {
      this.defaultCase(v);
   }

   public void caseAnnotationFloatElem(AnnotationFloatElem v) {
      this.defaultCase(v);
   }

   public void caseAnnotationIntElem(AnnotationIntElem v) {
      this.defaultCase(v);
   }

   public void caseAnnotationLongElem(AnnotationLongElem v) {
      this.defaultCase(v);
   }

   public void caseAnnotationStringElem(AnnotationStringElem v) {
      this.defaultCase(v);
   }

   public void defaultCase(Object object) {
   }

   public Object getResult() {
      return this.result;
   }

   public void setResult(Object result) {
      this.result = result;
   }
}
