package soot.tagkit;

import soot.util.Switch;

public class AnnotationEnumElem extends AnnotationElem {
   String typeName;
   String constantName;

   public AnnotationEnumElem(String t, String c, char kind, String name) {
      super(kind, name);
      this.typeName = t;
      this.constantName = c;
   }

   public String toString() {
      return super.toString() + " type name: " + this.typeName + " constant name: " + this.constantName;
   }

   public String getTypeName() {
      return this.typeName;
   }

   public void setTypeName(String newValue) {
      this.typeName = newValue;
   }

   public String getConstantName() {
      return this.constantName;
   }

   public void setConstantName(String newValue) {
      this.constantName = newValue;
   }

   public void apply(Switch sw) {
      ((IAnnotationElemTypeSwitch)sw).caseAnnotationEnumElem(this);
   }
}
