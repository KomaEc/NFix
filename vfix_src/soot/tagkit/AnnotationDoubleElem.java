package soot.tagkit;

import soot.util.Switch;

public class AnnotationDoubleElem extends AnnotationElem {
   double value;

   public AnnotationDoubleElem(double v, char kind, String name) {
      super(kind, name);
      this.value = v;
   }

   public String toString() {
      return super.toString() + " value: " + this.value;
   }

   public double getValue() {
      return this.value;
   }

   public void apply(Switch sw) {
      ((IAnnotationElemTypeSwitch)sw).caseAnnotationDoubleElem(this);
   }
}
