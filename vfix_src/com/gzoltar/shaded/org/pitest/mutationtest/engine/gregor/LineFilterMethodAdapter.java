package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor;

import com.gzoltar.shaded.org.pitest.reloc.asm.Label;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;

class LineFilterMethodAdapter extends MethodVisitor {
   private static final String DISABLE_REASON = "AVOIDED_LINE";
   private final MutationContext context;
   private final PremutationClassInfo classInfo;

   public LineFilterMethodAdapter(MutationContext context, PremutationClassInfo classInfo, MethodVisitor delegateMethodVisitor) {
      super(327680, delegateMethodVisitor);
      this.context = context;
      this.classInfo = classInfo;
   }

   public void visitLineNumber(int line, Label start) {
      if (this.classInfo.isLineToAvoid(line)) {
         this.context.disableMutations("AVOIDED_LINE");
      } else {
         this.context.enableMutatations("AVOIDED_LINE");
      }

      this.mv.visitLineNumber(line, start);
   }
}
