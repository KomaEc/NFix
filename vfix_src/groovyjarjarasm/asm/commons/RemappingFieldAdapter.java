package groovyjarjarasm.asm.commons;

import groovyjarjarasm.asm.AnnotationVisitor;
import groovyjarjarasm.asm.Attribute;
import groovyjarjarasm.asm.FieldVisitor;

public class RemappingFieldAdapter implements FieldVisitor {
   private final FieldVisitor fv;
   private final Remapper remapper;

   public RemappingFieldAdapter(FieldVisitor var1, Remapper var2) {
      this.fv = var1;
      this.remapper = var2;
   }

   public AnnotationVisitor visitAnnotation(String var1, boolean var2) {
      AnnotationVisitor var3 = this.fv.visitAnnotation(var1, var2);
      return var3 == null ? null : new RemappingAnnotationAdapter(var3, this.remapper);
   }

   public void visitAttribute(Attribute var1) {
      this.fv.visitAttribute(var1);
   }

   public void visitEnd() {
      this.fv.visitEnd();
   }
}
