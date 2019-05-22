package com.gzoltar.shaded.org.pitest.mutationtest.mocksupport;

import com.gzoltar.shaded.org.pitest.reloc.asm.ClassVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;

public class JavassistInputStreamInterceptorAdapater extends ClassVisitor {
   public JavassistInputStreamInterceptorAdapater(ClassVisitor arg0) {
      super(327680, arg0);
   }

   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
      return new JavassistInputStreamInterceptorMethodVisitor(this.cv.visitMethod(access, name, desc, signature, exceptions));
   }
}
