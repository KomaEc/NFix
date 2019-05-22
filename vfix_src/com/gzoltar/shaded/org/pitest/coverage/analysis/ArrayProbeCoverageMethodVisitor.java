package com.gzoltar.shaded.org.pitest.coverage.analysis;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.analysis.InstructionCounter;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.Type;
import java.util.List;
import sun.pitest.CodeCoverageStore;

public class ArrayProbeCoverageMethodVisitor extends AbstractCoverageStrategy {
   private int probeHitArrayLocal;

   public ArrayProbeCoverageMethodVisitor(List<Block> blocks, InstructionCounter counter, int classId, MethodVisitor writer, int access, String name, String desc, int probeOffset) {
      super(blocks, counter, classId, writer, access, name, desc, probeOffset);
   }

   void prepare() {
      this.probeHitArrayLocal = this.newLocal(Type.getType("[Z"));
      this.pushConstant(this.blocks.size());
      this.mv.visitIntInsn(188, 4);
      this.mv.visitVarInsn(58, this.probeHitArrayLocal);
   }

   void generateProbeReportCode() {
      this.pushConstant(this.classId);
      this.pushConstant(this.probeOffset);
      this.mv.visitVarInsn(25, this.probeHitArrayLocal);
      this.methodVisitor.visitMethodInsn(184, CodeCoverageStore.CLASS_NAME, "visitProbes", "(II[Z)V", false);
   }

   void insertProbe() {
      this.mv.visitVarInsn(25, this.probeHitArrayLocal);
      this.pushConstant(this.probeCount);
      this.pushConstant(1);
      this.mv.visitInsn(84);
   }
}
