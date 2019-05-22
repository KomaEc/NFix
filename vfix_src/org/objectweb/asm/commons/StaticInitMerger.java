package org.objectweb.asm.commons;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class StaticInitMerger extends ClassVisitor {
   private String name;
   private MethodVisitor clinit;
   private final String prefix;
   private int counter;

   public StaticInitMerger(String prefix, ClassVisitor cv) {
      this(327680, prefix, cv);
   }

   protected StaticInitMerger(int api, String prefix, ClassVisitor cv) {
      super(api, cv);
      this.prefix = prefix;
   }

   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
      this.cv.visit(version, access, name, signature, superName, interfaces);
      this.name = name;
   }

   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
      MethodVisitor mv;
      if ("<clinit>".equals(name)) {
         int a = 10;
         String n = this.prefix + this.counter++;
         mv = this.cv.visitMethod(a, n, desc, signature, exceptions);
         if (this.clinit == null) {
            this.clinit = this.cv.visitMethod(a, name, desc, (String)null, (String[])null);
         }

         this.clinit.visitMethodInsn(184, this.name, n, desc, false);
      } else {
         mv = this.cv.visitMethod(access, name, desc, signature, exceptions);
      }

      return mv;
   }

   public void visitEnd() {
      if (this.clinit != null) {
         this.clinit.visitInsn(177);
         this.clinit.visitMaxs(0, 0);
      }

      this.cv.visitEnd();
   }
}
