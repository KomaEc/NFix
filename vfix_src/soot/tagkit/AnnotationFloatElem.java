package soot.tagkit;

import soot.util.Switch;

public class AnnotationFloatElem extends AnnotationElem {
   float value;

   public AnnotationFloatElem(float v, char kind, String name) {
      super(kind, name);
      this.value = v;
   }

   public String toString() {
      return super.toString() + " value: " + this.value;
   }

   public float getValue() {
      return this.value;
   }

   public void apply(Switch sw) {
      ((IAnnotationElemTypeSwitch)sw).caseAnnotationFloatElem(this);
   }
}
