package com.gzoltar.shaded.org.pitest.coverage.analysis;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.analysis.InstructionCounter;
import com.gzoltar.shaded.org.pitest.reloc.asm.Handle;
import com.gzoltar.shaded.org.pitest.reloc.asm.Label;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.commons.AdviceAdapter;
import java.util.Iterator;
import java.util.List;

abstract class AbstractCoverageStrategy extends AdviceAdapter {
   protected final MethodVisitor methodVisitor;
   protected final int classId;
   protected final int probeOffset;
   protected final List<Block> blocks;
   private final InstructionCounter counter;
   private final Label before = new Label();
   private final Label handler = new Label();
   protected int probeCount = 0;

   AbstractCoverageStrategy(List<Block> blocks, InstructionCounter counter, int classId, MethodVisitor writer, int access, String name, String desc, int probeOffset) {
      super(327680, writer, access, name, desc);
      this.methodVisitor = writer;
      this.classId = classId;
      this.counter = counter;
      this.blocks = blocks;
      this.probeOffset = probeOffset;
   }

   abstract void prepare();

   abstract void generateProbeReportCode();

   abstract void insertProbe();

   public void visitCode() {
      super.visitCode();
      this.prepare();
      this.mv.visitLabel(this.before);
   }

   public void visitMaxs(int maxStack, int maxLocals) {
      this.mv.visitTryCatchBlock(this.before, this.handler, this.handler, (String)null);
      this.mv.visitLabel(this.handler);
      this.generateProbeReportCode();
      this.mv.visitInsn(191);
      this.mv.visitMaxs(maxStack, this.nextLocal);
   }

   protected void onMethodExit(int opcode) {
      if (opcode != 191) {
         this.generateProbeReportCode();
      }

   }

   protected void pushConstant(int value) {
      switch(value) {
      case 0:
         this.mv.visitInsn(3);
         break;
      case 1:
         this.mv.visitInsn(4);
         break;
      case 2:
         this.mv.visitInsn(5);
         break;
      case 3:
         this.mv.visitInsn(6);
         break;
      case 4:
         this.mv.visitInsn(7);
         break;
      case 5:
         this.mv.visitInsn(8);
         break;
      default:
         if (value <= 127) {
            this.mv.visitIntInsn(16, value);
         } else if (value <= 32767) {
            this.mv.visitIntInsn(17, value);
         } else {
            this.mv.visitLdcInsn(value);
         }
      }

   }

   public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
      this.insertProbeIfAppropriate();
      super.visitFrame(type, nLocal, local, nStack, stack);
   }

   public void visitInsn(int opcode) {
      this.insertProbeIfAppropriate();
      super.visitInsn(opcode);
   }

   public void visitIntInsn(int opcode, int operand) {
      this.insertProbeIfAppropriate();
      super.visitIntInsn(opcode, operand);
   }

   public void visitVarInsn(int opcode, int var) {
      this.insertProbeIfAppropriate();
      super.visitVarInsn(opcode, var);
   }

   public void visitTypeInsn(int opcode, String type) {
      this.insertProbeIfAppropriate();
      super.visitTypeInsn(opcode, type);
   }

   public void visitFieldInsn(int opcode, String owner, String name, String desc) {
      this.insertProbeIfAppropriate();
      super.visitFieldInsn(opcode, owner, name, desc);
   }

   public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
      this.insertProbeIfAppropriate();
      super.visitMethodInsn(opcode, owner, name, desc, itf);
   }

   public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
      this.insertProbeIfAppropriate();
      super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
   }

   public void visitJumpInsn(int opcode, Label label) {
      this.insertProbeIfAppropriate();
      super.visitJumpInsn(opcode, label);
   }

   public void visitLabel(Label label) {
      super.visitLabel(label);
      this.insertProbeIfAppropriate();
   }

   public void visitLdcInsn(Object cst) {
      this.insertProbeIfAppropriate();
      super.visitLdcInsn(cst);
   }

   public void visitIincInsn(int var, int increment) {
      this.insertProbeIfAppropriate();
      super.visitIincInsn(var, increment);
   }

   public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
      this.insertProbeIfAppropriate();
      super.visitTableSwitchInsn(min, max, dflt, labels);
   }

   public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
      this.insertProbeIfAppropriate();
      super.visitLookupSwitchInsn(dflt, keys, labels);
   }

   public void visitMultiANewArrayInsn(String desc, int dims) {
      this.insertProbeIfAppropriate();
      super.visitMultiANewArrayInsn(desc, dims);
   }

   public void visitLineNumber(int line, Label start) {
      this.insertProbeIfAppropriate();
      super.visitLineNumber(line, start);
   }

   private void insertProbeIfAppropriate() {
      if (this.needsProbe(this.counter.currentInstructionCount())) {
         this.insertProbe();
         ++this.probeCount;
      }

   }

   private boolean needsProbe(int currentInstructionCount) {
      Iterator i$ = this.blocks.iterator();

      Block each;
      do {
         if (!i$.hasNext()) {
            return false;
         }

         each = (Block)i$.next();
      } while(!each.firstInstructionIs(currentInstructionCount - 1));

      return true;
   }
}
