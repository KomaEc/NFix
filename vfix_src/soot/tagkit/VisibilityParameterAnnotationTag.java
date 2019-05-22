package soot.tagkit;

import java.util.ArrayList;
import java.util.Iterator;

public class VisibilityParameterAnnotationTag implements Tag {
   private int num_params;
   private int kind;
   private ArrayList<VisibilityAnnotationTag> visibilityAnnotations;

   public VisibilityParameterAnnotationTag(int num, int kind) {
      this.num_params = num;
      this.kind = kind;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer("Visibility Param Annotation: num params: " + this.num_params + " kind: " + this.kind);
      if (this.visibilityAnnotations != null) {
         Iterator var2 = this.visibilityAnnotations.iterator();

         while(var2.hasNext()) {
            VisibilityAnnotationTag tag = (VisibilityAnnotationTag)var2.next();
            sb.append("\n");
            if (tag != null) {
               sb.append(tag.toString());
            }
         }
      }

      sb.append("\n");
      return sb.toString();
   }

   public String getName() {
      return "VisibilityParameterAnnotationTag";
   }

   public String getInfo() {
      return "VisibilityParameterAnnotation";
   }

   public byte[] getValue() {
      throw new RuntimeException("VisibilityParameterAnnotationTag has no value for bytecode");
   }

   public void addVisibilityAnnotation(VisibilityAnnotationTag a) {
      if (this.visibilityAnnotations == null) {
         this.visibilityAnnotations = new ArrayList();
      }

      this.visibilityAnnotations.add(a);
   }

   public ArrayList<VisibilityAnnotationTag> getVisibilityAnnotations() {
      return this.visibilityAnnotations;
   }

   public int getKind() {
      return this.kind;
   }
}
