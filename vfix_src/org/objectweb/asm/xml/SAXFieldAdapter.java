package org.objectweb.asm.xml;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.TypePath;
import org.xml.sax.Attributes;

public final class SAXFieldAdapter extends FieldVisitor {
   SAXAdapter sa;

   public SAXFieldAdapter(SAXAdapter sa, Attributes att) {
      super(327680);
      this.sa = sa;
      sa.addStart("field", att);
   }

   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
      return new SAXAnnotationAdapter(this.sa, "annotation", visible ? 1 : -1, (String)null, desc);
   }

   public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
      return new SAXAnnotationAdapter(this.sa, "typeAnnotation", visible ? 1 : -1, (String)null, desc, typeRef, typePath);
   }

   public void visitEnd() {
      this.sa.addEnd("field");
   }
}
