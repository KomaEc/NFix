package soot.tagkit;

import soot.util.Switch;

public class AnnotationIntElem extends AnnotationElem {
   int value;

   public AnnotationIntElem(int v, char kind, String name) {
      super(kind, name);
      this.value = v;
   }

   public String toString() {
      return super.toString() + " value: " + this.value;
   }

   public int getValue() {
      return this.value;
   }

   public void apply(Switch sw) {
      ((IAnnotationElemTypeSwitch)sw).caseAnnotationIntElem(this);
   }
}
