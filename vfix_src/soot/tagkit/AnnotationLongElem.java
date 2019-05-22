package soot.tagkit;

import soot.util.Switch;

public class AnnotationLongElem extends AnnotationElem {
   long value;

   public AnnotationLongElem(long v, char kind, String name) {
      super(kind, name);
      this.value = v;
   }

   public String toString() {
      return super.toString() + " value: " + this.value;
   }

   public long getValue() {
      return this.value;
   }

   public void apply(Switch sw) {
      ((IAnnotationElemTypeSwitch)sw).caseAnnotationLongElem(this);
   }
}
