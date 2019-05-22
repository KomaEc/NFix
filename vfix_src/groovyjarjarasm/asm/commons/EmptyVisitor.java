package groovyjarjarasm.asm.commons;

import groovyjarjarasm.asm.AnnotationVisitor;
import groovyjarjarasm.asm.Attribute;
import groovyjarjarasm.asm.ClassVisitor;
import groovyjarjarasm.asm.FieldVisitor;
import groovyjarjarasm.asm.Label;
import groovyjarjarasm.asm.MethodVisitor;

public class EmptyVisitor implements ClassVisitor, FieldVisitor, MethodVisitor, AnnotationVisitor {
   public void visit(int var1, int var2, String var3, String var4, String var5, String[] var6) {
   }

   public void visitSource(String var1, String var2) {
   }

   public void visitOuterClass(String var1, String var2, String var3) {
   }

   public AnnotationVisitor visitAnnotation(String var1, boolean var2) {
      return this;
   }

   public void visitAttribute(Attribute var1) {
   }

   public void visitInnerClass(String var1, String var2, String var3, int var4) {
   }

   public FieldVisitor visitField(int var1, String var2, String var3, String var4, Object var5) {
      return this;
   }

   public MethodVisitor visitMethod(int var1, String var2, String var3, String var4, String[] var5) {
      return this;
   }

   public void visitEnd() {
   }

   public AnnotationVisitor visitAnnotationDefault() {
      return this;
   }

   public AnnotationVisitor visitParameterAnnotation(int var1, String var2, boolean var3) {
      return this;
   }

   public void visitCode() {
   }

   public void visitFrame(int var1, int var2, Object[] var3, int var4, Object[] var5) {
   }

   public void visitInsn(int var1) {
   }

   public void visitIntInsn(int var1, int var2) {
   }

   public void visitVarInsn(int var1, int var2) {
   }

   public void visitTypeInsn(int var1, String var2) {
   }

   public void visitFieldInsn(int var1, String var2, String var3, String var4) {
   }

   public void visitMethodInsn(int var1, String var2, String var3, String var4) {
   }

   public void visitJumpInsn(int var1, Label var2) {
   }

   public void visitLabel(Label var1) {
   }

   public void visitLdcInsn(Object var1) {
   }

   public void visitIincInsn(int var1, int var2) {
   }

   public void visitTableSwitchInsn(int var1, int var2, Label var3, Label[] var4) {
   }

   public void visitLookupSwitchInsn(Label var1, int[] var2, Label[] var3) {
   }

   public void visitMultiANewArrayInsn(String var1, int var2) {
   }

   public void visitTryCatchBlock(Label var1, Label var2, Label var3, String var4) {
   }

   public void visitLocalVariable(String var1, String var2, String var3, Label var4, Label var5, int var6) {
   }

   public void visitLineNumber(int var1, Label var2) {
   }

   public void visitMaxs(int var1, int var2) {
   }

   public void visit(String var1, Object var2) {
   }

   public void visitEnum(String var1, String var2, String var3) {
   }

   public AnnotationVisitor visitAnnotation(String var1, String var2) {
      return this;
   }

   public AnnotationVisitor visitArray(String var1) {
      return this;
   }
}
