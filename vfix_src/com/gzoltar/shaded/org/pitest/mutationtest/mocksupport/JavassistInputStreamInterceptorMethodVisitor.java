package com.gzoltar.shaded.org.pitest.mutationtest.mocksupport;

import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;

class JavassistInputStreamInterceptorMethodVisitor extends MethodVisitor {
   private static final String INTERCEPTOR_CLASS = classToName(JavassistInterceptor.class);

   public JavassistInputStreamInterceptorMethodVisitor(MethodVisitor mv) {
      super(327680, mv);
   }

   public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
      if (opcode == 185 && owner.equals("com/gzoltar/shaded/javassist/ClassPath") && name.equals("openClassfile")) {
         this.mv.visitMethodInsn(184, INTERCEPTOR_CLASS, name, "(Ljava/lang/Object;Ljava/lang/String;)Ljava/io/InputStream;", false);
      } else {
         this.mv.visitMethodInsn(opcode, owner, name, desc, itf);
      }

   }

   private static String classToName(Class<?> clazz) {
      return clazz.getName().replace(".", "/");
   }
}
