package com.gzoltar.shaded.org.pitest.bytecode;

import com.gzoltar.shaded.org.pitest.reloc.asm.AnnotationVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.Attribute;
import com.gzoltar.shaded.org.pitest.reloc.asm.ClassVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.FieldVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.Label;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;

public class NullVisitor extends ClassVisitor {
   public NullVisitor() {
      super(327680);
   }

   public void visit(int arg0, int arg1, String arg2, String arg3, String arg4, String[] arg5) {
   }

   public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
      return new NullVisitor.NullAnnotationVisitor();
   }

   public void visitAttribute(Attribute arg0) {
   }

   public void visitEnd() {
   }

   public FieldVisitor visitField(int arg0, String arg1, String arg2, String arg3, Object arg4) {
      return new FieldVisitor(327680) {
         public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
            return new NullVisitor.NullAnnotationVisitor();
         }

         public void visitAttribute(Attribute arg0) {
         }

         public void visitEnd() {
         }
      };
   }

   public void visitInnerClass(String arg0, String arg1, String arg2, int arg3) {
   }

   public MethodVisitor visitMethod(int arg0, String arg1, String arg2, String arg3, String[] arg4) {
      return new NullVisitor.NullMethodVisitor();
   }

   public void visitOuterClass(String arg0, String arg1, String arg2) {
   }

   public void visitSource(String arg0, String arg1) {
   }

   public static class NullMethodVisitor extends MethodVisitor {
      NullMethodVisitor() {
         super(327680);
      }

      public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
         return new NullVisitor.NullAnnotationVisitor();
      }

      public AnnotationVisitor visitAnnotationDefault() {
         return new NullVisitor.NullAnnotationVisitor();
      }

      public void visitAttribute(Attribute arg0) {
      }

      public void visitCode() {
      }

      public void visitEnd() {
      }

      public void visitFieldInsn(int arg0, String arg1, String arg2, String arg3) {
      }

      public void visitFrame(int arg0, int arg1, Object[] arg2, int arg3, Object[] arg4) {
      }

      public void visitIincInsn(int arg0, int arg1) {
      }

      public void visitInsn(int arg0) {
      }

      public void visitIntInsn(int arg0, int arg1) {
      }

      public void visitJumpInsn(int arg0, Label arg1) {
      }

      public void visitLabel(Label arg0) {
      }

      public void visitLdcInsn(Object arg0) {
      }

      public void visitLineNumber(int arg0, Label arg1) {
      }

      public void visitLocalVariable(String arg0, String arg1, String arg2, Label arg3, Label arg4, int arg5) {
      }

      public void visitLookupSwitchInsn(Label arg0, int[] arg1, Label[] arg2) {
      }

      public void visitMaxs(int arg0, int arg1) {
      }

      public void visitMethodInsn(int arg0, String arg1, String arg2, String arg3) {
      }

      public void visitMultiANewArrayInsn(String arg0, int arg1) {
      }

      public AnnotationVisitor visitParameterAnnotation(int arg0, String arg1, boolean arg2) {
         return new NullVisitor.NullAnnotationVisitor();
      }

      public void visitTableSwitchInsn(int arg0, int arg1, Label arg2, Label... labels) {
      }

      public void visitTryCatchBlock(Label arg0, Label arg1, Label arg2, String arg3) {
      }

      public void visitTypeInsn(int arg0, String arg1) {
      }

      public void visitVarInsn(int arg0, int arg1) {
      }
   }

   public static class NullAnnotationVisitor extends AnnotationVisitor {
      NullAnnotationVisitor() {
         super(327680);
      }

      public void visit(String arg0, Object arg1) {
      }

      public AnnotationVisitor visitAnnotation(String arg0, String arg1) {
         return new NullVisitor.NullAnnotationVisitor();
      }

      public AnnotationVisitor visitArray(String arg0) {
         return new NullVisitor.NullAnnotationVisitor();
      }

      public void visitEnd() {
      }

      public void visitEnum(String arg0, String arg1, String arg2) {
      }
   }
}
