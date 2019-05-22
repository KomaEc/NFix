package soot.tagkit;

import soot.util.Switch;

public class AnnotationClassElem extends AnnotationElem {
   String desc;

   public AnnotationClassElem(String s, char kind, String name) {
      super(kind, name);
      this.desc = s;
   }

   public String toString() {
      return super.toString() + " decription: " + this.desc;
   }

   public String getDesc() {
      return this.desc;
   }

   public void apply(Switch sw) {
      ((IAnnotationElemTypeSwitch)sw).caseAnnotationClassElem(this);
   }
}
