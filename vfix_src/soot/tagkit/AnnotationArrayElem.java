package soot.tagkit;

import java.util.ArrayList;
import soot.util.Switch;

public class AnnotationArrayElem extends AnnotationElem {
   ArrayList<AnnotationElem> values;

   public AnnotationArrayElem(ArrayList<AnnotationElem> t, char kind, String name) {
      super(kind, name);
      this.values = t;
   }

   public String toString() {
      return super.toString() + " values: " + this.values.toString();
   }

   public ArrayList<AnnotationElem> getValues() {
      return this.values;
   }

   public int getNumValues() {
      return this.values == null ? 0 : this.values.size();
   }

   public AnnotationElem getValueAt(int i) {
      return (AnnotationElem)this.values.get(i);
   }

   public void apply(Switch sw) {
      ((IAnnotationElemTypeSwitch)sw).caseAnnotationArrayElem(this);
   }
}
