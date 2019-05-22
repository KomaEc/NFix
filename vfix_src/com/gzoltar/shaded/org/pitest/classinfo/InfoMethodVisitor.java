package com.gzoltar.shaded.org.pitest.classinfo;

import com.gzoltar.shaded.org.pitest.reloc.asm.AnnotationVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.Label;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;

class InfoMethodVisitor extends MethodVisitor {
   private final ClassInfoBuilder classInfo;

   public InfoMethodVisitor(ClassInfoBuilder classInfo, MethodVisitor writer) {
      super(327680, writer);
      this.classInfo = classInfo;
   }

   public void visitLineNumber(int line, Label start) {
      this.classInfo.registerCodeLine(line);
   }

   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
      String type = desc.substring(1, desc.length() - 1);
      this.classInfo.registerAnnotation(type);
      return super.visitAnnotation(desc, visible);
   }
}
