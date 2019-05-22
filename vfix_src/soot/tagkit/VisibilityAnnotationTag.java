package soot.tagkit;

import java.util.ArrayList;
import java.util.Iterator;

public class VisibilityAnnotationTag implements Tag {
   private int visibility;
   private ArrayList<AnnotationTag> annotations = null;

   public VisibilityAnnotationTag(int vis) {
      this.visibility = vis;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer("Visibility Annotation: level: ");
      switch(this.visibility) {
      case 0:
         sb.append("RUNTIME (runtime-visible)");
         break;
      case 1:
         sb.append("CLASS (runtime-invisible)");
         break;
      case 2:
         sb.append("SOURCE");
      }

      sb.append("\n Annotations:");
      if (this.annotations != null) {
         Iterator var2 = this.annotations.iterator();

         while(var2.hasNext()) {
            AnnotationTag tag = (AnnotationTag)var2.next();
            sb.append("\n");
            sb.append(tag.toString());
         }
      }

      sb.append("\n");
      return sb.toString();
   }

   public String getName() {
      return "VisibilityAnnotationTag";
   }

   public String getInfo() {
      return "VisibilityAnnotation";
   }

   public int getVisibility() {
      return this.visibility;
   }

   public byte[] getValue() {
      throw new RuntimeException("VisibilityAnnotationTag has no value for bytecode");
   }

   public void addAnnotation(AnnotationTag a) {
      if (this.annotations == null) {
         this.annotations = new ArrayList();
      }

      this.annotations.add(a);
   }

   public ArrayList<AnnotationTag> getAnnotations() {
      return this.annotations;
   }

   public boolean hasAnnotations() {
      return this.annotations != null;
   }
}
