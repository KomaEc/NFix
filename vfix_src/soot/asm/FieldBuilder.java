package soot.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import soot.SootField;

final class FieldBuilder extends FieldVisitor {
   private TagBuilder tb;
   private final SootField field;
   private final SootClassBuilder scb;

   FieldBuilder(SootField field, SootClassBuilder scb) {
      super(327680);
      this.field = field;
      this.scb = scb;
   }

   private TagBuilder getTagBuilder() {
      TagBuilder t = this.tb;
      if (t == null) {
         t = this.tb = new TagBuilder(this.field, this.scb);
      }

      return t;
   }

   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
      return this.getTagBuilder().visitAnnotation(desc, visible);
   }

   public void visitAttribute(Attribute attr) {
      this.getTagBuilder().visitAttribute(attr);
   }
}
