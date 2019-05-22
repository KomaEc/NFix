package groovyjarjarasm.asm.commons;

import groovyjarjarasm.asm.AnnotationVisitor;

public class RemappingAnnotationAdapter implements AnnotationVisitor {
   private final AnnotationVisitor av;
   private final Remapper renamer;

   public RemappingAnnotationAdapter(AnnotationVisitor var1, Remapper var2) {
      this.av = var1;
      this.renamer = var2;
   }

   public void visit(String var1, Object var2) {
      this.av.visit(var1, this.renamer.mapValue(var2));
   }

   public void visitEnum(String var1, String var2, String var3) {
      this.av.visitEnum(var1, this.renamer.mapType(var2), var3);
   }

   public AnnotationVisitor visitAnnotation(String var1, String var2) {
      AnnotationVisitor var3 = this.av.visitAnnotation(var1, this.renamer.mapType(var2));
      return var3 == null ? null : (var3 == this.av ? this : new RemappingAnnotationAdapter(var3, this.renamer));
   }

   public AnnotationVisitor visitArray(String var1) {
      AnnotationVisitor var2 = this.av.visitArray(var1);
      return var2 == null ? null : (var2 == this.av ? this : new RemappingAnnotationAdapter(var2, this.renamer));
   }

   public void visitEnd() {
      this.av.visitEnd();
   }
}
