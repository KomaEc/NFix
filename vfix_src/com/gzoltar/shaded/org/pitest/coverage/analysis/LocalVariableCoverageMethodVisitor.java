package com.gzoltar.shaded.org.pitest.coverage.analysis;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.analysis.InstructionCounter;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.Type;
import java.util.List;
import sun.pitest.CodeCoverageStore;

class LocalVariableCoverageMethodVisitor extends AbstractCoverageStrategy {
   private int[] locals;

   LocalVariableCoverageMethodVisitor(List<Block> blocks, InstructionCounter counter, int classId, MethodVisitor writer, int access, String name, String desc, int probeOffset) {
      super(blocks, counter, classId, writer, access, name, desc, probeOffset);
   }

   void prepare() {
      this.locals = new int[this.blocks.size()];

      for(int i = 0; i != this.blocks.size(); ++i) {
         this.locals[i] = this.newLocal(Type.getType("Z"));
         this.pushConstant(0);
         this.mv.visitVarInsn(54, this.locals[i]);
      }

   }

   void insertProbe() {
      this.pushConstant(1);
      this.mv.visitVarInsn(54, this.locals[this.probeCount]);
   }

   protected void generateProbeReportCode() {
      this.pushConstant(this.classId);
      this.pushConstant(this.probeOffset);
      int[] arr$ = this.locals;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         int i = arr$[i$];
         this.mv.visitVarInsn(21, i);
      }

      this.methodVisitor.visitMethodInsn(184, CodeCoverageStore.CLASS_NAME, "visitProbes", "(II" + String.format(String.format("%%0%dd", this.blocks.size()), 0).replace("0", "Z") + ")V", false);
   }
}
