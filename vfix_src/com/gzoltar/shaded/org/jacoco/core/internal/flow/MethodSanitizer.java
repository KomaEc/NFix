package com.gzoltar.shaded.org.jacoco.core.internal.flow;

import com.gzoltar.shaded.org.objectweb.asm.Label;
import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;
import com.gzoltar.shaded.org.objectweb.asm.commons.JSRInlinerAdapter;

class MethodSanitizer extends JSRInlinerAdapter {
   MethodSanitizer(MethodVisitor mv, int access, String name, String desc, String signature, String[] exceptions) {
      super(327680, mv, access, name, desc, signature, exceptions);
   }

   public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
      if (start.info != null && end.info != null) {
         super.visitLocalVariable(name, desc, signature, start, end, index);
      }

   }

   public void visitLineNumber(int line, Label start) {
      if (start.info != null) {
         super.visitLineNumber(line, start);
      }

   }
}
