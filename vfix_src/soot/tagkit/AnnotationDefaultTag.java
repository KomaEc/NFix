package soot.tagkit;

public class AnnotationDefaultTag implements Tag {
   private AnnotationElem defaultVal;

   public AnnotationDefaultTag(AnnotationElem def) {
      this.defaultVal = def;
   }

   public String toString() {
      return "Annotation Default: " + this.defaultVal;
   }

   public String getName() {
      return "AnnotationDefaultTag";
   }

   public String getInfo() {
      return "AnnotationDefault";
   }

   public AnnotationElem getDefaultVal() {
      return this.defaultVal;
   }

   public byte[] getValue() {
      throw new RuntimeException("AnnotationDefaultTag has no value for bytecode");
   }
}
