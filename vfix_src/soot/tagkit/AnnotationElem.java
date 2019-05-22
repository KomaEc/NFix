package soot.tagkit;

import soot.util.Switchable;

public abstract class AnnotationElem implements Switchable {
   char kind;
   String name;

   public AnnotationElem(char k, String name) {
      this.kind = k;
      this.name = name;
   }

   public String toString() {
      return "Annotation Element: kind: " + this.kind + " name: " + this.name;
   }

   public char getKind() {
      return this.kind;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
