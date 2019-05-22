package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor;

import com.gzoltar.shaded.org.pitest.reloc.asm.Label;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;

public class LineTrackingMethodVisitor extends MethodVisitor {
   private final MutationContext context;

   public LineTrackingMethodVisitor(MutationContext context, MethodVisitor delegateMethodVisitor) {
      super(327680, delegateMethodVisitor);
      this.context = context;
   }

   public void visitLineNumber(int line, Label start) {
      this.context.registerCurrentLine(line);
      this.mv.visitLineNumber(line, start);
   }
}
