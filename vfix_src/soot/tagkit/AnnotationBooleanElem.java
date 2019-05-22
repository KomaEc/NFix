package soot.tagkit;

import soot.util.Switch;

public class AnnotationBooleanElem extends AnnotationElem {
   boolean value;

   public AnnotationBooleanElem(boolean v, char kind, String name) {
      super(kind, name);
      this.value = v;
   }

   public String toString() {
      return super.toString() + " value: " + this.value;
   }

   public boolean getValue() {
      return this.value;
   }

   public void apply(Switch sw) {
      ((IAnnotationElemTypeSwitch)sw).caseAnnotationBooleanElem(this);
   }
}
