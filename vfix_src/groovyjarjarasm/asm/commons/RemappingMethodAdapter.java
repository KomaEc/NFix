package groovyjarjarasm.asm.commons;

import groovyjarjarasm.asm.AnnotationVisitor;
import groovyjarjarasm.asm.Label;
import groovyjarjarasm.asm.MethodVisitor;

public class RemappingMethodAdapter extends LocalVariablesSorter {
   protected final Remapper remapper;

   public RemappingMethodAdapter(int var1, String var2, MethodVisitor var3, Remapper var4) {
      super(var1, var2, var3);
      this.remapper = var4;
   }

   public void visitFieldInsn(int var1, String var2, String var3, String var4) {
      super.visitFieldInsn(var1, this.remapper.mapType(var2), this.remapper.mapFieldName(var2, var3, var4), this.remapper.mapDesc(var4));
   }

   public void visitMethodInsn(int var1, String var2, String var3, String var4) {
      super.visitMethodInsn(var1, this.remapper.mapType(var2), this.remapper.mapMethodName(var2, var3, var4), this.remapper.mapMethodDesc(var4));
   }

   public void visitTypeInsn(int var1, String var2) {
      super.visitTypeInsn(var1, this.remapper.mapType(var2));
   }

   public void visitLdcInsn(Object var1) {
      super.visitLdcInsn(this.remapper.mapValue(var1));
   }

   public void visitMultiANewArrayInsn(String var1, int var2) {
      super.visitMultiANewArrayInsn(this.remapper.mapDesc(var1), var2);
   }

   public void visitTryCatchBlock(Label var1, Label var2, Label var3, String var4) {
      super.visitTryCatchBlock(var1, var2, var3, var4 == null ? null : this.remapper.mapType(var4));
   }

   public void visitLocalVariable(String var1, String var2, String var3, Label var4, Label var5, int var6) {
      super.visitLocalVariable(var1, this.remapper.mapDesc(var2), this.remapper.mapSignature(var3, true), var4, var5, var6);
   }

   public AnnotationVisitor visitAnnotation(String var1, boolean var2) {
      AnnotationVisitor var3 = this.mv.visitAnnotation(var1, var2);
      return (AnnotationVisitor)(var3 == null ? var3 : new RemappingAnnotationAdapter(var3, this.remapper));
   }

   public AnnotationVisitor visitAnnotationDefault() {
      AnnotationVisitor var1 = this.mv.visitAnnotationDefault();
      return (AnnotationVisitor)(var1 == null ? var1 : new RemappingAnnotationAdapter(var1, this.remapper));
   }

   public AnnotationVisitor visitParameterAnnotation(int var1, String var2, boolean var3) {
      AnnotationVisitor var4 = this.mv.visitParameterAnnotation(var1, var2, var3);
      return (AnnotationVisitor)(var4 == null ? var4 : new RemappingAnnotationAdapter(var4, this.remapper));
   }

   public void visitFrame(int var1, int var2, Object[] var3, int var4, Object[] var5) {
      super.visitFrame(var1, var2, this.remapEntries(var2, var3), var4, this.remapEntries(var4, var5));
   }

   private Object[] remapEntries(int var1, Object[] var2) {
      for(int var3 = 0; var3 < var1; ++var3) {
         if (var2[var3] instanceof String) {
            Object[] var4 = new Object[var1];
            if (var3 > 0) {
               System.arraycopy(var2, 0, var4, 0, var3);
            }

            do {
               Object var5 = var2[var3];
               var4[var3++] = var5 instanceof String ? this.remapper.mapType((String)var5) : var5;
            } while(var3 < var1);

            return var4;
         }
      }

      return var2;
   }
}
