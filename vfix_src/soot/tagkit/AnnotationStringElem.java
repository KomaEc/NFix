package soot.tagkit;

import soot.util.Switch;

public class AnnotationStringElem extends AnnotationElem {
   String value;

   public AnnotationStringElem(String s, char kind, String name) {
      super(kind, name);
      this.value = s;
   }

   public String toString() {
      return super.toString() + " value: " + this.value;
   }

   public String getValue() {
      return this.value;
   }

   public void apply(Switch sw) {
      ((IAnnotationElemTypeSwitch)sw).caseAnnotationStringElem(this);
   }
}
