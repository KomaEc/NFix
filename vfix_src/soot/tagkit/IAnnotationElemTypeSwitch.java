package soot.tagkit;

import soot.util.Switch;

public interface IAnnotationElemTypeSwitch extends Switch {
   void caseAnnotationAnnotationElem(AnnotationAnnotationElem var1);

   void caseAnnotationArrayElem(AnnotationArrayElem var1);

   void caseAnnotationBooleanElem(AnnotationBooleanElem var1);

   void caseAnnotationClassElem(AnnotationClassElem var1);

   void caseAnnotationDoubleElem(AnnotationDoubleElem var1);

   void caseAnnotationEnumElem(AnnotationEnumElem var1);

   void caseAnnotationFloatElem(AnnotationFloatElem var1);

   void caseAnnotationIntElem(AnnotationIntElem var1);

   void caseAnnotationLongElem(AnnotationLongElem var1);

   void caseAnnotationStringElem(AnnotationStringElem var1);

   void defaultCase(Object var1);
}
