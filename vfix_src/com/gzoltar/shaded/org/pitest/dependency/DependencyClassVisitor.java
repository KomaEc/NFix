package com.gzoltar.shaded.org.pitest.dependency;

import com.gzoltar.shaded.org.pitest.functional.SideEffect1;
import com.gzoltar.shaded.org.pitest.reloc.asm.ClassVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;

class DependencyClassVisitor extends ClassVisitor {
   private final SideEffect1<DependencyAccess> typeReceiver;
   private String className;

   protected DependencyClassVisitor(ClassVisitor visitor, SideEffect1<DependencyAccess> typeReceiver) {
      super(327680, visitor);
      this.typeReceiver = this.filterOutJavaLangObject(typeReceiver);
   }

   private SideEffect1<DependencyAccess> filterOutJavaLangObject(final SideEffect1<DependencyAccess> child) {
      return new SideEffect1<DependencyAccess>() {
         public void apply(DependencyAccess a) {
            if (!a.getDest().getOwner().equals("java/lang/Object")) {
               child.apply(a);
            }

         }
      };
   }

   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
      this.className = name;
   }

   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
      MethodVisitor methodVisitor = this.cv.visitMethod(access, name, desc, signature, exceptions);
      DependencyAccess.Member me = new DependencyAccess.Member(this.className, name);
      return new DependencyClassVisitor.DependencyAnalysisMethodVisitor(me, methodVisitor, this.typeReceiver);
   }

   private static class DependencyAnalysisMethodVisitor extends MethodVisitor {
      private final DependencyAccess.Member member;
      private final SideEffect1<DependencyAccess> typeReceiver;

      public DependencyAnalysisMethodVisitor(DependencyAccess.Member member, MethodVisitor methodVisitor, SideEffect1<DependencyAccess> typeReceiver) {
         super(327680, methodVisitor);
         this.typeReceiver = typeReceiver;
         this.member = member;
      }

      public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
         this.typeReceiver.apply(new DependencyAccess(this.member, new DependencyAccess.Member(owner, name)));
         this.mv.visitMethodInsn(opcode, owner, name, desc, itf);
      }

      public void visitFieldInsn(int opcode, String owner, String name, String desc) {
         this.typeReceiver.apply(new DependencyAccess(this.member, new DependencyAccess.Member(owner, name)));
         this.mv.visitFieldInsn(opcode, owner, name, desc);
      }
   }
}
