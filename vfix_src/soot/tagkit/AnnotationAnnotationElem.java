package soot.tagkit;

import soot.util.Switch;

public class AnnotationAnnotationElem extends AnnotationElem {
   AnnotationTag value;

   public AnnotationAnnotationElem(AnnotationTag t, char kind, String name) {
      super(kind, name);
      this.value = t;
   }

   public String toString() {
      return super.toString() + "value: " + this.value.toString();
   }

   public AnnotationTag getValue() {
      return this.value;
   }

   public void apply(Switch sw) {
      ((IAnnotationElemTypeSwitch)sw).caseAnnotationAnnotationElem(this);
   }
}
